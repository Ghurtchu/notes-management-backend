package route.handler

import db.note.NotesRepositoryLive
import model.{JwtContent, LoginPayload, Note}
import pdi.jwt.{Jwt, JwtCirce}
import route.interface.*
import route.implementation.GetAllNotesServiceLive
import server.NotesServer
import zhttp.http.Response
import zio.*
import zio.json.*
import zhttp.http.*
import RequestHandlerDefinitions.GetAllNotesHandler

final case class GetAllNotesHandlerLive(getAllNotesService: GetAllNotesService) extends GetAllNotesHandler:
  
  override def handle(jwtContent: JwtContent): Task[Response] =
    getAllNotesService.getNotesByUserId(jwtContent.userId)
      .map(_.toJsonResponse)


object GetAllNotesHandlerLive:
  
  lazy val layer: URLayer[GetAllNotesService, GetAllNotesHandler] =
    ZLayer.fromFunction(GetAllNotesHandlerLive.apply)
  

