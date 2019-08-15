package tests.cases

import tests.testCase

val negatives = testCase(
	"negatives","""
a = (-1 * ((-1 * (5 * 15)) / 10));
print(a, "\n");
b = -a;
print(b, "\n");
print(-b, "\n");
print(-(1), "\n");
""","""
    1      1   Identifier      a
    1      3   Op_assign
    1      5   LeftParen
    1      6   Op_subtract
    1      7   Integer              1
    1      9   Op_multiply
    1     11   LeftParen
    1     12   LeftParen
    1     13   Op_subtract
    1     14   Integer              1
    1     16   Op_multiply
    1     18   LeftParen
    1     19   Integer              5
    1     21   Op_multiply
    1     23   Integer             15
    1     25   RightParen
    1     26   RightParen
    1     28   Op_divide
    1     30   Integer             10
    1     32   RightParen
    1     33   RightParen
    1     34   Semicolon
    2      1   Keyword_print
    2      6   LeftParen
    2      7   Identifier      a
    2      8   Comma
    2     10   String          "\n"
    2     14   RightParen
    2     15   Semicolon
    3      1   Identifier      b
    3      3   Op_assign
    3      5   Op_subtract
    3      6   Identifier      a
    3      7   Semicolon
    4      1   Keyword_print
    4      6   LeftParen
    4      7   Identifier      b
    4      8   Comma
    4     10   String          "\n"
    4     14   RightParen
    4     15   Semicolon
    5      1   Keyword_print
    5      6   LeftParen
    5      7   Op_subtract
    5      8   Identifier      b
    5      9   Comma
    5     11   String          "\n"
    5     15   RightParen
    5     16   Semicolon
    6      1   Keyword_print
    6      6   LeftParen
    6      7   Op_subtract
    6      8   LeftParen
    6      9   Integer              1
    6     10   RightParen
    6     11   Comma
    6     13   String          "\n"
    6     17   RightParen
    6     18   Semicolon
    7      1   End_of_input
""","""
Sequence
Sequence
Sequence
Sequence
Sequence
Sequence
;
Assign
Identifier    a
Multiply
Negate
Integer       1
;
Divide
Multiply
Negate
Integer       1
;
Multiply
Integer       5
Integer       15
Integer       10
Sequence
Sequence
;
Prti
Identifier    a
;
Prts
String        "\n"
;
Assign
Identifier    b
Negate
Identifier    a
;
Sequence
Sequence
;
Prti
Identifier    b
;
Prts
String        "\n"
;
Sequence
Sequence
;
Prti
Negate
Identifier    b
;
;
Prts
String        "\n"
;
Sequence
Sequence
;
Prti
Negate
Integer       1
;
;
Prts
String        "\n"
;
""","""
"\n"
   0 push  1
   5 neg
   6 push  1
  11 neg
  12 push  5
  17 push  15
  22 mul
  23 mul
  24 push  10
  29 div
  30 mul
  31 store [0]
  36 fetch [0]
  41 prti
  42 push  0
  47 prts
  48 fetch [0]
  53 neg
  54 store [1]
  59 fetch [1]
  64 prti
  65 push  0
  70 prts
  71 fetch [1]
  76 neg
  77 prti
  78 push  0
  83 prts
  84 push  1
  89 neg
  90 prti
  91 push  0
  96 prts
  97 halt
""","""
7
-7
7
-1
"""
)