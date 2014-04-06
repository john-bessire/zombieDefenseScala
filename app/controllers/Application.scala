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
import models.Geolocation
import common.ErrorHandling

object Application extends Controller {
  
  
    val form = Form(
	    tuple(
	      "firstname" -> text,
	      "lastname" -> text
	    )
	)


   
  def index = Action {

      
      
	  println ("Action Index")
	  Ok(views.html.index())
  }
    
    
    
    def test = Action {implicit request =>
    	println (request)

      //Redirect(routes.Application.index).withSession("token" -> t.token, "secret" -> t.secret)
      
    	Ok
    }
    
    def bearing = Action {      
    	var(latitude:Double, longitude:Double) = models.Geolocation.calculateNewLocationFromDistanceAndBearing(37.386052, -122.083851, 1.0, 0)
  
    	println("Latitide  = " + latitude)
    	println("Longitude = " + longitude)
    	
      Ok
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
	  
	  Geolocation.getLatitudeAndLongitude("Mountain View, Ca")

      
      println("After function called to get longitide and latitude")
      Ok("Done") // TODO - do someyhing here
  }
 
  
}