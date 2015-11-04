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

trait Observer[A] {
  def observe(in: A): Unit
}

/*
 * 
 *                                      --------> EventStream
 * X -------> EventStream ------->  EventStream ...
 *                                      --------> EventStream
 *                                      
 *                                      
 * A----\
 *       \
 *        ----> Joined[(A,B)]
 * B ----/
 */

sealed trait EventStream[A] {
  val observers: mutable.ListBuffer[Observer[A]] = new mutable.ListBuffer()
  
  def process(value: A): Unit =
    observers.foreach(_.observe(value))
  
  def addObserver(obs: Observer[A]) = {
    observers+=obs
    obs
  }
  
  def map[B](f: A => B): EventStream[B] = {
    val observer = Map(f)
    addObserver(observer)
    observer
  }  
  
    def join[B](that: EventStream[B]): EventStream[(A,B)] = {
      val node = Join(this,that)
      this.addObserver(node.leftObserver)
      that.addObserver(node.rightObserver)
      node
    }


  /*
  def join[B](that: EventStream[B]): EventStream[(A,B)] = {
    val stream = Join(this,that)
    this.addObserver(stream.left)
    that.addObserver(stream.right)
    stream
  }

  def foldP[B](seed: B)(f: (A, B) => B): EventStream[B] =
    addObserver(new FoldP(seed,f))
    */
}
//final case class Map[A,B](source: EventStream[A], f: A => B) extends EventStream[B]
final case class Map[A, B](f: A => B) extends Observer[A] with EventStream[B]{
  def observe(value: A) = process(f(value))
}

final case class Join[A,B](left: EventStream[A], right: EventStream[B]) extends EventStream[(A,B)]{
	var leftValue: Option[A] = None
	var rightValue: Option[B] = None

  val leftObserver: Observer[A] = new Observer[A]{
    def observe(value: A){
      leftValue = Some(value)
      send
    }
  }
  val rightObserver: Observer[B] = new Observer[B]{
    def observe(value: B){
      rightValue = Some(value)      
      send
    }
  }
  private def send() = {
    if (leftValue.isDefined && rightValue.isDefined) 
      process( (leftValue.get,rightValue.get) )
      
//    if(leftValue.isDefined){
//      val left = leftValue.get
//      if(rightValue.isDefined){
//        val right = rightValue.get
//        process( (left, right) )
//      }
//    }  
//    
//    leftValue.map{left =>
//      rightValue.map{right =>
//        process( (left,right) )  
//      }
//    }
//    
//    for{
//      left <- leftValue
//      right <- rightValue
//    } process( (left, right) )    
  }
}

//final case class FoldP[A,B](source: EventStream[A], seed: B, f: (A, B) => B) extends EventStream[B]
  
 /* final case class FoldP[A,B](source: EventStream[A], seed: B, f: (A, B) => B) extends EventStream[B] {
    def key(value: A): Unit = {
      val newSeed = f(value,seed)
      //seed = newSeed
    }
  }
 */
final case class Source[A]() extends EventStream[A]
