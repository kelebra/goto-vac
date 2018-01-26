package com.gotovac.frontend.pages

import com.gotovac.frontend.socket.ReplySocket
import com.gotovac.model.Credentials
import org.scalajs.dom.Element
import org.scalajs.dom.document.getElementById
import org.scalajs.dom.html.Input
import org.scalajs.dom.raw.MouseEvent

import scalatags.JsDom.all._

object Login extends Page {

  private val submitCredentials: MouseEvent => Unit =
    _ => ReplySocket.login(
      Credentials(
        getElementById("login").asInstanceOf[Input].value,
        getElementById("password").asInstanceOf[Input].value
      )
    )

  override val elements: List[Element] = List(
    div(`class` := "row",
      div(`class` := "col col-6 push-center",
        fieldset(
          legend("Credentials from secret e-mail:"),
          div(`class` := "form-item",
            label("Login"),
            input(`type` := "text", id := "login")
          ),
          div(`class` := "form-item",
            label("Password"),
            input(`type` := "password", id := "password")
          ),
          div(`class` := "form-item",
            button("Submit", onclick := submitCredentials)
          )
        )
      )
    ).render
  )
}
