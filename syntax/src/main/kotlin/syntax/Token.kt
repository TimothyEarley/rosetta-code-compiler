package syntax

//TODO think about using a common module (when done with rosetta)
data class Token(val type: Type, val value: String, val line: Int, val col: Int) {
	enum class Type(val representation: String) {
		Identifier("Identifier"),
		Integer("Integer"),
		StringType("String"),
		EndOfInput("End_of_input"),

		KeywordIf("Keyword_if"),
		KeywordElse("Keyword_else"),
		KeywordWhile("Keyword_while"),
		KeywordPrint("Keyword_print"),
		KeywordPutc("Keyword_putc"),

		OpMultiply("Op_multiply"),
		OpDivide("Op_divide"),
		OpMod("Op_mod"),
		OpAdd("Op_add"),
		OpSubtract("Op_subtract"),
		OpLessEqual("Op_lessequal"),
		OpLess("Op_less"),
		OpGreaterEqual("Op_greaterequal"),
		OpGreater("Op_greater"),
		OpEqual("Op_equal"),
		OpAssign("Op_assign"),
		OpNotEqual("Op_notequal"),
		OpNot("Op_not"),
		OpAnd("Op_and"),
		OpOr("Op_or"),

		LeftParen("LeftParen"),
		RightParen("RightParen"),
		LeftBrace("LeftBrace"),
		RightBrace("RightBrace"),
		Semicolon("Semicolon"),
		Comma("Comma"),
	}
}

fun readToken(source: String): Token {
	val split = source.split(" ", limit = 4)
	val (line, col, type, value) = if (split.size == 4) split else split + ""
	val l = line.toInt()
	val c = col.toInt()
	val tokenType =
		Token.Type.values().find { it.representation == type } ?: error("Unknown token: $type in $source")
	return Token(tokenType, value, l, c)
}