package net.gesn.controllers

import play.api.Logger
import play.api.mvc._

class MainController extends Controller {
  val logger: Logger = Logger(this.getClass)

  def index = Action { implicit request =>
    // on load do some checks for cookies to enable auto redir passed welcome screen
    Ok(net.gesn.views.html.index())
  }
}