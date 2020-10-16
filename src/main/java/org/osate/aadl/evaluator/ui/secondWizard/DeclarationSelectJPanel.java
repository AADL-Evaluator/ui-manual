package org.osate.aadl.evaluator.ui.secondWizard;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.jdesktop.swingx.JXTree;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.project.Subcomponent;
import org.osate.aadl.evaluator.ui.ComponentCodeJDialog;

public abstract class DeclarationSelectJPanel extends javax.swing.JPanel 
{
    private final Executor executor;   
    private JXTree tree;
    
    public DeclarationSelectJPanel()
    {
        executor = Executors.newSingleThreadExecutor();
        
        initComponents();
        init();
    }
    
    private void init()
    {
        jScrollPane1.setViewportView(
            tree = new JXTree(
                new DefaultMutableTreeNode( "Component" , true )
            )
        );
        
        tree.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if( e.getClickCount() >= 3 ){
                    try
                    {
                        setSelect( getSelected() );
                    }
                    catch( Exception err )
                    {
                        JOptionPane.showMessageDialog( 
                            getRootPane() , 
                            err.getMessage() ,
                            "Error" ,
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
        
        tree.getSelectionModel().setSelectionMode( 
            TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION
        );
    }
    
    public void setSystem( final Component system )
    {
        executor.execute( new DeclarationRebuildRunnable( tree , system ) );
    }
    
    public List<Declaration> getSelected() throws Exception
    {
        if( tree.getSelectionPath() == null
            || tree.getSelectionPath().getPathCount() < 2 )
        {
            throw new Exception( "Please, select a declaration." );
        }
        
        List<Declaration> subcomponents = new LinkedList<>();
        
        for( TreePath path : tree.getSelectionPaths() )
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
                .getLastPathComponent();
            
            if( !(node.getUserObject() instanceof Subcomponent) )
            {
                throw new Exception( "Please, select a subcomponent." );
            }
            
            Subcomponent sub = (Subcomponent) node.getUserObject();
            
            if( !subcomponents.isEmpty() 
                && subcomponents.get( 0 ).getParent() != sub.getParent() )
            {
                throw new Exception( "You should select two or more subcomponents of the same component." );
            }
            
            subcomponents.add( sub );
        }
        
        return subcomponents;
    }
    
    public abstract void setSelect( List<Declaration> declarations );
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        viewJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();

        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);

        jLabel1.setText("Systems");
        jToolBar1.add(jLabel1);
        jToolBar1.add(filler1);

        viewJButton.setText("View");
        viewJButton.setFocusable(false);
        viewJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        viewJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        viewJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewJButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(viewJButton);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.PAGE_START);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void viewJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewJButtonActionPerformed
        if( tree.getSelectionPath() == null
            || tree.getSelectionPath().getPathCount() < 3 )
        {
            return ;
        }
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getPathComponent( 2 );
        Component component;
        
        if( node.getUserObject() instanceof Subcomponent )
        {
            component = ((Subcomponent) node.getUserObject()).getComponent();
        }
        else
        {
            node = (DefaultMutableTreeNode) tree.getSelectionPath().getPathComponent( 2 );
            component = (Component) node.getUserObject();
        }
        
        final ComponentCodeJDialog dialog = new ComponentCodeJDialog(
            SwingUtilities.getWindowAncestor( this )
        );
        dialog.setComponent( component );
        dialog.setVisible( true );
        dialog.dispose();
    }//GEN-LAST:event_viewJButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton viewJButton;
    // End of variables declaration//GEN-END:variables
}
