package org.shapelogic.sc.calculation

/**
 * Calc0 is basically a Closure with 0 inputs.
 * <br />
 * Used in a lazy calculation of a value, and lazy stream with no input.
 * <br />
 * The goal is to make lazy calculation and lazy streams as interchangeable as possible.
 * <br />
 * Using the same names as Neal Gafter's Closure prototype.
 * Might be changed to be a sub interface of them later.
 * <br />
 *
 * @author Sami Badawi
 *
 * @param <Out>
 */
trait Calc0[Out] {
  def invoke(): Out
}
