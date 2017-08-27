package net.gesn.controllers

import play.api.mvc._
import play.api.libs.json.Json

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import net.gesn.security._
import net.gesn.services.GatherService

class GatherController(gatherService: GatherService) extends Controller with SessionHeader {

  def serversPage() = Action.async { implicit request =>
    for {
      gathers <- gatherService.getServers()
    } yield {
      Ok(net.gesn.views.html.gathers(gathers))
    }
  }

  def getServers() = Action.async { implicit request =>
    for {
      servers <- gatherService.getServers()
    } yield {
      Ok(Json.toJson(servers))
    }
  }

}
