package io.koff.examples.variance

//Variance tip notes
object Variance {
  class GrandParent
  class Parent extends GrandParent
  class Child1 extends Parent
  class Child2 extends Parent

  //Three types of variance in scala
  //InVariant means that there is no relation between InVariant[Parent] and InVariant[Child1] or InVariant[Child2]
  class InVariant[T]
  //CoVariant means that CoVariant[Child1] and CoVariant[Child2] are subclasses of CoVariant[Parent]
  class CoVariant[+T]
  //ContraVariant means that ContraVariant[Parent] is a subclass of ContraVariant[Child2] and ContraVariant[Child2]
  class ContraVariant[-T]

  def inVariantExample() = {
    val parentInvariant = new InVariant[Parent]
    // Next lines cannot be compiled
    //val child1Invariant: InVariant[Child1] = parentInvariant
    //val child2Invariant: InVariant[Child2] = parentInvariant
  }

  def coVariantExample() = {
    val child1Covariant = new CoVariant[Child1]
    val child2Covariant = new CoVariant[Child2]
    //We can use values of CoVariant[Child1|Child2] as CoVariant[Parent]
    val parentCovariant1: CoVariant[Parent] = child1Covariant
    val parentCovariant2: CoVariant[Parent] = child2Covariant
  }

  def contraVariantExample() = {
    val parentContravariant = new ContraVariant[Parent]
    //Looks awkward but it is totally legit :)
    val child1Contravariant: ContraVariant[Child1] = parentContravariant
    val child2Contravariant: ContraVariant[Child2] = parentContravariant
  }

  def correctUsageOfCovariance() = {
    //we can use a covariant type as
    class Producer[+A](val value: A) {          // a type for immutable values
      private[this] var variable: A = ???       // a type for private mutable variables
      def simpleProduce(): A = ???              // a type for method outputs
      def complexProduce[B >: A](b: B): A = ??? // a lower type bound
    }
  }

  def incorrectUsageOfCovarience() = {
    //code below cannot be compiled
    //we can not use a covariant type as
    //class Producer[+A](var variable: A) { // a type for public mutable variables
    //  def consume(a: A): Unit = ???       // a type for method parameters
    //}
  }

  def correctUsageOfContravarience() = {
    //we can use a contravariant type as
    class Consumer[-A]() {
      private[this] var variable: A = ???   // a type for private mutable variables
      def consume(a: A): Unit = ???         // a type for method parameters
      def complex[B <: A](b: B): Unit = ??? // a upper type bound
    }
  }

  def incorrectUsageOfContravarience() = {
    //code below cannot be compiled
    //we can not use a contravariant type as
    //class Producer[-A](val value: A, var variable: A) { // a type for public immutable values and publis mutable variables
    //  def produce(): A = ???                            // a type for for method outputs
    //  def complexProduce[B >: A](b: B): A = ???         // a type for lower
    //}
  }
}
