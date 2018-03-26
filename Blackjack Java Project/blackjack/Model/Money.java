package _07blackjack.blackjack.Model;

/**
 * Created by seanschwartz on 5/13/17.
 */

/**
 * A money class
 *
 * Provides methods for rendering and updating a money object
 *
 * @author Sean Schwartz
 * @version 1.0
 * @since 2017-05-14
 */
public class Money {

    /** An the player's money */
    private int mPlayerMoney;

    /**
     * The default constructor of a money object
     */
    public Money()
    {
        this.mPlayerMoney = 1000;
    }

    /**
     * A method for subtracting from a player's money
     * @param amount the amount wanted to subtract
     */
    public void subtractMoney(int amount)
    {
        mPlayerMoney -= amount;
    }

    /**
     * A method for adding to a player's money
     * @param amount the amount to add to the player
     */
    public void addMoney(int amount)
    {
        mPlayerMoney += amount;
    }

    /**
     * A method for getting the money for a player
     * @return the player's money
     */
    public int getMoney()
    {
        return mPlayerMoney;
    }

}
