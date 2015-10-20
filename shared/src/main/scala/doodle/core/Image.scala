package doodle.core

sealed trait Image {
  def above(below: Image): Image =
    Above(this, below)
    
  def below(top: Image): Image =
    Above(top, this)

  def beside(that: Image): Image =
    Beside(this, that)

  def on(bottom: Image): Image =
    On(this, bottom)

  def draw(canvas: Canvas): Unit = {
    // structural recursion
    // Set size of canvas based on bounding box coordinates
    // Then draw image on canvas
    val boundingBox: BoundingBox = BoundingBox(this) //This means we need apply method in bounding box that takes image
    draw(canvas, boundingBox.width, boundingBox.height)
  }

  // A helper method you will probably want
  def draw(canvas: Canvas, originX: Double, originY: Double): Unit = {
    canvas.setSize(width, height)
    canvas.setOrigin(origin.x.ceil.toInt, origin.y.ceil.toInt)
    //Draw?
   }
}
final case class Circle(radius: Double) extends Image
final case class Rectangle(width: Double, height: Double) extends Image
final case class Above(above: Image, below: Image) extends Image
final case class Beside(left: Image, right: Image) extends Image
final case class On(top: Image, bottom: Image) extends Image
