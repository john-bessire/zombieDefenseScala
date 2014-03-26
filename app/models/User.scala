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
    human:      String      // Human, Zombie, injured and in unknown state
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
	    get[String]("users.human")  map {
			case id ~ created ~ lastActive ~ lastLogin ~ userName ~ email ~ password ~ human => 
			  	User(id, created, lastActive, lastLogin, userName, email, password, human)
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
		val human     = user.human
		

		var results = DB.withConnection { implicit connection =>
	      	SQL(	
     			"""
	      			INSERT INTO users (created, last_active, last_login, user_name, email, password, human ) 
	      			VALUES ('1999-01-08','1999-01-08','1999-01-08', {userName}, {email}, {password}, {human})
	      	    """
	      	
      		).on(
      			'userName   -> user.userName,
      			'email 	    -> user.email,
      			'password   -> user.password,
      			'human      -> user.human
 
      		).executeInsert()
      		
    	} match {
	        case Some(long) => new Id[Long](long) // The Primary Key
	        case None       => throw new Exception("SQL Error - Did not insert User.")
	        
    	}
    	
    	// TODO - catch error PSQLException: ERROR: duplicate key value violates unique constrain
    	
    	results
	} // End - userCreate
	
	
}  // End of object User





