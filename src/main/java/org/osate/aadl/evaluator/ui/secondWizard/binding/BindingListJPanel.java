package org.osate.aadl.evaluator.ui.secondWizard.binding;

import fluent.gui.impl.swing.FluentTable;
import fluent.gui.table.CustomTableColumn;
import fluent.gui.table.FieldTableColumn;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import org.osate.aadl.evaluator.evolution.Binding;
import org.osate.aadl.evaluator.evolution.BindingUtils;
import static org.osate.aadl.evaluator.evolution.BindingUtils.TYPE_COMPATIBLE;
import static org.osate.aadl.evaluator.evolution.BindingUtils.TYPE_COMPATIBLE_WITH_WRAPPER;
import static org.osate.aadl.evaluator.evolution.BindingUtils.TYPE_INCOMPATIBLE;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.project.Component;

public class BindingListJPanel extends javax.swing.JPanel 
{
    private final Executor executor;
    
    private Evolution evolution;
    private Component systemCopy;
    
    private FluentTable<Binding> table;
    
    public BindingListJPanel() 
    {
        executor = Executors.newSingleThreadExecutor();
        
        initComponents();
        init();
    }
    
    private void init()
    {
        createTable();
        
        // --- //
        
        table.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if( e.getClickCount() >= 2 ){
                    editJButton.doClick();
                }
            }
        });
        
        table.addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if( e.getKeyCode() == KeyEvent.VK_ENTER ){
                    editJButton.doClick();
                }
                else if( e.getKeyCode() == KeyEvent.VK_DELETE ){
                    statusJButton.doClick();
                }
            }
        } );
        
        // --- //
        
        detailsJButton.addActionListener( new BindingDetailsActionListener( this ) );
        editJButton.addActionListener( new BindingEditActionListener( this ) );
        statusJButton.addActionListener( new BindingDeleteActionListener( table ) );
    }
    
    private void createTable()
    {
        jScrollPane1.setViewportView( 
            table = new FluentTable<>( "bindings" ) 
        );
        
        table.addColumn( new CustomTableColumn<Binding,String>( "Connection" ){
            @Override
            public String getValue( Binding binding ) {
                return binding.getConnection() == null 
                    ? "" 
                    : binding.getConnection().getName();
            }
        });
        
        table.addColumn( new FieldTableColumn( "Candidate" , "partA" ) );
        
        table.addColumn( new CustomTableColumn<Binding,String>( "Software/Hardware" ){
            @Override
            public String getValue( int index , Binding binding ) {
                try
                {
                    if( BindingUtils.isAllPartsWereSetted( binding ) )
                    {
                        switch( BindingUtils.isCompatible( systemCopy , binding ) )
                        {
                            case TYPE_COMPATIBLE : return binding.getPartB();
                            case TYPE_INCOMPATIBLE : return binding.getPartB() + " (incompatible)";
                            case TYPE_COMPATIBLE_WITH_WRAPPER : return binding.getPartB() + "(using wrapper)";
                            default: return "unknwon";
                        }
                    }
                    else
                    {
                        return binding.getPartB();
                    }
                }
                catch( Exception err )
                {
                    return binding.getPartB() + " (Error to valid it)";
                }
            }
            
        });
        
        table.addColumn( new CustomTableColumn<Binding,String>( "Directional" ){
            @Override
            public String getValue( Binding binding ) {
                return binding.isBidirect() ? "Bidirect" : "Unidirect";
            }
        });
        
        table.addColumn( new FieldTableColumn( "Bus" , "bus" ) );
        table.addColumn( new FieldTableColumn( "CPU" , "cpu" ) );
        table.setUp();
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                table.setTabelaVaziaMensagem( "No connection was found and created." );
                table.setTabelaVazia();
            }
        });
    }
    
    public void setEvolution( final Evolution evolution )
    {
        this.evolution  = evolution;
        this.systemCopy = evolution.getSystem().clone();
        
        executor.execute( new BindingRunnable( this ){
            @Override
            public Component getSystemCopy() {
                return systemCopy;
            }

            @Override
            public Evolution getEvolution() {
                return evolution;
            }
            
            @Override
            public void setData( List<Binding> bindings ){
                getTable().setData( bindings );
            }

            @Override
            public void addData( Relationship relationship ){
                getTable().addData( relationship.getBinding() );
            }
        });
    }

    public Evolution getEvolution()
    {
        return evolution;
    }

    public FluentTable<Binding> getTable() 
    {
        return table;
    }

    public Component getSystemCopy()
    {
        return systemCopy;
    }

    public Component getSystemWithConnections()
    {
        Component system = systemCopy.clone();
        
        
        
        return systemCopy;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        detailsJButton = new javax.swing.JButton();
        editJButton = new javax.swing.JButton();
        statusJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();

        jToolBar1.setFloatable(false);

        jLabel1.setText("Connections");
        jToolBar1.add(jLabel1);
        jToolBar1.add(filler1);

        detailsJButton.setText("Details");
        detailsJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        detailsJButton.setFocusable(false);
        detailsJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        detailsJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(detailsJButton);

        editJButton.setText("Edit");
        editJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editJButton.setFocusable(false);
        editJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(editJButton);

        statusJButton.setText("Delete");
        statusJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        statusJButton.setFocusable(false);
        statusJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        statusJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(statusJButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton detailsJButton;
    private javax.swing.JButton editJButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton statusJButton;
    // End of variables declaration//GEN-END:variables
}
