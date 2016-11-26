package io.koff.examples.variance.example.impl

import io.koff.examples.variance.example.model.Event
import io.koff.examples.variance.example.{EventHandler, HandlerRegister}

import scala.collection.mutable
import scala.reflect.ClassTag
import scala.reflect.classTag

/**
  * The very simple thread-unsafe implementation of HandlerRegister using mutable.Map
  */
class SimpleHandlerRegisterImpl extends HandlerRegister {
  private val handlerMap = mutable.Map.empty[Class[_], Seq[EventHandler[_]]]

  override def addHandler[E <: Event, T >: E <: Event, H <: EventHandler[T]](eventType: Class[E], eventHandler: H): Unit = {
    val handlers = handlerMap.getOrElse(eventType, Seq.empty)
    val toSave = handlers :+ eventHandler
    handlerMap.put(eventType, toSave)
  }

  override def handleEvent[E <: Event : ClassTag](event: E): Unit = {
    val tag = classTag[E]
    val handlers = handlerMap.getOrElse(tag.runtimeClass, Seq.empty)
    handlers.foreach { handler =>
      handler.asInstanceOf[EventHandler[E]].handle(event)
    }
  }
}
