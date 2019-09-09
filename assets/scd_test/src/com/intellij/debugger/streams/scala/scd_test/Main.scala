package com.intellij.debugger.streams.scala.scd_test

object Main {
  def main(args: Array[String]): Unit = {
    val it = (0 to 5)
    val foo = it
      .map(_ * 2)
      .filter( _ % 3 == 0)
      .sum
    println(foo)
  }

}
