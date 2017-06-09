sealed trait Event

sealed trait SpeciesEvent extends Event {
  def species: Species
}

case class Born(species: Species) extends SpeciesEvent
case class Died(species: Species) extends SpeciesEvent
