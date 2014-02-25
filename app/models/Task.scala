package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

import scala.concurrent.Future
//import akka.util.internal.Timeout

import play.api.libs.ws._


//import ExecutionContext.Implicits.global

import scala.concurrent._





import play.api.mvc._


case class Task(id: Long, label: String)

	object Task {
  
	  def all(): List[Task] = DB.withConnection { implicit c =>
	    SQL("select * from task").as(task *)
	  }

	  def create(label: String) {
	    
	    println("Task Create")
	    
	    DB.withConnection { implicit c =>
	      SQL("insert into task (label) values ({label})").on(
	        'label -> label
	      ).executeUpdate()
	    }
	  }
	
	  def delete(id: Long) {
	    
	    println("Task delete")
	    
	    DB.withConnection { implicit c =>
	      SQL("delete from task where id = {id}").on(
	        'id -> id
	      ).executeUpdate()
	    }
	  }

	  val task = {
	    get[Long]("id") ~
	    get[String]("label") map {
	      case id~label => Task(id, label)
	    }
	  }
  
 
}