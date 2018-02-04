package com.gotovac.frontend.pages

import com.gotovac.frontend.socket.BroadcastSocket
import com.gotovac.frontend.util.{JsDateUtils, MonthOffset, daysOfYear, draw, offsetMonths}
import com.gotovac.model.{GroupState, StateUpdate}
import org.scalajs.dom.raw.MouseEvent
import org.scalajs.dom.document

import scala.scalajs.js.Date
import scalatags.JsDom.all._

object Calendar {

  def renderCalendarView(state: GroupState): Unit =
    draw(
      List(
        div(`class` := "row", style := "padding: 1%",
          div(`class` := "col col-5 push-center", style := "text-align: center",
            h2("Vacation calendar")
          ),
        ),
        div(`class` := "row",
          div(`class` := "row", calendarTable(state))
        )
      ).map(_.render)
    )

  private def calendarTable(state: GroupState) = {
    val dates = generateDates
    val months = offsetMonths(dates)

    table(`class` := "bordered",
      tr(style := "text-align: center", td(), months.map(monthCell)),
      state.data
        .keys
        .map(login => login -> dates)
        .map {
          case (login, dateLine) => tr(td(b(login)), dateLine.map(dateCell))
        }
        .toList
    )
  }

  private def monthCell(month: MonthOffset) =
    td(colspan := month._2, month._1)

  private def dateCell(date: Date) = {
    val dateId = date.id
    td(
      id := dateId,
      style := "white-space: nowrap;",
      date.dayOfWeek,
      onclick := dateClick(dateId)
    )
  }

  private def dateClick(dateId: String): MouseEvent => Unit =
    _ => BroadcastSocket.modifyDate(dateId, isSelected(dateId))

  def update(delta: StateUpdate): Unit = ???

  private def generateDates = {
    val start = new Date()
    daysOfYear(start, start.plusYears(1), List(start))
  }

  private def isSelected(dateId: String): Boolean =
    document.getElementById(dateId).classList.contains("success")
}
