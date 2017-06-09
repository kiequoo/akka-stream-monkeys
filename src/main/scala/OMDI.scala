import akka.stream.scaladsl.Sink
import java.time.temporal.ChronoUnit

/* Open Monkey Death Interface */
trait OMDI {
  def reportDeath(species: Species, gender: Gender, age: Long)
}

object OMDIReportingSink {
  def apply(omdi: OMDI) =
    Sink.foreach[(Event, Monkey)] {
      case (_: Died, Monkey(_, species, gender, dob, Some(dod))) =>
        omdi.reportDeath(species, gender, ChronoUnit.YEARS.between(dob, dod))
      case _ =>
    }
}