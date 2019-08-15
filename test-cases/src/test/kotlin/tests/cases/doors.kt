package tests.cases

import tests.testCase

val doors = testCase(
	"doors","""
/* 100 Doors */
i = 1;
while (i * i <= 100) {
    print("door ", i * i, " is open\n");
    i = i + 1;
}
""","""
    2      1 Identifier      i
    2      3 Op_assign
    2      5 Integer             1
    2      6 Semicolon
    3      1 Keyword_while
    3      7 LeftParen
    3      8 Identifier      i
    3     10 Op_multiply
    3     12 Identifier      i
    3     14 Op_lessequal
    3     17 Integer           100
    3     20 RightParen
    3     22 LeftBrace
    4      5 Keyword_print
    4     10 LeftParen
    4     11 String          "door "
    4     18 Comma
    4     20 Identifier      i
    4     22 Op_multiply
    4     24 Identifier      i
    4     25 Comma
    4     27 String          " is open\n"
    4     39 RightParen
    4     40 Semicolon
    5      5 Identifier      i
    5      7 Op_assign
    5      9 Identifier      i
    5     11 Op_add
    5     13 Integer             1
    5     14 Semicolon
    6      1 RightBrace
    7      1 End_of_input
""","""
Sequence
Sequence
;
Assign
Identifier     i
Integer        1
While
LessEqual
Multiply
Identifier     i
Identifier     i
Integer        100
Sequence
Sequence
;
Sequence
Sequence
Sequence
;
Prts
String         "door "
;
Prti
Multiply
Identifier     i
Identifier     i
;
Prts
String         " is open\n"
;
Assign
Identifier     i
Add
Identifier     i
Integer        1
""","""
Datasize: 1 Strings: 2
"door "
" is open\n"
    0 push  1
    1 store [0]
    2 fetch [0]
    3 fetch [0]
    4 mul
    5 push  100
    6 le
    7 jz     (13) 21
    8 push  0
    9 prts
   10 fetch [0]
   11 fetch [0]
   12 mul
   13 prti
   14 push  1
   15 prts
   16 fetch [0]
   17 push  1
   18 add
   19 store [0]
   20 jmp    (-17) 2
   21 halt
""","""
door 1 is open
door 4 is open
door 9 is open
door 16 is open
door 25 is open
door 36 is open
door 49 is open
door 64 is open
door 81 is open
door 100 is open
"""
)