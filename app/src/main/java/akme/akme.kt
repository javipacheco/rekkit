package akme

import kategory.*

typealias Service<A> = Either<AkmeException, A>

sealed class AkmeException {

    data class UiException(val msg: String) : AkmeException()

    data class ApiException(val msg: String) : AkmeException()

}

fun <A> ServiceRight(op: () -> A): Either<AkmeException, A> = Either.Right(op())

fun <A> ServiceLeft(ex: AkmeException): Either<AkmeException, A> = Either.Left(ex)

fun ServiceMonad() = Either.monad<AkmeException>()

fun <A> A.catchOnly(ex: AkmeException): Service<A> = catchOnly(ex){this}

fun <A> A.catchUi(): Service<A> = this.catchOnly(AkmeException.UiException(""))

fun <A> catchUi(default: () -> A): Service<A> = default().catchOnly(AkmeException.UiException(""))

fun <A> catchOnly(ex: AkmeException, default: () -> A): Service<A> = try {
    ServiceRight({ default() })
} catch (e: Throwable) {
    ServiceLeft<A>(ex)
}