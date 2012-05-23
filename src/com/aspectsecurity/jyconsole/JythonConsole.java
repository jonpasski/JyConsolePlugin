package com.aspectsecurity.jyconsole;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class JythonConsole extends MessageConsole
{
  MessageConsoleStream out;
  MessageConsoleStream err;
  boolean showedGreeting = false;

  public JythonConsole(String name, ImageDescriptor imageDescriptor)
  {
    super(name, imageDescriptor);
  }

  public boolean showedGreeting()
  {
    return this.showedGreeting;
  }

  public void setGreeted() {
    this.showedGreeting = true;
  }

  public MessageConsoleStream getOut() {
    return this.out;
  }
  public void setOut(MessageConsoleStream out) {
    this.out = out;
  }
  public MessageConsoleStream getErr() {
    return this.err;
  }
  public void setErr(MessageConsoleStream err) {
    this.err = err;
  }
}