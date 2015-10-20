package doodle.core

// A local coordinate system to draw within

final case class BoundingBox(leftCoordinate: Double,  rightCoordinate: Double, topCoordinate: Double, bottomCoordinate: Double) {

  // Want to calculate width and height from coordinates
  // Need to figure out centre of boundingbox?
  // Then want to figure out what to do with different types of image or operation => circle, rectangle, above, beside and on
    val height: Double =  topCoordinate - bottomCoordinate
    val width: Double =  rightCoordinate - leftCoordinate
    
    //Need apply method overridden to handle image => need companion object
    object BoundingBox{
      
    def apply(image: Image): BoundingBox = image match {    
      case Rectangle(width, height) =>
        BoundingBox(-width/2, height/2, width/2, -height/2)
      }  
   }
}