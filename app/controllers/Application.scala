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

import play.mvc.Results.Redirect


object Application extends Controller {
  
  
	// TODO - Most of this page is code under development that will be
    // deleted or moved elsewhere. Project currently consists of API calls
    // and web page displays have not been created yet.
  
    val form = Form(
	    tuple(
	      "firstname" -> text,
	      "lastname" -> text
	    )
	)

	
	//def index = Action { implicit request =>
	
	def index = Action {
      
	    Ok(views.html.index.render)  
    }
    
     
    def bearing = Action {      
      
    	var(latitude:Double, longitude:Double) = models.Geolocation.calculateNewLocationFromDistanceAndBearing(37.386052, -122.083851, 1.0, 0)
  
    	println("Latitide  = " + latitude)
    	println("Longitude = " + longitude)
    	
      Ok("Bearing and direction start lat/lon = ")
    }
    
    def distance = Action {
    	val latitude1 = 37.386052
    	val longitude1 = -122.083851
      
    	var(latitude2:Double, longitude2:Double) = models.Geolocation.calculateNewLocationFromDistanceAndBearing(latitude1, longitude1, 1.0, 0)
 
    	var distance = models.Geolocation.calculateDistanceBetweenTwoPoints(latitude1, longitude1, latitude2, longitude2)
 
    	println("Distance = " + distance)
    	
    	Ok("Distance = %f".format(distance))
    
    }
  
  
  
	def getLocation = Action {
		println("Get Location called")  
	  	
		//Geolocation.getLatitudeAndLongitudeFromAddress("Mountain View, Ca")
		//Geolocation.getLatitudeAndLongitudeFromAddress("When in New York city we stopped at Central Park")
		//Geolocation.getLatitudeAndLongitudeFromAddress("We went hiking at yosemite")
		//Geolocation.getLatitudeAndLongitudeFromAddress("Lunch will be at 600 Montgomery St, San Francisco, CA tomorrow")
		
		
		
		
		Geolocation.getLatitudeAndLongitudeFromAddress("On my visit to New York City, New York we were surrounded zombies")
	  
		println("After function called to get longitide and latitude")
		Ok("Done") // TODO - do someyhing here
	}
 
  
  
} // End of object Application



  

