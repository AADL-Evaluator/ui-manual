package org.osate.aadl.evaluator.ui.secondWizard.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.osate.aadl.evaluator.project.Feature;
import org.osate.aadl.evaluator.project.Subcomponent;

public class ComponentDeleteActionListener implements ActionListener
{
    private final BindingJDialog dialog;

    public ComponentDeleteActionListener( final BindingJDialog dialog )
    {
        this.dialog = dialog;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        for( TreePath path : dialog.getTree().getSelectionPaths() )
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            
            if( node.getUserObject() instanceof Feature )
            {
                node = (DefaultMutableTreeNode) node.getParent();
            }
            
            if( node.getUserObject() instanceof Subcomponent )
            {
                ((DefaultTreeModel) dialog.getTree().getModel()).removeNodeFromParent( 
                    node 
                );
            }
        }
    }
    
}
