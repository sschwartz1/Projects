package _07blackjack.blackjack.View.images;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by lamont on 5/13/17.
 */
public class ImageLoaderFrame extends JFrame{


        /** The width of the frame */
        private static final int FRAME_WIDTH = 800;

        /** The height of the frame */
        private static final int FRAME_HEIGHT = 600;


        public ImageLoaderFrame() {

            //Initialize and add the frame components to the frame
            createComponents();
            setSize(FRAME_WIDTH, FRAME_HEIGHT);
        }
        public void createComponents() {

            //Creating a panel to place all components
            JPanel panel = new JPanel();

            try
            {
                /** The ImageLoader class with the directory of images defined in the resources directory.
                 *  There are multiple of ways to load the resources into the frame (e.g. by using  either
                 *  enumerations or primitive ints and strings.
                 */
                //Here's three ways to load the "Ace" of Diamonds card using the ImageLoader
                BufferedImage img  = ImageLoader.loadCard(1, "diamonds"); //You'll need to look in the file to figure out the strings and numbers
                BufferedImage img2 = ImageLoader.loadCard(ImageLoader.CardNumber.ACE, ImageLoader.CardSuit.DIAMONDS);
                BufferedImage img3 = ImageLoader.loadCard(1, ImageLoader.CardSuit.DIAMONDS);

                //Lets load the Jack of Spades
                BufferedImage img4 = ImageLoader.loadCard(ImageLoader.CardNumber.JACK, ImageLoader.CardSuit.SPADES);
                BufferedImage img5 = ImageLoader.loadCard(11, "spades");


                //This is how you can resize the image
                Image scaledImg = img.getScaledInstance(80, 80,  Image.SCALE_SMOOTH);
                Image scaledImg2 = img5.getScaledInstance(80, 80,  Image.SCALE_SMOOTH);

                //Now place the image inside a label
                JLabel label = new JLabel(new ImageIcon(scaledImg));
                JLabel label2 = new JLabel(new ImageIcon(scaledImg2));

                panel.add(label);
                panel.add(label2);

                //We make a scrollPane to allow the panel to be scrollable. You can look into the documentation or
                //online about scrollPane.
                // https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html
                JScrollPane scrollPane = new JScrollPane(panel);

                add(scrollPane);



            }catch (IOException e){
                e.printStackTrace();
            }



            //add(panel);

        }
        public static void main(String[] args) {

            //Instantiate the customized frame "Hello World Frame" from the main method
            JFrame frame = new ImageLoaderFrame();
            frame.setTitle("Image Loader App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

        }

    }
