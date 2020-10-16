package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.ui.edit.ComponentJDialog;

public class ComponentEditActionListener implements ActionListener
{
    private final JTree tree;
    
    public ComponentEditActionListener( JTree jtree )
    {
        this.tree = jtree;
    }

    public Evolution getEvolution()
    {
        return null;
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

        final ComponentJDialog dialog = new ComponentJDialog( 
            SwingUtilities.getWindowAncestor( tree ) 
        );
        
        dialog.setComponent( getComponent( node ) );
        dialog.setVisible( true );
        dialog.dispose();
        
        if( dialog.isSaved() )
        {
            Component component = dialog.getComponent();
            
            node.setUserObject( component );
            ((DefaultTreeModel) tree.getModel()).nodeChanged( node );
            
            if( getEvolution() != null )
            {
                getEvolution().getComponents().put( 
                    component.getFullName() , 
                    component 
                );
            }
        }
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
    
    private Component getComponent( DefaultMutableTreeNode node )
    {
        Component component = (Component) node.getUserObject();
        
        return getEvolution() == null 
            ? component 
            : component.clone();
    }
    
}
