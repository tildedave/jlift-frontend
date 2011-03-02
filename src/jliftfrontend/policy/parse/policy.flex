/*
 * File          : policy.flex
 * Project       : jlift-frontend
 * Description   : Lexer for Policy File
 * Author(s)     : dhking
 *
 * Created       : Feb 8, 2008 6:14:02 PM 
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jliftfrontend.policy.parse;

import java_cup.runtime.*;

%%
%public
%class PolicyLexer
%cup
%line
%column
%eofval{
	return symbol( Symbols.EOF );
%eofval}

%state BLOCK_COMMENT, END_OF_LINE_COMMENT

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

Identifier = [:jletter:] [:jletterdigit:]*
ClassString = ({Identifier}\.)*{Identifier}

%%

<YYINITIAL>
{
    "/*"        { yybegin(BLOCK_COMMENT); }
    "//"        { yybegin(END_OF_LINE_COMMENT); }
	";" 		{ return symbol( Symbols.SEMI ); }
	"{"			{ return symbol( Symbols.LBRACE ); }
	"}"			{ return symbol( Symbols.RBRACE ); }
	"[" 		{ return symbol( Symbols.LBRACK); }
	"]" 		{ return symbol( Symbols.RBRACK); }
	"("			{ return symbol( Symbols.LPAREN ); }
	")"			{ return symbol( Symbols.RPAREN ); }
	"::="		{ return symbol( Symbols.DEF ); }
	":"			{ return symbol( Symbols.COLON ); }
	","			{ return symbol( Symbols.COMMA ); }
	"*"         { return symbol( Symbols.STAR ); }
	label		{ return symbol( Symbols.LABEL ); }
	param		{ return symbol( Symbols.PARAM ); }
	begin		{ return symbol( Symbols.BEGIN ); }
	returnvalue { return symbol( Symbols.RETURNVALUE ); }
	return		{ return symbol( Symbols.RETURN ); }
	principal	{ return symbol( Symbols.PRINCIPAL ); }
	constructor { return symbol( Symbols.CONSTRUCTOR ); }
	arraybase   { return symbol( Symbols.ARRAYBASE ); }
	class		{ return symbol( Symbols.CLASS ); }
	field		{ return symbol( Symbols.FIELD ); }
	method		{ return symbol( Symbols.METHOD ); }
	local		{ return symbol( Symbols.LOCAL ); }
	{Identifier}  { return symbol( Symbols.ID, new String( yytext() ) ); }
	{ClassString} { return symbol( Symbols.CLASSNAME, new String( yytext() ) ); }
	"."         { return symbol( Symbols.DOT ); }
	{WhiteSpace} { /* ignore */ }
}

<BLOCK_COMMENT> {
    "*/"                         { yybegin(YYINITIAL); }
    .|\n                         { /* ignore */ }
}

<END_OF_LINE_COMMENT> {
	\n							 { yybegin(YYINITIAL); }
	.							 { /* ignore */ }
}

/* error fallback */
.|\n                             { throw new Error("Illegal character <"+
                                                    yytext()+"> at line " + (yyline + 1) + ", column " + yycolumn); }