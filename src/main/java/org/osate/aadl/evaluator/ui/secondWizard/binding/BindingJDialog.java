package org.osate.aadl.evaluator.ui.secondWizard.binding;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXTree;
import org.osate.aadl.evaluator.evolution.Binding;
import org.osate.aadl.evaluator.evolution.BindingUtils;
import org.osate.aadl.evaluator.evolution.BusUtils;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Feature;
import org.osate.aadl.evaluator.project.Subcomponent;

public class BindingJDialog extends javax.swing.JDialog 
{
    private final Executor executor;
    
    private Evolution evolution;
    private Component system;
    
    private JXTree tree;
    private Binding binding;
    private boolean saved;
    
    public BindingJDialog( java.awt.Window parent )
    {
        super( parent );
        
        executor = Executors.newSingleThreadExecutor();
        
        initComponents();
        init();
        
        setTitle( "Binding" );
        setSize( 600 , 400 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        add( new JXHeader( 
            "Binding" , 
            "Please, create or edit a binding inside a system." ) 
        , BorderLayout.NORTH );
        
        // TODO: mostrar os subcomponents e as features.
        // TODO: deve selecionar uma feature.
        jScrollPane3.setViewportView( 
            tree = new JXTree( new DefaultMutableTreeNode( "Subcomponents" ) 
        ) );
        
        selectByJComboBox.setSelectedIndex( 0 );
        selectByJComboBox.addItemListener( new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if( e.getStateChange() != ItemEvent.SELECTED ){
                    return ;
                }
                
                rebuild();
            }
        });
        
