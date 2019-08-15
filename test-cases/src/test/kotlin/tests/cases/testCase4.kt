package tests.cases

import tests.testCase

val testCase4 = testCase(
	"test case 4","""
/*** test printing, embedded \n and comments with lots of '*' ***/
print(42);
print("\nHello World\nGood Bye\nok\n");
print("Print a slash n - \\n.\n");
""","""
    2      1   Keyword_print
    2      6   LeftParen
    2      7   Integer 42
    2      9   RightParen
    2     10   Semicolon
    3      1   Keyword_print
    3      6   LeftParen
    3      7   String "\nHello World\nGood Bye\nok\n"
    3     38   RightParen
    3     39   Semicolon
    4      1   Keyword_print
    4      6   LeftParen
    4      7   String "Print a slash n - \\n.\n"
    4     33   RightParen
    4     34   Semicolon
    5      1   End_of_input
""","""
Sequence
Sequence
Sequence
;
Sequence
;
Prti
Integer 42
;
Sequence
;
Prts
String "\nHello World\nGood Bye\nok\n"
;
Sequence
;
Prts
String "Print a slash n - \\n.\n"
;
""","""
Datasize: 0 Strings: 2
"\nHello World\nGood Bye\nok\n"
"Print a slash n - \\n.\n"
   0 push  42
   5 prti
   6 push  0
  11 prts
  12 push  1
  17 prts
  18 halt
""","""
42
Hello World
Good Bye
ok
Print a slash n - \n.
"""
)