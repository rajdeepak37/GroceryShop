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

import I10N.i10n  

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
        error(?("User_Error_UserNameCannotBeBlank"))
      } else {
        UserStore.retrieveByName(requestUser.name) match {
          case None => UserStore.create((requestUser.name, requestUser.password))
          case _ => error(i10n("User_Error_UserAlreadyExists", requestUser.name))
        }
        redirectTo("list")
      }
    }

    val nameXhtml = text("", doName(_))
    val passwordXhtml = password("", doPassword(_))
    val submitXhtml = submit(?("User_Add"), doSubmit)

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
      error(i10n("User_Error_YouAreAlreadyLoggedInAs", sessionUser.name, sessionLoggedInNickName))
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
              case ole: OptimisticLockException => error(?("User_Error_ErrorLoggingInUserTryAgain"))
              case pe: PersistenceException => error(?("User_Error_ErrorLoggingInUser"))
            }
          case _ => error(i10n("User_Error_NicknameForUserIsAlreadyInUse", givenNickName, requestUser.name))
        }
      } else if (loginUserExists) {
        error(i10n("User_Error_PasswordForUserIsNotCorrect", requestUser.name))
      } else {
        error(i10n("User_Error_UserDoesNotExist", requestUser.name))
      }
      redirectTo("list")
    }

    val nameXhtml = text("", doName(_))
    val nickNameXhtml = text("", doNickName(_))
    val passwordXhtml = password("", doPassword(_))
    val submitXhtml = submit(?("User_Login"), doSubmit)

    bind(
      "user", xhtml,
      "name" -> nameXhtml,
      "nickName" -> nickNameXhtml,
      "password" -> passwordXhtml,
      "submit" -> submitXhtml)
  }

  def ifLoggedIn(xhtml: NodeSeq): NodeSeq =
    if (sessionLoggedInNickName == "") {
      error(?("User_Error_YouAreNotLoggedIn"))
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
        case ole: OptimisticLockException => error(?("User_Error_ErrorLoggingOutUserTryAgain"))
        case pe: PersistenceException => error(?("User_Error_ErrorLoggingOutUser"))
      }
      redirectTo("list")
    }

    val submitXhtml = submit(?("User_Logout"), doSubmit)

    bind(
      "user", xhtml,
      "submit" -> submitXhtml)
  }

  def logoutUserText(xhtml: NodeSeq): NodeSeq = {

    val logoutXhtml = Text(i10n("User_LogoutUser", sessionUser.name))

    bind(
      "user", xhtml,
      "logout" -> logoutXhtml)
  }

}

}

}
