package net.gesn.actors

import akka.actor.{Props, Actor}
import net.gesn.dao.{LoginRepository, PlayerRepository}
import net.gesn.models.{Login, Player}
import org.joda.time.DateTime
import play.api.Logger

object DatabaseActor {
  case class SuccessfulLogin(override val uuid: Option[Long],
                             override val steamId: String,
                             override val ip: String,
                             override val loginTime: DateTime,
                             override val successful: Boolean = true) extends Login
  case class FailureLogin(override val uuid: Option[Long],
                          override val steamId: String,
                          override val ip: String,
                          override val loginTime: DateTime,
                          override val successful: Boolean = false) extends Login

  def props(playerRepository: PlayerRepository, loginRepository: LoginRepository): Props =
    Props(classOf[DatabaseActor], playerRepository, loginRepository)
}

class DatabaseActor(playerRepository: PlayerRepository, loginRepository: LoginRepository) extends Actor {

  import DatabaseActor._

  val logger: Logger = Logger(this.getClass)

  val playerActor = context.actorOf(PlayerActor.props(playerRepository), "playerRepo")

  override def receive = {
    case m: SuccessfulLogin =>
      loginRepository.insertLogin(m)
      if(!playerRepository.doesPlayerExist(m.steamId)) {
        playerActor ! m
      }

    case m: FailureLogin =>
      loginRepository.insertLogin(m)

    case _ =>
      logger.error("Unknown message received")
  }
}
