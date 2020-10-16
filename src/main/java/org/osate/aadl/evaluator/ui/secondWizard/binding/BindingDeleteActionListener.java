package org.osate.aadl.evaluator.ui.secondWizard.binding;

import fluent.gui.impl.swing.FluentTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.osate.aadl.evaluator.evolution.Binding;

public class BindingDeleteActionListener implements ActionListener
{
    private final FluentTable<Binding> table;

    public BindingDeleteActionListener( FluentTable<Binding> table )
    {
        this.table = table;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        table.removeSelectedObjects();
    }
    
}