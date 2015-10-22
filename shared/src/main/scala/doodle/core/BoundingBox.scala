package doodle.core

// A local coordinate system to draw within

final case class BoundingBox(leftCoordinate: Double,
    rightCoordinate: Double,
    topCoordinate: Double,
    bottomCoordinate: Double) {

  // Want to calculate width and height from coordinates
  // Need to figure out centre of boundingbox?
  // Then want to figure out what to do with different types of image or operation => circle, rectangle, above, beside and on
    val height: Double =  topCoordinate - bottomCoordinate
    val width: Double =  rightCoordinate - leftCoordinate
    
    def above(that: BoundingBox): BoundingBox =
       BoundingBox(
          this.leftCoordinate min that.leftCoordinate,
          (this.height + that.height) / 2,
          this.rightCoordinate max that.rightCoordinate,
          -(this.height + that.height) / 2)
          
    def beside(that: BoundingBox): BoundingBox =
      BoundingBox(
          -(this.width + that.width) / 2,
          this.topCoordinate max that.topCoordinate,
          (this.width + that.width) / 2,
          this.bottomCoordinate min that.bottomCoordinate)
          
    def on(that: BoundingBox): BoundingBox =
       BoundingBox(
          this.leftCoordinate min that.leftCoordinate,
          this.topCoordinate max that.topCoordinate,
          this.rightCoordinate max that.rightCoordinate,
          this.bottomCoordinate min that.bottomCoordinate)
          
}
    
    //Need apply method overridden to handle image => need companion object
    object BoundingBox{
      
    def apply(image: Image): BoundingBox = image match {    
      //case Rectangle(width, height) => BoundingBox(-width/2, height/2, width/2, -height/2)
      case Circle(r) => BoundingBox(-r, r, r, -r)
      case Rectangle(w, h) => BoundingBox(-w/2, h/2, w/2, -h/2)
      case Above(a, b) => a.boundingBox above b.boundingBox
      case On(o, u) => o.boundingBox on u.boundingBox
      case Beside(l, r) => l.boundingBox beside r.boundingBox
      }  
   }