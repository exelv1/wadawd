package net.gesn.practkl.controllers

import net.gesn.security.{GesnAuthAction, SessionHeader}
import play.api.Logger
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DashboardController extends Controller with SessionHeader {
  val logger: Logger = Logger(this.getClass)

  def dashboard = Action.async { implicit request =>
    Future {
      Ok(net.gesn.practkl.views.html.dashboard())
    }.recover({
      case e: Exception =>
        logger.error(s"An error occurred while viewing dashboard: ${e.getMessage}", e)
        Redirect(net.gesn.controllers.routes.MainController.index()).withNewSession
          .flashing("status" -> "danger", "message" -> "Unable to view dashboard")
    })
  }

}
