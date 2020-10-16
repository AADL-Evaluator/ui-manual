package org.osate.aadl.evaluator.ui.secondWizard;

import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.ui.mainWizard.AadlUIAction;
import org.osate.aadl.evaluator.ui.p3.EvolutionListJPanel;

public class ManualAadlUIAction extends AadlUIAction
{

    public ManualAadlUIAction()
    {
        super( "Changing a component to other" );
    }
    
    @Override
    public List<Evolution> execute( EvolutionListJPanel panel ) throws Exception
    {
        Evolution evolution = new Evolution( panel.getSystemOriginal() );
        
        final EvolutionWizardJDialog dialog = new EvolutionWizardJDialog(
            SwingUtilities.getWindowAncestor( panel )
        );
        
        dialog.setEvolution( evolution );
        dialog.setVisible( true );
        dialog.dispose();
        
        if( !dialog.isSaved() )
        {
            throw new Exception( "You canceled the operation." );
        }
        
        return Arrays.asList( dialog.getEvolution() );
    }
    
}
