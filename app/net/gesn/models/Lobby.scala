package net.gesn.models

case class Lobby(uuid: String, title: String, channel: String, gameType: GameType)

object Lobby {

  val mockLobby = Lobby("uuid", "Lobby 1", Channel.mockChannel.uuid, GameType.GATHER)

}

case class LobbyPlayer(uuid: Option[Long], lobby: String, steamId: String, ready: Boolean)

object LobbyPlayer {

  val mockLobbyPlayers = LobbyPlayer(None, Lobby.mockLobby.uuid, Player.mockPlayer.steamid, true)

}
