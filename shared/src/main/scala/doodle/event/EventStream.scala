package doodle.event

import scala.collection.mutable


//sealed trait EventStream[A] {
//  def map[B](f: A => B): EventStream[B] =
//    Map(this, f)
//
//  def join[B](that: EventStream[B]): EventStream[(A,B)] =
//    Join(this, that)
//
//  def scan[B](seed: B)(f: (A, B) => B): EventStream[B] =
//    Scan(this, seed, f)
//}
//final case class Map[A,B](source: EventStream[A], f: A => B) extends EventStream[B]
//final case class Join[A,B](left: EventStream[A], right: EventStream[B]) extends EventStream[(A,B)]
//final case class Scan[A,B](source: EventStream[A], seed: B, f: (A, B) => B) extends EventStream[B]
//final case class Source[A](callbackHandler: ((A => Unit)) => Unit) extends EventStream[A]

trait Stream[A] {
  def key(in: A): Unit
}

sealed trait EventStream[A] {
  val streams: mutable.ListBuffer[Stream[A]] = new mutable.ListBuffer()
  
  def process(stream: A): Unit =
    streams.foreach(_.key(stream))
  
  def addStream(stream: Stream[A]) = {
    streams+=stream
    stream
  }
  
  def map[B](f: A => B): EventStream[B] = {
    val stream = map(f)
    addStream(stream)
    stream
  }  

  def join[B](that: EventStream[B]): EventStream[(A,B)] = {
    val stream = Join(this,that)
    this.addStream(stream.left)
    that.addStream(stream.right)
    stream
  }

  def foldP[B](seed: B)(f: (A, B) => B): EventStream[B] =
    addStream(new FoldP(seed,f))
}
//final case class Map[A,B](source: EventStream[A], f: A => B) extends EventStream[B]
final case class Map[A, B](f: A => B) extends Stream[A] with EventStream[B]{
  def key(value: A) = process(f(value))
}

//final case class Join[A,B](left: EventStream[A], right: EventStream[B]) extends EventStream[(A,B)]

final case class Join[A, B](left: EventStream[A], right: EventStream[B]) extends EventStream[(A,B)] {
  var lastLeft: Option[A] = None //must be most recent value for each stream and can be none 
  var lastRight: Option[B] = None //must be most recent value for each stream and can be none
  val leftStream = new Stream[A]{ def key(value: A) = {lastLeft = Some(value) }
  }
//final case class FoldP[A,B](source: EventStream[A], seed: B, f: (A, B) => B) extends EventStream[B]
  
  final case class FoldP[A,B](source: EventStream[A], seed: B, f: (A, B) => B) extends EventStream[B] {
    def key(value: A): Unit = {
      val newSeed = f(value,seed)
      //seed = newSeed
    }
  }
  
final case class Source[A]() extends EventStream[A] { def push(value: A): Unit = addStream(value) }
}