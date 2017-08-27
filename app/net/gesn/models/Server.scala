package net.gesn.models

import play.api.libs.json._

import scala.slick.ast.MappedScalaType
import scala.slick.driver.PostgresDriver.MappedColumnType

case class Server(uuid: Long,
                  joinPassword: Option[String],
                  rootPassword: Option[String],
                  serverName: String,
                  ip: String,
                  port: String,
                  location: String,
                  serverType: ServerType,
                  gameType: GameType)
case class ServerType(uuid: String, description: String)
case class GameType(uuid: String, description: String)

object ServerType {
  implicit val serverTypeFormat = Json.format[ServerType]

  val PUBLIC = ServerType("public", "")
  val PREMIUM = ServerType("premium", "")
  val INVITE = ServerType("invite", "")
}

object GameType {
  implicit val gameTypeFormat = Json.format[GameType]

  val GATHER = GameType("gather", "")
  //val OFFICIAL = GameType("official", "")
}

object Server {
  implicit val serverFormat = Json.format[Server]

  val mockServer = Server(999L, Some("password1"), None, "GESN.net Gather Server 1", "ip", "port", "Europe", ServerType.PUBLIC, GameType.GATHER)
}

