package gen

import java.io.File
import java.util.*
import kotlin.math.abs

fun main(args: Array<String>) {
	require(args.size == 1) { "Please pass a file as the first argument" }
	val source = File(args[0]).readText()
	println(gen(source))
}

fun gen(input: String): String {
	val ast = readAST(input.lineSequence().filterNot(String::isBlank))

	return (ast.compile() + "halt").print()
}

fun readAST(lines: Sequence<String>): AST {

	fun Sequence<String>.recurse(): Pair<AST, Sequence<String>> {
		val split = first().split("\\s+".toRegex(), 2)
		val (type, value) = if (split.size == 2) split else split + ""

		val tail = drop(1)
		return when (type) {
			";" -> AST.EMPTY to tail
			in listOf(
				"Sequence",
				"Prts",
				"Prti",
				"Prtc",
				"Assign",
				"While",
				"Less",
				"LessEqual",
				"NotEqual",
				"Add",
				"Negate",
				"Greater",
				"Divide",
				"Multiply",
				"Mod",
				"If", //TODO check
				"Subtract"
			) -> {
				val (first, firstLines) = tail.recurse()
				val (second, secondLines) = firstLines.recurse()
				AST.Binary(type, first, second) to secondLines
			}
			in listOf(
				"String",
				"Identifier",
				"Integer"
			) -> AST.Terminal(type, value) to tail
			else -> error("Unknown type $type")
		}
	}

	return lines.recurse().also { (_, lines) ->
		require(lines.toList().isEmpty()) { "Not everything was parsed: ${lines.toList()}" }
	}.first
}

sealed class AST {
	object EMPTY : AST()
	data class Binary(val name: String, val left: AST, val right: AST): AST()
	data class Terminal(val name: String, val value: String): AST()
}

data class Instruction(val address: Int, var name: String)

data class Assembly(
	val variables: Map<String, Int>,
	val constantStrings: List<String>,
	val code: List<Instruction>
) {

	val pc: Int get() = code.size

	fun print(): String =
		"Datasize: ${variables.size} Strings: ${constantStrings.size}\n" +
		(if (constantStrings.isNotEmpty()) constantStrings.joinToString(separator = "\n", postfix = "\n") else "") +
		code.joinToString(transform = { "%4d %-4s".format(it.address, it.name).trimEnd() }, separator = "\n")

	fun addConstantString(string: String): Assembly =
		if (constantStrings.contains(string)) this
		else this.copy(constantStrings = constantStrings + string)

	fun addVariable(string: String): Assembly =
		if (variables.contains(string)) this
		else this.copy(variables = variables + (string to variables.size))

	operator fun plus(name: String): Assembly = this.copy(
		code = code + Instruction(code.size, name)
	)

}

fun AST.compile(state: Assembly = Assembly(emptyMap(), emptyList(), emptyList())): Assembly = when(this) {
	AST.EMPTY -> state
	is AST.Binary -> when (name) {
		"Sequence" -> left.compile(state).let { right.compile(it) }
		//TODO merge all the cases of compiler right, compile left, do instruction
		"Prts" -> left.compile(state) + "prts"
		"Prti" -> left.compile(state) + "prti"
		"Prtc" -> left.compile(state) + "prtc"
		"Assign" -> (left as AST.Terminal).value.let { id ->
			right.compile(state).addVariable(id).let {
				it + "store [${it.variables[id]}]"
			}
		}
		"While" -> {
			val start = state.pc
			lateinit var jzPlaceholder: Instruction
			// test
			(left.compile(state) +
					// jz to end
					"jz placeholder"
					).let {
				jzPlaceholder = it.code.last()
				// body
				right.compile(it)
			}.let {
				// jmp back
				jzPlaceholder.name = "jz ${jumpDescriptor(jzPlaceholder.address, it.pc + 1)}"
				it + "jmp ${jumpDescriptor(it.pc, start)}"
			}
		}
		"If" -> {
			lateinit var jzPlaceholder: Instruction
			(left.compile(state) + "jz placeholder").let {
				jzPlaceholder = it.code.last()
				val (_, ifBody, elseBody) = right as AST.Binary
				ifBody.compile(it).let {
					jzPlaceholder.name = "jz ${jumpDescriptor(jzPlaceholder.address, it.pc)}"
					it //TODO else
				}
			}
		}
		"Less" -> left.compile(state).let { right.compile(it) + "lt" }
		"LessEqual" -> left.compile(state).let { right.compile(it) + "le" }
		"Equal" -> left.compile(state).let { right.compile(it) + "eq" }
		"NotEqual" -> left.compile(state).let { right.compile(it) + "ne" }

		"Greater" -> left.compile(state).let { right.compile(it) + "gt" }
		"Add" -> left.compile(state).let { right.compile(it) + "add" }
		"Subtract" -> left.compile(state).let { right.compile(it) + "sub" }
		"Divide" -> left.compile(state).let { right.compile(it) + "div" }
		"Multiply" -> left.compile(state).let { right.compile(it) + "mul" }
		"Mod" -> left.compile(state).let { right.compile(it) + "mod" }
		"Negate" -> left.compile(state) + "neg"

		else -> TODO(name)
	}
	is AST.Terminal -> when(name) {
		"String" -> state.addConstantString(value).let {
			it + "push ${it.constantStrings.indexOf(value)}"
		}
		"Integer" -> state + "push $value"
		"Identifier" -> state + "fetch [${state.variables[value]}]"
		else -> TODO(this.toString())
	}
}

private fun jumpDescriptor(from: Int, to: Int) = if (from > to) {
	"(${to - from + 1}) $to"
} else {
	"(${to - from - 1}) $to"
}