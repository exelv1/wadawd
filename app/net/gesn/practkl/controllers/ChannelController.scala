package net.gesn.practkl.controllers

import net.gesn.models.{Channel, Lobby, Server}
import net.gesn.security.{GesnAuthAction, SessionHeader}
import play.api.Logger
import play.api.mvc._

class ChannelController extends Controller with SessionHeader {
  val logger: Logger = Logger(this.getClass)

  def channels = GesnAuthAction { implicit request =>
    logger.debug("Channels page")
    // TODO remove mock
    val channel = Channel.mockChannel

    val channels = List(channel, channel, channel, channel, channel)
    Ok(net.gesn.practkl.views.html.channels(channels))
  }

  def channel(uuid: String) = GesnAuthAction { implicit request =>
    val channel = Channel.mockChannel
    val lobbies = List(Lobby.mockLobby, Lobby.mockLobby, Lobby.mockLobby, Lobby.mockLobby, Lobby.mockLobby)

    Ok(net.gesn.practkl.views.html.channel(channel, lobbies))
  }

}
