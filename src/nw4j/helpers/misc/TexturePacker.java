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
import java.util.Iterator;
import java.util.LinkedList;
import nw4j.helpers.NativeType;
import static nw4j.wrapper.c.allocators.MemoryAccessor.*;

/**
 * Класс создаёт регионы изображений, которые поступают в метод {@link TexturePacker#put(BufferedImage)} или {@link TexturePacker#put(int, int, int[])}
 * Так, чтобы из них можно было собрать текстурный атлас.
 * Данный класс поддеживает любые размеры изображений.
 * */
public class TexturePacker implements Iterable<TexturePacker.Node>{

	/**Корневой узел*/
	private Node root;

	/**
	 * 
	 * @param w - максимальная ширина региона
	 * @param h - максимальная высота региона
	 * */
	public TexturePacker(int w, int h) {
		if (w < 0 || h < 0) throw new IllegalArgumentException("Texture packer size can't may be < 0!");
		root = new Node(0, 0, w, h);
	}

	/**
	 * Вставляет массив, который представляет изображение, в упаковщик текстур.
	 * 
	 * @param xsize ширина изображения 
	 * @param ysize высота изображения
	 * @param data  данные изображения
	 * */
	public Node put(int width, int height, int[] data) {
		if (width > root.rect.w) throw new IllegalArgumentException("Width > max width! Width = " + width + ". Max width = " + root.rect.w);
		if (height > root.rect.h) throw new IllegalArgumentException("Height > max height! Height = " + height + ". Max height= " + root.rect.h);
		Node n = root.put(width, height);
		if (n != null) {
			n.setData(data);
		}
		return n;
	}

	public Node put(int width, int height, long pointerToData) {
		if (width > root.rect.w) throw new IllegalArgumentException("Width > max width! Width = " + width + ". Max width = " + root.rect.w);
		if (height > root.rect.h) throw new IllegalArgumentException("Height > max height! Height = " + height + ". Max height= " + root.rect.h);
		Node n = root.put(width, height);
		if (n != null) {
			n.setData(pointerToData);
		}
		return n;
	}

	/**
	 * Вставляет изображение в упаковщик текстур
	 * Если вставляемое изображение больше, чем максимальная ширина и высота структуры, то будет сгенерировано исключение.
	 * */
	public void put(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		int[] data = new int[w * h];
		img.getRGB(0, 0, w, h, data, 0, w);
		put(w, h, data);
	}

	/**
	 * Тип, указывающий координаты суб-региона.
	 * Грубо говоря значения являются текстурными координатами в пикселях.
	 * */
	public static class Rect{
		public final int x, y, w, h;
		Rect(int x, int y, int w, int h){
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		public IUV convertUV() {
			return new UV(x, y, x + w, y + h);
		}
	};

	public static class Node{
		/**Координаты и размеры узла*/
		private final Rect rect;

		/**Дочерние узлы*/
		private final Node[] childs;

		/**Идентификатор изображения*/
		private int imageID; 

		/**Данные изображения*/
		private int[] pixels;

		/**Указатель на данные изображения*/
		private long pointerToPixels;

		public Node(int x, int y, int w, int h) {
			this.childs = new Node[2];
			this.rect = new Rect(x, y, w, h);
		}

		void setData(int[] pixels) {
			imageID++;
			this.pixels = pixels; 
		}

		void setData(long pointerToData) {
			imageID++;
			this.pointerToPixels = pointerToData;
		}

		public int getID() {
			return imageID;
		} 

		public Rect getRectangle() {
			return rect;
		}

		public int[] getPixels() {
			return pixels;
		}

		public long getPointerToPixels() {
			return pointerToPixels;
		}

		private boolean isLeaf() {
			return childs[0] == null && childs[1] == null;
		}

