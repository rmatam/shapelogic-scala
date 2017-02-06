package org.shapelogic.sc.color

import scala.collection.mutable.ArrayBuffer

/**
 * BaseAreaFactory the base for GrayAreaFactory and ColorAreaFactory, it is a
 * factory and store for IColorAndVariance.
 * <br />
 * @author Sami Badawi
 *
 */
abstract class BaseAreaFactory extends ValueAreaFactory {

  val _store = new ArrayBuffer[IColorAndVariance]()
  var _backgroundColor: Int = 0
  var _maxArea: Int = 0

  override def getStore(): ArrayBuffer[IColorAndVariance] = {
    _store
  }

  /** Returns the biggest color. */
  override def getBackgroundColor(): Int = {
    _backgroundColor
  }

  override def sort(): Unit = {
    _store.sortBy(el => el.getArea)
  }

  override def areasGreaterThan(minSize: Int): Int = {
    sort()
    val firstBigger = _store.indexOf((el: IColorAndVariance) => minSize <= el.getArea)
    scala.math.max(firstBigger - 1, 0)
  }
}
