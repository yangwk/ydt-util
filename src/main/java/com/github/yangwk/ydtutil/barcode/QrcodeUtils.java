package com.github.yangwk.ydtutil.barcode;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码工具类
 * 
 * @author yangwk
 */
public class QrcodeUtils {
	
	private static final String CHARSET = "UTF-8";
	
	/**
	 * @see #decode(BufferedImage) 
	 */
	public static String decode(File file){
		try {
			return decode( ImageIO.read(file) );
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @see #decode(BufferedImage) 
	 */
	public static String decode(InputStream input){
		try {
			return decode( ImageIO.read(input) );
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 二维码图片解码
	 * @author yangwk
	 */
	public static String decode(BufferedImage image) throws Exception {
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap bitmap = new BinaryBitmap(binarizer);
		//参数设置
		Map<DecodeHintType,Object> hints = new HashMap<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
		
		Result result = new MultiFormatReader().decode(bitmap, hints);
		String decodedResult = result.getText();
		return decodedResult;
	}
	
	/**
	 * @param innerImagePath 内嵌图片路径，如果为null则不内嵌
	 * @see #encode(String, int, InputStream, OutputStream)
	 */
	public static void encode(String contents, int width, String innerImagePath, String absolutePath){
		File file = new File(absolutePath);
		Boolean error = Boolean.FALSE;
		try {
			encode(contents, width,
					innerImagePath == null ? null : new FileInputStream(innerImagePath), 
					new FileOutputStream(file));
		} catch (Exception e) {
			error = Boolean.TRUE;
			throw new RuntimeException(e);
		}finally{
			if(error && file.isFile()){
				boolean deleted = file.delete();
				if(! deleted){
					throw new RuntimeException("qrcode encode error , can't delete created file : "+file.getAbsolutePath());
				}
			}
		}
		
	}

	/**
	 * 编码成二维码图片，以png格式输出
	 * @author yangwk
	 * @param contents 二维码内容
	 * @param width 二维码宽度（高宽比为1）
	 * @param innerImageInput 内嵌图片流，如果为null则不内嵌。will be closed
	 * @param output will be closed
	 * @throws Exception 
	 */
	public static void encode(String contents, int width, InputStream innerImageInput, OutputStream output) {
		try{
			int height = width;	//高宽比为1
			//参数设置
		    Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		    hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);	//容错
		    hints.put(EncodeHintType.MARGIN, 0);	//边距
		    
		    BitMatrix matrix = new MultiFormatWriter().encode(
		    		contents, BarcodeFormat.QR_CODE, width, height, hints);
		    //颜色
		    MatrixToImageConfig config = new MatrixToImageConfig();	//默认，黑白
//		    config = new MatrixToImageConfig(Integer.MAX_VALUE -5548, MatrixToImageConfig.WHITE );
		    
		    BufferedImage image = toBufferedImage(matrix, config);
		    if(innerImageInput != null){
		    	image = writeInnerImage(innerImageInput,image);
		    }
		    
		    String format = "png";
		    if (!ImageIO.write(image, format, output)) {
		      throw new IOException("Could not write an image of format " + format);
		    }
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(innerImageInput,output);
		}
	}
	
	private static BufferedImage writeInnerImage(InputStream innerImageInput, BufferedImage image){
		try {
			BufferedImage innerImage = ImageIO.read(innerImageInput);
			
			int percent = 20;	//不能超过百分比范围
			if( innerImage.getWidth() > image.getWidth()*percent/100 ||
					innerImage.getHeight()*0.2 > image.getHeight()*percent/100 ){ 
				throw new RuntimeException("innerImage is over "+percent+"%, image: "+image.getWidth()+
						"X"+image.getHeight()+" , innerImage: "+innerImage.getWidth()+"X"+innerImage.getHeight());
			}
			
			int nestX = (image.getWidth()-innerImage.getWidth())/2;
	        int nestY = (image.getHeight()-innerImage.getHeight() )/2;	//绘在中间
	        
	        Graphics2D g = image.createGraphics();
	        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	        g.drawImage(innerImage, nestX, nestY, null);
	        g.dispose();
	        innerImage.flush();
			return image;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @see MatrixToImageWriter#toBufferedImage(BitMatrix, MatrixToImageConfig)
	 */
	private static BufferedImage toBufferedImage(BitMatrix matrix, MatrixToImageConfig config) {
		
		int width = matrix.getWidth();
	    int height = matrix.getHeight();
	    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);	//支持透明
	    int onColor = config.getPixelOnColor();
	    int offColor = config.getPixelOffColor();
	    for (int x = 0; x < width; x++) {
	      for (int y = 0; y < height; y++) {
	        image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
	      }
	    }
	    return image;
	  }
	
	

}
