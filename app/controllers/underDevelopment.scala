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
import play.api.libs.iteratee.Enumerator


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
	     
           //Users.generateZombieOutbreak(36.165449, -115.143156, 100, 1.0, 3, 50, 3.0)

      	Ok("temp1")
    }



      
      
    def temp2 = Action {  // Simple google map
      
      
      
    
    	//Ok(views.html.zombieOutbreakMap())
        Ok("temp 2")
    } 
    
    def temp3 = Action {
      
    	val today = Calendar.getInstance().getTime()
    	val sdf = new SimpleDateFormat("EEEEE MMMMM m, k:m:s")

    	var zombies = models.User.userGetUsersWithinRadius(common.Globals.statusZombie, 36.165449, -115.1431566,1000.0)
    	
    	println(zombies)
      
      
    	var temp1:String = "Will this finally work"
    	var temp2:String = "It works: " + sdf.format(today)

    	val colors = Map("red" -> "#FF0000", "azure" -> "#F0FFFF")

    	
    	Ok(views.html.tempGoogleMapGeolocation.render(temp1, temp2, zombies ))

    }
    
    def oauth = {
      
		controllers.Twitter.authenticate
    }
       
    def auth = Action {implicit request => 
  	  	println("Headers = " + request.headers)
		println("Body =    " + request.body)
      
    	Ok("Function auth")
    }
    
    
 
    
    
} // End of object Under Development



object Twitter extends Controller {
 
	    //.setOAuthConsumerKey("1HNZuoq17HmGHCkwl4flg")
	    //.setOAuthConsumerSecret("uVsFvXopnTn6LrSqUNfOEqCI0Xk2eMNQDFLEXKe6I")
	    //.setOAuthAccessToken("18912024-ZpKVxobPp9UtOI8LvWEYCSXldQf4PiKMvSVGBTPFw")
	    //.setOAuthAccessTokenSecret("R80fxNsLXjMEBjtwDy2FfBTKMMQCswLWs9MvHPKpg")

  val KEY = ConsumerKey("1HNZuoq17HmGHCkwl4flg", "uVsFvXopnTn6LrSqUNfOEqCI0Xk2eMNQDFLEXKe6I")

  val TWITTER = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", KEY),
    false)

  def authenticate = Action { request =>
    request.queryString.get("oauth_verifier").flatMap(_.headOption).map { verifier =>
      val tokenPair = sessionTokenPair(request).get
      // We got the verifier; now get the access token, store it and back to index
      TWITTER.retrieveAccessToken(tokenPair, verifier) match {
        case Right(t) => {
          // We received the authorized tokens in the OAuth object - store it before we proceed
          Redirect(routes.Application.index).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      }
    }.getOrElse(
      TWITTER.retrieveRequestToken("http://localhost:9000/auth") match {
        case Right(t) => {
          // We received the unauthorized tokens in the OAuth object - store it before we proceed
          Redirect(TWITTER.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      })
  }

  def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
    for {
      token <- request.session.get("token")
      secret <- request.session.get("secret")
    } yield {
      RequestToken(token, secret)
    }
  }
} // End of object Twitter




