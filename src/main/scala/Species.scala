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
