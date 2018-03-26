package _07blackjack.blackjack.View;

import _07blackjack.blackjack.Controller.Blackjack;
import _07blackjack.blackjack.View.images.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by seanschwartz on 5/12/17.
 */

/**
 * The GUI for a Blackjack game object
 *
 * Provides methods for rendering and updating the interface for a game of blackjack
 *
 * @author Sean Schwartz
 * @version 1.0
 * @since 2017-05-14
 */
public class BlackjackFrame extends JFrame {

    /** The object reference to the controller */
    private Blackjack mController;

    /** Frame height and width */
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 800;

    /** The buttons for the interface */
    private JButton mHitButton;
    private JButton mStayButton;
    private JButton mBetButton;
    private JButton mDealButton;

    /** The text areas for the money bet and remaining */
    private JTextArea mMoneyBetTextArea;
    private JTextArea mMoneyLeftTextArea;

    /** The different panels in the frame */
    private JPanel cardPanel;
    private JPanel playerCardPanel;
    private JPanel computerCardPanel;
    private JPanel moneyPanel;
    private JPanel buttonPanel;

    /** A Scroll pane for the card panel */
    private JScrollPane scrollPane;

    /**
     * The constructor for a blackjack frame object
     * @param controller the object reference to the controller
     */
    public BlackjackFrame(Blackjack controller)
    {
        this.mController = controller;
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLayout(new BorderLayout());
        createComponents();

    }

    /** A method for creating the components of the frame */
    public void createComponents()
    {
        JPanel panel = new JPanel();
        createCardPanel();
        createButtonPanel();
        createMoneyPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        add(moneyPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

    }

    /** A method for creating the panel where the cards are displayed */
    public void createCardPanel()
    {
        cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(4,10));

        JLabel playerLabel = new JLabel("Player's Cards");
        JLabel computerLabel = new JLabel("Dealer's Cards");
        playerCardPanel = new JPanel();
        computerCardPanel = new JPanel();

        cardPanel.add(playerLabel);
        cardPanel.add(playerCardPanel);
        cardPanel.add(computerLabel);
        cardPanel.add(computerCardPanel);

        scrollPane = new JScrollPane(cardPanel);
    }

    /** A method to add an arrow to seperate different hands of blackjack */
    public void addCardHandBorder()
    {
        JLabel nextHandLabel = new JLabel("->");
        playerCardPanel.add(nextHandLabel);
        JLabel nextHandLabel2 = new JLabel("->");
        computerCardPanel.add(nextHandLabel2);
        add(scrollPane);
    }

