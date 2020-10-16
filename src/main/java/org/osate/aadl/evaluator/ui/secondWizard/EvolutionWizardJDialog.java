package org.osate.aadl.evaluator.ui.secondWizard;

import fluent.gui.impl.swing.FluentWizardPanel;
import fluent.gui.impl.swing.wizard.PaginaPanel;
import fluent.gui.impl.swing.wizard.WizardAdapter;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.List;
import javax.swing.JOptionPane;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.ui.secondWizard.binding.BindingListJPanel;
import org.osate.aadl.evaluator.ui.secondWizard.candidate.CandidateListJPanel;
import org.osate.aadl.evaluator.ui.secondWizard.result.EvolutionResultJPanel;

public class EvolutionWizardJDialog extends javax.swing.JDialog 
{
    private FluentWizardPanel wizard;
    private Evolution original;
    private Evolution evolution;
    
    private DeclarationSelectJPanel componentSelectJPanel;
    private CandidateListJPanel candidateListJPanel;
    private BindingListJPanel bindingListJPanel;
    private EvolutionResultJPanel resultJPanel;
    
    private boolean saved;
    
    public EvolutionWizardJDialog( java.awt.Window parent )
    {
        super( parent );
        
        initComponents();
        init();
        
        setTitle( "Evolution" );
        setSize( 800 , 350 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        saved = false;
        
        add( wizard = new FluentWizardPanel( "Evolution" ) {
            @Override
            public void fechar() {
                setVisible( false );
            }
        } , BorderLayout.CENTER );
        
        wizard.addListener(new WizardAdapter(){
            @Override
            public boolean canChangePage(int currentPageIndex, int nextPageIndex) {
                try
                {
                    if( currentPageIndex == 0 )
                    {
                        evolution.getDeclarations().clear();
                        evolution.getDeclarations().addAll( componentSelectJPanel.getSelected() );

                        candidateListJPanel.setEvolution( evolution );
                    }
                    else if( currentPageIndex == 1 && nextPageIndex == 2 )
                    {
                        if( !candidateListJPanel.isToNextPage() )
                        {
                            return false;
                        }
                        
                        evolution = candidateListJPanel.getEvolution();
                        
                        evolution.getCandidates().clear();
                        evolution.getCandidates().addAll(
                            candidateListJPanel.getTable().getTabelModel().getData()
                        );
                        
                        bindingListJPanel.setEvolution( evolution );
                    }
                    else if( currentPageIndex == 2 && nextPageIndex == 3 )
                    {
                        evolution = bindingListJPanel.getEvolution();
                        
                        evolution.getBindings().clear();
                        evolution.getBindings().addAll(
                            bindingListJPanel.getTable().getTabelModel().getData()
                        );
                        
                        resultJPanel.setEvolution( evolution );
                    }

                    return true;
                }
                catch( Exception err )
                {
                    err.printStackTrace();
                    
                    JOptionPane.showMessageDialog( 
                        rootPane , 
                        err.getMessage() , 
                        "Error" , 
                        JOptionPane.ERROR_MESSAGE 
                    );
                    
                    return false;
                }
            }

            @Override
            public void finish( Collection<PaginaPanel> pages ) {
                saved = true;
                setVisible( false );
            }
        });
        
        addPages();
    }
    
    private void addPages()
    {
        wizard.getPaginas().add( new PaginaPanel( 
            componentSelectJPanel = new DeclarationSelectJPanel() {
                @Override
                public void setSelect( List<Declaration> declarations ) {
                    wizard.proxima();
                }
            } , 
                "Please, select a component to change it."
            )
        );
        
        wizard.getPaginas().add( new PaginaPanel( 
            candidateListJPanel = new CandidateListJPanel() , 
                "Please, select one or more component to change all declaration were selected."
            )
        );
        
        wizard.getPaginas().add( new PaginaPanel( 
            bindingListJPanel = new BindingListJPanel() , 
                "Please, select one or more component to change all declaration were selected."
            )
        );
        
        wizard.getPaginas().add(new PaginaPanel( 
            resultJPanel = new EvolutionResultJPanel() , 
                "It is the last page and it show the AADL result, "
                + "and the component and reqspec affected by the changes."
            )
        );
        
        wizard.setPaginaAtual( -1 );
        wizard.proxima();
    }
    
    // ---------------------------- //
    // ---------------------------- //
    // ---------------------------- //

    public DeclarationSelectJPanel getComponentSelectJPanel()
    {
        return componentSelectJPanel;
    }

    public CandidateListJPanel getCandidateListJPanel()
    {
        return candidateListJPanel;
    }

    public BindingListJPanel getBindingListJPanel()
    {
        return bindingListJPanel;
    }

    public EvolutionResultJPanel getEvolutionResultJPanel() 
    {
        return resultJPanel;
    }
    
    // ---------------------------- //
    // ---------------------------- //
    // ---------------------------- //

    public void setEvolution( Evolution evolution )
    {
        this.original  = evolution;
        this.evolution = evolution.clone();
        
        this.componentSelectJPanel.setSystem( evolution.getSystem() );
    }

    public Evolution getEvolution()
    {
        return evolution;
    }
    
    public boolean isSaved()
    {
        return saved;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
