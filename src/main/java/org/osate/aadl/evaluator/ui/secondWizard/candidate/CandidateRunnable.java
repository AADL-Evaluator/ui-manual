package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXTree;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.evolution.FuncionalityUtils;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.project.Feature;
import org.osate.aadl.evaluator.project.Property;
import org.osate.aadl.evaluator.ui.JTreeUtils;

public class CandidateRunnable implements Runnable
{
    public final static int BY_AUTO = -1;
    public final static int BY_FUNCIONALITY = 0;
    public final static int BY_FEATURE = 1;
    public final static int BY_TYPE = 2;
    
    private final CandidateRebuild rebuild;
    private final JXTree tree;
    private final int selectBy;
    private JXHeader header;
    
    public CandidateRunnable( JXTree tree , int selectBy )
    {
        this.tree = tree;
        this.rebuild  = new CandidateRebuild();
        this.selectBy = selectBy;
    }

    public CandidateRunnable setHeader( JXHeader header )
    {
        this.header = header;
        return this;
    }
    
    public CandidateRunnable setEvolution( Evolution evolution )
    {
        this.rebuild.setEvolution( evolution );
        
        return this;
    }
    
    public CandidateRunnable setCandidate( Candidate candidate )
    {
        this.rebuild.setCandidate( candidate );
        return this;
    }

    // ----------------- //
    // ----------------- //
    // ----------------- //
    
    @Override
    public void run() 
    {
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        root.removeAllChildren();
        
        DefaultMutableTreeNode selected = rebuild( root );
        
        treeModel.setRoot( root );
        JTreeUtils.setTreeExpandedState( tree , true );
        
        if( selected != null )
        {
            tree.getSelectionModel().setSelectionPath( 
                new TreePath( selected.getPath() )
            );
        }
    }
    
    private DefaultMutableTreeNode rebuild( DefaultMutableTreeNode root )
    {
        Map<String,List<Property>> funcionalities = FuncionalityUtils.list( rebuild.getEvolution() );
        List<Feature> features = getFeatureAlls();
        
        switch( selectBy )
        {
            case BY_AUTO : return selectByAuto( root , funcionalities.keySet() , features );
            case BY_FUNCIONALITY : return selectByFuncionalities( root , funcionalities.keySet() );
            case BY_FEATURE : return selectByFeatures( root , features );
            default : return selectByType( root );
        }
    }
    
    private DefaultMutableTreeNode selectByAuto( DefaultMutableTreeNode root , Collection<String> f1 , Collection<Feature> f2 )
    {
        Map<String,List<Property>> funcionalities = FuncionalityUtils.list( rebuild.getEvolution() );
        List<Feature> features = getFeatureAlls();
        
        if( !funcionalities.isEmpty() )
        {
            return selectByFuncionalities( root , f1 );
        }
        else if( !features.isEmpty() )
        {
            return selectByFeatures( root , f2 );
        }
        else
        {
            return selectByType( root );
        }
    }
            
    private DefaultMutableTreeNode selectByFuncionalities( DefaultMutableTreeNode root , Collection<String> funcionalities )
    {
        String name = "";
        for( String f : funcionalities )
        {
            name += name.isEmpty() ? "" : " or ";
            name += f;
        }

        header.setDescription( 
            "All candidades selected by funcionality " + name +
            ". Please, select one candidate and this funcionality." 
        );

        return rebuild.rebuild( 
            root , 
            new CondictionFuncionality( funcionalities ) 
        );
    }
    
    private DefaultMutableTreeNode selectByFeatures( DefaultMutableTreeNode root , Collection<Feature> features )
    {
        String name = "";
        for( Feature f : features )
        {
            name += name.isEmpty() ? "" : ", ";
            name += f.getName();
        }

        header.setDescription( 
            "All candidades selected by features " + name +
            ". Please, select one candidate." 
        );

        return rebuild.rebuild( 
            root , 
            new CondictionFeature( features ) 
        );
    }
    
    private DefaultMutableTreeNode selectByType( DefaultMutableTreeNode root )
    {
        String name = rebuild.getEvolution()
            .getDeclarations()
            .get( 0 )
            .getComponent()
            .getType();;
        
        header.setDescription( 
            "All candidades selected by type " + name +
            ". Please, select one candidate." 
        );
        
        return rebuild.rebuild( 
            root , 
            new CondictionType() 
        );
    }
    
    private List<Feature> getFeatureAlls()
    {
        List<Feature> features = new LinkedList<>();
        
        for( Declaration declaration : rebuild.getEvolution().getDeclarations() )
        {
            Component c = declaration.getComponent();
            
            if( c == null )
            {
                continue ;
            }
            
            features.addAll( c.getFeaturesAll().values() );
        }
        
        return features;
    }
    
    
}
