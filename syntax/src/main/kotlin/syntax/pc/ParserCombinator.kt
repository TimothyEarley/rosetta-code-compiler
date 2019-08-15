package syntax.pc

import syntax.Token

//TODO encapsulate in builder method

data class ParserInputState(
	val tokens: Sequence<Token>,
	val skippedError: ParserState.Error? = null
) {
	fun ok() = ParserState.Ok<Token>(
		next = this.copy(tokens = tokens.drop(1)),
		result = tokens.first()
	)

	fun ParserContext.parseError(expected: Token.Type) = ParserState.Error(context, expected, tokens.first()).let {
		if (skippedError != null) it neither skippedError
		else it
	}
}

typealias Parser<T> = Lazy<ParserInputState.() -> ParserState<T>>

fun <T> Parser<T>.run(tokens: Sequence<Token>): ParserState<T> = this.value.invoke(
	ParserInputState(tokens = tokens)
)

fun ParserContext.t(type: Token.Type): Parser<Token> = lazyOf(fun ParserInputState.() =
	if (tokens.first().type == type) ok()
	else parseError(type))


fun <T> many(parser: Parser<T>): Parser<List<T>> = lazyOf(fun ParserInputState.(): ParserState<List<T>> {
	val list = mutableListOf<T>()
	var next: ParserInputState = this
	forever {
		when (val result = parser.value(next)) {
			is ParserState.Ok -> {
				list.add(result.result)
				next = result.next
			}
			is ParserState.Error -> return ParserState.Ok(
				next.copy(skippedError = result),
				list
			)
		}
	}
})

infix fun <T> Parser<T>.or(other: Parser<T>): Parser<T> = lazyOf(fun ParserInputState.() =
	value() mapLeft { err -> other.value(this).mapLeft { err neither it } })

fun <T> optional(parser: Parser<T>): Parser<T?> = lazyOf(fun ParserInputState.(): ParserState<T?> {
	return when (val result = parser.value(this)) {
		is ParserState.Ok -> result
		is ParserState.Error -> ParserState.Ok(this.copy(skippedError = result), null)
	}
})

fun <A, B> Parser<A>.map(f: (A) -> B): Parser<B> = lazyOf(fun ParserInputState.(): ParserState<B> = this.value() map f)

// marker object for ignored tokens
object SKIP

fun <T> Parser<T>.skip(): Parser<SKIP> = this.map { SKIP }

// plus = andThen

infix operator fun <A, B> Parser<A>.plus(other: Parser<B>): Parser<Pair<A, B>> =
	lazyOf(fun ParserInputState.(): ParserState<Pair<A, B>> = this@plus.value(this) flatMap { a ->
		other.value(a.next).map { b -> a.result to b }
	})

@JvmName("skipPlus")
infix operator fun <B> Parser<SKIP>.plus(other: Parser<B>): Parser<B> =
	lazyOf(fun ParserInputState.(): ParserState<B> = this@plus.value(this) flatMap { a ->
		other.value(a.next)
	})

@JvmName("plusSkip")
infix operator fun <A> Parser<A>.plus(other: Parser<SKIP>): Parser<A> =
	lazyOf(fun ParserInputState.(): ParserState<A> = this@plus.value(this) flatMap { a ->
		other.value(a.next).map { a.result }
	})

@JvmName("skipPlusSkip")
infix operator fun Parser<SKIP>.plus(other: Parser<SKIP>): Parser<SKIP> =
	lazyOf(fun ParserInputState.(): ParserState<SKIP> = this@plus.value(this) flatMap { a ->
		other.value(a.next)
	})


data class ParserContext(val context: String)
fun <T> context(name: String, f: ParserContext.() -> Parser<T>): Parser<T> =
	ParserContext(name).run(f)

// helper
inline fun forever(f: () -> Unit): Nothing {
	while (true) {
		f()
	}
}