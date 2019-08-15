package syntax

sealed class AST {

	sealed class Statement(val type: String, val left: AST, val right: AST?) : AST() {
		class Sequence(left: Statement, right: Statement): Statement("Sequence", left, right)

		class Assign(
			identifier: Expression.Identifier,
			expression: Expression
		): Statement("Assign", identifier, expression)

		data class Prts(val string: Expression.StringExpr): Statement("Prts", string, null)
		data class Prti(val expr: Expression): Statement("Prti", expr, null)
		data class While(val cond: Expression, val body: Statement): Statement("While", cond, body)
	}

	sealed class Expression : AST() {
		data class Identifier(val name: String): Expression()
		data class Integer(val value: Int): Expression()
		data class StringExpr(val value: String): Expression()
		data class BiExpression(val op: Operator, val left: Expression, val right: Expression): Expression()
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
	}
}

fun AST?.toFlatString(): String = when (this) {
	null -> ";\n"
	is AST.Statement -> "$type\n${left.toFlatString()}${right.toFlatString()}"
	is AST.Expression.Identifier -> "Identifier $name\n"
	is AST.Expression.Integer -> "Integer $value\n"
	is AST.Expression.StringExpr -> "String $value\n"
	is AST.Expression.BiExpression -> "${op.name}\n${left.toFlatString()}${right.toFlatString()}"
}