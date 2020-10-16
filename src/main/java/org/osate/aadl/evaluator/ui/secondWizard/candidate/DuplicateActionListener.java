package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.ComponentPackage;
import org.osate.aadl.evaluator.project.Feature;
import org.osate.aadl.evaluator.project.Project;
import org.osate.aadl.evaluator.project.Property;
import org.osate.aadl.evaluator.project.Subcomponent;
import org.osate.aadl.evaluator.ui.ProjectUtils;

public class DuplicateActionListener implements ActionListener
{
    private final JTree jtree;

    public DuplicateActionListener( JTree jtree )
    {
        this.jtree = jtree;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        for( TreePath path : jtree.getSelectionPaths() )
        {
            duplicate( path );
        }
    }
    
    private void duplicate( TreePath path )
    {
        DefaultMutableTreeNode node = getSelectedNode( 
            (DefaultMutableTreeNode) path.getLastPathComponent() 
        );
        
        if( node == null )
        {
            return ;
        }
        
        Component cloned = duplicate( node.getUserObject() );
        
        // ------ create and add the new component
        ((DefaultTreeModel) jtree.getModel()).insertNodeInto(
            new DefaultMutableTreeNode( cloned ) , 
            (DefaultMutableTreeNode) node.getParent() , 
            node.getParent().getChildCount()
        );
    }
    
    private DefaultMutableTreeNode getSelectedNode( DefaultMutableTreeNode node )
    {
        if( node.getUserObject() instanceof Feature
            || node.getUserObject() instanceof Subcomponent
            || node.getUserObject() instanceof Component )
        {
            return node;
        }
        else if( node.getUserObject() instanceof ComponentPackage 
            || node.getUserObject() instanceof Project )
        {
            return null;
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
    
    private Component duplicate( Object object )
    {
        if( object instanceof Feature )
        {
            return duplicate( ((Feature) object).getComponent() );
        }
        else if( object instanceof Subcomponent )
        {
            return duplicate( ((Subcomponent) object).getComponent() );
        }
        else if( object instanceof Subcomponent )
        {
            return duplicate( ((Subcomponent) object).getComponent() );
        }
        else if( object instanceof Component )
        {
            return duplicate( (Component) object );
        }
        else
        {
            return null;
        }
    }
    
    private Component duplicate( Component component )
    {
        Component cloned = component.clone();
        cloned.getProperties().clear();                             // clear all properties

        for( Property p : component.getPropertiesAll() )            // clone all properties (including this, implemented and extends)
        {
            cloned.getProperties().add( p.clone() );
        }

        cloned.setName( 
            ProjectUtils.getSuggestName(                            // get a suggest name
                cloned.getParent().getComponents().keySet() ,       // pass components registred
                cloned.getName()                                    // pass original name
            )
        );
        
        cloned.getParent().getComponents().put( 
            cloned.getName() , 
            cloned 
        );
        
        return cloned;
    }
    
}