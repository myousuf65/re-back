//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//std::string output_path = "../../TestFiles/Output/";
//std::string input_path = "../../TestFiles/";

void AnnotationHighLevelAPI(PTPDFDoc *doc)
{
	// The following code snippet traverses all annotations in the document
	NSLog(@"Traversing all annotations in the document..."); 

	int page_num = 1;
    PTPageIterator* itr;
	for (itr = [doc GetPageIterator: 1]; [itr HasNext]; [itr Next]) 
	{
		NSLog(@"Page %d: ",  page_num++); 

		PTPage* page = [itr Current];
		int num_annots = [page GetNumAnnots];
        int i = 0;
		for (i=0; i<num_annots; ++i) 
		{
			PTAnnot* annot = [page GetAnnot: i];
			if (![annot IsValid]) continue;
			NSLog(@"Annot Type: %@", [[[[annot GetSDFObj] Get: @"Subtype"] Value] GetName]); 

			PTPDFRect* bbox = [annot GetRect];
			
			NSNumberFormatter *formatter = [[NSNumberFormatter alloc] init];
			formatter.numberStyle = NSNumberFormatterDecimalStyle;
			formatter.maximumFractionDigits = 4;
			NSString *x1 = [formatter stringFromNumber:[NSNumber numberWithDouble:[bbox GetX1]]];
			NSString *y1 = [formatter stringFromNumber:[NSNumber numberWithDouble:[bbox GetY1]]];
			NSString *x2 = [formatter stringFromNumber:[NSNumber numberWithDouble:[bbox GetX2]]];
			NSString *y2 = [formatter stringFromNumber:[NSNumber numberWithDouble:[bbox GetY2]]];

			NSLog(@"  Position: %s, %s, %s, %s", [x1 UTF8String], [y1 UTF8String], [x2 UTF8String], [y2 UTF8String]);

			switch ([annot GetType]) 
			{
			case e_ptLink: 
				{
					PTLink *link = [[PTLink alloc] initWithAnn: annot];
					PTAction* action = [link GetAction];
					if (![action IsValid])
					{
						continue;
					}
					if ([action GetType] == e_ptGoTo) 
					{
						PTDestination* dest = [action GetDest];
						if (![dest IsValid]) {
							NSLog(@"  Destination is not valid\n");
						}
						else {
							int page_num = [[dest GetPage] GetIndex];
							NSLog(@"  Links to: page number %d in this document\n", page_num);
						}
					}
                    else if ([action GetType] == e_ptURI) 
					{
                        NSString *uri = [[[[action GetSDFObj] Get: @"URI"] Value] GetAsPDFText];
                        NSLog(@"  Links to: %@\n", uri);
					}
					// ...
                }
				break; 
			case e_ptWidget:
				break; 
			case e_ptFileAttachment:
				break; 
				// ...
			default:
				break; 
			}
		}
	}

	// Use the high-level API to create new annotations.
    PTPage *first_page = [doc GetPage:1];

	// Create a hyperlink...
    PTLink *hyperlink = [PTLink CreateWithAction: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 85 y1: 570 x2: 503 y2: 524] action: [PTAction CreateURI: [doc GetSDFDoc] uri: @"http://www.pdftron.com"]];
	[first_page AnnotPushBack: hyperlink];

	// Create an intra-document link...
	PTAction *goto_page_3 = [PTAction CreateGoto: [PTDestination CreateFitH: [doc GetPage:3] top: 0]];
	PTLink *link = [PTLink CreateWithAction: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 85 y1: 458 x2: 503 y2: 502] action: goto_page_3];

	// Set the annotation border width to 3 points...
	PTBorderStyle *border_style = [[PTBorderStyle alloc] initWithS: e_ptsolid b_width: 3 b_hr:0 b_vr:0];
	//[link SetBorderStyle: border_style];
    [link SetColor: [[PTColorPt alloc] initWithX: 0 y:0 z:1 w:0] numcomp: 3];

	// Add the new annotation to the first page
	[first_page AnnotPushBack: link];

	// Create a stamp annotation ...
	PTRubberStamp *stamp = [PTRubberStamp Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 30 y1: 30 x2: 300 y2: 200] icon: e_ptDraft];
	[stamp SetRubberStampIconName: @"Draft"];
	[first_page AnnotPushBack: stamp];

	// Create a file attachment annotation (embed the 'peppers.jpg').
	PTFileAttachment *file_attach = [PTFileAttachment CreateFileAttchWithPath: [doc GetSDFDoc] pos:[[PTPDFRect alloc] initWithX1: 80 y1: 280 x2: 200 y2: 320] path: @"../../TestFiles/peppers.jpg" icon_name: e_ptPushPin];
	[first_page AnnotPushBack: file_attach];

	PTInk* ink = [PTInk Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 110 y1: 10 x2: 300 y2: 200]];
	PTPDFPoint* pt3 = [[PTPDFPoint alloc] initWithPx: 110 py: 10];
	//pt3.x = 110; pt3.y = 10;
	[ink SetPoint: 0 pointindex: 0 pt: pt3];
	[pt3 setX: 150]; [pt3 setY: 50];
	[ink SetPoint: 0 pointindex: 1 pt: pt3];
	[pt3 setX: 190]; [pt3 setY: 60];
	[ink SetPoint: 0 pointindex: 2 pt: pt3];
	[pt3 setX: 180]; [pt3 setY: 90];
	[ink SetPoint: 1 pointindex: 0 pt: pt3];
	[pt3 setX: 190]; [pt3 setY: 95];
	[ink SetPoint: 1 pointindex: 1 pt: pt3];
	[pt3 setX: 200]; [pt3 setY: 100];
	[ink SetPoint: 1 pointindex: 2 pt: pt3];
	[pt3 setX: 166]; [pt3 setY: 86];
	[ink SetPoint: 2 pointindex: 0 pt: pt3];
	[pt3 setX: 196]; [pt3 setY: 96];
	[ink SetPoint: 2 pointindex: 1 pt: pt3];
	[pt3 setX: 221]; [pt3 setY: 121];
	[ink SetPoint: 2 pointindex: 2 pt: pt3];
	[pt3 setX: 288]; [pt3 setY: 188];
	[ink SetPoint: 2 pointindex: 3 pt: pt3];
	[ink SetColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 1 w: 0] numcomp: 3];
	[first_page AnnotPushBack: ink];

	//Create a Polygon annotation..
	//Polygon *poly2 = [Polygon Create: doc PDFRect[100, 60, 300, 480]];
	//Point *pllp;
	//pllp.x=105;
	//pllp.y=68;
	//poly2.SetVertex(0, pllp);
	//pllp.x=155;
	//pllp.y=92;
	//poly2.SetVertex(1, pllp);
	//pllp.x=189;
	//pllp.y=133;
	//poly2.SetVertex(2, pllp);
	//pllp.x=200;
	//pllp.y=140;
	//poly2.SetVertex(3, pllp);
	//pllp.x=230;
	//pllp.y=389;
	//poly2.SetVertex(4, pllp);
	//pllp.x=300;
	//pllp.y=405;
	//poly2.SetVertex(5, pllp);
	//poly2.SetColor(ColorPt(0, 0, 1), 3);
	//poly2.SetInteriorColor(ColorPt(1, 0, 0), 3);
	//first_page.AnnotPushBack(poly2);

	// ...
}

