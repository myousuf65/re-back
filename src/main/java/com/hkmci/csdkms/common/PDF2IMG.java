package com.hkmci.csdkms.common;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDF2IMG {

	 /**
     * Convert all PDF
     * @param fileAddress 
     * @param filename 
     * @param type 
     */
    public static void pdf2png(String fileAddress,String filename,String type) {
    	//Convert PDF to Image and define the type and size
        File file = new File(fileAddress+"\\"+filename+".pdf");
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 144); // Windows native DPI
                // BufferedImage srcImage = resize(image, 240, 240);//generate the thumbnail
                ImageIO.write(image, type, new File(fileAddress+"\\"+filename+"_"+(i+1)+"."+type));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
 
/**
     *Define the Start page and end page
     * @param fileAddress 
     * @param filename 
     * @param indexOfStart start from 0
     * @param indexOfEnd -1 mean all
     * @param type 
     */
    public static String pdf2png(String fileAddress,String filename,int indexOfStart,String type, String folderName) { //int indexOfEnd
    	//Convert PDF to Image and define the type and size
        File file = new File(fileAddress+"/pdf/" + folderName + "/" +filename);
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            //Integer pageCount = doc.getNumberOfPages();
            //System.out.println("pageCount: " + pageCount);
            for (Integer i = indexOfStart; i < indexOfStart + 1; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 72); // Windows native DPI
                BufferedImage srcImage = resize(image, 0.5);//generate the thumbnail
                ImageIO.write(srcImage, type, new File(fileAddress + "/pdf/"  + folderName + "/" + filename + "." + type));
            }
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "pdf/"  + folderName + "/" + filename + "." + type;
    }
    

    public static String pdf2pngForDownload(String fileAddress,String filename,int indexOfStart,String type, Long userId) { //int indexOfEnd
    	//Convert PDF to Image and define the type and size
        File file = new File(fileAddress+"/pdf/water_mark/" + userId + "/" +filename);
        System.out.println("PDF to PNG for Downlaod = "+ file);
        
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            //Integer pageCount = doc.getNumberOfPages();
            //System.out.println("pageCount: " + pageCount);
            for (Integer i = indexOfStart; i < indexOfStart + 1; i++) {
            	System.out.println("I is = "+ i);
                BufferedImage image = renderer.renderImageWithDPI(i, 72); // Windows native DPI
                ImageIO.write(image, type, new File(fileAddress + "/pdf/water_mark/"  + userId + "/" + filename + ".png"));
            }
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "pdf/water_mark/"  + filename + "/" + filename + "." + type;
    }
     
    
    
    
    
    
    
    public static String pdf2LocalPng(String fileAddress,String filename,int indexOfStart,String type, String folderName) throws Exception, IOException { //int indexOfEnd
        //Convert PDF to Image and define the type and size
    	System.out.println("File Info: " + fileAddress + "/pdf/" + folderName + "/" +filename);
        File file = new File(fileAddress+"/pdf/" + folderName + "/" +filename);
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            Integer pageCount = doc.getNumberOfPages();
            System.out.println("pageCount: " + pageCount);
            for (Integer i = indexOfStart; i < indexOfStart + 1; i++) {
            	if(renderer.renderImageWithDPI(i, 144) == null) {
            		doc.close();
            		return filename + "." + type;
            	}else {
            		BufferedImage image = renderer.renderImageWithDPI(i, 72); // Windows native DPI
            		BufferedImage srcImage = resize(image, 0.5);//generate the thumbnail
                    ImageIO.write(srcImage, type, new File(fileAddress + "/pdf/" + folderName + "/" + filename + "." + type));
            	}
            }
            doc.close();
        } catch (NullPointerException e) {
            //e.printStackTrace();
//        	return filename + "." + type;
        }
		return filename + "." + type;
    }
    
    public static void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
 
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
    
    public static BufferedImage resize(BufferedImage inputImage,int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        //File inputFile = new File(inputImagePath);
        //BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        
        return outputImage;
 
        // extracts extension of output file
        //String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);
 
        // writes to output file
        //ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
 
    /**
     * Resizes an image by a percentage of original size (proportional).
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param percent a double number specifies percentage of the output image
     * over the input image.
     * @throws IOException
     */
    public static void resize(String inputImagePath,
            String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }
    
    public static BufferedImage resize(BufferedImage inputImage,double percent) throws IOException {
        //File inputFile = new File(inputImagePath);
        //BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        return resize(inputImage, scaledWidth, scaledHeight);
    }
	
}
