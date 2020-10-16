package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXTree;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Property;

public class CandidateJDialog extends javax.swing.JDialog 
{
    private final Executor executor;
    
    private Evolution evolution;
    private Candidate candidate;
    
    private JXHeader header;
    private JXTree tree;
    private boolean saved;
    private final Map<String,List<Property>> funcionalities;
    
    public CandidateJDialog( java.awt.Window parent )
    {
        super( parent );
        
        this.executor = Executors.newSingleThreadExecutor();
        this.funcionalities = new LinkedHashMap<>();
        
        initComponents();
        init();
        
        setTitle( "Candidate Edit" );
        setSize( 700 , 500 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        add( 
            header = new JXHeader( 
                "Candidate" , 
                "Please, select a candidate and all funcionalities related to it." 
            ) , 
            BorderLayout.NORTH 
        );
        
        selectByJComboBox.addItemListener( new ItemListener() {
            @Override
            public void itemStateChanged( ItemEvent e ) {
                if( e.getStateChange() != ItemEvent.SELECTED ){
                    return ;
                }
                
                int selectBy = selectByJComboBox.getSelectedIndex() - 1;
                
                executor.execute( new CandidateRunnable( tree , selectBy )
                    .setHeader( header )
                    .setEvolution( evolution )
                    .setCandidate( candidate ) 
                );
            }
        } );
        
        jScrollPane2.setViewportView( 
            tree = new JXTree( new DefaultMutableTreeNode( "Candidates" ) ) 
        );
        
        tree.getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                executor.execute( new FuncionalityRunnable( tree , funcionalityJList , funcionalities )
                    .setCandidate( candidate )
                    .setEvolution( evolution )
                );
            }
        } );
        
        funcionalityJList.setModel( new DefaultListModel<>() );
        
        viewJButton.addActionListener( new ComponentViewActionListener( tree ) );
        duplicateJButton.addActionListener( new DuplicateActionListener( tree ) );
        deleteJButton.addActionListener( new ComponentDeleteActionListener( tree ) );
        
        editJButton.addActionListener( new ComponentEditActionListener( tree ){
            @Override
            public Evolution getEvolution() {
                return evolution;
            }
        });
    }

    public void setEvolution( Evolution evolution )
    {
        this.evolution = evolution;
    }
    
    public void setCandidate( Candidate candidate )
    {
        this.candidate = candidate;
        
        executor.execute( new CandidateRunnable( tree , CandidateRunnable.BY_AUTO )
            .setHeader( header )
            .setEvolution( evolution )
            .setCandidate( candidate ) 
        );
        
        executor.execute( new FuncionalityRunnable( tree , funcionalityJList , funcionalities )
            .setCandidate( candidate )
            .setEvolution( evolution )
        );
    }
    
    public Candidate getCandidate()
    {
        candidate.setComponent( getComponentSelected() );
        
        candidate.getFuncionalities().clear();
        
        for( String selected : funcionalityJList.getSelectedValuesList() )
        {
            candidate.getFuncionalities().put( 
                selected , 
                funcionalities.get( selected )
            );
        }
        
        return candidate;
    }

    private Component getComponentSelected()
    {
        if( tree.getSelectionPath() == null )
        {
            return null;
        }
        
        TreePath path = tree.getSelectionPath();
        
        while( true )
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            
            if( node.getUserObject() instanceof Component )
            {
                return (Component) node.getUserObject();
            }
            else if( node.getParent() == null ) 
            {
                return null;
            }
            else
            {
                path = path.getParentPath();
            }
        }
    }
    
    public boolean isSaved() 
    {
        return saved;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        saveJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        selectByJComboBox = new javax.swing.JComboBox<>();
        viewJButton = new javax.swing.JButton();
        editJButton = new javax.swing.JButton();
        duplicateJButton = new javax.swing.JButton();
        deleteJButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        nothingJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        funcionalityJList = new javax.swing.JList<>();

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

        jSplitPane1.setDividerLocation(500);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(jPanel2);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jToolBar2.setFloatable(false);

        jLabel1.setText("Candidates");
        jToolBar2.add(jLabel1);
        jToolBar2.add(filler2);

        selectByJComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Auto", "Funcionality", "Feature", "Type" }));
        jToolBar2.add(selectByJComboBox);

        viewJButton.setText("View");
        viewJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        viewJButton.setFocusable(false);
        viewJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        viewJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(viewJButton);

        editJButton.setText("Edit");
        editJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editJButton.setFocusable(false);
        editJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(editJButton);

        duplicateJButton.setText("Duplicate");
        duplicateJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        duplicateJButton.setFocusable(false);
        duplicateJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        duplicateJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(duplicateJButton);

        deleteJButton.setText("Delete");
        deleteJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteJButton.setFocusable(false);
        deleteJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(deleteJButton);

        jPanel3.add(jToolBar2, java.awt.BorderLayout.PAGE_START);
        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel3);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jToolBar3.setFloatable(false);

        jLabel2.setText("Funcionalities:");
        jToolBar3.add(jLabel2);
        jToolBar3.add(filler3);

        nothingJButton.setText("Nothing");
        nothingJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nothingJButton.setFocusable(false);
        nothingJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nothingJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(nothingJButton);

        jPanel4.add(jToolBar3, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setViewportView(funcionalityJList);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveJButtonActionPerformed
        try
        {
            if( funcionalityJList.getModel().getSize() > 0 
                && funcionalityJList.getSelectedIndex() == -1 )
            {
                throw new Exception( "Please, select at least one funcionality." );
            }
            
            if( getComponentSelected() == null )
            {
                throw new Exception( "Please, select a component." );
            }

            saved = true;
            setVisible( false );
        }
        catch( Exception err )
        {
            JOptionPane.showMessageDialog( 
                rootPane , 
                err.getMessage() ,
                "Error" ,
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_saveJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        saved = false;
        setVisible( false );
    }//GEN-LAST:event_cancelJButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.JButton deleteJButton;
    private javax.swing.JButton duplicateJButton;
    private javax.swing.JButton editJButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JList<String> funcionalityJList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JButton nothingJButton;
    private javax.swing.JButton saveJButton;
    private javax.swing.JComboBox<String> selectByJComboBox;
    private javax.swing.JButton viewJButton;
    // End of variables declaration//GEN-END:variables
}