void AnnotationLowLevelAPI(PTPDFDoc *doc)
{
	PTPage* page = [[doc GetPageIterator: 1] Current]; 

	PTObj* annots = [page GetAnnots];

	if (!annots) 
	{
		// If there are no annotations, create a new annotation 
		// array for the page.
		annots = [doc CreateIndirectArray];  
		[[page GetSDFObj] Put: @"Annots" obj:annots];
	}

	// Create a Text annotation
	PTObj * annot = [doc CreateIndirectDict];
	[annot PutName: @"Subtype" name: @"Text"];
	[annot PutBool: @"Open" value: YES];
	[annot PutString: @"Contents" value: @"The quick brown fox ate the lazy mouse."];
	[annot PutRect: @"Rect" x1:266 y1:116 x2:430 y2:204];

	// Insert the annotation in the page annotation array
	[annots PushBack:annot ];

	// Create a Link annotation
	PTObj * link1 = [doc CreateIndirectDict];
	[link1 PutName: @"Subtype" name: @"Link"];
	PTDestination *dest = [PTDestination CreateFit: [doc GetPage: 2]];
	[link1 Put: @"Dest" obj:[dest GetSDFObj]];
	[link1 PutRect: @"Rect" x1:85 y1:705 x2:503 y2:661];
	[annots PushBack: link1];

	// Create another Link annotation
	PTObj * link2 = [doc CreateIndirectDict];
	[link2 PutName: @"Subtype" name: @"Link"];
	PTDestination *dest2 = [PTDestination CreateFit: [doc GetPage:3]];
	[link2 Put: @"Dest" obj:[dest2 GetSDFObj]];
	[link2 PutRect: @"Rect" x1:85 y1:638 x2:503 y2:594];
	[annots PushBack: link2];

	// Note that PDFNet API can be used to modify existing annotations. 
	// In the following example we will modify the second link annotation 
	// (link2) so that it points to the 10th page. We also use a different 
	// destination page fit type.

	// link2 = annots.GetAt(annots.Size()-1);
	[link2 Put: @"Dest" obj: [[PTDestination CreateXYZ: [doc GetPage: 10] left:100 top:792-70 zoom:10] GetSDFObj]];

	// Create a third link annotation with a hyperlink action (all other 
	// annotation types can be created in a similar way)
	PTObj * link3 = [doc CreateIndirectDict];
	[link3 PutName: @"Subtype" name: @"Link"];
	[link3 PutRect: @"Rect" x1:85 y1:570 x2:503 y2:524];

	// Create a URI action 
	PTObj * action = [link3 PutDict: @"A"];
	[action PutName: @"S" name: @"URI"];
	[action PutString: @"URI" value: @"http://www.pdftron.com"];

	[annots PushBack: link3];
}

