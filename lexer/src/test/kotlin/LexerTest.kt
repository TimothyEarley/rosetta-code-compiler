package lexer

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.AbstractAnnotationSpec
import io.kotlintest.specs.StringSpec

class LexerTest : StringSpec({

	"test edge cases" {
		lexer("""
			ifprint

		""".trimIndent()) shouldBe """
    1      1   Identifier ifprint
    2      1   End_of_input
""".trim('\n')
	}

	"no whitspace error" {
		val error = shouldThrow<LexerException> { lexer("42fred") }
		error.message shouldBe "Error: Invalid number. Starts like a number, but ends in non-numeric characters at line 1, col 1: 42fred"
	}

	"Multi-character constant" {
		val error = shouldThrow<LexerException> { lexer("'xx'") }
		error.message shouldBe "Error: Multi-character constant at line 1, col 1: 'xx'"
	}

	"Unknown escape sequence char" {
		val error = shouldThrow<LexerException> { lexer("'\\r'") }
		error.message shouldBe "Error: Unknown escape sequence at line 1, col 1: '\\r'"
	}

	"Unknown escape sequence string".config(enabled = false) {
		val error = shouldThrow<LexerException> { lexer("\"\\r\"") }
		error.message shouldBe "Error: Unknown escape sequence at line 1, col 1: '\\r'"
	}

	"End-of-file in comment. Closing comment characters not found" {
		val error = shouldThrow<LexerException> { lexer("/* foo bar\n bar foo ") }
		error.message shouldBe "Error: End-of-file in comment. Closing comment characters not found at line 1, col 1: /* foo bar"
	}

	"End-of-file while scanning string literal. Closing string character not found" {
		val error = shouldThrow<LexerException> { lexer("\"foo bar") }
		error.message shouldBe "Error: End-of-file while scanning string literal. Closing string character not found at line 1, col 1: \"foo bar"
	}

	"End-of-file while scanning character literal. Closing string character not found" {
		val error = shouldThrow<LexerException> { lexer("\'x") }
		error.message shouldBe "Error: End-of-file while scanning character literal. Closing string character not found at line 1, col 1: 'x"
	}

	"Empty character constant" {
		val error = shouldThrow<LexerException> { lexer("''") }
		error.message shouldBe "Error: Empty character constant at line 1, col 1: ''"
	}

})