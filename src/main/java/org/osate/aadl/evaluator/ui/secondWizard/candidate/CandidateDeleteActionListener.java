package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import fluent.gui.impl.swing.FluentTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.osate.aadl.evaluator.evolution.Candidate;

public class CandidateDeleteActionListener implements ActionListener
{
    private final FluentTable<Candidate> table;

    public CandidateDeleteActionListener( FluentTable<Candidate> table )
    {
        this.table = table;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        table.removeSelectedObjects();
    }
    
}