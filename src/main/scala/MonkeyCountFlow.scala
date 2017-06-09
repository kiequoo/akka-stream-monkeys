import akka.NotUsed
import akka.stream.scaladsl.Flow
import scala.concurrent.ExecutionContext.Implicits.global

object MonkeyCountFlow {
  def apply(repo: MonkeyRepo): Flow[SpeciesEvent, Unit, NotUsed] =
    Flow[SpeciesEvent].mapAsync(1) { ev =>
      repo
        .getMonkeyCount(ev.species)
        .map((ev, _))
    }.map {
      case (ev: Born, count) => (ev, count + 1)
      case (ev: Died, count) => (ev, count - 1)
    }.mapAsync(1) {
      case (ev, count) =>
        repo.setMonkeyCount(ev.species, count)
    }
}
