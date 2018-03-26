package _07blackjack.blackjack.Controller;

import _07blackjack.blackjack.Model.Hand;
import _07blackjack.blackjack.Model.Money;
import _07blackjack.blackjack.View.BlackjackFrame;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by seanschwartz on 5/11/17.
 */

/**
 * A Blackjack GUI
 *
 * Provides methods for rendering and updating a blackjack game GUI
 *
 * @author Sean Schwartz
 * @version 1.0
 * @since 2017-05-14
 */
public class Blackjack {

    /** The Blackjack frame object */
    private BlackjackFrame mBlackjackFrame;

    /** The user's hand */
    private Hand mPlayerHand;

    /** The dealer's hand */
    private Hand mComputerHand;

    /** The user's money object */
    private Money mPlayerMoney;

    /** The amount money bet on a hand */
    private int mBetMoney;


    /** The default Blackjack object constructor */
    public Blackjack()
    {
        mPlayerMoney = new Money();
        mPlayerHand = new Hand();
        mComputerHand = new Hand();
    }

    /** A method for dealing cards, activated when the deal cards button is clicked */
    public void dealCards()
    {
        mBlackjackFrame.addCardHandBorder();
        mBlackjackFrame.enableAllButtons();
        mBlackjackFrame.dimBetButton();
        // Creates a new hand for the player and dealer
        mPlayerHand = new Hand();
        mComputerHand = new Hand();
        // Displays dealt cards on the GUI
        mBlackjackFrame.addInitialCards();
        // If player gets blackjack
        if (mPlayerHand.getHandValue() == 21 & mComputerHand.getHandValue() != 21)
        {
            // Shows dealers other card
            mBlackjackFrame.addComputerSecondCard();
            // Adds money won to player's "wallet"
            mPlayerMoney.addMoney(2 * mBetMoney);
            mBetMoney = 0;
            // Updates the GUI to show the new money and dims certain buttons
            mBlackjackFrame.updateMoney();
            mBlackjackFrame.dimHitAndStayButton();
            mBlackjackFrame.enableBetButton();
        }
        // If both player and dealer get blackjack
        else if (mPlayerHand.getHandValue() == 21 && mComputerHand.getHandValue() == 21)
        {
            mBlackjackFrame.addComputerSecondCard();
            mPlayerMoney.addMoney(mBetMoney);
            mBetMoney = 0;
            mBlackjackFrame.clearBetMoney();
            mBlackjackFrame.dimHitAndStayButton();
            mBlackjackFrame.enableBetButton();
        }
        // If dealer gets blackjack and player doesn't
        else if (mPlayerHand.getHandValue() != 21 && mComputerHand.getHandValue() == 21)
        {
            mBlackjackFrame.addComputerSecondCard();
            mBetMoney = 0;
            mBlackjackFrame.clearBetMoney();
            mBlackjackFrame.dimHitAndStayButton();
            mBlackjackFrame.enableBetButton();
        }
    }

    /** A method to get the amount of cards in a player's hand */
    public int getNumberOfPlayerCards()
    {
        return mPlayerHand.getHandSize();
    }

    /**
     * A method to get the value of the player's card for loading the image
      * @param cardNumber integer of the card in the player's hand
     * @return the number portion of the file string for that card
     */
    public int getPlayerCardNumber(int cardNumber)
    {
        return mPlayerHand.getCardValue(cardNumber);
    }

    /**
     * A method to return the suit of a given card in a player's hand
     * @param cardNumber integer of the card in the player's hand
     * @return the suit of that card
     */
    public String getPlayerCardSuit(int cardNumber)
    {
        return mPlayerHand.getCardSuit(cardNumber);
    }

    /**
     * A method to get the value of the player's last card for loading the card image
     * @return the number portion of the file string for the last dealt player card
     */
    public int getPlayersLastCardNumber()
    {
        return mPlayerHand.getLastDealtCardValue();
    }

    /**
     * A method for getting the suit of the last player card dealt
     * @return the string of the suit of the last player card dealt
     */
    public String getPlayersLastCardSuit()
    {
        return mPlayerHand.getLastDealtCardSuit();
    }

    /**
     * A method to get the value of the computers's card for loading the image
     * @param cardNumber integer of the card in the computers's hand
     * @return the number portion of the file string for that card
     */
    public int getComputerCardNumber(int cardNumber)
    {
        return mComputerHand.getCardValue(cardNumber);
    }

