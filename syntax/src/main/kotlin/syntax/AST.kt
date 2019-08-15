package syntax

import syntax.AST.Statement.EMPTY.type

//TODO remove AST and only consider Tree / Terminal nodes?

sealed class AST {

	sealed class Statement(val type: String, val left: AST, val right: AST) : AST() {
		class Sequence(left: Statement, right: Statement): Statement("Sequence", left, right)

		object EMPTY : Statement(";", EMPTY, EMPTY)

		class Assign(
			identifier: Expression.Identifier,
			expression: Expression
		): Statement("Assign", identifier, expression)

		data class Prts(val string: Expression.StringExpr): Statement("Prts", string, EMPTY)
		data class Prti(val expr: Expression): Statement("Prti", expr, EMPTY)
		data class Prtc(val expr: Expression): Statement("Prtc", expr, EMPTY)
		data class While(val cond: Expression, val body: Statement): Statement("While", cond, body)

		private data class IfBody( val ifBody: Statement, val elseBody: Statement?): Statement("If", ifBody, elseBody ?: EMPTY)
		data class If(val cond: Expression, val ifBody: Statement, val elseBody: Statement?):
			Statement("If", cond, IfBody(ifBody, elseBody))
	}

	sealed class Expression : AST() {
		object EMPTY : Expression()

		data class Identifier(val name: String): Expression()
		data class Integer(val value: Int): Expression()
		data class StringExpr(val value: String): Expression()
		open class BiExpression(val op: Operator, val left: Expression, val right: Expression): Expression()
		data class Negate(val expr: Expression): BiExpression(Operator.Negate, expr, EMPTY)
		data class Not(val expr: Expression): BiExpression(Operator.Not, expr, EMPTY)
	}

	// nonterminal
	sealed class Operator(val name: String) {
		object Less : Operator("Less")
		object LessEqual : Operator("LessEqual")
		object Greater : Operator("Greater")
		object GreaterEqual : Operator("GreaterEqual")
		object Equal : Operator("Equal")
		object NotEqual : Operator("NotEqual")

		object Add : Operator("Add")
		object Subtract : Operator("Subtract")

		object Multiply: Operator("Multiply")
		object Divide: Operator("Divide")
		object Mod: Operator("Mod")

		object Negate: Operator("Negate")
		object Not: Operator("Not")
	}
}

fun AST.toFlatString(): String = when (this) {
	is AST.Statement.EMPTY -> "$type\n"
	is AST.Statement -> "$type\n${left.toFlatString()}${right.toFlatString()}"
	is AST.Expression.EMPTY -> ";\n"
	is AST.Expression.Identifier -> "Identifier $name\n"
	is AST.Expression.Integer -> "Integer $value\n"
	is AST.Expression.StringExpr -> "String $value\n"
	is AST.Expression.BiExpression -> "${op.name}\n${left.toFlatString()}${right.toFlatString()}"
}