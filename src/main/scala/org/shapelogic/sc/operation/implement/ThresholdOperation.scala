package org.shapelogic.sc.operation

import spire.math.Numeric
import spire.implicits._

import org.shapelogic.sc.image.BufferImage
import org.shapelogic.sc.numeric._
import org.shapelogic.sc.pixel._
import spire.math._
import spire.implicits._
import scala.reflect.ClassTag
import scala.util.Try

/**
 * Should take an image and a value
 *
 * Many input channels one output channel possibly an alpha output channel
 * This has knowledge of the internals of the numbers
 *
 * Return gray scale image with 2 values 0 and 255
 *
 * @param T: Input image type, e.g. Byte
 * @param C: Calculation type, e.g. Int
 * @param promoter: Promote and demote number from input to calculation
 *
 */
sealed class ThresholdOperation[ //
@specialized(Byte, Short, Int, Long, Float, Double) T: ClassTag, //Input image type
@specialized(Byte, Short, Int, Long, Float, Double) C: ClassTag: Numeric: Ordering //Calculation  type
](
    inputImage: BufferImage[T],
    threshold: C)(
        implicit promoter: NumberPromotion.Aux[T, C]) {

  lazy val thresholdSum = threshold * inputImage.numBandsNoAlpha

  lazy val numBands = inputImage.numBands
  lazy val alphaChannel = inputImage.alphaChannel
  lazy val verboseLogging: Boolean = true

  //XXX this is very strange, it is not possible to take this out lazily
  val outBuffer: Array[T] = new Array[T](inputImage.pixelCount)
  val outputImage = new BufferImage[T](
    width = inputImage.width,
    height = inputImage.height,
    numBands = 1,
    bufferInput = outBuffer,
    rgbOffsetsOpt = None)

  lazy val inBuffer = inputImage.data
  lazy val inputNumBands = inputImage.numBands
  lazy val indexColorPixel: IndexColorPixel[T] = IndexColorPixel.apply(inputImage)
  lazy val pixelOperation: PixelOperation[T] = new PixelOperation(inputImage)

  var low = 0
  var high = 0

  lazy val lowValue: T = promoter.minValueBuffer // 0
  lazy val highValue: T = promoter.maxValueBuffer //-1 // 255

  def sumOfChannel(index: Int): C = {
    var sum: C = 0
    cfor(0)(_ < numBands, _ + 1) { i =>
      if (i != alphaChannel)
        sum = sum + promoter.promote(inputImage.data(index + i))
    }
    sum
  }

  /**
   * This easily get very inefficient
   */
  def handleIndex(index: Int, indexOut: Int): Unit = {
    try {
      val sumValue = sumOfChannel(index)
      if (thresholdSum < sumValue) { //Problem with sign 
        high += 1
        outBuffer(indexOut) = highValue
      } else {
        low += 1
        outBuffer(indexOut) = lowValue
      }
    } catch {
      case ex: Throwable => {
        println(ex.getMessage)
      }
    }
  }

  /**
   * Run over input and output
   * Should I do by line?
   */
  def calc(): BufferImage[T] = {
    val pointCount = inputImage.width * inputImage.height
    pixelOperation.reset()
    var indexOut: Int = -1
    var index: Int = pixelOperation.index
    while (pixelOperation.hasNext) {
      index = pixelOperation.next()
      indexOut += 1
      handleIndex(index, indexOut)
    }
    if (verboseLogging)
      println(s"low count: $low, high: $high, index: $index, indexOut: $indexOut")
    outputImage
  }

  lazy val result: BufferImage[T] = calc()
}

object ThresholdOperation {
  import PrimitiveNumberPromotersAux.AuxImplicit._

  def makeByteTransform(inputImage: BufferImage[Byte], parameter: String): BufferImage[Byte] = {
    val threshold: Int = Try(parameter.trim().toInt).getOrElse(100)
    val thresholdOperation = new ThresholdOperation[Byte, Int](inputImage, threshold)
    println(s"Start makeByteTransform()")
    val res = thresholdOperation.result
    println(s"End makeByteTransform()")
    res
  }
}
