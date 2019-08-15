package syntax.pc

import syntax.Token

sealed class ParserState<out T> {
	data class Ok<T>(
		val next: ParserInputState,
		val result: T
	) : ParserState<T>()

	data class Error(val context: String, val expected: Token.Type, val actual: Token) : ParserState<Nothing>() {
		val msg: String by lazy { "(${actual.line}, ${actual.col}) $context: Expecting '${expected.findSymbol(null)}', found '${actual.type.findSymbol(actual)}'\n" }

		infix fun neither(other: Error): Error = listOf(this, other).maxWith(
			compareBy({ it.actual.line }, { it.actual.col })
		)!!
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


fun Token.Type.findSymbol(token: Token?): String = when (this) {
	Token.Type.Identifier -> token?.value ?: "An identifier"
	Token.Type.Integer -> token?.value ?: "An Integer"
	Token.Type.StringType -> token?.value ?: "A String"
	Token.Type.EndOfInput -> "End of input"
	Token.Type.KeywordIf -> "if"
	Token.Type.KeywordElse -> "else"
	Token.Type.KeywordWhile -> "while"
	Token.Type.KeywordPrint -> "print"
	Token.Type.KeywordPutc -> "putc"
	Token.Type.OpMultiply -> "*"
	Token.Type.OpDivide -> "/"
	Token.Type.OpMod -> "%"
	Token.Type.OpAdd -> "+"
	Token.Type.OpSubtract -> "-"
	Token.Type.OpLessEqual -> "<="
	Token.Type.OpLess -> "<"
	Token.Type.OpGreaterEqual -> ">="
	Token.Type.OpGreater -> ">"
	Token.Type.OpEqual -> "=="
	Token.Type.OpAssign -> "="
	Token.Type.OpNotEqual -> "!="
	Token.Type.OpNot -> "!"
	Token.Type.OpAnd -> "&&"
	Token.Type.OpOr -> "||"
	Token.Type.LeftParen -> "("
	Token.Type.RightParen -> ")"
	Token.Type.LeftBrace -> "{"
	Token.Type.RightBrace -> "}"
	Token.Type.Semicolon -> ";"
	Token.Type.Comma -> ","
}