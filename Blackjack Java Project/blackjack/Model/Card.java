package _07blackjack.blackjack.Model;

/**
 * Created by seanschwartz on 5/12/17.
 */

/**
 * A card class
 *
 * Provides methods for rendering a card object
 *
 * @author Sean Schwartz
 * @version 1.0
 * @since 2017-05-14
 */
public class Card {

    /** A list of card image names */
    private String[] mNames = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};

    /** A list of suit strings */
    private String[] mSuits = {"hearts", "diamonds", "spades", "clubs"};

    /** A list of card values */
    private int[] mCardValues = {11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};

    /** A list of integers of image values */
    private int[] mImageNumberValues = {1,2,3,4,5,6,7,8,9,10,11,12,13};

    /** The suit number of the card */
    private int mSuitNumber;

    /** The value of the card */
    private int mCardValue;

    /**
     * The constructor of a card object
     * @param value the value of the card
     * @param suitNumber the suit number of the card
     */
    public Card(int value, int suitNumber)
    {
        this.mSuitNumber = suitNumber;
        this.mCardValue = value;
    }

    /**
     * A method of getting the value of the card
     * @return the value of the card
     */
    public int getCardValue()
    {
        return mCardValues[mCardValue];
    }

    /**
     * A method for getting the suit of the card
     * @return the suit of the card
     */
    public String getCardSuit()
    {
        return mSuits[mSuitNumber];
    }

    /**
     * A method for getting the the image value of the card
     * @return the image value of the card
     */
    public int getCardImageValue()
    {
        return mImageNumberValues[mCardValue];
    }

}
