package lexer

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.AbstractAnnotationSpec
import io.kotlintest.specs.StringSpec

class LexerTest : StringSpec({

	"test 1" {
		lexer("""
				/*
				  Hello world
				*/
				print("Hello, World!\n");

				""".trimIndent()
		) shouldBe """
				4 1 Keyword_print
				4 6 LeftParen
				4 7 String "Hello, World!\n"
				4 24 RightParen
				4 25 Semicolon
				5 1 End_of_input""".trimIndent()
	}

	"test 2" { lexer("""
				/*
				  Show Ident and Integers
				 */
				phoenix_number = 142857;
				print(phoenix_number, "\n");

				""".trimIndent()) shouldBe """
				4 1 Identifier phoenix_number
				4 16 Op_assign
				4 18 Integer 142857
				4 24 Semicolon
				5 1 Keyword_print
				5 6 LeftParen
				5 7 Identifier phoenix_number
				5 21 Comma
				5 23 String "\n"
				5 27 RightParen
				5 28 Semicolon
				6 1 End_of_input""".trimIndent()
	}

	"test 3" {
		lexer("""
			/*
			  All lexical tokens - not syntactically correct, but that will
			  have to wait until syntax analysis
			 */
			/* Print   */  print    /* Sub     */  -
			/* Putc    */  putc     /* Lss     */  <
			/* If      */  if       /* Gtr     */  >
			/* Else    */  else     /* Leq     */  <=
			/* While   */  while    /* Geq     */  >=
			/* Lbrace  */  {        /* Eq      */  ==
			/* Rbrace  */  }        /* Neq     */  !=
			/* Lparen  */  (        /* And     */  &&
			/* Rparen  */  )        /* Or      */  ||
			/* Uminus  */  -        /* Semi    */  ;
			/* Not     */  !        /* Comma   */  ,
			/* Mul     */  *        /* Assign  */  =
			/* Div     */  /        /* Integer */  42
			/* Mod     */  %        /* String  */  "String literal"
			/* Add     */  +        /* Ident   */  variable_name
			/* character literal */  '\n'
			/* character literal */  '\\'
			/* character literal */  ' '

		""".trimIndent()) shouldBe """
			5 16 Keyword_print
			5 40 Op_subtract
			6 16 Keyword_putc
			6 40 Op_less
			7 16 Keyword_if
			7 40 Op_greater
			8 16 Keyword_else
			8 40 Op_lessequal
			9 16 Keyword_while
			9 40 Op_greaterequal
			10 16 LeftBrace
			10 40 Op_equal
			11 16 RightBrace
			11 40 Op_notequal
			12 16 LeftParen
			12 40 Op_and
			13 16 RightParen
			13 40 Op_or
			14 16 Op_subtract
			14 40 Semicolon
			15 16 Op_not
			15 40 Comma
			16 16 Op_multiply
			16 40 Op_assign
			17 16 Op_divide
			17 40 Integer 42
			18 16 Op_mod
			18 40 String "String literal"
			19 16 Op_add
			19 40 Identifier variable_name
			20 26 Integer 10
			21 26 Integer 92
			22 26 Integer 32
			23 1 End_of_input
		""".trimIndent()
	}


	"test 4" {
		lexer("""
			/*** test printing, embedded \n and comments with lots of '*' ***/
			print(42);
			print("\nHello World\nGood Bye\nok\n");
			print("Print a slash n - \\n.\n");

		""".trimIndent()) shouldBe """
			2 1 Keyword_print
			2 6 LeftParen
			2 7 Integer 42
			2 9 RightParen
			2 10 Semicolon
			3 1 Keyword_print
			3 6 LeftParen
			3 7 String "\nHello World\nGood Bye\nok\n"
			3 38 RightParen
			3 39 Semicolon
			4 1 Keyword_print
			4 6 LeftParen
			4 7 String "Print a slash n - \\n.\n"
			4 33 RightParen
			4 34 Semicolon
			5 1 End_of_input
		""".trimIndent()
	}


	"test edge cases" {
		lexer("""
			ifprint

		""".trimIndent()) shouldBe """
			1 1 Identifier ifprint
			2 1 End_of_input
		""".trimIndent()
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