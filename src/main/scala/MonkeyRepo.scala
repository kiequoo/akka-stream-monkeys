import scala.collection.concurrent.TrieMap
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class MonkeyRepo {
  private val latency = 10

  private val _monkeys = TrieMap[Species, Int]()

  def getMonkeyCount(name: Species): Future[Int] = Future {
    Thread.sleep(latency)
    _monkeys.getOrElse(name, 0)
  }

  def setMonkeyCount(name: Species, count: Int): Future[Unit] = Future {
    Thread.sleep(latency)
    count match {
      case 0 => _monkeys -= name
      case x => _monkeys += (name -> x)
    }
  }

  def monkeys: Future[Map[Species, Int]] = Future {
    Thread.sleep(latency)
    _monkeys.toMap
  }
}
