package com.imaginej {

package snippet {

import java.util.Locale

import xml.{Text, NodeSeq, Group}
import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.http.{S, SHtml, SessionVar}
import net.liftweb.util.Helpers

import Helpers._
import S._  
import SHtml._

object currentLocale extends SessionVar[Box[Locale]](Box(Locale.US))

class IndexSnippet {
  def doChange(locale: Locale) = currentLocale(Full(locale))
  val changeXhtml = selectObj(
    List(new Locale("nl", "BE"), Locale.US).map(locale => (locale, locale.getDisplayName)), currentLocale, doChange)
  def changeLocale(xhtml: Group): NodeSeq =
    bind(
      "locale", xhtml,
      "change" -> changeXhtml)


}

}

}