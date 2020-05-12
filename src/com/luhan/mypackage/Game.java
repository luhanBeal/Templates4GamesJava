package com.luhan.mypackage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {

    public boolean isRunning;
    private Thread thread;
            //canvas and frame
    public static JFrame frame;
    private final int WIDTH = 140;
    private final int HEIGHT = 80;
    private final int SCALE = 5;
            //Background
    private BufferedImage image;
            //add var Spritesheet
    private Spritesheet sheet;
            //ADD player on the sheet
    private BufferedImage[] player;
    // quantos frames para animar o personagem
    private int frames=0, maxFrames=10;
    private int curAnimation=0, maxAnimations=4;


    public Game() {
        sheet = new Spritesheet("/spritesheet.png");
        player = new BufferedImage[4];
        player[0] = sheet.getSprite(0,0,10,10);
        player[1] = sheet.getSprite(10,0,10,10);
        player[2] = sheet.getSprite(0,0,10,10);
        player[3] = sheet.getSprite(20,0,10,10);
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        initFrame();
        // background
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    // metodo chamado pelo construtor pra ficar mais organizado
    public void initFrame() {
        frame = new JFrame("Meu jogo #1");
        frame.add(this);
        //usuario nao pode redimensionar janela
        frame.setResizable(false);
        //? not sure - setar dimensoes??
        frame.pack();
        // janela no centro
        frame.setLocationRelativeTo(null);
        //terminar a operação quando fechar
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set visible
        frame.setVisible(true);
    }

    //Synchronized é avançado mas recomendado para jogos!!!!!!!!!!11
    public synchronized void start() {
        isRunning = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

            // pode ser Tick ou outro nome - serve para atualizar o jogo!!!
            public void update() {
                frames ++;
                if (frames > maxFrames) {
                    frames = 0;
                    curAnimation++;
                    if (curAnimation >= maxAnimations) {
                        curAnimation = 0;
                    }
                }
            }

            public void render() {
                BufferStrategy bs = this.getBufferStrategy();
                if (bs == null) {
                    this.createBufferStrategy(3);
                    return;
                }
                // criar um retangulo

                Graphics g = image.getGraphics();
                //Background
                g.setColor(new Color(0,0,255));
                g.fillRect(0,0,WIDTH,HEIGHT);

                /* Renderização do jogo!!   */
                // casting g
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(player[curAnimation], 30,30, null);
                g.drawImage(player[1], 20, 20, null);


                /***********************/
                // limpar dados no jogo!
                g.dispose();
                g = bs.getDrawGraphics();
                g.drawImage(image,0,0,WIDTH*SCALE, HEIGHT*SCALE, null);
                bs.show();
            }

            //thread
            @Override
            public void run() {
                // pegar o FPS em nanoSegundos, long pq é um numero grande!
                long lastTime = System.nanoTime();
                double amountOfTicks = 60.0;
                double ns = 1000000000 / amountOfTicks;
                double delta = 0;
                int frames = 0;
                double timer = System.currentTimeMillis();

                while(isRunning) {
                    long now = System.nanoTime();
                    delta += (now - lastTime) / ns;
                    lastTime = now;
                    if (delta >= 1) {
                        update();
                        render();
                        /***********************
                         * Para trackear os frames!!!!!
                         ************************/
                        frames++;
                        delta--;
                    }

                    if (System.currentTimeMillis() - timer >= 1000) {
                        System.out.println("FPS: " + frames);
                        frames = 0;
                        timer += 1000;
                    }
                }
                stop();
            }

}

