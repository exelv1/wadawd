package net.gesn.services

import scala.concurrent._

import net.gesn.models.Server
import scala.concurrent.ExecutionContext.Implicits.global

class GatherService {


  def getServers(): Future[List[Server]] = {
    Future {
      List(Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer, Server.mockServer)
    }
  }
}
