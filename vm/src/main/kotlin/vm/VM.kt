package vm

import java.io.File
import java.util.*

fun main(args: Array<String>) {
	require(args.size == 1) { "Please pass a file as the first argument" }
	val source = File(args[0]).readText()
	run(source, System.out)
}

private val headerRegex = Regex("Datasize: (?<datasize>\\d+) Strings: (?<strings>\\d+)")
private val instructionRegex = Regex("\\s*(?<line>\\d+)\\s*(?<instruction>\\w+)\\s*(?<arg>.*)")
private val jumpRegex = Regex("\\(?-?\\d+\\) (?<target>\\d+)")

class VmState(
	var pc: Int = 0,
	var running: Boolean = true,
	val stack: Deque<Int> = ArrayDeque(),
	val out: Appendable,
	val strings: List<String>,
	dataSize: Int
) {
	val data: IntArray = IntArray(dataSize)
	override fun toString(): String = "State(pc=$pc, running=$running, stack=${stack.toList().joinToString(separator = "|", prefix = "|", postfix = ">")}, data=${data.joinToString(separator = "|")}, out=$out)"
}

typealias Instruction = VmState.() -> Unit

fun run(code: String): String = buildString {
	run(code, this)
}

fun run(byteCode: String, out: Appendable) {
	val lines = byteCode.split('\n')

	// prep

	val header = lines[0]
	val match = headerRegex.matchEntire(header) ?: error("Malformed header: $header")
	val datasize = match.groups["datasize"]!!.value.toInt()
	val strings = match.groups["strings"]!!.value.toInt()

	val constantStrings = lines.slice(1..strings).map {
		it.removeSurrounding("\"").unescape()
	}

	val code = lines.drop(1 + strings).map {
		val (line, instruction, args) = (instructionRegex.matchEntire(it) ?: error("Malformed line: $it")).destructured
		line.toInt() to parseInstruction(instruction, args)
	}.toMap()

	val state = VmState(dataSize = datasize, out = out, strings = constantStrings)

	// execute
	while (state.running) {
		val instruction = code[state.pc++] ?: error("Segmentation fault at ${state.pc}")
		instruction(state)
	}

}

private fun String.unescape(): String {
	var i = 0
	val out = StringBuilder()
	while (i < length) {
		val c = this[i++]
		out.append(when (c) {
			'\\' -> when (this[i++]) {
				'n' -> '\n'
				'\\' -> '\\'
				else -> error("Illegal escape sequence in $this")
			}
			else -> c
		})
	}
	return out.toString()
}

private fun parseInstruction(instruction: String, args: String): Instruction = when (instruction) {
	"push" -> {
		val value = args.toInt();
		{ stack.push(value) }
	}
	"prts" -> {{ out.append(strings[stack.pop()]) }}
	"prti" -> {{ out.append(stack.pop().toString()) }}
	"halt" -> {{ running = false }}
	"store" -> {
		val index = index(args);
		{ data[index] = stack.pop() }
	}
	"fetch" -> {
		val index = index(args);
		{ stack.push(data[index]) }
	}
	"lt" -> boolOp { a, b -> a < b }
	"le" -> boolOp { a,b -> a <= b }
	"ne" -> boolOp { a,b -> a != b}
	"add" -> op(Int::plus)
	"mul" -> op(Int::times)
	"div" -> op(Int::div)
	"mod" -> op(Int::rem)
	"neg" -> {{stack.push(-stack.pop())}}
	"jz" -> {
		val target = jumpTarget(args);
		{ if (stack.pop() == 0) pc = target }
	}
	"jmp" -> {
		val target = jumpTarget(args);
		{ pc = target }
	}
	else -> error("Unknown instruction $instruction $args")
}

fun boolOp(f: (Int, Int) -> Boolean): Instruction = op { a,b -> f(a,b).toInt() }
fun op(f: (Int, Int) -> Int): Instruction = {
	val first = stack.pop()
	val second = stack.pop()
	stack.push(f(second, first))
}

private fun index(args: String) = args.removeSurrounding("[", "]").toInt()
private fun jumpTarget(args: String) =
	jumpRegex.matchEntire(args)?.groups?.get("target")?.value?.toInt()
		?: error("Malformed jump target: $args")


private fun Boolean.toInt() = if (this) 1 else 0