		public Node put(int width, int height) {

			if (!isLeaf()) {
				Node n = childs[0].put(width, height);
				if (n != null) return n;
				return childs[1].put(width, height);
			}

			if(rect.w < width || rect.h < height || imageID != 0) {
				return null;
			} 

			if (rect.w == width && rect.h == height) {
				return this;
			}

			int dw = rect.w - width;
			int dh = rect.h - height;

			if (dw > dh) {
				childs[0] = new Node(rect.x, rect.y, 					width, rect.h);
				childs[1] = new Node(rect.x + width, rect.y, 			rect.w-width, rect.h);
			}else {
				childs[0] = new Node(rect.x, rect.y, 					rect.w, height);
				childs[1] = new Node(rect.x, rect.y + height, 			rect.w, rect.h-height);
			}		

			return childs[0].put(width, height);
		}
	}

	@Override
	public Iterator<Node> iterator() {
		return new NodeIterator(root);
	};

	/**
	 * Итератор для итерирования по дереву упаковщика текстур.
	 * Метод итерирования - обход в ширину.
	 * */
	private static class NodeIterator implements Iterator<Node>{

		private final LinkedList<Node> queue;

		public NodeIterator(Node root) {
			queue = new LinkedList<>();
			queue.add(root);
		}

		@Override
		public Node next() {
			Node n = queue.removeFirst();
			if (n.childs[0] != null) queue.addLast(n.childs[0]);
			if (n.childs[1] != null) queue.addLast(n.childs[1]);
			return n;
		}

		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}
	}

	public static interface IUV{
		/**
		 * Возвращает массив значений текстурных координат.
		 * 
		 * Индексы значений:
		 * u0 = 0,
		 * v0 = 1,
		 * u1 = 2,
		 * v1 = 3
		 * */
		int[] getUvs();

		void getUvs(
				@NativeType(" int* ") long u0, 
				@NativeType(" int* ") long v0, 
				@NativeType(" int* ") long u1, 
				@NativeType(" int* ") long v1
				);
		/**
		 * Возвращает массив значений текстурных координат в стиле OpenGL.
		 * @param w - ширина изображения.
		 * @param h - высота изображения.
		 * 
		 * 
		 * Индексы значений:
		 * u0 = 0,
		 * v0 = 1,
		 * u1 = 2,
		 * v1 = 3
		 * */
		float[] getUvsOpenGlStyle(int w, int h);

		void getUvsOpenGlStyle( int w, int h,
				@NativeType(" float* ") long u0, 
				@NativeType(" float* ") long v0, 
				@NativeType(" float* ") long u1, 
				@NativeType(" float* ") long v1);
	}

	private static class UV implements IUV{
		private final int u0, v0, u1, v1;

		public UV(int u0, int v0, int u1, int v1) {
			this.u0 = u0;
			this.v0 = v0;
			this.u1 = u1;
			this.v1 = v1;
		}

		@Override
		public int[] getUvs() {
			return new int[] {u0, v0, u1, v1};
		}

		@Override
		public float[] getUvsOpenGlStyle(int w, int h) {
			float fw = w;
			float fh = h;
			return new float[] {u0 / fw, v0 / fh, u1 / fw, v1 / fh};
		}

		@Override
		public String toString() {
			return "[" + u0 + ", " + v0 + ", " + u1 + ", " + v1 + "]";
		}

		@Override
		public void getUvs(@NativeType("int*") long u0, @NativeType("int*") long v0, @NativeType("int*") long u1, @NativeType("int*") long v1) {
			setInt(u0, this.u0);
			setInt(v0, this.v0);
			setInt(u1, this.u1);
			setInt(v1, this.v1);
		}

		@Override
		public void getUvsOpenGlStyle(int w, int h, @NativeType("float*") long u0, @NativeType("float*") long v0, @NativeType("float*") long u1, @NativeType("float*") long v1) {
			setFloat(u0, this.u0 / (float)w);
			setFloat(v0, this.v0 / (float)h);
			setFloat(u1, this.u1 / (float)w);
			setFloat(v1, this.v1 / (float)h);
		}
	}

}