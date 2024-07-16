package com.hkmci.csdkms.storage;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;



import com.aspose.pdf.devices.PngDevice;
import com.aspose.pdf.devices.Resolution;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;

import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
//import com.spire.pdf.FileFormat;
//import com.spire.pdf.PdfDocument;
//import com.spire.pdf.PdfVersion;

public class WaterMark {

    public boolean doWaterMark(String filePath, String newFilePath, String rankName,String staff_no) throws DocumentException, IOException {
        // PDF File
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
       // Calendar cal = Calendar.getInstance();
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // Add Water Mark
        
		
		// PdfDocument document = new PdfDocument();
//       document.loadFromFile(input);
		
		 
	  // document.loadFromFile(newFilePath);
//       document.getFileInfo().setVersion(PdfVersion.Version_1_7);
//       document.saveToFile(newFilePath, FileFormat.PDF);
//       document.close();
//       System.out.println("WaterMark ==   line 52: Change it to Version 1_7");
//		
       // document.loadFromFile(newFilePath);
       // document.getFileInfo().getVersion();
       // System.out.println("Document version = "+  document.getFileInfo().getVersion() );
        setWatermark(bos, newFilePath, rankName,1, staff_no);
        return true;
    }
    
    public static void setWatermark2 (BufferedOutputStream bos, String input, String rankName , Integer permission, String staff_no)
    throws DocumentException, IOException{
    
    	try {
    		 
			// Load a existing PDF file
			PDDocument pdDocument = PDDocument
					.load(new File(input));
			System.out.println("WaterMark  : Line 56 ====");
			// Get the pages and create a map with page number and the image to
			// be water marked.
			HashMap<Integer, String> overlayProps = new HashMap<Integer, String>();
			for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
				overlayProps.put(i + 1, input);
				System.out.println("Water Mark : Line 62 ====="+ i);
			}
 
			// Using the Overlay object add the map of properties to the PDF
			Overlay overlay = new Overlay();
			overlay.setInputPDF(pdDocument);
			overlay.setOverlayPosition(Overlay.Position.BACKGROUND);
			overlay.overlay(overlayProps);
 
			// Save the PDF to a new or same location
			pdDocument.save(input);
			pdDocument.close();
			System.out.println("PDF saved to the location !!!");
 
		} catch (IOException ioe) {
			System.out.println("Error while saving pdf" + ioe.getMessage());
		}
 
    }
    
    
    
    
    
    
    

    /**
     * 
     * @param bos Output path
     * @param input
     *            Original PDF Path
     * @param waterMarkName
     *            Water Mark
     * @param permission
     *            permission code
     * @throws DocumentException
     * @throws IOException
     */
    public static void setWatermark(BufferedOutputStream bos, String input, String rankName, Integer permission, String staff_no)
            throws DocumentException, IOException {
    	System.out.println("Input = "+input);
//        PdfDictionary page;
//        PdfDocument document = new PdfDocument();
////        document.loadFromFile(input);
//        document.loadFromFile(input);
////        document.getFileInfo().setVersion(PdfVersion.Version_1_7);
////        document.saveToFile(input, FileFormat.PDF);
//        document.close();

        
        PdfReader reader = new PdfReader(input);
        
        PdfStamper stamper = new PdfStamper(reader, bos);
      
        Integer total = reader.getNumberOfPages() + 1;

        
//        stamper.setEncryption(null, null,
//                PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA);
//        
  
     //  Color color = stamper.getPageSize().getBackgroundColor();
       Color color = stamper.getReader().getPageSize(1).getBackgroundColor();
       System.out.println("Color = "+ color);

        PdfContentByte content;
        //PdfContentByte bottom;
//        BaseFont base  = BaseFont.createFont("file:/SimSun.ttf", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        BaseFont base = BaseFont.createFont(BaseFont.TIMES_ROMAN,
                BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        BaseFont base_blod = BaseFont.createFont(BaseFont.TIMES_BOLD,
                BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        PdfGState gs = new PdfGState();
        String bottom_string = "“This official document is for the use by the intended recipient only. Any unauthorized use, "
        		+ "retention, disclosure, copying, printing, forwarding or dissemination of this official document "
        		+ "is strictly prohibited and may lead to civil or criminal liabilities. If you are not the intended "
        		+ "recipient of this official document, you are required to promptly restore this official document to the "
        		+ "sender and destroy any copies of this official document. Please notify the sender immediately if you have "
        		+ "received this official document in error.”";
        
        
        
        
        
        for (int i = 1; i < total; i++) {
        
        
            float heigh = stamper.getReader().getPageSize(i).getHeight();
            System.out.println("Page is rotate : "+ heigh);
            float width = stamper.getReader().getPageSize(i).getWidth();
            System.out.println("Page is rotate 2 : "+width);
            Integer rotation = stamper.getReader().getPageRotation(i);
            System.out.println("Rotation = "+rotation);
        	
       
            
            
            
            
        	Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(new Date());
			
			Date endDate=rightNow.getTime();
			
            content = stamper.getOverContent(i);// Water Mark on top of content
         
            if ((heigh> width && rotation ==0) || (heigh> width && rotation >100)){
            	System.out.println("(heigh> width && rotation ==0) || (heigh< width && rotation ==270) ");
            // content = stamper.getUnderContent(i);//Water Mark on bottom of content
            gs.setFillOpacity(0.30f);
            content.setGState(gs);
            content.beginText();
            content.setColorFill(Color.BLACK);
            //content.setFontAndSize( base, 125);
            content.setFontAndSize(base, (heigh-width)/2);
            content.setWordSpacing(30);
            content.setTextMatrix(70, 200);
//            content.showTextAligned(Element.ALIGN_CENTER, rankName + " " + staff_no + " ！" , 380, 500, 55);
            content.showTextAligned(Element.ALIGN_CENTER, rankName + " " + staff_no + " ！" , width/2 , (heigh/2)+100, 55);
            content.setColorFill(Color.BLACK);
           // content.setFontAndSize( base_blod, 25);
            content.setFontAndSize( base_blod, (heigh-width)/10);
            content.setWordSpacing(30);
            content.setTextMatrix(70, 200);
//            content.showTextAligned(Element.ALIGN_CENTER, endDate.toString() , 450, 350, 55);
            content.showTextAligned(Element.ALIGN_CENTER, endDate.toString() , (width/2)+70, (heigh/2)-50, 55);
            
            content.setColorFill(Color.BLACK);
            content.setFontAndSize(base_blod, 10);
            content.setWordSpacing(0);
            content.showTextAligned(Element.ALIGN_LEFT, "Important Note", 30, 120, 0);
            content.setFontAndSize(base, 10);
            //Phrase bottom_watermark = new Phrase(10,new Chunk(bottom_string,FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, Color.LIGHT_GRAY)));
           
            //content.setLeading(20);
            //content.showTextAligned(Element.ALIGN_JUSTIFIED, bottom_string, 0, 74, 0);
//            content.showTextAligned(Element.ALIGN_JUSTIFIED, "“This official document is for the use by the intended recipient only. Any unauthorized use, retention, ", 0, 74, 0);
//            content.showTextAligned(Element.ALIGN_JUSTIFIED, "disclosure, copying, printing, forwarding or dissemination of this official document is strictly prohibited and", 0, 58, 0);
//            content.showTextAligned(Element.ALIGN_JUSTIFIED, "may lead to civil or criminal liabilities. If you are not the intended recipient of this official document, you", 0, 42, 0);
//            content.showTextAligned(Element.ALIGN_JUSTIFIED, "are required to promptly restore this official document to the sender and destroy any copies of this official ", 0, 26, 0);
//            content.showTextAligned(Element.ALIGN_JUSTIFIED, "document. Please notify the sender immediately if you have received this official document in error.” ", 0, 10, 0);
            content.endText();
            ColumnText ct = new ColumnText(content);
            ct.setSimpleColumn(new Phrase(new Chunk(bottom_string, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
                               30, 115, 565, 30, 15, Element.ALIGN_JUSTIFIED | Element.ALIGN_TOP);
            ct.go(); 
            
            } else if (width > heigh && rotation == 0 ||rotation == 90 &&heigh > width ){
            	System.out.println("Width > Height ");
                gs.setFillOpacity(0.30f);
                content.setGState(gs);
                content.beginText();
                content.setColorFill(Color.BLACK);
                content.setFontAndSize( base, 125);
                content.setWordSpacing(30);
                content.setTextMatrix(70, 200);
                content.showTextAligned(Element.ALIGN_CENTER, rankName + " " + staff_no + " ！" , 410, 380, 15);
                content.setColorFill(Color.BLACK);
                content.setFontAndSize( base_blod, 25);
                content.setWordSpacing(30);
                content.setTextMatrix(70, 200);
                content.showTextAligned(Element.ALIGN_CENTER, endDate.toString() , 470, 280, 15);
                
                content.setColorFill(Color.BLACK);
                content.setFontAndSize(base_blod, 10);
                content.setWordSpacing(0);
                content.showTextAligned(Element.ALIGN_LEFT, "Important Note", 30, 120, 0);
                content.setFontAndSize(base, 10);
                //Phrase bottom_watermark = new Phrase(10,new Chunk(bottom_string,FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, Color.LIGHT_GRAY)));
               
                //content.setLeading(20);
                //content.showTextAligned(Element.ALIGN_JUSTIFIED, bottom_string, 0, 74, 0);
//                content.showTextAligned(Element.ALIGN_JUSTIFIED, "“This official document is for the use by the intended recipient only. Any unauthorized use, retention, ", 0, 74, 0);
//                content.showTextAligned(Element.ALIGN_JUSTIFIED, "disclosure, copying, printing, forwarding or dissemination of this official document is strictly prohibited and", 0, 58, 0);
//                content.showTextAligned(Element.ALIGN_JUSTIFIED, "may lead to civil or criminal liabilities. If you are not the intended recipient of this official document, you", 0, 42, 0);
//                content.showTextAligned(Element.ALIGN_JUSTIFIED, "are required to promptly restore this official document to the sender and destroy any copies of this official ", 0, 26, 0);
//                content.showTextAligned(Element.ALIGN_JUSTIFIED, "document. Please notify the sender immediately if you have received this official document in error.” ", 0, 10, 0);
                content.endText();
                ColumnText ct = new ColumnText(content);
                ct.setSimpleColumn(new Phrase(new Chunk(bottom_string, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
                                   30, 115, 565, 30, 15, Element.ALIGN_JUSTIFIED | Element.ALIGN_TOP);
                ct.go(); 
            	
            } else {
            	System.out.println("Width > Height ");
                gs.setFillOpacity(0.30f);
                content.setGState(gs);
                content.beginText();
                content.setColorFill(Color.BLACK);
                content.setFontAndSize( base, 125);
                content.setWordSpacing(30);
                content.setTextMatrix(70, 200);
                content.showTextAligned(Element.ALIGN_CENTER, rankName + " " + staff_no + " ！" , heigh/2 , (width/2)+100, 55);
                content.setColorFill(Color.BLACK);
                content.setFontAndSize( base_blod, 25);
                content.setWordSpacing(30);
                content.setTextMatrix(70, 200);
                content.showTextAligned(Element.ALIGN_CENTER, endDate.toString() , (heigh/2)+70, (width/2)-50, 55);
                
                content.setColorFill(Color.BLACK);
                content.setFontAndSize(base_blod, 10);
                content.setWordSpacing(0);
                content.showTextAligned(Element.ALIGN_LEFT, "Important Note", 30, 120, 0);
                content.setFontAndSize(base, 10);
                //Phrase bottom_watermark = new Phrase(10,new Chunk(bottom_string,FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, Color.LIGHT_GRAY)));
               
                //content.setLeading(20);
                //content.showTextAligned(Element.ALIGN_JUSTIFIED, bottom_string, 0, 74, 0);
//                content.showTextAligned(Element.ALIGN_JUSTIFIED, "“This official document is for the use by the intended recipient only. Any unauthorized use, retention, ", 0, 74, 0);
//                content.showTextAligned(Element.ALIGN_JUSTIFIED, "disclosure, copying, printing, forwarding or dissemination of this official document is strictly prohibited and", 0, 58, 0);
//                content.showTextAligned(Element.ALIGN_JUSTIFIED, "may lead to civil or criminal liabilities. If you are not the intended recipient of this official document, you", 0, 42, 0);
//                content.showTextAligned(Element.ALIGN_JUSTIFIED, "are required to promptly restore this official document to the sender and destroy any copies of this official ", 0, 26, 0);
//                content.showTextAligned(Element.ALIGN_JUSTIFIED, "document. Please notify the sender immediately if you have received this official document in error.” ", 0, 10, 0);
                content.endText();
                ColumnText ct = new ColumnText(content);
                ct.setSimpleColumn(new Phrase(new Chunk(bottom_string, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
                                   30, 115, 565, 30, 15, Element.ALIGN_JUSTIFIED | Element.ALIGN_TOP);
                ct.go(); 
            	
            }
            
            
            
        }
           

       
        
        
        stamper.close();

    }
   
 

    
}
