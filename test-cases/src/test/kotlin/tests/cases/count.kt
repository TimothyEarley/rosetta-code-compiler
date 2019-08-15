package tests.cases

import tests.testCase

val count = testCase(
	"count","""
count = 1;
while (count < 10) {
    print("count is: ", count, "\n");
    count = count + 1;
}
""","""
    1      1   Identifier      count
    1      7   Op_assign
    1      9   Integer              1
    1     10   Semicolon
    2      1   Keyword_while
    2      7   LeftParen
    2      8   Identifier      count
    2     14   Op_less
    2     16   Integer             10
    2     18   RightParen
    2     20   LeftBrace
    3      5   Keyword_print
    3     10   LeftParen
    3     11   String          "count is: "
    3     23   Comma
    3     25   Identifier      count
    3     30   Comma
    3     32   String          "\n"
    3     36   RightParen
    3     37   Semicolon
    4      5   Identifier      count
    4     11   Op_assign
    4     13   Identifier      count
    4     19   Op_add
    4     21   Integer              1
    4     22   Semicolon
    5      1   RightBrace
    6      1   End_of_input
""","""
Sequence
Sequence
;
Assign
Identifier    count
Integer       1
While
Less
Identifier    count
Integer       10
Sequence
Sequence
;
Sequence
Sequence
Sequence
;
Prts
String        "count is: "
;
Prti
Identifier    count
;
Prts
String        "\n"
;
Assign
Identifier    count
Add
Identifier    count
Integer       1
""","""
Datasize: 1 Strings: 2
"count is: "
"\n"
   0 push  1
   1 store [0]
   2 fetch [0]
   3 push  10
   4 lt
   5 jz     (11) 17
   6 push  0
   7 prts
   8 fetch [0]
   9 prti
  10 push  1
  11 prts
  12 fetch [0]
  13 push  1
  14 add
  15 store [0]
  16 jmp    (-13) 2
  17 halt
""","""
count is: 1
count is: 2
count is: 3
count is: 4
count is: 5
count is: 6
count is: 7
count is: 8
count is: 9
"""
)