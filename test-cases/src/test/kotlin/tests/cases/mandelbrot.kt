package tests.cases

import tests.testCase

val mandelbrot = testCase(
	"mandelbrot","""
{
/*
 This is an integer ascii Mandelbrot generator
 */
    left_edge   = -420;
    right_edge  =  300;
    top_edge    =  300;
    bottom_edge = -300;
    x_step      =    7;
    y_step      =   15;
 
    max_iter    =  200;
 
    y0 = top_edge;
    while (y0 > bottom_edge) {
        x0 = left_edge;
        while (x0 < right_edge) {
            y = 0;
            x = 0;
            the_char = ' ';
            i = 0;
            while (i < max_iter) {
                x_x = (x * x) / 200;
                y_y = (y * y) / 200;
                if (x_x + y_y > 800 ) {
                    the_char = '0' + i;
                    if (i > 9) {
                        the_char = '@';
                    }
                    i = max_iter;
                }
                y = x * y / 100 + y0;
                x = x_x - y_y + x0;
                i = i + 1;
            }
            putc(the_char);
            x0 = x0 + x_step;
        }
        putc('\n');
        y0 = y0 - y_step;
    }
}
""","""
   1      1 LeftBrace
    5      5 Identifier      left_edge
    5     17 Op_assign
    5     19 Op_subtract
    5     20 Integer           420
    5     23 Semicolon
    6      5 Identifier      right_edge
    6     17 Op_assign
    6     20 Integer           300
    6     23 Semicolon
    7      5 Identifier      top_edge
    7     17 Op_assign
    7     20 Integer           300
    7     23 Semicolon
    8      5 Identifier      bottom_edge
    8     17 Op_assign
    8     19 Op_subtract
    8     20 Integer           300
    8     23 Semicolon
    9      5 Identifier      x_step
    9     17 Op_assign
    9     22 Integer             7
    9     23 Semicolon
   10      5 Identifier      y_step
   10     17 Op_assign
   10     21 Integer            15
   10     23 Semicolon
   12      5 Identifier      max_iter
   12     17 Op_assign
   12     20 Integer           200
   12     23 Semicolon
   14      5 Identifier      y0
   14      8 Op_assign
   14     10 Identifier      top_edge
   14     18 Semicolon
   15      5 Keyword_while
   15     11 LeftParen
   15     12 Identifier      y0
   15     15 Op_greater
   15     17 Identifier      bottom_edge
   15     28 RightParen
   15     30 LeftBrace
   16      9 Identifier      x0
   16     12 Op_assign
   16     14 Identifier      left_edge
   16     23 Semicolon
   17      9 Keyword_while
   17     15 LeftParen
   17     16 Identifier      x0
   17     19 Op_less
   17     21 Identifier      right_edge
   17     31 RightParen
   17     33 LeftBrace
   18     13 Identifier      y
   18     15 Op_assign
   18     17 Integer             0
   18     18 Semicolon
   19     13 Identifier      x
   19     15 Op_assign
   19     17 Integer             0
   19     18 Semicolon
   20     13 Identifier      the_char
   20     22 Op_assign
   20     24 Integer            32
   20     27 Semicolon
   21     13 Identifier      i
   21     15 Op_assign
   21     17 Integer             0
   21     18 Semicolon
   22     13 Keyword_while
   22     19 LeftParen
   22     20 Identifier      i
   22     22 Op_less
   22     24 Identifier      max_iter
   22     32 RightParen
   22     34 LeftBrace
   23     17 Identifier      x_x
   23     21 Op_assign
   23     23 LeftParen
   23     24 Identifier      x
   23     26 Op_multiply
   23     28 Identifier      x
   23     29 RightParen
   23     31 Op_divide
   23     33 Integer           200
   23     36 Semicolon
   24     17 Identifier      y_y
   24     21 Op_assign
   24     23 LeftParen
   24     24 Identifier      y
   24     26 Op_multiply
   24     28 Identifier      y
   24     29 RightParen
   24     31 Op_divide
   24     33 Integer           200
   24     36 Semicolon
   25     17 Keyword_if
   25     20 LeftParen
   25     21 Identifier      x_x
   25     25 Op_add
   25     27 Identifier      y_y
   25     31 Op_greater
   25     33 Integer           800
   25     37 RightParen
   25     39 LeftBrace
   26     21 Identifier      the_char
   26     30 Op_assign
   26     32 Integer            48
   26     36 Op_add
   26     38 Identifier      i
   26     39 Semicolon
   27     21 Keyword_if
   27     24 LeftParen
   27     25 Identifier      i
   27     27 Op_greater
   27     29 Integer             9
   27     30 RightParen
   27     32 LeftBrace
   28     25 Identifier      the_char
   28     34 Op_assign
   28     36 Integer            64
   28     39 Semicolon
   29     21 RightBrace
   30     21 Identifier      i
   30     23 Op_assign
   30     25 Identifier      max_iter
   30     33 Semicolon
   31     17 RightBrace
   32     17 Identifier      y
   32     19 Op_assign
   32     21 Identifier      x
   32     23 Op_multiply
   32     25 Identifier      y
   32     27 Op_divide
   32     29 Integer           100
   32     33 Op_add
   32     35 Identifier      y0
   32     37 Semicolon
   33     17 Identifier      x
   33     19 Op_assign
   33     21 Identifier      x_x
   33     25 Op_subtract
   33     27 Identifier      y_y
   33     31 Op_add
   33     33 Identifier      x0
   33     35 Semicolon
   34     17 Identifier      i
   34     19 Op_assign
   34     21 Identifier      i
   34     23 Op_add
   34     25 Integer             1
   34     26 Semicolon
   35     13 RightBrace
   36     13 Keyword_putc
   36     17 LeftParen
   36     18 Identifier      the_char
   36     26 RightParen
   36     27 Semicolon
   37     13 Identifier      x0
   37     16 Op_assign
   37     18 Identifier      x0
   37     21 Op_add
   37     23 Identifier      x_step
   37     29 Semicolon
   38      9 RightBrace
   39      9 Keyword_putc
   39     13 LeftParen
   39     14 Integer            10
   39     18 RightParen
   39     19 Semicolon
   40      9 Identifier      y0
   40     12 Op_assign
   40     14 Identifier      y0
   40     17 Op_subtract
   40     19 Identifier      y_step
   40     25 Semicolon
   41      5 RightBrace
   42      1 RightBrace
   43      1 End_of_input
""","""
Sequence
;
Sequence
Sequence
Sequence
Sequence
Sequence
Sequence
Sequence
Sequence
Sequence
;
Assign
Identifier     left_edge
Negate
Integer        420
;
Assign
Identifier     right_edge
Integer        300
Assign
Identifier     top_edge
Integer        300
Assign
Identifier     bottom_edge
Negate
Integer        300
;
Assign
Identifier     x_step
Integer        7
Assign
Identifier     y_step
Integer        15
Assign
Identifier     max_iter
Integer        200
Assign
Identifier     y0
Identifier     top_edge
While
Greater
Identifier     y0
Identifier     bottom_edge
Sequence
Sequence
Sequence
Sequence
;
Assign
Identifier     x0
Identifier     left_edge
While
Less
Identifier     x0
Identifier     right_edge
Sequence
Sequence
Sequence
Sequence
Sequence
Sequence
Sequence
;
Assign
Identifier     y
Integer        0
Assign
Identifier     x
Integer        0
Assign
Identifier     the_char
Integer        32
Assign
Identifier     i
Integer        0
While
Less
Identifier     i
Identifier     max_iter
Sequence
Sequence
Sequence
Sequence
Sequence
Sequence
;
Assign
Identifier     x_x
Divide
Multiply
Identifier     x
Identifier     x
Integer        200
Assign
Identifier     y_y
Divide
Multiply
Identifier     y
Identifier     y
Integer        200
If
Greater
Add
Identifier     x_x
Identifier     y_y
Integer        800
If
Sequence
Sequence
Sequence
;
Assign
Identifier     the_char
Add
Integer        48
Identifier     i
If
Greater
Identifier     i
Integer        9
If
Sequence
;
Assign
Identifier     the_char
Integer        64
;
Assign
Identifier     i
Identifier     max_iter
;
Assign
Identifier     y
Add
Divide
Multiply
Identifier     x
Identifier     y
Integer        100
Identifier     y0
Assign
Identifier     x
Add
Subtract
Identifier     x_x
Identifier     y_y
Identifier     x0
Assign
Identifier     i
Add
Identifier     i
Integer        1
Prtc
Identifier     the_char
;
Assign
Identifier     x0
Add
Identifier     x0
Identifier     x_step
Prtc
Integer        10
;
Assign
Identifier     y0
Subtract
Identifier     y0
Identifier     y_step
""","""
Datasize: 15 Strings: 0
    0 push  420
    1 neg
    2 store [0]
    3 push  300
    4 store [1]
    5 push  300
    6 store [2]
    7 push  300
    8 neg
    9 store [3]
   10 push  7
   11 store [4]
   12 push  15
   13 store [5]
   14 push  200
   15 store [6]
   16 fetch [2]
   17 store [7]
   18 fetch [7]
   19 fetch [3]
   20 gt
   21 jz     (81) 103
   22 fetch [0]
   23 store [8]
   24 fetch [8]
   25 fetch [1]
   26 lt
   27 jz     (68) 96
   28 push  0
   29 store [9]
   30 push  0
   31 store [10]
   32 push  32
   33 store [11]
   34 push  0
   35 store [12]
   36 fetch [12]
   37 fetch [6]
   38 lt
   39 jz     (49) 89
   40 fetch [10]
   41 fetch [10]
   42 mul
   43 push  200
   44 div
   45 store [13]
   46 fetch [9]
   47 fetch [9]
   48 mul
   49 push  200
   50 div
   51 store [14]
   52 fetch [13]
   53 fetch [14]
   54 add
   55 push  800
   56 gt
   57 jz     (12) 70
   58 push  48
   59 fetch [12]
   60 add
   61 store [11]
   62 fetch [12]
   63 push  9
   64 gt
   65 jz     (2) 68
   66 push  64
   67 store [11]
   68 fetch [6]
   69 store [12]
   70 fetch [10]
   71 fetch [9]
   72 mul
   73 push  100
   74 div
   75 fetch [7]
   76 add
   77 store [9]
   78 fetch [13]
   79 fetch [14]
   80 sub
   81 fetch [8]
   82 add
   83 store [10]
   84 fetch [12]
   85 push  1
   86 add
   87 store [12]
   88 jmp    (-53) 36
   89 fetch [11]
   90 prtc
   91 fetch [8]
   92 fetch [4]
   93 add
   94 store [8]
   95 jmp    (-70) 24
   96 push  10
   97 prtc
   98 fetch [7]
   99 fetch [5]
  100 sub
  101 store [7]
  102 jmp    (-83) 18
  103 halt
""","""
1111111111111111111111122222222222222222222222222222222222222222222222222222222222222222222222222211111
1111111111111111111122222222222222222222222222222222222222222222222222222222222222222222222222222222211
1111111111111111112222222222222222222222222222222222222222222222222222222222222222222222222222222222222
1111111111111111222222222222222222233333333333333333333333222222222222222222222222222222222222222222222
1111111111111112222222222222333333333333333333333333333333333333222222222222222222222222222222222222222
1111111111111222222222233333333333333333333333344444456655544443333332222222222222222222222222222222222
1111111111112222222233333333333333333333333444444445567@@6665444444333333222222222222222222222222222222
11111111111222222333333333333333333333334444444445555679@@@@7654444443333333222222222222222222222222222
1111111112222223333333333333333333333444444444455556789@@@@98755544444433333332222222222222222222222222
1111111122223333333333333333333333344444444445556668@@@    @@@76555544444333333322222222222222222222222
1111111222233333333333333333333344444444455566667778@@      @987666555544433333333222222222222222222222
111111122333333333333333333333444444455556@@@@@99@@@@@@    @@@@@@877779@5443333333322222222222222222222
1111112233333333333333333334444455555556679@   @@@               @@@@@@ 8544333333333222222222222222222
1111122333333333333333334445555555556666789@@@                        @86554433333333322222222222222222
1111123333333333333444456666555556666778@@ @                         @@87655443333333332222222222222222
111123333333344444455568@887789@8777788@@@                            @@@@65444333333332222222222222222
111133334444444455555668@@@@@@@@@@@@99@@@                              @@765444333333333222222222222222
111133444444445555556778@@@         @@@@                                @855444333333333222222222222222
11124444444455555668@99@@             @                                 @655444433333333322222222222222
11134555556666677789@@                                                @86655444433333333322222222222222
111                                                                 @@876555444433333333322222222222222
11134555556666677789@@                                                @86655444433333333322222222222222
11124444444455555668@99@@             @                                 @655444433333333322222222222222
111133444444445555556778@@@         @@@@                                @855444333333333222222222222222
111133334444444455555668@@@@@@@@@@@@99@@@                              @@765444333333333222222222222222
111123333333344444455568@887789@8777788@@@                            @@@@65444333333332222222222222222
1111123333333333333444456666555556666778@@ @                         @@87655443333333332222222222222222
1111122333333333333333334445555555556666789@@@                        @86554433333333322222222222222222
1111112233333333333333333334444455555556679@   @@@               @@@@@@ 8544333333333222222222222222222
111111122333333333333333333333444444455556@@@@@99@@@@@@    @@@@@@877779@5443333333322222222222222222222
1111111222233333333333333333333344444444455566667778@@      @987666555544433333333222222222222222222222
1111111122223333333333333333333333344444444445556668@@@    @@@76555544444333333322222222222222222222222
1111111112222223333333333333333333333444444444455556789@@@@98755544444433333332222222222222222222222222
11111111111222222333333333333333333333334444444445555679@@@@7654444443333333222222222222222222222222222
1111111111112222222233333333333333333333333444444445567@@6665444444333333222222222222222222222222222222
1111111111111222222222233333333333333333333333344444456655544443333332222222222222222222222222222222222
1111111111111112222222222222333333333333333333333333333333333333222222222222222222222222222222222222222
1111111111111111222222222222222222233333333333333333333333222222222222222222222222222222222222222222222
1111111111111111112222222222222222222222222222222222222222222222222222222222222222222222222222222222222
1111111111111111111122222222222222222222222222222222222222222222222222222222222222222222222222222222211
"""
)