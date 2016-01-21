package tanks.bf;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class ImageLoader {
    private final String EAGLE_IMAGE_NAME = "images/Eagle.png";
    private final String BRICK_IMAGE_NAME = "images/bricks_full2.jpg";
    private final String CRUSHED_BRICK_IMAGE_NAME = "images/crushed_brick.png";
    private final String WATER_IMAGE_NAME = "images/water_full.jpg";
    private final String ROCK_IMAGE_NAME = "images/rocks_full.jpg";
    private final String CRUSHED_ROCK_IMAGE_NAME = "images/crushed_rock.png";
    private final String GROUND_IMAGE_NAME = "images/ground_full.jpg";
    private final String T34_IMAGE_NAME = "images/t-34.png";
    private final String BT7_IMAGE_NAME = "images/bt7-4.png";
    private final String TIGER_IMAGE_NAME = "images/tiger-4.png";

    public Image iEagle;
    public Image iBrick;
    public Image iCrushedBrick;
    public BufferedImage iWater;
    public Image iRock;
    public Image iCrushedRock;
    public Image iGround;
    public Image iT34;
    public Image iBt7;
    public Image iTiger;

    public ImageLoader() {
        try {
            loadImagesFromJar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImagesFromDir() throws IOException {

        iEagle = ImageIO.read(new File(EAGLE_IMAGE_NAME));
        iBrick = ImageIO.read(new File(BRICK_IMAGE_NAME));
        iCrushedBrick = ImageIO.read(new File(CRUSHED_BRICK_IMAGE_NAME));
        iWater = ImageIO.read(new File(WATER_IMAGE_NAME));
        iRock = ImageIO.read(new File(ROCK_IMAGE_NAME));
        iCrushedRock = ImageIO.read(new File(CRUSHED_ROCK_IMAGE_NAME));
        iGround = ImageIO.read(new File(GROUND_IMAGE_NAME));
        iT34 = ImageIO.read(new File(T34_IMAGE_NAME));
        iBt7 = ImageIO.read(new File(BT7_IMAGE_NAME));
        iTiger = ImageIO.read(new File(TIGER_IMAGE_NAME));

    }

    private void loadImagesFromJar() throws IOException {

        iEagle = ImageIO.read(getImageURL(EAGLE_IMAGE_NAME));
        iBrick = ImageIO.read(getImageURL(BRICK_IMAGE_NAME));
        iCrushedBrick = ImageIO.read(getImageURL(CRUSHED_BRICK_IMAGE_NAME));
        iWater = ImageIO.read(getImageURL(WATER_IMAGE_NAME));
        iRock = ImageIO.read(getImageURL(ROCK_IMAGE_NAME));
        iCrushedRock = ImageIO.read(getImageURL(CRUSHED_ROCK_IMAGE_NAME));
        iGround = ImageIO.read(getImageURL(GROUND_IMAGE_NAME));
        iT34 = ImageIO.read(getImageURL(T34_IMAGE_NAME));
        iBt7 = ImageIO.read(getImageURL(BT7_IMAGE_NAME));
        iTiger = ImageIO.read(getImageURL(TIGER_IMAGE_NAME));

    }

    private URL getImageURL (String imageName) throws IOException {
        URL url = ImageLoader.class.getClassLoader().getResource(imageName);
        return url;
    }
}
