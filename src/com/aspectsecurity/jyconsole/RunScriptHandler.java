package com.aspectsecurity.jyconsole;

import com.aspectsecurity.jyconsole.editors.JythonEditor;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class RunScriptHandler extends AbstractHandler
{
  public Object execute(ExecutionEvent event)
    throws ExecutionException
  {
    IEditorPart part = HandlerUtil.getActiveEditor(event);

    if ((part instanceof JythonEditor)) {
      JythonEditor editor = (JythonEditor)part;
      editor.runScript();
    }

    return null;
  }
}