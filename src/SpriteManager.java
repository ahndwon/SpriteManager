import processing.core.PApplet;
import processing.core.PImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SpriteManager {
    private static HashMap<Integer, ArrayList<PImage>> sprites = new HashMap<>();

    private SpriteManager() {
    }

    public static void loadImage(
            PApplet pApplet,
            int state, String path) {
        checkKey(state);

        PImage image = pApplet.loadImage(path);
        ArrayList<PImage> images = new ArrayList<>();
        images.add(image);

        sprites.put(state, images);
    }

    public static void loadImageByXY(PApplet pApplet, int state, String path, int x, int y, int width, int height) {
        checkKey(state);

        PImage image = pApplet.loadImage(path).get(x, y, width, height);
        ArrayList<PImage> images = new ArrayList<>();
        images.add(image);

        sprites.put(state, images);
    }

    public static void loadImage(
            PApplet pApplet,
            int state, String path,
            int row, int column,
            int sizeX, int sizeY) {
        checkKey(state);

        PImage image = pApplet.loadImage(path);

        int startX = column * sizeX;
        int startY = row * sizeY;

        ArrayList<PImage> images = new ArrayList<>();
        images.add(image.get(startX, startY, sizeX, sizeY));

        sprites.put(state, images);
    }

    public static void loadSprite(
            PApplet pApplet,
            int state, String path,
            int row, int column, int sizeX, int sizeY, int spriteCount) {
        checkKey(state);

        PImage image = pApplet.loadImage(path);

        int width = image.width;
        int height = image.height;
        int startX = column * sizeX;
        int startY = row * sizeY;

        ArrayList<PImage> images = new ArrayList<>();

        for (int i = 0; i < spriteCount; i++) {
            PImage img = image.get(startX, startY, sizeX, sizeY);
            images.add(img);
            startX += sizeX;

            if (startX + sizeX > width && i != spriteCount - 1) {
                startY += sizeY;
                startX = column * sizeX;
            }
            if (startY + sizeY > height)
                throw new IllegalArgumentException("Requested image exceeds existing image");
        }
        sprites.put(state, images);
    }

    public static void loadSprite(
            PApplet pApplet,
            int state, String path,
            int sizeX, int sizeY, int[] indices) {
        checkKey(state);

        PImage image = pApplet.loadImage(path);
        if (image == null) try {
            throw new IOException("NO IMAGE");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = 0;
        int height = 0;
        if (image != null) {
            width = image.width;
            height = image.height;

        }
        int columnCount = width / sizeX;

        ArrayList<PImage> images = new ArrayList<>();
        for (int index : indices) {
            int startX = (index % columnCount) * sizeX;
            int startY = (index / columnCount) * sizeY;
            assert image != null;
            images.add(image.get(startX, startY, sizeX, sizeY));

            if (startX + sizeX > width) {
                startY += sizeY;
                startX = (indices[0] % columnCount) * sizeX;
            }
            if (startY + sizeY > height)
                throw new IllegalArgumentException("Requested image exceeds existing image");
        }
        sprites.put(state, images);
    }

    public static void loadSprite(
            PApplet pApplet,
            int state, String path,
            int row, int column, int sizeX, int sizeY, int spriteCount, boolean reverse) {
        checkKey(state);

        PImage image = pApplet.loadImage(path);
        int width = spriteCount * sizeX;
        int height = image.height;
        int startX = column * sizeX;
        int startY = row * sizeY;

        ArrayList<PImage> images = new ArrayList<>();

        if (reverse) {
            System.out.println("reverse operating");
            startX = sizeX * (spriteCount - 2);
            startY = row * sizeY;
            for (int i = 0; i < spriteCount - 2; i++) {
                System.out.println("startX: " + startX + ", startY: " + startY + ", sizeX: " + sizeX + ", sizeY: " + sizeY);
                PImage img = image.get(startX, startY, sizeX, sizeY);
                images.add(img);
                startX -= sizeX;
            }
        }
        sprites.put(state, images);
    }

    public static ArrayList<PImage> getImages(int state) {
        checkKey(state);
        return sprites.get(state);
    }

    public static PImage getImage(int state, int index) {
        checkKey(state);
        ArrayList<PImage> images = sprites.get(state);
        return images.get(index % images.size());
    }

    public static PImage getImage(int state) {
        checkKey(state);
        if (sprites.get(state).size() != 1) {
            throw new IllegalArgumentException("add index");
        } else {
            ArrayList<PImage> images = sprites.get(state);
            return images.get(0);
        }
    }

    public static boolean checkExistence(int key) {
        return sprites.containsKey(key);
    }

    private static void checkKey(int state) {
        if (sprites.containsKey(state)) {
            throw new IllegalArgumentException("Image already loaded");
        }
    }

    private static void checkLoad(int state) {
        if (!sprites.containsKey(state))
            throw new IllegalArgumentException("LOAD IMAGE");
    }
}
