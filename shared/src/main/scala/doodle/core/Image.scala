package doodle.core

import doodle.backend.Canvas

sealed trait Image {
  
  /*val boundingBox: BoundingBox = this match {    
      //case Rectangle(width, height) => BoundingBox(-width/2, height/2, width/2, -height/2)
      case Circle(r) => BoundingBox(-r, r, r, -r)
      case Rectangle(w, h) => BoundingBox(-w/2, h/2, w/2, -h/2)
      case Above(a, b) => a.boundingBox above b.boundingBox
      case On(o, u) => o.boundingBox on u.boundingBox
      case Beside(l, r) => l.boundingBox beside r.boundingBox
  }*/
  
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
    
      val boundingBox: BoundingBox =
    BoundingBox(this)
    
    def draw(canvas: Canvas): Unit = draw(canvas, DrawingContext.whiteLines, Vec.zero)
    
     def draw(canvas: Canvas, context: DrawingContext, origin: Vec): Unit = {
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
     
     case Rectangle(w,h) =>  
       canvas.rectangle(origin.x - w/2, origin.y + h/2, w, h)
       doStrokeAndFill()
     
     case a @ Above(t, b) =>
        val box = a.boundingBox
        val tBox = t.boundingBox
        val bBox = b.boundingBox

        val tOriginY = origin.y + box.topCoordinate - (tBox.height / 2)
        val bOriginY = origin.y + box.bottomCoordinate + (bBox.height / 2)

        t.draw(canvas, context, Vec(origin.x, tOriginY))
        b.draw(canvas, context, Vec(origin.x, bOriginY))
        
     case On(o, u) => 
        u.draw(canvas, context, origin)
        o.draw(canvas, context, origin)     
        
     case b @ Beside(l, r) =>
        val box = b.boundingBox
        val lBox = l.boundingBox
        val rBox = r.boundingBox

        val lOriginX = origin.x + box.leftCoordinate  + (lBox.width / 2)
        val rOriginX = origin.x + box.rightCoordinate - (rBox.width / 2)

        l.draw(canvas, context, Vec(lOriginX, origin.y))
        r.draw(canvas, context, Vec(rOriginX, origin.y))
       
     case ContextTransform(f, i) => i.draw(canvas, f(context), origin)
    }
}
}
final case class Circle(radius: Double) extends Image
final case class Rectangle(width: Double, height: Double) extends Image
final case class Above(above: Image, below: Image) extends Image
final case class Beside(left: Image, right: Image) extends Image
final case class On(top: Image, bottom: Image) extends Image
final case class ContextTransform(f: DrawingContext => DrawingContext, image: Image) extends Image
