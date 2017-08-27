package net.gesn.services

import net.gesn.models.{LobbyPlayer, Player, Lobby}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class LobbyService {

  def getLobby(uuid: String): Future[Lobby] = {
    Future{
      Lobby.mockLobby
    }
  }

    def getLobbyPlayers(uuid: String): Future[List[LobbyPlayer]] = {
    Future {
      List(LobbyPlayer.mockLobbyPlayers)
    }
  }

  def setReady(uuid: String, player: Player): Future[Boolean] = {
    Future{
      true
    }
  }

  def setUnReady(uuid: String, player: Player): Future[Boolean] = {
    Future{
      true
    }
  }
}
