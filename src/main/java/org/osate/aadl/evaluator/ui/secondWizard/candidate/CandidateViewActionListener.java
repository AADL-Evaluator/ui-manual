package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import fluent.gui.impl.swing.FluentTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.ui.ComponentCodeJDialog;

public class CandidateViewActionListener implements ActionListener
{
    private final FluentTable<Candidate> table;

    public CandidateViewActionListener( FluentTable<Candidate> table )
    {
        this.table = table;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        Candidate candidate = table.getSelectedObject();
        if( candidate == null )
        {
            return ;
        }
        
        final ComponentCodeJDialog dialog = new ComponentCodeJDialog(
            SwingUtilities.getWindowAncestor( table )
        );
        
        dialog.setComponent( candidate.getComponent() );
        dialog.setVisible( true );
        dialog.dispose();
    }
    
}