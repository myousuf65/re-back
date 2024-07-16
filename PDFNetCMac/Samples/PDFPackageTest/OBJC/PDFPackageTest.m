//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//-----------------------------------------------------------------------------------
/// This sample illustrates how to create, extract, and manipulate PDF Portfolios
/// (a.k.a. PDF Packages) using PDFNet SDK.
//-----------------------------------------------------------------------------------


void AddPackage(PTPDFDoc *doc, NSString *file, NSString* desc) 
{
	PTNameTree *files = [PTNameTree Create: [doc GetSDFDoc] name: @"EmbeddedFiles"];
    PTFileSpec *fs = [PTFileSpec Create: [doc GetSDFDoc] path: file embed: true];
	[files Put: [file dataUsingEncoding: NSUTF8StringEncoding] key_sz:(int)file.length value: [fs GetSDFObj]];
	[fs SetDesc: desc];

	PTObj * collection = [[doc GetRoot] FindObj: @"Collection"];
	if (!collection) collection = [[doc GetRoot] PutDict: @"Collection"];

	// You could here manipulate any entry in the Collection dictionary. 
	// For example, the following line sets the tile mode for initial view mode
	// Please refer to section '2.3.5 Collections' in PDF Reference for details.
	[collection PutName: @"View" name: @"T"];
}

void AddCoverPage(PTPDFDoc *doc) 
{
	// Here we dynamically generate cover page (please see ElementBuilder 
	// sample for more extensive coverage of PDF creation API).
    PTPDFRect * rect = [[PTPDFRect alloc] init];
    [rect Set: 0 y1: 0 x2: 200 y2: 200];
	PTPage *page = [doc PageCreate: rect];

	PTElementBuilder *b = [[PTElementBuilder alloc] init];
	PTElementWriter *w = [[PTElementWriter alloc] init];
	[w WriterBeginWithPage: page placement: e_ptoverlay page_coord_sys: YES compress: YES];
	PTFont *font = [PTFont Create: [doc GetSDFDoc] type: e_pthelvetica embed: NO];
	[w WriteElement: [b CreateTextBeginWithFont: font font_sz: 12]];
	PTElement *e = [b CreateTextRun: @"My PDF Collection"];
    PTMatrix2D *mtx = [[PTMatrix2D alloc] initWithA: 1 b: 0 c: 0 d: 1 h: 50 v: 96];
	[e SetTextMatrixWithMatrix2D: mtx];
	[[e GetGState] SetFillColorSpace: [PTColorSpace CreateDeviceRGB]];
    [[e GetGState] SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0]];
	[w WriteElement: e];
	[w WriteElement: [b CreateTextEnd]];
	[w End];
	[doc PagePushBack: page];

	// Alternatively we could import a PDF page from a template PDF document
	// (for an example please see PDFPage sample project).
	// ...
}

int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        // Create a PDF Package.
        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] init];
            AddPackage(doc, @"../../TestFiles/numbered.pdf", @"My File 1");
            AddPackage(doc, @"../../TestFiles/newsletter.pdf", @"My Newsletter...");
            AddPackage(doc, @"../../TestFiles/peppers.jpg", @"An image");
            AddCoverPage(doc);
            [doc SaveToFile: @"../../TestFiles/Output/package.pdf" flags: e_ptlinearized];
            NSLog(@"Done.");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
    // Extract parts from a PDF Package.
        @try  
        {	 
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/package.pdf"];
            [doc InitSecurityHandler];

            PTNameTree *files = [PTNameTree Find: [doc GetSDFDoc] name: @"EmbeddedFiles"];
            if([files IsValid]) 
            { 
                // Traverse the list of embedded files.
                PTDictIterator *i = [files GetIterator];
            int counter = 0;
                for (; [i HasNext]; [i Next], ++counter) 
                {
                    NSString *entry_name = [[i Key] GetAsPDFText];
                    NSLog(@"Part: %@", entry_name);
                PTFileSpec *file_spec = [[PTFileSpec alloc] initWithF: [i Value]];
                    PTFilter *stm = [file_spec GetFileData];
                    if (stm) 
                    {
                        NSString *str = [NSString stringWithFormat: @"../../TestFiles/Output/extract_%d", counter];
                    [stm WriteToFile: str append: NO];
                    }
                }
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


