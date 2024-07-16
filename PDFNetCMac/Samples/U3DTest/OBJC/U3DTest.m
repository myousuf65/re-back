//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

void Create3DAnnotation(PTPDFDoc* doc, PTObj* annots)
{
	// ---------------------------------------------------------------------------------
	// Create a 3D annotation based on U3D content. PDF 1.6 introduces the capability 
	// for collections of three-dimensional objects, such as those used by CAD software, 
	// to be embedded in PDF files.
	PTObj * link_3D = [doc CreateIndirectDict];
	[link_3D PutName: @"Subtype" name: @"3D"];

	// Annotation location on the page
	PTPDFRect * link_3D_rect = [[PTPDFRect alloc] init];
    [link_3D_rect Set: 25 y1: 180 x2: 585 y2: 643];
	[link_3D PutRect: @"Rect" x1: [link_3D_rect GetX1] y1: [link_3D_rect GetY1] x2: [link_3D_rect GetX2] y2: [link_3D_rect GetY2]];
	[annots PushBack: link_3D];

	// The 3DA entry is an activation dictionary (see Table 9.34 in the PDF Reference Manual) 
	// that determines how the state of the annotation and its associated artwork can change.
	PTObj * activation_dict_3D = [link_3D PutDict: @"3DA"];

	// Set the annotation so that it is activated as soon as the page containing the 
	// annotation is opened. Other options are: PV (page view) and XA (explicit) activation.
	[activation_dict_3D PutName: @"A" name: @"PO"];  

	// Embed U3D Streams (3D Model/Artwork).
	{
		PTMappedFile *u3d_file = [[PTMappedFile alloc] initWithFilename: @"../../TestFiles/dice.u3d"];
		PTFilterReader *u3d_reader = [[PTFilterReader alloc] initWithFilter: u3d_file];

		// To embed 3D stream without compression, you can omit the second parameter in CreateIndirectStream.
		PTFilter *f = [[PTFilter alloc] init];
		PTObj * u3d_data_dict = [doc CreateIndirectStream: u3d_reader filter_chain: [[PTFlateEncode alloc] initWithInput_filter: [[PTFilter alloc] init] compression_level: -1 buf_sz: 256]];
		[u3d_data_dict PutName: @"Subtype" name: @"U3D"];
		[link_3D Put: @"3DD" obj: u3d_data_dict];
	}

	// Set the initial view of the 3D artwork that should be used when the annotation is activated.
	PTObj * view3D_dict = [link_3D PutDict: @"3DV"];
	{
		[view3D_dict PutString: @"IN" value: @"Unnamed"];
		[view3D_dict PutString: @"XN" value: @"Default"];
		[view3D_dict PutName: @"MS" name: @"M"];
		[view3D_dict PutNumber: @"CO" value: 27.5];

		// A 12-element 3D transformation matrix that specifies a position and orientation 
		// of the camera in world coordinates.
		PTObj * tr3d = [view3D_dict PutArray: @"C2W"]; 
		[tr3d PushBackNumber: 1]; [tr3d PushBackNumber: 0]; [tr3d PushBackNumber: 0]; 
		[tr3d PushBackNumber: 0]; [tr3d PushBackNumber: 0]; [tr3d PushBackNumber: -1];
		[tr3d PushBackNumber: 0]; [tr3d PushBackNumber: 1]; [tr3d PushBackNumber: 0]; 
		[tr3d PushBackNumber: 0]; [tr3d PushBackNumber: -27.5]; [tr3d PushBackNumber: 0];

	}

	// Create annotation appearance stream, a thumbnail which is used during printing or
	// in PDF processors that do not understand 3D data.
	PTObj * ap_dict = [link_3D PutDict: @"AP"];
	{
		PTElementBuilder *builder = [[PTElementBuilder alloc] init];
		PTElementWriter *writer = [[PTElementWriter alloc] init];
		[writer WriterBeginWithSDFDoc: [doc GetSDFDoc] compress: true];

		NSString *thumb_pathname = @"../../TestFiles/dice.jpg";
		PTImage *image = [PTImage CreateWithFile: [doc GetSDFDoc] filename: thumb_pathname encoder_hints: [[PTObj alloc] init]];
		[writer WritePlacedElement: [builder CreateImageWithCornerAndScale: image x: 0.0 y: 0.0 hscale: [link_3D_rect Width] vscale: [link_3D_rect Height]]];

		PTObj * normal_ap_stream = [writer End];
		[normal_ap_stream PutName: @"Subtype" name: @"Form"];
		[normal_ap_stream PutRect: @"BBox" x1: 0 y1: 0 x2: [link_3D_rect Width] y2: [link_3D_rect Height]];
        [ap_dict Put: @"N" obj: normal_ap_stream];
	}
}

int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        @try  
        {	 
            PTPDFDoc *doc = [[PTPDFDoc alloc] init];
        PTPDFRect * rect = [[PTPDFRect alloc] init]; 
        [rect Set: 0 y1: 0 x2: 612 y2: 792];
            PTPage *page = [doc PageCreate: rect];
            [doc PagePushBack: page];
            PTObj * annots = [doc CreateIndirectArray];
            [[page GetSDFObj] Put: @"Annots" obj: annots];
        
            Create3DAnnotation(doc, annots);
            [doc SaveToFile: @"../../TestFiles/Output/dice_u3d.pdf" flags: e_ptlinearized];
            NSLog(@"Done");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        return ret;
    }
}
