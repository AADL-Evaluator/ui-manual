package org.osate.aadl.evaluator.ui.secondWizard.binding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.osate.aadl.evaluator.evolution.Binding;
import org.osate.aadl.evaluator.evolution.BusUtils;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.evolution.ProcessorUtils;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Connection;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.project.Feature;
import org.osate.aadl.evaluator.project.Subcomponent;
import org.osate.aadl.evaluator.evolution.SubcomponentUtils;
import org.osate.aadl.evaluator.project.Property;

public abstract class BindingRunnable implements Runnable
{
    private final JPanel panel;

    public BindingRunnable( final JPanel panel )
    {
        this.panel = panel;
    }
    
    public abstract Component getSystemCopy();
    public abstract Evolution getEvolution();
    public abstract void setData( List<Binding> bindings );
    public abstract void addData( Relationship relationship );
    
    @Override
    public void run() 
    {
        if( !getEvolution().getBindings().isEmpty() )
        {
            setData( getEvolution().getBindings() );
            return ;
        }
        
        Evolution evolution = getEvolution();
        setData( null );
        
        // procura pela conexões
        Component component = evolution
            .getDeclarations()
            .get( 0 )
            .getParent();
        
        List<Binding> exists = findAll( evolution , component );
        Map<Candidate,List<Feature>> featureAvaliable = getFeatureAvaliable( evolution );
        
        List<Relationship> replacement = suggestions( exists , featureAvaliable );
        suggestionsWrapper( replacement , featureAvaliable ); // try rebind
        
        findOtherBusInSystem( evolution , replacement );
        
        removeAndAddCandidates( evolution );                // remove old component and add news
        
        addChanges( replacement );           // add all connections
        addAvaliables( featureAvaliable );
    }
    
    private void removeAndAddCandidates( Evolution evolution )
    {
        // remove as conexões antigas
        for( Declaration declaration : evolution.getDeclarations() )
        {
            //TODO: se a declaração estiver no pai ou no implementation?
            getSystemCopy().getSubcomponents().remove( 
                declaration.getName() 
            );
        }
        
        // adiciona as novas
        for( Candidate candidate : evolution.getCandidates() )
        {
            Subcomponent sub = SubcomponentUtils.add( 
                getSystemCopy() , 
                candidate.getComponent() 
            );
            
            candidate.setSubComponentName( sub.getName() );
        }
    }
    
    private void addChanges( List<Relationship> replacement )
    {
        for( Relationship relation : replacement )
        {
            addData( relation );
        }
    }
    
    private void addAvaliables( Map<Candidate,List<Feature>> avaliable )
    {
        for( Map.Entry<Candidate,List<Feature>> entry : avaliable.entrySet() )
        {
            for( Feature feature : entry.getValue() )
            {
                addData( new Relationship( new Binding( 
                    entry.getKey().getSubComponentName() + "." + feature.getName() , 
                    "" 
                ) , null ) );
            }
        }
    }
    
    // -----------------------
    // ----------------------- VINCULAR
    // -----------------------
    
    private List<Relationship> suggestions( List<Binding> exists , Map<Candidate,List<Feature>> avaliable )
    {
        Map<Candidate,List<Feature>> copy = new HashMap<>( avaliable );
        List<Relationship> replacement = new LinkedList<>();
        
        for( Binding binding : exists )
        {
            List<Change> changes = new LinkedList<>();
            
            Feature a = binding.getConnection().getFeatureA();
            Feature b = binding.getConnection().getFeatureB();
            
            for( Map.Entry<Candidate,List<Feature>> entry : copy.entrySet() )
            {
                Candidate candidate = entry.getKey();
                
                for( Feature f : entry.getValue() )
                {
                    if( a != null && a.isValueEquals( f ) )
                    {
                        Change change = new Change();
                        change.candidate = candidate;
                        change.connection = binding.getConnection();
                        change.feature = f;
                        change.partA = true;
                        
                        changes.add( change );
                    }
                    else if( b != null && b.isValueEquals( f ) )
                    {
                        Change change = new Change();
                        change.candidate = candidate;
                        change.connection = binding.getConnection();
                        change.feature = f;
                        change.partA = false;
                        
                        changes.add( change );
                    }
                }
            }
            
            if( changes.size() == 1 )
            {
                replacement.add( 
                    new Relationship( binding , changes.get( 0 ) ) 
                );
                
                Change change = changes.get( 0 );
                avaliable.get( change.candidate ).remove( change.feature );
            }
            else if( changes.size() > 1 )
            {
                BindingConflictJDialog d = new BindingConflictJDialog(
                    SwingUtilities.getWindowAncestor( panel )
                );
                d.setChanges( changes );
                d.setVisible( true );
                d.dispose();
                
                List<Change> selected = d.getChangesSelected();
                Change c1 = selected.remove( 0 );
                
                replacement.add( new Relationship( binding , c1 ) );
                
                avaliable.get( c1.candidate ).remove( c1.feature );
                
                // associe with other selected
                if( !selected.isEmpty() )
                {
                    for( Change c2 : selected )
                    {
                        Binding cloned = binding.clone();
                        cloned.setConnection( null );
                        
                        replacement.add( new Relationship( binding , c2 ) );
                        
                        avaliable.get( c2.candidate ).remove( c2.feature );
                    }
                }
            }
            else
            {
                replacement.add( new Relationship( binding , null ) );
            }
        }
        
        return replacement;
    }
    
