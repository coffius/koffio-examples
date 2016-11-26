package io.koff.examples.variance.example.impl

import io.koff.examples.variance.example.EventHandler
import io.koff.examples.variance.example.model.ChangeUserPhoneEvent

/**
  * Sends SMS to the new phone number if a user change it.
  */
class CheckNumberEventHandler extends EventHandler[ChangeUserPhoneEvent]{
  override def handle(event: ChangeUserPhoneEvent): Unit = {
    println(s"SMS has been sent to the phone:${event.newPhone}")
  }
}

/* In this example we don't need more than one copy of the handler */
object CheckNumberEventHandler extends CheckNumberEventHandler
