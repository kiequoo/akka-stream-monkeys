import akka.NotUsed
import akka.stream.scaladsl.Flow

object MonkeyCountFlow {
  def apply(repo: MonkeyRepo): Flow[SpeciesEvent, Unit, NotUsed] =
    Flow[SpeciesEvent].map { ev =>
      (ev, repo.getMonkeyCount(ev.species))
    }.map {
      case (ev: Born, count) => (ev, count + 1)
      case (ev: Died, count) => (ev, count - 1)
    }.map {
      case (ev, count) =>
        repo.setMonkeyCount(ev.species, count)
    }
}
