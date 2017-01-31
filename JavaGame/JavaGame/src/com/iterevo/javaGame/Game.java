package com.iterevo.javaGame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.iterevo.javaGame.graphics.Screen;

public class Game extends Canvas  implements Runnable {
	private static final long serialVersionUID = 1L;
	
	
	public static int width = 300;
	public static int height = width / 16 * 9;//168
	public static int scale = 3;
	public static String title = "Rain";
	
	private Thread gameThread;
	private JFrame frame;
	private boolean isRunning = false;
	
	private Screen screen;
	
	private BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels =((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public Game(){
		Dimension size = new Dimension(width*scale,height*scale);
		setPreferredSize(size);
		
		screen = new Screen(width,height);
		
		frame = new JFrame();
	}
	
	public synchronized void start(){
		isRunning = true;
		gameThread = new Thread(this,"Display");
		gameThread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try{
			gameThread.join();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}	
	}
	
	public void run(){
		long lastTime = System.nanoTime();
		long timber = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;// 60.0 is the update rate.
		double delta = 0;
		int frames = 0;
		int updates = 0;
		while(isRunning){
			long now = System.nanoTime();
			delta += (now-lastTime) / ns;
			lastTime = now;
			while (delta >= 1){
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timber > 1000){
				timber += 1000;
				System.out.println(updates + "ups," + frames + "fps");
				frame.setTitle(title + " | " + updates + " updates per second, " + frames + "fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void update(){}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if (bs == null){
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();	
		screen.render();
		
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,getWidth(),getHeight(),null);
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args){
		Game game = new Game();
		game.frame.setResizable(false);
//		game.frame.setTitle();
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}
}
