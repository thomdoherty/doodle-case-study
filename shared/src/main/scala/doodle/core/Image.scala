package doodle.core

import doodle.backend.Canvas

sealed trait Image {
  
  val boundingBox: BoundingBox = this match {    
      //case Rectangle(width, height) => BoundingBox(-width/2, height/2, width/2, -height/2)
      case Circle(r) => BoundingBox(-r, r, r, -r)
      case Rectangle(w, h) => BoundingBox(-w/2, h/2, w/2, -h/2)
      case Above(a, b) => a.boundingBox above b.boundingBox
      case On(o, u) => o.boundingBox on u.boundingBox
      case Beside(l, r) => l.boundingBox beside r.boundingBox
  }
  
  def above(below: Image): Image =
    Above(this, below)
    
  def below(top: Image): Image =
    Above(top, this)

  def beside(that: Image): Image =
    Beside(this, that)

  def on(bottom: Image): Image =
    On(this, bottom)
    
  def lineColor(color: Color): Image =
    ContextTransform(_.lineColor(color), this)

  def lineWidth(width: Double): Image =
    ContextTransform(_.lineWidth(width), this)

  def fillColor(color: Color): Image =
    ContextTransform(_.fillColor(color), this)

  /*def draw(canvas: Canvas): Unit = {
    // structural recursion
    // Set size of canvas based on bounding box coordinates
    // Then draw image on canvas
    val boundingBox: BoundingBox = BoundingBox(this) //This means we need apply method in bounding box that takes image
    draw(canvas, boundingBox.width, boundingBox.height)    
  }*/
    
    //def draw(canvas: Canvas): Unit =  draw(canvas, DrawingContext.whiteLines, Vec.zero)
  
  def draw(canvas: Canvas): Unit = this match {
     case Circle(r) => canvas.circle(0.0, 0.0, r)
     case Rectangle(w,h) => canvas.rectangle(-w/2, h/2, w/2, -h/2)
     case Above(a, b) => a.draw(canvas); b.draw(canvas)
     case On(o, u) => o.draw(canvas); u.draw(canvas)
     case Beside(l, r) => l.draw(canvas); r.draw(canvas)
     }

  // A helper method you will probably want
  //def draw(canvas: Canvas, originX: Double, originY: Double): Unit = {
    //canvas.setSize(width, height)
    //canvas.setOrigin(origin.x.ceil.toInt, origin.y.ceil.toInt)
    //Draw?
  /*  def draw(canvas: Canvas): Unit = {
      val context = DrawingContext.whiteLines
      val origin = Vec.zero
      
     def doStrokeAndFill() = {
      context.fill.foreach { fill =>
        canvas.setFill(fill.color)
        canvas.fill()
      }
      context.stroke.foreach { stroke =>
        canvas.setStroke(stroke)
        canvas.stroke()
      }
    }
      
      this match {
      case Circle(r) =>
        canvas.circle(origin.x, origin.y, r)
        doStrokeAndFill()

      case Rectangle(w, h) =>
        canvas.rectangle(origin.x - w/2, origin.y + h/2, w, h)
        doStrokeAndFill()

      }
   }*/
}
final case class Circle(radius: Double) extends Image
final case class Rectangle(width: Double, height: Double) extends Image
final case class Above(above: Image, below: Image) extends Image
final case class Beside(left: Image, right: Image) extends Image
final case class On(top: Image, bottom: Image) extends Image
final case class ContextTransform(f: DrawingContext => DrawingContext, image: Image) extends Image
