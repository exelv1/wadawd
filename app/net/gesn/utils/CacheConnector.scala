package net.gesn.utils

import net.gesn.models.{Player, UserSession}
import org.joda.time.DateTime
import play.api.Logger
import play.api.Play.current
import play.api.cache.Cache
import play.api.mvc.{Request, Session}

import scala.concurrent.duration._


class CacheConnector {
  val logger: Logger = Logger(this.getClass)
  val expiration: Duration = 60 minutes

  val UUID = "uuid"
  val USER_SESSION_PATH = ".userSession"
  val ACTIVE_PATH = ".active"

  // TODO consider using database for login tracking and cache is volatile

  def getSessionId()(implicit session: Session): String = {
    session.get(UUID).get
  }

  def refreshActivity()(implicit session: Session) {
    setActive(DateTime.now)
    getUserSession() match {
      case Some(userSession) =>
        setUserSession(userSession)
      case _ =>
        // TODO throw session lost exception
    }
  }

  def setActive(time: DateTime)(implicit session: Session) {
    Cache.set(getSessionId + ACTIVE_PATH, time, expiration)
  }

  def getLastActive()(implicit session: Session): Option[DateTime] = {
    Cache.getAs[DateTime](getSessionId + ACTIVE_PATH)
  }

  def setUserSession(userSession: UserSession)(implicit session: Session) {
    setUserSession(getSessionId, userSession)
  }

  def setUserSession(uuid: String, userSession: UserSession) {
    logger.debug(s"Setting [$uuid] to user [${userSession.playerId}]")
    Cache.set(uuid.concat(USER_SESSION_PATH), userSession, expiration)
  }

  def getUserSession()(implicit session: Session): Option[UserSession] = {
    // TODO consider get or else, but would have to connect to db and check same ip to stop session hijack
    val ret = Cache.getAs[UserSession](getSessionId + USER_SESSION_PATH)
    logger.debug(s"UID [$getSessionId] returning [$ret]")
    ret
  }

  def clear()(implicit session: Session) = {
    Cache.remove(getSessionId)
  }
}
