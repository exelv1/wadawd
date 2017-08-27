package net.gesn.dao

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.typesafe.config.{Config, ConfigFactory}
import net.gesn.models.Server
import net.gesn.tables.ServerTable
import play.api.Logger

import scala.slick.driver.MySQLDriver.simple._

class ServerRepository {

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

  lazy val servers = TableQuery[ServerTable]

  def findServerById(serverId: Long) = servers.filter(_.uuid === serverId)

  def getServerById(serverId: Long): Option[Server] = {
    val query = findServerById(serverId)

    db.withSession { implicit session =>
      val rows = query.list
      rows.headOption
    }
  }

  def findAvailableServers(activeServerIds: List[Long]) = servers.filterNot(_.uuid inSet activeServerIds)

  def getGetAvailableServers(activeServerIds: List[Long]): List[Server] = {
    val query = findAvailableServers(activeServerIds)

    db.withSession { implicit session =>
      query.list
    }
  }

  //def getAvailableServersForLobby()

  /*def insertServer(login: Login): Int = {
    db.withSession { implicit session =>
      login match {
        case x: SuccessfulLogin =>
          successfulLogins += login.asInstanceOf[SuccessfulLogin]
        case x: FailureLogin =>
          failureLogins += login.asInstanceOf[FailureLogin]
      }
    }
  }*/

}
