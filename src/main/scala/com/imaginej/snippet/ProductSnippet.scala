package com.imaginej {

package snippet {

import xml.{Text, NodeSeq}
import net.liftweb.common.Empty
import net.liftweb.http.{SessionVar, RequestVar, S, SHtml}
import net.liftweb.util.Helpers

import Helpers._
import S._
import SHtml._

import domain.grocery._
import model._

object productRequestVar extends RequestVar(new ProductEntity)

object productSessionVar extends SessionVar(new ProductEntity)

object cartSessionVar extends SessionVar(Cart(List[CartItem]()))

class ProductSnippet {
  def sessionCategory = categorySessionVar.is

  def requestProduct = productRequestVar.is

  def sessionProduct = productSessionVar.is

  def list(xhtml: NodeSeq): NodeSeq = {
    val sessionCategoryName = sessionCategory.name
    for{
      product@(ProductEntity(_, _, _, CategoryEntity(`sessionCategoryName`, _))) <- ProductStore.retrieveAll
      nameXhtml = Text(product.name)
      quantityInStockXhtml = Text(product.quantityInStock.toString)
      priceXhtml = Text(product.price.toString)
      editXhtml = link("edit", () => productSessionVar(product), Text("Edit " + product.name + " Product"))
      transferXhtml = link("transfer", () => productSessionVar(product), Text("Transfer " + product.name + " Product"))
      addToCartXhtml = link("/cart/add", () => productSessionVar(product), Text("Add " + product.name + " Product To Cart"))
      node <- bind(
        "product", xhtml,
        "name" -> nameXhtml,
        "quantityInStock" -> quantityInStockXhtml,
        "price" -> priceXhtml,
        "edit" -> editXhtml,
        "transfer" -> transferXhtml,
        "addToCart" -> addToCartXhtml)
    } yield node
  }

  def add(xhtml: NodeSeq): NodeSeq = {

    def doName(productName: String) = {
      requestProduct.name = productName
    }

    def doSubmit() = {
      if (requestProduct.name.length == 0) {
        error("Product name cannot be blank")
      } else {
        ProductStore.retrieveByName(requestProduct.name) match {
          case None => ProductStore.create((requestProduct.name, sessionCategory.name))
          case _ => error("Product " + requestProduct.name + " of category " + sessionCategory.name + " already exists")
        }
        redirectTo("list")
      }
    }

    val nameXhtml = text("", doName(_))
    val submitXhtml = submit("Add", doSubmit)

    bind(
      "product", xhtml,
      "name" -> nameXhtml,
      "submit" -> submitXhtml)
  }

  def edit(xhtml: NodeSeq): NodeSeq = {

    def doQuantityInStock(productQuantityInStock: String) = {
      sessionProduct.quantityInStock = productQuantityInStock.toInt
    }

    def doPrice(productPrice: String) {
      sessionProduct.price = productPrice.toDouble
    }

    def doSubmit() = {
      ProductStore.update((sessionProduct.name, sessionProduct.quantityInStock, sessionProduct.price))
      redirectTo("list")
    }

    val quantityInStockXhtml = text(sessionProduct.quantityInStock.toString, doQuantityInStock(_))
    val priceXhtml = text(sessionProduct.price.toString, doPrice(_))
    val submitXhtml = submit("Edit", doSubmit)

    bind(
      "product", xhtml,
      "quantityInStock" -> quantityInStockXhtml,
      "price" -> priceXhtml,
      "submit" -> submitXhtml)
  }

  def transfer(xhtml: NodeSeq): NodeSeq = {

    val fromProductCategoryName = sessionProduct.category.name

    def doCategory(productCategoryName: String) = {
      sessionProduct.category.name = productCategoryName
    }

    def doSubmit() = {
      ProductService.transfer((sessionProduct.name, fromProductCategoryName, sessionProduct.category.name))
      redirectTo("list")
    }

    val categoryXhtml = {
      val categoryOptions = for{
        category <- CategoryStore.retrieveAll
        if (category.name != sessionProduct.category.name)
      } yield (category.name, category.name)

      select(categoryOptions, Empty, doCategory(_))
    }
    val submitXhtml = submit("Transfer", doSubmit)

    bind(
      "product", xhtml,
      "category" -> categoryXhtml,
      "submit" -> submitXhtml)
  }

}

}

}