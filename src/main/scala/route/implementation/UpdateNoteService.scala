package route.implementation

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.{RecordCreator, RecordUpdater}
import zhttp.http.Response
import zio.*
import zio.json.*


class UpdateNoteService extends RecordUpdater[Note] {

  private val notesRepository: CRUD[Note] = NotesRepository()

  override def updateRecord(id: Int, note: Note): Task[Either[String, String]] = for {
    updateStatus <- notesRepository.update(id, note)
  } yield updateStatus

}
