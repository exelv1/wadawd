package net.gesn.dao

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.typesafe.config.{Config, ConfigFactory}
import net.gesn.models.{Game, DbPlayer}
import net.gesn.tables.{GameTable, PlayerTable}
import play.api.Logger

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.TableQuery

class GameRepository {

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

  lazy val games = TableQuery[GameTable]

  def insertGame(game: Game): Int = {
    db.withSession { implicit session =>
      games += game
    }
  }

  def findGameById(gameId: Long) = games.filter(_.uuid === gameId)

  def getGameById(gameId: Long): Option[Game] = {
    val query = findGameById(gameId)

    db.withSession { implicit session =>
      val rows = query.list
      rows.headOption
    }
  }

  def findActiveGames = games.filter(_.isComplete)

  def getActiveGames: List[Game] = {
    val query = findActiveGames

    db.withSession { implicit session =>
      query.list
    }
  }

}
