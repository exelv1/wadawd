package net.gesn.play

import akka.actor.ActorSystem
import com.typesafe.config.{ ConfigFactory, Config }
import net.gesn.actors.DatabaseActor
import net.gesn.dao.{LoginRepository, PlayerRepository}

import play.api.{ Logger, Application, GlobalSettings }
import net.gesn.controllers._
import net.gesn.services.{LobbyService, ChannelService, GatherService}
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Global extends GlobalSettings {
  val logger: Logger = Logger(this.getClass)

  val config: Config = ConfigFactory.load()

  //Services
  val gatherService: GatherService = new GatherService()
  val channelService: ChannelService = new ChannelService()
  val lobbyService: LobbyService = new LobbyService()

  //DAO
  val playerRepository: PlayerRepository = new PlayerRepository
  val loginRepository: LoginRepository = new LoginRepository

  //Actors
  val actorSystem: ActorSystem = ActorSystem("gesnActors")
  val dbActor = actorSystem.actorOf(DatabaseActor.props(playerRepository, loginRepository), "dbActor")

  //Controllers
  lazy val accountController: AccountController = new AccountController(dbActor)
  lazy val mainController: MainController = new MainController()
  lazy val gatherController: GatherController = new GatherController(gatherService)
  lazy val channelController: ChannelController = new ChannelController(channelService)
  lazy val lobbyController: LobbyController = new LobbyController(gatherService, lobbyService)

  override def onStart(app: Application) {
    logger.info("Application has started")
  }

  override def onStop(app: Application) {
    logger.info("Application shutdown...")
  }

  override def onHandlerNotFound(request: RequestHeader) = {
    logger.info(s"[404] Event occurred to uri: [${request.uri}] from ip: [${request.remoteAddress}]")
    Future {
      Redirect(
        net.gesn.controllers.routes.MainController.index()
      ).withNewSession.flashing("type" -> "danger", "message" -> s"404: Page not found [${request.uri}]")
    }
  }

  override def onError(request: RequestHeader, ex: Throwable) = {
    logger.error(s"[500] Internal server error occurred: ${ex.getMessage}", ex)
    Future {
      Redirect(
        net.gesn.controllers.routes.MainController.index()
      ).withNewSession.flashing("type" -> "danger", "message" -> s"500: Internal server error")
    }
  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    logger.info(s"Bad request occurred to [${request.toString}] with error: [$error]")

    Future {
      Redirect(
        net.gesn.controllers.routes.MainController.index()
      ).withNewSession.flashing("type" -> "danger", "message" -> s"Bad request: $error")
    }
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    controllerClass match {
      case x if x == classOf[AccountController]      => accountController.asInstanceOf[A]
      case x if x == classOf[MainController]         => mainController.asInstanceOf[A]
      case x if x == classOf[GatherController]       => gatherController.asInstanceOf[A]
      case x if x == classOf[ChannelController]      => channelController.asInstanceOf[A]
      case x if x == classOf[LobbyController]        => lobbyController.asInstanceOf[A]
      case _                                         => super.getControllerInstance(controllerClass)
    }
  }
}