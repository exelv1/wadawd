package net.gesn.tables

import net.gesn.models.{ Server, ServerType, GameType }
//import scala.slick.driver.PostgresDriver._
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.driver.PostgresDriver.MappedColumnType

class ServerTable(tag: Tag) extends Table[Server](tag, "user") {

  implicit val serverTypeColumnType = MappedColumnType.base[ServerType, String](
    { b => b.toString },    // map ServerType to String
    { i => i.asInstanceOf[ServerType] } // map String to ServerType
  )
  implicit val gameTypeColumnType = MappedColumnType.base[GameType, String](
    { b => b.toString },    // map GameType to String
    { i => i.asInstanceOf[GameType] } // map String to GameType
  )

  def uuid = column[Long]("unique_id", O.PrimaryKey, O.NotNull)
  def joinPassword = column[String]("join_password", O.NotNull)
  def rootPassword = column[String]("root_password", O.NotNull)
  def serverName = column[String]("server_name", O.NotNull)
  def ip = column[String]("ip", O.NotNull)
  def port = column[String]("port", O.NotNull)
  def location = column[String]("location", O.NotNull)
  def serverType = column[ServerType]("server_type", O.NotNull)
  def gameType = column[GameType]("game_type", O.NotNull)


  /* TODO do we need these in db?
  def playerSlots = column[Int]("player_slots", O.NotNull)

  def spectatorSlots = column[Int]("spectator_slots", O.NotNull)
  */


  def  * = (uuid, joinPassword.?, rootPassword.?, ip, port, serverName, location, serverType, gameType) <> ((Server.apply _).tupled, Server.unapply _)
}

