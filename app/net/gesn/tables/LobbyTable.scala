package net.gesn.tables

import net.gesn.models.{LobbyPlayer, GameType, Lobby}

import scala.slick.driver.MySQLDriver.MappedColumnType
import scala.slick.driver.MySQLDriver.simple._

class LobbyTable(tag: Tag) extends Table[Lobby](tag, "lobby") {

  implicit val gameTypeColumnType = MappedColumnType.base[GameType, String](
    { b => b.toString },    // map GameType to String
    { i => i.asInstanceOf[GameType] } // map String to GameType
  )

  def uuid = column[String]("uuid", O.PrimaryKey, O.NotNull)
  def title = column[String]("title", O.NotNull)
  def channel = column[String]("channel", O.NotNull)
  def gameType = column[GameType]("game_type", O.NotNull)
  /* TODO not sure if needed
    def max_ready = column[String]("ip", O.NotNull)
  */

  def  * = (uuid, title, channel, gameType) <> ((Lobby.apply _).tupled, Lobby.unapply _)

}

class LobbyPlayerTable(tag: Tag) extends Table[LobbyPlayer](tag, "lobby_players") {

  def uuid = column[Long]("uuid", O.PrimaryKey, O.NotNull, O.AutoInc)
  def lobby = column[String]("lobby", O.NotNull)
  def steamId = column[String]("steam_id", O.NotNull)
  def ready = column[Boolean]("ready", O.NotNull)

  def * = (uuid.?, lobby, steamId, ready) <> ((LobbyPlayer.apply _).tupled, LobbyPlayer.unapply _)

}

