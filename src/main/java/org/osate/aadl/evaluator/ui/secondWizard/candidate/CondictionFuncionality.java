package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.util.Collection;
import static org.osate.aadl.evaluator.evolution.FuncionalityUtils.hasOneThisFuncionalities;
import org.osate.aadl.evaluator.project.Component;

public class CondictionFuncionality extends Condiction<String>
{
    
    public CondictionFuncionality( Collection<String> funcionalities ) 
    {
        super( funcionalities );
    }
    
    @Override
    public boolean isAcceptable( Component component ) 
    {
        // !isTheSame( component , evolution.getDeclarations() ) &&
        return hasOneThisFuncionalities( component , cache ) 
            && component.isImplementation();
    }
    
}
