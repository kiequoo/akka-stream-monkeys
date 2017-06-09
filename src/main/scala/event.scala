import java.time.LocalDate

sealed trait Event {
  def name: String
}

case class Born(name: String, species: Species, gender: Gender, dob: LocalDate = LocalDate.now) extends Event
case class Died(name: String, dod: LocalDate = LocalDate.now) extends Event
