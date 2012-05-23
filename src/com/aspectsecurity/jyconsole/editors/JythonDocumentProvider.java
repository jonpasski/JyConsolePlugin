package com.aspectsecurity.jyconsole.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class JythonDocumentProvider extends FileDocumentProvider
{
  protected IDocument createDocument(Object element)
    throws CoreException
  {
    return super.createDocument(element);
  }
}