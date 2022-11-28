package route.handler

import route.interface.*
import zio.*
import route.implementation.SignupServiceLive
import zhttp.http.{Request, Response, Status}
import model.*
import route.interface.CreateNoteService
import zio.json.*
import model.LoginPayload
import RequestHandlerDefinitions.SignupHandler

final case class SignupHandlerLive(signupService: SignupService) extends SignupHandler:

  final override def handle(request: Request): Task[Response] = 
    for
      userEither <- request.bodyAsString.map(_.fromJson[User])
      response   <- userEither.fold(_ => ZIO.succeed(Response.text("Invalid Json")), mapSignupServiceResultToResponse)
    yield response
  
  
  private def mapSignupServiceResultToResponse(user: User): Task[Response] =
    signupService
      .signUp(user)
      .map(_.fold(
        Response.text(_).setStatus(Status.Conflict),
        token => Response.text(token).setStatus(Status.Created)
      ))
  

object SignupHandlerLive:

  lazy val layer: URLayer[SignupService, SignupHandler] =
    ZLayer.fromFunction(SignupHandlerLive.apply)

