package cop5556sp17;

import java.util.*;

public class Scanner {
	/**
	 * Kind enum
	 */
	
	public static enum Kind {
		IDENT(""), INT_LIT(""), KW_INTEGER("integer"), KW_BOOLEAN("boolean"), 
		KW_IMAGE("image"), KW_URL("url"), KW_FILE("file"), KW_FRAME("frame"), 
		KW_WHILE("while"), KW_IF("if"), KW_TRUE("true"), KW_FALSE("false"), 
		SEMI(";"), COMMA(","), LPAREN("("), RPAREN(")"), LBRACE("{"), 
		RBRACE("}"), ARROW("->"), BARARROW("|->"), OR("|"), AND("&"), 
		EQUAL("=="), NOTEQUAL("!="), LT("<"), GT(">"), LE("<="), GE(">="), 
		PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"), NOT("!"), 
		ASSIGN("<-"), OP_BLUR("blur"), OP_GRAY("gray"), OP_CONVOLVE("convolve"), 
		KW_SCREENHEIGHT("screenheight"), KW_SCREENWIDTH("screenwidth"), 
		OP_WIDTH("width"), OP_HEIGHT("height"), KW_XLOC("xloc"), KW_YLOC("yloc"), 
		KW_HIDE("hide"), KW_SHOW("show"), KW_MOVE("move"), OP_SLEEP("sleep"), 
		KW_SCALE("scale"), EOF("eof");

		Kind(String text) {
			this.text = text;
		}

		final String text;
		private static final HashMap<String, Kind> map = createMap(); 

		String getText() {
			return text;
		}
		
		private static HashMap<String, Kind> createMap() {
			HashMap<String, Kind> map = new HashMap<String, Kind>();
			
			map.put("integer",      KW_INTEGER);
			map.put("boolean",      KW_BOOLEAN);
			map.put("image",        KW_IMAGE);
			map.put("url",          KW_URL);
			map.put("file",         KW_FILE);
			map.put("frame",        KW_FRAME);
			map.put("while",        KW_WHILE);
			map.put("if",           KW_IF);
			map.put("true",         KW_TRUE);
			map.put("false",        KW_FALSE);
			map.put("blur",         OP_BLUR);
			map.put("gray",         OP_GRAY);
			map.put("convolve",     OP_CONVOLVE);
			map.put("screenheight", KW_SCREENHEIGHT);
			map.put("screenwidth",  KW_SCREENWIDTH);
			map.put("width",        OP_WIDTH);
			map.put("height",       OP_HEIGHT);
			map.put("xloc",         KW_XLOC);
			map.put("yloc",         KW_YLOC);
			map.put("hide",         KW_HIDE);
			map.put("show",         KW_SHOW);
			map.put("move",         KW_MOVE);
			map.put("sleep",        OP_SLEEP);
			map.put("scale",        KW_SCALE);

			return map;
		}
		
		public static boolean isReservedWord(String word) {
			return map.containsKey(word);
		}
		
		public static Kind getKindOfReservedWord(String word) {
			return map.get(word);
		}
	}
	
	public static enum State {
		START,
		IN_IDENT,             /* a token - a terminal state */
		IN_KEYWORD,           /* a token - a terminal state */
		IN_FRAME_OP_KEYWORD,  /* a token - a terminal state */
		IN_FILTER_OP_KEYWORD, /* a token - a terminal state */
		IN_IMAGE_OP_KEYWORD,  /* a token - a terminal state */
		IN_SEPERATOR,         /* a token - a terminal state */
		IN_OPERATOR,          /* a token - a terminal state */
		IN_INT_LITERAL,       /* a token - a terminal state */
		IN_IDENT_PART,
		IN_IDENT_START,
		IN_GOT_EQUAL,
		IN_GOT_GREATER,
		IN_GOT_LESS,
		IN_GOT_NOT,
		IN_GOT_MINUS,
		IN_GOT_OR,
		IN_GOT_ORMINUS,
		IN_DIV,
		IN_COMMENT,
		IN_END_COMMENT,
		ERROR,
	}
	
