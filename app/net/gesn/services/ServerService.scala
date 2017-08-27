package net.gesn.services

import net.gesn.dao.{GameRepository, ServerRepository}
import net.gesn.models.Server

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ServerService(serverRepository: ServerRepository, gameRepository: GameRepository) {
  def getAvailableServers: Future[List[Server]] = {
    Future {
      val servers = gameRepository.getActiveGames.map(_.server)
      serverRepository.getGetAvailableServers(servers)
    }
  }

  def getAvailableServersForLobby(lobbyId: String): Future[List[Server]] = {
    Future {
      val servers = gameRepository.getActiveGames.filter(_.lobby == lobbyId).map(_.server)
      serverRepository.getGetAvailableServers(servers)
    }
  }
}
