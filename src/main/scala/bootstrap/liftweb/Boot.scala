package bootstrap.liftweb

import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._

import net.liftweb.common.Full

import com.imaginej.domain.user._
import com.imaginej.snippet.{loggedInNickNameSessionVar, userSessionVar}
import com.imaginej.comet.SessionActor

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

    LiftRules.viewDispatch.append {
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

    LiftRules.cometCreation.append {
      case CometCreationInfo("SessionActor", name, defaultXml, attributes, session) =>
        new SessionActor(session, Full("SessionActor"), name, defaultXml, attributes)
    }

    // Build SiteMap
    val entries = SiteMap(
      Menu(Loc("Home", "index" :: Nil, "Home")),
      Menu(Loc("User Login", "user" :: "login" :: Nil, "User Login")),
      Menu(Loc("User Logout", "user" :: "logout" :: Nil, "User Logout")),
      Menu(Loc("User List", "user" :: "list" :: Nil, "User List")),
      Menu(Loc("Category List", "category" :: "list" :: Nil, "Category List")),
      Menu(Loc("Add User", "user" :: "add" :: Nil, "Add User", Hidden)),
      Menu(Loc("Add Category", "category" :: "add" :: Nil, "Add Category", Hidden)),
      Menu(Loc("Product List", "product" :: "list" :: Nil, "Product List", Hidden)),
      Menu(Loc("Add Product", "product" :: "add" :: Nil, "Add Product", Hidden)),
      Menu(Loc("Edit Product", "product" :: "edit" :: Nil, "Edit Product", Hidden)),
      Menu(Loc("Transfer Product", "product" :: "transfer" :: Nil, "Transfer Product", Hidden)),
      Menu(Loc("Add To Cart", "cart" :: "add" :: Nil, "Add To Cart", Hidden)),
      Menu(Loc("Cart", "cart" :: "list" :: Nil, "Cart", Hidden)),
      Menu(Loc("Remove Cart Item", "cart" :: "remove" :: Nil, "Remove Cart Item", Hidden)),
      Menu(Loc("Checked Out", "cart" :: "checkedOut" :: Nil, "Checked Out", Hidden))
      )
    LiftRules.setSiteMap(entries)
  }
}

