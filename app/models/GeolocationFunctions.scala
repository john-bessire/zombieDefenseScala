package models

import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._
import java.net.URLEncoder
import play.api.libs.ws.WS
import scala.concurrent.ExecutionContext.Implicits.global


//            Useful web sites for design and debug
//
// Itouch Map (Click to get longitude and latitude
//     http://itouchmap.com/latlong.html
//
// Calculate distance, bearing and more between Latitude/Longitude points
//     http://www.movable-type.co.uk/scripts/latlong.html
//
// Aviation Formulary V1.46
//     http://williams.best.vwh.net/avform.htm#Dist


object Geolocation {
  
  
  	// =================================================================================
	//	                     calculateNewLocationFromDistanceAndBearing
	//
	//    Calculate a new position form the distance and bearing from the
    //        latitude and longitude passed to this function.
    // 
    //    Formula came Aviation Formulary V1.46
    //        from http://williams.best.vwh.net/avform.htm#Dist
	//
	def calculateNewLocationFromDistanceAndBearing(latitude:Double, longitude:Double, distanceKm:Double, 
	    bearing:Double): (Double, Double) = {
	  	  
		var radius = 6371.0  // Radius of the earth 6371.0
		
		var lat1 = latitude.toRadians
		var lon1 = longitude.toRadians
				
		var lat2 = Math.asin( Math.sin(lat1)*Math.cos(distanceKm/radius) + 
          Math.cos(lat1)*Math.sin(distanceKm/radius)*Math.cos(bearing) );
		var lon2 = lon1 + Math.atan2(Math.sin(bearing)*Math.sin(distanceKm/radius)*Math.cos(lat1), 
                 Math.cos(distanceKm/radius)-Math.sin(lat1)*Math.sin(lat2));
		
		return (lat2.toDegrees, lon2.toDegrees)
	
	} // calculateNewLocationFromDistanceAndBearing
	
	
	// =================================================================================
	//                                getLatitudeAndLongitude
	//
	def getLatitudeAndLongitude(address: String): Option[(Double, Double)] = {
	    implicit val timeout = Timeout(50000 milliseconds)
	    
	    // Encoded the address in order to remove the spaces from the address (spaces will be replaced by '+')
	    //@purpose There should be no spaces in the parameter values for a GET request
	    val addressEncoded = URLEncoder.encode(address, "UTF-8");
	    val jsonContainingLatitudeAndLongitude = WS.url("http://maps.googleapis.com/maps/api/geocode/json?address=" + addressEncoded + "&sensor=true").get()
	   
	    val future = jsonContainingLatitudeAndLongitude map {
	      response => (response.json \\ "location")
	    }
	
	    // Wait until the future completes (Specified the timeout above)
	
	    val result = Await.result(future, timeout.duration).asInstanceOf[List[play.api.libs.json.JsObject]]
	    
	    //Fetch the values for Latitude & Longitude from the result of future
	    val latitude = (result(0) \\ "lat")(0).toString.toDouble
	    val longitude = (result(0) \\ "lng")(0).toString.toDouble
	  
	    println("Latidue   = " + latitude)
	    println("longitude = " + longitude)
	    
	    Option(latitude, longitude)
	} // getLatitudeAndLongitude
 
  
} // End of object Geolocation