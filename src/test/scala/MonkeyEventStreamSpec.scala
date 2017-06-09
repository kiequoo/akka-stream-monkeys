import java.time.LocalDate

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.testkit.{ImplicitSender, TestKit}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.SpecificationLike
import org.specs2.specification.Scope
import Species._
import org.specs2.matcher.FutureMatchers

class MonkeyEventStreamSpec(implicit ee: ExecutionEnv)
  extends TestKit(ActorSystem()) with ImplicitSender with SpecificationLike with FutureMatchers {

  "Monkey Pipeline" should {
    "process the events, populating the repo" in new TestScope {

      implicit val materializer = ActorMaterializer()

      val pipeline =
        MonkeyRunnableGraph(source, monkeyRepo, parallelism = 8)

      val t0 = System.currentTimeMillis()

      (for {
        _ <- pipeline.run()
        res <- monkeyRepo.monkeys
      } yield {
        val t1 = System.currentTimeMillis()
        println("Elapsed time: " + (t1 - t0) + "ms")

        res.to[List].map(_._2) mustEqual List(
          Monkey("Pratibha",Baboon,Female,LocalDate.parse("1980-06-24")),
          Monkey("Christine",Marmoset,Female,LocalDate.parse("1989-02-03")),
          Monkey("Lalitha",Tamarin,Female,LocalDate.parse("1990-08-14")),
          Monkey("Varghese",Gorilla,Male,LocalDate.parse("1978-02-03"),Some(LocalDate.parse("1983-04-22"))),
          Monkey("Duygu",SpiderMonkey,Female,LocalDate.parse("1995-06-17")),
          Monkey("Sedef",Marmoset,Male,LocalDate.parse("1987-01-01")),
          Monkey("Ramesh",ProboscisMonkey,Male,LocalDate.parse("1993-05-16"),Some(LocalDate.parse("1994-11-12"))),
          Monkey("Cronus",Howler,Male,LocalDate.parse("1996-08-07")),
          Monkey("Karlene",Gorilla,Male,LocalDate.parse("1984-04-12")),
          Monkey("Farrah",Howler,Male,LocalDate.parse("1995-03-05")),
          Monkey("Rachel",Baboon,Female,LocalDate.parse("1980-07-03")),
          Monkey("Elodia",Capuchin,Female,LocalDate.parse("1992-06-12")),
          Monkey("Sokratis",Chimp,Male,LocalDate.parse("1982-03-23"),Some(LocalDate.parse("1997-10-31"))),
          Monkey("Jojo",Chimp,Female,LocalDate.parse("1981-12-16")),
          Monkey("Rufino",Chimp,Male,LocalDate.parse("1979-04-01")),
          Monkey("Dubravka",Chimp,Female,LocalDate.parse("1985-08-26"))
        )
      }).await
    }
  }

  trait TestScope extends Scope {
    val monkeyRepo = new MonkeyRepo

    val source = Source(
      List(
        Born("Varghese", Gorilla, Male, LocalDate.parse("1978-02-03")),
        Born("Rufino", Chimp, Male, LocalDate.parse("1979-04-01")),
        Born("Pratibha", Baboon, Female, LocalDate.parse("1980-06-24")),
        Born("Rachel", Baboon, Female, LocalDate.parse("1980-07-03")),
        Born("Jojo", Chimp, Female, LocalDate.parse("1981-12-16")),
        Born("Sokratis", Chimp, Male, LocalDate.parse("1982-03-23")),
        Died("Varghese", LocalDate.parse("1983-04-22")),
        Born("Karlene", Gorilla, Male, LocalDate.parse("1984-04-12")),
        Born("Dubravka", Chimp, Female, LocalDate.parse("1985-08-26")),
        Born("Sedef", Marmoset, Male, LocalDate.parse("1987-01-01")),
        Born("Christine", Marmoset, Female, LocalDate.parse("1989-02-03")),
        Born("Lalitha", Tamarin, Female, LocalDate.parse("1990-08-14")),
        Born("Elodia", Capuchin, Female, LocalDate.parse("1992-06-12")),
        Born("Ramesh", ProboscisMonkey, Male, LocalDate.parse("1993-05-16")),
        Died("Ramesh", LocalDate.parse("1994-11-12")),
        Born("Farrah", Howler, Male, LocalDate.parse("1995-03-05")),
        Born("Duygu", SpiderMonkey, Female, LocalDate.parse("1995-06-17")),
        Born("Cronus", Howler, Male, LocalDate.parse("1996-08-07")),
        Died("Sokratis", LocalDate.parse("1997-10-31"))
      )
    )
  }
}
