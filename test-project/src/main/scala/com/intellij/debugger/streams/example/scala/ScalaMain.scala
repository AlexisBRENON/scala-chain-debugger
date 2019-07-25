package com.intellij.debugger.streams.example.scala

object ScalaMain {
  def main(args: Array[String]): Unit = {
    val actual = (1 to 5)
      .map(x => x * x)
      .filter(_ % 2 == 0)
      .zipWithIndex
      .flatMap(t => Seq.fill(t._1)(t._2)).sum
    println(actual)
  }
}
