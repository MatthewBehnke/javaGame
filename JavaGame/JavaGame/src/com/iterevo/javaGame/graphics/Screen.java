package com.iterevo.javaGame.graphics;

public class Screen {

	private int width, height;
	public int [] pixels;
	
	int xTime = 100, yTime = 50;
	int counter = 0;
	
	public Screen(int width,int height){
		this.width = width;
		this.height = height;
		pixels = new int [width * height];// 50,400
	}
	
	public void clear(){
		for (int i = 0; i < pixels.length; i++){
			pixels[i] = 0;
		}
	}
	
	public void render(){
		counter ++;
		if(counter % 10 == 0) xTime--;
		if(counter % 100 == 0) yTime--;
		
		for(int y = 0; y < height; y++){
			if(yTime < 0 || yTime >= height)break;
			for(int x = 0; x< width; x++){
				if (xTime < 0 || xTime >= width)break;
				pixels[xTime + yTime * width] = 0xff00ff;
			}
		}
	}
	
}