	/**
	 * Thrown by Scanner when an illegal character is encountered
	 */
	@SuppressWarnings("serial")
	public static class IllegalCharException extends Exception {
		public IllegalCharException(String message) {
			super(message);
		}
	}
	
	/**
	 * Thrown by Scanner when an int literal is not a value that can be represented by an int.
	 */
	@SuppressWarnings("serial")
	public static class IllegalNumberException extends Exception {
		public IllegalNumberException(String message){
			super(message);
		}
	}

	/**
	 * Holds the line and position in the line of a token.
	 */
	static class LinePos {
		public final int line;
		public final int posInLine;
		
		public LinePos(int line, int posInLine) {
			super();
			this.line = line;
			this.posInLine = posInLine;
		}

		@Override
		public String toString() {
			return "LinePos [line=" + line + ", posInLine=" + posInLine + "]";
		}
	}

	public class Token {
		public final Kind kind;
		public final int pos;  //position in input array
		public final int length;  

		//returns the text of this Token
		public String getText() {
			return chars.substring(pos, pos+length);
		}
		
		private int lineno(int pos) {
			String[] lines  = chars.substring(0, pos+1).split("\n");
			return lines.length - 1;
		}
		
		private int posInLine(int pos) {
			int val = chars.lastIndexOf("\n", pos);
			
			int noOfTokens = getNoOfTokensBetween(val, pos);
			int noOfWhiteSpaces = 0;
			
			int i = (val == -1 ? 0 : val);
			
			for (; i < pos; i++) {
				if (Character.isWhitespace(chars.charAt(i))) {
					noOfWhiteSpaces++;
				}
			}
			
			return noOfTokens + noOfWhiteSpaces;
		}
		
		//returns a LinePos object representing the line and column of this Token
		LinePos getLinePos(){
			LinePos linepos = new LinePos(lineno(pos), posInLine(pos));
			return linepos;
		}

		Token(Kind kind, int pos, int length) {
			this.kind = kind;
			this.pos = pos;
			this.length = length;
		}

		/** 
		 * Precondition:  kind = Kind.INT_LIT,  the text can be represented with a Java int.
		 * Note that the validity of the input should have been checked when the Token was created.
		 * So the exception should never be thrown.
		 * 
		 * @return  int value of this token, which should represent an INT_LIT
		 * @throws NumberFormatException
		 */
		public int intVal() throws NumberFormatException{
			//TODO IMPLEMENT THIS
			return 0;
		}
		
	}

	
	final ArrayList<Token> tokens;
	final String chars;
	int tokenNum;
	
	Scanner(String chars) {
		this.chars = chars;
		tokens = new ArrayList<Token>();
	}
	
