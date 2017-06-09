import scala.collection.concurrent.TrieMap
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class MonkeyRepo {
  private val latency = 10

  private val _monkeys = TrieMap.empty[String, Monkey]

  def getMonkey(name: String): Future[Option[Monkey]] = Future {
    Thread.sleep(latency)
    _monkeys.get(name)
  }

  def setMonkey(monkey: Monkey): Future[Unit] = Future {
    Thread.sleep(latency)
    _monkeys += (monkey.name -> monkey)
    ()
  }

  def monkeys: Future[Map[String, Monkey]] = Future {
    Thread.sleep(latency)
    _monkeys.toMap
  }
}
