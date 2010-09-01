package com.imaginej {

package snippet {

import xml.{Text, NodeSeq}

import net.liftweb.common.Full
import net.liftweb.http.{SessionVar, S, SHtml}
import net.liftweb.http.js.JsCmds
import net.liftweb.util.Helpers

import Helpers._
import S._
import SHtml._

import domain.grocery._
import model._

import I10N.i10n  

object cartItemSessionVar extends SessionVar(CartItem(new ProductEntity, 0))

class CartSnippet {
  def sessionCart = cartSessionVar.is

  def sessionProduct = productSessionVar.is

  def sessionUser = userSessionVar.is

  def sessionLoggedInNickName = loggedInNickNameSessionVar.is

  def sessionCartItem = cartItemSessionVar.is

  def list(xhtml: NodeSeq): NodeSeq = for{
    cartItem <- sessionCart.cartItems
    productNameXhtml = Text(cartItem.product.name)
    quantityXhtml = ajaxSelect(
      (cartItem.quantity / 2 to 2 * cartItem.quantity).toList.map(i => (i.toString, i.toString)),
      Full(cartItem.quantity.toString),
      value => {
        cartItem.quantity = value.toInt
        JsCmds.SetHtml("totalAmount", Text(sessionCart.totalAmount.toString))
      })
    removeXhtml = link("remove", () => cartItemSessionVar(cartItem), Text(i10n("Cart_RemoveCartItem", cartItem.product.name)))
    node <- bind(
      "cartItem", xhtml,
      "productName" -> productNameXhtml,
      "quantity" -> quantityXhtml,
      "remove" -> removeXhtml)
  } yield node

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
    val submitXhtml = submit(?("Cart_Add"), doSubmit)

    bind(
      "cart", xhtml,
      "quantity" -> quantityXhtml,
      "submit" -> submitXhtml)
  }

  def remove(xhtml: NodeSeq): NodeSeq = {

    def doSubmit() = {
      sessionCart.cartItems = for{
        cartItem <- sessionCart.cartItems
        if (cartItem.product.name != sessionCartItem.product.name)
      } yield cartItem
      redirectTo("list")
    }

    val submitXhtml = submit(?("Cart_Remove"), doSubmit)

    bind(
      "cart", xhtml,
      "submit" -> submitXhtml)
  }

  def totalAmount(xhtml: NodeSeq): NodeSeq = {

    val totalAmountXhtml =
    <span id="totalAmount">
      {sessionCart.totalAmount.toString}
    </span>

    bind(
      "cart", xhtml,
      "totalAmount" -> totalAmountXhtml)
  }

  def checkedOut(xhtml: NodeSeq): NodeSeq = {
    val checkedOutXhtml = if (sessionLoggedInNickName != "") {
      try {
        CheckOutService.checkOut((sessionUser.name, for{
          CartItem(product, quantity) <- sessionCart.cartItems
        } yield (product.name, quantity)))
        cartSessionVar(Cart(List[CartItem]()))
        Text(?("Cart_CartCheckedOut"))
      } catch {
        case e: CheckOutProductsNotInStockException => error(i10n(e.getMessage(), sessionProduct.name)); redirectTo("/cart/list")
        case e: CheckOutUserNotEnoughFundsException => error(i10n(e.getMessage(), sessionUser.name)); redirectTo("/user/list")
      }
    } else {
      error(?("Cart_Error_NoUserLoggedIn"))
      redirectTo("/user/list")
    }
    bind(
      "cart", xhtml,
      "checkedOut" -> checkedOutXhtml)
  }
  
  def addProductText(xhtml: NodeSeq): NodeSeq = {

    val addXhtml = Text(i10n("Cart_AddProduct", sessionProduct.name))

    bind(
      "cart", xhtml,
      "add" -> addXhtml)
  }

}

}

}