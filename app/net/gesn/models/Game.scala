package net.gesn.models

import org.joda.time.DateTime

case class Game(uuid: Option[Long],
                created: DateTime,
                lobby: String,
                game_type: String,
                server: Long,
                isComplete: Boolean) {

}
