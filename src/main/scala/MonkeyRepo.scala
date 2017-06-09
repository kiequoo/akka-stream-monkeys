import scala.collection.concurrent.TrieMap
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class MonkeyRepo {

  private val _monkeys = TrieMap[Species, Int]()

  def getMonkeyCount(name: Species): Future[Int] = Future {
    println(s"Getting count for $name")
    _monkeys.getOrElse(name, 0)
  }

  def setMonkeyCount(name: Species, count: Int): Future[Unit] = Future {
    println(s"Setting count for $name to $count")
    _monkeys += (name -> count)
  }

  def monkeys: Future[Map[Species, Int]] = Future(_monkeys.toMap)
}
