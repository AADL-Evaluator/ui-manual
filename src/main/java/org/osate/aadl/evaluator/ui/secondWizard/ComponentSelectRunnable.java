package org.osate.aadl.evaluator.ui.secondWizard;

import java.util.Collection;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.ComponentPackage;
import org.osate.aadl.evaluator.project.Declaration;

public class ComponentSelectRunnable implements Runnable
{
    private final ComponentSelectJDialog dialog;

    public ComponentSelectRunnable( final ComponentSelectJDialog dialog )
    {
        this.dialog = dialog;
    }
    
    @Override
    public void run()
    {
        rebuild();
    }
    
    private void rebuild()
    {
        DefaultTreeModel treeModel = (DefaultTreeModel) dialog.getTree().getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        
        root.removeAllChildren();
        
        for( ComponentPackage aadl : dialog.getProject().getPackages().values() )
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode( aadl , true );
            
            for( Component component : aadl.getComponents().values() )
            {
                if( isToAdd( component ) )
                {
                    node.add( createNode( component ) );
                }
            }
            
            if( node.getChildCount() > 0 )
            {
                root.add( node );
            }
        }
        
        treeModel.setRoot( root );
    }
    
    private DefaultMutableTreeNode createNode( Component component )
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( component , true );
        
        if( !component.getFeatures().isEmpty() )
        {
            node.add( createNode( "Features" , component.getFeatures().values() ) );
        }
        
        if( !component.getSubcomponents().isEmpty() )
        {
            node.add( createNode( "Subcomponents" , component.getSubcomponents().values() ) );
        }
        
        if( !component.getConnections().isEmpty() )
        {
            node.add( createNode( "Connections" , component.getConnections().values() ) );
        }
        
        if( !component.getProperties().isEmpty() )
        {
            node.add( createNode( "Properties" , component.getProperties() ) );
        }
        
        return node;
    }
    
    private DefaultMutableTreeNode createNode( String name , Collection<? extends Declaration> declarations )
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( name , true );
        
        for( Declaration declaration : declarations )
        {
            node.add( new DefaultMutableTreeNode( declaration ) );
        }

        return node;
    }
    
    private boolean isToAdd( Component component )
    {
        boolean isAllType = dialog.getTypeJComboBox().getSelectedIndex() <= 0;
        String type = dialog.getTypeJComboBox().getSelectedItem().toString();
        String name = dialog.getNameJTextField().getText().trim().toUpperCase();
        
        // verify if the component is the same type of selected
        boolean isType = isAllType 
            ? true 
            : component.getType().equalsIgnoreCase( type );
        
        // verify if the component and parent contains the name digited
        boolean isName = name.isEmpty()
            ? true
            : (component.getName().toUpperCase().contains( name )
            || component.getParent().getName().toUpperCase().contains( name ) );
        
        // the component is selected if the both condictions are true
        return isType && isName;
    }
    
}
