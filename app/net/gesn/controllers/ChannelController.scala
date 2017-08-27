package net.gesn.controllers

import net.gesn.security.{GesnAuthAction, SessionHeader}
import net.gesn.services.ChannelService
import net.gesn.models.Channel
import net.gesn.views._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class ChannelController(channelService: ChannelService) extends Controller with SessionHeader {
  def channelPage(uuid: String) = GesnAuthAction.async { implicit request =>
    for {
      channel <- channelService.getChannel(uuid)
    } yield {
      Ok(html.channel(channel))
    }
  }

  def channelsPage() = Action.async { implicit request =>
    for {
      channels <- channelService.getChannels()
    } yield {
      Ok(html.channels(channels))
    }
  }

  def getChannels() = Action.async { implicit request =>
    for {
      channels <- channelService.getChannels()
    } yield {
      Ok(Json.toJson(channels))
    }
  }

  def createChannelPage() = Action.async { implicit request =>
    Future {
      Ok(html.createChannel(channelForm))
    }
  }

  def createChannel() = Action.async { implicit request =>
    channelForm.bindFromRequest.fold(
    formWithErrors => {
      Future {
        BadRequest("Error")
      }
    }, { channelData =>
      Future {
        Ok("Created")
      }
    }
    )
  }

  def updateChannelPage(uuid: String) = Action.async { implicit request =>
    for {
      c <- Future{Channel.mockChannel}
    } yield {
      val filledForm = channelForm.fill(c)
      Ok(html.createChannel(filledForm))
    }
  }

  def updateChannel(uuid: String) = Action.async { implicit request =>
    channelForm.bindFromRequest.fold(
    formWithErrors => {
      Future {
        BadRequest("Error")
      }
    }, { channelData =>
      Future {
        Ok("Updated")
      }
    }
    )
  }

  /* forms */
  private[this] val channelForm = Form(
    mapping(
      "uuid" -> text,
      "title" -> text,
      "description" -> text,
      "servers" -> number,
      "flags" -> list(text),
      "onlinePlayers" -> optional(number),
      "totalPlayers" -> optional(number)
    )(Channel.apply)(Channel.unapply)
  )
}
