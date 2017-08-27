package net.gesn.models

import org.joda.time.DateTime

trait Login {
  def uuid: Option[Long]
  def steamId: String
  def ip: String
  def loginTime: DateTime
  def successful: Boolean
}
