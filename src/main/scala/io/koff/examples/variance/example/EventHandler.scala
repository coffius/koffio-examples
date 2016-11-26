package io.koff.examples.variance.example

import io.koff.examples.variance.example.model.Event

/**
  * Basic trait for an event handler
  */
trait EventHandler[-T <: Event] {
  /**
    * Handle a particular event
    * @param event an event to handle
    */
  def handle(event: T): Unit
}