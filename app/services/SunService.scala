package services

import models.SunInfo
import play.api.libs.ws.WSClient

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class SunService(wsClient: WSClient) {
  def getSunInfo(lon: Double,  lat: Double): Future[SunInfo] = {
    val responseF =  wsClient.url("http://api.sunrise-sunset.org/json?" + "lat=-33.8830&lng=151.2167&formatted=0").get()
    responseF.map { res =>
      val json = res.json
      val sunriseTimeStr = (json \ "results" \ "sunrise").as[String]
      val sunsetTimeStr = (json \ "results" \ "sunset").as[String]
      val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
      val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("Australia/Sydney"))
      SunInfo(sunriseTime.format(formatter), sunsetTime.format(formatter))
    }
  }
}
