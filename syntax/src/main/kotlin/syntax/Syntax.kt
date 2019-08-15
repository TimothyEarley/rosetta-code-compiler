package syntax

import syntax.pc.ParserState
import syntax.pc.fold
import syntax.pc.run
import java.io.File


fun main(args: Array<String>) {
	require(args.size == 1) { "Please pass a file as the first argument" }
	val source = File(args[0]).readText()
	println(syntax(source))
}

fun syntax(input: String): String {
	val tokens = input.lineSequence().map(::readToken)
	return Grammer.parser.run(tokens).fold(
		ParserState.Error::msg,
		AST::toFlatString
	)
}