import scala.collection.concurrent.TrieMap

class MonkeyRepo {
  
  private val _monkeys = TrieMap[Species, Int]()

  def getMonkeyCount(name: Species): Int = {
    println(s"Getting count for $name")
    _monkeys.getOrElse(name, 0)
  }

  def setMonkeyCount(name: Species, count: Int): Unit = {
    println(s"Setting count for $name to $count")
    _monkeys += (name -> count)
  }

  def monkeys: Map[Species, Int] = _monkeys.toMap
}
