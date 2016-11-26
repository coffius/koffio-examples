package io.koff.examples.variance.example

import io.koff.examples.variance.example.model.Event

import scala.reflect.ClassTag

/**
  * Contains list of handlers
  */
trait HandlerRegister {
  /**
    * Adds a handler for a particular type of events
    * @param eventType the class of events handled by the eventHandler
    * @param eventHandler the event handler which should handle all events of the specific type(eventType)
    * @tparam E type of the event we want to handle
    * @tparam T intermediate type.
    *           Means that we want to handle an event of type `E` as `T`.
    *           And `T` should be between Event and `E` in the chain of inheritance.
    *           If we have such types: `Event <- SubEventType <- SubSubEventType <- ... <- E`
    *           then `T` can be one of these types
    * @tparam H type of the handler which should handle all events of type `E`
    */
  def addHandler[E <: Event, T >: E <: Event, H <: EventHandler[T]](eventType: Class[E], eventHandler: H): Unit

  /**
    * Transfer an event to all handlers which were registered to handle events of type `E`
    * @param event event to handle
    */
  def handleEvent[E <: Event : ClassTag](event: E): Unit
}
