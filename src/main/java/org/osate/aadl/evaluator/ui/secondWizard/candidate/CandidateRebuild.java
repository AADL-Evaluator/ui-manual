package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.util.Collection;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.ComponentPackage;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.project.Project;

public class CandidateRebuild 
{
    private Evolution evolution;
    private Candidate candidate;
    private Project project;

    public CandidateRebuild() 
    {
        // faz nada
    }
    
    public CandidateRebuild setEvolution( Evolution evolution )
    {
        this.evolution = evolution;
        this.project = evolution.getSystem().getParent().getParent();
        
        return this;
    }
    
    public CandidateRebuild setCandidate( Candidate candidate )
    {
        this.candidate = candidate;
        return this;
    }

    public Evolution getEvolution() 
    {
        return evolution;
    }
    
    public DefaultMutableTreeNode rebuild( DefaultMutableTreeNode root , Condiction condiction )
    {
        if( evolution.getDeclarations()
            .get( 0 )
            .getComponent() == null )
        {
            JOptionPane.showConfirmDialog( null , "The component of the feature was not found." );
            return null;
        }
        
        condiction.setEvolution( evolution );
        
        DefaultMutableTreeNode selected = null;
        
        for( ComponentPackage pack : project.getPackages().values() )
        {
            DefaultMutableTreeNode packNode = new DefaultMutableTreeNode( pack , true );
            
            for( Component component : pack.getComponents().values() )
            {
                if( !condiction.isAcceptable( component )
                    || hasSelected( component ) )
                {
                    continue ;
                }

                DefaultMutableTreeNode componentNode = createNode( component );
                packNode.add( componentNode );

                if( isToSelect( component )  )
                {
                    selected = componentNode;
                }
            }
            
            if( packNode.getChildCount() > 0 )
            {
                root.add( packNode );
            }
        }
        
        return selected;
    }
    
    private boolean isToSelect( Component component )
    {
        return candidate.getComponent() != null 
            && candidate.getComponent().getFullName().equalsIgnoreCase( component.getFullName() );
    }
    
    private boolean hasSelected( Component component )
    {
        for( Candidate selected : evolution.getCandidates() )
        {
            if( selected != this.candidate 
                && selected.getComponent() == component )
            {
                return true;
            }
        }
        
        return false;
    }
    
    private DefaultMutableTreeNode createNode( Component component )
    {
        // caso o component tenha sido editado, deve-se pegar do getComponents()
        
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( 
            evolution.getComponents().containsKey( component.getFullName() )
                ? evolution.getComponents().get( component.getFullName() ) 
                : component 
        );
        
        /*
        Retirei isso aqui para não poluir muito o visual
        alem disso, existe o botão View para visualizar o componente.
        
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
        */
        
        return node;
    }
    
    private DefaultMutableTreeNode createNode( String name , Collection<? extends Declaration> objects )
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( name , true );
        
        for( Declaration declaration : objects )
        {
            node.add( new DefaultMutableTreeNode( declaration ) );
        }

        return node;
    }
    
}