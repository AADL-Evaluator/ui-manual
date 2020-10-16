package org.osate.aadl.evaluator.ui.secondWizard.binding;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Collection;
import org.jdesktop.swingx.JXHeader;
import org.osate.aadl.evaluator.evolution.Binding;
import org.osate.aadl.evaluator.evolution.BindingUtils;
import static org.osate.aadl.evaluator.evolution.BindingUtils.getFeatureName;
import static org.osate.aadl.evaluator.evolution.BindingUtils.getSubcomponentName;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Subcomponent;

public class BindingDetailsJDialog extends javax.swing.JDialog 
{
    
    public BindingDetailsJDialog( java.awt.Window parent )
    {
        super( parent );
        
        initComponents();
        init();
        
        setTitle( "Binding Details" );
        setSize( 600 , 400 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        add( new JXHeader( 
            "Binding Details" , 
            "This window show a resume of the interfaces & connections selected." 
        ) , BorderLayout.NORTH );
        
        viewJTextArea.setFont( new Font( Font.MONOSPACED , Font.PLAIN , 12 ) );
    }
    
    public void setData( Component system , Collection<Binding> bindings )
    {
        for( Binding binding : bindings )
        {
            if( !viewJTextArea.getText().isEmpty() )
            {
                viewJTextArea.append( "\n\n" );
                viewJTextArea.append( "---" );
                viewJTextArea.append( "\n\n" );
            }
            
            setData( system , binding );
        }
    }
    
    public void setData( Component system , Binding binding )
    {
        try
        {
            if( binding.getConnection() != null )
            {
                viewJTextArea.append( binding.getConnection().toString() );
                viewJTextArea.append( "\n" );
            }
            
            viewJTextArea.append( "Candidate: " + binding.getPartA() );
            viewJTextArea.append( "     (" + getValue(system , binding.getPartA() ) + ")\n" );
            
            viewJTextArea.append( "Soft/hard: " + binding.getPartB() );
            viewJTextArea.append( "     (" + getValue(system , binding.getPartB() ) + ")\n" );
            
            viewJTextArea.append( "Status: " + (BindingUtils.getCompatibleMessage( system , binding ) ) );
        }
        catch( Exception err )
        {
            viewJTextArea.append( "Status: Error to valid it" );
            viewJTextArea.append( " (reason: " + err.getMessage() + ")" );
        }
    }
    
    public String getValue( Component system , String part )
    {
        String subName = getSubcomponentName( part );
        String feaName = getFeatureName( part );
        
        if( subName == null )
        {
            return "The binding link to nobody!";
        }
        
        Subcomponent subcomponent = system.getSubcomponent( subName );
        
        if( subcomponent == null )
        {
            return "subcomponent " + subName + " not found!";
        }
        
        String value = system.getSubcomponent( subName ).getValue();
        
        if( feaName != null )
        {
            Component c = subcomponent.getComponent();
            
            if( c == null )
            {
                return "component " + subcomponent.getComponentReferenceName() + " not found!";
            }
            
            value = c.getFeature( feaName ).getValue();
        }
        
        return value;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        viewJTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        viewJTextArea.setEditable(false);
        viewJTextArea.setColumns(20);
        viewJTextArea.setRows(5);
        jScrollPane1.setViewportView(viewJTextArea);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea viewJTextArea;
    // End of variables declaration//GEN-END:variables
}
