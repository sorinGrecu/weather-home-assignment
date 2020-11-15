# Home assignment: Weather Application #

#### What this is ####
* An application, made in Java with Spring Boot, that basically acts as a caching proxy for a weather data supplier, in our case [World Weather Online (WWO)](https://www.worldweatheronline.com/developer/ "Fitbit is cool!")  
* When one of the application's endpoints is called, it will check the cache for the data, if not it will fetch it from WWO and cache it for a given amount of time (configurable, more on that bellow)  
* It is deployed in Heroku and can be tested [here](https://home-assignment-weather-api.herokuapp.com/swagger-ui/ "Godspeed!")
#### Design details ####

* It uses `Spring Boot`, `Gradle` and Google Guava's `LoadingCache` 
* In order to avoid the overhead of parsing the huge JSON response that WWO returns, it uses [JsonPath](https://github.com/json-path/JsonPath "Fitbit is actually awesome!") to only parse certain paths that we are interested in
* The weather data we are interested in is then cached for a configurable amount of time, taking into account the date, as the staleness of the cache might be affected by day-change (example, cache is refreshed at 23:50, for the forecast we return Monday-Tuesday-Wednesday, but after the day-change we should return Tuesday-Wednesday-Thursday) 
* It also takes into consideration some timezone details, for example when in Bucharest the time is 23:00, in Hong Kong the time is 04:00, so the forecast dates are different from ours)   

#### How to use ####
It is deployed in Heroku and can be tested [here](https://home-assignment-weather-api.herokuapp.com/swagger-ui/ "Godspeed!")  
Be aware that Heroku puts applications to sleep if unused, so the initial loading time might be longer because of [this](https://devcenter.heroku.com/articles/dynos#dyno-sleeping "Application might be sleepy")
#### Endpoints ####
* `/current?city=Bucharest`  
* `/forecast?city=Bucharest`  
  
Endpoints can also be seen by visiting `/swagger-ui/` 

#### Features ####
* Forecast is shown for 3 days by default _(configurable)_
* Caches for 1 hour by default _(configurable)_
* Humidity is shown in decimal format (`[0,1]`), but we can also switch to (`[0,100]`) _(configurable)_
* Call durations are logged in order to find bottlenecks
* Mappings are defined in the Spring properties _(configurable)_, if WWO changes its schema (without versioning), we can change our mappings on the fly
* Swagger
 
#### Test coverage #####
* `100%` coverage

#### Further ideas ####
* Redis cache implementation
* Caffeine cache implementation and async cache reloading through WebFlux
* Gradle Docker script


#### Author ####
[Sorin Grecu](https://drive.google.com/file/d/1A4HK-LJh1FAjBgZAftD0tNIdL4HBpzIZ/view?usp=sharing "Godspeed!")




