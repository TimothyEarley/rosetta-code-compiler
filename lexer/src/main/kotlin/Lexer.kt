package lexer

import java.io.File

fun main(args: Array<String>) {
    require(args.size == 1) { "Please pass a file as the first argument" }
    val source = File(args[0]).readText()
    println(lexer(source))
}

@Throws(LexerException::class)
fun lexer(source: String): String = Lexer(source).parse().joinToString(separator = "\n")

class LexerException(msg: String): Exception(msg)

private data class Token(val name: String, val value: String?, val position: Position) {
	override fun toString(): String =
		"%5d  %5d   %s %s".format(position.line, position.col, name, value ?: "").trimEnd()
}

// lines and columns are 1-indexed
private data class Position(val index: Int = 0, val line: Int = 1, val col: Int = 1) {
	override fun toString(): String = "line $line, col $col"
}

private class Lexer(private val source: String) {

	var position = Position()
		private set

    // store position of last match for token construction
	var markedPosition: Position? = null
		private set
    
    private var match: String? = null // for regex match

    val isEOF: Boolean get() = position.index == source.length
	val value: Char get() = source[position.index]

	fun mark() {
		markedPosition = position
	}

	fun match(char: Char, skip: Boolean = true): Boolean {
		if (value != char) return false
		if (skip) skip(1)
		return true
	}

    fun match(word: String, skip: Boolean = true): Boolean {
        if (position.index + word.length > source.length) return false
        word.forEachIndexed { i, c ->
            if (source[position.index + i] != c ) return false
        }
        if (skip) skip(word.length)
        return true
    }
    
    fun matchRegex(regex: String, skip: Boolean = true): Boolean {
        val matchRegex = "\\A$regex".toRegex()

        val result = matchRegex.find(source.substring(position.index)) ?: return false
        match = result.value
        if(skip) skip(result.value.length)
        return true
    }
    
    fun skipTo(word: String, eofHandler: (String) -> String): String {
        val skipTo = source.indexOf(word, position.index)
		if (skipTo !in source.indices) return eofHandler(source.substring(position.index))
        val skipped = source.substring(position.index, skipTo)
        skip(skipTo - position.index + word.length)
        return skipped
    }

	fun rewindToMark() {
		position = markedPosition ?: return
	}

    fun token(name: String, value: String? = null) = Token(name, value, markedPosition ?: error("No position marked"))
    fun tokenRegexValue(name: String) = token(name, match ?: error("No match set"))

    // unsafe
    private fun skip(length: Int) {
		var (index, line, col) = position
        repeat(length) {
            if (source[index] == '\n') {
                line++
                col = 1
            } else {
                col++
            }
            index++
        }
		position = Position(index, line, col)
    }
}

private fun Lexer.parse(): List<Token> {
    val tokens = mutableListOf<Token>()
    while ( ! isEOF ) {
		mark()
        val next = when {
            match(' ') -> null
			match('\t') -> null
            match('\n') -> null

            match("/*") -> {
				skipTo("*/") { raise("End-of-file in comment. Closing comment characters not found") }
				null
			}

            matchRegex("if\\b") -> token("Keyword_if")
            matchRegex("else\\b") -> token("Keyword_else")
            matchRegex("while\\b") -> token("Keyword_while")
            matchRegex("print\\b") -> token("Keyword_print")
            matchRegex("putc\\b") -> token("Keyword_putc")

            match('*') -> token("Op_multiply")
            match('/') -> token("Op_divide")
            match('%') -> token("Op_mod")
            match('+') -> token("Op_add")
            match('-') -> token("Op_subtract")
            match("<=") -> token("Op_lessequal")
            match('<') -> token("Op_less")
            match(">=") -> token("Op_greaterequal")
            match('>') -> token("Op_greater")
            match("==") -> token("Op_equal")
            match('=') -> token("Op_assign")
            match("!=") -> token("Op_notequal")
            match("!") -> token("Op_not")
            match("&&") -> token("Op_and")
            match("||") -> token("Op_or")

            match('(') -> token("LeftParen")
            match(')') -> token("RightParen")
            match('{') -> token("LeftBrace")
            match('}') -> token("RightBrace")
            match(';') -> token("Semicolon")
            match(',') -> token("Comma")

            matchRegex("[0-9]+\\b") -> tokenRegexValue("Integer")

			match('\'') -> {
				val contents = skipTo("'") {
					raise("End-of-file while scanning character literal. Closing string character not found")
				}
				token("Integer", when {
					contents == "\\n" -> 10
					contents == "\\\\" -> 92
					contents.startsWith("\\") -> raise("Unknown escape sequence")
					else -> when (contents.length) {
						1 -> contents.single().toInt()
						0 -> raise("Empty character constant")
						else ->  raise("Multi-character constant")
					}
				}.toString())
			}

			match("\"") -> {
				val contents = skipTo("\"") {
					raise("End-of-file while scanning string literal. Closing string character not found")
				}
				token("String", '"' + contents + '"')
			}

            matchRegex(""""[^"\n]*"""") -> tokenRegexValue("String")
            matchRegex("[_a-zA-Z][_a-zA-Z0-9]*") -> tokenRegexValue("Identifier")
            
            //match error types
			matchRegex("[0-9]+[^0-9]", false) -> raise("Invalid number. Starts like a number, but ends in non-numeric characters")
			else -> raise("Unrecognised character '$value'")
        }
        if (next != null) tokens += next
    }

	mark()
    return tokens + token("End_of_input")
}

private fun Lexer.raise(msg: String): Nothing {
	rewindToMark()
	throw LexerException("Error: $msg at $position: ${skipTo("\n") { it }}")
}

