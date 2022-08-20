package util.search

import route.handler.SearchCriteria
import zio.Task
import route.handler.SearchCriteria

trait CanSearch[A] {
  def searchByTitle(title: String, criteria: SearchCriteria): Task[Either[String, List[A]]]
}
