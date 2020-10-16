package org.osate.aadl.evaluator.ui.secondWizard.binding;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import org.jdesktop.swingx.JXHeader;
import org.osate.aadl.evaluator.ui.secondWizard.binding.BindingRunnable.Change;

public class BindingConflictJDialog extends javax.swing.JDialog 
{
    private DefaultListModel<String> listModel;
    private List<BindingRunnable.Change> changes;
    
    public BindingConflictJDialog( java.awt.Window parent )
    {
        super( parent );
        
        initComponents();
        init();
        
        setTitle( "Select one option" );
        setSize( 400 , 300 );
        setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        add( new JXHeader( 
            "Select one option" ,
            "We detect two or more options to replace a exist connection. Please," +
            " you must select one option to continue. Of course, you can change" +
            " you decision later."
        ) , BorderLayout.NORTH );
        
        connectionJList.requestFocusInWindow();
        connectionJList.setModel( 
            listModel = new DefaultListModel<>() 
        );
        
        connectionJList.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if( e.getClickCount() >= 2 ){
                    selectJButtonActionPerformed( null );
                }
            }
        } );
        
        connectionJList.addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if( e.getKeyCode() == KeyEvent.VK_ENTER ){
                    selectJButtonActionPerformed( null );
                }
            }
        } );
    }

    public void setChanges( List<BindingRunnable.Change> changes )
    {
        this.changes = changes;
        
        listModel.removeAllElements();
        
        for( BindingRunnable.Change change : changes )
        {
            listModel.addElement( change.getConnection().toString() );
        }
        
        if( listModel.size() > 0 )
        {
            connectionJList.setSelectedIndex( 0 );
        }
    }
    
    public List<Change> getChangesSelected()
    {
        List<Change> selected = new LinkedList<>();
        
        for( int index : connectionJList.getSelectedIndices() )
        {
            selected.add( changes.get( index ) );
        }
        
        return selected;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        selectJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        connectionJList = new javax.swing.JList<>();

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

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_END);

        jScrollPane1.setViewportView(connectionJList);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectJButtonActionPerformed
        setVisible( false );
    }//GEN-LAST:event_selectJButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> connectionJList;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton selectJButton;
    // End of variables declaration//GEN-END:variables
}
