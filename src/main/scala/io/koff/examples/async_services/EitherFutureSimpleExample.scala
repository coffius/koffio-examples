package io.koff.examples.async_services

/**
 * Simple example of how to use scalaz.Either and scala Futures together for creating fail-fast logic
 * @author Aleksei Shamenev (coffius@gmail.com)
 */
object EitherFutureSimpleExample extends App {
  import scala.concurrent.{Await, Future}

/**
 * The result calculation will be finished only if all of async actions(doSuccessOperationN)
 * are finished successfully(return ServiceSuccess)
 *
 * If only one of doSuccessOperationN() is finished with error(return ServiceFailure)
 * then the result calculation will also return error(ServiceFailure)
 */
  import scala.concurrent.ExecutionContext.Implicits.global

  import scala.concurrent.duration._
  import scalaz.Functor
  import scalaz.{\/, Monad}
  import scalaz.{-\/, \/-}
  import scalaz.EitherT.eitherT

  //redefine either classes for better readability
  val ServiceSuccess = \/-
  val ServiceFailure = -\/

  /**
   * Parametric type with an calculation result of an action
   */
  type ServiceResult[T] = String \/ T

  /**
   * Parametric type with an async calculation result of an action
   */
  type FutureServiceResult[T] = Future[ServiceResult[T]]

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
   * doSuccessOperation1(), ... doSuccessOperationN() are examples of some simple actions
   * like db requests or requests to external services. All of them return a future of a service result
   */
  def doSuccessOperation1(): FutureServiceResult[Outcome] = {
    Future {
      Thread.sleep(5000)
      ServiceSuccess(Outcome("success#first"))
    }
  }

  /**
   * Successful async operation #2
   */
  def doSuccessOperation2(): FutureServiceResult[Outcome] = {
    Future {
      Thread.sleep(10000)
      ServiceSuccess(Outcome("success#second"))
    }
  }

  /**
   * Successful async operation #3
   */
  def doSuccessOperation3(): FutureServiceResult[Outcome] = {
    Future {
      Thread.sleep(15000)
      ServiceSuccess(Outcome("success#third"))
    }
  }

  /**
   * Complex action which should execute successfully.
   * This is the example of the complex operation which needs additional data for completion of calculation.
   * This action requests needed data in parallel.
   */
  def doSuccessfulComplexAction(): FutureServiceResult[String] = {
    //start execution in parallel
    val futureResult1 = doSuccessOperation1()
    val futureResult2 = doSuccessOperation2()
    val futureResult3 = doSuccessOperation3()

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

  //print result of calculation
  val futResult = doSuccessfulComplexAction().map{
    case ServiceSuccess(value) => println("success value: " + value)
    case ServiceFailure(error) => println("err value: " + error)
  }

  //Wait 20 seconds for result
  Await.result(futResult, 20.seconds)
}
