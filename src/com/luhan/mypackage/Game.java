package com.luhan.mypackage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

    public boolean isRunning;
    private Thread thread;
            //canvas and frame
    public static JFrame frame;
    private final int WIDTH = 240;
    private final int HEIGHT = 160;
    private final int SCALE = 3;

    public Game() {
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        initFrame();
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

            }

            public void render() {
                BufferStrategy bs = this.getBufferStrategy();
                if (bs == null) {
                    this.createBufferStrategy(3);
                    return;
                }
                // criar um retangulo
                Graphics g = bs.getDrawGraphics();
                g.setColor(Color.black);
                g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
                g.setColor(new Color(20,19,50));
                g.fillOval(10, 10, 50, 50);
                g.setColor(new Color(255,190,190));
                g.fillRoundRect(250, 20, 50, 50, 120, 120);
                //Strings
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.white);
                g.drawString("Olá Jogo!", 100,60);
                bs.show();
            }

            //thread
            @Override
            public void run() {
                // pegar o FPS em nanoSegundos, long pq é um numero grande!
                long lastTime = System.nanoTime();
                double amountOfTicks = 60.0;
                double ns = 100000000 / amountOfTicks;
                double delta = 0;
//                int frames = 0;
//                double timer = System.currentTimeMillis();

                while(isRunning) {
                    long now = System.nanoTime();
                    delta += (now - lastTime) / ns;
                    lastTime = now;
                    if (delta >= 1) {
                        update();
                        render();
                        /***********************
                         * Para trackear os frames está comentado!!!!!
                         ************************/
//                        frames++;
//                        delta--;
                    }

//                    if (System.currentTimeMillis() - timer >= 1000) {
//                        System.out.println("FPS: " + frames);
//                        frames = 0;
//                        timer += 1000;
//                    }
                    /*************
                     * Aqui segue outra maneira para limitar os frames!
                     ************/
//                    try {
//                        thread.sleep(1000/60);
//                    } catch(InterruptedException e) {
//                        e.printStackTrace();
//                    }

                }
                stop();
            }

}

