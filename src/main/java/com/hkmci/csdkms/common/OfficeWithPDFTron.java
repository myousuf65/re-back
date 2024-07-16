//package com.hkmci.csdkms.common;
//
//import javax.annotation.Resource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.hkmci.csdkms.storage.StorageService;
//import com.pdftron.common.PDFNetException;
//import com.pdftron.pdf.Convert;
//import com.pdftron.pdf.DocumentConversion;
//import com.pdftron.pdf.OfficeToPDFOptions;
//import com.pdftron.pdf.PDFDoc;
//import com.pdftron.pdf.PDFNet;
//import com.pdftron.sdf.SDFDoc;
//
////---------------------------------------------------------------------------------------
//// The following sample illustrates how to use the PDF.Convert utility class to convert 
//// MS Office files to PDF
////
//// This conversion is performed entirely within the PDFNet and has *no* external or
//// system dependencies dependencies -- Conversion results will be the sam whether
//// on Windows, Linux or Android.
////
//// Please contact us if you have any questions.	
////---------------------------------------------------------------------------------------
//public class OfficeWithPDFTron {
//	@Autowired
//	@Resource
//    private StorageService storageService;
//
//    public void doTest(String filename, String fileType) {
//    	 // first the one-line conversion interface
//    	Long startTime=System.currentTimeMillis();   //Time start
//        simpleDocxConvert(filename + "." + fileType, filename + ".pdf");
//        Long endTime=System.currentTimeMillis(); //Time end
//        System.out.println("OfficeWithPDFTron -- " + filename + "： "+(endTime-startTime)+"ms");
//        // then the more flexible line-by-line interface
//        //flexibleDocxConvert(filename + "." + fileType, filename + ".pdf");
//       
//        PDFNet.terminate();
//    }
//    
//
//    public void simpleDocxConvert(String inputFilename, String outputFilename) {
//    	
//    	String resourcePath = System.getProperty("user.dir") + "/kms-resource";
//    	
//        String input_path = resourcePath + "/";
//        String output_path = resourcePath + "/pdf/";
//        //System.out.println(resourcePath);
//        try {
//
//            // perform the conversion with no optional parameters
//            PDFDoc pdfdoc = new PDFDoc();
//            Convert.officeToPdf(pdfdoc, input_path + inputFilename, null);
//
//            // save the result
//            pdfdoc.save(output_path + outputFilename, SDFDoc.SaveMode.INCREMENTAL, null);
//            // output PDF pdfdoc
//
//            // And we're done!
//            System.out.println("Done conversion " + output_path + outputFilename);
//        } catch (PDFNetException e) {
//            System.out.println("Unable to convert MS Office document, error:");
//            e.printStackTrace();
//            System.out.println(e);
//        }
//    }
//
//    public static void flexibleDocxConvert(String inputFilename, String outputFilename) {
//    	String resourcePath = System.getProperty("user.dir") + "/kms-resource";
//    	
//        String input_path = resourcePath + "/";
//        String output_path = resourcePath + "/pdf/";
//        try {
//            OfficeToPDFOptions options = new OfficeToPDFOptions();
//            options.setSmartSubstitutionPluginPath(input_path);
//
//            // create a conversion object -- this sets things up but does not yet
//            // perform any conversion logic.
//            // in a multithreaded environment, this object can be used to monitor
//            // the conversion progress and potentially cancel it as well
//            DocumentConversion conversion = Convert.streamingPdfConversion(
//                    input_path + inputFilename, options);
//
//            System.out.println(inputFilename + ": " + Math.round(conversion.getProgress() * 100.0)
//                    + "% " + conversion.getProgressLabel());
//
//            // actually perform the conversion
//            while (conversion.getConversionStatus() == DocumentConversion.e_incomplete) {
//                conversion.convertNextPage();
//                System.out.println(inputFilename + ": " + Math.round(conversion.getProgress() * 100.0)
//                        + "% " + conversion.getProgressLabel());
//            }
//
//            if (conversion.tryConvert() == DocumentConversion.e_success) {
//                int num_warnings = conversion.getNumWarnings();
//
//                // print information about the conversion
//                for (int i = 0; i < num_warnings; ++i) {
//                    System.out.println("Warning: " + conversion.getWarningString(i));
//                }
//
//                // save the result
//                PDFDoc doc = conversion.getDoc();
//                doc.save(output_path + outputFilename, SDFDoc.SaveMode.NO_FLAGS, null);
//                // output PDF doc
//
//                // done
//                System.out.println("Done conversion " + output_path + outputFilename);
//            } else {
//                System.out.println("Encountered an error during conversion: " + conversion.getErrorString());
//            }
//        } catch (PDFNetException e) {
//            System.out.println("Unable to convert MS Office document, error:");
//            e.printStackTrace();
//            System.out.println(e);
//        }
//    }
//
//}
