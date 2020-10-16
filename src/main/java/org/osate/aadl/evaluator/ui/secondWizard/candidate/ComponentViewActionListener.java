package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Subcomponent;
import org.osate.aadl.evaluator.ui.ComponentCodeJDialog;

public class ComponentViewActionListener implements ActionListener
{
    private final JTree tree;

    public ComponentViewActionListener( final JTree tree )
    {
        this.tree = tree;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        Component component;
        
        if( node.getUserObject() instanceof Subcomponent )
        {
            component = ((Subcomponent) node.getUserObject()).getComponent();
        }
        else if( node.getUserObject() instanceof Component )
        {
            component = (Component) node.getUserObject();
        }
        else
        {
            return ;
        }
        
        final ComponentCodeJDialog dialog = new ComponentCodeJDialog(
            SwingUtilities.getWindowAncestor( tree )
        );
        dialog.setComponent( component );
        dialog.setVisible( true );
        dialog.dispose();
    }
    
}
