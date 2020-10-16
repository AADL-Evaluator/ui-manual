package org.osate.aadl.evaluator.ui.secondWizard;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jdesktop.swingx.JXTree;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Project;

public class ComponentSelectJDialog extends javax.swing.JDialog 
{
    private final Executor executor;
    
    private Project project;
    private JXTree tree;
    private boolean saved;
    
    public ComponentSelectJDialog( java.awt.Window parent )
    {
        super( parent );
        
        executor = Executors.newSingleThreadExecutor();
        
        initComponents();
        init();
        
        setTitle( "Select a component" );
        setSize( 500 , 600 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        jScrollPane1.setViewportView( 
            tree = new JXTree( new DefaultMutableTreeNode( "Project" ) ) 
        );
        
        typeJComboBox.removeAllItems();
        typeJComboBox.addItem( "All Types" );
        typeJComboBox.addItem( Component.TYPE_ABSTRACT );
        typeJComboBox.addItem( Component.TYPE_BUS );
        typeJComboBox.addItem( Component.TYPE_DATA );
        typeJComboBox.addItem( Component.TYPE_DEVICE );
        typeJComboBox.addItem( Component.TYPE_MEMORY );
        typeJComboBox.addItem( Component.TYPE_PROCESS );
        typeJComboBox.addItem( Component.TYPE_PROCESSOR );
        typeJComboBox.addItem( Component.TYPE_SYSTEM );
        typeJComboBox.addItem( Component.TYPE_THREAD );
        typeJComboBox.setSelectedIndex( 0 );
        
        // Fiz isso apenas para ganhar espaço
        typeJComboBox.addItemListener( new ItemListener() {
            @Override public void itemStateChanged(ItemEvent e) { rebuild(); }
        });
        
        // Fiz isso apenas para ganhar espaço
        nameJTextField.getDocument().addDocumentListener( new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { rebuild(); }
            @Override public void removeUpdate(DocumentEvent e) { rebuild(); }
            @Override public void changedUpdate(DocumentEvent e) { rebuild(); }
        });
    }

    public void setProject( Project project )
    {
        this.project = project;
    }
    
    public void rebuild()
    {
        executor.execute( new ComponentSelectRunnable( this ) );
    }

    public boolean isSaved()
    {
        return saved;
    }
    
    public Component getComponentSelectd() throws Exception
    {
        if( tree.getSelectionPath() == null
            || tree.getSelectionPath().getPathCount() < 3 )
        {
            throw new Exception( "Please, select a system." );
        }
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
            .getSelectionPath()
            .getPathComponent( 2 );
        
        return (Component) node.getUserObject();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        selectJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        typeJComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        nameJTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();

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

        jToolBar2.setFloatable(false);

        jLabel1.setText("Type: ");
        jToolBar2.add(jLabel1);

        jToolBar2.add(typeJComboBox);

        jLabel2.setText("  Name: ");
        jToolBar2.add(jLabel2);

        nameJTextField.setMaximumSize(new java.awt.Dimension(200, 25));
        nameJTextField.setMinimumSize(new java.awt.Dimension(200, 25));
        nameJTextField.setPreferredSize(new java.awt.Dimension(200, 25));
        jToolBar2.add(nameJTextField);

        jPanel1.add(jToolBar2, java.awt.BorderLayout.PAGE_START);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

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
    
    public Project getProject()
    {
        return project;
    }

    public JXTree getTree()
    {
        return tree;
    }

    public JComboBox<String> getTypeJComboBox()
    {
        return typeJComboBox;
    }

    public JTextField getNameJTextField()
    {
        return nameJTextField;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JButton selectJButton;
    private javax.swing.JComboBox<String> typeJComboBox;
    // End of variables declaration//GEN-END:variables
}
