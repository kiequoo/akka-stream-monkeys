import akka.NotUsed
import akka.routing.ConsistentHash
import akka.stream.scaladsl.Flow

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object EventApplier {
  def apply(monkey: Option[Monkey], ev: Event): Monkey =
    (monkey, ev) match {
      case (None, Born(name, species, gender, dob)) =>
        Monkey(name, species, gender, dob)
      case (Some(m), _: Born) =>
        throw new RuntimeException(s"We already have a monkey called ${m.name}")
      case (Some(m), Died(_, dod)) =>
        m.died(dod)
      case (None, Died(name, _)) =>
        throw new RuntimeException(s"No monkey called $name exists")
    }
}

object MonkeyUpdateFlow {
  def apply(repo: MonkeyRepo, parallelism: Int = 1): Flow[Event, (Event, Monkey), NotUsed] = {
    val hash = ConsistentHash(0 until parallelism, 1)
    Flow[Event]
      .groupBy(parallelism, ev => hash.nodeFor(ev.name))
      .mapAsync(1) { ev =>
        for {
          monkey <- repo.getMonkey(ev.name)
          updatedMonkey <- Future.successful(EventApplier(monkey, ev))
          _ <- repo.setMonkey(updatedMonkey)
        } yield (ev, updatedMonkey)
      }
      .mergeSubstreams
  }
}
