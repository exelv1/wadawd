package net.gesn.tables

import net.gesn.models.Game
import org.joda.time.DateTime

import scala.slick.driver.MySQLDriver.simple._

class GameTable(tag: Tag) extends Table[Game](tag, "games") {

  import DateTimeConverter._

  def uuid = column[Option[Long]]("uuid", O.PrimaryKey, O.NotNull, O.AutoInc)
  def created = column[DateTime]("created", O.NotNull)
  def lobby = column[String]("lobby", O.NotNull)
  def gameType = column[String]("game_type", O.NotNull)
  def server = column[Long]("server", O.NotNull)
  def isComplete = column[Boolean]("is_complete", O.NotNull)

  def * = (uuid, created, lobby, gameType, server, isComplete) <> ((Game.apply _).tupled, Game.unapply _)
}