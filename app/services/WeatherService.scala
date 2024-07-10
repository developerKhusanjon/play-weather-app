package services

import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class WeatherService(wsClient: WSClient) {
  def getTemperature(lat: Double, lon: Double): Future[Double] = {
    val responseF = wsClient.url("https://api.open-meteo.com/v1/"
      + s"forecast?latitude=52.52&longitude=13.41&current=temperature_2m,wind_speed_10m&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m").get()
    responseF.map{ res =>
      val json = res.json
      val temp = (json \ "main" \ "temp").as[Double]
      temp
    }
  }
}