	/**
	 * Initializes Scanner object by traversing chars and adding tokens to tokens list.
	 * 
	 * @return this scanner
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	public Scanner scan() throws IllegalCharException, IllegalNumberException {
		int pos = 0; 
		int length = chars.length();
		State state = State.START;
		int startPos = 0;
		int ch;
		
		while(pos < length) {
			ch = pos < length ? chars.charAt(pos) : -1;
			switch (state) {
				case START:
					pos = skipWhiteSpace(pos);
					ch = pos < length ? chars.charAt(pos) : -1;
					startPos = pos;
					switch (ch) {
						case -1:
							tokens.add(new Token(Kind.EOF, pos, 0));
							pos++;
							break;
						case '+':
							tokens.add(new Token(Kind.PLUS, startPos, 1));
							pos++;
							break;
						case '%':
							tokens.add(new Token(Kind.MOD, startPos, 1));
							pos++;
							break;
						case '&':
							tokens.add(new Token(Kind.AND, startPos, 1));
							pos++;
							break;
						case '*':
							tokens.add(new Token(Kind.TIMES, startPos, 1));
							pos++;
							break;
						case ';':
							tokens.add(new Token(Kind.SEMI, startPos, 1));
							pos++;
							break;
						case ',':
							tokens.add(new Token(Kind.COMMA, startPos, 1));
							pos++;
							break;
						case '(':
							tokens.add(new Token(Kind.LPAREN, startPos, 1));
							pos++;
							break;
						case ')':
							tokens.add(new Token(Kind.RPAREN, startPos, 1));
							pos++;
							break;
						case '{':
							tokens.add(new Token(Kind.LBRACE, startPos, 1));
							pos++;
							break;
						case '}':
							tokens.add(new Token(Kind.RBRACE, startPos, 1));
							pos++;
							break;
						case '0':
							tokens.add(new Token(Kind.INT_LIT, startPos, 1));
							pos++;
							break;
						case '=':
							state = State.IN_GOT_EQUAL;
							pos++;
							break;
						case '>':
							state = State.IN_GOT_GREATER;
							pos++;
							break;
						case '<':
							state = State.IN_GOT_LESS;
							pos++;
							break;
						case '!':
							state = State.IN_GOT_NOT;
							pos++;
							break;
						case '-':
							state = State.IN_GOT_MINUS;
							pos++;
							break;
						case '|':
							state = State.IN_GOT_OR;
							pos++;
							break;
						case '/':
							state = State.IN_DIV;
							pos++;
							break;
						default:
							if (Character.isDigit(ch)) {
								state = State.IN_INT_LITERAL;
								pos++;
							} else if (Character.isJavaIdentifierStart(ch)) {
								state = State.IN_IDENT;
								pos++;
							} else {
								/* go to ERROR state */
								state = State.ERROR;
							}
					}
					break;
				case IN_IDENT:
					if (Character.isJavaIdentifierPart(ch)) {
						pos++;
					} else {
						Token t = new Token(Kind.IDENT, startPos, pos - startPos);
						if (Kind.isReservedWord(t.getText())) {
							t = new Token(Kind.getKindOfReservedWord(t.getText()),
														startPos, pos - startPos);
						}
						tokens.add(t);
						state = State.START;
					} 
					break;
				case IN_INT_LITERAL:
					if (Character.isDigit(ch)) {
						pos++;
					} else {
						Token t = new Token(Kind.INT_LIT, startPos, pos - startPos);
						tokens.add(t);
						
						try {
							int x = Integer.parseInt(t.getText());
						} catch (NumberFormatException e) {
							throw new Scanner.IllegalNumberException("Illegal integer value at line " 
									+ t.getLinePos().line + " column " + t.getLinePos().posInLine);
						}
						state = State.START;
					}
					break;
				case IN_KEYWORD:
					break;
				case IN_FRAME_OP_KEYWORD:
					break;
				case IN_FILTER_OP_KEYWORD:
					break;
				case IN_IMAGE_OP_KEYWORD:
					break;
				case IN_SEPERATOR:
					break;
				case IN_OPERATOR:
					break;
				case IN_IDENT_PART:
					break;
				case IN_IDENT_START:
					break;
				case IN_GOT_EQUAL:
					if (ch == '=') {
						tokens.add(new Token(Kind.EQUAL, startPos, pos - startPos));
						state = State.START;
						pos++;
					} else {
						state = State.ERROR;
					}
					break;
				case IN_GOT_GREATER:
					if (ch == '=') {
						tokens.add(new Token(Kind.GE, startPos, pos - startPos));
						pos++;
					} else {
						tokens.add(new Token(Kind.GT, startPos, 1));
					}
					state = State.START;
					break;
				case IN_GOT_LESS:
					if (ch == '=') {
						tokens.add(new Token(Kind.LE, startPos, pos - startPos));
						pos++;						
					} else if (ch == '-') {
						tokens.add(new Token(Kind.ASSIGN, startPos, pos - startPos));
						pos++;
					} else {
						tokens.add(new Token(Kind.LT, startPos, 1));
					}
					break;
				case IN_GOT_NOT:
					if (ch == '=') {
						tokens.add(new Token(Kind.NOTEQUAL, startPos, pos - startPos));
						pos++;
					} else {
						tokens.add(new Token(Kind.EQUAL, startPos, 1));
					}
					state = State.START;
					break;
				case IN_GOT_MINUS:
					if (ch == '>') {
						tokens.add(new Token(Kind.ARROW, startPos, pos - startPos));
						pos++;
					} else {
						tokens.add(new Token(Kind.MINUS, startPos, 1));
					}
					state = State.START;
					break;
				case IN_GOT_OR:
					if (ch == '-') {
						state = State.IN_GOT_ORMINUS;
						pos++;
					} else {
						tokens.add(new Token(Kind.OR, startPos, 1));
						state = State.START;
					}
					break;
				case IN_GOT_ORMINUS:
					if (ch == '>') {
						tokens.add(new Token(Kind.BARARROW, startPos, pos - startPos));
						pos++;
						state = State.START;
					} else {
						state = State.ERROR;
					}
					break;
				case IN_DIV:
					if (ch == '*') {
						state = State.IN_COMMENT;
						pos++;
					} else {
						tokens.add(new Token(Kind.DIV, startPos, 1));
						state = State.START;
					}
					break;
				case IN_COMMENT:
					if ((pos+1)>=length)  {
						state = State.START;
					} else if (ch == '*' && (chars.charAt(pos+1) == '/')) {
						state = State.IN_END_COMMENT;
					}
					pos++;
					break;
				case IN_END_COMMENT:
					if (ch != '/') {
						state = State.ERROR;
					} else {
						state = State.START;
						pos++;
					}
					break;
				case ERROR:
					int line = 0;
					String[] lines  = chars.substring(0, pos+1).split("\n");
					line = lines.length - 1;
					
					int val = chars.lastIndexOf("\n", pos);
					throw new IllegalCharException("Illegal character at line " 
							+ line + " column " + (val == -1? pos: pos - val));
				default:
					System.out.println("Unhandled state");
			} //switch(state)
		} //while
		
		//tokens.add(new Token(Kind.EOF,pos,0));
		return this;  
	}

	/*
	 * Return the next token in the token list and update the state so that
	 * the next call will return the Token..  
	 */
	public Token nextToken() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum++);
	}
	
	/*
	 * Return the next token in the token list without updating the state.
	 * (So the following call to next will return the same token.)
	 */
	public Token peek(){
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum+1);		
	}

	/**
	 * Returns a LinePos object containing the line and position in line of the 
	 * given token.  
	 * 
	 * Line numbers start counting at 0
	 * 
	 * @param t
	 * @return
	 */
	public LinePos getLinePos(Token t) {
		return t.getLinePos();
	}
	
	/** 
	 * Skips the whitespaces starting from position 'pos'
	 * till next non-whitespace position. Returns the position
	 * of the next non-whitespace character
	 * 
	 * @param pos
	 * @return
	 */
	public int skipWhiteSpace(int pos) {
		int length = chars.length();
		int ch;
		
		while (pos <= length) {
			ch = pos < length ? chars.charAt(pos) : -1;

			if (Character.isWhitespace(ch)) {
				pos++;
			} else {
				break;
			}
		}
		
		return pos;
	}
	
	public int getNoOfTokens() {
		return tokens.size();
	}
	
	public int getNoOfTokensBetween(int pos1, int pos2) {
		Iterator<Token> itr = tokens.iterator();
		int noOfTokens = 0;
		
		while (itr.hasNext()) {
			Token t = itr.next();
			if (t.pos > pos1 && t.pos < pos2) {
				noOfTokens++;
			}
		}
		return noOfTokens;
	}
}
