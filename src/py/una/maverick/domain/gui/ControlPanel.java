// This file is part of the 'texasholdem' project, an open source
// Texas Hold'em poker application written in Java.
//
// Copyright 2009 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package py.una.maverick.domain.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import py.una.maverick.domain.C;
import py.una.maverick.domain.Humano;


/**
 * Panel with buttons to let a human player select an action.
 * 
 * @author Oscar Stigter
 */
public class ControlPanel extends JPanel implements ActionListener {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 4059653681621749416L;
    


    /** The Check button. */
    private final JButton checkButton;
    
    /** The Call button. */
    private final JButton callButton;
    
    /** The Bet button. */
    private final JButton betButton;
   
    
    /** The Fold button. */
    private final JButton foldButton;
    
    
    /** The betting panel. */
    private final AmountPanel amountPanel;

    /** Monitor while waiting for user input. */
    private final Object monitor = new Object();
    
    /** The selected action. */
    private Integer selectedAction;
    
    //private Humano humano = new Humano();
    
    /**
     * Constructor.
     */
    public ControlPanel() {
        setBackground(UIConstants.TABLE_COLOR);
        //continueButton = createActionButton(Action.CONTINUE);
        callButton = createActionButton(C.ACTIONS_NAMES[1]);
        checkButton = createActionButton(C.ACTIONS_NAMES[0]);
        betButton = createActionButton(C.ACTIONS_NAMES[2]);
        foldButton = createActionButton(C.ACTIONS_NAMES[3]);
        amountPanel = new AmountPanel();
    }
    
    /**
     * Waits for the user to click the Continue button.
     * @param acciones
     */
    public void waitForUserInput(List<Integer>acciones) {
        
        getUserInput(0, 0, acciones);
    }
    
    /**
     * Waits for the user to click an action button and returns the selected
     * action.
     * 
     * @param minBet
     *            The minimum bet.
     * @param cash
     *            The player's remaining cash.
     * @param allowedActions
     *            The allowed actions.
     * 
     * @return The selected action.
     */
    public Integer getUserInput(int minBet, int cash, final List<Integer> allowedActions) {
        for (Integer allowedAction : allowedActions) {
            System.out.println("py.una.maverick.domain.gui.ControlPanel.getUserInput()" + C.ACTIONS_NAMES[allowedAction]);
        }
        selectedAction = null;
        while (selectedAction == null) {
            // Show the buttons for the allowed actions.
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    removeAll();
                    
                        if (allowedActions.contains(C.PASAR)) {
                            System.out.println("AGREGANDO BOTON PASAR");
                            add(checkButton);
                            repaint();
                        }
                        if (allowedActions.contains(C.IGUALAR)) {
                            System.out.println("AGREGANDO BOTON IGUALAR");
                            add(callButton);
                            repaint();
                        }
                        if (allowedActions.contains(C.SUBIR)) {
                            System.out.println("AGREGANDO BOTON SUBIR");
                            add(betButton);
                            repaint();
                        }
                        if (allowedActions.contains(C.RETIRARSE)) {
                            System.out.println("AGREGANDO BOTON RETIRARSE");
                            add(foldButton);
                            repaint();
                        }
                    
                    //repaint();
                }
            });
            
            // Wait for the user to select an action.
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    // Ignore.
                }
            }
            
            // In case of a bet or raise, show panel to select amount.
            if (selectedAction == C.SUBIR) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        removeAll();
                        add(amountPanel);
                        repaint();
                    }
                });
                selectedAction = amountPanel.show(selectedAction, minBet, cash);
                if (selectedAction == C.SUBIR) {
                    //humano.subir(amountPanel.getAmount());
                } else {
                    // User cancelled.
                    selectedAction = null;
                }
            }
        }
        
        return selectedAction;
    }
    
    /*
     * (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
         if (source == checkButton) {
            selectedAction = C.PASAR;
        } else if (source == callButton) {
            selectedAction = C.IGUALAR;
        } else if (source == betButton) {
            selectedAction = C.SUBIR;
        } else {
            selectedAction = C.RETIRARSE;
        }
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }
    
    /**
     * Creates an action button.
     * 
     * @param action
     *            The action.
     * 
     * @return The button.
     */
    private JButton createActionButton(String action) {
        String label = action;
        JButton button = new JButton(label);
        button.setMnemonic(label.charAt(0));
        button.setSize(100, 30);
        button.addActionListener(this);
        return button;
    }

}