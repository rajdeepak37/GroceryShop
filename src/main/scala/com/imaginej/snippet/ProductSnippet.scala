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

import I10N.i10n

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
      editXhtml = link("edit", () => productSessionVar(product), Text(i10n("Product_EditProduct", product.name)))
      transferXhtml = link("transfer", () => productSessionVar(product), Text(i10n("Product_TransferProduct", product.name)))
      addToCartXhtml = link("/cart/add", () => productSessionVar(product), Text(i10n("Product_AddProductToCart", product.name)))
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
        error(?("Product_Error_ProductNameCannotBeBlank"))
      } else {
        ProductStore.retrieveByName(requestProduct.name) match {
          case None => ProductStore.create((requestProduct.name, sessionCategory.name))
          case _ => error(i10n("Product_Error_ProductAlreadyExists", requestProduct.name, sessionCategory.name))
        }
        redirectTo("list")
      }
    }

    val nameXhtml = text("", doName(_))
    val submitXhtml = submit(?("Product_Add"), doSubmit)

    bind(
      "product", xhtml,
      "name" -> nameXhtml,
      "submit" -> submitXhtml)
  }

  def edit(xhtml: NodeSeq): NodeSeq = {

    def doQuantityInStock(productQuantityInStock: String) = {
      try {
        sessionProduct.quantityInStock = productQuantityInStock.toInt
      } catch {
        case e: NumberFormatException => error(i10n("Product_Error_IntFormatException", productQuantityInStock))
      }
    }

    def doPrice(productPrice: String) {
      try {
        sessionProduct.price = productPrice.toDouble
      } catch {
        case e: NumberFormatException => error(i10n("Product_Error_DoubleFormatException", productPrice))
      }
    }

    def doSubmit() = {
      ProductStore.update((sessionProduct.name, sessionProduct.quantityInStock, sessionProduct.price))
      redirectTo("list")
    }

    val quantityInStockXhtml = text(sessionProduct.quantityInStock.toString, doQuantityInStock(_))
    val priceXhtml = text(sessionProduct.price.toString, doPrice(_))
    val submitXhtml = submit(?("Product_Edit"), doSubmit)

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
    val submitXhtml = submit(?("Product_Transfer"), doSubmit)

    bind(
      "product", xhtml,
      "category" -> categoryXhtml,
      "submit" -> submitXhtml)
  }

  def productListText(xhtml: NodeSeq): NodeSeq = {

    val listXhtml = Text(i10n("Product_ProductList", sessionCategory.name))

    bind(
      "product", xhtml,
      "list" -> listXhtml)
  }

  def addProductText(xhtml: NodeSeq): NodeSeq = {

    val addXhtml = Text(i10n("Product_AddProduct", sessionCategory.name))

    bind(
      "product", xhtml,
      "add" -> addXhtml)
  }

}

}

}