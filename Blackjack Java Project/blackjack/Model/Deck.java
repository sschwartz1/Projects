package _07blackjack.blackjack.Model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by seanschwartz on 5/12/17.
 */

/**
 * A deck class
 *
 * Provides methods for rendering and updating a deck object
 *
 * @author Sean Schwartz
 * @version 1.0
 * @since 2017-05-14
 */
public class Deck {

    /** An ArrayList of cards */
    private ArrayList<Card> mDeck;

    /** A random object */
    private Random ran = new Random();

    /** The default constructor for a deck object */
    public Deck()
    {
        // Creates a deck of 6 decks of cards
        mDeck = new ArrayList<Card>();
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 13; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    mDeck.add(new Card(j,k));
                }
            }
        }
    }

    /** A method for shuffling the deck */
    public void shuffle()
    {
        // Empties Deck
        for (int i = 0; i < mDeck.size(); i++)
        {
            mDeck.remove(i);
        }
        // Creates new deck
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 13; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    mDeck.add(new Card(j,k));
                }
            }
        }
    }

    /**
     * A method for getting the next card in the deck
     * @return the next card in the deck
     */
    public Card getNextCard()
    {
        int randomCard = ran.nextInt(mDeck.size());
        Card nextCard = mDeck.get(randomCard);
        mDeck.remove(randomCard);
        if (mDeck.size() == 104)
        {
            shuffle();
        }
        return nextCard;
    }


}
