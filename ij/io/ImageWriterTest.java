package ij.io;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.image.ColorModel;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import ij.VirtualStack;
import ij.io.Assert;

/*
	    for each test
	      create a file info
	      populate the correct image data from an image (long[][])
	      call writeImage() to a byte stream
	      compare byte stream to expectedBytes()
	        this is affected by Intel byte order	      
	        
	 TODO
	   intel byte orders - will affect various PixelFormat pixelsFromBytes() routines
	   virtual stacks - will need to collect some data first and then implement tests
	   refactor code below to make it simpler
 */

public class ImageWriterTest {

	//  *******    Helper methods  ****************************
	
	byte[] concat(byte[] a, byte[] b)
	{
		byte[] output = new byte[a.length + b.length];
		
		System.arraycopy(a, 0, output, 0, a.length);
		System.arraycopy(b, 0, output, a.length, b.length);
		
		return output;
	}
	
	short[] concat(short[] a, short[] b)
	{
		short[] output = new short[a.length + b.length];
		
		System.arraycopy(a, 0, output, 0, a.length);
		System.arraycopy(b, 0, output, a.length, b.length);
		
		return output;
	}
	
	int[] concat(int[] a, int[] b)
	{
		int[] output = new int[a.length + b.length];
		
		System.arraycopy(a, 0, output, 0, a.length);
		System.arraycopy(b, 0, output, a.length, b.length);
		
		return output;
	}
	
	float[] concat(float[] a, float[] b)
	{
		float[] output = new float[a.length + b.length];
		
		System.arraycopy(a, 0, output, 0, a.length);
		System.arraycopy(b, 0, output, a.length, b.length);
		
		return output;
	}

	//  *******    Tests     *************************************************

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConstructor() {
		FileInfo fi = new FileInfo();
		assertNotNull(new ImageWriter(fi));
	}

	private void tryNoPixAndNoVirtStack()
	{
		try {
			FileInfo fi = new FileInfo();
			fi.pixels = null;
			fi.virtualStack = null;
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageWriter writer = new ImageWriter(fi);
			writer.write(stream);
			fail();
		}
		catch (IOException e)
		{
			assertTrue(true);
		}
	}

	private void tryBadPix()
	{
		try {
			FileInfo fi = new FileInfo();
			fi.nImages = 2;
			fi.virtualStack = null;
			fi.pixels = "Any Object not instanceof Object[]";
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageWriter writer = new ImageWriter(fi);
			writer.write(stream);
			fail();
		}
		catch (IOException e)
		{
			assertTrue(true);
		}
	}
	
	// TODO - write this
	private void tryColor8BitVirtualStack() {
		// if (fi.nImages>1 && fi.virtualStack!=null)
		FileInfo fi = new FileInfo();
		fi.intelByteOrder = false;
		fi.fileType = FileInfo.COLOR8;
		fi.nImages = 2;
		fi.virtualStack = new VirtualStack();
	}
	
