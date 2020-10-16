package org.osate.aadl.evaluator.ui.secondWizard.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

public class BindingDetailsActionListener implements ActionListener
{
    private final BindingListJPanel panel;

    public BindingDetailsActionListener( BindingListJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        if( panel.getTable().getSelectedRow() == -1 )
        {
            return ;
        }
        
        BindingDetailsJDialog dialog = new BindingDetailsJDialog(
            SwingUtilities.getWindowAncestor( panel )
        );
        
        dialog.setData( 
            panel.getSystemCopy() , 
            panel.getTable().getSelectedObjects()
        );
        
        dialog.setVisible( true );
        dialog.dispose();
    }
    
}