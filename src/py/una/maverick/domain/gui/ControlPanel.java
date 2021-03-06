
package py.una.maverick.domain.gui;

import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
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
import py.una.maverick.domain.Jugador;


public class ControlPanel extends JPanel implements ActionListener {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 4059653681621749416L;
    


    private final JButton checkButton;
    
    private final JButton callButton;
    
    private final JButton betButton;
   
    private final JButton foldButton;
    
    private final AmountPanel amountPanel;

    private final Object monitor = new Object();
    
    private Integer selectedAction;
    
    private Jugador humano;

    public ControlPanel() {
        setBackground(UIConstants.TABLE_COLOR);
        //continueButton = createActionButton(Action.CONTINUE);
        callButton = createActionButton(C.ACTIONS_NAMES[1]);
        checkButton = createActionButton(C.ACTIONS_NAMES[0]);
        betButton = createActionButton(C.ACTIONS_NAMES[2]);
        foldButton = createActionButton(C.ACTIONS_NAMES[3]);
        amountPanel = new AmountPanel();
    }
    

    public void waitForUserInput(List<Integer>acciones) {
        
        getUserInput(0, 0, acciones);
    }
    

    public Integer getUserInput(int minBet, int cash, final List<Integer> allowedActions) {
        selectedAction = null;
        while (selectedAction == null) {
            // Show the buttons for the allowed actions.
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    removeAll();                    
                        if (allowedActions.contains(C.PASAR)) {
                            add(checkButton);
                        }
                        if (allowedActions.contains(C.IGUALAR)) {
                            add(callButton);
                        }
                        if (allowedActions.contains(C.SUBIR)) {
                            add(betButton);
                        }
                        if (allowedActions.contains(C.RETIRARSE)) {
                            add(foldButton);
                        }
                    
                    repaint();
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
                if (selectedAction != null && selectedAction == C.SUBIR) {
                    humano.subir(amountPanel.getAmount());
                } else {
                    // User cancelled.
                    selectedAction = null;
                }
            }
        }
        
        return selectedAction;
    }

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
    

    private JButton createActionButton(String action) {
        String label = action;
        JButton button = new JButton(label);
        button.setMnemonic(label.charAt(0));
        button.setSize(100, 30);
        button.addActionListener(this);
        return button;
    }

    public Jugador getHumano() {
        return humano;
    }

    public void setHumano(Jugador humano) {
        this.humano = humano;
    }
}
