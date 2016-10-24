
package py.una.maverick.domain.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import py.una.maverick.bots.DummyBot;
import py.una.maverick.domain.C;
import py.una.maverick.domain.Carta;
import py.una.maverick.domain.Client;


import py.una.maverick.domain.Humano;
import py.una.maverick.domain.Jugador;
import py.una.maverick.domain.Mesa;

public class Main extends JFrame implements Client{
    
    /** Serial version UID. */
    private static final long serialVersionUID = -5414633931666096443L;
    

    /** The size of the big blind. */
    private static final int BIG_BLIND = 10;
    
    /** The starting cash per player. */
    private static final int STARTING_CASH = 500;
    
   private Mesa mesa = new Mesa();
    
    /** The players at the table. */
    private final Map<String, Jugador> players;
    
    /** The GridBagConstraints. */
    private final GridBagConstraints gc;
    
    /** The board panel. */
    private final BoardPanel boardPanel;
    
    /** The control panel. */
    private final ControlPanel controlPanel;
    
    /** The player panels. */
    private final Map<String, PlayerPanel> playerPanels;
    
    /** The human player. */
    private final Jugador pokerBot;
    
    /** The current dealer's name. */
    private String dealerName; 

    /** The current actor's name. */
    private String actorName; 
    
    /**
     * Constructor.
     */
    public Main() {
        super("Texas Hold'em poker");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UIConstants.TABLE_COLOR);
        setLayout(new GridBagLayout());

        gc = new GridBagConstraints();
        
        controlPanel = new ControlPanel();
        
        boardPanel = new BoardPanel(controlPanel);        
        addComponent(boardPanel, 1, 1, 1, 1);
        
        players = new LinkedHashMap<>();
        pokerBot = new DummyBot();
        players.put(pokerBot.getNombre(), pokerBot);
        Humano humano = new Humano(this);
        controlPanel.setHumano(humano);
        players.put(humano.getNombre(),   humano);
        List<Jugador>jugadores = new ArrayList<>();
        jugadores.add(humano);
        jugadores.add(pokerBot);
        mesa.setJugadores(jugadores);
        playerPanels = new HashMap<String, PlayerPanel>();
        int i = 0;
        for (Jugador player : players.values()) {
            PlayerPanel panel = new PlayerPanel();
            playerPanels.put(player.getNombre(), panel);
            switch (i++) {
                case 0:
                    // North position.
                    addComponent(panel, 1, 0, 1, 1);
                    break;
                case 1:
                    // South position.
                    addComponent(panel, 1, 2, 1, 1);
                    break;
                case 2:
                    // East position.
                    addComponent(panel, 2, 1, 1, 1);
                    break;
                case 3:
                    // West position.
                    addComponent(panel, 0, 1, 1, 1);
                    break;
                default:
                    // Do nothing.
            }
        }
        
        // Show the frame.
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        // Start the game.
        mesa.startGame();
        
        
    }
    
    /**
     * The application's entry point.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void joinedTable(int bigBlind, List<Jugador> players) {
        for (Jugador player : players) {
            PlayerPanel playerPanel = playerPanels.get(player.getNombre());
            if (playerPanel != null) {
                playerPanel.update(player);
            }
        }
    }

    @Override
    public void messageReceived(String message) {
        boardPanel.setMessage(message);
        boardPanel.waitForUserInput(players.get("humano").accionesPosibles());
    }

    @Override
    public void handStarted(Jugador dealer) {
        setDealer(false);
        dealerName = dealer.getNombre();
        setDealer(true);
    }

    @Override
    public void actorRotated(Jugador actor) {
        setActorInTurn(false);
        actorName = actor.getNombre();
        setActorInTurn(true);
    }

    @Override
    public void boardUpdated(List<Carta> cards, int bet, int pot) {
        boardPanel.update(cards, bet, pot);
    }

    @Override
    public void playerUpdated(Jugador player) {
        PlayerPanel playerPanel = playerPanels.get(player.getNombre());
        if (playerPanel != null) {
            playerPanel.update(player);
        }
    }

    @Override
    public void playerActed(Jugador player) {
        String name = player.getNombre();
        PlayerPanel playerPanel = playerPanels.get(name);
        if (playerPanel != null) {
            playerPanel.update(player);
            Integer action = player.getAction();
            if (action != null) {
                boardPanel.setMessage(String.format("%s %s.", name, C.ACTIONS_NAMES[action]));
                //if (player.getClient() != this) {
                    boardPanel.waitForUserInput(player.accionesPosibles());
                //}
            }
        } else {
            throw new IllegalStateException(
                    String.format("no hay playerpanel para '%s'", name));
        }
    }

    @Override
    public Integer act(int minBet, int currentBet, List<Integer> allowedActions) {
        boardPanel.setMessage("Seleccione una acción:");
        return controlPanel.getUserInput(minBet, pokerBot.getFichas(), allowedActions);
    }

    /**
     * Adds an UI component.
     * 
     * @param component
     *            The component.
     * @param x
     *            The column.
     * @param y
     *            The row.
     * @param width
     *            The number of columns to span.
     * @param height
     *            The number of rows to span.
     */
    private void addComponent(Component component, int x, int y, int width, int height) {
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = width;
        gc.gridheight = height;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        getContentPane().add(component, gc);
    }

    /**
     * Sets whether the actor  is in turn.
     * 
     * @param isInTurn
     *            Whether the actor is in turn.
     */
    private void setActorInTurn(boolean isInTurn) {
        if (actorName != null) {
            PlayerPanel playerPanel = playerPanels.get(actorName);
            if (playerPanel != null) {
                playerPanel.setInTurn(isInTurn);
            }
        }
    }

    

    private void setDealer(boolean isDealer) {
        if (dealerName != null) {
            PlayerPanel playerPanel = playerPanels.get(dealerName);
            if (playerPanel != null) {
                playerPanel.setDealer(isDealer);
            }
        }
    }

}
