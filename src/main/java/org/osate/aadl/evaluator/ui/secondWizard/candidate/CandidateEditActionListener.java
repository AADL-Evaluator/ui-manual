package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.Evolution;

public class CandidateEditActionListener implements ActionListener
{
    private final CandidateListJPanel panel;

    public CandidateEditActionListener( CandidateListJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        Candidate candidate = panel.getTable().getSelectedObject();
        if( candidate == null )
        {
            return ;
        }
        
        Evolution evolution = panel.getEvolution().clone();
        evolution.getCandidates().clear();
        evolution.getCandidates().addAll( panel.getTable().getTabelModel().getData() );
        
        // --- //
        
        final CandidateJDialog dialog = new CandidateJDialog(
            SwingUtilities.getWindowAncestor( panel )
        );
        
        dialog.setEvolution( evolution );
        dialog.setCandidate( candidate );
        dialog.setVisible( true );
        dialog.dispose();
        
        if( dialog.isSaved() )
        {
            panel.getTable().getTabelModel().setData( 
                panel.getTable().getSelectedRow() , 
                dialog.getCandidate()
            );
        }
    }
    
}
