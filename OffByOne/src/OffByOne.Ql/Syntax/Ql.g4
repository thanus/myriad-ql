﻿grammar Ql;

form : 'form' Identifier '{' stat+ '}' ;
question : StringLiteral Identifier ':' + Type ;
stat
	: question
	| if
	;

if : 'if' booleanExpression '{' stat+ '}' else? ;
else : 'else' (if | '{' stat+ '}') ;

booleanExpression
	: 'true'
	| 'false'
	| Identifier
	| 'not' booleanExpression
	| booleanExpression ('and'|'or') booleanExpression
	| numericExpression ('>'|'<'|'=='|'<='|'>=') numericExpression
	| Date ('>'|'<'|'=='|'<='|'>=') Date
	;

numericExpression
	: NumericLiteral
	| Identifier
	| numericExpression ('*'|'/') numericExpression
	| numericExpression ('+'|'-') numericExpression
	;

Type 
	: 'boolean'
	| 'string'
	| 'integer'
	| 'date'
	| 'decimal'
	| 'money'
	;

NumericLiteral : Money | Decimal | SignedInt;

SignedInt : '-'? Int+ ;
Money : SignedInt '.' Digit Digit ;
Decimal : SignedInt '.' [0-9]+ ;
Date : '\'' Digit Digit '-' Digit Digit '-' Digit Digit Digit Digit '\'' ;

fragment
Int: Digit | ([1-9] Digit*) ;

fragment
Digit: [0-9] ;

StringLiteral : '"' (Escaped | . )*? '"' ;

fragment
Escaped : '\\' [btnr"\\] ;

Identifier : [a-z][a-zA-Z]* ;

BlockComment : '/*' .*? '*/' -> skip;
LineComment : '//' .*? '\n'  -> skip;
WS : [ \t\r\n]+ -> skip ;
