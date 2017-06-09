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

        res mustEqual Map(
          SpiderMonkey -> 1,
          Gorilla -> 1,
          Chimp -> 3,
          Baboon -> 2,
          Howler -> 2,
          Marmoset -> 2,
          Capuchin -> 1,
          Tamarin -> 1
        )
      }).await
    }
  }

  trait TestScope extends Scope {
    val monkeyRepo = new MonkeyRepo

    val source = Source(
      List(
        Born(Gorilla),
        Born(Chimp),
        Born(Baboon),
        Born(Baboon),
        Born(Chimp),
        Born(Chimp),
        Died(Gorilla),
        Born(Gorilla),
        Born(Chimp),
        Born(Marmoset),
        Born(Marmoset),
        Born(Tamarin),
        Born(Capuchin),
        Born(ProboscisMonkey),
        Died(ProboscisMonkey),
        Born(Howler),
        Born(SpiderMonkey),
        Born(Howler),
        Died(Chimp)
      )
    )
  }
}
