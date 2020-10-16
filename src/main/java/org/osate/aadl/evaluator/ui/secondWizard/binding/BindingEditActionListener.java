package org.osate.aadl.evaluator.ui.secondWizard.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

public class BindingEditActionListener implements ActionListener
{
    private final BindingListJPanel panel;

    public BindingEditActionListener( final BindingListJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        int row = panel.getTable().getSelectedRow();
        if( row  == -1 )
        {
            return ;
        }
        
        // --- //
        
        final BindingJDialog dialog = new BindingJDialog( 
            SwingUtilities.getWindowAncestor( panel ) 
        );
        
        dialog.setEvolution( panel.getEvolution() );
        dialog.setSystem( panel.getSystemCopy() );
        dialog.setBinding( panel.getTable().getSelectedObject() );
        dialog.rebuild();
        dialog.setVisible( true );
        dialog.dispose();
        
        if( dialog.isSaved() )
        {
            panel.getTable().getTabelModel().setData( 
                row , 
                dialog.getBinding() 
            );
        }
    }
    
}
