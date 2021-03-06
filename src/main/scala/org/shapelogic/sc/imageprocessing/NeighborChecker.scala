package org.shapelogic.sc.imageprocessing

import org.shapelogic.sc.util.Constants
import spire.implicits._
import org.shapelogic.sc.image.BufferImage

/**
 * Neighbor Checker.
 *
 * Runs around a point and find what type all the neighbor points have
 *
 * @author Sami Badawi
 *
 */
class NeighborChecker(
    val image: BufferImage[Byte],
    currentPixelIndex: Int) extends IPixelTypeFinder {

  //Find and set the type of all the neighbor points
  val extraNeighborPoint: FirstDirectionForType = new FirstDirectionForType()
  val junction: FirstDirectionForType = new FirstDirectionForType()
  val other: FirstDirectionForType = new FirstDirectionForType()
  val used: FirstDirectionForType = new FirstDirectionForType()
  val vCornerPoint: FirstDirectionForType = new FirstDirectionForType()
  val localPixelTypeCalculator: PixelTypeCalculator = new PixelTypeCalculator()

  val _pixels: Array[Byte] = image.data
  val bufferLenght = image.bufferLenght
  val cyclePoints: Array[Int] = image.cyclePoints

  /** Run over the neighbors points and put them in categories. */
  def checkNeighbors(): Unit = {
    cfor(0)(_ < Constants.DIRECTIONS_AROUND_POINT, _ + 1) { iInt =>
      val i = iInt.toByte
      var pixelIndexI: Int = currentPixelIndex + cyclePoints(i)
      var pixel: Byte = if (0 <= pixelIndexI && pixelIndexI < bufferLenght)
        _pixels(pixelIndexI)
      else
        PixelType.BACKGROUND_POINT.color
      if (pixel == PixelType.PIXEL_FOREGROUND_UNKNOWN.color) {
        localPixelTypeCalculator.setup()
        findPointType(pixelIndexI, localPixelTypeCalculator)
        pixel = localPixelTypeCalculator.getPixelType().color
        _pixels(pixelIndexI) = pixel
      }
      var isUsed: Boolean = PixelType.isUsed(pixel)
      if (isUsed) {
        used.addDirection(i, isUsed)
      }

      if (PixelType.BACKGROUND_POINT.color == pixel) {
        //        continue
      } else if (PixelType.PIXEL_JUNCTION.equalsIgnore(pixel)) {
        junction.addDirection(i, isUsed)
      } else if (PixelType.PIXEL_EXTRA_NEIGHBOR.equalsIgnore(pixel)) {
        extraNeighborPoint.addDirection(i, isUsed)
      } else if (PixelType.PIXEL_V_CORNER.equalsIgnore(pixel)) {
        vCornerPoint.addDirection(i, isUsed)
      } else {
        other.addDirection(i, isUsed)
      }
    }
  }

  def allNeighbors(): Int = {
    return extraNeighborPoint.count +
      junction.count +
      other.count +
      used.count +
      vCornerPoint.count
  }

  def falseJunction(): Boolean = {
    return 0 < vCornerPoint.count && allNeighbors() - vCornerPoint.count <= 2
  }

  def margin: Int = 0
  lazy val xMin: Int = image.xMin + margin
  lazy val xMax: Int = image.xMax - margin
  lazy val yMin: Int = image.yMin + margin
  lazy val yMax: Int = image.yMax - margin

  lazy val priorityBasedPixelTypeFinder = new PriorityBasedPixelTypeFinder(image)

  /**
   * XXX Not sure if I am using the right PixelTypeFinder
   */
  override def findPointType(pixelIndex: Int,
    reusedPixelTypeCalculator: PixelTypeCalculator): PixelTypeCalculator = {
    return priorityBasedPixelTypeFinder.findPointType(pixelIndex, reusedPixelTypeCalculator)
  }
}
