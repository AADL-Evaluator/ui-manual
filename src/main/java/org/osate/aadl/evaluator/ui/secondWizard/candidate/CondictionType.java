package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.project.Component;

public class CondictionType extends Condiction<Object>
{
    private String type;

    public CondictionType() 
    {
        super( null );
    }

    @Override
    public void setEvolution( Evolution evolution )
    {
        super.setEvolution( evolution );
        
        type = evolution.getDeclarations()
            .get( 0 )
            .getComponent()
            .getType();
    }
    
    @Override
    public boolean isAcceptable( Component component )
    {
        return type != null 
            && type.equalsIgnoreCase( component.getType() );
    }
    
}
