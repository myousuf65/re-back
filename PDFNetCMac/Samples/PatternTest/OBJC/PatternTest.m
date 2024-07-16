//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

PTObj* CreateTilingPattern(PTPDFDoc* doc) 
{
	PTElementWriter *writer = [[PTElementWriter alloc] init];	
	PTElementBuilder *eb = [[PTElementBuilder alloc] init];

	// Create a new pattern content stream - a heart. ------------
	[writer WriterBeginWithSDFDoc: [doc GetSDFDoc] compress: YES];
	[eb PathBegin];
	[eb MoveTo: 0 y: 0];
	[eb CurveTo: 500 cy1: 500 cx2: 125 cy2: 625 x2: 0 y2: 500];
    [eb CurveTo: -125 cy1: 625 cx2: -500 cy2: 500 x2: 0 y2: 0];
	PTElement *heart = [eb PathEnd];
    [heart SetPathFill: YES]; 
	
	// Set heart color to red.
	[[heart GetGState] SetFillColorSpace: [PTColorSpace CreateDeviceRGB]]; 
	[[heart GetGState] SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0]]; 
	[writer WriteElement: heart];

	PTObj * pattern_dict = [writer End];

	// Initialize pattern dictionary. For details on what each parameter represents please 
	// refer to Table 4.22 (Section '4.6.2 Tiling Patterns') in PDF Reference Manual.
	[pattern_dict PutName: @"Type" name: @"Pattern"];
	[pattern_dict PutNumber: @"PatternType" value: 1];

	// TilingType - Constant spacing.
	[pattern_dict PutNumber: @"TilingType" value: 1]; 

	// This is a Type1 pattern - A colored tiling pattern.
	[pattern_dict PutNumber: @"PaintType" value: 1];

	// Set bounding box
	[pattern_dict PutRect: @"BBox" x1: -253 y1: 0 x2: 253 y2: 545];

	// Create and set the matrix
	PTMatrix2D *pattern_mtx = [[PTMatrix2D alloc] initWithA: 0.04 b: 0 c: 0 d: 0.04 h: 0 v: 0];
	[pattern_dict PutMatrix: @"Matrix" value: pattern_mtx];

	// Set the desired horizontal and vertical spacing between pattern cells, 
	// measured in the pattern coordinate system.
	[pattern_dict PutNumber: @"XStep" value: 1000];
	[pattern_dict PutNumber: @"YStep" value: 1000];
	
	return pattern_dict; // finished creating the Pattern resource
}

PTObj * CreateImageTilingPattern(PTPDFDoc* doc) 
{
	PTElementWriter *writer = [[PTElementWriter alloc] init];	
	PTElementBuilder *eb = [[PTElementBuilder alloc] init];

	// Create a new pattern content stream - a single bitmap object ----------
	[writer WriterBeginWithSDFDoc: [doc GetSDFDoc] compress: YES];
	PTImage *image = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/dice.jpg"];
	PTElement *img_element = [eb CreateImageWithCornerAndScale: image x: 0 y: 0 hscale: [image GetImageWidth] vscale: [image GetImageHeight]];
	[writer WritePlacedElement: img_element];
	PTObj * pattern_dict = [writer End];

	// Initialize pattern dictionary. For details on what each parameter represents please 
	// refer to Table 4.22 (Section '4.6.2 Tiling Patterns') in PDF Reference Manual.
	[pattern_dict PutName: @"Type" name: @"Pattern"];
	[pattern_dict PutNumber: @"PatternType" value: 1];

	// TilingType - Constant spacing.
	[pattern_dict PutNumber: @"TilingType" value: 1]; 

	// This is a Type1 pattern - A colored tiling pattern.
	[pattern_dict PutNumber: @"PaintType" value: 1];

	// Set bounding box
	[pattern_dict PutRect: @"BBox" x1: -253 y1: 0 x2: 253 y2: 545];

	// Create and set the matrix
	PTMatrix2D *pattern_mtx = [[PTMatrix2D alloc] initWithA: 0.3 b: 0 c: 0 d: 0.3 h: 0 v:0];
	[pattern_dict PutMatrix: @"Matrix" value: pattern_mtx];

	// Set the desired horizontal and vertical spacing between pattern cells, 
	// measured in the pattern coordinate system.
	[pattern_dict PutNumber: @"XStep" value: 300];
	[pattern_dict PutNumber: @"YStep" value: 300];
	
	return pattern_dict; // finished creating the Pattern resource
}

