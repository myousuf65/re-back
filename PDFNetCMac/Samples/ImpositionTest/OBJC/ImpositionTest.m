//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//-----------------------------------------------------------------------------------
// The sample illustrates how multiple pages can be combined/imposed 
// using PDFNet. Page imposition can be used to arrange/order pages 
// prior to printing or to assemble a 'master' page from several 'source' 
// pages. Using PDFNet API it is possible to write applications that can 
// re-order the pages such that they will display in the correct order 
// when the hard copy pages are compiled and folded correctly. 
//-----------------------------------------------------------------------------------

int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        // Relative path to the folder containing test files.
        NSString *input_path = @"../../TestFiles/newsletter.pdf";
        NSString *output_path = @"../../TestFiles/Output/newsletter_booklet.pdf";

        @try
        {	 
            NSLog(@"-------------------------------------------------");
            NSLog(@"Opening the input pdf...");

            NSString *filein = input_path;
            NSString *fileout = output_path;

            PTPDFDoc *in_doc = [[PTPDFDoc alloc] initWithFilepath: filein];
            [in_doc InitSecurityHandler];

            // Create a list of pages to import from one PDF document to another.
            PTVectorPage *import_pages = [[PTVectorPage alloc] init];
        PTPageIterator *itr;
            for (itr=[in_doc GetPageIterator: 1]; [itr HasNext]; [itr Next]) {
                [import_pages add: [itr Current]];
            }

            PTPDFDoc *new_doc = [[PTPDFDoc alloc] init];
            PTVectorPage *imported_pages = [new_doc ImportPages: import_pages import_bookmarks: NO];

            // Paper dimension for A3 format in points. Because one inch has 
            // 72 points, 11.69 inch 72 = 841.69 points
            PTPDFRect * media_box = [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 1190.88 y2: 841.69]; 
            double mid_point = [media_box Width]/2;

            PTElementBuilder *builder = [[PTElementBuilder alloc] init];
            PTElementWriter *writer = [[PTElementWriter alloc] init];
        
        size_t i;
            for (i=0; i<[imported_pages size]; ++i)
            {
                // Create a blank new A3 page and place on it two pages from the input document.
                PTPage *new_page = [new_doc PageCreate: media_box];
                [writer WriterBeginWithPage: new_page placement: e_ptoverlay page_coord_sys: YES compress: YES];

                // Place the first page
                PTPage *src_page = [imported_pages get:(int)i];
                PTElement *element = [builder CreateFormWithPage: src_page];

                double sc_x = mid_point / [src_page GetPageWidth: e_ptcrop];
                double sc_y = [media_box Height] / [src_page GetPageHeight: e_ptcrop];
                double scale = sc_x < sc_y ? sc_x : sc_y; // min(sc_x, sc_y)
                [[element GetGState] SetTransform: scale b: 0 c: 0 d: scale h: 0 v: 0];
                [writer WritePlacedElement: element];
                
                // Place the second page
                ++i; 
                if (i<[imported_pages size])	{
                    src_page = [imported_pages get:(int)i];
                    element = [builder CreateFormWithPage: src_page];
                    sc_x = mid_point / [src_page GetPageWidth: e_ptcrop];
                    sc_y = [media_box Height] / [src_page GetPageHeight: e_ptcrop];
                    scale = sc_x < sc_y ? sc_x : sc_y; // min(sc_x, sc_y)
                    [[element GetGState] SetTransform: scale b: 0 c: 0 d: scale h: mid_point v: 0];
                    [writer WritePlacedElement: element];
                }

                [writer End];
                [new_doc PagePushBack: new_page];
            }		

            [new_doc SaveToFile: fileout flags: e_ptlinearized ];
            NSLog(@"Done. Result saved in newsletter_booklet.pdf...");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        NSLog(@"Done.");
        return ret;
    }
}
