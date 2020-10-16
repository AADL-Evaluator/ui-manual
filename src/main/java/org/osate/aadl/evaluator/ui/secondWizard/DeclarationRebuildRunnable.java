package org.osate.aadl.evaluator.ui.secondWizard;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.jdesktop.swingx.JXTree;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.project.Feature;
import org.osate.aadl.evaluator.project.Subcomponent;

public class DeclarationRebuildRunnable implements Runnable
{
    private final static Logger LOGGER = Logger.getLogger( DeclarationRebuildRunnable.class.getName() );
    
    private final JXTree tree;
    private final Component system;
    
    public DeclarationRebuildRunnable( final JXTree tree , final Component system )
    {
        this.tree = tree;
        this.system = system;
    }
    
    @Override
    public void run() 
    {
        LOGGER.info( "Declaration rebuild..." );
        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) ((DefaultTreeModel) tree.getModel()).getRoot();
        root.removeAllChildren();
        
        ((DefaultTreeModel) tree.getModel()).setRoot( 
            createNode( null , system ) 
        );
    }
    
    private DefaultMutableTreeNode createNode( Declaration declaration , Component component )
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( 
            declaration == null ? component : declaration , 
            true 
        );
        
        Map<String,Feature> features = component.getFeaturesAll();
        Map<String,Subcomponent> subcomponents = component.getSubcomponentsAll();
        
        LOGGER.log( Level.INFO , "Create node.....: {0}" , component.getFullName() );
        LOGGER.log( Level.INFO , "........features: {0}" , features.keySet() );
        LOGGER.log( Level.INFO , "...subcomponents: {0}" , subcomponents.keySet() );
        
        if( !features.isEmpty() )
        {
            node.add( createNode( "Features" , features.values() ) );
        }
        
        if( !subcomponents.isEmpty() )
        {
            node.add( createNode( "Subcomponents" , subcomponents.values() ) );
        }
        
        return node;
    }
    
    private DefaultMutableTreeNode createNode( String name , Collection<? extends Declaration> declarations )
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( name , true );
        
        for( Declaration declaration : declarations )
        {
            LOGGER.log( Level.WARNING  , "processing: {0} {1}.{2}" , new Object[]{ 
                declaration.getType() ,
                declaration.getParent() , 
                declaration.getName() 
            } );
            
            Component ref = declaration.getComponent();
            
            if( ref == null )
            {
                LOGGER.log( Level.WARNING  , "component not found: {0}" , declaration.getComponentReferenceName() );
                node.add( new DefaultMutableTreeNode( declaration ) );
            }
            else
            {
                node.add( createNode( declaration , ref ) );
            }
        }

        return node;
    }
    
}