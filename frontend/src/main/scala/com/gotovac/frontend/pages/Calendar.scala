package com.gotovac.frontend.pages

import com.gotovac.frontend.pages.components.CalendarSelection
import com.gotovac.frontend.socket.BroadcastSocket
import com.gotovac.frontend.socket.storage.LoginStorage
import com.gotovac.frontend.util.{JsDateUtils, MonthOffset, daysOfYear, draw, offsetMonths}
import com.gotovac.model.Types.Login
import com.gotovac.model.{GroupState, SelectedDate, StateUpdate}
import org.scalajs.dom.raw.MouseEvent

import scala.scalajs.js.Date
import scalatags.JsDom.all._

object Calendar {

  def renderCalendarView(state: GroupState): Unit = {
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
    state.data.foreach {
      case (login, dates) =>
        dates.foreach(date => update(login, date, selected = true))
    }
  }

  def update(delta: StateUpdate): Unit = update(delta.token.login, delta.date, delta.selected)

  private def update(login: Login,
                     selectedDate: SelectedDate,
                     selected: Boolean): Unit = {
    val id = CalendarSelection.cellId(login, selectedDate.date)
    if (selected) CalendarSelection.select(id)
    else CalendarSelection.unSelect(id)
  }

  private def calendarTable(state: GroupState) = {
    val dates = generateDates
    val months = offsetMonths(dates)

    table(`class` := "bordered",
      tr(style := "text-align: center", td(), months.map(monthCell)),
      state.data.keys.toList.sorted
        .map(login => login -> dates)
        .map {
          case (login, dateLine) =>
            tr(
              td(b(login)),
              dateLine.map(date =>
                dateCell(login, date, LoginStorage.token.login == login)
              )
            )
        }
        .toList
    )
  }

  private def monthCell(month: MonthOffset) = td(colspan := month._2, month._1)

  private def dateCell(login: Login, date: Date, canModify: Boolean) = {
    val identifier = CalendarSelection.cellId(login, date)
    val pointer = if (canModify) "cursor: pointer;" else ""
    val onclickFunction: MouseEvent => Unit =
      if (canModify) dateClick(identifier) else _ => Unit
    td(
      id := identifier,
      style := s"white-space: nowrap; $pointer",
      date.dayOfWeek,
      onclick := onclickFunction
    )
  }

  private def dateClick(identifier: String): MouseEvent => Unit =
    _ => BroadcastSocket.modifyDate(
      SelectedDate(CalendarSelection.cellUTCDate(identifier)),
      !CalendarSelection.isSelected(identifier)
    )

  private def generateDates = {
    val start = new Date()
    start.setHours(0, 0, 0, 0)
    daysOfYear(start, start.plusYears(1), List(start))
  }
}
