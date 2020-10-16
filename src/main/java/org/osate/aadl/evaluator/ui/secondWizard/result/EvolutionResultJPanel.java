package org.osate.aadl.evaluator.ui.secondWizard.result;

import org.osate.aadl.evaluator.ui.mainWizard.AadlComponentRunnable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.project.Component;

public class EvolutionResultJPanel extends javax.swing.JPanel 
{
    private final Executor executor;
    private Evolution evolution;
    private Component system;
    
    public EvolutionResultJPanel() 
    {
        executor = Executors.newSingleThreadExecutor();
        
        initComponents();
        init();
    }
    
    private void init()
    {
        // do nothing
    }

    public void setEvolution( Evolution evolution ) throws Exception
    {
        this.evolution = evolution;
        this.system = evolution.getSystemWidthChanges();
        
        this.executor.execute(new AadlComponentRunnable( aadlJTextArea , evolution , system ) 
        );
    }

    public Evolution getEvolution() 
    {
        return evolution;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        aadlJTextArea = new javax.swing.JTextArea();
        resultJScrollPane = new javax.swing.JScrollPane();
        reqJScrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        aadlJTextArea.setEditable(false);
        aadlJTextArea.setColumns(20);
        aadlJTextArea.setRows(5);
        jScrollPane1.setViewportView(aadlJTextArea);

        jTabbedPane1.addTab("AADL Result", jScrollPane1);
        jTabbedPane1.addTab("Results", resultJScrollPane);
        jTabbedPane1.addTab("Reqspec", reqJScrollPane);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea aadlJTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JScrollPane reqJScrollPane;
    private javax.swing.JScrollPane resultJScrollPane;
    // End of variables declaration//GEN-END:variables
}
