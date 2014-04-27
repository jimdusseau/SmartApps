/**
 *  Smartfan
 *
 *  Author: Jim Dusseau
 *  Date: 2014-04-07
 *
 *  Thanks to Kris Linquist and his original Keep It Cool script
 */

 preferences {
  section("Choose a temperature sensor... ") {
    input "sensor", "capability.temperatureMeasurement", title: "Sensor"
  }

  section("Choose your fans...") {
    input "switches", "capability.switch", multiple: true
  }

  section("What is your zip code?") {
    input "zipcode", "text", title: "Zipcode?"
  }

  section("Turn fan off when it's colder than ") {
    input "lowTemp", "number", title: "Degrees F"
  }
}

def installed() {
  initialize()
}

def updated() {
  unschedule()
  initialize()
}

def initialize() {
  schedule("*/10 * * * * ?", scheduleCheck)
}

def scheduleCheck() {
  def data = getWeatherFeature( "conditions", zipcode )
  log.debug "Outside temp = $data.current_observation.temp_f  and internal temp = $sensor.currentTemperature"

  def internalTemp = sensor.currentTemperature

  def colderOutside = def colderOutside = internalTemp > data.current_observation.temp_finternalTemp
  log.debug "colder outside: $colderOutside Internal temp: $internalTemp low temp: $lowTemp"

  if(colderOutside && internalTemp > lowTemp) {
    log.debug "Turning fans on"
    //sendPush "Turning fans on"
    switches.on()
  }
  else {
    log.debug "Turning fans off"
    //sendPush "Turning fans off"
    switches.off()
  }
}
