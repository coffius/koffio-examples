package io.koff.examples.variance.example.impl

import io.koff.examples.variance.example.EventHandler
import io.koff.examples.variance.example.model.Event

/**
  * Logs all events to stdout using .toString
  */
class ConsoleLogEventHandler extends EventHandler[Event]{
  override def handle(event: Event): Unit = {
    println(s"logger - event has been received: [$event]")
  }
}

/* In this example we don't need more than one copy of the handler */
object ConsoleLogEventHandler extends ConsoleLogEventHandler