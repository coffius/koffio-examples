package io.koff.examples.variance.example.impl

import io.koff.examples.variance.example.EventHandler
import io.koff.examples.variance.example.model.UpdateEvent

/**
  * Drops the cached value for the updated entity
  */
class DropCacheEventHandler extends EventHandler[UpdateEvent]{
  override def handle(event: UpdateEvent): Unit = {
    //Of course there is no real cache =^ __ ^=
    println(s"cached value for [UUID: ${event.entityId}] has been dropped")
  }
}

/* In this example we don't need more than one copy of the handler */
object DropCacheEventHandler extends DropCacheEventHandler