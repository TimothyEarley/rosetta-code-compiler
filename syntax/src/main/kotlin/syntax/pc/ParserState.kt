package syntax.pc

import syntax.Token

sealed class ParserState<out T> {
	data class Ok<T>(
		val next: ParserInputState,
		val result: T
	) : ParserState<T>()

	sealed class Error(msgBuilder: () -> String) : ParserState<Nothing>() {
		val msg: String by lazy(msgBuilder)

		data class Expected(val expected: Token.Type, val actual: Token) :
			Error({ "Expected $expected but got $actual" })

		data class Neither(val options: List<Expected>) : Error({
			"All failed: \n${options
				.sortedWith(
					compareBy({ -it.actual.line }, { -it.actual.col })
				)
				.joinToString(separator = "\n") { it.msg }}"
		})

		infix fun neither(other: Error): Error = Neither(
			when (this) {
				is Neither -> when (other) {
					is Neither -> options + other.options
					is Expected -> options + other
				}
				is Expected -> when (other) {
					is Neither -> other.options + this
					is Expected -> listOf(this, other)
				}
			}
		)
	}
}

fun <A, B> ParserState<A>.fold(onError: (ParserState.Error) -> B, onOk: (A) -> B): B = when (this) {
	is ParserState.Ok -> onOk(this.result)
	is ParserState.Error -> onError(this)
}

infix fun <A, B> ParserState<A>.map(f: (A) -> B): ParserState<B> = when (this) {
	is ParserState.Ok -> ParserState.Ok(next, f(result))
	is ParserState.Error -> this
}

infix fun <A> ParserState<A>.mapLeft(f: (ParserState.Error) -> ParserState<A>): ParserState<A> = when (this) {
	is ParserState.Ok -> this
	is ParserState.Error -> f(this)
}


infix fun <A, B> ParserState<A>.flatMap(f: (ParserState.Ok<A>) -> ParserState<B>): ParserState<B> =
	when (this) {
		is ParserState.Ok -> f(this)
		is ParserState.Error -> this
	}