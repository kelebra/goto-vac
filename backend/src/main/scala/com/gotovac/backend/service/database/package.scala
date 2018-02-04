package com.gotovac.backend.service

import com.gotovac.backend.service.repository.{StateRepository, UserRepository}
import com.gotovac.model._
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

package object database {

  object UserRepository extends UserRepository {

    override def authorize(attempt: Credentials): Option[Token] = {
      val userQuery =
        Db.credentials
          .filter(_.login === attempt.login)
          .filter(_.password === attempt.password)

      val credentialsAreValid = Db.run(userQuery.exists.result)
      if (credentialsAreValid) {
        val token = Token(attempt.login)
        Db.run(userQuery.update((attempt.login, attempt.password, token.value)))
        Option(token)
      } else None
    }

    override def hasValidSession(token: Token): Boolean =
      Db.run(
        Db.credentials
          .filter(_.login === token.login)
          .filter(_.token === token.value)
          .exists
          .result
      )
  }

  object StateRepository extends StateRepository {

    override def getState(token: Token): GroupState = {
      GroupState(
        Db.run(
          Db.credentials
            .joinLeft(Db.state).on(_.login === _.login)
            .result
            .map(_.map {
              case ((l1, _, _), Some((_, date, utc))) => l1 -> Seq(SelectedDate(date, utc))
              case ((l1, _, _), None)                 => l1 -> Seq()
            })
        ).toMap
      )
    }

    override def updateState(stateUpdate: StateUpdate): Unit =
      Db.run(
        if (stateUpdate.selected)
          Db.state.insertOrUpdate(
            (stateUpdate.token.login, stateUpdate.date.date, stateUpdate.date.zone)
          )
        else
          Db.state
            .filter(_.login === stateUpdate.token.login)
            .filter(_.date === stateUpdate.date.date)
            .filter(_.utc === stateUpdate.date.zone)
            .delete
      )
  }

}
