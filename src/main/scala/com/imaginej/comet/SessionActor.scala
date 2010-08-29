package com.imaginej {

package comet {

import java.util.Calendar

import scala.xml.{Text, NodeSeq}
import net.liftweb._
import http._
import common._
import util._
import Helpers._
import js._
import JsCmds._

import snippet.{accessedSessionVar}

class SessionActor(initSession: LiftSession,
                   initType: Box[String],
                   initName: Box[String],
                   initDefaultXml: NodeSeq,
                   initAttributes: Map[String, String]) extends CometActor {
  val timeUnitInSeconds = initAttributes("timeUnitInSeconds").toInt

  val sessionTimeInSeconds = initAttributes("sessionTime").toInt * timeUnitInSeconds

  val timeToShowWarningInSeconds = initAttributes("timeToShowWarning").toInt * timeUnitInSeconds

  override def defaultPrefix = Full("session")

  private lazy val timeLeftId = uniqueId + "_timeLeft"

  ActorPing.schedule(this, Tick, (sessionTimeInSeconds / 2) seconds)

  def timeLeftInSeconds = {
    val nowInMillis = {
      val localNow = Calendar.getInstance
      localNow.setTime(timeNow)
      localNow.getTimeInMillis
    }
    val lastAccessedInMillis = {
      val localAccessed = Calendar.getInstance
      localAccessed.setTime(accessedSessionVar.is)
      localAccessed.getTimeInMillis
    }
    ((lastAccessedInMillis + sessionTimeInSeconds * 1000) - nowInMillis) / 1000
  }

  def timeLeftInSecondsXhtml =
    <span>
      <font color='red' id={timeLeftId}></font>
    </span>

  def render = bind("timeLeftInSeconds" -> timeLeftInSecondsXhtml)

  override def lowPriority = {
    case Tick => {
      val localTimeLeftInSeconds = timeLeftInSeconds
      if (localTimeLeftInSeconds >= timeToShowWarningInSeconds) {
        println("scheduling to receive a tick after " + (localTimeLeftInSeconds / 2) + " second(s)")
        ActorPing.schedule(this, Tick, (localTimeLeftInSeconds / 2) seconds)
      } else if (localTimeLeftInSeconds >= timeUnitInSeconds) {
        partialUpdate(SetHtml(timeLeftId, Text("Warning: approximately " + localTimeLeftInSeconds + " session time seconds left")))
        println("scheduling to receive a tick after " + timeUnitInSeconds + " second(s)")
        ActorPing.schedule(this, Tick, timeUnitInSeconds seconds)
      } else {
        println("about to shutdown session")
        partialUpdate(SetHtml(timeLeftId, Text("about to shutdown session")))
        initSession.terminateHint
      }
    }
  }

  initCometActor(initSession, initType, initName, initDefaultXml, initAttributes)
}

case object Tick
}

}
