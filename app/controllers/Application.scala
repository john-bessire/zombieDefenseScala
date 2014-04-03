package controllers

import play.api._
import play.api.mvc._
import java.util.Calendar
import java.text.SimpleDateFormat
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws.WS
import models.Geolocation

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
	  
	  Geolocation.fetchLatitudeAndLongitude("Mountain View, Ca")

      
      println("After function called to get longitide and latitude")
      Ok("Done") // TODO - do someyhing here
  }
 
  
  def test = TODO
}