import java.time.LocalDate

case class Species(name: String) extends AnyVal {
  override def toString = name
}

object Species {
  val Gorilla = Species("gorilla")
  val Chimp = Species("chimp")
  val Baboon = Species("baboon")
  val Marmoset = Species("marmoset")
  val Tamarin = Species("tamarin")
  val Capuchin = Species("capuchin")
  val SpiderMonkey = Species("spider monkey")
  val Howler = Species("howler")
  val Macaque = Species("macaque")
  val ProboscisMonkey = Species("proboscis monkey")
}

sealed trait Gender
case object Male extends Gender
case object Female extends Gender

trait Monkey {
  def name: String
  def species: Species
  def gender: Gender
  def dob: LocalDate
  def dod: Option[LocalDate]

  def died(dod: LocalDate): Monkey
}

object Monkey {
  def apply(
    name: String,
    species: Species,
    gender: Gender,
    dob: LocalDate,
    dod: Option[LocalDate] = None
  ): Monkey = MonkeyImpl(name, species, gender, dob, dod)
}

private case class MonkeyImpl(
  name: String,
  species: Species,
  gender: Gender,
  dob: LocalDate,
  dod: Option[LocalDate]
) extends Monkey {
  override def died(dateOfDeath: LocalDate): Monkey =
    dod match {
      case Some(_) => throw new RuntimeException("What is dead may never die!")
      case None => copy(dod = Some(dateOfDeath))
    }

}
