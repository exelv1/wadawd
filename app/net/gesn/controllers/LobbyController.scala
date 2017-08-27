package net.gesn.controllers

import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.json.Json

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import net.gesn.security._
import net.gesn.services.{GatherService, LobbyService}
import net.gesn.models.Lobby
import net.gesn.views._

case class LobbyRequest(uuid: String)

class LobbyController(gatherService: GatherService, lobbyService: LobbyService) extends Controller with SessionHeader {

  val logger: Logger = Logger(this.getClass)

  def lobby(uuid: String) = Action.async { implicit request =>
    for {
      l <- lobbyService.getLobby(uuid)
      lp <- lobbyService.getLobbyPlayers(uuid)
    } yield {
      Ok(html.lobby(l, lp))
    }
  }

  def join(uuid: String) = GesnAuthAction.async { implicit request =>
    logger.debug(s"Joined lobby [$uuid]")
    for {
      l <- lobbyService.getLobby(uuid)
      lp <- lobbyService.getLobbyPlayers(uuid)
    } yield {
      Ok(html.lobby(l, lp))
    }
  }

  def setReady(uuid: String) = Action.async { implicit request =>
    // TODO ???
    logger.debug(s"Player [???] setting ready on [$uuid]")

    for {
      b <- lobbyService.setReady(uuid, header.user.get)
    } yield {
      logger.info(s"Player [???] set ready on [$uuid]")
      Redirect(net.gesn.controllers.routes.LobbyController.lobby(uuid))
    }
  }

  def setUnReady(uuid: String) = Action.async { implicit request =>
    // TODO ???
    logger.debug(s"Player [???] setting unready on [$uuid]")

    for {
      b <- lobbyService.setUnReady(uuid, header.user.get)
    } yield {
      logger.info(s"Player [???] set unready on [$uuid]")
      Redirect(net.gesn.controllers.routes.LobbyController.lobby(uuid))
    }
  }

}
