package net.gesn.dao

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.typesafe.config.{Config, ConfigFactory}
import net.gesn.models.DbPlayer
import net.gesn.tables.PlayerTable
import play.api.Logger

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.TableQuery

class PlayerRepository {

  val logger: Logger = Logger(this.getClass)
  val config: Config = ConfigFactory.load()

  val db = {
    val dbConf = config.getConfig("dbSettings")

    val ds = new ComboPooledDataSource
    ds.setDriverClass(dbConf.getString("driver"))
    ds.setJdbcUrl(dbConf.getString("url"))
    ds.setUser(dbConf.getString("username"))
    ds.setPassword(dbConf.getString("password"))
    Database.forDataSource(ds)
  }

  lazy val players = TableQuery[PlayerTable]

  def insertPlayer(player: DbPlayer): Int = {
    db.withSession { implicit session =>
      players += player
    }
  }

  def findPlayerById(steamId: String) = players.filter(_.uuid === steamId)

  def getPlayerById(steamId: String): Option[DbPlayer] = {
    val query = findPlayerById(steamId)

    db.withSession { implicit session =>
      val rows = query.list
      rows.headOption
    }
  }

  def doesPlayerExist(steamId: String): Boolean = {
    val query = findPlayerById(steamId)

    db.withSession { implicit session =>
      val rows = query.list
      rows.nonEmpty
    }
  }

}
