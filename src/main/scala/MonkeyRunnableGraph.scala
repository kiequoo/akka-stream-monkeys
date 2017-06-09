import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Keep, RunnableGraph, Source}

import scala.concurrent.Future

object MonkeyRunnableGraph {
  def apply(
    source: Source[Event, NotUsed],
    repo: MonkeyRepo,
    omdi: OMDI,
    parallelism: Int = 1
  ): RunnableGraph[Future[Done]] = {
    source
      .via(MonkeyUpdateFlow(repo, parallelism))
      .toMat(OMDIReportingSink(omdi))(Keep.right)
  }
}
