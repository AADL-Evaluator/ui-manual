package org.osate.aadl.evaluator.ui.secondWizard.binding;

import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.jdesktop.swingx.JXTree;
import org.osate.aadl.evaluator.evolution.Binding;
import org.osate.aadl.evaluator.evolution.BindingUtils;
import org.osate.aadl.evaluator.evolution.BusUtils;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Feature;
import org.osate.aadl.evaluator.project.Subcomponent;

public class RebuildRunnable implements Runnable
{
    public static final int SELECT_COMPATIBLE = 0;
    public static final int SELECT_TYPE = 1;
    public static final int SELECT_ALL = 2;
    
    private JXTree tree;
    private JComboBox<String> cpuAndBuses;
    private Component system;
    private Binding binding;
    private int selectBy;
    
    public RebuildRunnable()
    {
        // do nothing
    }

    public RebuildRunnable setTree( JXTree tree )
    {
        this.tree = tree;
        return this;
    }

    public RebuildRunnable setCpuAndBuses( JComboBox<String> cpuAndBuses )
    {
        this.cpuAndBuses = cpuAndBuses;
        return this;
    }
    
    public RebuildRunnable setSystem( Component system )
    {
        this.system = system;
        return this;
    }

    public RebuildRunnable setBinding( Binding binding )
    {
        this.binding = binding;
        return this;
    }

    public RebuildRunnable setSelectBy( int selectBy )
    {
        this.selectBy = selectBy;
        return this;
    }
    
    // -------------------------- //
    // -------------------------- //
    // -------------------------- //
    
    @Override
    public void run() 
    {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) ((DefaultTreeModel) tree.getModel()).getRoot();
        root.removeAllChildren();
        
        ((DefaultTreeModel) tree.getModel()).setRoot( 
            create( system ) 
        );
        
        if( cpuAndBuses != null ) 
        {
            cpuAndBusFilled();
        }
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                SwingUtilities.getWindowAncestor( tree ).revalidate();
            }
        });
    }
    
    private void cpuAndBusFilled()
    {
        cpuAndBuses.removeAllItems();
        cpuAndBuses.addItem( "" );
        
        for( Map.Entry<String,List<String>> entry : BusUtils.getProcessors( system ).entrySet() )
        {
            String bus = entry.getKey();
            
            for( String cpu : entry.getValue() )
            {
                cpuAndBuses.addItem( 
                    bus + " & " + cpu 
                );
            }
        }
        
        if( binding.getBus() != null && binding.getCpu() != null )
        {
            cpuAndBuses.setSelectedItem( 
                binding.getBus() + " & " + binding.getCpu() 
            );
        }
        
        if( cpuAndBuses.getSelectedIndex() == -1 )
        {
            cpuAndBuses.setSelectedIndex( 0 );
        }
    }
    
    private DefaultMutableTreeNode create( Component component )
    {
        String subName = BindingUtils.getSubcomponentName( binding.getPartA() );
        String feaName = BindingUtils.getFeatureName( binding.getPartA() );
        
        if( subName == null )
        {
            return new DefaultMutableTreeNode( component );
        }
        
        Subcomponent subcomponent = system.getSubcomponent( subName );
        
        if( subcomponent == null )
        {
            System.out.println( "subcomponent not found!" );
            System.out.println( "subcomponent avaliables: " + system.getSubcomponentsAll().keySet() );
            
            return new DefaultMutableTreeNode( component );
        }
        
        String name = subcomponent.getComponentReferenceName();
        String type = subcomponent.getValueType();
        
        if( feaName != null )
        {
            Component c = subcomponent.getComponent();
            
            if( c == null )
            {
                System.out.println( "component not found!" );
                return new DefaultMutableTreeNode( component );
            }
            
            Feature feature = c.getFeature( feaName );
            name = feature.getComponentReferenceName();
            type = feature.getComponent() != null 
                ? feature.getComponent().getType()
                : "";
            
            if( c.getFeature( feaName ).isBus() && cpuAndBuses != null )
            {
                cpuAndBuses.setEnabled( false );
            }
        }
        else
        {
            if( subcomponent.isBus() && cpuAndBuses != null )
            {
                cpuAndBuses.setEnabled( false );
            }
        }
        
        // ---- //
        
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( component );
        
        for( Subcomponent sub : component.getSubcomponentsAll().values() )
        {
            if( sub.getName().equalsIgnoreCase( subName ) )
            {
                continue ;
            }
            
            DefaultMutableTreeNode n = create( sub , sub.getComponent() , name , type );
            if( n != null )
            {
                node.add( n );
            }
        }
        
        return node;
    }
    
    private DefaultMutableTreeNode create( Subcomponent subcomponent , Component component , String componentName , String type )
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( subcomponent );
        
        if( subcomponent.getValue().endsWith( componentName ) )
        {
            return node;
        }
        
        if( component != null )
        {
            for( Feature feature : component.getFeaturesAll().values() )
            {
                if( isApproved( feature , componentName , type ) )
                {
                    node.add( new DefaultMutableTreeNode( feature ) );
                }
            } 
        }
        
        return node.getChildCount() == 0 && selectBy != SELECT_ALL
            ? null
            : node;
    }
    
    private boolean isApproved( Feature feature , String componentName , String type )
    {
        return selectBy == SELECT_ALL
            || (selectBy == SELECT_TYPE && feature.getComponent() != null && feature.getComponent().getType().equalsIgnoreCase( type ) )
            || (selectBy == SELECT_COMPATIBLE && feature.getValue().endsWith( componentName ) );
    }
    
}