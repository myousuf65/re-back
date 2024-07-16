//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//-----------------------------------------------------------------------------------
// This sample demonstrates how to create layers in PDF.
// The sample also shows how to extract and render PDF layers in documents 
// that contain optional content groups (OCGs)
//
// With the introduction of PDF version 1.5 came the concept of Layers. 
// Layers, or as they are more formally known Optional Content Groups (OCGs),
// refer to sections of content in a PDF document that can be selectively 
// viewed or hidden by document authors or consumers. This capability is useful 
// in CAD drawings, layered artwork, maps, multi-language documents etc.
// 
// Notes: 
// ---------------------------------------
// - This sample is using CreateLayer() utility method to create new OCGs. 
//   CreateLayer() is relatively basic, however it can be extended to set 
//   other optional entries in the 'OCG' and 'OCProperties' dictionary. For 
//   a complete listing of possible entries in OC dictionary please refer to 
//   section 4.10 'Optional Content' in the PDF Reference Manual.
// - The sample is grouping all layer content into separate Form XObjects. 
//   Although using PDFNet is is also possible to specify Optional Content in 
//   Content Streams (Section 4.10.2 in PDF Reference), Optional Content in  
//   XObjects results in PDFs that are cleaner, less-error prone, and faster 
//   to process.
//-----------------------------------------------------------------------------------

PTObj * CreateGroup1(PTPDFDoc *doc, PTObj * layer);
PTObj * CreateGroup2(PTPDFDoc *doc, PTObj * layer);
PTObj * CreateGroup3(PTPDFDoc *doc, PTObj * layer);
PTGroup *CreateLayer(PTPDFDoc *doc, NSString *layer_name);

int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        @try  
        {	 
            PTPDFDoc *doc = [[PTPDFDoc alloc] init];

            // Create three layers...
            PTGroup *image_layer = CreateLayer(doc, @"Image Layer");
            PTGroup *text_layer = CreateLayer(doc, @"Text Layer");
            PTGroup *vector_layer = CreateLayer(doc, @"Vector Layer");

            // Start a new page ------------------------------------
            PTPage *page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2:792]];

            PTElementBuilder *builder = [[PTElementBuilder alloc] init];	// ElementBuilder is used to build new Element objects
            PTElementWriter *writer= [[PTElementWriter alloc] init];	// ElementWriter is used to write Elements to the page	
            [writer WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];		// Begin writing to the page

            // Add new content to the page and associate it with one of the layers.
            PTElement *element = [builder CreateFormWithObj: CreateGroup1(doc, [image_layer GetSDFObj])];
            [writer WriteElement: element];

            element = [builder CreateFormWithObj: CreateGroup2(doc, [vector_layer GetSDFObj])];
            [writer WriteElement: element];

            // Add the text layer to the page...
            if (NO)  // set to true to enable 'ocmd' example.
            {
                // A bit more advanced example of how to create an OCMD text layer that 
                // is visible only if text, image and path layers are all 'ON'.
                // An example of how to set 'Visibility Policy' in OCMD.
                PTObj * ocgs = [doc CreateIndirectArray];
                [ocgs PushBack: [image_layer GetSDFObj]];
                [ocgs PushBack: [vector_layer GetSDFObj]];
                [ocgs PushBack: [text_layer GetSDFObj]];
                PTOCMD *text_ocmd = [PTOCMD Create: doc ocgs: ocgs vis_policy: e_ptAllOn];
                element = [builder CreateFormWithObj: CreateGroup3(doc, [text_ocmd GetSDFObj])];
            }
            else {
                element = [builder CreateFormWithObj: CreateGroup3(doc, [text_layer GetSDFObj])];
            }
            [writer WriteElement: element];

            // Add some content to the page that does not belong to any layer...
            // In this case this is a rectangle representing the page border.
            element = [builder CreateRect: 0 y: 0 width: [page GetPageWidth: e_ptcrop] height: [page GetPageHeight: e_ptcrop]];
            [element SetPathFill: NO];
            [element SetPathStroke: YES];
            [[element GetGState] SetLineWidth: 40];
            [writer WriteElement: element];

            [writer End];  // save changes to the current page
            [doc PagePushBack: page];

            // Set the default viewing preference to display 'Layer' tab.
            PTPDFDocViewPrefs *prefs = [doc GetViewPrefs];
            [prefs SetPageMode: e_ptUseOC];

            [doc SaveToFile: @"../../TestFiles/Output/pdf_layers.pdf" flags: e_ptlinearized];
            NSLog(@"Done.");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        // The following is a code snippet shows how to selectively render 
        // and export PDF layers.
        @try  
        {	 
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/pdf_layers.pdf"];
            [doc InitSecurityHandler];

            if (![doc HasOC]) {
            NSLog(@"The document does not contain 'Optional Content'");
            }
            else {
                PTConfig *init_cfg = [doc GetOCGConfig];
                PTContext *ctx = [[PTContext alloc] initWithConfig: init_cfg];

                PTPDFDraw *pdfdraw = [[PTPDFDraw alloc] initWithDpi: 92];
                [pdfdraw SetImageSize: 1000 height: 1000 preserve_aspect_ratio: YES];
                [pdfdraw SetOCGContext: ctx]; // Render the page using the given OCG context.

                PTPage *page = [doc GetPage: 1]; // Get the first page in the document.
                [pdfdraw  Export:page filename: @"../../TestFiles/Output/pdf_layers_default.png" format: @"PNG"];

                // Disable drawing of content that is not optional (i.e. is not part of any layer).
                [ctx SetNonOCDrawing: NO];
            
                // Now render each layer in the input document to a separate image.
                PTObj * ocgs = [doc GetOCGs]; // Get the array of all OCGs in the document.
                if (ocgs != 0) {
                    unsigned long i, sz = [ocgs Size];
                    for (i=0; i<sz; ++i) {
                        PTGroup *ocg = [[PTGroup alloc] initWithOcg: [ocgs GetAt: i]];
                        [ctx ResetStates: NO];
                        [ctx SetState: ocg state: YES];
                        NSString* fname = [@"../../TestFiles/Output/pdf_layers_" stringByAppendingFormat: @"%@.png", [ocg GetName]];
                        //[pdfdraw  page filename: fname format: @"PNG"];
                    }
                }

                // Now draw content that is not part of any layer...
            [ctx SetNonOCDrawing: YES];
                [ctx SetOCDrawMode: e_ptNoOC];
                //[pdfdraw  page filename: @"../../TestFiles/Output/pdf_layers_non_oc.png" format: @"PNG" ];
            }

            NSLog(@"Done.");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        return ret;
    }
}


