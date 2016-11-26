package io.koff.examples.variance.example.model

import java.util.UUID

/**
  * The very basic trait to describe an event
  */
trait Event

/*
 * For simplicity the whole hierarchy of events is here in one file
 */

/**
  * The marker trait for events for data updates
  */
trait UpdateEvent extends Event{
  /** Id of the updated entity */
  def entityId: UUID
}

/**
  * The marker trait for events about wares
  */
trait WareEvent extends Event {
  /**Id the ware which is a reason of the event */
  def wareId: UUID
}

/**
  * The marker trait for events about users
  */
trait UserEvent extends Event {
  /** Email of the user who is a reason of the event */
  def email: String
}

/*
 * Specific events
 */
case class ChangeWarePriceEvent(wareId: UUID,
                                oldPrice: BigDecimal,
                                newPrice: BigDecimal) extends WareEvent with UpdateEvent {
  override def entityId: UUID = wareId
}

case class ChangeUserPhoneEvent(userId: UUID, userEmail: String, oldPhone: String, newPhone: String) extends UserEvent with UpdateEvent {
  override def email: String = userEmail
  /* Let's say that a user email is an unique identifier of a user */
  override def entityId: UUID = userId
}