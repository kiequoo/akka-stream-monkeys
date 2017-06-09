import akka.routing.ConsistentHash
import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}

import scala.concurrent.Future

object MonkeyRunnableGraph {
  def apply(
    source: Source[SpeciesEvent, NotUsed],
    repo: MonkeyRepo,
    parallelism: Int = 1
  ): RunnableGraph[Future[Done]] = {
    val hash = ConsistentHash(0 until parallelism, 1)

    source
      .groupBy(parallelism, ev => hash.nodeFor(ev.species.toString))
      .via(MonkeyCountFlow(repo))
      .mergeSubstreams
      .toMat(
        Sink.ignore
      )(Keep.right)
  }
}