// A utility function used to add new Content Groups (Layers) to the document.
PTGroup *CreateLayer(PTPDFDoc *doc, NSString *layer_name)
{
	PTGroup *grp = [PTGroup Create: doc name: layer_name];
	PTConfig *cfg = [doc GetOCGConfig];
	if (![cfg IsValid]) {
		cfg = [PTConfig Create: doc default_config: YES];
		[cfg SetName: @"Default"];
	}

	// Add the new OCG to the list of layers that should appear in PDF viewer GUI.
	PTObj * layer_order_array = [cfg GetOrder];
	if (!layer_order_array) {
        layer_order_array = [doc CreateIndirectArray];
		[cfg SetOrder: layer_order_array];
	}
	[layer_order_array PushBack: [grp GetSDFObj]];

	return grp;
}

// Creates some content (3 images) and associate them with the image layer
PTObj * CreateGroup1(PTPDFDoc *doc, PTObj * layer) 
{
	PTElementWriter *writer = [[PTElementWriter alloc] init];
	[writer WriterBeginWithSDFDoc: [doc GetSDFDoc] compress: YES];

	// Create an Image that can be reused in the document or on the same page.		
	PTImage *img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/peppers.jpg"];

	PTElementBuilder *builder = [[PTElementBuilder alloc] init];
	PTElement *element = [builder CreateImageWithMatrix: img mtx: [[PTMatrix2D alloc] initWithA: [img GetImageWidth]/2 b: -145 c: 20 d: [img GetImageHeight]/2 h: 200 v: 150]];
	[writer WritePlacedElement: element];

	PTGState *gstate = [element GetGState];	// use the same image (just change its matrix)
	[gstate SetTransform: 200 b: 0 c: 0 d: 300 h: 50 v: 450];
	[writer WritePlacedElement: element];

	// use the same image again (just change its matrix).
	[writer WritePlacedElement: [builder CreateImageWithCornerAndScale: img x: 300 y: 600 hscale: 200 vscale: -150]];

	PTObj * grp_obj = [writer End];	

	// Indicate that this form (content group) belongs to the given layer (OCG).
	[grp_obj PutName: @"Subtype" name: @"Form"];
	[grp_obj Put: @"OC" obj: layer];	
	[grp_obj PutRect: @"BBox" x1: 0 y1: 0 x2: 1000 y2: 1000];  // Set the clip box for the content.

	return grp_obj;
}

