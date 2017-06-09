import akka.NotUsed
import akka.stream.scaladsl.Flow

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MonkeyCountFlow {
  def apply(repo: MonkeyRepo): Flow[SpeciesEvent, Unit, NotUsed] =
    Flow[SpeciesEvent].mapAsync(1) { ev =>
      for {
        count <- repo.getMonkeyCount(ev.species)
        updatedCount <- Future.successful {
          ev match {
            case _: Born => count + 1
            case _: Died => count - 1
          }
        }
        _ <- repo.setMonkeyCount(ev.species, updatedCount)
      } yield ()
    }
}
