import scala.collection.concurrent.TrieMap
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class MonkeyRepo {
  private val latency = 10

  private val _monkeys = TrieMap[Species, Int]()

  def getMonkeyCount(name: Species): Future[Int] = Future {
    println(s"Getting count for $name")
    Thread.sleep(latency)
    _monkeys.getOrElse(name, 0)
  }

  def setMonkeyCount(name: Species, count: Int): Future[Unit] = Future {
    println(s"Setting count for $name to $count")
    Thread.sleep(latency)
    _monkeys += (name -> count)
  }

  def monkeys: Future[Map[Species, Int]] = Future {
    Thread.sleep(latency)
    _monkeys.toMap
  }
}
