package com.aspectsecurity.jyconsole.editors;

import com.aspectsecurity.jyconsole.JythonConsole;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.python.core.PyException;
import org.python.util.PythonInterpreter;

public class JythonEditor extends TextEditor
{
  private static final String RUN_ACTION_ID = "jyconsole.RunScript";
  Action runAction;
  Action clearAction;
  Action saveAction;

  public JythonEditor()
  {
    setSourceViewerConfiguration(new JythonSourceViewerConfiguration());
    setDocumentProvider(new JythonDocumentProvider());
  }

  public void dispose() {
    super.dispose();
  }

  protected void createActions() {
    super.createActions();

    this.runAction = new Action()
    {
      public void run() {
        FileEditorInput input = (FileEditorInput)JythonEditor.this.getEditorInput();
        final String path = getPath(input);

        IDocumentProvider provider = JythonEditor.this.getDocumentProvider();
        IDocument doc = provider.getDocument(JythonEditor.this.getEditorInput());

        final String script = doc.get();

        Job job = new Job("Jython script execution") {
          protected IStatus run(IProgressMonitor monitor) {
            monitor.beginTask("Running script...", 100);

            JythonConsole console = JythonEditor.this.findConsole(path);
            console.activate();

            MessageConsoleStream out = console.getOut();
            MessageConsoleStream err = console.getErr();

            if (!console.showedGreeting()) {
              console.setGreeted();
              out.println("Welcome to JyConsole!");
              out.println("=====================");
              out.println();
            }

            try
            {
              PythonInterpreter interp = new PythonInterpreter();
              interp.setOut(out);
              interp.setErr(out);
              interp.exec(script);
            } catch (PyException pe) {
              pe.printStackTrace();
              err.println(pe.getMessage());
            }
            monitor.done();
            return Status.OK_STATUS;
          }
        };
        job.schedule();
      }

      private String getPath(FileEditorInput input) {
        return "JyConsole - " + input.getFile().getFullPath().toOSString();
      }
    };
    this.runAction.setText("Run script");
    this.runAction.setToolTipText("Runs the written script");
    this.runAction.setAccelerator(262226);
    this.runAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
      .getImageDescriptor("IMG_TOOL_REDO"));

    this.clearAction = new Action() {
      public void run() {
        IDocumentProvider provider = JythonEditor.this.getDocumentProvider();
        IDocument doc = provider.getDocument(JythonEditor.this.getEditorInput());
        doc.set("");
      }
    };
    this.clearAction.setText("Clear script");
    this.clearAction.setToolTipText("Clears the script editor");
    this.clearAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
      .getImageDescriptor("IMG_ELCL_REMOVE"));
  }

  protected IMenuListener createContextMenuListener()
  {
    IMenuListener listener = new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager mnu) {
        mnu.add(JythonEditor.this.runAction);
        mnu.add(JythonEditor.this.clearAction);
        mnu.add(new Separator());
      }
    };
    return listener;
  }

  private JythonConsole createConsole(String name)
  {
    ConsolePlugin plugin = ConsolePlugin.getDefault();
    IConsoleManager conMan = plugin.getConsoleManager();
    IConsole[] existing = conMan.getConsoles();
    for (int i = 0; i < existing.length; i++) {
      if (name.equals(existing[i].getName()))
        return (JythonConsole)existing[i];
    }
    final JythonConsole myConsole = new JythonConsole(name, null);
    conMan.addConsoles(new IConsole[] { myConsole });
    Runnable r = new Runnable() {
        public void run() {
            MessageConsoleStream out = myConsole.newMessageStream();
            MessageConsoleStream err = myConsole.newMessageStream();

            out.setColor(new Color(Display.getDefault(), 0, 255, 0));
            err.setColor(new Color(Display.getDefault(), 255, 0, 0));

            myConsole.setOut(out);
            myConsole.setErr(err);

            myConsole.setBackground(Display.getDefault().getSystemColor(2));
          }
        };
    
    Display.getDefault().syncExec(r);
    return myConsole;
  }

  public void runScript() {
    this.runAction.run();
  }

  private JythonConsole findConsole(String name) {
    return createConsole(name);
  }
}