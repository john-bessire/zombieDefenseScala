package models

import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._



case class User(
    id: 	      Pk[Long] = NotAssigned,
    created:      Date,
    lastActive:   Option[Date],
    lastLogin:	  Option[Date],
    userName:     String,
    email: 	      String,
    password:     String,
    livingStatus: String,      // livingStatus, Zombie, injured and in unknown state
    latitude:     Double,
    longitude:    Double
)

object User {
  

	val userTableLayout = {
		get[Pk[Long]]("users.id") ~
		get[Date]("users.created") ~
		get[Option[Date]]("users.last_active") ~
		get[Option[Date]]("users.last_login") ~
	    get[String]("users.user_name") ~
	    get[String]("users.email") ~
	    get[String]("users.password") ~
	    get[String]("users.livingStatus") ~
		get[Double]("users.latitude") ~
	    get[Double]("users.longitude")  map {
			case id ~ created ~ lastActive ~ lastLogin ~ userName ~ email ~ password ~ livingStatus ~ latitude ~ longitude => 
			  	User(id, created, lastActive, lastLogin, userName, email, password, livingStatus, latitude, longitude)
	    }
	}
	
	
  
	// =======================================================================
	//                    userFindAll
	//
	//    List the entire contents of the table users.  This is not recommended if
	//        the table has lots of users since the listing will take a long time
	//
	def userFindAll: List[User] = {
	  
		println("Start findALL")
		println("+++++ User = " + User+ " +++++")
	    DB.withConnection { implicit connection =>
	      		SQL("""select * from "users" """	      	    
	      	).as(User.userTableLayout *)
	    }
	}
	
	
	// =======================================================================
	//                userById
	//
	//    Retrieve on user from the table users by User Id
    //
	def userById(id: Long): Option[User] = {
		DB.withConnection { implicit connection =>
	      		SQL("""select * from "users" where id = {id}"""
      	    ).on(
	  			'id -> id
			).as(User.userTableLayout.singleOpt)
	    }
	}
 	
	// =======================================================================
	//              userCreate
    //	
	//    Create a user object in the table users
	//
	def userCreate(user: User): Pk[Long] = {

		val user_name     = user.userName
		val email         = user.email
		val password      = user.password
		val livingStatus  = user.livingStatus
		val latitude      = user.latitude
		val longitude     = user.longitude
					
		// New user so set the account created, last active and last login times to today.
		var created:Date = Calendar.getInstance.getTime 	
		
	    var last_active:Date = created
	    var last_login:Date  = created
		println("userCreate2 - Created     = " + created)
		println("userCreate2 - Last Login  = " + last_active)
		println("userCreate2 - Last login  = " + last_login)		
		
		val sqlString = 
  			"INSERT INTO users (created, last_active, last_login, user_name, email, password, livingstatus, latitude, longitude ) " +
	      			"VALUES ( '" + created + "', '" +
                    last_active + "', '" +
                    last_login + "', '" + 
                    user_name + "', '" +
                    email + "', '" +
                    password + "', '" +
                    livingStatus + "', " +
                    latitude + ", " +
                    longitude + ")"
                    
		println("SQL = " + sqlString)
				
		DB.withConnection { implicit connection =>
	      	SQL(
	      	    sqlString
	      	    
	      	    /*
	      	     * TODO - Getting errors from created, last_active and last_login
     			"""
	      			INSERT INTO users (created, last_active, last_login, user_name, email, password, livingstatus, latitude, longitude ) 
	      			VALUES ({created},{last_active},{last_login}, {userName}, {email}, {password}, {livingStatus}, {latitude}, {longitude})
	      	    """	 
	      	    * 
	      	    */     	
      		).on(
      			'userName      -> user.userName,
      			'email 	       -> user.email,
      			'password      -> user.password,
      			'livingStatus  -> user.livingStatus,
      			'latitude      -> user.latitude,
      			'longitude     -> user.longitude
 
      		).executeInsert()
      		
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert User.")	        
    	}
    	 
	} // End - userCreate
	
	// =======================================================================
	//                  doesDatabaseValueExistInTable
	//
	//    Verify value such as User Name doesn't already exist
	//
	def doesDatabaseValueExistInTable (columnName:String, tableName:String, value:String ): Boolean = {
		
		var valueExist = false;
		
		val sqlString = "SELECT * FROM " + tableName + " WHERE " + columnName + " = '" + value + "'"
		//println("Sql String = " + sqlString)
	  
		var results = DB.withConnection { implicit connection =>
		  	SQL (
		  	    sqlString
		  	).as(get[String](columnName)*) 		  	
		}
		
		if (results.size >= 1) { valueExist = true}

		return valueExist
	  
	} // End of doesDatabaseValueExistInTable
	
	

	
}  // End of object User






