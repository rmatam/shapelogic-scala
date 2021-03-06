package org.shapelogic.sc.polygon

import java.awt.Rectangle
/**
 * This BBox should work for all underlying types
 *
 *  I think that this should be immutable
 *  I would have to change the fields to be private and have getters
 *
 * @author Sami Badawi
 *
 */
class BBox(pointIn: IPoint2D) extends GeometricShape2D {
  var minVal: IPoint2D = pointIn
  var maxVal: IPoint2D = pointIn

  def isEmpty(): Boolean = {
    minVal == null
  }

  def addPoint(pointIn: IPoint2D): Unit = {
    if (isEmpty()) {
      minVal = pointIn.copy()
      maxVal = pointIn.copy()
    } else {
      min(pointIn)
      max(pointIn)
    }
  }

  def addPoint(x: Int, y: Int): Unit = {
    val pointIn: CPointInt = new CPointInt(x, y)
    if (isEmpty()) {
      minVal = pointIn.copy()
      maxVal = pointIn.copy()
    } else {
      min(pointIn)
      max(pointIn)
    }
  }

  def min(pointIn: IPoint2D): Unit = {
    val minX = Math.min(pointIn.getX(), minVal.getX())
    val minY = Math.min(pointIn.getY(), minVal.getY())
    minVal.setLocation(minX, minY)
  }

  def max(pointIn: IPoint2D): Unit = {
    val maxX = Math.max(pointIn.getX(), maxVal.getX())
    val maxY = Math.max(pointIn.getY(), maxVal.getY())
    maxVal.setLocation(maxX, maxY)
  }

  def isEven(input: Long): Boolean = {
    (input & 1L) == 0L
  }

  def getCenter(): IPoint2D = {
    val center: IPoint2D = minVal.copy().add(maxVal)
    if (center.isInstanceOf[CPointInt]) {
      val x: Long = center.getX().toLong
      val y: Long = center.getY().toLong
      if (isEven(x) && isEven(y)) {
        new CPointInt((x / 2).toInt, (y / 2).toInt)
      }
    }
    center.multiply(0.5)
    center
  }

  def getDiagonalVector(): IPoint2D = {
    if (null != maxVal && null != minVal) {
      val diagonal: IPoint2D = maxVal.copy().minus(minVal)
      diagonal
    } else
      new CPointInt(0, 0)
  }

  /**
   * A point on the diagonal line
   *
   * @param fraction 0 -> minVal, 1 -> maxVal, 0.5 -> middle point
   */
  def getDiagonalVector(fraction: Double): IPoint2D = {
    val diagonal: IPoint2D = Calculator2D.spannedPoint(minVal, maxVal, fraction)
    diagonal
  }

  override def toString(): String = {
    val sb: StringBuffer = new StringBuffer("bbox:")
    sb.append("min=")
    if (null != minVal)
      sb.append(minVal.toString())
    sb.append(" max=")
    if (null != maxVal)
      sb.append(maxVal.toString())
    sb.append("\n")
    sb.toString()
  }

  override def getDiameter(): Double = {
    getDiagonalVector().distanceFromOrigin()
  }

  def getRectangle(): Rectangle = {
    if (minVal == null)
      null
    else
      new Rectangle(
        minVal.getX().toInt, minVal.getY().toInt,
        maxVal.getX().toInt, maxVal.getY().toInt)
  }

  /** Defined as x/y */
  def getAspectRatio(): Double = {
    val x = getDiagonalVector().getX()
    val y = getDiagonalVector().getY()
    if (y != 0.0)
      x / y
    else
      Double.PositiveInfinity
  }

  def add(bBox: BBox): Unit = {
    if (isEmpty()) {
      minVal = bBox.minVal.copy()
      maxVal = bBox.maxVal.copy()
    } else {
      addPoint(bBox.minVal)
      addPoint(bBox.maxVal)
    }
  }

  //  def maxVal_=(x$1: org.shapelogic.sc.polygon.IPoint2D): Unit = ???
  //  def minVal_=(x$1: org.shapelogic.sc.polygon.IPoint2D): Unit = ???
}
