package black.alias.pixelpie.sprite;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import black.alias.pixelpie.*;

public class Sprite {

	public int pixFrames, pixWidth, waitFrames, currentFrame, currentWait;
	public boolean hasIlum;
	public PImage sprite, IlumSprite;
	public float[] IlumMap;
	final PixelPie pie;

	/**
	 * Constructor.
	 * @param frames
	 * @param fps
	 * @param flipX
	 * @param flipY
	 * @param colormap
	 * @param ilummap
	 * @param pie
	 */
	public Sprite(int frames, int fps, boolean flipX, boolean flipY, String colormap, String ilummap, PixelPie pie) {
		
		// Keep reference to PixelPie.
		this.pie = pie;
		sprite = pie.app.loadImage(colormap);
		pixFrames = PApplet.constrain(frames, 1, 999) - 1;

		// Flip the sprite if required.
		if (flipX) {
			sprite = flipX(sprite, frames);
		}
		if (flipY) {
			sprite = flipY(sprite);
		}

		// Check if animated.
		// Single frame.
		if (pixFrames == 0) {
			pixWidth = sprite.width;
		}

		// Animated.
		else {

			// Test if dimensions are correct. (Width/frames should result in an integer)
			if (sprite.width % frames == 0) {
				pixWidth = sprite.width / frames;
			} else {
				pie.log.printlg("Image " + pie.app.dataPath(colormap) + " has incorrect width for amount of frames.");
			}

			// Determine wait time before each frame.
			waitFrames = PApplet.constrain(Math.round(pie.frameRate / fps), 1, 100) - 1;
		}

		// If it has an IlumMap...
		if ((ilummap != null) && !ilummap.isEmpty()) {

			// Load the IlumMap.
			IlumSprite = pie.app.loadImage(ilummap);
			
			// Check if the IlumMap is real or a hoax.
			if (IlumSprite != null) {

				// If the IlumMap is the same size as the image...
				if (IlumSprite.pixels.length == sprite.pixels.length) {

					// Set hasIlum to true.
					hasIlum = true;

					// Resize the IlumMap array to the size of the image.
					IlumMap = new float[IlumSprite.pixels.length];

					// Record the brightness of each pixel.
					for (int i = 0; i < IlumSprite.pixels.length; i++) {
						IlumMap[i] = (IlumSprite.pixels[i] & 0xFF) / 255.0f;
					}

					// ...Else, report the error in IlumMap size.
				} else {
					pie.log.printlg("IlumMap " + ilummap + " is not the same size as parent sprite.");
				}
				
			// ...Else, report the file missing.
			} else {
				pie.log.printlg("IlumMap " + ilummap + " does not exist.");
			}
		}
	}
	
	/**
	 * Flip image horizontally.
	 * @param img
	 * @param frames
	 * @return
	 */
	private PImage flipX (PImage img, int frames) {
		PImage reverse = pie.app.createImage(img.width, img.height, PConstants.ARGB);
		int frameWidth = Math.round(img.width/frames);
		for (int i = 0; i < frames; i++) {
			for (int x = 0; x < frameWidth; x++) {
				for (int y = 0; y < img.height; y++) {
					reverse.pixels[(frameWidth * i) + x + (y * img.width)] = img.pixels[(frameWidth * i) + (frameWidth - x - 1) + (y * img.width)];
				}
			}
		}
		return reverse;
	}

	/**
	 * Flip image vertically.
	 * @param img
	 * @return
	 */
	private PImage flipY(PImage img) {
		PImage reverse = pie.app.createImage(img.width, img.height, PConstants.ARGB);
		for (int x = 0; x < img.width; x++) {
			for (int y = 0; y < img.height; y++) {
				reverse.pixels[x + y * img.width] = img.pixels[x + (img.height - y - 1) * img.width];
			}
		}
		return reverse;
	}
}
