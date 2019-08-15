package tests.cases

import tests.testCase

val gcd = testCase(
	"gcd","""
/* Compute the gcd of 1071, 1029:  21 */

a = 1071;
b = 1029;

while (b != 0) {
    new_a = b;
    b     = a % b;
    a     = new_a;
}
print(a);
""","""
    3      1 Identifier      a
    3      3 Op_assign
    3      5 Integer          1071
    3      9 Semicolon
    4      1 Identifier      b
    4      3 Op_assign
    4      5 Integer          1029
    4      9 Semicolon
    6      1 Keyword_while
    6      7 LeftParen
    6      8 Identifier      b
    6     10 Op_notequal
    6     13 Integer             0
    6     14 RightParen
    6     16 LeftBrace
    7      5 Identifier      new_a
    7     11 Op_assign
    7     13 Identifier      b
    7     14 Semicolon
    8      5 Identifier      b
    8     11 Op_assign
    8     13 Identifier      a
    8     15 Op_mod
    8     17 Identifier      b
    8     18 Semicolon
    9      5 Identifier      a
    9     11 Op_assign
    9     13 Identifier      new_a
    9     18 Semicolon
   10      1 RightBrace
   11      1 Keyword_print
   11      6 LeftParen
   11      7 Identifier      a
   11      8 RightParen
   11      9 Semicolon
   12      1 End_of_input
""","""
Sequence
Sequence
Sequence
Sequence
;
Assign
Identifier     a
Integer        1071
Assign
Identifier     b
Integer        1029
While
NotEqual
Identifier     b
Integer        0
Sequence
Sequence
Sequence
;
Assign
Identifier     new_a
Identifier     b
Assign
Identifier     b
Mod
Identifier     a
Identifier     b
Assign
Identifier     a
Identifier     new_a
Sequence
;
Prti
Identifier     a
;
""","""
Datasize: 3 Strings: 0
    0 push  1071
    5 store [0]
   10 push  1029
   15 store [1]
   20 fetch [1]
   25 push  0
   30 ne
   31 jz     (45) 77
   36 fetch [1]
   41 store [2]
   46 fetch [0]
   51 fetch [1]
   56 mod
   57 store [1]
   62 fetch [2]
   67 store [0]
   72 jmp    (-53) 20
   77 fetch [0]
   82 prti
   83 halt
""","""

"""
)