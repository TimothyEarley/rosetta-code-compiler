package syntax

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import lexer.lexer

/*
    stmt_list           =   {stmt} ;

    stmt                =   ';'
                          | Identifier '=' expr ';'
                          | 'while' paren_expr stmt
                          | 'if' paren_expr stmt ['else' stmt]
                          | 'print' '(' prt_list ')' ';'
                          | 'putc' paren_expr ';'
                          | '{' stmt_list '}'
                          ;

    paren_expr          =   '(' expr ')' ;

    prt_list            =   (string | expr) {',' (String | expr)} ;

    expr                =   and_expr            {'||' and_expr} ;
    and_expr            =   equality_expr       {'&&' equality_expr} ;
    equality_expr       =   relational_expr     [('==' | '!=') relational_expr] ;
    relational_expr     =   addition_expr       [('<' | '<=' | '>' | '>=') addition_expr] ;
    addition_expr       =   multiplication_expr {('+' | '-') multiplication_expr} ;
    multiplication_expr =   primary             {('*' | '/' | '%') primary } ;
    primary             =   Identifier
                          | Integer
                          | '(' expr ')'
                          | ('+' | '-' | '!') primary
                          ;

 */

class SyntaxTest : StringSpec({

	"test 1" {
		syntax(lexer("""
			count = 1;
			while (count < 10) {
				print("count is: ", count, "\n");
				count = count + 1;
			}
		""".trimIndent())) shouldBe """
			Sequence
			Sequence
			;
			Assign
			Identifier count
			Integer 1
			While
			Less
			Identifier count
			Integer 10
			Sequence
			Sequence
			;
			Sequence
			Sequence
			Sequence
			;
			Prts
			String "count is: "
			;
			Prti
			Identifier count
			;
			Prts
			String "\n"
			;
			Assign
			Identifier count
			Add
			Identifier count
			Integer 1
		""".trimIndent()
	}

})