void CreateTestAnnots(PTPDFDoc *doc)
{
	PTElementWriter *ew = [[PTElementWriter alloc] init];
	PTElementBuilder *eb = [[PTElementBuilder alloc] init];
	PTElement *element = [[PTElement alloc] init];

    PTPage *first_page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 600 y2: 600]];
	[doc PagePushBack: first_page];
	[ew WriterBeginWithPage: first_page placement: e_ptoverlay page_coord_sys: NO compress: NO];	// begin writing to this page
	[ew End];  // save changes to the current page

	//
	// Test of a free text annotation.
	//
	{
        PTFreeText *txtannot = [PTFreeText Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 10 y1: 400 x2: 160 y2: 570]];
		[txtannot SetContents: @"\n\nSome swift brown fox snatched a gray hare out of the air by freezing it with an angry glare."
							  "\n\nAha!\n\nAnd there was much rejoicing!"];
		//std::vector<double> dash( 2, 2.0 );
		[txtannot SetBorderStyle: [[PTBorderStyle alloc] initWithS: e_ptsolid b_width: 1 b_hr: 10 b_vr: 20] oldStyleOnly: YES];
		[txtannot SetQuaddingFormat: 0];
		[first_page AnnotPushBack: txtannot];
		[txtannot RefreshAppearance];
	}
	{
		PTFreeText *txtannot = [PTFreeText Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 100 y1: 100 x2: 350 y2: 500]];
		[txtannot SetContentRect: [[PTPDFRect alloc] initWithX1: 200 y1: 200 x2: 350 y2: 500]];
		[txtannot SetContents: @"\n\nSome swift brown fox snatched a gray hare out of the air by freezing it with an angry glare."
							  "\n\nAha!\n\nAnd there was much rejoicing!"];
		[txtannot SetCalloutLinePointsWithKneePoint: [[PTPDFPoint alloc] initWithPx: 200 py: 300] p2: [[PTPDFPoint alloc] initWithPx: 150 py: 290] p3: [[PTPDFPoint alloc] initWithPx: 110 py: 110]];
		//std::vector<double> dash( 2, 2.0 );
        [txtannot SetBorderStyle: [[PTBorderStyle alloc] initWithS: e_ptsolid b_width: 1 b_hr: 10 b_vr: 20 ] oldStyleOnly: YES];
		[txtannot SetEndingStyle: e_ptClosedArrow];
		[txtannot SetColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w: 0] numcomp: 3];
        [txtannot SetQuaddingFormat: 1];
		[first_page AnnotPushBack: txtannot];
		[txtannot RefreshAppearance];
	}
	{
		PTFreeText *txtannot = [PTFreeText Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 400 y1: 10 x2: 550 y2: 400]];
		[txtannot SetContents: @"\n\nSome swift brown fox snatched a gray hare out of the air by freezing it with an angry glare."
							  "\n\nAha!\n\nAnd there was much rejoicing!"];
		[txtannot SetBorderStyle: [[PTBorderStyle alloc] initWithS: e_ptsolid b_width: 1 b_hr: 10 b_vr: 20 ] oldStyleOnly: YES];
		[txtannot SetColor: [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w: 0] numcomp: 3];
        [txtannot SetOpacity: 0.2 ];
		[txtannot SetQuaddingFormat: 2];
		[first_page AnnotPushBack: txtannot];
		[txtannot RefreshAppearance];
	}

    PTPage *page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 600 y2: 600]];
    [doc PagePushBack: page];
    [ew WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to this page
	[eb Reset: [[PTGState alloc] init]];			// Reset the GState to default
	[ew End];  // save changes to the current page

	{
		//Create a Line annotation...
		PTLineAnnot *line=[PTLineAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 250 y1: 250 x2: 400 y2: 400]];
		[line SetStartPoint: [[PTPDFPoint alloc] initWithPx: 350 py: 270]];
		[line SetEndPoint: [[PTPDFPoint alloc] initWithPx: 260 py: 370]];
		[line SetStartStyle: e_ptl_Square];
		[line SetEndStyle: e_ptl_Circle];
		[line SetColor: [[PTColorPt alloc] initWithX: 0.3 y: 0.5 z: 0 w: 0] numcomp: 3];
		[line SetContents: @"Dashed Captioned"];
		[line SetShowCaption: YES];
		[line SetCaptionPosition: e_ptTop ];
		NSMutableArray *dash = [NSMutableArray arrayWithCapacity: 2];
        [dash addObject: @2.0];
        [dash addObject: @2.0];
		[line SetBorderStyle: [[PTBorderStyle alloc] initWithS: e_ptdashed b_width: 2 b_hr: 0 b_vr: 0 b_dash: dash] oldStyleOnly: NO];
		[line RefreshAppearance];
		[page AnnotPushBack: line];
	}
	{
        PTLineAnnot *line=[PTLineAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 347 y1: 377 x2: 600 y2: 600]];
        [line SetStartPoint: [[PTPDFPoint alloc] initWithPx: 385 py: 410]];
		[line SetEndPoint: [[PTPDFPoint alloc] initWithPx: 540 py: 555]];
		[line SetStartStyle: e_ptl_Circle];
		[line SetEndStyle: e_ptOpenArrow];
        [line SetColor: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0] numcomp: 3];
		[line SetContents: @"Inline Caption"];
		[line SetShowCaption: YES];
		[line SetCaptionPosition: e_ptInline ];
        [line SetLeaderLineExtensionLength: -4];
		[line SetLeaderLineLength: -12 ];
		[line SetLeaderLineOffset: 2];
		[line RefreshAppearance];
		[page AnnotPushBack: line];
	}
	{
        PTLineAnnot *line=[PTLineAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 10 y1: 400 x2: 200 y2: 600]];
        [line SetStartPoint: [[PTPDFPoint alloc] initWithPx: 25 py: 426]];
		[line SetEndPoint: [[PTPDFPoint alloc] initWithPx: 180 py: 555]];
		[line SetStartStyle: e_ptl_Circle];
        [line SetEndStyle: e_ptl_Square];
        [line SetColor: [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w: 0] numcomp: 3];
        [line SetInteriorColor: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0] CompNum: 3];
		[line SetContents: @"Offset Caption"];
		[line SetShowCaption: YES];
		[line SetCaptionPosition: e_ptTop ];
		[line SetTextHOffset: -60];
		[line SetTextVOffset: 10];
		[line RefreshAppearance];
		[page AnnotPushBack: line];
	}
	{
        PTLineAnnot *line=[PTLineAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 200 y1: 10 x2: 400 y2: 70]];
        [line SetStartPoint: [[PTPDFPoint alloc] initWithPx: 220 py: 25]];
		[line SetEndPoint: [[PTPDFPoint alloc] initWithPx: 370 py: 60]];
        [line SetStartStyle: e_ptButt];
        [line SetEndStyle: e_ptOpenArrow];
        [line SetColor: [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w: 0] numcomp: 3];
        [line SetContents: @"Regular Caption"];
		[line SetShowCaption: YES];
		[line SetCaptionPosition: e_ptTop ];
        [line RefreshAppearance];
		[page AnnotPushBack: line];	
    }
	{
        PTLineAnnot *line=[PTLineAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 200 y1: 70 x2: 400 y2: 130]];
        [line SetStartPoint: [[PTPDFPoint alloc] initWithPx: 220 py: 111]];
		[line SetEndPoint: [[PTPDFPoint alloc] initWithPx: 370 py: 78]];
        [line SetStartStyle: e_ptl_Circle];
        [line SetEndStyle: e_ptDiamond];
        [line SetContents: @"Circle to Diamond"];
		[line SetColor: [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w: 0] numcomp: 3];
		[line SetInteriorColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w: 0] CompNum: 3];
		[line SetShowCaption: YES];
		[line SetCaptionPosition: e_ptTop];
		[line RefreshAppearance];
		[page AnnotPushBack: line];
	}
	{
		PTLineAnnot *line=[PTLineAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 10 y1: 100 x2: 160 y2: 200]];
		[line SetStartPoint: [[PTPDFPoint alloc] initWithPx: 15 py: 110]];
        [line SetEndPoint: [[PTPDFPoint alloc] initWithPx: 150 py: 190]];
		[line SetStartStyle: e_ptSlash];
		[line SetEndStyle: e_ptClosedArrow];
		[line SetContents: @"Slash to CArrow"];
		[line SetColor: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0] numcomp: 3];
		[line SetInteriorColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 1 w: 0] CompNum: 3];
		[line SetShowCaption: YES];
		[line SetCaptionPosition: e_ptTop];
		[line RefreshAppearance];
		[page AnnotPushBack: line];
	}
	{
		PTLineAnnot *line=[PTLineAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 270 y1: 270 x2: 570 y2: 433]];
		[line SetStartPoint: [[PTPDFPoint alloc] initWithPx: 300 py: 400]];
		[line SetEndPoint: [[PTPDFPoint alloc] initWithPx: 550 py: 300]];
		[line SetStartStyle: e_ptRClosedArrow];
		[line SetEndStyle: e_ptROpenArrow];
		[line SetContents: @"ROpen & RClosed arrows"];
		[line SetColor: [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w: 0] numcomp: 3];
		[line SetInteriorColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w: 0] CompNum: 3];
		[line SetShowCaption: YES];
		[line SetCaptionPosition: e_ptTop];
		[line RefreshAppearance];
		[page AnnotPushBack: line];
	}
	{
		PTLineAnnot *line=[PTLineAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 195 y1: 395 x2: 205 y2: 505]];
		[line SetStartPoint: [[PTPDFPoint alloc] initWithPx: 200 py: 400]];
		[line SetEndPoint: [[PTPDFPoint alloc] initWithPx: 200 py: 500]];
		[line RefreshAppearance];
		[page AnnotPushBack: line];
	}
	{
		PTLineAnnot *line=[PTLineAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 55 y1: 299 x2: 150 y2: 301]];
		[line SetStartPoint: [[PTPDFPoint alloc] initWithPx: 55 py: 300]];
		[line SetEndPoint: [[PTPDFPoint alloc] initWithPx: 155 py: 300]];
		[line SetStartStyle: e_ptl_Circle];
		[line SetEndStyle: e_ptl_Circle];
		[line SetContents: @"Caption that's longer than its line."];
		[line SetColor: [[PTColorPt alloc] initWithX: 1 y: 0 z: 1 w: 0] numcomp: 3];
		[line SetInteriorColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w: 0] CompNum: 3];
		[line SetShowCaption: YES];
		[line SetCaptionPosition: e_ptTop];
		[line RefreshAppearance];
		[page AnnotPushBack: line];
	}
	{
		PTLineAnnot *line=[PTLineAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 300 y1: 200 x2: 290 y2: 234]];
		[line SetStartPoint: [[PTPDFPoint alloc] initWithPx: 310 py: 210]];
		[line SetEndPoint: [[PTPDFPoint alloc] initWithPx: 380 py: 220]];
        [line SetColor: [[PTColorPt alloc] initWithX: 0 y: 0 z: 0 w: 0] numcomp: 3];
		[line RefreshAppearance];
		[page AnnotPushBack: line];
	}

	PTPage *page3 = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 600 y2: 600]];
	[ew WriterBeginWithPage: page3 placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to the page
	[ew End];  // save changes to the current page
	[doc PagePushBack: page3];
	{
		PTCircle *circle=[PTCircle Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 300 y1: 300 x2: 390 y2: 350]];
		[circle SetColor: [[PTColorPt alloc] initWithX: 0 y: 0 z: 0 w: 0] numcomp: 3];
		[circle RefreshAppearance];
		[page3 AnnotPushBack: circle];
	}
	{
		PTCircle *circle=[PTCircle Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 100 y1: 100 x2: 200 y2: 200]];
		[circle SetColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w: 0] numcomp: 3];
		[circle SetInteriorColor: [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w: 0] CompNum: 3];
		NSMutableArray *dash = [NSMutableArray arrayWithCapacity: 2];
        [dash addObject: @2.0];
        [dash addObject: @4.0];
		[circle SetBorderStyle: [[PTBorderStyle alloc] initWithS: e_ptdashed b_width: 3 b_hr: 0 b_vr: 0 b_dash: dash] oldStyleOnly: NO];
		[circle SetPadding: 2];
		[circle RefreshAppearance];
		[page3 AnnotPushBack: circle];
	}
	{
		PTSquare *sq = [PTSquare Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 10 y1: 200 x2: 80 y2: 300]];
		[sq SetColor: [[PTColorPt alloc] initWithX: 0 y: 0 z: 0 w: 0] numcomp: 3]; 
		[sq RefreshAppearance];
		[page3 AnnotPushBack: sq];
	}
	{
		PTSquare *sq = [PTSquare Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 500 y1: 200 x2: 580 y2: 300]];
		[sq SetColor: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0] numcomp: 3]; 
		[sq SetInteriorColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 1 w: 0] CompNum: 3];
		NSMutableArray *dash = [NSMutableArray arrayWithCapacity: 2];
        [dash addObject: @4.0];
        [dash addObject: @2.0];
		[sq SetBorderStyle: [[PTBorderStyle alloc] initWithS: e_ptdashed b_width: 6 b_hr: 0 b_vr: 0 b_dash: dash] oldStyleOnly: NO];
		[sq SetPadding: 4];
		[sq RefreshAppearance];
		[page3 AnnotPushBack: sq];
	}
	{
		PTPolygon *poly = [PTPolygon Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 5 y1: 500 x2: 125 y2: 590]];
		[poly SetColor: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0] numcomp: 3]; 
		[poly SetInteriorColor: [[PTColorPt alloc] initWithX: 1 y: 1 z: 0 w: 0] CompNum: 3];
		[poly SetVertex: 0 pt: [[PTPDFPoint alloc] initWithPx: 12 py: 510]];
        [poly SetVertex: 1 pt: [[PTPDFPoint alloc] initWithPx: 100 py: 510]];
		[poly SetVertex: 2 pt: [[PTPDFPoint alloc] initWithPx: 100 py: 555]];
		[poly SetVertex: 3 pt: [[PTPDFPoint alloc] initWithPx: 35 py: 544]];
		[poly SetBorderStyle: [[PTBorderStyle alloc] initWithS: e_ptsolid b_width: 4 b_hr: 0 b_vr: 0] oldStyleOnly: NO];
		[poly SetPadding: 4];
		[poly RefreshAppearance];
		[page3 AnnotPushBack: poly];
	}
	{		
        PTPolygon *poly = [PTPolygon Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 400 y1: 10 x2: 500 y2: 90]];
		[poly SetColor: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0] numcomp: 3]; 
		[poly SetInteriorColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w: 0] CompNum: 3];
		[poly SetVertex: 0 pt: [[PTPDFPoint alloc] initWithPx: 405 py: 20]];
        [poly SetVertex: 1 pt: [[PTPDFPoint alloc] initWithPx: 440 py: 40]];
		[poly SetVertex: 2 pt: [[PTPDFPoint alloc] initWithPx: 410 py: 60]];
		[poly SetVertex: 3 pt: [[PTPDFPoint alloc] initWithPx: 470 py: 80]];
        [poly SetBorderStyle: [[PTBorderStyle alloc] initWithS: e_ptsolid b_width: 2 b_hr: 0 b_vr: 0] oldStyleOnly: NO];
		[poly SetPadding: 4];
        [poly SetStartStyle: e_ptRClosedArrow];
		[poly SetEndStyle: e_ptClosedArrow];
		[poly RefreshAppearance];
		[page3 AnnotPushBack: poly];
	}
	{
		PTLink *lk = [PTLink Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 5 y1: 5 x2: 55 y2: 24]];
		//lk.SetColor( ColorPt(0,1,0), 3 );
		[lk RefreshAppearance];
		[page3 AnnotPushBack: lk];
	}


	PTPage *page4 = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 600 y2: 600]];
	[ew WriterBeginWithPage: page4 placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to the page
	[ew End];  // save changes to the current page
	[doc PagePushBack: page4];

	{	
		[ew WriterBeginWithPage: page4 placement: e_ptoverlay page_coord_sys: YES compress: YES];
		PTFont *font = [PTFont Create: [doc GetSDFDoc] type: e_pthelvetica embed: NO];
		element = [eb CreateTextBeginWithFont: font font_sz: 16];
		[element SetPathFill: YES];
		[ew WriteElement: element];
		element = [eb CreateTextRunWithFont: @"Some random text on the page" font: font font_sz: 16];
		[element SetTextMatrix: 1 b: 0 c: 0 d: 1 h: 100 v: 500];
		[ew WriteElement: element];
		[ew WriteElement: [eb CreateTextEnd]];
		[ew End];
	}
	{
		PTHighlightAnnot *hl = [PTHighlightAnnot Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 100 y1: 490 x2: 150 y2: 515]];
		[hl SetColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w: 0] numcomp: 3]; 
		[hl RefreshAppearance];
        [page4 AnnotPushBack: hl];
	}
	{
		PTSquiggly *sq = [PTSquiggly Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 100 y1: 450 x2: 250 y2: 600]];
		//sq.SetColor( ColorPt(1,0,0), 3 );
        PTPDFPoint *p1 = [[PTPDFPoint alloc] initWithPx: 122 py: 455];
        PTPDFPoint *p2 = [[PTPDFPoint alloc] initWithPx: 240 py: 545];
        PTPDFPoint *p3 = [[PTPDFPoint alloc] initWithPx: 230 py: 595];
        PTPDFPoint *p4 = [[PTPDFPoint alloc] initWithPx: 101 py: 500];
		[sq SetQuadPoint: 0 qp: [[PTQuadPoint alloc] initWithP11: p1 p22: p2 p33: p3 p44: p4]];
		[sq RefreshAppearance];
		[page4 AnnotPushBack: sq];
	}
	{
		PTCaret *cr = [PTCaret Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 100 y1: 40 x2: 129 y2: 69]];
		[cr SetColor: [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w: 0] numcomp: 3]; 
		[cr SetSymbol: @"P"];
		[cr RefreshAppearance];
		[page4 AnnotPushBack: cr];
	}


	PTPage *page5 = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 600 y2: 600]];
	[ew WriterBeginWithPage: page5 placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to the page
	[ew End];  // save changes to the current page
	[doc PagePushBack: page5];
	PTFileSpec *fs = [PTFileSpec Create: [doc GetSDFDoc] path: [NSSearchPathForDirectoriesInDomains (NSDocumentDirectory, NSUserDomainMask, YES)[0] stringByAppendingPathComponent:@"butterfly.png"] embed: NO];
	PTPage *page6 = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 600 y2: 600]];
	[ew WriterBeginWithPage: page6 placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to the page
	[ew End];  // save changes to the current page
	[doc PagePushBack: page6];

    int ipage;
	for( ipage =0; ipage < 2; ++ipage ) {
        int iann;
		for( iann =0; iann < 100; iann++ ) {
			if( ! (iann > e_ptTag) ) {
				PTFileAttachment *fa = [PTFileAttachment CreateFileAttchWithFileSpec: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 50+50*iann y1: 100 x2: 70+50*iann y2: 120] fs: fs icon_name: iann];
				if(ipage) [fa SetColor: [[PTColorPt alloc] initWithX: 1 y: 1 z: 0 w: 0] numcomp: 3];
				[fa RefreshAppearance];
				if( ipage == 0 )
					[page5 AnnotPushBack: fa];
				else
					[page6 AnnotPushBack: fa];
			}
			if( iann > e_ptNote ) break;
			PTText *txt = [PTText Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 10+iann*50 y1: 200 x2: 30+iann*50 y2: 220]];
			[txt SetTextIconType: iann];
			[txt SetContents: [txt GetIconName]];
			if( ipage )	[txt SetColor: [[PTColorPt alloc] initWithX: 1 y: 1 z: 0 w: 0] numcomp: 3];
			[txt RefreshAppearance];
			if( ipage == 0 )
				[page5 AnnotPushBack: txt];
			else 
				[page6 AnnotPushBack: txt];
		}
	}
	{
		PTText *txt = [PTText Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 10 y1: 20 x2: 30 y2: 40]];
		[txt SetTextIconName: @"UserIcon"];
		[txt SetContents: @"User defined icon, unrecognized by appearance generator"];
		[txt SetColor: [[PTColorPt alloc] initWithX: 0 y: 1 z: 0 w: 0] numcomp: 3];
		[txt RefreshAppearance];
		[page6 AnnotPushBack: txt];
	}
	{
		PTInk *ink = [PTInk Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 100 y1: 400 x2: 200 y2: 550]];
		[ink SetColor: [[PTColorPt alloc]  initWithX: 0 y: 0 z: 1 w: 0] numcomp: 3];
		[ink SetPoint: 1 pointindex: 3 pt: [[PTPDFPoint alloc] initWithPx: 220 py: 505]];
        [ink SetPoint: 1 pointindex: 0 pt: [[PTPDFPoint alloc] initWithPx: 100 py: 490]];
		[ink SetPoint: 0 pointindex: 1 pt: [[PTPDFPoint alloc] initWithPx: 120 py: 410]];
        [ink SetPoint: 0 pointindex: 0 pt: [[PTPDFPoint alloc] initWithPx: 100 py: 400]];
		[ink SetPoint: 1 pointindex: 2 pt: [[PTPDFPoint alloc] initWithPx: 180 py: 490]];
        [ink SetPoint: 1 pointindex: 1 pt: [[PTPDFPoint alloc] initWithPx: 140 py: 440]];		
		[ink SetBorderStyle: [[PTBorderStyle alloc] initWithS: e_ptsolid b_width: 3 b_hr: 0 b_vr: 0] oldStyleOnly: NO];
		[ink RefreshAppearance];
		[page6 AnnotPushBack: ink];
	}


	PTPage *page7 = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 600 y2: 600]];
	[ew WriterBeginWithPage: page7 placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to the page
	[ew End];  // save changes to the current page
	[doc PagePushBack: page7];

	{
		PTSound *snd = [PTSound Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 100 y1: 500 x2: 120 y2: 520]];
		[snd SetColor: [[PTColorPt alloc] initWithX: 1 y: 1 z: 0 w: 0] numcomp: 3];
		[snd SetSoundIconType: e_ptSpeaker];
		[snd RefreshAppearance];
		[page7 AnnotPushBack: snd];
	}
	{
		PTSound *snd = [PTSound Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 200 y1: 500 x2: 220 y2: 520]];
		[snd SetColor: [[PTColorPt alloc] initWithX: 1 y: 1 z: 0 w: 0] numcomp: 3];
		[snd SetSoundIconType: e_ptMic];
		[snd RefreshAppearance];
		[page7 AnnotPushBack: snd];
	}




	PTPage *page8 = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 600 y2: 600]];
	[ew WriterBeginWithPage: page8 placement: e_ptoverlay page_coord_sys: YES compress: YES];	// begin writing to the page
	[ew End];  // save changes to the current page
	[doc PagePushBack: page8];

    int ipage1;
	for( ipage1 =0; ipage1 < 2; ++ipage1 ) {
		double px = 5, py = 520;
        PTRubberStampIcon istamp;
		for( istamp = e_ptApproved; istamp <= e_ptDraft; istamp = istamp + 1 ) {
            PTRubberStamp *st = [PTRubberStamp Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 1 y1: 2 x2: 200 y2: 200] icon:e_ptDraft];
            [st SetRubberStampIconType: istamp];
            [st SetContents: [st GetIconName]];
            [st SetRect: [[PTPDFRect alloc] initWithX1: px y1: py x2: px+100 y2: py+25]];
				py = py - 100;
				if( py < 0 ) {
					py = 520;
					px = px + 200;
				}
				if( ipage == 0 )
					//[page7 AnnotPushBack: st];
					;
				else {
					[page8 AnnotPushBack: st];
					[st RefreshAppearance];
				}
		}
	}
	PTRubberStamp *st = [PTRubberStamp Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 400 y1: 5 x2: 550 y2: 45] icon: e_ptDraft];
	[st SetRubberStampIconName: @"UserStamp"];
	[st SetContents: @"User defined stamp"];
     [page8 AnnotPushBack: st];
	[st RefreshAppearance];
}

