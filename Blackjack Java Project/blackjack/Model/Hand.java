package _07blackjack.blackjack.Model;

import java.util.ArrayList;

/**
 * Created by seanschwartz on 5/12/17.
 */

/**
 * A Hand Class
 *
 * Provides methods for rendering and updating a hand object
 *
 * @author Sean Schwartz
 * @version 1.0
 * @since 2017-05-14
 */
public class Hand {

    /** A static deck object */
    private static Deck mDeck = new Deck();

    /** An ArrayList of cards */
    private ArrayList<Card> mHand = new ArrayList<Card>();

    /** The default constructor for a hand object */
    public Hand() {
        mHand.add(mDeck.getNextCard());
        mHand.add(mDeck.getNextCard());
    }

    /**
     * A method for getting the hand size
     * @return the size of the hand
     */
    public int getHandSize() {
        return mHand.size();
    }

    /**
     * A method for getting the value of a card for image rendering
     * @param number the card number in the hand
     * @return the value for the card
     */
    public int getCardValue(int number) {
        return mHand.get(number).getCardImageValue();
    }

    /**
     * A method for getting the suit of a card
     * @param number the card in the hand
     * @return the card suit
     */
    public String getCardSuit(int number) {
        return mHand.get(number).getCardSuit();
    }

    /** A method for adding a card to the hand */
    public void hit() {
        mHand.add(mDeck.getNextCard());
    }

    /**
     * A method for getting the last card dealt in a hand
     * @return the card image value
     */
    public int getLastDealtCardValue()
    {
        return mHand.get(mHand.size()-1).getCardImageValue();
    }

    /**
     * A method for getting the suit of the last card in the hand
     * @return the suit of the card
     */
    public String getLastDealtCardSuit()
    {
        return mHand.get(mHand.size()-1).getCardSuit();
    }

    /**
     * A method for getting the total score of the hand
     * @return the score of the hand
     */
    public int getHandValue()
    {
        int sum = 0;
        int aceCount = 0;
        for (int i = 0; i < mHand.size(); i++)
        {
            if (mHand.get(i).getCardValue() == 11)
            {
                aceCount += 1;
            }
            sum += mHand.get(i).getCardValue();
        }
        if (aceCount > 0)
        {
            for (int j = 0; j < aceCount; j++)
            {
                if (sum > 21)
                {
                    sum -= 10;
                }
            }
        }
        return sum;
    }

    /**
     * A method for testing if the hand contains an ace
     * @return true if there is, false if there is not
     */
    public boolean isAce()
    {
        for (int i = 0; i < mHand.size(); i++)
        {
            if (mHand.get(i).getCardValue() == 11)
            {
                return true;
            }
        }
        return false;
    }
}
