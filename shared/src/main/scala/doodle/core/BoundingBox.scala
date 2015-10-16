package doodle.core

// A local coordinate system to draw within

final case class BoundingBox(leftCoordinate: Double,  rightCoordinate: Double, topCoordinate: Double, bottomCoordinate: Double) {

  // Want to calculate width and height from coordinates
  // Need to figure out centre of boundingbox
  // Then want to figure out what to do with different types of image or operation => circle, rectangle, above, beside and on
}