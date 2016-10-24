
package py.una.maverick.domain.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import py.una.maverick.domain.C;

public class AmountPanel extends JPanel implements ChangeListener, ActionListener {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 171860711156799253L;

    /** Number of increasing amounts to choose from (ticks on slider bar). */
    private static final int NO_OF_TICKS = 10;

    /** Slider with the amount to bet or raise. */
    private final JSlider amountSlider;

    /** Label with selected amount. */
    private final JLabel amountLabel;
    
    /** Bet/Raise button. */
    private final JButton betRaiseButton;
    
    /** Cancel button. */
    private final JButton cancelButton;
    
    /** Incremental bet amounts mapped to slider's index. */
    private final HashMap<Integer, Integer> sliderAmounts;
    
    /** Monitor while waiting for user input. */
    private final Object monitor = new Object();
    
    private Integer defaultAction;
    
    /** The selected action. */
    private Integer selectedAction;
    
    /**
     * Constructor.
     */
    public AmountPanel() {
        setBackground(UIConstants.TABLE_COLOR);
        
        sliderAmounts = new HashMap<Integer, Integer>();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        amountSlider = new JSlider();
        amountSlider.setBackground(UIConstants.TABLE_COLOR);
        amountSlider.setMajorTickSpacing(1);
        amountSlider.setMinorTickSpacing(1);
        amountSlider.setPaintTicks(true);
        amountSlider.setSnapToTicks(true);
        amountSlider.addChangeListener(this);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 5);
        add(amountSlider, gbc);

        amountLabel = new JLabel(" ");
        amountLabel.setForeground(UIConstants.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 0, 5, 0);
        add(amountLabel, gbc);
        
        betRaiseButton = new JButton("Bet");
        betRaiseButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(betRaiseButton, gbc);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(cancelButton, gbc);
    }
    

    public Integer show(Integer defaultAction, int minBet, int maxBet) {
        this.defaultAction = defaultAction;
        
        betRaiseButton.setText(C.ACTIONS_NAMES[defaultAction]);
        selectedAction = null;
        
        // Determine incremental amounts on slider bar.
        sliderAmounts.clear();
        int noOfValues = 0;
        int value = minBet;
        while (value < maxBet && noOfValues < (NO_OF_TICKS - 1)) {
            sliderAmounts.put(noOfValues, value);
            noOfValues++;
            value *= 2;
        }
        sliderAmounts.put(noOfValues, maxBet);
        amountSlider.setMinimum(0);
        amountSlider.setMaximum(noOfValues);
        amountSlider.setValue(0);
        
        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
        
        return selectedAction;
    }
    
    /**
     * Returns the selected amount.
     * 
     * @return The selected amount.
     */
    public int getAmount() {
        int index = amountSlider.getValue();
        return sliderAmounts.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public void stateChanged(ChangeEvent e) {
        int index = amountSlider.getValue();
        int amount = sliderAmounts.get(index);
        amountLabel.setText(String.format("$ %d", amount));
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == betRaiseButton) {
            selectedAction = defaultAction;
        } else if (e.getSource() == cancelButton) {
            selectedAction = null;
        }
        
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }
    
}
