package com.gotovac.backend.service.database

import com.gotovac.model.Types.{Date, Login, UTC}
import slick.jdbc.meta.MTable

import scala.concurrent.Await
import scala.language.postfixOps

import scala.concurrent.ExecutionContext.Implicits.global

object Db {

  import slick.jdbc.SQLiteProfile.api._

  import scala.concurrent.duration._

  private implicit val duration: Duration = 3 seconds

  class CredentialsTable(tag: Tag) extends Table[(Login, String, String)](tag, "CREDENTIALS") {

    def login = column[Login]("LOGIN", O.PrimaryKey)

    def password = column[String]("PASSWORD")

    def token = column[String]("TOKEN")

    def * = (login, password, token)
  }

  class TokensTable(tag: Tag) extends Table[(Login, String)](tag, "TOKENS") {

    def login = column[Login]("LOGIN", O.PrimaryKey)

    def token = column[String]("TOKEN")

    def * = (login, token)
  }

  class StateTable(tag: Tag) extends Table[(Login, Date, UTC)](tag, "STATE") {

    def login = column[Login]("LOGIN", O.PrimaryKey)

    def date = column[Date]("DATE")

    def utc = column[UTC]("UTC")

    def * = (login, date, utc)
  }

  val credentials = TableQuery[CredentialsTable]

  val tokens = TableQuery[TokensTable]

  val state = TableQuery[StateTable]

  private val applicationTables = List(credentials, tokens, state)

  val db = Database.forConfig("fileDb")

  def init(): Unit = {
    val existing = db.run(MTable.getTables)
    val createTablesIfDoNotExist = existing.flatMap(v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = applicationTables.filter(table =>
        !names.contains(table.baseTableRow.tableName)).map(_.schema.create)
      db.run(DBIO.sequence(createIfNotExist))
    })
    Await.result(createTablesIfDoNotExist, Duration.Inf)
  }

  def run[T, E <: Effect](action: DBIOAction[T, NoStream, E]): T =
    Await.result(db.run(action), duration)
}
