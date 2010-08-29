package com.imaginej {

package snippet {

import java.util.Date
import xml.{Text, NodeSeq}
import net.liftweb.http.SessionVar
import net.liftweb.util.Helpers

import Helpers._

object accessedSessionVar extends SessionVar(new Date)

class AccessSnippet {
  def accessed(xhtml: NodeSeq): NodeSeq = {
    bind("session", xhtml,
      "reset" -> {
        accessedSessionVar(new Date)
        Text("")
      })

  }

}

}

}
