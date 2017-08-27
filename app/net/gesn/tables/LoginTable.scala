package net.gesn.tables

import java.sql.Timestamp

import net.gesn.actors.DatabaseActor.{FailureLogin, SuccessfulLogin}
import org.joda.time.DateTime

import scala.slick.driver.MySQLDriver.simple._

object DateTimeConverter {
  /** Implicit mapping of Joda DateTime to java.sql.Timestamp */
  implicit def dateTime =
    MappedColumnType.base[DateTime, Timestamp](
      dt => new Timestamp(dt.getMillis),
      ts => new DateTime(ts.getTime)
    )
}

trait LoginTable { this: Table[_] =>
  import DateTimeConverter._

  def uuid = column[Long]("uuid", O.PrimaryKey, O.NotNull, O.AutoInc)
  def steamId = column[String]("steam_id", O.NotNull)
  def ip = column[String]("ip", O.NotNull)
  def loginTime = column[DateTime]("login_time", O.NotNull)
  def successful = column[Boolean]("successful", O.NotNull)

}

class SuccessfulLoginTable(tag: Tag) extends Table[SuccessfulLogin](tag, "logins") with LoginTable {

  def * = (uuid.?, steamId, ip, loginTime, successful) <> ((SuccessfulLogin.apply _).tupled, SuccessfulLogin.unapply _)
}

class FailureLoginTable(tag: Tag) extends Table[FailureLogin](tag, "logins") with LoginTable {

  def * = (uuid.?, steamId, ip, loginTime, successful) <> ((FailureLogin.apply _).tupled, FailureLogin.unapply _)
  
}

