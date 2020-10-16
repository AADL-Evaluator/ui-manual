package org.osate.aadl.evaluator.ui.secondWizard.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.ui.secondWizard.ComponentSelectJDialog;
import org.osate.aadl.evaluator.evolution.SubcomponentUtils;

public class ComponentAddActionListener implements ActionListener
{
    private final BindingJDialog dialog;

    public ComponentAddActionListener( final BindingJDialog dialog )
    {
        this.dialog = dialog;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        final ComponentSelectJDialog d = new ComponentSelectJDialog( dialog );
        d.setProject( dialog.getSystem().getParent().getParent() );
        d.rebuild();
        d.setVisible( true );
        d.dispose();
        
        try
        {
            if( d.isSaved() )
            {
                Component selected = d.getComponentSelectd();
                Component system = dialog.getSystem();
                
                SubcomponentUtils.add( system , selected );
                SubcomponentUtils.add( dialog.getEvolution().getSystem() , selected );
                
                dialog.setSystem( system );
                dialog.rebuild();
            }
        }
        catch( Exception err )
        {
            err.printStackTrace();
            
            JOptionPane.showMessageDialog( 
                dialog , 
                err.getMessage() ,
                "Error" ,
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
}
