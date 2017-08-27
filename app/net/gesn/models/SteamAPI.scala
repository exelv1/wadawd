package net.gesn.models

import com.typesafe.config.{ConfigFactory, Config}
import play.Play
import play.api.Logger
import play.api.http.Status
import play.api.libs.json.{JsValue, JsResultException, JsArray}
import play.api.libs.ws.{WSResponse, WSRequestHolder}
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

object SteamAPI {

  val config: Config = ConfigFactory.load()

  val STEAM_API_KEY = config.getString("steam.apiKey")
  val logger = Logger(this.getClass)

  def userInfo(steamId: String): Future[Option[Player]] = {
    val holder : WSRequestHolder = play.api.libs.ws.WS.url("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/")
      .withQueryString(("key" -> STEAM_API_KEY), ("steamids" -> steamId))

    holder.get().map { response =>
      if (response.status == Status.OK) {
        val user: JsArray = (response.json \ "response" \ "players").as[JsArray]

        // TODO find a way of getting optional response
        Some(
          Player(
            steamId,
            (user(0) \ "communityvisibilitystate").as[Int],
            (user(0) \ "profilestate").as[Int],
            (user(0) \ "personaname").as[String],
            (user(0) \ "profileurl").as[String],
            (user(0) \ "avatar").as[String],
            (user(0) \ "avatarmedium").as[String],
            (user(0) \ "avatarfull").as[String],
            (user(0) \ "personastate").as[Int]
          )
        )
      } else {
        logger.error(s"Api error getting userInfo, status code ${response.status}")
        None
      }
    }

  }

}