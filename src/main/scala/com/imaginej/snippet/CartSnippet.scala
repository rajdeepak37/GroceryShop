package com.imaginej {

package snippet {

import xml.{Attribute, Text, NodeSeq}

import net.liftweb.common.Full
import net.liftweb.http.{SessionVar, S, SHtml}
import net.liftweb.http.js.JsCmds
import net.liftweb.util.Helpers

import Helpers._
import S._
import SHtml._

import domain.grocery._
import model._

import Util.baseWithTableFor
import Util.baseWithFormFor
import Util.baseWithSnippetFor

object cartItemSessionVar extends SessionVar(CartItem(new ProductEntity, 0))

class CartSnippet {
  def sessionCart = cartSessionVar.is

  def sessionProduct = productSessionVar.is

  def sessionUser = userSessionVar.is

  def sessionLoggedInNickName = loggedInNickNameSessionVar.is

  def sessionCartItem = cartItemSessionVar.is

  def baseWithTableForList(xhtml: NodeSeq): NodeSeq =
    baseWithTableFor("cart/list", 3, <lift:cartSnippet.list eager_eval="true"/>)

  def list(xhtml: NodeSeq): NodeSeq = {
    sessionCart.cartItems.flatMap(cartItem => {

      val productNameXhtml = Text(cartItem.product.name)
      val quantityXhtml = ajaxSelect(
        (cartItem.quantity / 2 to 2 * cartItem.quantity).toList.map(i => (i.toString, i.toString)),
        Full(cartItem.quantity.toString),
        v => {
          cartItem.quantity = v.toInt
          JsCmds.SetHtml("totalAmount", Text(sessionCart.totalAmount.toString))
        })
      val removeXhtml = link("remove", () => cartItemSessionVar(cartItem), Text("Remove " + cartItem.product.name + " Cart Item"))

      bind("cartItem", xhtml,
        "productName" -> productNameXhtml,
        "quantity" -> quantityXhtml,
        "remove" -> removeXhtml)
    })
  }

  def baseWithFormForAdd(xhtml: NodeSeq): NodeSeq =
    baseWithFormFor("cart/add", <lift:cartSnippet.add form="post" eager_eval="true"/>)

  def add(xhtml: NodeSeq): NodeSeq = {
    var givenQuantity = ""

    def doQuantity(quantity: String) = {
      givenQuantity = quantity
    }

    def doSubmit() = {
      sessionCart.add(sessionProduct, givenQuantity.toInt)
      redirectTo("/cart/list")
    }

    val quantityXhtml = text("", doQuantity(_))
    val submitXhtml = submit("Add", doSubmit)

    bind("cart", xhtml,
      "quantity" -> quantityXhtml,
      "submit" -> submitXhtml)
  }

  def addProductText(xhtml: NodeSeq): NodeSeq = {

    val addXhtml = Text("Add " + sessionProduct.name)

    bind("cart", xhtml,
      "add" -> addXhtml)
  }

  def baseWithFormForRemove(xhtml: NodeSeq): NodeSeq =
    baseWithFormFor("cart/remove", <lift:cartSnippet.remove form="post" eager_eval="true"/>)

  def remove(xhtml: NodeSeq): NodeSeq = {

    def doSubmit() = {
      sessionCart.cartItems = for{
        cartItem <- sessionCart.cartItems
        if (cartItem.product.name != sessionCartItem.product.name)
      } yield cartItem
      redirectTo("list")
    }

    val submitXhtml = submit("Remove", doSubmit)

    bind("cart", xhtml,
      "submit" -> submitXhtml)
  }

  def totalAmount(xhtml: NodeSeq): NodeSeq = {

    val totalAmountXhtml =
    <span id="totalAmount">
      {sessionCart.totalAmount.toString}
    </span>

    bind("cart", xhtml,
      "totalAmount" -> totalAmountXhtml)
  }

  def baseWithSnippetForCheckOut(xhtml: NodeSeq): NodeSeq =
    baseWithSnippetFor("cart/checkOut", <lift:cartSnippet.checkOut form="post" eager_eval="true"/>)

  def checkOut(xhtml: NodeSeq): NodeSeq = {
    val checkOutXhtml = if (sessionLoggedInNickName != "") {
      try {
        CheckOutService.checkOut((sessionUser.name, for{
          CartItem(product, quantity) <- sessionCart.cartItems
        } yield (product.name, quantity)))
        cartSessionVar(Cart(List[CartItem]()))
        Text("Cart Checked Out")
      } catch {
        case e: CheckOutProductsNotInStockException => error(e.getMessage()); redirectTo("/cart/list")
        case e: CheckOutUserNotEnoughFundsException => error(e.getMessage()); redirectTo("/user/list")
      }
    } else {
      error("No user logged in")
      redirectTo("/user/list")
    }
    bind("cart", xhtml,
      "checkOut" -> checkOutXhtml)
  }

}

}

}