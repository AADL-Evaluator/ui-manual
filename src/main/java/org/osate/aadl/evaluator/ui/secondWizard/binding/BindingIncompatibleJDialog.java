package org.osate.aadl.evaluator.ui.secondWizard.binding;

import fluent.gui.impl.swing.FluentTable;
import fluent.gui.table.CustomTableColumn;
import fluent.gui.table.FieldTableColumn;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXHeader;
import org.osate.aadl.evaluator.evolution.Binding;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.project.Feature;

public class BindingIncompatibleJDialog extends javax.swing.JDialog 
{
    private FluentTable<Line> table;
    private Binding binding;
    private boolean saved;
    
    public BindingIncompatibleJDialog( java.awt.Window parent )
    {
        super( parent );
        
        initComponents();
        init();
        
        setTitle( "Connection without a compatible" );
        setSize( 600 , 300 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        add( new JXHeader( 
            "Connection without a compatible" ,
            "We can't find a component or feature to replace. Please, "
            + "select or not one of theses options avaliable." 
        ) , BorderLayout.NORTH );
        
        jScrollPane.setViewportView( 
            table = new FluentTable<>( "lines" ) 
        );
        
        table.addColumn( new CustomTableColumn<Line,String>( "Candidate" , 50 ){
            @Override
            public String getValue(int rowIndex, Line line) {
                return line.candidate.getComponent().getName();
            }
        });
        
        table.addColumn( new FieldTableColumn( "Feature"   , "feature" , 100 ) );
        table.setUp();
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                table.setTabelaVaziaMensagem( "No candidate is avaliable." );
                table.setTabelaVazia();
            }
        });
        
        table.requestFocusInWindow();
        
        table.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if( e.getClickCount() >= 2 ){
                    selectJButtonActionPerformed( null );
                }
            }
        } );
        
        table.addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if( e.getKeyCode() == KeyEvent.VK_ENTER ){
                    selectJButtonActionPerformed( null );
                }
            }
        } );
    }

    public void setBinding( Binding binding )
    {
        this.binding = binding;
        
        this.connectionJTextField.setText( 
            binding.getConnection().getName() 
            + " : " 
            + binding.getConnection().getValue() 
        );
    }
    
    public boolean setAvaliable( Map<Candidate,List<Feature>> avaliable ) throws Exception
    {
        final List<Line> lines = new LinkedList<>();
        
        Feature a = binding.getConnection().getFeatureA();
        Feature b = binding.getConnection().getFeatureB();
        
        for( Map.Entry<Candidate,List<Feature>> entry : avaliable.entrySet() )
        {
            for( Feature feature : entry.getValue() )
            {
                if( a != null && a.getFeatureType().equalsIgnoreCase( feature.getFeatureType() ) )
                {
                    lines.add( new Line( entry.getKey() , feature ) );
                }
                else if( b != null && b.getFeatureType().equalsIgnoreCase( feature.getFeatureType() ) )
                {
                    lines.add( new Line( entry.getKey() , feature ) );
                }
            }
        }
        
        if( lines.size() == 1 )
        {
            saved = true;
            
            SwingUtilities.invokeAndWait( new Runnable() {
                @Override
                public void run() {
                    table.setData( lines );
                }
            });
            
            SwingUtilities.invokeAndWait( new Runnable() {
                @Override
                public void run() {
                    table.getSelectionModel().setSelectionInterval( 0 , 0 );
                }
            });
            
            return false;
        }
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                table.setData( lines );
            }
        });
        
        return !lines.isEmpty();
    }
    
    public boolean hasOptions()
    {
        return table.getTabelModel().getRowCount() > 0;
    }
    
    public List<Line> getSelected()
    {
        return table.getSelectedRow() == -1 
            ? new LinkedList<Line>()
            : table.getSelectedObjects();
    }

    public boolean isSaved()
    {
        return saved;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        selectJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        connectionJTextField = new javax.swing.JTextField();
        jScrollPane = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToolBar1.setFloatable(false);
        jToolBar1.add(filler1);

        selectJButton.setText("Select");
        selectJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        selectJButton.setFocusable(false);
        selectJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        selectJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        selectJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectJButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(selectJButton);

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

        connectionJTextField.setEditable(false);
        connectionJTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(connectionJTextField, java.awt.BorderLayout.PAGE_START);
        jPanel1.add(jScrollPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectJButtonActionPerformed
        saved = true;
        setVisible( false );
    }//GEN-LAST:event_selectJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        saved = false;
        setVisible( false );
    }//GEN-LAST:event_cancelJButtonActionPerformed
    
    public class Line {
        Candidate candidate;
        Feature feature;

        public Line(Candidate candidate, Feature feature) {
            this.candidate = candidate;
            this.feature = feature;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.JTextField connectionJTextField;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton selectJButton;
    // End of variables declaration//GEN-END:variables
}