    /**
     * Este método irá combinar as features que não são compatíveis.
     * 
     * @param replacement
     * @param avaliable 
     */
    private void suggestionsWrapper( List<Relationship> replacement , Map<Candidate,List<Feature>> avaliable )
    {
        if( avaliable.isEmpty() )
        {
            return ;
        }
        
        List<Relationship> copy = new LinkedList<>( replacement );
        
        for( Relationship relationship : copy )
        {
            if( relationship.change != null )
            {
                continue ;
            }
            
            final BindingIncompatibleJDialog dialog = new BindingIncompatibleJDialog(
                SwingUtilities.getWindowAncestor( panel )
            );
            
            dialog.setBinding( relationship.binding );
            
            try
            {
                dialog.setVisible( dialog.setAvaliable( avaliable ) );
            }
            catch( Exception err )
            {
                err.printStackTrace();
            }
            
            dialog.dispose();
            
            if( dialog.isSaved() )
            {
                boolean added = false;
                
                for( BindingIncompatibleJDialog.Line line : dialog.getSelected() )
                {
                    Change change = new Change();
                    change.candidate = line.candidate;
                    change.connection = relationship.binding.getConnection();
                    change.feature = line.feature;
                    change.partA = true;

                    if( added )
                    {
                        change.connection = null;
                        replacement.add( new Relationship( relationship.binding.clone() , change ) );
                    }
                    else
                    {
                        replacement.remove( relationship );
                        replacement.add( new Relationship( relationship.binding , change ) );
                        
                        added = true;
                    }

                    avaliable.get( line.candidate ).remove( line.feature );
                }
            }
        }
    }
    
    private Map<Candidate,List<Feature>> getFeatureAvaliable( Evolution evolution )
    {
        Map<Candidate,List<Feature>> avaliable = new HashMap<>();
        
        for( Candidate candidate : evolution.getCandidates() )
        {
            avaliable.put( 
                candidate , 
                new LinkedList<>(
                    candidate.getComponent().getFeaturesAll().values()
                )
            );
        }
        
        return avaliable;
    }
    
    // -----------------------
    // ----------------------- CONNECTIONS EXISTS
    // -----------------------
    
    private void findOtherBusInSystem( Evolution evolution , List<Relationship> replacement )
    {
        for( Relationship relationship : replacement )
        {
            if( relationship.change == null
                || !relationship.change.feature.isBus() )
            {
                continue ;
            }
            
            for( Subcomponent sub : evolution.getSystem().getSubcomponentsAll().values() )
            {
                // se não é um bus, se já foi usada e se não for compatível, cancelar!
                if( !sub.isBus() 
                    || relationship.change == null
                    || relationship.change.feature == null 
                    || relationship.change.feature.getComponent() == null )
                {
                    continue ;
                }
                
                String t1 = sub.getComponent().getFullName();
                String t2 = relationship.change.feature.getComponent().getFullName();
                
                if( wasItUsed( sub , relationship ) )
                {
                    System.out.println( "is was used: " + t1 + " | " + t2 );
                    continue ;
                }
                
                if( !t1.equalsIgnoreCase( t2 ) )
                {
                    System.out.println( "é diferente: " + t1 + " | " + t2 );
                    continue;
                }
                
                // ---- cloned
                
                Binding binding = relationship.binding.clone();
                binding.setPartB( sub.getName() );
                
                Change change = relationship.change.clone();
                
                relationship.alternatives.add( new Relationship( binding , change ) );
            }
        }
    }
    
