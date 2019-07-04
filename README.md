# Scala stream debugger

> Debug iterables operation chains

## Evaluated expressions

#### ✔ Supported

#### Basic sequences

```scala
(1 to 5)
  .map(x => x*x)
  .filter(_%2 == 0)
  .zipWithIndex
  .flatMap(t => Seq.fill(t._1)(t._2))
  .foldLeft(0)(_+_)
```

#### For comprehensions

```scala
for(i <- 1 to 10; j <- 1 to 10) yield (i, j, i * j)

for(i <- 1 to 10 if i > 5; j <- 1 to 10 if j % 2 == 0) yield (i, j, i * j)
```

#### Maps

```scala
(1 to (10, 1))
  .map{i => (('a'+i-1).toChar, Seq.fill(i*i)(i))}
.toMap
  .map {
    case (k, l) if l.length % 2 == 0 => {
      (k, l.zipWithIndex.filter { _._2 % (k - 'a') == 0})
    }
    case x => x
    // case _ => _ // <-- This does not work
  }
```

#### Implicits

Frame:
```scala
def product(x: Int)(implicit y: Int): Int = ???
implicit val factor: Int = 4
```

Expression:
```scala
(0 to (5, 1)).map{product(_)(factor)}
```

#### Options, Try, Either

```scala
import scala.util.{Try, Success, Failure}
Some(true)
.map(!_)
.toRight(0)
.left.map(b => Try(b.toString.toInt))
.map(_ => Success(10))
.getOrElse(Failure(IllegalArgumentException))
```

### ✘ Unsupported

```scala
def product(x: Int)(implicit y: Int): Int = ???
implicit val factor: Int = 4

(0 to (5, 1)).map{product(_)(implicitly[Int])}

(0 to (5, 1)).map{product(_)}

// But this works
implicitly[Int] // Returns 4
```

