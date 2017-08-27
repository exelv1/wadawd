package net.gesn.dao

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.typesafe.config.{Config, ConfigFactory}
import net.gesn.actors.DatabaseActor.{FailureLogin, SuccessfulLogin}
import net.gesn.models.Login
import net.gesn.tables.{FailureLoginTable, SuccessfulLoginTable, LoginTable}
import play.api.Logger

import scala.slick.driver.MySQLDriver.simple._

class LoginRepository {

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

  lazy val failureLogins = TableQuery[FailureLoginTable]
  lazy val successfulLogins = TableQuery[SuccessfulLoginTable]

  def insertLogin(login: Login): Int = {
    db.withSession { implicit session =>
      login match {
        case x: SuccessfulLogin =>
          successfulLogins += login.asInstanceOf[SuccessfulLogin]
        case x: FailureLogin =>
          failureLogins += login.asInstanceOf[FailureLogin]
      }
    }
  }

}
