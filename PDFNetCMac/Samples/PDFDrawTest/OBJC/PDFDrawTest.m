//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>
#include <math.h>
#include <stdio.h>

//---------------------------------------------------------------------------------------
// The following sample illustrates how to convert PDF documents to various raster image 
// formats (such as PNG, JPEG, BMP, TIFF, etc), as well as how to convert a PDF page to 
// GDI+ Bitmap for further manipulation and/or display in WinForms applications.
//---------------------------------------------------------------------------------------
int main(int argc, char *argv[])
{
    @autoreleasepool {
        @try
        {
            // The first step in every application using PDFNet is to initialize the 
            // library and set the path to common PDF resources. The library is usually 
            // initialized only once, but calling Initialize() multiple times is also fine.
            [PTPDFNet Initialize: 0];

            // Optional: Set ICC color profiles to fine tune color conversion 
            // for PDF 'device' color spaces...

            // PDFNet::SetResourcesPath("../../../resources");
            // PDFNet::SetColorManagement();
            // PDFNet::SetDefaultDeviceCMYKProfile("D:/Misc/ICC/USWebCoatedSWOP.icc");
            // PDFNet::SetDefaultDeviceRGBProfile("AdobeRGB1998.icc"); // will search in PDFNet resource folder.

            // ----------------------------------------------------
            // Optional: Set predefined font mappings to override default font 
            // substitution for documents with missing fonts...

            // PDFNet::AddFontSubst("StoneSans-Semibold", "C:/WINDOWS/Fonts/comic.ttf");
            // PDFNet::AddFontSubst("StoneSans", "comic.ttf");  // search for 'comic.ttf' in PDFNet resource folder.
            // PDFNet::AddFontSubst(PDFNet::e_Identity, "C:/WINDOWS/Fonts/arialuni.ttf");
            // PDFNet::AddFontSubst(PDFNet::e_Japan1, "C:/Program Files/Adobe/Acrobat 7.0/Resource/CIDFont/KozMinProVI-Regular.otf");
            // PDFNet::AddFontSubst(PDFNet::e_Japan2, "c:/myfonts/KozMinProVI-Regular.otf");
            // PDFNet::AddFontSubst(PDFNet::e_Korea1, "AdobeMyungjoStd-Medium.otf");
            // PDFNet::AddFontSubst(PDFNet::e_CNS1, "AdobeSongStd-Light.otf");
            // PDFNet::AddFontSubst(PDFNet::e_GB1, "AdobeMingStd-Light.otf");

            PTPDFDraw *draw = [[PTPDFDraw alloc] initWithDpi: 92];  // PDFDraw class is used to rasterize PDF pages.

            //--------------------------------------------------------------------------------
            // Example 1) Convert the first page to PNG and TIFF at 92 DPI. 
            // A three step tutorial to convert PDF page to an image.
            @try 
            {
                // A) Open the PDF document.
                PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/tiger.pdf"];

                // Initialize the security handler, in case the PDF is encrypted.
                [doc InitSecurityHandler];  

                // B) The output resolution is set to 92 DPI.
                [draw SetDPI: 92];

                // C) Rasterize the first page in the document and save the result as PNG.
                [draw Export:[[doc GetPageIterator: 1] Current] filename: @"../../TestFiles/Output/tiger_92dpi.png" format: @"PNG"];

                NSLog(@"Example 1: tiger_92dpi.png");

                // Export the same page as TIFF
                [draw Export:[[doc GetPageIterator: 1] Current] filename: @"../../TestFiles/Output/tiger_92dpi.tif" format: @"TIFF"];
            }
            @catch(NSException *e)	
            {
                NSLog(@"%@", e.reason);
            }

            //--------------------------------------------------------------------------------
            // Example 2) Convert the all pages in a given document to JPEG at 72 DPI.
            NSLog(@"Example 2:");
            PTObjSet *hint_set = [[PTObjSet alloc] init]; //  A collection of rendering 'hits'.
            @try 
            {
                PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
                // Initialize the security handler, in case the PDF is encrypted.
                [doc InitSecurityHandler];  

                [draw SetDPI: 72]; // Set the output resolution is to 72 DPI.

                // Use optional encoder parameter to specify JPEG quality.
                PTObj *encoder_param=[hint_set CreateDict];
                [encoder_param PutNumber: @"Quality" value: 80];

                // Traverse all pages in the document.
                PTPageIterator *itr;
                for (itr=[doc GetPageIterator: 1]; [itr HasNext]; [itr Next]) {
                    NSString *filename = [NSString stringWithFormat: @"newsletter%d.jpg", [[itr Current] GetIndex]];
                    NSLog(@"%@", filename);
                    NSString *filenameWithPath = [@"../../TestFiles/Output/" stringByAppendingPathComponent: filename];

                    [draw ExportWithObj: [itr Current] filename: filenameWithPath format: @"JPEG" encoder_params: encoder_param];
                }

                NSLog(@"Done.");
            }
            @catch(NSException *e)	
            {
                NSLog(@"%@", e.reason);
            }
        
            // Examples 3-5
            @try  
            {				
                // Common code for remaining samples.
                PTPDFDoc *tiger_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/tiger.pdf"];
                // Initialize the security handler, in case the PDF is encrypted.
                [tiger_doc InitSecurityHandler];  
                PTPage *page = [tiger_doc GetPage: 1];

                //--------------------------------------------------------------------------------
                // Example 3) Convert the first page to raw bitmap. Also, rotate the 
                // page 90 degrees and save the result as RAW.
                [draw SetDPI: 100]; // Set the output resolution is to 100 DPI.
                [draw SetRotate: e_pt90];  // Rotate all pages 90 degrees clockwise.

                PTBitmapInfo* buf = [draw GetBitmap: page pix_fmt: e_ptrgb demult: NO];

                // Save the raw RGB data to disk.
                [[buf GetBuffer] writeToFile: @"../../TestFiles/Output/tiger_100dpi_rot90.raw" atomically: NO];

                NSLog(@"Example 3: tiger_100dpi_rot90.raw");
                [draw SetRotate: e_pt0];  // Disable image rotation for remaining samples.

                //--------------------------------------------------------------------------------
                // Example 4) Convert PDF page to a fixed image size. Also illustrates some 
                // other features in PDFDraw class such as rotation, image stretching, exporting 
                // to grayscale, or monochrome.

                // Initialize render 'gray_hint' parameter, that is used to control the 
                // rendering process. In this case we tell the rasterizer to export the image as 
                // 1 Bit Per Component (BPC) image.
                PTObj *mono_hint=[hint_set CreateDict];  
                [mono_hint PutNumber: @"BPC" value: 1];

                // SetImageSize can be used instead of SetDPI() to adjust page  scaling 
                // dynamically so that given image fits into a buffer of given dimensions.
                [draw SetImageSize: 1000 height: 1000 preserve_aspect_ratio: YES];		// Set the output image to be 1000 wide and 1000 pixels tall
                [draw ExportWithObj: page filename: @"../../TestFiles/Output/tiger_1000x1000.png" format: @"PNG" encoder_params: mono_hint];
                NSLog(@"Example 4: tiger_1000x1000.png");

                [draw SetImageSize: 200 height: 400 preserve_aspect_ratio: YES];	    // Set the output image to be 200 wide and 300 pixels tall
                [draw SetRotate: e_pt180];  // Rotate all pages 90 degrees clockwise.

                // 'gray_hint' tells the rasterizer to export the image as grayscale.
                PTObj *gray_hint=[hint_set CreateDict];  
                [gray_hint PutName: @"ColorSpace" name: @"Gray"];

                [draw ExportWithObj: page filename: @"../../TestFiles/Output/tiger_200x400_rot180.png" format: @"PNG" encoder_params: gray_hint];
                NSLog(@"Example 4: tiger_200x400_rot180.png");

                [draw SetImageSize: 400 height: 200 preserve_aspect_ratio: NO];  // The third parameter sets 'preserve-aspect-ratio' to false.
                [draw SetRotate: e_pt0];    // Disable image rotation.
				[draw  Export:page filename: @"../../TestFiles/Output/tiger_400x200_stretch.jpg" format: @"JPEG"];
                NSLog(@"Example 4: tiger_400x200_stretch.jpg");

                //--------------------------------------------------------------------------------
                // Example 5) Zoom into a specific region of the page and rasterize the 
                // area at 200 DPI and as a thumbnail (i.e. a 50x50 pixel image).
                PTPDFRect *zoom_rect = [[PTPDFRect alloc] initWithX1: 216 y1: 522 x2: 330 y2: 600];
                [page SetCropBox: zoom_rect];	// Set the page crop box.

                // Select the crop region to be used for drawing.
                [draw SetPageBox: e_ptcrop];
                [draw SetDPI: 900];  // Set the output image resolution to 900 DPI.
                [draw Export:page filename: @"../../TestFiles/Output/tiger_zoom_900dpi.png" format: @"PNG"];
                NSLog(@"Example 5: tiger_zoom_900dpi.png");

                // -------------------------------------------------------------------------------
                // Example 6)
                [draw SetImageSize: 50 height: 50 preserve_aspect_ratio: YES];	   // Set the thumbnail to be 50x50 pixel image.
                [draw Export:page filename: @"../../TestFiles/Output/tiger_zoom_50x50.png" format: @"PNG"];
                NSLog(@"Example 6: tiger_zoom_50x50.png");
            }
            @catch(NSException *e)	
            {
                NSLog(@"%@", e.reason);
            }
        
            PTObj *cmyk_hint = [hint_set CreateDict];
            [cmyk_hint PutName: @"ColorSpace" name: @"CMYK"];

            //--------------------------------------------------------------------------------
            // Example 7) Convert the first PDF page to CMYK TIFF at 92 DPI.
            // A three step tutorial to convert PDF page to an image
            @try 
            {
                // A) Open the PDF document.
                PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/tiger.pdf"];
                // Initialize the security handler, in case the PDF is encrypted.
                [doc InitSecurityHandler];  

                // B) The output resolution is set to 92 DPI.
                [draw SetDPI: 92];

                // C) Rasterize the first page in the document and save the result as TIFF.
                PTPage *pg = [doc GetPage: 1];
                [draw ExportWithObj: pg filename: @"../../TestFiles/Output/out1.tif" format: @"TIFF" encoder_params: cmyk_hint];
                NSLog(@"Example 7: out1.tif");
            }
            @catch(NSException *e)	
            {
                NSLog(@"%@", e.reason);
            }
        
            //--------------------------------------------------------------------------------
            // Example 8) PDFRasterizer can be used for more complex rendering tasks, such as 
            // strip by strip or tiled document rendering. In particular, it is useful for 
            // cases where you cannot simply modify the page crop box (interactive viewing,
            // parallel rendering).  This example shows how you can rasterize the south-west
            // quadrant of a page.
            @try
            {
                // A) Open the PDF document.
                PTPDFDoc* doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/tiger.pdf"];
                // Initialize the security handler, in case the PDF is encrypted.
                [doc InitSecurityHandler];

                    // B) Get the page matrix 
                PTPage* pg = [doc GetPage: 1];
                PTBox box = e_ptcrop;
                PTMatrix2D* mtx = [pg GetDefaultMatrix: YES box_type: box angle: e_pt0];
                    // We want to render a quadrant, so use half of width and height
                const double pg_w = [pg GetPageWidth: box] / 2;
                const double pg_h = [pg GetPageHeight: box] / 2;

                // C) Scale matrix from PDF space to buffer space
                const double dpi = 96.0f;
                const double scale = dpi / 72.0f; // PDF space is 72 dpi
                const size_t buf_w = floor(scale * pg_w);
                const size_t buf_h = floor(scale * pg_h);
                const int bytes_per_pixel = 4; // RGBA buffer
                const size_t buf_size = buf_w * buf_h * bytes_per_pixel;
                [mtx Translate: 0 v: -pg_h]; // translate by '-pg_h' since we want south-west quadrant
                    //mtx = Common::Matrix2D(scale, 0, 0, scale, 0, 0) * mtx;
                PTMatrix2D* scale_mtx = [[PTMatrix2D alloc] initWithA: scale b: 0 c: 0 d: scale h: 0 v:0];
                mtx = [scale_mtx Multiply: mtx];

                    // D) Rasterize page into memory buffer, according to our parameters
                PTPDFRasterizer* rast = [[PTPDFRasterizer alloc] initWithType: e_ptBuiltIn];
                NSData* buf = [rast Rasterize: pg width:(int)buf_w height:(int)buf_h stride: (int)buf_w * bytes_per_pixel num_comps: bytes_per_pixel demult: YES device_mtx: mtx clip: Nil scrl_clip_regions: Nil];

                // buf now contains raw BGRA bitmap.
                NSLog(@"Example 8: Successfully rasterized into memory buffer.");
            }
            @catch (NSException* e)
            {
                NSLog(@"%@", e.reason);
            }



            //--------------------------------------------------------------------------------
            // Example 9) Export raster content to PNG using different image smoothing settings. 
            @try 
            {
                PTPDFDoc* text_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/lorem_ipsum.pdf"];
                [text_doc InitSecurityHandler];

                [draw SetImageSmoothing:false hq_image_resampling:false];
                [draw Export:[[text_doc GetPageIterator:1] Current] filename: @"../../TestFiles/Output/raster_text_no_smoothing.png" format:@"PNG"];
                NSLog(@"Example 9 a): raster_text_no_smoothing.png. Done.");

                [draw SetImageSmoothing:true hq_image_resampling:false];
                [draw Export:[[text_doc GetPageIterator:1] Current] filename: @"../../TestFiles/Output/raster_text_smoothed.png" format:@"PNG"];
                NSLog(@"Example 9 b): raster_text_smoothed.png. Done.");

                [draw SetImageSmoothing:true hq_image_resampling:true];
                [draw Export:[[text_doc GetPageIterator:1] Current] filename: @"../../TestFiles/Output/raster_text_high_quality.png" format:@"PNG"];
                NSLog(@"Example 9 c): raster_text_high_quality.png. Done.");
            }
            @catch (NSException *e)
            {
                NSLog(@"%@", e.reason);
            }


            //--------------------------------------------------------------------------------
            // Example 10) Export separations directly, without conversion to an output colorspace
            @try
            {
                PTPDFDoc* separation_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/op_blend_test.pdf"];
                [separation_doc InitSecurityHandler];

                PTObj* separation_hint = [hint_set CreateDict];
                [separation_hint PutName:@"ColorSpace" name:@"Separation"];
                [draw SetDPI:96];
                [draw SetImageSmoothing:true hq_image_resampling:true];
                [draw SetOverprint: e_ptop_on];

                [draw Export:[[separation_doc GetPageIterator:1] Current] filename: @"../../TestFiles/Output/merged_separations.png" format: @"PNG"];
                NSLog(@"Example 10 a): merged_separations.png. Done.");

                [draw ExportWithObj:[[separation_doc GetPageIterator:1] Current] filename: @"../../TestFiles/Output/separation" format: @"PNG" encoder_params: separation_hint];
                NSLog(@"Example 10 b): separation_[ink].png. Done.");

                [draw ExportWithObj:[[separation_doc GetPageIterator:1] Current] filename: @"../../TestFiles/Output/separation_NChannel.tif" format: @"TIFF" encoder_params: separation_hint];
                NSLog(@"Example 10 c): separation_NChannel.tif. Done.");
            }
            @catch (NSException *e)
            {
                NSLog(@"%@", e.reason);
            }



        }
        @catch(NSException *e)	
        {
            NSLog(@"%@", e.reason);
        }




        return 0;
    }
}

