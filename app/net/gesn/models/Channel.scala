package net.gesn.models

import play.api.libs.json.Json

case class Channel(uuid: String, title: String, description: String, servers: Int, flags: List[String], onlinePlayers: Option[Int], totalPlayers: Option[Int])

object Channel {
  implicit val channelFormat = Json.format[Channel]

  val mockChannel = Channel("euGathers1", "EU Gathers", "Gathers based in the EU", 1, List("Public"), Some(53), Some(204))
}