    /**
     * A method to return the suit of a given card in a computer's hand
     * @param cardNumber integer of the card in the computer's hand
     * @return the suit of that card
     */
    public String getComputerCardSuit(int cardNumber)
    {
        return mComputerHand.getCardSuit(cardNumber);
    }

    /**
     * A method to get the value of the computer's last card for loading the card image
     * @return the number portion of the file string for the last dealt computer card
     */
    public int getComputersLastCardNumber()
    {
        return mComputerHand.getLastDealtCardValue();
    }

    /**
     * A method for getting the suit of the last computer card dealt
     * @return the string of the suit of the last computer card dealt
     */
    public String getComputersLastCardSuit()
    {
        return mComputerHand.getLastDealtCardSuit();
    }

    /** A method for updating the model and GUI after the player hits */
    public void hit()
    {
        // Only hits if the hand is 21 or less
        if (mPlayerHand.getHandValue() < 22)
        {
            // Adds a card to the player's hand
            mPlayerHand.hit();
            // Displays new card in the GUI
            mBlackjackFrame.addPlayersCards();
            // If the player busts
            if (mPlayerHand.getHandValue() > 21)
            {
                mBlackjackFrame.addComputerSecondCard();
                mBetMoney = 0;
                mBlackjackFrame.clearBetMoney();
                mBlackjackFrame.dimHitAndStayButton();
                mBlackjackFrame.enableBetButton();
            }
        }
    }

    /** A method for updating the model and GUI after the player hits stay button */
    public void computerHit()
    {
        // Shows dealer's second card
        mBlackjackFrame.addComputerSecondCard();
        // Checks for a soft ace hit
        if (mComputerHand.getHandValue() == 17 && mComputerHand.isAce() == true)
        {
            mComputerHand.hit();
            mBlackjackFrame.addComputersCards();
        }
        // Dealer hits while under 17
        while (mComputerHand.getHandValue() < 17)
        {
            mComputerHand.hit();
            // Shows dealer's new cards on the GUI
            mBlackjackFrame.addComputersCards();
        }
        // Computer busts
        if (mComputerHand.getHandValue() > 21)
        {
            mPlayerMoney.addMoney(2 * mBetMoney);
            mBetMoney = 0;
            mBlackjackFrame.updateMoney();
            mBlackjackFrame.dimHitAndStayButton();
            mBlackjackFrame.enableBetButton();
        }
        // Computer stays
        // Player wins
        if (mPlayerHand.getHandValue() > mComputerHand.getHandValue())
        {
            mPlayerMoney.addMoney(2 * mBetMoney);
            mBetMoney = 0;
            mBlackjackFrame.updateMoney();
            mBlackjackFrame.dimHitAndStayButton();
            mBlackjackFrame.enableBetButton();
        }
        // Player pushes
        else if (mPlayerHand.getHandValue() == mComputerHand.getHandValue())
        {
            mPlayerMoney.addMoney(mBetMoney);
            mBetMoney = 0;
            mBlackjackFrame.updateMoney();
            mBlackjackFrame.dimHitAndStayButton();
            mBlackjackFrame.enableBetButton();
        }
        else
        {
            mBetMoney = 0;
            mBlackjackFrame.clearBetMoney();
            mBlackjackFrame.dimHitAndStayButton();
            mBlackjackFrame.enableBetButton();
        }
    }

    /**
     * A method for adding money to the money that is being bet on the had
     * @param money the money that is being bet on the hand
     */
    public void addBetMoney(int money)
    {
        mBetMoney += money;
        mPlayerMoney.subtractMoney(money);
    }

    /**
     * A method for getting the amount of money bet on the hand
     * @return the money bet on the hand
     */
    public int getBetMoney()
    {
        return mBetMoney;
    }

    /**
     * A method for getting the player's remaining money
     * @return the player's remaining money
     */
    public int getMoneyLeft()
    {
        return mPlayerMoney.getMoney();
    }

    public void setFrameDelegate(BlackjackFrame frame) {
        this.mBlackjackFrame = frame;
    }

    public static void main(String[] args)
    {
        Blackjack blackjack = new Blackjack();
        BlackjackFrame frame = new BlackjackFrame(blackjack);
        blackjack.setFrameDelegate(frame);
        frame.setTitle("Blackjack");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
