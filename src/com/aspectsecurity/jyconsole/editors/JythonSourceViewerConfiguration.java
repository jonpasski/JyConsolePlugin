package com.aspectsecurity.jyconsole.editors;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class JythonSourceViewerConfiguration extends SourceViewerConfiguration
{
  private JythonRuleScanner scanner;

  public JythonRuleScanner getTagScanner()
  {
    if (this.scanner == null) {
      this.scanner = new JythonRuleScanner();
    }
    return this.scanner;
  }

  public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
    PresentationReconciler reconciler = new PresentationReconciler();
    DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getTagScanner()) {
      public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent event, boolean documentPartitioningChanged) {
        return partition;
      }
    };
    reconciler.setDamager(dr, "__dftl_partition_content_type");
    reconciler.setRepairer(dr, "__dftl_partition_content_type");
    return reconciler;
  }
}