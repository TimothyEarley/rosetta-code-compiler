package syntax

import syntax.Token.Type.*
import syntax.pc.*

object Grammer {

	//TODO precedence

	private val identifier: Parser<AST.Expression.Identifier> =
		t(Identifier).map { AST.Expression.Identifier(it.value) }

	private val string: Parser<AST.Expression.StringExpr> =
		t(StringType).map { AST.Expression.StringExpr(it.value) }

	private val primary: Parser<AST.Expression> by lazy {
		identifier or
		t(Integer).map { AST.Expression.Integer(it.value.toInt()) } or
		t(LeftParen).skip() + lazy { expr.value } + t(RightParen).skip() or
		(t(OpAdd) or t(OpSubtract) or t(OpNot)).map { TODO() } + lazy { primary.value }
	}

	private val multiplicationExpr: Parser<AST.Expression> =
		primary //TODO mutl

	private val additionExpr: Parser<AST.Expression> =
		(multiplicationExpr + many(
			(
					t(OpAdd).map { AST.Operator.Add } or
							t(OpSubtract).map { AST.Operator.Subtract }
					) + multiplicationExpr
		)).map { (left, rights) ->
			if (rights.isEmpty()) left
			//TODO foldLeft or foldRight?
			else rights.fold(left) { l, (op, r) ->
				AST.Expression.BiExpression(op, l, r)
			}
		}

	private val relationalExpr: Parser<AST.Expression> =
		(additionExpr + optional(
			(
				t(OpLess).map { AST.Operator.Less } or
				t(OpLessEqual).map { AST.Operator.LessEqual } or
				t(OpGreater).map { AST.Operator.Greater } or
				t(OpGreaterEqual).map { AST.Operator.GreaterEqual }
			) + additionExpr
		)).map { (left, right) ->
			if (right == null) left
			else AST.Expression.BiExpression(right.first, left, right.second)
		}
	private val equalityExpr: Parser<AST.Expression> =
		relationalExpr //TODO equals

	private val andExpr: Parser<AST.Expression> = equalityExpr //TODO and

	private val expr: Parser<AST.Expression> =
		(andExpr + many(
			(t(OpOr) + andExpr)
				.map { (_, _) -> TODO() }
		))
			.map { (left, rights) ->
				if (rights.isEmpty()) left
				else TODO()
			}

	private val parenExpr: Parser<AST.Expression> =
		t(LeftParen).skip() + expr + t(RightParen).skip()

	private val prtSingle =
		string.map { AST.Statement.Prts(it) } or
		expr.map { AST.Statement.Prti(it) }

	private val prtList: Parser<AST.Statement> =
		(prtSingle + many(t(Comma).skip() + prtSingle))
			.map { (head, tail) ->
				if (tail.isEmpty()) head
				else statementSeq(head, tail)
			}

	private val stmt: Parser<AST.Statement> by lazy {
		t(Semicolon).map { TODO() } or

		(identifier + t(OpAssign).skip() + lazy { expr.value } + t(Semicolon).skip())
			.map { (i, e) -> AST.Statement.Assign(i, e) } or

		(t(KeywordWhile).skip() + parenExpr + lazy { stmt.value })
			.map { (cond, body) -> AST.Statement.While(cond, body) } or

		(t(KeywordIf).skip() + parenExpr + lazy { stmt.value } + optional(
			t(KeywordElse).skip() + lazy { stmt.value }
		)).map { TODO() } or

		t(KeywordPrint).skip() + t(LeftParen).skip() + prtList + t(RightParen).skip() + t(Semicolon).skip() or

		(t(KeywordPutc).skip() + parenExpr)
			.map { TODO() } or

		(t(LeftBrace).skip() + lazy { stmtList.value }) + t(RightBrace).skip()
	}

	private val stmtList: Parser<AST.Statement> =
		many(stmt).map { statementSeq(it.first(), it.drop(1)) }

	private fun statementSeq(head: AST.Statement, tail: List<AST.Statement>): AST.Statement =
		tail.fold(head) { l, r ->
			AST.Statement.Sequence(l, r)
		}

	val parser: Parser<AST> = stmtList + t(EndOfInput).skip()

}