package bootstrap.liftweb

import xml.Text

import net.liftweb.http._
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc._

import net.liftweb.common.Full
import auth.{AuthRole, HttpBasicAuthentication, userRoles}

import S._

import com.imaginej.domain.user._
import com.imaginej.comet.SessionActor
import com.imaginej.snippet.{currentLocale, loggedInNickNameSessionVar, userSessionVar}

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  UserService.initLoggedInNickNames

  LiftSession.onSetupSession = List((_: LiftSession) => {
    println("set up session")
  })

  LiftSession.onAboutToShutdownSession = List((_: LiftSession) => {
    println("about to shut down session")
    if (loggedInNickNameSessionVar.is != "") {
      UserService.removeLoggedInNickName((userSessionVar.is.name, loggedInNickNameSessionVar.is))
    }
  })

  def boot {
    // where to search
    LiftRules.addToPackages("com.imaginej")

    // view dispatch
    LiftRules.viewDispatch.append {
      case "index" :: Nil => {
        Left(() => Full(com.imaginej.view.index.View.doIt()))
      }

      case "user" :: "list" :: Nil => {
        Left(() => Full(com.imaginej.view.user.View.doList()))
      }
      case "user" :: "add" :: Nil => {
        Left(() => Full(com.imaginej.view.user.View.doAdd()))
      }
      case "user" :: "login" :: Nil => {
        Left(() => Full(com.imaginej.view.user.View.doLogin()))
      }
      case "user" :: "logout" :: Nil => {
        Left(() => Full(com.imaginej.view.user.View.doLogout()))
      }

      case "category" :: "list" :: Nil => {
        Left(() => Full(com.imaginej.view.category.View.doList()))
      }
      case "category" :: "add" :: Nil => {
        Left(() => Full(com.imaginej.view.category.View.doAdd()))
      }

      case "product" :: "list" :: Nil => {
        Left(() => Full(com.imaginej.view.product.View.doList()))
      }
      case "product" :: "add" :: Nil => {
        Left(() => Full(com.imaginej.view.product.View.doAdd()))
      }
      case "product" :: "edit" :: Nil => {
        Left(() => Full(com.imaginej.view.product.View.doEdit()))
      }
      case "product" :: "transfer" :: Nil => {
        Left(() => Full(com.imaginej.view.product.View.doTransfer()))
      }

      case "cart" :: "list" :: Nil => {
        Left(() => Full(com.imaginej.view.cart.View.doList()))
      }
      case "cart" :: "add" :: Nil => {
        Left(() => Full(com.imaginej.view.cart.View.doAdd()))
      }
      case "cart" :: "remove" :: Nil => {
        Left(() => Full(com.imaginej.view.cart.View.doRemove()))
      }
      case "cart" :: "checkedOut" :: Nil => {
        Left(() => Full(com.imaginej.view.cart.View.doCheckedOut()))
      }
    }

    // comet
    LiftRules.cometCreation.append {
      case CometCreationInfo("SessionActor", name, defaultXml, attributes, session) =>
        new SessionActor(session, Full("SessionActor"), name, defaultXml, attributes)
    }

    // site map
    val entries = SiteMap(
      Menu(Loc("Home", "index" :: Nil, ?("Menu_Home"))),
      Menu(Loc("User Login", "user" :: "login" :: Nil, ?("Menu_UserLogin"))),
      Menu(Loc("User Logout", "user" :: "logout" :: Nil, ?("Menu_UserLogout"))),
      Menu(Loc("User List", "user" :: "list" :: Nil, ?("Menu_UserList"))),
      Menu(Loc("Category List", "category" :: "list" :: Nil, ?("Menu_CategoryList"))),
      Menu(Loc("Add User", "user" :: "add" :: Nil, "", Hidden)),
      Menu(Loc("Add Category", "category" :: "add" :: Nil, "", Hidden)),
      Menu(Loc("Product List", "product" :: "list" :: Nil, "", Hidden)),
      Menu(Loc("Add Product", "product" :: "add" :: Nil, "", Hidden)),
      Menu(Loc("Edit Product", "product" :: "edit" :: Nil, "", Hidden)),
      Menu(Loc("Transfer Product", "product" :: "transfer" :: Nil, "", Hidden)),
      Menu(Loc("Add To Cart", "cart" :: "add" :: Nil, "", Hidden)),
      Menu(Loc("Cart", "cart" :: "list" :: Nil, "", Hidden)),
      Menu(Loc("Remove Cart Item", "cart" :: "remove" :: Nil, "", Hidden)),
      Menu(Loc("Checked Out", "cart" :: "checkedOut" :: Nil, "", Hidden))
      )
    LiftRules.setSiteMap(entries)

    // roles
    val roles = AuthRole("Admin")
    // protected resources
    LiftRules.httpAuthProtectedResource.append {
      case (Req("category" :: "add" :: _, _, _)) => roles.getRoleByName("Admin")
      case (Req("product" :: "add" :: _, _, _)) => roles.getRoleByName("Admin")
      case (Req("product" :: "edit" :: _, _, _)) => roles.getRoleByName("Admin")
      case (Req("product" :: "transfer" :: _, _, _)) => roles.getRoleByName("Admin")
    }
    // authentication
    LiftRules.authentication = HttpBasicAuthentication("lift") {
      case ("lucdup", "1234", req) =>
        userRoles(AuthRole("Admin"))
        true
    }
    // i10n
    LiftRules.resourceNames = "default" :: "menu" :: "index" :: "user" :: "category" :: "product" :: "cart" :: Nil
    LiftRules.localeCalculator = r => currentLocale openOr LiftRules.defaultLocaleCalculator(r)
  }
}

