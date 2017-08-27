package net.gesn.services

import net.gesn.models.Channel

import net.gesn.security.SessionHeader

import play.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class ChannelService() extends Controller with SessionHeader {

  def getChannel(uuid: String): Future[Channel] = {
    Future {
      Channel.mockChannel
    }
  }

  def getChannels(): Future[List[Channel]] = {
    Future {
      List(Channel.mockChannel, Channel.mockChannel, Channel.mockChannel, Channel.mockChannel, Channel.mockChannel, Channel.mockChannel)
    }
  }
}
