package com.intellij.debugger.streams.scala

object Main {
  def product(x: Int)(implicit y: Int): Int = {
    x * y
  }

  def main(args: Array[String]): Unit = {
    implicit val factor: Int = 4
    println("Test frame")
  }
}
