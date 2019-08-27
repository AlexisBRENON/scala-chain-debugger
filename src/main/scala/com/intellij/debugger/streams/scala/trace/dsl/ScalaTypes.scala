package com.intellij.debugger.streams.scala.trace.dsl

import com.intellij.debugger.streams.trace.dsl.Types
import com.intellij.debugger.streams.trace.impl.handler.`type`.{ArrayType, ClassTypeImpl, GenericType, ListType, ListTypeImpl, MapType}
import kotlin.jvm.functions


object ScalaTypes extends Types {
  override def getANY: GenericType = new ClassTypeImpl("scala.Any", "scala.Any")

  override def getBOOLEAN: GenericType = ???

  override def getDOUBLE: GenericType = ???

  override def getEXCEPTION: GenericType = ???

  override def getINT: GenericType = ???

  override def getLONG: GenericType = ???

  override def getSTRING: GenericType = ???

  override def getTIME: GenericType = ???

  override def getVOID: GenericType = ???

  override def array(genericType: GenericType): ArrayType = ???

  override def linkedMap(genericType: GenericType, genericType1: GenericType): MapType = ???

  override def list(elementType: GenericType): ListType =
    new ListTypeImpl(elementType,
      (e: String) => s"scala.collection.Traversable[$e]",
      "scala.collection.Traversable.empty()")

  override def map(genericType: GenericType, genericType1: GenericType): MapType = ???

  override def nullable(function1: functions.Function1[_ >: Types, _ <: GenericType]): GenericType = ???
}
