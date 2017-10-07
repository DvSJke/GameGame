package game;

import game.Input.InputHandler;
import game.graphics.RendererGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas{
    private static final long serialVersionUID = 1L;

//ПЕРЕМЕННЫЕ С РАЗМЕРАМИ ДЛЯ ОКНА
    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3;
    private String title = "Game";


    private Thread thread;
    private boolean running = false;
    private BufferStrategy bs = null;
    private JFrame frame = new JFrame();
    private RendererGame renderer;
    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int x = 0, y = 0;
//ОСНОВНОЙ МЕТОД КЛАССА С ОПИСАНИЕМ ОКНА
    public Game() {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);
        renderer = new RendererGame(width, height, pixels);
        frame.addKeyListener(new InputHandler());
}

//МЕТОД СТАРТА
    private synchronized void start(){
        running = true;
        thread = new Thread(() -> {
            init();
            long jvmLastTime = System.nanoTime();
            long timer = System.currentTimeMillis();
            double jvmPartTime = 1_000_000_000.0 / 60.0;
            double delta = 0;
            int updates = 0;
            int frames = 0;
            while(running){
                long jvmNow = System.nanoTime();
                delta += (jvmNow - jvmLastTime);
                jvmLastTime = jvmNow;
                if (delta >= jvmPartTime) {
                    update();
                    updates++;
                    delta = 0;
                }
                render();
                frames++;
                if(System.currentTimeMillis() - timer > 1000){
                    timer += 1000;
                    frame.setTitle(title + " | " + "Updates: " + updates + ", " + "Frames: " + frames);
                    updates = 0;
                    frames = 0;
                }
            }
        });
        thread.start();
    }

//МЕТОД ОСТАНОВКИ
    private synchronized void stop() throws InterruptedException {
        running = false;
        thread.join();
    }

//ИНИЦИАЛИЗАЦИЯ ПАРАМЕТРОВ
    private  void init(){
        frame.setResizable(false);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

//ОСНОВНОЙ ЦИКЛ ИГРЫ
  //  public void run() {
// ПЕРЕНЕСЕНО В ЛЯМБДУ start()
    //}

//РЕНДЕРИНГ
    private void render(){
        if (bs == null) {
            createBufferStrategy(3);
            bs = getBufferStrategy();
        }
        renderer.clear();
        renderer.renderGame(x, y);
        renderer.renderPerson(x, y);
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

//ОБНОВЛЕНИЕ КАДРОВ
    private void update() {
        if(InputHandler.isKeyPressed(KeyEvent.VK_RIGHT)) x++;
        if(InputHandler.isKeyPressed(KeyEvent.VK_LEFT)) x--;
        if(InputHandler.isKeyPressed(KeyEvent.VK_ENTER)) {
            renderer.fill();
        };


        if(InputHandler.isKeyPressed(KeyEvent.VK_SPACE) && y > -100)  {
            y--;
            System.out.println(y);
            }
        if(!InputHandler.isKeyPressed(KeyEvent.VK_SPACE) && y !=0) {
            y++;
            System.out.println(y);
        }
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
