
package py.una.maverick.domain.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import py.una.maverick.domain.C;
import py.una.maverick.domain.Carta;
import py.una.maverick.domain.Jugador;



public class PlayerPanel extends JPanel {
    
    private static final long serialVersionUID = 5851738752943098606L;

    private static final Icon BUTTON_PRESENT_ICON =
            ResourceManager.getIcon("/images/button_present.png");
    
    private static final Icon BUTTON_ABSENT_ICON =
            ResourceManager.getIcon("/images/button_absent.png");
    
    private static final Icon CARD_PLACEHOLDER_ICON =
        ResourceManager.getIcon("/images/card_placeholder.png");

    private static final Icon CARD_BACK_ICON =
            ResourceManager.getIcon("/images/card_back.png");
    
    private static final Border BORDER = new EmptyBorder(10, 10, 10, 10);
    
    private JLabel nameLabel;
    
    private JLabel cashLabel;
    
    private JLabel actionLabel;
    
    private JLabel betLabel;

    private JLabel card1Label;

    private JLabel card2Label;

    private JLabel dealerButton;
    
    /**
     * Constructor.
     */
    public PlayerPanel() {
        setBorder(BORDER);
        setBackground(UIConstants.TABLE_COLOR);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        nameLabel = new MyLabel();
        cashLabel = new MyLabel();
        actionLabel = new MyLabel();
        betLabel = new MyLabel();
        card1Label = new JLabel(CARD_PLACEHOLDER_ICON);
        card2Label = new JLabel(CARD_PLACEHOLDER_ICON);
        dealerButton = new JLabel(BUTTON_ABSENT_ICON);
        
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        add(dealerButton, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.insets = new Insets(1, 1, 1, 1);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        add(nameLabel, gc);
        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(cashLabel, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(actionLabel, gc);
        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(betLabel, gc);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        add(card1Label, gc);
        gc.gridx = 1;
        gc.gridy = 3;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        add(card2Label, gc);

        setInTurn(false);
        setDealer(false);
    }

    public void update(Jugador player) {
        nameLabel.setText(player.getNombre());
        cashLabel.setText("$ " + player.getFichas());
        int bet = player.getMesa().getApuestas().getOrDefault(player.getNombre(),0);
        if (bet == 0) {
            betLabel.setText(" ");
        } else {
            betLabel.setText("$ " + bet);
        }
        Integer action = player.getAction();
        if (action != null) {
            actionLabel.setText(C.ACTIONS_NAMES[action]);
        } else {
            actionLabel.setText(" ");
        }
        if (action != C.RETIRARSE) {
            Carta[] cards = player.getCartas();
            if (cards.length == 2 && player.getNombre().equals("humano")) {
                // Visible cards.
                card1Label.setIcon(ResourceManager.getCardImage(cards[0]));
                card2Label.setIcon(ResourceManager.getCardImage(cards[1]));
            } else {
                // Hidden cards (face-down).
                card1Label.setIcon(CARD_BACK_ICON);
                card2Label.setIcon(CARD_BACK_ICON);
            }
        } else {
            // No cards.
            card1Label.setIcon(CARD_PLACEHOLDER_ICON);
            card2Label.setIcon(CARD_PLACEHOLDER_ICON);
        }
        if(player.getMesa().getEstadoRonda().equals(C.SHOWDOWN)){
            Carta[] cards = player.getCartas();
            card1Label.setIcon(ResourceManager.getCardImage(cards[0]));
            card2Label.setIcon(ResourceManager.getCardImage(cards[1]));
        }
    }
    

    public void setDealer(boolean isDealer) {
        if (isDealer) {
            dealerButton.setIcon(BUTTON_PRESENT_ICON);
        } else {
            dealerButton.setIcon(BUTTON_ABSENT_ICON);
        }
    }
    

    public void setInTurn(boolean inTurn) {
        if (inTurn) {
            nameLabel.setForeground(Color.YELLOW);
        } else {
            nameLabel.setForeground(Color.GREEN);
        }
    }
    
    private static class MyLabel extends JLabel {

        private static final long serialVersionUID = 3607645928062082095L;

        public MyLabel() {
            setBorder(UIConstants.LABEL_BORDER);
            setForeground(UIConstants.TEXT_COLOR);
            setHorizontalAlignment(JLabel.HORIZONTAL);
            setText(" ");
        }
        
    } 
    
}
