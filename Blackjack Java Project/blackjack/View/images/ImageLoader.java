package _07blackjack.blackjack.View.images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @version  1.0
 * @author Lamont Samuels
 * @since  11-13-16
 */

public class ImageLoader {

    private static ImageLoader sInstance = new ImageLoader();

    public enum CardNumber {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    }
    public enum CardSuit {
        DIAMONDS,
        SPADES,
        HEARTS,
        CLUBS
    }

    /**
     * Should not be able to create an image loader instance outside of this class
     */
    private ImageLoader() {}

    /**
     * Computes a string representation for a CardSuit enumeration
     * @param suit - the card suit
     * @return a string representaiton for the the card suit
     */
    private static String getSuit(CardSuit suit) {
        String strSuit = "";

        switch (suit) {
            case DIAMONDS:
                strSuit = "diamonds";
                break;
            case SPADES:
                strSuit = "spades";
                break;
            case HEARTS:
                strSuit = "hearts";
                break;
            case CLUBS:
                strSuit = "clubs";
                break;
        }
        return strSuit;
    }
    /**
     * Computes a string representation for a CardSuit enumeration
     * @param suit - the card suit
     * @return a string representaiton for the the card suit
     * @throws IllegalArgumentException if the suit is not a valid suit name
     */
    private static CardSuit getCardSuit(String suit) throws IllegalArgumentException {
        String strSuit = "";

        if(suit.equals("diamonds")){
            return CardSuit.DIAMONDS;
        }else if(suit.equals("clubs")){
            return CardSuit.CLUBS;
        }else if(suit.equals("spades")){
            return CardSuit.SPADES;
        }else if(suit.equals("hearts")){
            return CardSuit.HEARTS;
        } else {
            throw new IllegalArgumentException("Suit is not a valid suit");
        }
    }
    /**
     * Returns a buffered image from the resources directory of the blackjack package
     * @param number - the card number
     * @param suit - the card suit
     * @return a buffered image of the card number and suit
     * @throws IOException
     */
    public static BufferedImage loadCard (int  number, CardSuit suit) throws IOException {

        if (number < 1 || number > 13){
            throw new IllegalArgumentException("Card number must be between 1-13");
        }
        if (suit == null){
            throw new IllegalArgumentException("Card suit must not be null");
        }


        String file = number + "_of_" + getSuit(suit) + ".png";
        BufferedImage img = ImageIO.read(sInstance.getClass().getResource("resources/" + file));
        // System.out.println("Height of Image = " + img.getHeight());
        return img;
    }
    /**
     * Returns a buffered image from the resources directory of the blackjack package
     * @param number - the card number
     * @param suit - the card suit
     * @return a buffered image of the card number and suit
     * @throws IOException
     */
    public static BufferedImage loadCard (CardNumber number, CardSuit suit) throws IOException {

        int ordinal = number.ordinal();
        //Ace has value 1 not zero
        if(ordinal == 0) {
            ordinal++;
        }
        return loadCard(ordinal, suit);
    }
    /**
     * Returns a buffered image from the resources directory of the blackjack package
     * The string must be either "diamonds", "clubs", "hearts", "spades"
     * @param number - the card number
     * @param suit - the card suit
     * @return a buffered image of the card number and suit
     * @throws IOException
     */
    public static BufferedImage loadCard (int number, String suit) throws IOException {

        return loadCard(number, getCardSuit(suit));
    }
}
