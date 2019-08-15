package tests.cases

import tests.testCase

val helloWorld = testCase(
	"hello world",
	"""
/*
  Hello world
 */
print("Hello, World!\n");
""", """
    4      1   Keyword_print
    4      6   LeftParen
    4      7   String "Hello, World!\n"
    4     24   RightParen
    4     25   Semicolon
    5      1   End_of_input
""", """
Sequence
;
Sequence
;
Prts
String "Hello, World!\n"
;
""", """
Datasize: 0 Strings: 1
"Hello, World!\n"
   0 push  0
   5 prts
   6 halt
""", """
Hello, World!
"""
)