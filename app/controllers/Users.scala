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
import scala.util.Random


import java.lang.String


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
	  
	  	println("Users.byId: " + id)
	  
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
	//      The field "livingStatus" has three states (human, zombie, unknown)
	// 
	def createUser = Action { implicit request =>
	    request.body.asJson.map { json =>
	        println("Users.createUser - json: " + json)
	        
	        val error = "*^*Error*@*"
	        var status:Boolean = true
	        var errorMessage = ""
	          
	        var userName    = ((json \ "userName").validate[String]).getOrElse(error)
	        var email    	= ((json \ "email").validate[String]).getOrElse(error)	        
	        var password 	= ((json \ "password").validate[String]).getOrElse(error)
	        var livingStatus	    = ((json \ "livingStatus").validate[String]).getOrElse(error)  
	       	        
	        if (userName == error) {status = false; errorMessage = "Invalid user name"}
	        if (email    == error) {status = false; errorMessage = "Invalid email address"}
	        if (password == error) {status = false; errorMessage = "Invalid password"}
	        if (livingStatus    == error) {status = false; errorMessage = "Invalid livingStatus"}
  	              
	        //  Check for valid user name
	        if (status == true) {
	    		var (statusUserName:Boolean, msgUserName:String) = checkValidUserName(userName)
	    			    		
	    		if ( statusUserName == false){
		        	status = false       	
	    			errorMessage = msgUserName   
		        }
	        }

	        // Check for valid email address
	        if (status == true) {
	        	var (statusEmail:Boolean, msgEmail:String) = checkValidEmailAddress(email)
	            if (statusEmail == false) {
	            	status = false
	            	errorMessage = msgEmail
	            }
	        }
	        
	        // Check for valid password
	        if (status == true) {
	            var (statusPassword:Boolean, msgPassword:String) = checkValidPassword(password)
	            if (statusPassword == false){
	            	status = false
	            	errorMessage = msgPassword
	            }         
	        }     
	        
	        // Check for duplicate user name
	        if (status == true) {
	        	// Check for duplicate user name
		    	var duplicate = User.doesDatabaseValueExistInTable("user_name", "users", userName)
    			if (duplicate == true) {
    				status = false
    				errorMessage = "Duplicate user name"

    			}
	        }
	        
	        // Create the user
	        if (status == true) {
    	        
		        // Create a new user and get user Id for the response
		        val userPk = User.userCreate(User(NotAssigned, null, null, null, 
		            userName, email, password, livingStatus))	 
		        
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
	            val error = common.ErrorHandling.errorJsonMessage(errorMessage)
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
			"livingStatus"  -> user.livingStatus
		)
	}
	
	// =======================================================================
	//                        checkValidUserName
	//
	//  
	//    The only symbols allowed are .(dot), -(dash) or _(underscore).
	//    Must not begin with any symbols.
	//    Must not end with any symbols.
	//    Must not include consecutive symbols. 
	//
	def checkValidUserName (userName:String): (Boolean, String) = {
	  
		var errorMessage = ""
		var status = true
		  
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

	// =======================================================================
	//                     checkValidEmailAddress
	//
	def checkValidEmailAddress(email:String): (Boolean, String) = {

		val maxLength = 254
		
		var status = true
		var errorMessage = ""
		
		// Email must contain a period but not first or last character
		if (email.indexOf(".") <= 0 ) {status = false} 
		if (email.lastIndexOf(".") == email.length()) {status = false}
		
		// Can't have two periods in a row
		if (email.indexOf("..") >= 0) {status = false} 
		
		// Must have an @
		if (email.indexOf("@") == -1) {status = false} 
	
		if (status == false) {errorMessage = "Ivalid email address"}
	  
		return (status, errorMessage)
	} // End of checkValidEmailAddress
	
	// =======================================================================
	//                      checkValidPassword
	// 
	//    Password MUST include at least 
	//        1 UPPERCASE alphabetic character, 
	//        1 LOWERCASE alphabetic character
	//        1 numeric character. 
	//    Use a combination of 
	//        uppercase and lowercase letters (Aa–Zz),
	//        numbers (0–9), 
	//        symbols ( @ # $ % ^ & * ( ) _ + | ~ - = ` { } [ ] : ; " < > ? , . /). 
	//    The symbols that are NOT allowed are 
	//        \ (back slash) or "(quotes). 
	//
	//    List of allowed symbols !@#$%^&*()_+|~-=`{}[]:;<>?,./)
	// 
	def checkValidPassword(password:String): (Boolean, String) = {
	  
		val minLength = 8
		val maxLength  = 150
		
		var status:Boolean = true
		var errorMessage = ""

		// Characters allowed in password
		var pattern = "[a-zA-Z0-9\\!\\@\\#\\$\\%\\^\\&\\&\\*\\(\\)\\_\\+\\|\\~\\-\\=\\`\\{\\}\\[\\]\\:\\;\\<\\>\\?\\,\\.\\/]".r
				
		// Find all valid characters in password
		val temp = (pattern findAllIn password).mkString("")
		
		// After all valid characters removed password and temp should be equal
		if (temp != password) {status = false}
		
		// Check for at least on uppercase character, lowercase and number
		val upper  = ("[A-Z]*".r findAllIn password).mkString("")
		val lower  = ("[a-z]*".r findAllIn password).mkString("")
		val digits = ("[0-9]*".r findAllIn password).mkString("")		
		
		if (upper.length()  == 0 ) {status = false}
		if (lower.length()  == 0 ) {status = false}
		if (digits.length() == 0) {status = false}

		// Check length of password
		if (password.length() > maxLength) {status = false}
		if (password.length() < minLength) {status = false}
		
		if (status == false) {errorMessage = "Password nust be between " + minLength + " and "  + maxLength + ". Contain one lower case letter, Conatin one upper case letter, contain one number and use can use the symbols !@#$%^&*()_+|~-=`{}[]:;<>?,./"}
	  
		return (status, errorMessage)
	} // checkValidPassword


	// =======================================================================
	//                            getRandomFirstName
	//	
	def generateRandomFirstName(mf:String): String = {
	  
		var firstName = ""
	  
		if (mf.toLowerCase() == "m") {  // Male first names
	  
			var guyFirstNames = Array("Mark", "Marcus", "Marky", " Maurice", 
			    "Jim", "James", "Jessie", "Jon", "John", "Johnn", "Johnny", "Jaun", 
			    "Bill", "Billy", "William", "Willard", 
			    "Tom", "Thomas", "Tommy", "Timmy", 
			    "Rick", "Ricky", "Ricardo")
			    
		    firstName =  guyFirstNames(Random.nextInt(guyFirstNames.length))
		    	    
		} else {  // Female first names
			var womanFirstNames = Array("Anne", "Amy", "Cindy", "Shelly", "Merry", "Marie", "michelle", 
			    "Jenifer", "Jessica", "Sharon", "Shelly", "Tina", "Tammy")
			    
			firstName =  womanFirstNames(Random.nextInt(womanFirstNames.length))	

		}
		
		firstName
	} // End of getRandomFirstName
	
		
	// =======================================================================
	//                       generateRandomUserName
	//	
	def generateRandomUserName(): String = {

		var maleFemale = Array ("m", "f")
		var mf = maleFemale(Random.nextInt(maleFemale.length))
		
		var userName =  generateRandomFirstName(mf) + Random.nextInt(1000000).toString

		return userName
	} // End of generateRandomUserName
	
	
	def generateRandomUser (longitude:Double, latitude:Double, radius:Double, livingStatus:String) {

		var userName = generateRandomUserName()
	  
		var user = Json.obj(
			"userName"   	-> userName,
			"email"      	-> (userName + "@" + userName + ".com"),
			"password" 		-> ("password" + userName),
			"livingStatus"  -> livingStatus
		)
	
	
	}
		

} // End of object Users
	

