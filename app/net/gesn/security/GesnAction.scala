package net.gesn.security

import net.gesn.utils.CacheConnector
import play.api.Logger
import play.api.mvc.Results._
import play.api.mvc._
import scala.concurrent._

trait GesnAuthAction extends ActionBuilder[Request] {
  val logger: Logger = Logger(this.getClass)

  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    try {
      implicit val session: Session = request.session
      // Authentication condition not met - redirect to login page
      if (SecurityChecker.isLoggedIn) {
        SecurityChecker.sessionNotChanged(request.remoteAddress)
        block(request)
        // Proceed with action as normal
      } else {
        logger.warn("Session lost")
        request.headers.get("Referer") match {
          case Some(x) =>
            logger.info(s"Referer [$x] contains [${request.path}]")
            if (x contains request.path) {
              Future.successful(Redirect(net.gesn.practkl.controllers.routes.DashboardController.dashboard()).
                flashing("type" -> "danger", "message" -> "You need to be logged in to access that page"))
            } else {
              Future.successful(Redirect(x).
                flashing("type" -> "danger", "message" -> "You need to be logged in to access that page"))
            }
          case _ =>
            Future.successful(Redirect(net.gesn.practkl.controllers.routes.DashboardController.dashboard()).
              flashing("type" -> "danger", "message" -> "You need to be logged in to access that page"))
        }
      }
    } catch {
      case e: Exception =>
        logger.error(s"Error caught: ${e.getMessage}", e)
        Future.successful(Redirect(net.gesn.controllers.routes.MainController.index()).withNewSession.
          flashing("type" -> "danger", "message" -> "An general error has occurred"))
    }
  }
}

trait GesnNonAuthAction extends ActionBuilder[Request] {
  val logger: Logger = Logger(this.getClass)

  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    try {
      // Authentication condition not met - redirect to login page
      if (request.session.isEmpty)
        block(request)
      // Proceed with action as normal
      else
        Future.successful(Redirect(net.gesn.controllers.routes.MainController.index()).
          flashing("type" -> "danger", "message" -> "You can't access that page while logged in"))
    } catch {
        case e: Exception =>
          logger.error(s"Error caught: ${e.getMessage}", e)
          Future.successful(Redirect(net.gesn.controllers.routes.MainController.index()).withNewSession.
            flashing("type" -> "danger", "message" -> "An general error has occurred"))
    }
  }
}

object GesnAuthAction extends GesnAuthAction

object GesnNonAuthAction extends GesnNonAuthAction

object SecurityChecker {
  val cacheConnector = new CacheConnector

  def isLoggedIn()(implicit session: Session): Boolean = {
    session.isEmpty match {
      case false =>
        cacheConnector.getUserSession().isDefined
      case _ =>
        false
    }
  }
  def sessionNotChanged(ipAddress: String)(implicit session: Session): Boolean = {
    val ip = cacheConnector.getUserSession().get.ipAddress
    ipAddress equals ip
  }
}
