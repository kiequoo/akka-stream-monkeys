case class Species(name: String) extends AnyVal {
  override def toString = name
}

object Species {
  val Gorilla = Species("gorilla")
  val Chimp = Species("chimp")
  val Baboon = Species("baboon")
}
