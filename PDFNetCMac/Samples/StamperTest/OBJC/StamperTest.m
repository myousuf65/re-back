//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//---------------------------------------------------------------------------------------
// The following sample shows how to add new content (or watermark) PDF pages
// using 'pdftron.PDF.Stamper' utility class. 
//
// Stamper can be used to PDF pages with text, images, or with other PDF content 
// in only a few lines of code. Although Stamper is very simple to use compared 
// to ElementBuilder/ElementWriter it is not as powerful or flexible. In case you 
// need full control over PDF creation use ElementBuilder/ElementWriter to add 
// new content to existing PDF pages as shown in the ElementBuilder sample project.
//---------------------------------------------------------------------------------------
int main(int argc, char * argv[])
{
    @autoreleasepool {

        int ret = 0;
        
        [PTPDFNet Initialize: 0];

        //--------------------------------------------------------------------------------
        // Example 1) Add text stamp to all pages, then remove text stamp from odd pages. 
        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];
            PTStamper *s = [[PTStamper alloc] initWithSize_type: e_ptrelative_scale a: 0.5 b: 0.5];
            [s SetAlignment: e_pthorizontal_center vertical_alignment: e_ptvertical_center];
            PTColorPt *red = [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0]; // set text color to red
            [s SetFontColor: red];
            PTPageSet *set = [[PTPageSet alloc] initWithRange_start: 1 range_end: [doc GetPageCount] filter: e_ptall];
            [s StampText: doc src_txt: @"If you are reading this\nthis is an even page" dest_pages: set];
            //delete all text stamps in even pages
            PTPageSet *set2 = [[PTPageSet alloc] initWithRange_start: 1 range_end: [doc GetPageCount] filter: e_ptodd];
            [PTStamper DeleteStamps: doc page_set: set2];

            [doc SaveToFile: @"../../TestFiles/Output/newsletter.ex1.pdf" flags: e_ptlinearized];
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }
        
        //--------------------------------------------------------------------------------
        // Example 2) Add Image stamp to first 2 pages. 
        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];

            PTStamper *s = [[PTStamper alloc] initWithSize_type: e_ptrelative_scale a: 0.05 b: 0.05];
            PTImage *img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/peppers.jpg"];
            [s SetSize: e_ptrelative_scale a: 0.5 b: 0.5];
            //set position of the image to the center, left of PDF pages
            [s SetAlignment: e_pthorizontal_left vertical_alignment: e_ptvertical_center];
            PTColorPt *pt = [[PTColorPt alloc] initWithX: 0 y: 0 z: 0 w: 0];
            [s SetFontColor: pt];
            [s SetRotation: 180];
            [s SetAsBackground: NO];
            //only stamp first 2 pages
            PTPageSet *ps = [[PTPageSet alloc] initWithRange_start: 1 range_end: 2 filter: e_ptall];
            [s StampImage: doc src_img: img dest_pages: ps];

            [doc SaveToFile: @"../../TestFiles/Output/newsletter.ex2.pdf" flags: e_ptlinearized];
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }

        //--------------------------------------------------------------------------------
        // Example 3) Add Page stamp to all pages. 
        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];

            PTPDFDoc *fish_doc = [[PTPDFDoc alloc ] initWithFilepath: @"../../TestFiles/fish.pdf"];
            [fish_doc InitSecurityHandler];

            PTStamper *s = [[PTStamper alloc] initWithSize_type: e_ptrelative_scale a: 0.5 b: 0.5];
            PTPage *src_page = [fish_doc GetPage: 1];
            PTPDFRect * page_one_crop = [src_page GetCropBox];
            // set size of the image to 10% of the original while keep the old aspect ratio
            [s SetSize: e_ptabsolute_size a: [page_one_crop Width] * 0.1  b: -1];
            [s SetOpacity: 0.4];
            [s SetRotation: -67];
            //put the image at the bottom right hand corner
            [s SetAlignment: e_pthorizontal_right vertical_alignment: e_ptvertical_bottom];
            PTPageSet *ps = [[PTPageSet alloc] initWithRange_start: 2 range_end: [doc GetPageCount] filter: e_ptall];
            [s StampPage: doc src_page: src_page dest_pages: ps];
            
            [doc SaveToFile: @"../../TestFiles/Output/newsletter.ex3.pdf" flags: e_ptlinearized];	
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }
        
        //--------------------------------------------------------------------------------
        // Example 4) Add Image stamp to first 20 odd pages.
        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];

            PTStamper *s = [[PTStamper alloc] initWithSize_type: e_ptabsolute_size a: 20 b: 20];
            [s SetOpacity: 1];
            [s SetRotation: 45];                
            [s SetAsBackground: YES];
            [s SetPosition: 30 vertical_distance: 40 use_percentage: NO];
            PTImage *img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/peppers.jpg"];
            PTPageSet *ps = [[PTPageSet alloc] initWithRange_start: 1 range_end: 20 filter: e_ptodd];
            [s StampImage: doc src_img: img dest_pages: ps];

            [doc SaveToFile: @"../../TestFiles/Output/newsletter.ex4.pdf" flags: e_ptlinearized];
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }

        //--------------------------------------------------------------------------------
        // Example 5) Add text stamp to first 20 even pages
        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];

            PTStamper *s = [[PTStamper alloc] initWithSize_type: e_ptrelative_scale a: 0.5 b: 0.5];
            [s SetPosition: 0 vertical_distance: 0 use_percentage: NO];
            [s SetOpacity: 0.7];
            [s SetRotation: 90];
            [s SetSize: e_pts_font_size a: 80 b: -1];
            [s SetTextAlignment: e_ptalign_center];
            PTPageSet *ps = [[PTPageSet alloc] initWithRange_start: 1 range_end: 20 filter: e_pteven];
            [s StampText: doc src_txt: @"Goodbye\nMoon" dest_pages: ps];

            [doc SaveToFile: @"../../TestFiles/Output/newsletter.ex5.pdf" flags: e_ptlinearized];
        }
        @catch (NSException* e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }
        
        //--------------------------------------------------------------------------------
        // Example 6) Add first page as stamp to all even pages
        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];

            PTPDFDoc *fish_doc = [[PTPDFDoc alloc ] initWithFilepath: @"../../TestFiles/fish.pdf"];
            [fish_doc InitSecurityHandler];

            PTStamper *s = [[PTStamper alloc] initWithSize_type: e_ptrelative_scale a: 0.3 b: 0.3];
            [s SetOpacity: 1];
            [s SetRotation: 270];
            [s SetAsBackground: YES];
            [s SetPosition: 0.5 vertical_distance: 0.5 use_percentage: YES];
            [s SetAlignment: e_pthorizontal_left vertical_alignment: e_ptvertical_bottom];
            PTPage *page_one = [fish_doc GetPage: 1];
            PTPageSet *ps = [[PTPageSet alloc] initWithRange_start: 1 range_end: [doc GetPageCount] filter: e_pteven];
            [s StampPage: doc src_page: page_one dest_pages: ps];

            [doc SaveToFile: @"../../TestFiles/Output/newsletter.ex6.pdf" flags: e_ptlinearized];
        }
        @catch (NSException* e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }

        //--------------------------------------------------------------------------------
        // Example 7) Add image stamp at top right corner in every pages
        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];

            PTStamper *s = [[PTStamper alloc] initWithSize_type: e_ptrelative_scale a: 0.1 b: 0.1];
            [s SetOpacity: 0.8];
            [s SetRotation: 135];
            [s SetAsBackground: NO];
            [s ShowsOnPrint: NO];
            [s SetAlignment: e_pthorizontal_left vertical_alignment: e_ptvertical_top];
            [s SetPosition: 10 vertical_distance: 10 use_percentage: YES];

            PTImage *img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/peppers.jpg"];
            PTPageSet *ps = [[PTPageSet alloc] initWithRange_start: 1 range_end: [doc GetPageCount] filter: e_ptall];
            [s StampImage: doc src_img: img dest_pages: ps];

            [doc SaveToFile: @"../../TestFiles/Output/newsletter.ex7.pdf" flags: e_ptlinearized];
        }
        @catch (NSException* e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }

        //--------------------------------------------------------------------------------
        // Example 8) Add Text stamp to first 2 pages, and image stamp to first page.
        //          Because text stamp is set as background, the image is top of the text
        //          stamp. Text stamp on the first page is not visible.
        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];

            PTStamper *s = [[PTStamper alloc] initWithSize_type: e_ptrelative_scale a: 0.07 b: -0.1];
            [s SetAlignment: e_pthorizontal_right vertical_alignment: e_ptvertical_bottom];
            [s SetAlignment: e_pthorizontal_center vertical_alignment: e_ptvertical_top];
            [s SetFont: [PTFont Create: [doc GetSDFDoc] type: e_ptcourier embed: YES]];
            PTColorPt *red = [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0];
            [s SetFontColor: red]; //set color to red
            [s SetTextAlignment: e_ptalign_right];
            [s SetAsBackground: YES]; //set text stamp as background
            PTPageSet *ps = [[PTPageSet alloc] initWithRange_start: 1 range_end: 2 filter: e_ptall];
            [s StampText: doc src_txt: @"This is a title!" dest_pages: ps];

            PTImage *img = [PTImage Create: [doc GetSDFDoc] filename: @"../../TestFiles/peppers.jpg"];
            [s SetAsBackground: NO]; // set image stamp as foreground
            PTPageSet *first_page_ps = [[PTPageSet alloc] initWithOne_page: 1];
            [s StampImage: doc src_img: img dest_pages: first_page_ps];

            [doc SaveToFile: @"../../TestFiles/Output/newsletter.ex8.pdf" flags: e_ptlinearized];
        }
        @catch (NSException* e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }
        
        //NSLog(@"Done.");
        return ret;
    }
}
