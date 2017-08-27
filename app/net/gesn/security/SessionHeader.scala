package net.gesn.security

import net.gesn.models.{Header, Player, SteamAPI}
import net.gesn.utils.CacheConnector
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

trait SessionHeader {
  val cacheConnector = new CacheConnector

  implicit def header[A](implicit request: Request[A]): Header = {
    implicit val session: Session = request.session
    val mockFriend = Player.mockPlayer//User("user01010", None, "Friend1", "Friend", "One", None)
    val playerId = cacheConnector.getUserSession().get.playerId // TODO don't use .get
    val user = Await.result(SteamAPI.userInfo(playerId), 5 seconds)
    val mockFriends = List(mockFriend, mockFriend, mockFriend, mockFriend)
    val mockUser = Player.mockUser

    Header(user, mockFriends)
  }
}
