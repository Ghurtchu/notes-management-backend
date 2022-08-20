package route.handler

import model.*
import util.*
import route.interface.CommonRequestHandler
import util.search.{NotesSearchService, CanSearch}
import zhttp.http.*
import zio.*
import zio.json.*

import java.time.Instant
import java.util.Date

class SearchNoteRoute {

  private val searchService: CanSearch[Note] = NotesSearchService

  final def handle(request: Request): Task[Response] = for {
    title        <- ZIO.succeed(request.url.queryParams("title").head)
    isExact      <- ZIO.succeed {
      request.url.queryParams.get("exact")
        .fold(SearchCriteria.nonExact)(criteria => if criteria.head == "true" then SearchCriteria.exact else SearchCriteria.nonExact)
    }
    searchResult <- searchService.searchByTitle(title, isExact)
    response     <- ZIO.succeed(searchResult.fold(
      err  => Response.text(err),
      note => Response.text(note.toJsonPretty)
    ))
  } yield response

}

sealed trait SearchCriteria {
  def isExact: Boolean
}

object SearchCriteria {
  def exact: SearchCriteria = Exact

  def nonExact: SearchCriteria = NonExact
}

case object Exact extends SearchCriteria {
  final override def isExact: Boolean = true
}

case object NonExact extends SearchCriteria {
  final override def isExact: Boolean = false
}
