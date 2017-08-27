package net.gesn.controllers

import akka.actor.ActorRef
import com.typesafe.config.{Config, ConfigFactory}
import net.gesn.models._
import net.gesn.security.SessionHeader
import net.gesn.actors.DatabaseActor._
import org.joda.time.DateTime
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.openid.OpenID
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class RegisterData(username: String, password: String, passwordRepeat: String, email: String)
case class LoginData(username: String, password: String)

class AccountController(dbActor: ActorRef) extends Controller with SessionHeader {

  val logger: Logger = Logger(this.getClass)
  val config: Config = ConfigFactory.load()

  private[this] val registerForm = Form(
    mapping(
      "username" -> text
        .verifying("Please enter a username", {
          !_.isEmpty
        }),
      "password" -> text
        .verifying("Please enter a password", {
          !_.isEmpty
        }),
      "passwordRepeat"  -> text
        .verifying("Please enter your password again", {
          !_.isEmpty
        }),
      "email"  -> text
        .verifying("Please enter your email", {
          !_.isEmpty
        })
    )(RegisterData.apply)(RegisterData.unapply)
  )

  private[this] val loginForm = Form(
    mapping(
      "email" -> text
        .verifying("Please enter a email", {
          !_.isEmpty
        }),
      "password" -> text
        .verifying("Please enter a password", {
          !_.isEmpty
        })
    )(LoginData.apply)(LoginData.unapply)
  )

  def loginPage = Action { implicit request =>
    Ok(net.gesn.views.html.login(loginForm))
  }

  def steamLogin = Action.async { implicit request =>
    logger.debug("A steam login attempt occurred")

    OpenID.redirectURL("http://steamcommunity.com/openid",
      net.gesn.controllers.routes.AccountController.openIDCallback.absoluteURL(),
      realm = Option(config.getString("steam.redirectUrl")))
      .map(url => {
        val url2 = url.replace("http%3A%2F%2Fsteamcommunity.com%2Fopenid", "http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select")
        val url3 = url2.replace("http%3A%2F%2Fsteamcommunity.com%2F", "http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select")
        Redirect(url3)
      })
      .recover {
      case e: Exception =>
        logger.error(s"There was an error: ${e.getMessage}", e)
        Redirect(net.gesn.controllers.routes.AccountController.loginPage())
    }
  }

  def openIDCallback = Action.async { implicit request =>
    logger.debug("Got a open id callback")

    OpenID.verifiedId.flatMap { info =>
      val steamId = info.id.replace("http://steamcommunity.com/openid/id/", "")
      val userInfo = SteamAPI.userInfo(steamId)
      userInfo.map { user =>
        // TODO don't use .get
        logger.info(s"[user logged in] name: [${user.get.personname}] SteamID: [${user.get.steamid}]")

        dbActor ! SuccessfulLogin(None, steamId, request.remoteAddress, DateTime.now)

        val userSession: UserSession = UserSession(request.remoteAddress, steamId)

        cacheConnector.setUserSession(steamId, userSession)

        val redirectTo = net.gesn.practkl.controllers.routes.DashboardController.dashboard()
        Redirect(redirectTo).withSession("uuid" -> steamId)
      }
    }.recover {
      case e: Exception =>
        logger.error(s"Error when attempting to call back1: ${e.getMessage}", e)
        Redirect(net.gesn.controllers.routes.AccountController.loginPage())
    }
  }

  def login = Action.async { implicit request =>
    logger.debug("A login attempt occurred")
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Future {
          BadRequest(net.gesn.views.html.login(formWithErrors))
        }
      },
      userData => {
        val uuid = java.util.UUID.randomUUID.toString

        logger.debug(s"Successful login on [${userData.username}]. Given uuid [$uuid]")
        Future {
          Ok("To be implemented")
        }
      }
    )
  }

  def registerPage = Action { implicit request =>
    Ok(net.gesn.views.html.register(registerForm))
  }

  def register = Action.async { implicit request =>
    logger.debug("A register attempt occurred")
    registerForm.bindFromRequest.fold(
      formWithErrors => {
        Future {
          BadRequest(net.gesn.views.html.register(formWithErrors))
        }
      },
      userData => {
        logger.debug(s"Account ${userData.username} successfully registered")
        Future {
          Ok("To be implemented")
        }
      }
    )
  }

  def logout = Action { implicit request =>
    logger.debug("Logout attempted")
    cacheConnector.clear()
    Redirect(net.gesn.controllers.routes.MainController.index()).withNewSession
  }
}