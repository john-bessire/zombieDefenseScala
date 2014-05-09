//         Under Development
//
// This is where new API calls and function will be tested before they are
// moved to the correct locations or deleted.

package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import java.util.Calendar
import java.text.SimpleDateFormat
import play.api.libs.ws.WS
import models.Geolocation
import common.ErrorHandling
import play.api.libs.oauth._
import play.mvc.Results.Redirect
import twitter4j._
import scala.collection.mutable.HashMap
import play.api.libs.json.Json


object Util {
    val config = new twitter4j.conf.ConfigurationBuilder()
	    .setOAuthConsumerKey("1HNZuoq17HmGHCkwl4flg")
	    .setOAuthConsumerSecret("uVsFvXopnTn6LrSqUNfOEqCI0Xk2eMNQDFLEXKe6I")
	    .setOAuthAccessToken("18912024-ZpKVxobPp9UtOI8LvWEYCSXldQf4PiKMvSVGBTPFw")
	    .setOAuthAccessTokenSecret("R80fxNsLXjMEBjtwDy2FfBTKMMQCswLWs9MvHPKpg")
	    .build
    
	def simpleStatusListener = new StatusListener() {
    	def onStatus(status: Status) { println(status.getText) }
		def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
		def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
		def onException(ex: Exception) { ex.printStackTrace }
		def onScrubGeo(arg0: Long, arg1: Long) {}
		def onStallWarning(warning: StallWarning) {}
	}
}

object StatusStreamer {
  def main(args: Array[String]) {
    val twitterStream = new TwitterStreamFactory(Util.config).getInstance
    twitterStream.addListener(Util.simpleStatusListener)
    twitterStream.sample
    Thread.sleep(2000)
    twitterStream.cleanUp
    twitterStream.shutdown
  }
}

object underDevelopment  extends Controller  {
  
  
	val helloForm = Form(
	    tuple(
	      "name" -> nonEmptyText,
	      "repeat" -> number(min = 1, max = 100),
	      "color" -> optional(text)
	    )
	)
    
      def temp1 = Action {  // Generate zombie outbreaks.
                  
	  println("Generate zombie outbreak")
   	
           Users.generateRandomUser (27.123D, -127.456D, 1.0D, common.Globals.statusHuman)
           Users.generateRandomUser (27.123D, -127.456D, 1.0D, common.Globals.statusZombie)
           
           Users.generateZombieOutbreak(37.123, -127.456, 100, 1.0, 3, 50, 3.0)
           
           
           
     	
      	Ok("temp 1")
    }



      
      
    def temp2 = Action {  // Simple google map
      
      
      
    
    	//Ok(views.html.zombieOutbreakMap())
        Ok("temp 2")
    } 
    
    def temp3 = Action {
      
    	val today = Calendar.getInstance().getTime()
    	val sdf = new SimpleDateFormat("EEEEE MMMMM m, k:m:s")

    	var zombies = models.User.userGetUsersWithinRadius(common.Globals.statusZombie, 37.123, -127.456,1000.0)
    	
    	println(zombies)
      
      
    	var temp1:String = "Will this finally work"
    	var temp2:String = "It works: " + sdf.format(today)

    	val colors = Map("red" -> "#FF0000", "azure" -> "#F0FFFF")

    	var tempJson = Json.obj(
    			"name" -> "bob",
    			"age" -> 42
    	)
    	
    	Ok(views.html.tempGoogleMapGeolocation.render(temp1, temp2, zombies ))

     	//Ok(temp)
 
    }
       
    def auth = Action {implicit request => 
  	  	println("Headers = " + request.headers)
		println("Body =    " + request.body)
      
    	Ok("Function auth")
    }
    
    
    def twitter4j = Action {
 
    	println("Twitter4j")
      
    	Ok("Twitter4j library")
    }
       
    
    def twitter = Action { implicit request => 
	  	println("Headers = " + request.headers)
		println("Body =    " + request.body)

	    Ok("Redirect form Twitter")      
      
    }
    
}