int main(int argc, char *argv[])
{
    @autoreleasepool {
        
        int ret = 0;
        [PTPDFNet Initialize: 0];

        @try  
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/numbered.pdf"];
            [doc InitSecurityHandler];

            // An example of using SDF/Cos API to add any type of annotations.
            AnnotationLowLevelAPI(doc);
            [doc SaveToFile: @"../../TestFiles/Output/annotation_test1.pdf" flags: e_ptlinearized];
            NSLog(@"%@", @"Done. Results saved in annotation_test1.pdf");

            // An example of using the high-level PDFNet API to read existing annotations,
            // to edit existing annotations, and to create new annotation from scratch.
            AnnotationHighLevelAPI(doc);
            [doc SaveToFile: @"../../TestFiles/Output/annotation_test2.pdf" flags: e_ptlinearized];
            NSLog(@"%@", @"Done. Results saved in annotation_test2.pdf");

            // an example of creating various annotations in a brand new document
            PTPDFDoc *doc1 = [[PTPDFDoc alloc] init];
            CreateTestAnnots( doc1 );
            [doc1 SaveToFile: @"../../TestFiles/Output/new_annot_test_api.pdf" flags: e_ptlinearized];
            NSLog(@"%@", @"Saved new_annot_test_api.pdf");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        return ret;
        
    }
	
}

