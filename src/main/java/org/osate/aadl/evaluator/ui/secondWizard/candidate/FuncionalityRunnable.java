package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.evolution.FuncionalityUtils;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Property;

public class FuncionalityRunnable implements Runnable
{
    private final JTree tree;
    private final JList<String> list;
    private final Map<String,List<Property>> funcionalities;
    
    private Evolution evolution;
    private Candidate candidate;
    
    public FuncionalityRunnable( JTree tree , JList<String> list , Map<String,List<Property>> funcionalities )
    {
        this.tree = tree;
        this.list = list;
        this.funcionalities = funcionalities;
    }
    
    public FuncionalityRunnable setEvolution( Evolution evolution )
    {
        this.evolution = evolution;
        return this;
    }
    
    public FuncionalityRunnable setCandidate( Candidate candidate )
    {
        this.candidate = candidate;
        return this;
    }

    @Override
    public void run() 
    {
        DefaultListModel<String> listModel = (DefaultListModel<String>) list.getModel();
        listModel.removeAllElements();
        
        list.setSelectedIndices(
            feed( listModel , getFuncionalitiesAvaliable() )
        );
    }
    
    private int[] feed( DefaultListModel<String> listModel , Collection<String> funcionalities )
    {
        if( funcionalities == null || funcionalities.isEmpty() )
        {
            return new int[ 0 ];
        }
        
        int[] indices = new int[ candidate.getFuncionalities().size() ];
        int pos = 0;
        
        for( String funcionality : funcionalities )
        {
            listModel.addElement( funcionality );
            
            if( candidate.getFuncionalities().containsKey( funcionality ) )
            {
                indices[ pos++ ] = listModel.getSize() - 1;
            }
        }
        
        return indices;
    }
    
    private Collection<String> getFuncionalitiesAvaliable()
    {
        if( tree.getSelectionPath() == null )
        {
            return null;
        }
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        if( !(node.getUserObject() instanceof Component) )
        {
            return null;
        }
        
        Component component = (Component) node.getUserObject();
        
        funcionalities.clear();
        funcionalities.putAll( FuncionalityUtils.list( component ) );
        
        Set<String> available = FuncionalityUtils.available( evolution );
        available.addAll( candidate.getFuncionalities().keySet() );
        
        for( String a : new LinkedList<>( available ) )
        {
            if( !funcionalities.containsKey( a ) )
            {
                available.remove( a );
            }
        }
        
        return available;
    }
    
}