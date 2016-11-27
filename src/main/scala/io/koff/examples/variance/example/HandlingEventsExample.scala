package io.koff.examples.variance.example

import java.util.UUID

import io.koff.examples.variance.example.impl._
import io.koff.examples.variance.example.model._


object HandlingEventsExample {
  private val handlerRegister: HandlerRegister = new SimpleHandlerRegisterImpl

  handlerRegister.addHandler(classOf[ChangeUserPhoneEvent], ConsoleLogEventHandler)
  handlerRegister.addHandler(classOf[ChangeUserPhoneEvent], DropCacheEventHandler)
  handlerRegister.addHandler(classOf[ChangeUserPhoneEvent], CheckNumberEventHandler)

  handlerRegister.addHandler(classOf[ChangeWarePriceEvent], ConsoleLogEventHandler)
  // don't want to drop cache because of ware - let's say we don't have a cache for wares in our system
  // handlerRegister.addHandler(classOf[ChangeWarePriceEvent], DropCacheAfterUpdateEventHandler)
  handlerRegister.addHandler(classOf[ChangeWarePriceEvent], DiscountEventHandler)

  // but we can not handle ChangeUserPhoneEvent with DiscountEventHandler
  // handlerRegister.addHandler(classOf[ChangeUserPhoneEvent], DiscountEventHandler) // compilation error here
  // Also we have to define EventHandler[-T] in order to make possible to use general handlers (ConsoleLog*, DropCache*)
  // If we define the class as EventHandler[T] - we will have compile errors using addHandler(...)
  // For example here: handlerRegister.addHandler(classOf[ChangeUserPhoneEvent], ConsoleLogEventHandler)

  def main(args: Array[String]): Unit = {
    handlerRegister.handleEvent(ChangeWarePriceEvent(
      wareId = UUID.randomUUID(),
      oldPrice = 100,
      newPrice = 80
    ))

    handlerRegister.handleEvent(ChangeUserPhoneEvent(
      userId = UUID.randomUUID(),
      userEmail = "test@example.com",
      oldPhone = "+79998887766",
      newPhone = "+318745846985"
    ))
  }
}