        addJButton.addActionListener( new ComponentAddActionListener( this ) );
        editJButton.addActionListener( new ComponentEditActionListener( this ) );
        deleteJButton.addActionListener( new ComponentDeleteActionListener( this ) );
    }

    public void setEvolution( Evolution evolution )
    {
        this.evolution = evolution;
    }
    
    public void setSystem( Component system )
    {
        this.system = system;
    }
    
    public void setBinding( Binding binding )
    {
        this.binding = binding;
        
        connectionJTextField.setText( 
            binding.getConnection() == null 
                ? "* new *"
                : binding.getConnection().getName()
        );
        
        direcionalJComboBox.setSelectedIndex( binding.isBidirect() ? 0 : 1 );
    }
    
    public void rebuild()
    {
        this.executor.execute( new RebuildRunnable()
            .setBinding( binding )
            .setCpuAndBuses( cpuBusJComboBox )
            .setSystem( system )
            .setTree( tree )
            .setSelectBy( selectByJComboBox.getSelectedIndex() )
        );
    }
    
    public Binding getBinding()
    {
        if( binding == null )
        {
            binding = new Binding();
        }
        
        String bus = null;
        String cpu = null;
        
        if( !cpuBusJComboBox.isEnabled() )
        {
            String busName = getSubcomponentSelected().getName();
            
            cpu = BusUtils.toString( 
                BusUtils.getProcessorsByBus( system , busName ) 
            );
        }
        else if( cpuBusJComboBox.getSelectedIndex() == -1 )
        {
            cpuBusJComboBox.setSelectedIndex( 0 );
        }
        else if( cpuBusJComboBox.getSelectedIndex() > 0 )
        {
            String str[] = cpuBusJComboBox.getSelectedItem().toString().split(
                Pattern.quote( "&" )
            );
            
            bus = str[0].trim();
            cpu = str[1].trim();
        }
        
        binding.setBidirect( direcionalJComboBox.getSelectedIndex() == 0 );
        binding.setBus( bus );
        binding.setCpu( cpu );
        binding.setPartB( getPartSelected() );
        
        // TODO: if user want to change PartA?
        
        return binding;
    }
    
    public boolean isSaved()
    {
        return saved;
    }

    public Component getSystem() 
    {
        return system;
    }

    public Evolution getEvolution()
    {
        return evolution;
    }
    
    public Subcomponent getSubcomponentSelected()
    {
        if( tree.getSelectionPath() == null )
        {
            return null;
        }
        
        DefaultMutableTreeNode last = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        
        if( last.getUserObject() instanceof Feature )
        {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) last.getParent();
            
            return ((Subcomponent) parent.getUserObject());
        }
        else if( last.getUserObject() instanceof Subcomponent )
        {
            return ((Subcomponent) last.getUserObject());
        }
        else 
        {
            return null;
        }
    }
    
    private String getPartSelected()
    {
        if( tree.getSelectionPath() == null )
        {
            return null;
        }
        
        DefaultMutableTreeNode last = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        
        if( last.getUserObject() instanceof Feature )
        {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) last.getParent();
            
            return ((Subcomponent) parent.getUserObject()).getName() 
                + "." + ((Feature) last.getUserObject()).getName();
        }
        else if( last.getUserObject() instanceof Subcomponent )
        {
            return ((Subcomponent) last.getUserObject()).getName();
        }
        else 
        {
            return null;
        }
    }
    
    private boolean isCompatible()
    {
        try
        {
            int compatible = BindingUtils.isCompatible( 
                  system 
                , binding.getPartA()
                , getPartSelected() );
            
            if( compatible == BindingUtils.TYPE_COMPATIBLE
                || compatible == BindingUtils.TYPE_COMPATIBLE_WITH_WRAPPER )
            {
                return true;
            }
            
            int r = JOptionPane.showConfirmDialog( 
                this , 
                "The component selected is incompatible. Do you want continue?" 
            );

            return r == JOptionPane.YES_OPTION;
        }
        catch( Exception err )
        {
            err.printStackTrace();
            
            JOptionPane.showMessageDialog( 
                this , 
                err.getMessage() , 
                "Error" , 
                JOptionPane.ERROR_MESSAGE 
            );
            
            return false;
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        saveJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel6 = new javax.swing.JLabel();
        connectionJTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        direcionalJComboBox = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        cpuBusJComboBox = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        selectByJComboBox = new javax.swing.JComboBox<>();
        addJButton = new javax.swing.JButton();
        editJButton = new javax.swing.JButton();
        deleteJButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToolBar1.setFloatable(false);
        jToolBar1.add(filler1);

        saveJButton.setText("Save");
        saveJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saveJButton.setFocusable(false);
        saveJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveJButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(saveJButton);

        cancelJButton.setText("Cancel");
        cancelJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancelJButton.setFocusable(false);
        cancelJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cancelJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(cancelJButton);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_END);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar2.setFloatable(false);

        jLabel6.setText("Connection: ");
        jToolBar2.add(jLabel6);

        connectionJTextField.setEditable(false);
        connectionJTextField.setMaximumSize(new java.awt.Dimension(100, 25));
        connectionJTextField.setMinimumSize(new java.awt.Dimension(100, 25));
        connectionJTextField.setPreferredSize(new java.awt.Dimension(100, 25));
        jToolBar2.add(connectionJTextField);

        jLabel3.setText("  Directional: ");
        jToolBar2.add(jLabel3);

        direcionalJComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "bidirect", "unidirect" }));
        jToolBar2.add(direcionalJComboBox);

        jLabel1.setText("  CPU & Bus: ");
        jToolBar2.add(jLabel1);

        jToolBar2.add(cpuBusJComboBox);

        jPanel1.add(jToolBar2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jToolBar4.setFloatable(false);

        jLabel2.setText("Subcomponents");
        jToolBar4.add(jLabel2);
        jToolBar4.add(filler3);

        selectByJComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Compatible", "Same type", "All" }));
        jToolBar4.add(selectByJComboBox);

        addJButton.setText("add");
        addJButton.setFocusable(false);
        addJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar4.add(addJButton);

        editJButton.setText("edit");
        editJButton.setFocusable(false);
        editJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar4.add(editJButton);

        deleteJButton.setText("delete");
        deleteJButton.setFocusable(false);
        deleteJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar4.add(deleteJButton);

        jPanel3.add(jToolBar4, java.awt.BorderLayout.PAGE_START);
        jPanel3.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveJButtonActionPerformed
        if( isCompatible() )
        {
            saved = true;
            setVisible( false );
        }
    }//GEN-LAST:event_saveJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        saved = false;
        setVisible( false );
    }//GEN-LAST:event_cancelJButtonActionPerformed

    public JXTree getTree() 
    {
        return tree;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJButton;
    private javax.swing.JButton cancelJButton;
    private javax.swing.JTextField connectionJTextField;
    private javax.swing.JComboBox<String> cpuBusJComboBox;
    private javax.swing.JButton deleteJButton;
    private javax.swing.JComboBox<String> direcionalJComboBox;
    private javax.swing.JButton editJButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JButton saveJButton;
    private javax.swing.JComboBox<String> selectByJComboBox;
    // End of variables declaration//GEN-END:variables
}
