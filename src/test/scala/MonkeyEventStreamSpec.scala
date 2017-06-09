import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
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

      val flow = MonkeyCountFlow(monkeyRepo)

      val pipeline =
        source
        .via(flow)
        .toMat(
          Sink.ignore
        )(Keep.right)

      val t0 = System.nanoTime()

      (for {
        _ <- pipeline.run()
        res <- monkeyRepo.monkeys
      } yield {
        val t1 = System.nanoTime()
        println("Elapsed time: " + ((t1 - t0) / 1000 / 1000) + "ms")

        res mustEqual Map(
          Gorilla -> 1,
          Chimp -> 4,
          Baboon -> 2
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
        Born(Chimp)
      )
    )
  }
}
