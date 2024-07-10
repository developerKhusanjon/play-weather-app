
import com.softwaremill.macwire.wire
import com.typesafe.config.Config
import controllers.AssetsComponents
import play.Environment
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.DefaultControllerComponents
import play.api.routing.Router
import play.api.routing.Router.Routes
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import play.filters.HttpFiltersComponents
import play.inject.ApplicationLifecycle

class AppApplicationLoader extends ApplicationLoader {

  override def load(context: ApplicationLoader.Context): Application = ???
}

class AppComponents(context: ApplicationLoader.Context) extends BuiltInComponentsFromContext(context)
  with AhcWSComponents with AssetsComponents with HttpFiltersComponents {

  override lazy val controllerComponents: DefaultControllerComponents = wire[DefaultControllerComponents]

  lazy val prefix: String = "/"

  lazy val router: Router = wire[Routes]

  lazy val applicationController: Application = wire[Application]
}