    /** A method for displaying the initial dealt cards */
    public void addInitialCards()
    {
        // Displays both the cards in the players hand
        for (int i = 0; i < mController.getNumberOfPlayerCards(); i++)
        {
            int number = mController.getPlayerCardNumber(i);
            String suit = mController.getPlayerCardSuit(i);
            try
            {
                BufferedImage img  = ImageLoader.loadCard(number, suit);
                Image scaledImg = img.getScaledInstance(80, 80,  Image.SCALE_SMOOTH);
                JLabel label = new JLabel(new ImageIcon(scaledImg));
                playerCardPanel.add(label);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        // Displays just one card for the dealer after cards are dealt
        int number = mController.getComputerCardNumber(0);
        String suit = mController.getComputerCardSuit(0);
        try
        {
            BufferedImage img  = ImageLoader.loadCard(number, suit);
            Image scaledImg = img.getScaledInstance(80, 80,  Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaledImg));
            computerCardPanel.add(label);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        add(scrollPane);
        revalidate();
    }

    /** A method for adding a new card to the display after a player hits */
    public void addPlayersCards()
    {
        int number = mController.getPlayersLastCardNumber();
        String suit = mController.getPlayersLastCardSuit();
        try
        {
            BufferedImage img  = ImageLoader.loadCard(number, suit);
            Image scaledImg = img.getScaledInstance(80, 80,  Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaledImg));
            playerCardPanel.add(label);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        add(scrollPane);
        revalidate();
    }

    /** A method for adding additional computer cards to the display if the dealer needs to hit */
    public void addComputersCards()
    {
        int number = mController.getComputersLastCardNumber();
        String suit = mController.getComputersLastCardSuit();
        try
        {
            BufferedImage img  = ImageLoader.loadCard(number, suit);
            Image scaledImg = img.getScaledInstance(80, 80,  Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaledImg));
            computerCardPanel.add(label);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        add(scrollPane);
        revalidate();
    }

    /** A method for displaying the dealer's second card */
    public void addComputerSecondCard()
    {
        int number = mController.getComputerCardNumber(1);
        String suit = mController.getComputerCardSuit(1);
        try
        {
            BufferedImage img  = ImageLoader.loadCard(number, suit);
            Image scaledImg = img.getScaledInstance(80, 80,  Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaledImg));
            computerCardPanel.add(label);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        add(scrollPane);
        revalidate();
    }

    /** A method for creating the panel that displays the money */
    public void createMoneyPanel()
    {
        moneyPanel = new JPanel();

        JLabel moneyLeftLabel = new JLabel("Money Remaining: ");
        mMoneyLeftTextArea = new JTextArea(4,10);
        mMoneyLeftTextArea.setText(Integer.toString(mController.getMoneyLeft()));
        mMoneyLeftTextArea.setEditable(false);

        JLabel moneyBetLabel = new JLabel("Money Bet: ");
        mMoneyBetTextArea = new JTextArea(4,10);
        mMoneyBetTextArea.setText("0");
        mMoneyBetTextArea.setEditable(false);

        moneyPanel.add(moneyLeftLabel);
        moneyPanel.add(mMoneyLeftTextArea);
        moneyPanel.add(moneyBetLabel);
        moneyPanel.add(mMoneyBetTextArea);
    }

    /** A method for updating the money displays after a hand finishes */
    public void updateMoney()
    {
        mMoneyBetTextArea.setText("0");
        mMoneyLeftTextArea.setText(Integer.toString(mController.getMoneyLeft()));
    }

    /** A method for clearing the bet money display back to 0 */
    public void clearBetMoney()
    {
        mMoneyBetTextArea.setText("0");
    }

    /** A method for dimming buttons during a hand */
    public void dimHitAndStayButton()
    {
        mHitButton.setEnabled(false);
        mStayButton.setEnabled(false);
    }

    /** A method to dim the bet button when a hand is going on */
    public void dimBetButton()
    {
        mBetButton.setEnabled(false);
    }

    /** A method for showing the bet button */
    public void enableBetButton()
    {
        mBetButton.setEnabled(true);
    }

    /** A method for showing all buttons */
    public void enableAllButtons()
    {
        mHitButton.setEnabled(true);
        mStayButton.setEnabled(true);
        mBetButton.setEnabled(true);
        mDealButton.setEnabled(true);
    }

    /** A method for creating the buttons and their listeners */
    public void createButtonPanel()
    {
        buttonPanel = new JPanel();

        mHitButton = new JButton("Hit");
        class ClickListener implements ActionListener
        {
            public void actionPerformed(ActionEvent event)
            {
                mController.hit();
            }
        }
        ActionListener listener1 = new ClickListener();
        mHitButton.addActionListener(listener1);

        mStayButton = new JButton("Stay");
        class ClickListener2 implements ActionListener
        {
            public void actionPerformed(ActionEvent event)
            {
                mController.computerHit();
            }
        }
        ActionListener listener2 = new ClickListener2();
        mStayButton.addActionListener(listener2);

        mBetButton = new JButton("Bet 5");
        class ClickListener3 implements ActionListener
        {
            public void actionPerformed(ActionEvent event)
            {
                mController.addBetMoney(5);
                mMoneyBetTextArea.setText(Integer.toString(mController.getBetMoney()));
                mMoneyLeftTextArea.setText(Integer.toString(mController.getMoneyLeft()));

            }
        }
        ActionListener listener3 = new ClickListener3();
        mBetButton.addActionListener(listener3);

        mDealButton = new JButton("Deal Cards");
        class ClickListener4 implements ActionListener
        {
            public void actionPerformed(ActionEvent event)
            {
                mController.dealCards();
            }
        }
        ActionListener listener4 = new ClickListener4();
        mDealButton.addActionListener(listener4);


        buttonPanel.add(mHitButton);
        buttonPanel.add(mStayButton);
        buttonPanel.add(mBetButton);
        buttonPanel.add(mDealButton);
    }


}
