lazy val root = (project in file(".")).
  settings(
    name := "koffio-examples",
    version := "0.0.1",
    scalaVersion := "2.11.6"
  )

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.3"