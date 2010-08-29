package com.imaginej {

package model {

import domain.grocery._

case class Cart(var cartItems: List[CartItem]) {
  def add(product: ProductEntity, quantity: Int) {
    val otherCartItems = for{
      cartItem <- cartItems
      if (cartItem.product.name != product.name)
    } yield cartItem
    val maybeSameCartItem = for{
      cartItem <- cartItems
      if (cartItem.product.name == product.name)
    } yield cartItem
    val newCartItem = maybeSameCartItem match {
      case Nil => CartItem(product, quantity)
      case cartItem :: _ => CartItem(product, cartItem.quantity + quantity)
    }
    cartItems = newCartItem :: otherCartItems
  }

  def totalAmount = (cartItems foldLeft (0.0))((total, cartItem) => total + cartItem.product.price * cartItem.quantity)
}

}

}