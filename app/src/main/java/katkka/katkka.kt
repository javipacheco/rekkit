package katkka

import kategory.*

typealias Service<A> = Either<KatkkaException, A>

sealed class KatkkaException {

    data class UiException(val msg: String) : KatkkaException()

}

fun <A> ServiceRight(op: () -> A): Either<KatkkaException, A> = Either.Right(op())

fun <A> ServiceLeft(ex: KatkkaException): Either<KatkkaException, A> = Either.Left(ex)

fun ServiceMonad() = Either.monad<KatkkaException>()

fun <A> A.catchOnly(ex: KatkkaException): Service<A> = catchOnly(ex){this}

fun <A> A.catchUi(): Service<A> = this.catchOnly(KatkkaException.UiException(""))

fun <A> catchUi(default: () -> A): Service<A> = default().catchOnly(KatkkaException.UiException(""))

fun <A> catchOnly(ex: KatkkaException, default: () -> A): Service<A> = try {
    ServiceRight({ default() })
} catch (e: Throwable) {
    ServiceLeft<A>(ex)
}