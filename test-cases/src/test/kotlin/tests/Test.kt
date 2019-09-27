package tests

import gen.gen
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import io.kotlintest.tables.row
import lexer.lexer
import syntax.syntax
import tests.cases.*
import java.util.*

//TODO this overwrites spaces in strings
private fun String.normaliseWhitespace() = replace(" {2,}".toRegex(), " ")

fun testCase(
	name: String,
	source: String,
	lex: String,
	parse: String,
	gen: String?,
	out: String?
) = row(
	name,
	source.trimStart('\n'),
	lex.trim('\n').normaliseWhitespace(),
	parse.trimStart('\n').normaliseWhitespace(),
	gen?.trim('\n')?.normaliseWhitespace(),
	out?.trimStart('\n')
)

class Test : FreeSpec({

	forall(
		helloWorld,
		phoenixNumber,
		allSymbols,
		testCase4,
		count,
		doors,
		negatives,
		deep,
		gcd
//		mandelbrot TODO: wrong instructions being emitted or is the test wrong?
	) { name, source, lex, parse, gen, out ->
		//TODO gradle test runner does not support nested tests - use IntelliJ
		name - {
			"lexer"  {
				lexer(source).normaliseWhitespace() shouldBe lex
			}

			"syntax" {
				syntax(lex).normaliseWhitespace() shouldBe parse
			}

			"code gen".config(enabled = gen != null) {
				gen(parse).normaliseWhitespace() shouldBe gen!!
			}

			"out".config(enabled = gen != null && out != null) {
				vm.run(gen!!) shouldBe out!!
			}
		}
	}

})