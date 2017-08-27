package net.gesn.models

import org.joda.time.DateTime

case class Player(steamid: String,
                  communityvisibilitystate: Int,
                  profilestate: Int,
                  personname: String,
                  profileurl: String,
                  avatar: String,
                  avatarmedium: String,
                  avatarfull: String,
                  personastate: Int)

case class DbPlayer(steamId: String, name: String, firstLogin: DateTime, lastLogin: DateTime)

object DbPlayer {
  def convert(player: Player): DbPlayer = {
    DbPlayer(player.steamid, player.personname, DateTime.now, DateTime.now)
  }
}

object Player {
  val mockPlayer = Player("friend1", 0, 0 , "friend1", "http://steamcommunity.com/", "friend1", "friend1", "friend1", 0)
  val mockPlayer2 = Player("friend2", 0, 0 , "friend2", "http://steamcommunity.com/", "friend1", "friend1", "friend1", 0)
  val mockUser = Player("player1", 0, 0 , "player1", "http://steamcommunity.com/", "friend1", "friend1", "friend1", 0)
}
