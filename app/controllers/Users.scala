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
	        
	        // TODO check for valid data here
	        val userName 	= (json \ "userName").validate[String]	        
	        val email    	= (json \ "email").validate[String]	        
	        val password 	= (json \ "password").validate[String]	 
	        val human	    = (json \ "human").validate[String]
      
	        // Create a new user and get user Id for the response
	        val userPk = User.userCreate(User(NotAssigned, null, null, null, 
	            userName.get, email.get, password.get, human.get))	 
	        // TODO - add handler for duplicate user name
	        
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
		}.getOrElse {
			BadRequest("Expecting Json data")
		}
	}
	
		
	def convertUserToJson(user: User): JsObject = {
	    
	    Json.obj(
			"id"		 	-> user.id.get,
			"userName"   	-> user.userName,
			"email"      	-> user.email,
			"password" 		-> user.password,
			"human"         -> user.human
		)
	}


} // End of object Users
	

