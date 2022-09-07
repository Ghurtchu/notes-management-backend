package route

import db.{DbError, DbSuccess}

package object implementation:

  extension[A] (value: A)
    def withQuotes: String = s"`$value`"
    
  extension (dbErrorOrSuccess: Either[DbError, DbSuccess])
    def foldMap: Either[String, String] =
      dbErrorOrSuccess.fold(
        err     => Left(err.msg),
        success => Right(success.msg)
      )

