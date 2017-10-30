package basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scala.concurrent.duration._

class SpringBootOneSimulation extends Simulation {

  val rampUpTimeSecs = 20
  val testTimeSecs   = 60
  val noOfUsers      = 1000
  val minWaitMs      = 1000 milliseconds
  val maxWaitMs      = 3000 milliseconds

  val baseURL      = "http://localhost:9091"
  val baseName     = "spring-boot-one"
  val requestName  = baseName + "-request"
  val scenarioName = baseName + "-scenario"
  val URI          = "/process-blocking?minMs=500&maxMs=1000"

  val httpConf = http.baseURL(baseURL)

  val http_headers = Map(
    "Accept-Encoding" -> "gzip,deflate",
    "Content-Type" -> "text/json;charset=UTF-8",
    "Keep-Alive" -> "115")

  val scn = scenario(scenarioName)
    .during(testTimeSecs) {
      exec(
        http(requestName)
          .get(URI)
          .headers(http_headers)
          .check(status.is(200))
      )
      .pause(minWaitMs, maxWaitMs)
    }
  setUp(scn.inject(
  	nothingFor(rampUpTimeSecs), atOnceUsers(noOfUsers))
  ).protocols(httpConf) 
}