    private boolean wasItUsed( Subcomponent sub , Relationship relationship )
    {
        if( relationship.binding != null 
            && relationship.binding.getConnection() != null
            && relationship.binding.getConnection().getSubcomponentNameB().equalsIgnoreCase( sub.getName() ) )
        {
            return true;
        }
        
        if( relationship.change.connection != null
            && (relationship.change.connection.getSubcomponentNameA().equalsIgnoreCase( sub.getName() )
             || relationship.change.connection.getSubcomponentNameB().equalsIgnoreCase( sub.getName() ) ) )
        {
            return true;
        }
        
        for( Relationship alternative : relationship.alternatives )
        {
            return wasItUsed( sub , alternative );
        }
        
        return false;
    }
    
    private List<Binding> findAll( Evolution evolution , Component component )
    {
        List<Binding> bindinds = findAllConnections( evolution , component );
        
        for( Binding binding : bindinds )
        {
            if( binding.getConnection().getSubcomponentA().isBus() 
                || binding.getConnection().getSubcomponentB().isBus() )
            {
                String bus = binding.getConnection().getSubcomponentA().isBus()
                    ? binding.getConnection().getSubcomponentNameA()
                    : binding.getConnection().getSubcomponentNameB();
                
                binding.setCpu( BusUtils.toString( 
                    BusUtils.getProcessorsByBus( component , bus ) 
                ) );
            }
            else
            {
                // ---- find the BUS
                Property p1 = BusUtils.find( component , binding.getConnection().getName() );
                binding.setBus( BusUtils.getBusName( p1 ) );

                // ---- find the CPU
                String process = binding.getConnection().getSubcomponentB().isProcess()
                    ? binding.getConnection().getSubcomponentNameB()
                    : binding.getConnection().getSubcomponentNameA();

                Property p2 = ProcessorUtils.find( component , process );
                binding.setCpu( ProcessorUtils.getProcessorName( p2 ) );
            }
        }
        
        return bindinds;
    }
    
    private List<Binding> findAllConnections( Evolution evolution , Component component )
    {
        List<Binding> exists = new LinkedList<>();
        
        for( Connection connection : component.getConnectionsAll().values() )
        {
            if( evolution.getDeclarations().contains( connection.getSubcomponentA() ) )
            {
                exists.add( new Binding( connection , true ) );
            }
            else if( evolution.getDeclarations().contains( connection.getSubcomponentB() ) )
            {
                exists.add( new Binding( connection , false ) );
            }
        }
        
        return exists;
    }
    
    
    
    
    
    
    
    public class Relationship {
        private Binding binding;
        private Change change;
        private List<Relationship> alternatives;

        public Relationship( final Binding binding , final Change change ){
            this.binding = binding;
            this.change = change;
            this.alternatives = new LinkedList<>();
            
            if( change != null )
            {
                binding.setPartA( 
                    change.getCandidateName() + "." + change.feature.getName()
                );
            }
        }

        public Binding getBinding() {
            return binding;
        }

        public Change getChange() {
            return change;
        }

        public List<Relationship> getAlternatives() {
            return alternatives;
        }
        
    }
    
    
    
    
    public class Change implements Cloneable {
        private Candidate candidate;
        private Feature feature;
        private Connection connection;
        private boolean partA;

        public Connection getConnection() 
        {
            Connection clone = connection.clone();
                        
            if( partA )
            {
                clone.setValue( clone.getValue().replace( 
                    clone.getSubcomponentAndFeatureA() , 
                    getCandidateName() + "." + feature.getName()
                ) );
            }
            else
            {
                clone.setValue( clone.getValue().replace( 
                    clone.getSubcomponentAndFeatureB() , 
                    getCandidateName() + "." + feature.getName()
                ) );
            }
            
            return clone;
        }
        
        public String getCandidateName()
        {
            if( candidate.getSubComponentName() != null
                && !candidate.getSubComponentName().isEmpty() )
            {
                return candidate.getSubComponentName();
            }
            
            String name = candidate.getComponent().getName();
                    
            return name.contains( "." )
                ? name.substring( 0 , name.indexOf( "." ) )
                : name;
        }

        @Override
        protected Change clone()
        {
            try
            {
                return (Change) super.clone();
            }
            catch( Exception err )
            {
                Change change = new Change();
                change.connection = this.connection.clone();
                change.candidate = this.candidate;
                change.feature   = this.feature;
                change.partA     = this.partA;
                
                return change;
            }
        }
        
        
        
    }
    
}
