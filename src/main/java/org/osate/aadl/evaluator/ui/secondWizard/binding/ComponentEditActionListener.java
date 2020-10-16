package org.osate.aadl.evaluator.ui.secondWizard.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.ui.edit.ComponentJDialog;

public class ComponentEditActionListener implements ActionListener
{
    private final BindingJDialog dialog;

    public ComponentEditActionListener( final BindingJDialog dialog )
    {
        this.dialog = dialog;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        Component component = dialog.getSubcomponentSelected().getComponent();
        if( component == null )
        {
            JOptionPane.showMessageDialog( 
                dialog , 
                "The component was not found!" , 
                "Error" , 
                JOptionPane.ERROR_MESSAGE 
            );
            
            return ;
        }
        
        final ComponentJDialog d = new ComponentJDialog( dialog );
        d.setComponent( component.clone() );
        d.setVisible( true );
        d.dispose();
        
        try
        {
            if( d.isSaved() )
            {
                Component selected = d.getComponent();
                
                dialog.getEvolution().getComponents().put( 
                    selected.getFullName() , 
                    selected 
                );
                
                dialog.rebuild();
            }
        }
        catch( Exception err )
        {
            JOptionPane.showMessageDialog( 
                dialog , 
                err.getMessage() ,
                "Error" ,
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
}
