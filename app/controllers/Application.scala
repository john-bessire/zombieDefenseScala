package controllers

import play.api._
import play.api.mvc._
import java.util.Calendar
import java.text.SimpleDateFormat
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws.WS
import models.Geolocation
import common.ErrorHandling

import play.api.libs.oauth._
import play.mvc.Results.Redirect


import twitter4j.TwitterFactory
import twitter4j.Twitter
import twitter4j.conf.ConfigurationBuilder

object Application extends Controller {
  
  
	// TODO - Most of this page is code under development that will be
    // deleted or moved elsewhere. 
  
  
    val form = Form(
	    tuple(
	      "firstname" -> text,
	      "lastname" -> text
	    )
	)

	
	//def index = Action { implicit request =>
	
	def index = Action {implicit request => 
	  	println("Headers = " + request.headers)
		println("Body =    " + request.body)
	
    //def index(token:String, secret:String) = Action { 


	      
		// println ("Action Index")
		// Ok(views.html.index())
	    Ok("Index page")  
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
    
    
    def test = Action {
    	println("Start function test")
           Users.generateRandomUser (27.123D, -127.456D, 1.0D, common.Globals.statusHuman)
           Users.generateRandomUser (27.123D, -127.456D, 1.0D, common.Globals.statusZombie)
           
           Users.generateZombieOutbreak(37.123, -127.456, 100, 1.0, 3, 50, 3.0)
           
      	println("Function test called")
      	     
    	Ok ("End of Function test")
    }
    
    def bearing = Action {      
      
  
      
      
    	var(latitude:Double, longitude:Double) = models.Geolocation.calculateNewLocationFromDistanceAndBearing(37.386052, -122.083851, 1.0, 0)
  
    	println("Latitide  = " + latitude)
    	println("Longitude = " + longitude)
    	
      Ok("Function - bearing")
    }
    
    def distance = Action {
    	val latitude1 = 37.386052
    	val longitude1 = -122.083851
      
    	var(latitude2:Double, longitude2:Double) = models.Geolocation.calculateNewLocationFromDistanceAndBearing(latitude1, longitude1, 1.0, 0)
 
    	var distance = models.Geolocation.calculateDistanceBetweenTwoPoints(latitude1, longitude1, latitude2, longitude2)
 
    	println("Distance = " + distance)
    	
    	Ok("Distance")
    }
    
  
  def submit = Action { implicit request =>
		val (fname, lname) = form.bindFromRequest.get
		Ok("Hi %s %s".format(fname, lname))
  }


    def createMap = Action {
    	println("Action - Create map")
    	Ok  // Todo add create map code
    }
  
  def getLocation = Action {
	  println("Get Location called")  
	  
	  Geolocation.getLatitudeAndLongitudeFromAddress("Mountain View, Ca")

      
      println("After function called to get longitide and latitude")
      Ok("Done") // TODO - do someyhing here
  }
 
  
  	val KEY = ConsumerKey("1HNZuoq17HmGHCkwl4flg", "uVsFvXopnTn6LrSqUNfOEqCI0Xk2eMNQDFLEXKe6I")
 
	val TWITTER = OAuth(ServiceInfo(
	    "https://api.twitter.com/oauth/request_token",
	    "https://api.twitter.com/oauth/access_token",
	    "https://api.twitter.com/oauth/authorize", KEY),
	    true)
	    
	  

def authenticate = Action { request =>
  
  /*
    request.getQueryString("oauth_verifier").map { verifier =>
      val tokenPair = sessionTokenPair(request).get
      // We got the verifier; now get the access token, store it and back to index
      TWITTER.retrieveAccessToken(tokenPair, verifier) match {
        case Right(t) => {
          // We received the authorized tokens in the OAuth object - store it before we proceed
          
          println("Twitter toke recieved" + t.token)
          println("Twitter secret received" + t.secret)
          
          Redirect(routes.Application.index).withSession("token" -> t.token, "secret" -> t.secret)
 
        }
        case Left(e) => throw e
      }
    }.getOrElse(
      TWITTER.retrieveRequestToken("http://localhost:9000/auth") match {
        case Right(t) => {
          // We received the unauthorized tokens in the OAuth object - store it before we proceed
          
          println("Retreive Twitter token  = " + t.token)
          println("Retreive Twitter secret = " + t.secret)
          
          Redirect(TWITTER.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      })
      * 
      * 
      */
  	Ok("Authenticate")
  }

  def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
    for {
      token <- request.session.get("token")
      secret <- request.session.get("secret")
      
      
      
    } yield {
      RequestToken(token, secret)
    }
  }

 
} // End of Application