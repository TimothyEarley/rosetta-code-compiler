package tests.cases

import tests.testCase

val phoenixNumber = testCase(
	"phoenix number",
	"""
/*
  Show Ident and Integers
 */
phoenix_number = 142857;
print(phoenix_number, "\n");
""","""
    4      1   Identifier phoenix_number
    4     16   Op_assign 
    4     18   Integer 142857
    4     24   Semicolon 
    5      1   Keyword_print 
    5      6   LeftParen 
    5      7   Identifier phoenix_number
    5     21   Comma 
    5     23   String "\n"
    5     27   RightParen 
    5     28   Semicolon 
    6      1   End_of_input 
""","""
Sequence
Sequence
;
Assign
Identifier phoenix_number
Integer 142857
Sequence
Sequence
;
Prti
Identifier phoenix_number
;
Prts
String "\n"
;
""","""
		Datasize: 1 Strings: 1
		"\n"
		   0 push  142857
		   5 store [0]
		  10 fetch [0]
		  15 prti
		  16 push  0
		  21 prts
		  22 halt
	""".trimIndent(),"""
		142857
	""".trimIndent()
)