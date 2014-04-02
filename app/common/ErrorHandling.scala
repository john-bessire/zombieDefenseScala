
package common

import play.api.libs.json.Json._
import play.api.libs.json._

object ErrorHandling {
  
	// =======================================================================
	//                   errorJsonMessage
	//
	//    Convert error message string into Json
	//	
	def errorJsonMessage (message:String): JsObject = {
		val error = Json.obj (
			"error" -> message
		)
	  
		return error
	} // End of errorJsonMessage
  
  
}