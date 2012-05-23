package com.aspectsecurity.jyconsole.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class JythonWordDetector
  implements IWordDetector
{
  public boolean isWordPart(char c)
  {
    return false;
  }

  public boolean isWordStart(char c)
  {
    return false;
  }
}