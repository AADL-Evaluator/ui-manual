package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;

public class ComponentDeleteActionListener implements ActionListener
{
    private final JTree tree;

    public ComponentDeleteActionListener( JTree jtree )
    {
        this.tree = jtree;
    }
    
    @Override
    public void actionPerformed( ActionEvent e ) 
    {
        for( TreePath path : tree.getSelectionPaths() )
        {
            dialog( path );
        }
    }
    
    private void dialog( TreePath path )
    {
        DefaultMutableTreeNode node = getSelectedNode( 
            (DefaultMutableTreeNode) path.getLastPathComponent() 
        );

        if( node == null )
        {
            return ;
        }
        
        // remove from project virtually
        Component component = (Component) node.getUserObject();
        component.getParent().getComponents().remove( component.getName() );
        
        // remove node in safe mode
        ((DefaultTreeModel) tree.getModel()).removeNodeFromParent( node );
    }
    
    private DefaultMutableTreeNode getSelectedNode( DefaultMutableTreeNode node )
    {
        if( node.getUserObject() instanceof Declaration 
            && node.getParent() != null )
        {
            return getSelectedNode( (DefaultMutableTreeNode) node.getParent() );
        }
        else if( node.getUserObject() instanceof Component )
        {
            return node;
        }
        else if( node.getParent() != null )
        {
            return getSelectedNode( (DefaultMutableTreeNode) node.getParent() );
        }
        else
        {
            return null;
        }
    }
    
}
