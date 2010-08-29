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
    // where to search for snippets
    LiftRules.addToPackages("com.imaginej")

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
      Menu(Loc("Product List", "category" :: "product" :: "list" :: Nil, "Product List", Hidden)),
      Menu(Loc("Add Product", "category" :: "product" :: "add" :: Nil, "Add Product", Hidden)),
      Menu(Loc("Edit Product", "category" :: "product" :: "edit" :: Nil, "Edit Product", Hidden)),
      Menu(Loc("Transfer Product", "category" :: "product" :: "transfer" :: Nil, "Transfer Product", Hidden)),
      Menu(Loc("Add To Cart", "cart" :: "add" :: Nil, "Add To Cart", Hidden)),
      Menu(Loc("Cart", "cart" :: "list" :: Nil, "Cart", Hidden)),
      Menu(Loc("Remove Cart Item", "cart" :: "remove" :: Nil, "Remove Cart Item", Hidden)),
      Menu(Loc("Checked Out", "cart" :: "checkedOut" :: Nil, "Checked Out", Hidden))
      )
    LiftRules.setSiteMap(entries)
  }
}