// Creates some content (a path in the shape of a heart) and associate it with the vector layer
PTObj * CreateGroup2(PTPDFDoc *doc, PTObj * layer) 
{
	PTElementWriter *writer = [[PTElementWriter alloc] init];
	[writer WriterBeginWithSDFDoc: [doc GetSDFDoc] compress: YES];

	// Create a path object in the shape of a heart.
	PTElementBuilder *builder = [[PTElementBuilder alloc] init];
	[builder PathBegin];		// start constructing the path
	[builder MoveTo: 306 y: 396];
	[builder CurveTo: 681 cy1: 771 cx2: 399.75 cy2: 864.75 x2: 306 y2: 771];
	[builder CurveTo: 212.25 cy1: 864.75 cx2: -69 cy2: 771 x2: 306 y2: 396];
	[builder ClosePath];
	PTElement *element = [builder PathEnd]; // the path geometry is now specified.

	// Set the path FILL color space and color.
	[element SetPathFill: YES];
	PTGState *gstate = [element GetGState];
	[gstate SetFillColorSpace: [PTColorSpace CreateDeviceCMYK]]; 
	[gstate SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0]];  // cyan

	// Set the path STROKE color space and color.
	[element SetPathStroke: YES]; 
	[gstate SetStrokeColorSpace: [PTColorSpace CreateDeviceRGB]]; 
	[gstate SetStrokeColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0]];  // red
	[gstate SetLineWidth: 20];

	[gstate SetTransform: 0.5 b: 0 c: 0 d: 0.5 h: 280 v: 300];

	[writer WriteElement: element];

	PTObj * grp_obj = [writer End];	

	// Indicate that this form (content group) belongs to the given layer (OCG).
	[grp_obj PutName: @"Subtype" name: @"Form"];
	[grp_obj Put: @"OC" obj: layer];
	[grp_obj PutRect: @"BBox" x1: 0 y1: 0 x2: 1000 y2: 1000]; 	// Set the clip box for the content.

	return grp_obj;
}

// Creates some text and associate it with the text layer
PTObj * CreateGroup3(PTPDFDoc *doc, PTObj * layer) 
{
	PTElementWriter *writer = [[PTElementWriter alloc] init];
	[writer WriterBeginWithSDFDoc: [doc GetSDFDoc] compress: YES];

	// Create a path object in the shape of a heart.
	PTElementBuilder *builder = [[PTElementBuilder alloc] init];

	// Begin writing a block of text
	PTElement *element = [builder CreateTextBeginWithFont: [PTFont Create: [doc GetSDFDoc] type: e_pttimes_roman embed: NO] font_sz: 120];
	[writer WriteElement: element];

	element = [builder CreateTextRun: @"A text layer!"];

	// Rotate text 45 degrees, than translate 180 pts horizontally and 100 pts vertically.
	PTMatrix2D *transform = [PTMatrix2D RotationMatrix: -45 *  (3.1415/ 180.0)];
	[transform Concat: 1 b: 0 c: 0 d: 1 h: 180 v: 100];  
	[element SetTextMatrixWithMatrix2D: transform];

	[writer WriteElement: element];
	[writer WriteElement: [builder CreateTextEnd]];

	PTObj * grp_obj = [writer End];	

	// Indicate that this form (content group) belongs to the given layer (OCG).
	[grp_obj PutName: @"Subtype" name: @"Form"];
	[grp_obj Put: @"OC" obj: layer];
	[grp_obj PutRect: @"BBox" x1: 0 y1: 0 x2: 1000 y2: 1000]; 	// Set the clip box for the content.

	return grp_obj;
}

