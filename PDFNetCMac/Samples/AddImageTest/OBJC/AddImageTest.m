//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//-----------------------------------------------------------------------------------
// This sample illustrates how to embed various raster image formats
// (e.g. TIFF, JPEG, JPEG2000, JBIG2, GIF, PNG, BMP, etc.) in a PDF document.
//
// Note: On Windows platform this sample utilizes GDI+ and requires GDIPLUS.DLL to
// be present in the system path.
//-----------------------------------------------------------------------------------

int main (int argc, const char * argv[])
{
    @autoreleasepool {

        int ret = 0;
        [PTPDFNet Initialize: 0];

        PTPDFDoc* doc = [[PTPDFDoc alloc] init];
        
        PTElementBuilder* builder = [[PTElementBuilder alloc] init];    // Used to build new Element objects
        PTElementWriter* writer = [[PTElementWriter alloc] init];      // Used to write Elements to the page  
        
        PTPDFRect * rect = [[PTPDFRect alloc] init]; 
        [rect Set: 0 y1: 0 x2: 612 y2: 792];
        PTPage* page = [doc PageCreate: rect];  // Start a new page
        [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];            // Begin writing to this page

        // ----------------------------------------------------------
        // Add JPEG image to the output file
        PTImage* img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/peppers.jpg"];
        PTElement* element = [builder CreateImageWithCornerAndScale: img x: 50 y: 500 hscale: [img GetImageWidth]/2 vscale: [img GetImageHeight]/2];
        [writer WritePlacedElement: element];

        // ----------------------------------------------------------
        // Add a PNG image to the output file
        img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/butterfly.png"];
        element = [builder CreateImageWithMatrix: img mtx: [[PTMatrix2D alloc] initWithA: 100 b: 0 c: 0 d: 100 h: 300 v: 500]];
        [writer WritePlacedElement: element];
        
        // ----------------------------------------------------------
        // Add a GIF image to the output file
        img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/pdfnet.gif"];
        element = [builder CreateImageWithMatrix: img mtx: [[PTMatrix2D alloc] initWithA: [img GetImageWidth] b: 0 c: 0 d: [img GetImageHeight] h: 50 v: 350]];
        [writer WritePlacedElement: element];

        // ----------------------------------------------------------
        // Add a TIFF image to the output file
        
        img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/grayscale.tif"];
        element = [builder CreateImageWithMatrix: img mtx: [[PTMatrix2D alloc] initWithA: [img GetImageWidth] b: 0 c: 0 d: [img GetImageHeight] h: 10 v: 50]];
        [writer WritePlacedElement: element];
        
        [writer End];               // Save the page
        [doc PagePushBack: page];   // Add the page to the document page sequence

        // ----------------------------------------------------------
        // Embed a monochrome TIFF. Compress the image using lossy JBIG2 filter.
        
        page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 794]];
        [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];    // begin writing to this page
        
        // Note: encoder hints can be used to select between different compression methods. 
        // For example to instruct PDFNet to compress a monochrome image using JBIG2 compression.
        PTObjSet* hint_set = [[PTObjSet alloc] init];
        PTObj* enc=[hint_set CreateArray];  // Initialize encoder 'hint' parameter 
        [enc PushBackName: @"JBIG2"];
        [enc PushBackName: @"Lossy"];
        
        img = [PTImage CreateWithFile: [doc GetSDFDoc] filename: @"../../TestFiles/multipage.tif" encoder_hints: enc];
        element = [builder CreateImageWithMatrix: img mtx: [[PTMatrix2D alloc] initWithA: 612 b: 0 c: 0 d: 794 h: 0 v: 0]];
        [writer WritePlacedElement: element];
        
        [writer End];               // Save the page
        [doc PagePushBack: page];   // Add the page to the document page sequence*/

        // ----------------------------------------------------------
        // Add a JPEG2000 (JP2) image to the output file
        
        // Create a new page 
        page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 794]];
        [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];    // Begin writing to the page
        
        // Embed the image.
        img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/palm.jp2"];
        
        // Position the image on the page.
        element = [builder CreateImageWithMatrix: img mtx: [[PTMatrix2D alloc] initWithA: [img GetImageWidth] b: 0 c: 0 d: [img GetImageHeight] h: 96 v: 80]];
        [writer WritePlacedElement: element];
        
        // Write 'JPEG2000 Sample' text string under the image.
        [writer WriteElement: [builder CreateTextBeginWithFont: [PTFont Create: [doc GetSDFDoc] type: e_pttimes_roman embed: NO] font_sz: 32]];
        element = [builder CreateTextRun: @"JPEG2000 Sample"];
        [element SetTextMatrix: 1 b: 0 c: 0 d: 1 h: 190 v: 30];
        [writer WriteElement: element];
        [writer WriteElement: [builder CreateTextEnd]];
        
        [writer End];	// Finish writing to the page
        [doc PagePushBack: page];

        [doc SaveToFile: @"../../TestFiles/Output/addimage.pdf" flags: e_ptlinearized];
        
        NSLog(@"Done. Result saved in addimage.pdf...");
        return ret;
    }
}

