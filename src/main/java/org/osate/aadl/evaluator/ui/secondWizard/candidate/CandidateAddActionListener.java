package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.evolution.FuncionalityUtils;

public class CandidateAddActionListener implements ActionListener
{
    private final CandidateListJPanel panel;

    public CandidateAddActionListener( CandidateListJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        try
        {
            add();
        }
        catch( Exception err )
        {
            JOptionPane.showMessageDialog( 
                panel , 
                err.getMessage() ,
                "Error" ,
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void add() throws Exception
    {
        Evolution evolution = validate();
        
        final CandidateJDialog dialog = new CandidateJDialog(
            SwingUtilities.getWindowAncestor( panel )
        );
        
        dialog.setEvolution( evolution );
        dialog.setCandidate( new Candidate() );
        dialog.setVisible( true );
        dialog.dispose();
        
        if( dialog.isSaved() )
        {
            panel.getTable().addData( dialog.getCandidate() );
        }
    }
    
    private Evolution validate() throws Exception
    {
        Evolution evolution = panel.getEvolution().clone();
        evolution.getCandidates().clear();
        evolution.getCandidates().addAll( panel.getTable().getTabelModel().getData() );
        
        if( !FuncionalityUtils.list( evolution ).isEmpty() 
            && FuncionalityUtils.available( evolution ).isEmpty() )
        {
            throw new Exception( "All funcionalities were selected." );
        }
        
        return evolution;
    }
    
}