PTObj * CreateAxialShading(PTPDFDoc* doc) 
{
	// Create a new Shading object ------------
	PTObj * pattern_dict = [doc CreateIndirectDict];

	// Initialize pattern dictionary. For details on what each parameter represents 
	// please refer to Tables 4.30 and 4.26 in PDF Reference Manual
	[pattern_dict PutName: @"Type" name: @"Pattern"];
	[pattern_dict PutNumber: @"PatternType" value: 2]; // 2 stands for shading
	
	PTObj * shadingDict = [pattern_dict PutDict: @"Shading"];
	[shadingDict PutNumber: @"ShadingType" value: 2];
	[shadingDict PutName: @"ColorSpace" name: @"DeviceCMYK"];
	
	// pass the coordinates of the axial shading to the output
	PTObj * shadingCoords = [shadingDict PutArray: @"Coords"];
	[shadingCoords PushBackNumber: 0];
	[shadingCoords PushBackNumber: 0];
	[shadingCoords PushBackNumber: 612];
	[shadingCoords PushBackNumber: 794];

	// pass the function to the axial shading
	PTObj * function = [shadingDict PutDict: @"Function"];
	PTObj * C0 = [function PutArray: @"C0"];
	[C0 PushBackNumber: 1];
	[C0 PushBackNumber: 0];
    [C0 PushBackNumber: 0];
    [C0 PushBackNumber: 0];

	PTObj * C1 = [function PutArray: @"C1"];
	[C1 PushBackNumber: 0];
	[C1 PushBackNumber: 1];
	[C1 PushBackNumber: 0];
	[C1 PushBackNumber: 0];
	
	PTObj * domain = [function PutArray: @"Domain"];
	[domain PushBackNumber: 0];
	[domain PushBackNumber: 1];

	[function PutNumber: @"FunctionType" value: 2];
	[function PutNumber: @"N" value: 1];

	return pattern_dict;
}


int main(int argc, char *argv[])
{
    @autoreleasepool {
        [PTPDFNet Initialize: 0];

        @try  
        {	 
            PTPDFDoc *doc = [[PTPDFDoc alloc] init];
            PTElementWriter *writer = [[PTElementWriter alloc] init];	
            PTElementBuilder *eb = [[PTElementBuilder alloc] init];

            // The following sample illustrates how to create and use tiling patterns
            PTPage *page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 792]];
            [writer Begin: page placement: e_ptoverlay page_coord_sys: YES];

            PTElement *element = [eb CreateTextBeginWithFont: [PTFont Create: [doc GetSDFDoc] type: e_pttimes_bold embed: NO] font_sz: 1];
            [writer WriteElement: element];  // Begin the text block

            element = [eb CreateTextRun: @"G"];
            [element SetTextMatrix: 720 b: 0 c: 0 d: 720 h: 20 v: 240];
            PTGState *gs = [element GetGState];
            [gs SetTextRenderMode: e_ptfill_stroke_text];
            [gs SetLineWidth: 4];

            // Set the fill color space to the Pattern color space. 
            [gs SetFillColorSpace: [PTColorSpace CreatePattern]];
            [gs SetFillColorWithPattern: [[PTPatternColor alloc] initWithPattern: CreateTilingPattern(doc)]];

            [writer WriteElement: element];
            [writer WriteElement: [eb CreateTextEnd]]; // Finish the text block

            [writer End];	// Save the page
            [doc PagePushBack: page];
            //-----------------------------------------------

            /// The following sample illustrates how to create and use image tiling pattern
            page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 792]];
            [writer Begin: page placement: e_ptoverlay page_coord_sys: YES];

            [eb Reset: [[PTGState alloc] init]];
            element = [eb CreateRect: 0 y: 0 width: 612 height: 794];

            // Set the fill color space to the Pattern color space. 
            gs = [element GetGState];
            [gs SetFillColorSpace: [PTColorSpace CreatePattern]];
            [gs SetFillColorWithPattern: [[PTPatternColor alloc] initWithPattern: CreateImageTilingPattern(doc)]];
            [element SetPathFill: YES];		

            [writer WriteElement: element];

            [writer End];	// Save the page
            [doc PagePushBack: page];
            //-----------------------------------------------

            /// The following sample illustrates how to create and use PDF shadings
            page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 792]];
            [writer Begin: page placement: e_ptoverlay page_coord_sys: YES];
        
            [eb Reset: [[PTGState alloc] init]];
            element = [eb CreateRect: 0 y: 0 width: 612 height: 794];

            // Set the fill color space to the Pattern color space. 
            gs = [element GetGState];
            [gs SetFillColorSpace: [PTColorSpace CreatePattern]];
            [gs SetFillColorWithPattern: [[PTPatternColor alloc] initWithPattern: CreateAxialShading(doc)]];
            [element SetPathFill: YES];		
        
            [writer WriteElement: element];
        
            [writer End];	// Save the page
            [doc PagePushBack: page];
            //-----------------------------------------------

            [doc SaveToFile: @"../../TestFiles/Output/patterns.pdf" flags: e_ptremove_unused];
            NSLog(@"Done. Result saved in patterns.pdf...");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
        }
    }
}

