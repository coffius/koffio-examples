package io.koff.examples.variance.example.impl

import io.koff.examples.variance.example.EventHandler
import io.koff.examples.variance.example.model.ChangeWarePriceEvent


/**
  * Notifies users about discounts (the new price of a ware is less than the old one)
  */
class DiscountEventHandler extends EventHandler[ChangeWarePriceEvent]{
  override def handle(event: ChangeWarePriceEvent): Unit = {
    if(event.oldPrice > event.newPrice) {
      val discount = 1 - event.newPrice / event.oldPrice
      println(s"there is a discount[${discount * 100} %] for the ware[id: ${event.wareId}]")
    }
  }
}

/* In this example we don't need more than one copy of the handler */
object DiscountEventHandler extends DiscountEventHandler