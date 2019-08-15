package tests

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import io.kotlintest.tables.row
import lexer.lexer
import syntax.syntax
import tests.cases.allSymbols
import tests.cases.helloWorld
import tests.cases.phoenixNumber
import java.util.*

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
	lex.trim('\n'),
	parse.trimStart('\n'),
	gen?.trim('\n'),
	out?.trim('\n')
)

class Test : FreeSpec({

	forall(
		helloWorld,
		phoenixNumber,
		allSymbols
	) { name, source, lex, parse, gen, out ->
		//TODO gradle test runner does not support nested tests - use IntelliJ
		name - {
			"lexer"  {
				lexer(source) shouldBe lex
			}

			"syntax" {
				syntax(lex) shouldBe parse
			}

			"! code gen" {
				TODO()
			}

			"! out" {
				TODO()
			}
		}
	}

})