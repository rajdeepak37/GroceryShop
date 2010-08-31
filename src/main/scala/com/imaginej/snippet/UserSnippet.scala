package com.imaginej {

package snippet {

import xml.{Text, NodeSeq}

import net.liftweb.common.Empty
import net.liftweb.http.{SessionVar, RequestVar, S, SHtml}
import net.liftweb.util.Helpers

import javax.persistence.{OptimisticLockException, PersistenceException}

import Helpers._
import S._
import SHtml._

import domain.user._

object userRequestVar extends RequestVar(new UserEntity)
object userSessionVar extends SessionVar(new UserEntity)
object loggedInNickNameSessionVar extends SessionVar("")

class UserSnippet {
  def requestUser = userRequestVar.is
  // only sessionUser.name is correct
  def sessionUser = userSessionVar.is

  def sessionLoggedInNickName = loggedInNickNameSessionVar.is

  def list(xhtml: NodeSeq): NodeSeq = for{
    user <- UserStore.retrieveAll
    userName = user.name
    nameXhtml = {
      if (userName == sessionUser.name && sessionLoggedInNickName != "") {
        Text(userName + " (" + sessionLoggedInNickName + ")")
      } else {
        Text(userName)
      }
    }
    balanceXhtml = Text(user.balance.toString)
    loggedInAsXhtml = {
      val loggedInAsOptions = for{
        retrievedUser@UserEntity(`userName`, _) <- UserStore.retrieveAll
        nickName <- retrievedUser.retrieveAllNickNames
      } yield (nickName, nickName)

      select(loggedInAsOptions, Empty, _ => ())
    }
    node <- bind(
      "user", xhtml,
      "name" -> nameXhtml,
      "balance" -> balanceXhtml,
      "loggedInAs" -> loggedInAsXhtml)
  } yield node


  def add(xhtml: NodeSeq): NodeSeq = {

    def doName(userName: String) = {
      requestUser.name = userName
    }

    def doPassword(userPassword: String) = {
      requestUser.password = userPassword
    }

    def doSubmit() = {
      if (requestUser.name.length == 0) {
        error("User name cannot be blank")
      } else {
        UserStore.retrieveByName(requestUser.name) match {
          case None => UserStore.create((requestUser.name, requestUser.password))
          case _ => error("User " + requestUser.name + " already exists")
        }
        redirectTo("list")
      }
    }

    val nameXhtml = text("", doName(_))
    val passwordXhtml = password("", doPassword(_))
    val submitXhtml = submit("Add", doSubmit)

    bind(
      "user", xhtml,
      "name" -> nameXhtml,
      "password" -> passwordXhtml,
      "submit" -> submitXhtml)
  }

  def ifNotLoggedIn(xhtml: NodeSeq): NodeSeq =
    if (sessionLoggedInNickName == "") {
      xhtml
    } else {
      error("You are already logged in as " + sessionUser.name + " (" + sessionLoggedInNickName + ")")
      redirectTo("list")
    }

  def login(xhtml: NodeSeq): NodeSeq = {
    var loginUserExists = false
    var givenNickName = ""
    var givenPassword = ""

    def doName(userName: String) = {
      UserStore.retrieveByName(userName) match {
        case None => requestUser.name = userName
        case Some(user) => {loginUserExists = true; userRequestVar(user)}
      }
    }

    def doNickName(userNickName: String) = {
      givenNickName = userNickName
    }

    def doPassword(userPassword: String) = {
      givenPassword = userPassword
    }

    def doSubmit() = {
      if (loginUserExists && requestUser.password == givenPassword) {
        (for{
          user <- UserStore.retrieveAll
          if (user.name == requestUser.name && (user.retrieveAllNickNames contains givenNickName))
        } yield user) match {
          case Nil =>
            try {
              userSessionVar(requestUser)
              UserService.addLoggedInNickName((sessionUser.name, givenNickName))
              loggedInNickNameSessionVar(givenNickName)
            } catch {
              case ole: OptimisticLockException => error("Error logging in user (try again)")
              case pe: PersistenceException => error("Error logging in user")
            }
          case _ => error("Nickname " + givenNickName + " for user " + requestUser.name + " is already in use")
        }
      } else if (loginUserExists) {
        error("Password for user " + requestUser.name + " is not correct")
      } else {
        error("User " + requestUser.name + " does not exist")
      }
      redirectTo("list")
    }

    val nameXhtml = text("", doName(_))
    val nickNameXhtml = text("", doNickName(_))
    val passwordXhtml = password("", doPassword(_))
    val submitXhtml = submit("Login", doSubmit)

    bind(
      "user", xhtml,
      "name" -> nameXhtml,
      "nickName" -> nickNameXhtml,
      "password" -> passwordXhtml,
      "submit" -> submitXhtml)
  }

  def ifLoggedIn(xhtml: NodeSeq): NodeSeq =
    if (sessionLoggedInNickName == "") {
      error("You are not logged in")
      redirectTo("list")
    } else {
      xhtml
    }

  def logout(xhtml: NodeSeq): NodeSeq = {

    def doSubmit() = {
      try {
        UserService.removeLoggedInNickName((sessionUser.name, sessionLoggedInNickName))
        loggedInNickNameSessionVar("")
      } catch {
        case ole: OptimisticLockException => error("Error logging out user (try again)")
        case pe: PersistenceException => error("Error logging out user");
      }
      redirectTo("list")
    }

    val submitXhtml = submit("Logout", doSubmit)

    bind(
      "user", xhtml,
      "submit" -> submitXhtml)
  }

  def logoutUserText(xhtml: NodeSeq): NodeSeq = {

    val logoutXhtml = Text("Logout User " + sessionUser.name)

    bind(
      "user", xhtml,
      "logout" -> logoutXhtml)
  }

}

}

}
