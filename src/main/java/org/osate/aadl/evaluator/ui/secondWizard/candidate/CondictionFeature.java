package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.util.Collection;
import java.util.List;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.project.Feature;

public class CondictionFeature extends Condiction<Feature>
{

    public CondictionFeature( Collection<Feature> features ) 
    {
        super( features );
    }
    
    @Override
    public boolean isAcceptable( Component component )
    {
        return hasThisFeatures( component , evolution.getDeclarations() );
    }
    
    private boolean hasThisFeatures( Component c , List<Declaration> declarations )
    {
        Collection<String> featureNames = c.getFeaturesAll().keySet();
        
        for( Feature f : cache )
        {
            if( !declarations.get( 0 ).getComponent().getType().equals( c.getType() ) 
                || !featureNames.contains( f.getName() ) )
            {
                return false;
            }
        }
        
        return true;
    }
    
}