	private void tryColor8BitStack() {
		for (long[][][] imageSet : Images.ImageSets)
		{
			PixelFormat format = new Color8Format();
			byte[] expectedAfterWrite = new byte[] {};
			byte[][] imageStack = new byte[imageSet.length][];
			for (int i = 0; i < imageSet.length; i++)
			{
				byte[] expected = (byte[])(format.expectedResults(imageSet[i])); 
				imageStack[i] = expected;
				expectedAfterWrite = concat(expectedAfterWrite,expected);
			}
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.COLOR8;
			fi.nImages = imageSet.length;
			fi.virtualStack = null;
			fi.height = imageSet[0].length;
			fi.width = imageSet[0][0].length;
			fi.pixels = imageStack;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				byte[] actual = (byte[])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expectedAfterWrite,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryColor8BitImage() {
		for (long[][] image : Images.Images)
		{
			PixelFormat format = new Color8Format();
			byte[] expected = (byte[])(format.expectedResults(image)); 
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.COLOR8;
			fi.nImages = 1;
			fi.virtualStack = null;
			fi.height = image.length;
			fi.width = image[0].length;
			fi.pixels = expected;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				byte[] actual = (byte[])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expected,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryColor8() {
		tryColor8BitVirtualStack();
		tryColor8BitStack();
		tryColor8BitImage();
	}
	
	// TODO - write this
	private void tryGray8BitVirtualStack() {
		// if (fi.nImages>1 && fi.virtualStack!=null)
		// sub-case:	write8BitVirtualStack(out, fi.virtualStack);
		FileInfo fi = new FileInfo();
		fi.intelByteOrder = false;
		fi.fileType = FileInfo.GRAY8;
	}
	
	private void tryGray8BitStack() {
		for (long[][][] imageSet : Images.ImageSets)
		{
			PixelFormat format = new Gray8Format();
			byte[] expectedAfterWrite = new byte[] {};
			byte[][] imageStack = new byte[imageSet.length][];
			for (int i = 0; i < imageSet.length; i++)
			{
				byte[] expected = (byte[])(format.expectedResults(imageSet[i])); 
				imageStack[i] = expected;
				expectedAfterWrite = concat(expectedAfterWrite,expected);
			}
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.GRAY8;
			fi.nImages = imageSet.length;
			fi.virtualStack = null;
			fi.height = imageSet[0].length;
			fi.width = imageSet[0][0].length;
			fi.pixels = imageStack;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				byte[] actual = (byte[])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expectedAfterWrite,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryGray8BitImage() {
		for (long[][] image : Images.Images)
		{
			PixelFormat format = new Gray8Format();
			byte[] expected = (byte[])(format.expectedResults(image)); 
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.GRAY8;
			fi.nImages = 1;
			fi.virtualStack = null;
			fi.height = image.length;
			fi.width = image[0].length;
			fi.pixels = expected;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				byte[] actual = (byte[])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expected,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryGray8() {
		tryGray8BitVirtualStack();
		tryGray8BitStack();
		tryGray8BitImage();
	}
	
	// TODO - write this
	private void tryGray16SignedVirtualStack() {
		// if (fi.nImages>1 && fi.virtualStack!=null)
		// sub-case: write16BitVirtualStack(out, fi.virtualStack);
		FileInfo fi = new FileInfo();
		fi.intelByteOrder = false;
		fi.fileType = FileInfo.GRAY16_SIGNED;
	}
	
	private void tryGray16SignedStack() {
		for (long[][][] imageSet : Images.ImageSets)
		{
			PixelFormat format = new Gray16SignedFormat();
			short[] expectedAfterWrite = new short[] {};
			short[][] imageStack = new short[imageSet.length][];
			for (int i = 0; i < imageSet.length; i++)
			{
				short[] expected = (short[])(format.expectedResults(imageSet[i])); 
				imageStack[i] = expected;
				expectedAfterWrite = concat(expectedAfterWrite,expected);
			}
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.GRAY16_SIGNED;
			fi.nImages = imageSet.length;
			fi.virtualStack = null;
			fi.height = imageSet[0].length;
			fi.width = imageSet[0][0].length;
			fi.pixels = imageStack;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				short[] actual = (short[])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expectedAfterWrite,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryGray16SignedImage() {
		for (long[][] image : Images.Images)
		{
			PixelFormat format = new Gray16SignedFormat();
			short[] expected = (short[])(format.expectedResults(image)); 
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.GRAY16_SIGNED;
			fi.nImages = 1;
			fi.virtualStack = null;
			fi.height = image.length;
			fi.width = image[0].length;
			fi.pixels = expected;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				short[] actual = (short[])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expected,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryGray16Signed() {
		tryGray16SignedVirtualStack();
		tryGray16SignedStack();
		tryGray16SignedImage();
	}
	
	// TODO - write this
	private void tryGray16UnsignedVirtualStack() {
		// if (fi.nImages>1 && fi.virtualStack!=null)
		// sub-case: write16BitVirtualStack(out, fi.virtualStack);
		FileInfo fi = new FileInfo();
		fi.intelByteOrder = false;
		fi.fileType = FileInfo.GRAY16_UNSIGNED;
	}
	
	private void tryGray16UnsignedStack() {
		for (long[][][] imageSet : Images.ImageSets)
		{
			PixelFormat format = new Gray16UnsignedFormat();
			short[] expectedAfterWrite = new short[] {};
			short[][] imageStack = new short[imageSet.length][];
			for (int i = 0; i < imageSet.length; i++)
			{
				short[] expected = (short[])(format.expectedResults(imageSet[i])); 
				imageStack[i] = expected;
				expectedAfterWrite = concat(expectedAfterWrite,expected);
			}
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.GRAY16_UNSIGNED;
			fi.nImages = imageSet.length;
			fi.virtualStack = null;
			fi.height = imageSet[0].length;
			fi.width = imageSet[0][0].length;
			fi.pixels = imageStack;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				short[] actual = (short[])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expectedAfterWrite,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryGray16UnsignedImage() {
		for (long[][] image : Images.Images)
		{
			PixelFormat format = new Gray16UnsignedFormat();
			short[] expected = (short[])(format.expectedResults(image)); 
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.GRAY16_UNSIGNED;
			fi.nImages = 1;
			fi.virtualStack = null;
			fi.height = image.length;
			fi.width = image[0].length;
			fi.pixels = expected;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				short[] actual = (short[])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expected,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryGray16Unsigned() {
		tryGray16UnsignedVirtualStack();
		tryGray16UnsignedStack();
		tryGray16UnsignedImage();
	}
	
	// TODO - write this
	private void tryRgbVirtualStack() {
		// if (fi.nImages>1 && fi.virtualStack!=null)
		// sub-case: writeRGBVirtualStack(out, fi.virtualStack);
		FileInfo fi = new FileInfo();
		fi.fileType = FileInfo.RGB;
	}
	
	private void tryRgbStack() {
		for (long[][][] imageSet : Images.ImageSets)
		{
			PixelFormat format = new RgbFormat();
			int[] expectedAfterWrite = new int[] {};
			int[][] imageStack = new int[imageSet.length][];
			for (int i = 0; i < imageSet.length; i++)
			{
				int[] expected = (int[])(format.expectedResults(imageSet[i])); 
				imageStack[i] = expected;
				expectedAfterWrite = concat(expectedAfterWrite,expected);
			}
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.RGB;
			fi.nImages = imageSet.length;
			fi.virtualStack = null;
			fi.height = imageSet[0].length;
			fi.width = imageSet[0][0].length;
			fi.pixels = imageStack;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				int[] actual = (int[])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expectedAfterWrite,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryRgbImage() {
		for (long[][] image : Images.Images)
		{
			PixelFormat format = new RgbFormat();
			int[] expected = (int[])(format.expectedResults(image)); 
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.RGB;
			fi.nImages = 1;
			fi.virtualStack = null;
			fi.height = image.length;
			fi.width = image[0].length;
			fi.pixels = expected;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				int[] actual = (int[])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expected,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryRgb() {
		tryRgbVirtualStack();
		tryRgbStack();
		tryRgbImage();
	}
	
	// TODO - write this
	private void tryGray32FloatVirtualStack() {
		// if (fi.nImages>1 && fi.virtualStack!=null)
		// sub-case: writeFloatVirtualStack(out, fi.virtualStack);
		FileInfo fi = new FileInfo();
		fi.intelByteOrder = false;
		fi.fileType = FileInfo.GRAY32_FLOAT;
	}
	
	private void tryGray32FloatStack() {
		for (long[][][] imageSet : Images.ImageSets)
		{
			PixelFormat format = new Gray32FloatFormat();
			float[] expectedAfterWrite = new float[] {};
			float[][] imageStack = new float[imageSet.length][];
			for (int i = 0; i < imageSet.length; i++)
			{
				float[] expected = (float[])(format.expectedResults(imageSet[i])); 
				imageStack[i] = expected;
				expectedAfterWrite = concat(expectedAfterWrite,expected);
			}
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.GRAY32_FLOAT;
			fi.nImages = imageSet.length;
			fi.virtualStack = null;
			fi.height = imageSet[0].length;
			fi.width = imageSet[0][0].length;
			fi.pixels = imageStack;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				float[] actual = (float[])format.pixelsFromBytes(stream.toByteArray());
				Assert.assertFloatArraysEqual(expectedAfterWrite,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryGray32FloatImage() {
		for (long[][] image : Images.Images)
		{
			PixelFormat format = new Gray32FloatFormat();
			float[] expected = (float[])(format.expectedResults(image)); 
			
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.GRAY32_FLOAT;
			fi.nImages = 1;
			fi.virtualStack = null;
			fi.height = image.length;
			fi.width = image[0].length;
			fi.pixels = expected;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				float[] actual = (float[])format.pixelsFromBytes(stream.toByteArray());
				Assert.assertFloatArraysEqual(expected,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryGray32Float() {
		tryGray32FloatVirtualStack();
		tryGray32FloatStack();
		tryGray32FloatImage();
	}
	
	private void tryRgb48Image() {
		for (long[][] image : Images.Images)
		{
			PixelFormat format = new Rgb48Format();
			short[][] expectedAfterWrite = (short[][]) format.expectedResults(image);
			FileInfo fi = new FileInfo();
			fi.intelByteOrder = false;
			fi.fileType = FileInfo.RGB48;
			// no other preconditions!
			fi.height = image.length;
			fi.width = image[0].length;
			fi.pixels = expectedAfterWrite;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageWriter writer = new ImageWriter(fi);
				writer.write(stream);
				short[][] actual = (short[][])format.pixelsFromBytes(stream.toByteArray());
				assertArrayEquals(expectedAfterWrite,actual);
			} catch(Exception e)
			{
				fail(e.getMessage());
			}
		}
	}
	
	private void tryRgb48() {
		tryRgb48Image();
	}

	// TODO - note that fi.nImages == 1 implies no imageStack : its not possible to have a stack of 1 items.
	//   Therefore must make sure I don't pass a "stack" of 1 thing to the tryX() routines as ImageWriter will
	//   assume the data is not stacked and will fail to cast the pixels correctly.

	@Test
	public void testWritePixels() {

		tryNoPixAndNoVirtStack();
		tryBadPix();
		tryColor8();
		tryGray8();
		tryGray16Signed();
		tryGray16Unsigned();
		tryRgb();
		tryGray32Float();
		tryRgb48();
						
		// fake test 16 bit signed virtual stack
		VirtualStack vStack = new VirtualStack(5, 1, (ColorModel) null, "data/");
		
		vStack.addSlice("test1.tif");
		vStack.addSlice("test2.tif");
		
		assertNotNull(vStack.getProcessor(1));
		
		FileInfo fi = new FileInfo();
		//fi.directory = "data";
		//fi.fileName = "placeholder.tif";
		fi.width = 5;
		fi.height = 1;
		fi.fileType = FileInfo.GRAY16_UNSIGNED;
		fi.virtualStack = vStack;
		fi.nImages = 2;
		fi.pixels = null;
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageWriter writer = new ImageWriter(fi);
			writer.write(stream);
			byte[] bytes = stream.toByteArray();
			assertArrayEquals(new byte[] {-128,1,-128,2,-128,3,-128,4,-128,5,-128,1,-128,2,-128,3,-128,4,-128,5},bytes);
		} catch(Exception e)
		{
			System.out.println(e.getMessage());
			fail();
		}

	}
}
