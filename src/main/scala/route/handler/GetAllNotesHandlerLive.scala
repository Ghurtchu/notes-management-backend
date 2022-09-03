package route.handler

import db.NotesRepositoryLive
import model.{JwtContent, LoginPayload, Note}
import pdi.jwt.{Jwt, JwtCirce}
import route.interface.*
import route.implementation.GetAllNotesServiceLive
import server.NotesServer
import zhttp.http.Response
import zio.*
import zio.json.*
import zhttp.http.*

trait GetAllNotesHandler {
  def handle(jwtContent: JwtContent): Task[Response]
}

final case class GetAllNotesHandlerLive(getAllNotesService: GetAllNotesService) extends GetAllNotesHandler {
  
  override def handle(jwtContent: JwtContent): Task[Response] = for {
    notes         <- getAllNotesService.getNotesByUserId(jwtContent.id)
    response      <- ZIO.succeed(Response.text(notes.toJsonPretty))
  } yield response

}

object GetAllNotesHandlerLive {
  
  lazy val layer: URLayer[GetAllNotesService, GetAllNotesHandler] =
    ZLayer.fromFunction(GetAllNotesHandlerLive.apply _)
  
}