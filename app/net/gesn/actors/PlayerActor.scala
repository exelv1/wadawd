package net.gesn.actors

import akka.actor.{Actor, Props}
import net.gesn.actors.DatabaseActor.SuccessfulLogin
import net.gesn.dao.PlayerRepository
import net.gesn.models.{DbPlayer, SteamAPI}
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global

object PlayerActor {
  def props(playerRepository: PlayerRepository): Props =
    Props(classOf[PlayerActor], playerRepository)
}

class PlayerActor(playerRepository: PlayerRepository) extends Actor {

  val logger: Logger = Logger(this.getClass)

  override def receive = {
    case m: SuccessfulLogin =>
      SteamAPI.userInfo(m.steamId).map({ player =>
        playerRepository.insertPlayer(DbPlayer.convert(player.get)) // TODO don't use .get
      }).recover {
        case e: Exception =>
          logger.error(s"An error has occurred while getting player from steam API or while inserting player in db: ${e.getMessage}", e)
      }

    case _ =>
      logger.debug("User repo message received")
  }
}
