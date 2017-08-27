package net.gesn.controllers

import net.gesn.security.SessionHeader
import play.api.mvc._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class TeamController() extends Controller with SessionHeader {

  def createTeam() = Action.async { implicit request =>
    ???
  }

  def listTeams() = Action.async { implicit request =>
    ???
  }

  def getTeam() = Action.async { implicit request =>
    ???
  }

}
