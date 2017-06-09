val root = (project in file("."))
  .enablePlugins(AppPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "akka-stream-monkeys",
    // note: this requires an initialized git repo
    version := {
      try {
        "git rev-parse --short HEAD".!!.trim
      } catch {
        case _: Throwable => "0.1-SNAPSHOT"
      }
    },

    libraryDependencies := {
      val akka = {
        def akkaModule(name: String, version: String = "2.5.2") =
          "com.typesafe.akka" %% s"akka-$name" % version

        Seq(
          akkaModule("stream"),
          akkaModule("stream-testkit") % "test"
        )
      }

      val testing = {
        def specs2Module(name: String, version: String = "3.8.8") =
          "org.specs2" %% s"specs2-$name" % version % "test"

        Seq(
          specs2Module("core"),
          "org.scalamock" %% "scalamock-specs2-support" % "3.5.0" % "test" exclude("org.specs2", "specs2_2.12"),
          "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
        )
      }

      akka ++ testing
    }
  )
