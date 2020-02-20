package com.spotify.elitzur.validators

import com.spotify.elitzur.types.Owner


// Companion trait used to implicitly find companions for validation types and reduce the amount
// of reflection being done
private[elitzur] trait CompanionImplicit[T, A, C <: BaseCompanion[T, A]] extends Serializable {
  def companion: C
}

//scalastyle:off line.size.limit
case class SimpleCompanionImplicit[T, A <: com.spotify.elitzur.validators.BaseValidationType[T]](companion: BaseCompanion[T, A])
  extends CompanionImplicit[T, A, BaseCompanion[T, A]]

case class DynamicCompanionImplicit[T, U, A <: DynamicValidationType[T, U, A]](companion: DynamicCompanion[T, U, A])
  extends CompanionImplicit[T, A, DynamicCompanion[T, U, A]]
//scalastyle:on line.size.limit

trait BaseCompanion[T, A] extends Serializable {
  def validationType: String
  def bigQueryType: String
  def apply(data: T): A
  def parse(data: T): A
  def owner: Owner
  def description: String
}

//scalastyle:off line.size.limit
trait DynamicCompanion[T, U, A <: DynamicValidationType[T, U, A]]
  extends BaseCompanion[T, A] {
//scalastyle:on line.size.limit
  def setArg(i: A, a: U): A = i.setArg(a)
  def parseWithArg(data: T, arg: U): A
  def apply(data: T, arg: U): A =
    parseWithArg(data, arg)
}
