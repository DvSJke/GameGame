package game.graphics;


import java.util.Random;

public class RendererGame {

    private int width, height;
    private int pixels[] = null;



    private int tileIndex;
    private static  final int MAP_SIZE = 64;
    private static  final int MAP_SIZE_MASK = MAP_SIZE - 1;
    private int tiles[] = new int [MAP_SIZE * MAP_SIZE];
    private static final Random random = new Random();

    public RendererGame(int width, int height, int pixels[]){
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        fill();
    }

    public void fill() {
        for (int i = 0; i < MAP_SIZE * MAP_SIZE; i++) {
            tiles[i] = random.nextInt(0xffffff);
        }
    }

    public void renderPerson(int xOffset, int yOffset){
        for (int y = height / 2 - (MAP_SIZE / 8); y < height / 2 + (MAP_SIZE / 8); y++){
            int yy = y + yOffset;
            for (int x = width / 2 - (MAP_SIZE / 8); x < width / 2 + (MAP_SIZE / 8); x++){
                int xx = x + xOffset;
                tileIndex = ((yy >> 4) & MAP_SIZE_MASK) + (((xx >> 4) & MAP_SIZE_MASK) * MAP_SIZE);
                pixels[x + y * width] = 0;
            };
        }
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    public void renderGame(int xOffset, int yOffset){
        for (int y = 0; y < height; y++){
            int yy = y + yOffset;
            for (int x = 0; x < width; x++){
                int xx = x + xOffset;
                tileIndex = ((yy >> 4) & MAP_SIZE_MASK) + (((xx >> 4) & MAP_SIZE_MASK) * MAP_SIZE);
                pixels[x + y * width] = tiles[tileIndex];
            };
        }
    }
}
