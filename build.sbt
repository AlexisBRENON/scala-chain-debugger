name := "ScalaChainDebugger"
version := "0.1"
scalaVersion := "2.12.7"

ideaPluginName in ThisBuild := name.value
ideaBuild in ThisBuild := "192.5728.98"

lazy val ScalaChainDebugger: sbt.Project = project.in(file("."))
  .settings(
    ideaInternalPlugins ++= Seq("stream-debugger", "java"),
    ideaExternalPlugins += IdeaPlugin.Id("Scala", "org.intellij.scala", None)
  )

lazy val ideaRunner = createRunnerProject(ScalaChainDebugger, "idea-runner")
