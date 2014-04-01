package controllers

import models._
import views._
import anorm._
import play.api.db._
import play.api.mvc._
import play.api._
import play.api.libs.json.Json._
import play.api.libs.json._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Json
import play.api.mvc.Controller


import play.api.Play.current
import anorm.SqlParser._


object Users extends Controller {
 

	// =======================================================================
    //               allUsers
    //
    //    Retrieve all rows from the users table but could be very slow if the
    //        the table has a lot of users.
    // 
	def listAllUsers = Action { implicit request =>
	   println("Get all users")
	  
	    val users = User.userFindAll
        val usersJson = Json.obj("users"	-> {users.map(user => convertUserToJson(user))})

	    Json.toJson(usersJson)  	
	    
	    Ok(usersJson)
	}
  	
	// =======================================================================
	//                   byId
    // 
	//    Retrieve one user by User Id
	//
	def byId(id: Long) = Action { implicit request =>
	    User.userById(id) match { 
	        case Some(user) => {
	             val userJson = Json.obj( "user" -> convertUserToJson(user) )
	             
	             Ok(userJson)
	        }
	        case None => Ok(Json.obj("status" -> "None"))
	    }
	}
	

	// =======================================================================
    //                    createUser
	//
	//    Create a new user 
	// 
	def createUser = Action { implicit request =>
	    request.body.asJson.map { json =>
	        println("Users.createUser - json: " + json)
	        
	        var status:Boolean = true
	        var errorMessage = ""

	        // TODO - add error checking for JsError message
	        var userName 	= (json \ "userName").validate[String]       
	        var email    	= (json \ "email").validate[String]	        
	        var password 	= (json \ "password").validate[String]	 // Move password to login
	        var human	    = (json \ "human").validate[String] // Assume new users are still living          
        

 //   		println("User Name = " + userName)
//	        println("Email     = " + email)
//	        println("Password  = " + password)
		        
        
	        //  Check for valid user name
	        if (status == true) {
	    		var (statusUserName:Boolean, msgUserName:String) = checkValidUserName((json \ "userName").toString().replace(""""""", ""))
	    			    		
	    		if ( statusUserName == false){
		        	status = false       	
	    			errorMessage = msgUserName   
		        }
	        }
	        
	        
	        // Check for duplicate user name
	        if (status == true) {
	        	// Check for duplicate user name
		    	var duplicate = User.doesDatabaseValueExistInTable("user_name", "users", (json \ "userName").toString().replace(""""""", ""))
    			if (duplicate == true) {
    				status = false
    				errorMessage = "Duplicate user name"

    			}
	        }
	        
	        // Create the user
	        if (status == true) {
    	        
		        // Create a new user and get user Id for the response
		        val userPk = User.userCreate(User(NotAssigned, null, null, null, 
		            userName.get, email.get, password.get, human.get))	 
		        
		        // Get the user by Id
		        val getNewUser = User.userById(userPk.get)
		       
		        // Make sure the user from Id has exists and has data
		        getNewUser match {
		            case Some(getNewUser) => {
		                val jsonResp = Json.obj( "user" -> convertUserToJson(getNewUser))
			            Ok(jsonResp)
		            }
		            case None => BadRequest("User not found")
		        }

	        } else {
	            val error = errorJsonMessage(errorMessage)
		        Ok(error)	
	        }
	        
		}.getOrElse {
			BadRequest("Expecting Json data")
		}
			
		
	}  // End of createUser
	
		
	def convertUserToJson(user: User): JsObject = {
	    
	    Json.obj(
			"id"		 	-> user.id.get,
			"userName"   	-> user.userName,
			"email"      	-> user.email,
			"password" 		-> user.password,
			"human"         -> user.human
		)
	}

	
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
	
	
	
	// =======================================================================
	//                        checkValidUserName
	//
	//     Checks for valid characters and the length of the user name
	//
	def checkValidUserName (userName:String): (Boolean, String) = {
	  
		var errorMessage = ""
		var status = true
		  
		var tempJson = Json.obj()
		
		// Characters allowed in userName
		val pattern = "[a-zA-Z0-9]".r
		
		// Find all valid characters in userName
		val temp = (pattern findAllIn userName).mkString("")
		
		
		if (temp.trim().length() != userName.trim().length()) { 
			status = false

		} 
				
		// Check length of userName
		if (userName.length() > 35) {status = false}
		if (userName.length() < 2) {status = false}
		
		if (status == false) {
			errorMessage = "User name must be between 2 to 35 characters with number and alpahbet chacters"
		  
		}
		
		return (status, errorMessage)
	}  // End of checkValidUserName

	


} // End of object Users
	

