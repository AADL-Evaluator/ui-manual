package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.util.Collection;
import java.util.List;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;

public abstract class Condiction<T>
{
    protected final Collection<T> cache;
    protected Evolution evolution;

    public Condiction( Collection<T> cache )
    {
        this.cache = cache;
    }

    public void setEvolution( Evolution evolution )
    {
        this.evolution = evolution;
    }
    
    public abstract boolean isAcceptable( Component component );
    
    protected boolean isTheSame( Component c , List<Declaration> declarations )
    {
        for( Declaration d : declarations )
        {
            if( d.getComponent() == c )
            {
                return true;
            }
        }
        
        return false;
    }
    
}
