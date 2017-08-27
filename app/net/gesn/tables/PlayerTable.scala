package net.gesn.tables

import net.gesn.models.DbPlayer
import org.joda.time.DateTime

import scala.slick.driver.MySQLDriver.simple._

class PlayerTable(tag: Tag) extends Table[DbPlayer](tag, "players") {

  import DateTimeConverter._

  def uuid = column[String]("steam_id", O.PrimaryKey, O.NotNull)
  def name = column[String]("name", O.NotNull)
  def first_login = column[DateTime]("first_login", O.NotNull)
  def last_login = column[DateTime]("last_login", O.NotNull)

  def * = (uuid, name, first_login, last_login) <> ((DbPlayer.apply _).tupled, DbPlayer.unapply _)
}