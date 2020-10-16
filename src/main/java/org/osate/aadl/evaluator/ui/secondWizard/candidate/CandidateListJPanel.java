package org.osate.aadl.evaluator.ui.secondWizard.candidate;

import fluent.gui.impl.swing.FluentTable;
import fluent.gui.table.CustomTableColumn;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.osate.aadl.evaluator.evolution.BusUtils;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.evolution.FuncionalityUtils;

public class CandidateListJPanel extends javax.swing.JPanel 
{
    private Evolution evolution;
    private FluentTable<Candidate> table;
    
    public CandidateListJPanel()
    {
        initComponents();
        init();
    }
    
    private void init()
    {
        createTable();
        
        addJButton.addActionListener( new CandidateAddActionListener( this ) );
        editJButton.addActionListener( new CandidateEditActionListener( this ) );
        viewJButton.addActionListener( new CandidateViewActionListener( table ) );
        deleteJButton.addActionListener( new CandidateDeleteActionListener( table ) );
    }
    
    private void createTable()
    {
        candidateJScrollPane.setViewportView(
            table = new FluentTable<>( "candidates" )
        );
        
        table.addColumn( new CustomTableColumn<Candidate,String>( "Component" ){
            @Override
            public String getValue( Candidate candidate ){
                return candidate.getComponent().getName();
            }
        });
        
        table.addColumn( new CustomTableColumn<Candidate,String>( "Funcionalities" ){
            @Override
            public String getValue( Candidate candidate ){
                return candidate.getFuncionalitiesToString();
            }
        });
        
        table.setUp();
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                table.setTabelaVaziaMensagem( "No candidate was selected." );
                table.setTabelaVazia();
            }
        });
        
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
                if( e.getKeyCode() == KeyEvent.VK_DELETE ){
                    deleteJButton.doClick();
                }
                else if( e.getKeyCode() == KeyEvent.VK_ENTER ){
                    editJButton.doClick();
                }
            }
        });
    }

    public void setEvolution( Evolution evolution )
    {
        this.evolution = evolution;
        this.table.setData( evolution.getCandidates() );
    }
    
    public Evolution getEvolution()
    {
        return evolution;
    }

    public FluentTable<Candidate> getTable()
    {
        return table;
    }
    
    public boolean isToNextPage() throws Exception
    {
        Evolution e = evolution.clone();
        e.getCandidates().clear();
        e.getCandidates().addAll( getTable().getTabelModel().getData() );
        
        Set<String> avaliable = FuncionalityUtils.available( e );
        if( avaliable.isEmpty() )
        {
            return true;
        }
        
        String str = BusUtils.toString( avaliable );

        if( avaliable.size() > 1 )
        {
            int index = str.lastIndexOf( "," );

            str = str.substring( 0 , index ).trim()
                + " e " 
                + str.substring( index + 1 ).trim();
        }

        int r = JOptionPane.showConfirmDialog( 
            this , 
            avaliable.size() == 1 
                ? "The funcionality " + str + " was not selected. Do you want to continue to next page?" 
                : "The funcionalities " + str + " were not selected. Do you want to continue to next page?"
        );

        return r == JOptionPane.YES_OPTION;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        addJButton = new javax.swing.JButton();
        viewJButton = new javax.swing.JButton();
        editJButton = new javax.swing.JButton();
        deleteJButton = new javax.swing.JButton();
        candidateJScrollPane = new javax.swing.JScrollPane();

        jToolBar2.setFloatable(false);

        jLabel2.setText("Candidates Selected");
        jToolBar2.add(jLabel2);
        jToolBar2.add(filler1);

        addJButton.setText("Add");
        addJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addJButton.setFocusable(false);
        addJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(addJButton);

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

        deleteJButton.setText("Delete");
        deleteJButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteJButton.setFocusable(false);
        deleteJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(deleteJButton);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
            .addComponent(candidateJScrollPane)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(candidateJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJButton;
    private javax.swing.JScrollPane candidateJScrollPane;
    private javax.swing.JButton deleteJButton;
    private javax.swing.JButton editJButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JButton viewJButton;
    // End of variables declaration//GEN-END:variables
}
