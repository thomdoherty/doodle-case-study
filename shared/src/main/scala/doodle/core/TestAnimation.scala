package doodle.core

import doodle.backend.Key
import doodle.core._
import doodle.event._
import doodle.jvm.Java2DCanvas

object TestAnimation extends App {
  
  val source = Source[Int]
  val tomatoSauce = source.map(_*2)
  val mintSauce = source.map(_.toString)
  
  val joinedSauce = tomatoSauce.join(mintSauce)
 // source.map(_*2).map(println)
  joinedSauce.map(println)
  
  //source.process(1)
  List(1,2,3,4).foreach(source.process)
  /*
  val canvas = Java2DCanvas.canvas
  canvas.setSize(600, 600)
  val redraw = canvas.animationFrameEventStream(canvas)
  val keys = canvas.keyDownEventStream(canvas)

  val velocity = keys.foldp(Vec.zero)((key, prev) => {
    val velocity =
      key match {
        case Key.Up    => prev + Vec(0, 1)
        case Key.Right => prev + Vec(1, 0)
        case Key.Down  => prev + Vec(0, -1)
        case Key.Left  => prev + Vec(-1, 0)
        case _         => prev
      }
    Vec(velocity.x.min(5).max(-5), velocity.y.min(5).max(-5))
  })

  val location = redraw.join(velocity).map { case (ts, m) => m }.
    foldp(Vec.zero) { (velocity, prev) =>
      val location = prev + velocity
      Vec(location.x.min(300).max(-300), location.y.min(300).max(-300))
    }
    */

}