/*
 * Copyright (c) 2024, Miracle-Masterpiсe <mrmiraclemasterpiece@gmail.com or https://t.me/MiracleMasterpiece>. All rights reserved.
 * Use is subject to license terms.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * */
package nw4j.helpers.misc;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

import nw4j.helpers.Helpers;
import nw4j.helpers.NativeType;
import nw4j.helpers.misc.TexturePacker.IUV;
import nw4j.helpers.misc.TexturePacker.Node;
import nw4j.wrapper.c.allocators.MemoryAccessor;
import nw4j.wrapper.c.pointers.VoidPointer;

import static nw4j.wrapper.c.allocators.MemoryAccessor.*;

/**Класс для загрузки изображений и сборки их в одно больше изображение(атлас).*/
public class TexturePackerLoader implements Closeable, AutoCloseable {

	/**Директория с изображениями*/
	private File dir;
	
	/**Объект упаковщика изображений, который генерирует координаты регионов в которых хранятся изображения.*/
	private TexturePacker packer;
	
	/**Данные атласа.*/
	private int[] data;
	
	/**Список изображений.*/
	private List<ImageData> images;
	
	/**Высота и ширина атласа.*/
	private int w, h;
	
	/**Нативная память для хранения атласа.*/
	private @NativeType("char*") long nativePixels;
	
	/**Прямой байтовый буфер для хранения атласа.*/
	private ByteBuffer buffer;
	
	/**Карта, которая хранит имя изображения файла и текстурные координаты для него.*/
	private HashMap<String, IUV> uvs;
	
	/**
	 * @param width ширина атласа.
	 * @param height высота атласа.
	 * @param pathToImages путь до файла с изображениями.
	 * */
	public TexturePackerLoader(int width, int height, String pathToImages) {
		if (width <= 0 || height <= 0) throw new RuntimeException(new IllegalArgumentException("Width or height <= 0!"));
		if (pathToImages == null || pathToImages.isEmpty()) throw new RuntimeException(new IllegalArgumentException("Path to image equal null or empty!")); 
		
		dir = new File(pathToImages);
		if (!dir.exists()) throw new RuntimeException("Dir not exists!");
		
		images 	= new ArrayList<>();
		data 	= new int[width * height];
		packer 	= new TexturePacker(width, height);
		uvs 	= new HashMap<>();
		this.w 	= width;
		this.h	= height;
	}
	
	
	public void putImage(String name, BufferedImage img) {
		images.add(new ImageData(img, name));
	}
	
	public void loadImages() {
		File[] dirWithImages = dir.listFiles(new ImageFileFilter());
		
		for (File imgFile : dirWithImages) {
			try {
				BufferedImage img = ImageIO.read(imgFile);
				images.add(new ImageData(img, imgFile.getName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		images.sort(new ImageDataSorter());

		for (ImageData imgdata : images) {
			BufferedImage img = imgdata.getImage();
			int w = img.getWidth();
			int h = img.getHeight();
			int[] data = new int[w * h];
			img.getRGB(0, 0, w, h, data, 0, w);
			Node n = packer.put(w, h, data);
			if (n != null) {
				uvs.put(imgdata.getName(), n.getRectangle().convertUV());
			}
		}
		
		pack();
	}
	
	public int getWidth() {
		return w;
	}
	
	public int getHegiht() {
		return h;
	}
	
	private void pack() {
		for (TexturePacker.Node n : packer) {
			if (n.getID() == 0) continue;
			
			TexturePacker.Rect rect = n.getRectangle();
			int[] pixels = n.getPixels();
			int xo = rect.x;
			int yo = rect.y;
			int w = rect.w;
			int h = rect.h;
			
			for (int x = 0; x < w; ++x) {
				for (int y = 0; y < h; ++y) {
					int pixel = pixels[x + (y * w)];
					data[indexAt(x + xo, y + yo)] = pixel;
				}
			}
			
		}
	}
	
	public void save(String outputImageName) {
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, w, h, data, 0, w);
		File outputfile = new File(outputImageName + w + "x" + h + ".png");
		try {
			ImageIO.write(img, new String(new char[] {'p', 'n', 'g'}), outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ByteBuffer getBytes() {
		if (buffer == null) {
			allocByteBuf();
			int len = data.length;
			for (int i = 0; i < len; ++i) {
				int pixel = data[i];
				int a = (pixel >> 24) & 255;
				int r = (pixel >> 16) & 255;
				int g = (pixel >> 8)  & 255;
				int b = (pixel >> 0)  & 255;
				buffer.put((byte)r).put((byte)g).put((byte)b).put((byte)a);
			}
			buffer.flip();
		}
		return buffer;
	}
	
	public @NativeType(" char* ") long getNativeBytes() {
		if (nativePixels == VoidPointer.NULL) {
			getBytes();
		}
		return nativePixels;
	}
	
	private void allocByteBuf() {
		final int len = (w * h) << 2;
		nativePixels = Helpers.addressNonNull(malloc(len));
		buffer = MemorySegment.ofAddress(nativePixels).reinterpret(len).asByteBuffer().order(ByteOrder.nativeOrder());
	}
	
	private int indexAt(int x, int y) {
		return x + (y * w);
	}
	
	public ByteBuffer getImage() {
		return buffer; 
	}
	
	public IUV getUVsAt(String imgName) {
		return uvs.get(imgName);
	}
	
	private static final class ImageFileFilter implements FileFilter{
		final String endWith = new String(new char[] {'.', 'p', 'n', 'g'});

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(endWith);
		}
	};
	
	private static final class ImageDataSorter implements Comparator<ImageData>{
		@Override
		public int compare(ImageData o1, ImageData o2) {
			int s0 = S(o1);
			int s1 = S(o2);
			if (s0 > s1) return -1;
			if (s0 < s1) return  1;
			return 0;
		}
		
		private int S(ImageData b) {
			return b.getImage().getWidth() * b.getImage().getHeight();
		}
	};
	
	private static final class ImageData implements Cloneable{
		private final BufferedImage img;
		private final String name;
		
		public ImageData(BufferedImage img, String name) {
			this.img = img;
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public BufferedImage getImage() {
			return img;
		}
		
		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			return name.equals(obj);
		}
		
		@Override
		public String toString() {
			return name + ": " + img.toString();
		}
	}

	@Override
	public void close() {
		MemoryAccessor.free(nativePixels);
		nativePixels = VoidPointer.NULL;
		buffer 	= null;
		data 	= null;
		images 	= null;
	}
	
	public void destroy() {
		close();
	}
	
	public void free() {
		close();
	}
}
