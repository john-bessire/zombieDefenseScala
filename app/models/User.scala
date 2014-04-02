package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class User(
    id: 	    Pk[Long] = NotAssigned,
    created:    Date,
    lastActive: Option[Date],
    lastLogin:	Option[Date],
    userName:   String,
    email: 	    String,
    password:   String,
    being:      String      // being, Zombie, injured and in unknown state
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
	    get[String]("users.being")  map {
			case id ~ created ~ lastActive ~ lastLogin ~ userName ~ email ~ password ~ being => 
			  	User(id, created, lastActive, lastLogin, userName, email, password, being)
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

		val user_name = user.userName
		val email     = user.email
		val password  = user.password
		val being     = user.being
		
		DB.withConnection { implicit connection =>
	      	SQL(	      	    
     			"""
	      			INSERT INTO users (created, last_active, last_login, user_name, email, password, being ) 
	      			VALUES ('1999-01-08','1999-01-08','1999-01-08', {userName}, {email}, {password}, {being})
	      	    """	      	
      		).on(
      			'userName   -> user.userName,
      			'email 	    -> user.email,
      			'password   -> user.password,
      			'being      -> user.being
 
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






