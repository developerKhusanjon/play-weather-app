package controllers

import models.SunInfo

import javax.inject._
import play.api._
import play.api.libs.ws.WSClient
import play.api.mvc._

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, ws: WSClient) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action async  { implicit request: Request[AnyContent] =>
    val responseF = ws.url("http://api.sunrise-sunset.org/json?" + "lat=-33.8830&lng=151.2167&formatted=0").get()
    val weatherResponseF = ws.url("https://api.open-meteo.com/v1/"
      + s"forecast?latitude=52.52&longitude=13.41&current=temperature_2m,wind_speed_10m&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m").get()

    for {
      response <- responseF
      weather <- weatherResponseF
    } yield {
      val weatherJson = weather.json
      val temperature = (weatherJson \ "elevation").as[Double]

      val json = response.json
      val sunriseTimeStr = (json \ "results" \ "sunrise").as[String]
      val sunsetTimeStr = (json \ "results" \ "sunset").as[String]
      val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
      val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").
        withZone(ZoneId.of("Australia/Sydney"))

      val sunInfo = SunInfo(sunriseTime.format(formatter), sunsetTime.format(formatter))
      Ok(views.html.index(sunInfo, temperature))
    }
  }
}
