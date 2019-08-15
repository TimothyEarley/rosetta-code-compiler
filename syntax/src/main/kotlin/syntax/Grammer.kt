package syntax

import syntax.Token.Type.*
import syntax.pc.*

object Grammer {

	private fun ParserContext.identifier(): Parser<AST.Expression.Identifier> =
		t(Identifier).map { AST.Expression.Identifier(it.value) }

	private fun ParserContext.string(): Parser<AST.Expression.StringExpr> =
		t(StringType).map { AST.Expression.StringExpr(it.value) }

	private val primary: Parser<AST.Expression> by lazy {
		context("Primitive") {
			identifier() or

			t(Integer).map { AST.Expression.Integer(it.value.toInt()) } or

			t(LeftParen).skip() + lazy { expr.value } + t(RightParen).skip() or

			((t(OpAdd) or t(OpSubtract) or t(OpNot))+ lazy { primary.value }).map { (op, expr) ->
				when (op.type) {
					OpAdd -> expr
					OpSubtract -> AST.Expression.Negate(expr)
					OpNot -> AST.Expression.Not(expr)
					else -> error("Unknown token type matched")
				}
			}
		}
	}

	private val multiplicationExpr: Parser<AST.Expression> = context("Multiplication Expression") {
		(primary + many(
			(t(OpMultiply).map { AST.Operator.Multiply } or
					t(OpDivide).map { AST.Operator.Divide } or
					t(OpMod).map { AST.Operator.Mod }
					) + primary
		)).mapBiExpressions()
	}

	private val additionExpr: Parser<AST.Expression> = context("Addition Expression") {
		(multiplicationExpr + many(
			(
					t(OpAdd).map { AST.Operator.Add } or
							t(OpSubtract).map { AST.Operator.Subtract }
					) + multiplicationExpr
		)).mapBiExpressions()
	}

	private val relationalExpr: Parser<AST.Expression> = context("Relational Expression") {
		(additionExpr + optional(
			(
					t(OpLess).map { AST.Operator.Less } or
					t(OpLessEqual).map { AST.Operator.LessEqual } or
					t(OpGreater).map { AST.Operator.Greater } or
					t(OpGreaterEqual).map { AST.Operator.GreaterEqual }
					) + additionExpr
		)).mapBiExpression()
	}

	private val equalityExpr: Parser<AST.Expression> = context("Equality expression") {
		(relationalExpr + optional(
			(t(OpEqual).map { AST.Operator.Equal } or t(OpNotEqual).map { AST.Operator.NotEqual }) + relationalExpr
		)).mapBiExpression()
	}

	private val andExpr: Parser<AST.Expression> = equalityExpr //TODO and

	private val expr: Parser<AST.Expression> = context("Expression") {
		(andExpr + many(
			(t(OpOr) + andExpr)
				.map { (_, _) -> TODO() }
		))
			.map { (left, rights) ->
				if (rights.isEmpty()) left
				else TODO()
			}
	}

	private val parenExpr: Parser<AST.Expression> = context("Expression in Parentheses") {
		t(LeftParen).skip() + expr + t(RightParen).skip()
	}

	private fun ParserContext.prtSingle() =
		string().map { AST.Statement.Prts(it) } or
				expr.map { AST.Statement.Prti(it) }

	private fun ParserContext.prtList(): Parser<AST.Statement> =
		(prtSingle() + many(t(Comma).skip() + prtSingle()))
			.map { (head, tail) ->
				statementSeq(listOf(head) + tail)
			}

	private val stmt: Parser<AST.Statement> by lazy {
//				context("Empty Statement") {
//					t(Semicolon).map { TODO() }
//				} or

				context("Assignment") {
					(identifier() + t(OpAssign).skip() + lazy { expr.value } + t(Semicolon).skip())
						.map { (i, e) -> AST.Statement.Assign(i, e) }
				} or
				context("While") {
					(t(KeywordWhile).skip() + parenExpr + lazy { stmt.value })
						.map { (cond, body) -> AST.Statement.While(cond, body) }
				} or

				context("If") {
					(t(KeywordIf).skip() + parenExpr + lazy { stmt.value } + optional(
						t(KeywordElse).skip() + lazy { stmt.value }
					)).map { (condIfBody, elseBody) -> AST.Statement.If(condIfBody.first, condIfBody.second, elseBody) }
				} or

				context("Print") {
					t(KeywordPrint).skip() + t(LeftParen).skip() + prtList() + t(RightParen).skip() + t(
						Semicolon
					).skip()
				} or

				context("Putc") {
					(t(KeywordPutc).skip() + parenExpr + t(Semicolon).skip()).map { AST.Statement.Prtc(it) }
				} or

				context("Block") {
					t(LeftBrace).skip() + lazy { stmtList.value } + t(RightBrace).skip()
				}
	}

	private val stmtList: Parser<AST.Statement> =
		many(stmt).map { statementSeq(it) }

	private fun statementSeq(seq: List<AST.Statement>): AST.Statement =
		//the spec says generate more sequences than necessary, so that's what I will do:
		seq.fold<AST.Statement, AST.Statement>(AST.Statement.EMPTY) { l, r ->
			AST.Statement.Sequence(l, r)
		}


	private fun Parser<Pair<AST.Expression, Pair<AST.Operator, AST.Expression>?>>.mapBiExpression(): Parser<AST.Expression> =
		this.map { (left, right) ->
			if (right == null) left
			else AST.Expression.BiExpression(right.first, left, right.second)
		}

	private fun Parser<Pair<AST.Expression, List<Pair<AST.Operator, AST.Expression>>>>.mapBiExpressions(): Parser<AST.Expression> = this.map { (left, rights) ->
			if (rights.isEmpty()) left
			//TODO foldLeft or foldRight?
			else rights.fold(left) { l, (op, r) ->
				AST.Expression.BiExpression(op, l, r)
			}
	}

	val parser: Parser<AST> = context("Statement List") {
		stmtList + t(EndOfInput).skip()
	}

}