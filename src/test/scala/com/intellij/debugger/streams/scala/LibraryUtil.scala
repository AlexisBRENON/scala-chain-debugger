package com.intellij.debugger.streams.scala

import java.nio.file.FileSystems

case object LibraryUtil {
  val LIBRARIES_DIRECTORY: String = FileSystems.getDefault.getPath("lib").toAbsolutePath.toString

  val SCALA_STD_LIBRARY_JAR_NAME = "scala-library-2.12.7.jar"
}
