package controllers

import models.Task
import play.api._
import play.api.mvc._
import java.util.Calendar
import java.text.SimpleDateFormat
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws.WS
import models.Task
import models.Geolocation

object Application extends Controller {
  
  
  def createMap = Action{
   Redirect(routes.Application.tasks) 
  }
   
  def index = Action {
    
    println ("Action Index")
    Redirect(routes.Application.tasks)
  }

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.create(label)
        Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  val taskForm = Form(
    "label" -> nonEmptyText
  )
  
  def getLocation = Action {
	  println("Get Location called")  
	  
	  Geolocation.fetchLatitudeAndLongitude("Mountain View, Ca")

      
      println("After function called to get longitide and latitude")
      
	  Redirect(routes.Application.tasks)
  }
 
  
  def test = TODO
}