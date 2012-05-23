package com.aspectsecurity.jyconsole.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class JythonRuleScanner extends RuleBasedScanner
{
  private int defaultStyle = 0;
  private int boldStyle = 1;
  int[] keywordRgb = { 127, 0, 85 };
  int[] commentRgb = { 63, 127, 95 };
  int[] stringRgb = { 0, 0, 192 };

  private static String[] langKeywords = { 
    "and", "del", "from", "not", "while", 
    "as", "elif", "global", "or", "with", 
    "assert", "else", "if", "pass", "yield", 
    "break", "except", "import", "print", 
    "class", "exec", "in", "raise", 
    "continue", "finally", "is", "return", 
    "def", "for", "lambda", "try" };

  Color defaultFgColor = Display.getCurrent().getSystemColor(2);
  Color keywordFgColor = new Color(Display.getCurrent(), this.keywordRgb[0], this.keywordRgb[1], this.keywordRgb[2]);
  Color commentFgColor = new Color(Display.getCurrent(), this.commentRgb[0], this.commentRgb[1], this.commentRgb[2]);
  Color stringFgColor = new Color(Display.getCurrent(), this.stringRgb[0], this.stringRgb[1], this.stringRgb[2]);
  Color bgColor = null;
  private Token commentToken;
  private Token stringToken;
  private Token keyToken;
  private Token defaultToken;
  private IWordDetector wDetector = new IWordDetector() {
    public boolean isWordStart(char c) {
      return Character.isJavaIdentifierStart(c);
    }
    public boolean isWordPart(char c) {
      return !Character.isWhitespace(c);
    }
  };

  public JythonRuleScanner()
  {
    IRule[] rules = new IRule[5];
    this.defaultToken = new Token(new TextAttribute(this.defaultFgColor, this.bgColor, this.defaultStyle));

    setDefaultReturnToken(this.defaultToken);

    this.keyToken = new Token(new TextAttribute(this.keywordFgColor, this.bgColor, this.boldStyle));

    rules[0] = createRule(langKeywords, this.keyToken);

    this.commentToken = new Token(new TextAttribute(this.commentFgColor, this.bgColor, this.defaultStyle));

    rules[1] = new SingleLineRule("#", "", this.commentToken);

    rules[2] = new WhitespaceRule(new IWhitespaceDetector() {
      public boolean isWhitespace(char c) {
        return Character.isWhitespace(c);
      }
    });
    this.stringToken = new Token(new TextAttribute(this.stringFgColor, this.bgColor, this.defaultStyle));

    rules[3] = new MultiLineRule("\"", "\"", this.stringToken, '\\');
    rules[4] = new MultiLineRule("'", "'", this.stringToken, '\\');

    setRules(rules);
  }

  public void updateColors() {
    this.defaultToken.setData(new TextAttribute(this.defaultFgColor, this.bgColor, this.defaultStyle));
    this.keyToken.setData(new TextAttribute(this.keywordFgColor, this.bgColor, this.boldStyle));
    this.commentToken.setData(new TextAttribute(this.commentFgColor, this.bgColor, this.defaultStyle));
    this.stringToken.setData(new TextAttribute(this.stringFgColor, this.bgColor, this.defaultStyle));
  }

  private IRule createRule(String[] systemWords, Token style) {
    WordRule rule = new WordRule(this.wDetector) {
      public IToken evaluate(ICharacterScanner scanner) {
        if (JythonRuleScanner.this.fOffset > 0) {
          scanner.unread();
          if (!Character.isWhitespace(scanner.read())) {
            return Token.UNDEFINED;
          }
        }
        return super.evaluate(scanner);
      }
    };
    for (String word : systemWords) {
      rule.addWord(word, style);
    }
    return rule;
  }
}