//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        @try  
        {	 
            PTPDFDoc *doc = [[PTPDFDoc alloc] init];

            PTElementBuilder *eb = [[PTElementBuilder alloc] init];		// ElementBuilder is used to build new Element objects
            PTElementWriter *writer = [[PTElementWriter alloc] init];	// ElementWriter is used to write Elements to the page	

            PTElement *element;
            PTGState *gstate;

            // Start a new page ------------------------------------
            PTPage *page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 794]];

            [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to the page

            // Create an Image that can be reused in the document or on the same page.		
            PTImage *img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/peppers.jpg"];

            element = [eb CreateImageWithMatrix:img mtx: [[PTMatrix2D alloc] initWithA: [img GetImageWidth]/2 b: -145 c: 20 d: [img GetImageHeight]/2 h: 200 v: 150]];
            [writer WritePlacedElement: element];

            gstate = [element GetGState];	// use the same image (just change its matrix)
            [gstate SetTransform: 200 b: 0 c: 0 d: 300 h: 50 v: 450];
            [writer WritePlacedElement: element];

            // use the same image again (just change its matrix).
            [writer WritePlacedElement: [eb CreateImageWithCornerAndScale: img x: 300 y: 600 hscale: 200 vscale: -150]];

            [writer End];  // save changes to the current page
            [doc PagePushBack: page];
        
            // Start a new page ------------------------------------
            // Construct and draw a path object using different styles
            page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 794]];

            [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to the page
            [eb Reset: [[PTGState alloc] init]];			// Reset the GState to default

            [eb PathBegin];		// start constructing the path
            [eb MoveTo: 306 y: 396];
            [eb CurveTo: 681 cy1: 771 cx2: 399.75 cy2: 864.75 x2: 306 y2: 771];
            [eb CurveTo: 212.25 cy1: 864.75 cx2: -69 cy2: 771 x2: 306 y2: 396];
            [eb ClosePath];
            element = [eb PathEnd];			// the path is now finished
            [element SetPathFill: YES];		// the path should be filled

            // Set the path color space and color
            gstate = [element GetGState];
            [gstate SetFillColorSpace: [PTColorSpace CreateDeviceCMYK]]; 
            [gstate SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0]];  // cyan
            [gstate SetTransform: 0.5 b: 0 c: 0 d: 0.5 h: -20 v: 300];
            [writer WritePlacedElement: element];

            // Draw the same path using a different stroke color
            [element SetPathStroke: YES];		// this path is should be filled and stroked
            [gstate SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w: 0]];  // yellow
            [gstate SetStrokeColorSpace: [PTColorSpace CreateDeviceRGB]]; 
            [gstate SetStrokeColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0]];  // red
            [gstate SetTransform: 0.5 b: 0 c: 0 d: 0.5 h: 280 v: 300];
            [gstate SetLineWidth: 20];
            [writer WritePlacedElement: element];

            // Draw the same path with with a given dash pattern
            [element SetPathFill: NO];	// this path is should be only stroked
            [gstate SetStrokeColorWithColorPt: [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w: 0]];  // blue
            [gstate SetTransform: 0.5 b: 0 c: 0 d: 0.5 h: 280 v: 0];
            NSMutableArray *dash_pattern = [[NSMutableArray alloc] init];
            [dash_pattern addObject: @30.0];
            [gstate SetDashPattern: dash_pattern phase: 0];
            [writer WritePlacedElement: element];

            // Use the path as a clipping path
            [writer WriteElement: [eb CreateGroupBegin]];	// Save the graphics state
            // Start constructing the new path (the old path was lost when we created 
            // a new Element using CreateGroupBegin()).
            [eb PathBegin];		
            [eb MoveTo: 306 y: 396];
            [eb CurveTo: 681 cy1: 771 cx2: 399.75 cy2: 864.75 x2: 306 y2: 771];
            [eb CurveTo: 212.25 cy1: 864.75 cx2: -69 cy2: 771 x2: 306 y2: 396];
            [eb ClosePath];
            element = [eb PathEnd];	// path is now constructed
            [element SetPathClip: YES];	// this path is a clipping path
            [element SetPathStroke: YES];		// this path should be filled and stroked
            gstate = [element GetGState];
            [gstate SetTransform: 0.5 b: 0 c: 0 d: 0.5 h: -20 v: 0];

            [writer WriteElement: element];

            [writer WriteElement: [eb CreateImageWithCornerAndScale: img x: 100 y: 300 hscale: 400 vscale: 600]];
            
            [writer WriteElement: [eb CreateGroupEnd]];	// Restore the graphics state

            [writer End];  // save changes to the current page
            [doc PagePushBack: page];


            // Start a new page ------------------------------------
            page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 794]];

            [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to this page
            [eb Reset: [[PTGState alloc] init]];			// Reset the GState to default

            // Begin writing a block of text
            element = [eb CreateTextBeginWithFont: [PTFont Create: [doc GetSDFDoc] type: e_pttimes_roman embed: NO] font_sz: 12];
            [writer WriteElement: element];

            element = [eb CreateTextRun: @"Hello World!"];
            [element SetTextMatrix: 10 b: 0 c: 0 d: 10 h: 0 v: 600];
            [[element GetGState] SetLeading: 15];		 // Set the spacing between lines
            [writer WriteElement: element];

            [writer WriteElement: [eb CreateTextNewLine]];  // New line

            element = [eb CreateTextRun: @"Hello World!"];
            gstate = [element GetGState]; 
            [gstate SetTextRenderMode: e_ptstroke_text];
            [gstate SetCharSpacing: -1.25];
            [gstate SetWordSpacing: -1.25];
            [writer WriteElement: element];

            [writer WriteElement: [eb CreateTextNewLine]];  // New line

            element = [eb CreateTextRun: @"Hello World!"];
            gstate = [element GetGState]; 
            [gstate SetCharSpacing: 0];
            [gstate SetWordSpacing: 0];
            [gstate SetLineWidth: 3];
            [gstate SetTextRenderMode: e_ptfill_stroke_text];
            [gstate SetStrokeColorSpace: [PTColorSpace CreateDeviceRGB]]; 
            [gstate SetStrokeColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0]];	// red
            [gstate SetFillColorSpace: [PTColorSpace CreateDeviceCMYK]]; 
            [gstate SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0]];;	// cyan
            [writer WriteElement: element];


            [writer WriteElement: [eb CreateTextNewLine]];  // New line

            // Set text as a clipping path to the image.
            element = [eb CreateTextRun: @"Hello World!"];
            gstate = [element GetGState];
            [gstate SetTextRenderMode: e_ptclip_text];
            [writer WriteElement: element];

            // Finish the block of text
            [writer WriteElement: [eb CreateTextEnd]];		

            // Draw an image that will be clipped by the above text
            [writer WriteElement: [eb CreateImageWithCornerAndScale: img x: 10 y: 100 hscale: 1300 vscale: 720]];

            [writer End];  // save changes to the current page
            [doc PagePushBack: page];

            // Start a new page ------------------------------------
            //
            // The example illustrates how to embed the external font in a PDF document. 
            // The example also shows how ElementReader can be used to copy and modify 
            // Elements between pages.

            PTElementReader *reader = [[PTElementReader alloc] init];

            // Start reading Elements from the last page. We will copy all Elements to 
            // a new page but will modify the font associated with text.
            [reader Begin: [doc GetPage: [doc GetPageCount]]];

            page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 1300 y2: 794]];

            [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to this page
            [eb Reset: [[PTGState alloc] init]];		// Reset the GState to default

            // Embed an external font in the document.
            PTFont *font = [PTFont CreateTrueTypeFont: [doc GetSDFDoc] font_path: @"../../TestFiles/font.ttf" embed: YES subset: YES];

            while ((element = [reader Next]) != NULL) 	// Read page contents
            {
                if ([element GetType] == e_pttext) 
                {
                    [[element GetGState] SetFont: font font_sz: 12];
                }

                [writer WriteElement: element];
            }

            [reader End];
            [writer End];  // save changes to the current page

            [doc PagePushBack: page];


            // Start a new page ------------------------------------
            //
            // The example illustrates how to embed the external font in a PDF document. 
            // The example also shows how ElementReader can be used to copy and modify 
            // Elements between pages.

            // Start reading Elements from the last page. We will copy all Elements to 
            // a new page but will modify the font associated with text.
            [reader Begin: [doc GetPage: [doc GetPageCount]]];

            page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 1300 y2: 794]];

            [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to this page
            [eb Reset: [[PTGState alloc] init]];		// Reset the GState to default

            // Embed an external font in the document.
            PTFont *font2 = [PTFont CreateType1Font: [doc GetSDFDoc] font_path: @"../../TestFiles/Misc-Fixed.pfa" embed: YES];

            while ((element = [reader Next]) != NULL) 	// Read page contents
            {
                if ([element GetType] == e_pttext) 
                {
                    [[element GetGState] SetFont: font2 font_sz: 12];
                }

                [writer WriteElement: element];
            }

            [reader End];
            [writer End];  // save changes to the current page
            [doc PagePushBack: page];


            // Start a new page ------------------------------------
            page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 792]];

        [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to this page
            [eb Reset: [[PTGState alloc] init]];		// Reset the GState to default

            // Begin writing a block of text
            element = [eb CreateTextBeginWithFont: [PTFont Create: [doc GetSDFDoc] type: e_pttimes_roman embed: NO] font_sz: 12];
            [element SetTextMatrix: 1.5 b: 0 c: 0 d: 1.5 h: 50 v: 600];
            [[element GetGState] SetLeading: 15];	// Set the spacing between lines
        [writer WriteElement: element];

            NSString* para = @"A PDF text object consists of operators that can show "
            "text strings, move the text position, and set text state and certain "
            "other parameters. In addition, there are three parameters that are "
            "defined only within a text object and do not persist from one text "
            "object to the next: Tm, the text matrix, Tlm, the text line matrix, "
            "Trm, the text rendering matrix, actually just an intermediate result "
            "that combines the effects of text state parameters, the text matrix "
            "(Tm), and the current transformation matrix";

            double para_end = para.length;
            double text_run = 0;
            
        double para_width = 300; // paragraph width is 300 units
            double cur_width = 0;

            while (text_run < para_end) 
            {
                double text_run_end = [para rangeOfString: @" " options: NSLiteralSearch range: NSMakeRange(text_run, para.length-text_run)].location;
            
                if (text_run_end == NSNotFound) text_run_end = para_end - 1;

            NSString *str = [para substringWithRange: NSMakeRange(text_run, text_run_end-text_run+1)];
                element = [eb CreateTextRun: str];
                if (cur_width + [element GetTextLength] < para_width) 
                {
                    [writer WriteElement: element];
                    cur_width = cur_width + [element GetTextLength];
                }
                else 
                {
                    [writer WriteElement: [eb CreateTextNewLine]];  // New line
                    element = [eb CreateTextRun: str];
                    cur_width = [element GetTextLength];
                    [writer WriteElement: element];
                }

                text_run = text_run_end+1;
            }
            
            // -----------------------------------------------------------------------
                // The following code snippet illustrates how to adjust spacing between 
            // characters (text runs).
            element = [eb CreateTextNewLine];
            [writer WriteElement: element];  // Skip 2 lines
        [writer WriteElement: element]; 
            
            [writer WriteElement: [eb CreateTextRun: @"An example of space adjustments between inter-characters:"]]; 
        [writer WriteElement: [eb CreateTextNewLine]]; 
            
            // Write string "AWAY" without space adjustments between characters.
            element = [eb CreateTextRun: @"AWAY"];
            [writer WriteElement: element];  
            
        [writer WriteElement: [eb CreateTextNewLine]]; 
            
            // Write string "AWAY" with space adjustments between characters.
            element = [eb CreateTextRun: @"A"];
            [writer WriteElement: element];
            
            element = [eb CreateTextRun: @"W"];
            [element SetPosAdjustment: 140];
            [writer WriteElement: element];
            
            element = [eb CreateTextRun: @"A"];
            [element SetPosAdjustment: 140];
            [writer WriteElement: element];
            
            element = [eb CreateTextRun: @"Y again"];
            [element SetPosAdjustment: 115];
            [writer WriteElement: element];
            
            // Draw the same strings using direct content output...
            [writer Flush];  // flush pending Element writing operations.

            // You can also write page content directly to the content stream using 
            // ElementWriter.WriteString(...) and ElementWriter.WriteBuffer(...) methods.
            // Note that if you are planning to use these functions you need to be familiar
            // with PDF page content operators (see Appendix A in PDF Reference Manual). 
            // Because it is easy to make mistakes during direct output we recommend that 
            // you use ElementBuilder and Element interface instead.

            [writer WriteString: @"T* T* "]; // Skip 2 lines
            [writer WriteString: @"(Direct output to PDF page content stream:) Tj  T* "];
            [writer WriteString: @"(AWAY) Tj T* "];
            [writer WriteString: @"[(A)140(W)140(A)115(Y again)] TJ "];
            // Finish the block of text
            [writer WriteElement: [eb CreateTextEnd]];		

            [writer End];  // save changes to the current page
            [doc PagePushBack: page];

            // Start a new page ------------------------------------

            // Image Masks
            //
            // In the opaque imaging model, images mark all areas they occupy on the page as 
            // if with opaque paint. All portions of the image, whether black, white, gray, 
            // or color, completely obscure any marks that may previously have existed in the 
            // same place on the page.
            // In the graphic arts industry and page layout applications, however, it is common 
            // to crop or 'mask out' the background of an image and then place the masked image 
            // on a different background, allowing the existing background to show through the 
            // masked areas. This sample illustrates how to use image masks. 

            page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 792]];
            [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to this page

            // Create the Image Mask
            PTMappedFile *imgf = [[PTMappedFile alloc] initWithFilename: @"../../TestFiles/imagemask.dat"];
            PTFilterReader *mask_read = [[PTFilterReader alloc] initWithFilter: imgf];

            PTColorSpace *device_gray = [PTColorSpace CreateDeviceGray];
            PTImage *mask = [PTImage CreateWithStreamAndFormat: [doc GetSDFDoc] image_data: mask_read width: 64 height: 64 bpc: 1 color_space: device_gray input_format: e_ptascii_hex];
            
            [[mask GetSDFObj] PutBool: @"ImageMask" value: YES];

            element = [eb CreateRect: 0 y: 0 width: 612 height: 794];
        [element SetPathStroke: NO];
            [element SetPathFill: YES];
            [[element GetGState] SetFillColorSpace: device_gray];
            [[element GetGState] SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 0.8 y: 0 z: 0 w:0]];
            [writer WritePlacedElement: element];

            element = [eb CreateImageWithMatrix: mask mtx: [[PTMatrix2D alloc] initWithA: 200 b: 0 c: 0 d: -200 h: 40 v: 680]];
            [[element GetGState] SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 0.1 y: 0 z: 0 w:0]];
            [writer WritePlacedElement: element];

            [[element GetGState] SetFillColorSpace: [PTColorSpace CreateDeviceRGB]];
            [[element GetGState] SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w:0]];
            element = [eb CreateImageWithMatrix: mask mtx: [[PTMatrix2D alloc] initWithA: 200 b: 0 c: 0 d: -200 h: 320 v: 680]];
            [writer WritePlacedElement: element];

            [[element GetGState] SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w:0]];
            element = [eb CreateImageWithMatrix: mask mtx: [[PTMatrix2D alloc] initWithA: 200 b: 0 c: 0 d: -200 h: 40 v: 380]];
            [writer WritePlacedElement: element];

            {
                // This sample illustrates Explicit Masking. 
                PTImage *img = [PTImage CreateWithFile: [doc GetSDFDoc] filename: @"../../TestFiles/peppers.jpg" encoder_hints: [[PTObj alloc] init]];

                // mask is the explicit mask for the primary (base) image
                [img SetMaskWithImage: mask];

                element = [eb CreateImageWithMatrix: img mtx: [[PTMatrix2D alloc] initWithA: 200 b: 0 c: 0 d: -200 h: 320 v: 380]];
                [writer WritePlacedElement: element];
            }

            [writer End];  // save changes to the current page
            [doc PagePushBack: page];

            // Transparency sample ----------------------------------
            
            // Start a new page -------------------------------------
            page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 792]];
            [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to this page
            [eb Reset: [[PTGState alloc] init]];		// Reset the GState to default

            // Write some transparent text at the bottom of the page.
            element = [eb CreateTextBeginWithFont: [PTFont Create: [doc GetSDFDoc] type: e_pttimes_roman embed: NO] font_sz: 100];

            // Set the text knockout attribute. Text knockout must be set outside of 
            // the text group.
            gstate = [element GetGState];
            [gstate SetTextKnockout: NO];
            [gstate SetBlendMode: e_ptbl_difference];
            [writer WriteElement: element];

            element = [eb CreateTextRun: @"Transparency"];
            [element SetTextMatrix: 1 b: 0 c: 0 d: 1 h: 30 v: 30];
            gstate = [element GetGState];
            [gstate SetFillColorSpace: [PTColorSpace CreateDeviceCMYK]];
            [gstate SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w:0]];

            [gstate SetFillOpacity: 0.5];
            [writer WriteElement: element];

            // Write the same text on top the old; shifted by 3 points
            [element SetTextMatrix: 1 b: 0 c: 0 d: 1 h: 33 v: 33];
        [gstate SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w:0]];
            [gstate SetFillOpacity: 0.5];

            [writer WriteElement: element];
        [writer WriteElement: [eb CreateTextEnd]];

            // Draw three overlapping transparent circles.
            [eb PathBegin];		// start constructing the path
            [eb MoveTo: 459.223 y: 505.646];
            [eb CurveTo: 459.223 cy1: 415.841 cx2: 389.85 cy2: 343.04 x2: 304.273 y2: 343.04];
        [eb CurveTo: 218.697 cy1: 343.04 cx2: 149.324 cy2: 415.841 x2: 149.324 y2: 505.646];
            [eb CurveTo: 149.324 cy1: 595.45 cx2: 218.697 cy2: 668.25 x2: 304.273 y2: 668.25];
            [eb CurveTo: 389.85 cy1: 668.25 cx2: 459.223 cy2: 595.45 x2: 459.223 y2: 505.646];
            element = [eb PathEnd];
            [element SetPathFill: YES];

            gstate = [element GetGState];
            [gstate SetFillColorSpace: [PTColorSpace CreateDeviceRGB]];
            [gstate SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w:0]];                     // Blue Circle

            [gstate SetBlendMode: e_ptbl_normal];
            [gstate SetFillOpacity: 0.5];
            [writer WriteElement: element];

            // Translate relative to the Blue Circle
            [gstate SetTransform: 1 b: 0 c: 0 d: 1 h: 113 v: -185];                
            [gstate SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w:0]];                     // Green Circle
            [gstate SetFillOpacity: 0.5];
            [writer WriteElement: element];

            // Translate relative to the Green Circle
            [gstate SetTransform: 1 b: 0 c: 0 d: 1 h: -220 v: 0];
            [gstate SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w:0]];                     // Red Circle
            [gstate SetFillOpacity: 0.5];
            [writer WriteElement: element];

            [writer End];  // save changes to the current page
            [doc PagePushBack: page];

            // End page ------------------------------------

            [doc SaveToFile: @"../../TestFiles/Output/element_builder.pdf" flags: e_ptremove_unused];
            // doc.Save((output_path + "element_builder.pdf").c_str(), Doc::e_ptlinearized, NULL);
            NSLog(@"Done. Result saved in element_builder.pdf...");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        return ret;
    }
}
