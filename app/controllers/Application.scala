package controllers

import models.Task

import play.api._
import play.api.mvc._
import java.util.Calendar
import java.text.SimpleDateFormat

import play.api.data._
import play.api.data.Forms._

import play.api.libs.ws.WS

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
 
  
  def test = TODO
}