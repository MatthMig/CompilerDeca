lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Class related tokens
CLASS: 'class';
EXTENDS: 'extends';
PROTECTED: 'protected';
NEW: 'new';
THIS: 'this';
INSTANCEOF: 'instanceof';
NULL: 'null';
SUPER: 'super';
ASM: 'asm';
RETURN: 'return';
DOT: '.';

IMPORT: 'import';

OBRACE: '{';
CBRACE: '}';
OPARENT: '(';
CPARENT: ')';
PRINT: 'print';
PRINTX: 'printx';
PRINTLN: 'println';
PRINTLNX: 'printlnx';
READINT: 'readInt';
READFLOAT: 'readFloat';
TRUE: 'true';
FALSE: 'false';

// Conditions
NEQ: '!=';
EXCLAM: '!';
EQEQ: '==';
EQUALS: '=';
LEQ: '<=';
LT: '<';
GEQ: '>=';
GT: '>';
AND: '&&';
OR: '||';

PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';


// Statements
WHILE: 'while';
IF: 'if';
ELSE: 'else';


SEMI: ';';
COMMA: ',';

// Deca lexer rules.
//p.45
// Identificateurs
fragment DIGIT: '0' .. '9';
fragment LETTER: 'a' .. 'z' | 'A' .. 'Z';
IDENT: (LETTER | '$' | '_')(LETTER | '$' | '_' | DIGIT)*;

// Symboles spéciaux

// Littéraux entiers
fragment POSITIVE_DIGIT: '1' .. '9';
INT: '0'| (POSITIVE_DIGIT DIGIT*);

// Littéraux flottants
fragment NUM: DIGIT+;
fragment SIGN: ('-' | '+')?;
fragment EXP: ('E' | 'e') SIGN NUM;
fragment FLOATSUF: ('F' | 'f')?;

//// Decimal
fragment DEC: NUM '.' NUM; 
fragment FLOATDEC: (DEC | DEC EXP) FLOATSUF;

//// Hex
fragment HEXLETTER: 'A' .. 'F' | 'a' .. 'f';
fragment DIGITHEX: DIGIT | HEXLETTER;
fragment HEXPREF: '0x' | '0X';
fragment NUMHEX: DIGITHEX+;
fragment FLOATHEX: HEXPREF NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM FLOATSUF;
FLOAT: FLOATHEX | FLOATDEC;

// Strings
fragment SINGLE_QUOTE: '\'';
fragment STRING_CAR: ~('\n' | '"' | '\\');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR | '\n' | '\\"' | '\\\\')* '"';

// Skips
SPACE:   ( 
      ' '
   |  '\t'
   |  '\r'
   |  '\n'
   ) {
      skip();
   }
;

fragment LINE_COMMENT: '//' (. | '\n')*? '\n';
fragment MULTI_COMMENT: '/*' .*? '*/';
COMMENTS: (
      LINE_COMMENT
   |  MULTI_COMMENT
   ) {
      skip();
   }
;

// the include directive invokes the doInclude method during the lexing process.
fragment FILENAME: (LETTER | DIGIT | '.' | '-' | '_')+;
// look AbstractDecaLexer.java to know how finding a file and circularInclude work.
// Here we define a block of action for doInclude (look documentation of ANTLR4)
INCLUDE: '#include' (' ')* '"' FILENAME '"' {
   doInclude(getText());
   skip(); 
};
