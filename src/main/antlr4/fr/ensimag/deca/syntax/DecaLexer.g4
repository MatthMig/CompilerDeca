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

OBRACE: '{';
CBRACE: '}';
OPARENT: '(';
CPARENT: ')';
PRINT: 'print';
PRINTLN: 'println';
READINT: 'readInt';

// Conditions
EXCLAM: '!';
EQEQ: '==';
LEQ: '<=';
LT: '<';
AND: '&&';
OR: '||';

// Statements
WHILE: 'while';
IF: 'if';
ELSE: 'else';

SEMI: ';';
COMMA: ',';
EQUALS: '=';

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
fragment QUOTES: '"';
fragment SINGLE_QUOTE: '\'';
STRING: ( QUOTES .*? QUOTES ) | ( SINGLE_QUOTE .*? SINGLE_QUOTE ) ;

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
