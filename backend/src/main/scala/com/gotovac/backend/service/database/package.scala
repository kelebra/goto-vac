package com.gotovac.backend.service

import com.gotovac.backend.service.repository.{StateRepository, UserRepository}
import com.gotovac.model._
import slick.jdbc.PostgresProfile.api._

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
        Db.run(userQuery.update(
          (attempt.login, attempt.password, Option(token.value)))
        )
        Option(token)
      } else None
    }

    override def hasValidSession(token: Token): Boolean =
      Db.run(
        Db.credentials
          .filter(_.login === token.login)
          .filter(_.token === Option(token.value))
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
              case ((l1, _, _), Some((_, date))) => l1 -> Set(SelectedDate(date))
              case ((l1, _, _), None)            => l1 -> Set.empty[SelectedDate]
            })
        ).toMap
      )
    }

    override def updateState(stateUpdate: StateUpdate): Unit =
      Db.run(
        if (stateUpdate.selected)
          Db.state += (stateUpdate.token.login, stateUpdate.date.date)
        else
          Db.state
            .filter(_.login === stateUpdate.token.login)
            .filter(_.date === stateUpdate.date.date)
            .delete
      )
  }

}
