package terminal

object Main {
  def main(args: Array[String]): Unit = {
    // Breakpoint!
    Seq(1, 2, 3).forall(_ % 2 == 0)
  }
}
