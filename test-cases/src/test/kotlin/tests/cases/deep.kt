package tests.cases

import tests.testCase

val deep = testCase(
	"deep","""
print(---------------------------------+++5, "\n");
print(((((((((3 + 2) * ((((((2))))))))))))), "\n");
 
if (1) { if (1) { if (1) { if (1) { if (1) { print(15, "\n"); } } } } }
""","""
    1      1   Keyword_print
    1      6   LeftParen
    1      7   Op_subtract
    1      8   Op_subtract
    1      9   Op_subtract
    1     10   Op_subtract
    1     11   Op_subtract
    1     12   Op_subtract
    1     13   Op_subtract
    1     14   Op_subtract
    1     15   Op_subtract
    1     16   Op_subtract
    1     17   Op_subtract
    1     18   Op_subtract
    1     19   Op_subtract
    1     20   Op_subtract
    1     21   Op_subtract
    1     22   Op_subtract
    1     23   Op_subtract
    1     24   Op_subtract
    1     25   Op_subtract
    1     26   Op_subtract
    1     27   Op_subtract
    1     28   Op_subtract
    1     29   Op_subtract
    1     30   Op_subtract
    1     31   Op_subtract
    1     32   Op_subtract
    1     33   Op_subtract
    1     34   Op_subtract
    1     35   Op_subtract
    1     36   Op_subtract
    1     37   Op_subtract
    1     38   Op_subtract
    1     39   Op_subtract
    1     40   Op_add
    1     41   Op_add
    1     42   Op_add
    1     43   Integer              5
    1     44   Comma
    1     46   String          "\n"
    1     50   RightParen
    1     51   Semicolon
    2      1   Keyword_print
    2      6   LeftParen
    2      7   LeftParen
    2      8   LeftParen
    2      9   LeftParen
    2     10   LeftParen
    2     11   LeftParen
    2     12   LeftParen
    2     13   LeftParen
    2     14   LeftParen
    2     15   Integer              3
    2     17   Op_add
    2     19   Integer              2
    2     20   RightParen
    2     22   Op_multiply
    2     24   LeftParen
    2     25   LeftParen
    2     26   LeftParen
    2     27   LeftParen
    2     28   LeftParen
    2     29   LeftParen
    2     30   Integer              2
    2     31   RightParen
    2     32   RightParen
    2     33   RightParen
    2     34   RightParen
    2     35   RightParen
    2     36   RightParen
    2     37   RightParen
    2     38   RightParen
    2     39   RightParen
    2     40   RightParen
    2     41   RightParen
    2     42   RightParen
    2     43   RightParen
    2     44   Comma
    2     46   String          "\n"
    2     50   RightParen
    2     51   Semicolon
    4      1   Keyword_if
    4      4   LeftParen
    4      5   Integer              1
    4      6   RightParen
    4      8   LeftBrace
    4     10   Keyword_if
    4     13   LeftParen
    4     14   Integer              1
    4     15   RightParen
    4     17   LeftBrace
    4     19   Keyword_if
    4     22   LeftParen
    4     23   Integer              1
    4     24   RightParen
    4     26   LeftBrace
    4     28   Keyword_if
    4     31   LeftParen
    4     32   Integer              1
    4     33   RightParen
    4     35   LeftBrace
    4     37   Keyword_if
    4     40   LeftParen
    4     41   Integer              1
    4     42   RightParen
    4     44   LeftBrace
    4     46   Keyword_print
    4     51   LeftParen
    4     52   Integer             15
    4     54   Comma
    4     56   String          "\n"
    4     60   RightParen
    4     61   Semicolon
    4     63   RightBrace
    4     65   RightBrace
    4     67   RightBrace
    4     69   RightBrace
    4     71   RightBrace
    5      1   End_of_input
""","""
Sequence
Sequence
Sequence
;
Sequence
Sequence
;
Prti
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Negate
Integer       5
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
Prts
String        "\n"
;
Sequence
Sequence
;
Prti
Multiply
Add
Integer       3
Integer       2
Integer       2
;
Prts
String        "\n"
;
If
Integer       1
If
Sequence
;
If
Integer       1
If
Sequence
;
If
Integer       1
If
Sequence
;
If
Integer       1
If
Sequence
;
If
Integer       1
If
Sequence
;
Sequence
Sequence
;
Prti
Integer       15
;
Prts
String        "\n"
;
;
;
;
;
;
""","""
Datasize: 0 Strings: 1
"\n"
   0 push  5
   1 neg
   2 neg
   3 neg
   4 neg
   5 neg
   6 neg
   7 neg
   8 neg
   9 neg
  10 neg
  11 neg
  12 neg
  13 neg
  14 neg
  15 neg
  16 neg
  17 neg
  18 neg
  19 neg
  20 neg
  21 neg
  22 neg
  23 neg
  24 neg
  25 neg
  26 neg
  27 neg
  28 neg
  29 neg
  30 neg
  31 neg
  32 neg
  33 neg
  34 prti
  35 push  0
  36 prts
  37 push  3
  38 push  2
  39 add
  40 push  2
  41 mul
  42 prti
  43 push  0
  44 prts
  45 push  1
  46 jz     (12) 59
  47 push  1
  48 jz     (10) 59
  49 push  1
  50 jz     (8) 59
  51 push  1
  52 jz     (6) 59
  53 push  1
  54 jz     (4) 59
  55 push  15
  56 prti
  57 push  0
  58 prts
  59 halt
""","""
-5
10
15
"""
)