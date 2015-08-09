package io.koff.examples.async_services

/**
 * Simple example of how to use scalaz.Either and scala Futures together for creating fail-fast logic
 * @author Aleksei Shamenev (coffius@gmail.com)
 */
object EitherFutureSimpleExample extends App {
  import scala.concurrent.{Await, Future}

/**
 * The result calculation will be finished only if all of async actions(doSuccessActionN)
 * are finished successfully(return ActionSuccess)
 *
 * If only one of doSuccessActionN() is finished with error(return ActionFailure)
 * then the result calculation will also return error(ActionFailure)
 */
  import scala.concurrent.ExecutionContext.Implicits.global

  import scala.concurrent.duration._
  import scalaz.Functor
  import scalaz.{\/, Monad}
  import scalaz.{-\/, \/-}
  import scalaz.EitherT.eitherT

  //redefine either classes for better readability
  val ActionSuccess = \/-
  val ActionFailure = -\/

  /**
   * Parametric type with an calculation result of an action
   */
  type ActionResult[T] = String \/ T

  /**
   * Parametric type with an async calculation result of an action
   */
  type FutureActionResult[T] = Future[ActionResult[T]]

  /**
   * Example class with a result of some operation
   * @param value some result value
   */
  case class Outcome(value: String)

  //define Functor[Future] and Monad[Future] for work with future`s monad transformer
  implicit val FutureFunctor = new Functor[Future] {
    def map[A, B](a: Future[A])(f: A => B): Future[B] = a map f
  }

  implicit val FutureMonad = new Monad[Future] {
    def point[A](a: => A): Future[A] = Future(a)
    def bind[A, B](fa: Future[A])(f: (A) => Future[B]): Future[B] = fa flatMap f
  }

  /**
   * Successful async operation #1
   * doSuccessAction1(), ... doSuccessActionN() are examples of some simple actions
   * like db requests or requests to external services. All of them return a future of a service result
   */
  def doSuccessAction1(): FutureActionResult[Outcome] = {
    Future {
      Thread.sleep(5000)
      ActionSuccess(Outcome("success#first"))
    }
  }

  /**
   * Successful async operation #2
   */
  def doSuccessAction2(): FutureActionResult[Outcome] = {
    Future {
      Thread.sleep(10000)
      ActionSuccess(Outcome("success#second"))
    }
  }

  /**
   * Successful async operation #3
   */
  def doSuccessAction3(): FutureActionResult[Outcome] = {
    Future {
      Thread.sleep(15000)
      ActionSuccess(Outcome("success#third"))
    }
  }

  /**
   * Complex action which should execute successfully.
   * This is the example of the complex operation which needs additional data for completion of calculation.
   * This action requests needed data in parallel.
   */
  def doSuccessfulComplexAction(): FutureActionResult[String] = {
    //start execution in parallel
    val futureResult1 = doSuccessAction1()
    val futureResult2 = doSuccessAction2()
    val futureResult3 = doSuccessAction3()

    val monadResult = for {
      result1 <- eitherT(futureResult1)
      result2 <- eitherT(futureResult2)
      result3 <- eitherT(futureResult3)
    } yield {
      // if all operations complete successfully we will concatenate string
      result1.value + " " + result2.value + " " + result3.value
    }

    //return final result
    monadResult.run
  }

  /**
   * Example of ugly for-comprehension
   */
  def doUglyComplexAction(): FutureActionResult[String] = {
    //start execution in parallel
    val futureResult1 = doSuccessAction1()
    val futureResult2 = doSuccessAction2()
    val futureResult3 = doSuccessAction3()

    for{
      eitherResult1 <- futureResult1
      eitherResult2 <- futureResult2
      eitherResult3 <- futureResult3
    } yield {
      for {
        result1 <- eitherResult1
        result2 <- eitherResult2
        result3 <- eitherResult3
      } yield {
        // if all operations complete successfully we will concatenate string
        result1.value + " " + result2.value + " " + result3.value
      }
    }
  }

  //print result of calculation
  val futResult = doSuccessfulComplexAction().map{
    case ActionSuccess(value) => println("success value: " + value)
    case ActionFailure(error) => println("err value: " + error)
  }

  //Wait 20 seconds for result
  Await.result(futResult, 20.seconds)
}
