package org.shapelogic.sc.operation

import org.shapelogic.sc.image.BufferImage
import org.shapelogic.sc.pixel.SobelPixel._
import spire.math.Numeric
import spire.math.Numeric._
import spire.math.Integral
import spire.algebra._
import spire.math._
import spire.implicits._

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

object SobelOperation {
  implicit class SobelOperationByte(inputImage: BufferImage[Byte]) extends BaseOperation[Byte, Int](inputImage)(new SobelPixelByte(inputImage)) {
  }

  class SobelOperationShort(inputImage: BufferImage[Short]) extends BaseOperation[Short, Int](inputImage)(new SobelPixelShort(inputImage)) {
  }

  class SobelOperationInt(inputImage: BufferImage[Int]) extends BaseOperation[Int, Int](inputImage)(new SobelPixelInt(inputImage)) {
  }

  class SobelOperationFloat(inputImage: BufferImage[Float]) extends BaseOperation[Float, Float](inputImage)(new SobelPixelFloat(inputImage)) {
  }

  class SobelOperationDouble(inputImage: BufferImage[Double]) extends BaseOperation[Double, Double](inputImage)(new SobelPixelDouble(inputImage)) {
  }

  def sobelOperationByteFunction(inputImage: BufferImage[Byte]): BufferImage[Byte] = {
    val hasBufferImage = new SobelOperationByte(inputImage)
    hasBufferImage.result
  }

  /**
   *
   */
  def makeTransform[T](inputImage: BufferImage[T]): Unit = {

  }

  /**
   *
   */
  def makeByteTransform(inputImage: BufferImage[Byte]): BufferImage[Byte] = {
    inputImage.result
  }
}