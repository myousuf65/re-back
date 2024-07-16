//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//---------------------------------------------------------------------------------------
// The following sample illustrates how to convert HTML pages to PDF format using
// the PTHTML2PDF class.
// 
// 'pdftron.PDF.PTHTML2PDF' is an optional PDFNet Add-On utility class that can be 
// used to convert HTML web pages into PDF documents by using an external module (html2pdf).
//
// html2pdf modules can be downloaded from http://www.pdftron.com/pdfnet/downloads.html.
//
// Users can convert HTML pages to PDF using the following operations:
// - Simple one line static method to convert a single web page to PDF. 
// - Convert HTML pages from URL or string, plus optional table of contents, in user defined order. 
// - Optionally configure settings for proxy, images, java script, and more for each HTML page. 
// - Optionally configure the PDF output, including page size, margins, orientation, and more. 
// - Optionally add table of contents, including setting the depth and appearance.
//---------------------------------------------------------------------------------------

int main(int argc, char * argv[])
{
    @autoreleasepool {

        int ret = 0;

        // The first step in every application using PDFNet is to initialize the 
        // library and set the path to common PDF resources. The library is usually 
        // initialized only once, but calling Initialize() multiple times is also fine.
        [PTPDFNet Initialize: 0];

        // For PTHTML2PDF we need to locate the PTHTML2PDF module. If placed with the 
        // PDFNet library, or in the current working directory, it will be loaded
        // automatically. Otherwise, it must be set manually using PTHTML2PDF.SetModulePath.
        [PTHTML2PDF SetModulePath: @"../../../Lib"];

        //--------------------------------------------------------------------------------
        // Example 1) Simple conversion of a web page to a PDF doc. 

        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] init];
        PTHTML2PDF *converter = [[PTHTML2PDF alloc] init];
        [converter InsertFromURL: @"http://www.gutenberg.org/wiki/Main_Page"];
        
            // now convert a web page, sending generated PDF pages to doc
            if ( [converter Convert: doc] )
                [doc SaveToFile: @"../../TestFiles/Output/html2pdf_example_01.pdf" flags: e_ptlinearized];
        else
            NSLog(@"Conversion failed. HTTP Code: %d\n%@", [converter GetHTTPErrorCode], [converter GetLog]);
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }

        //--------------------------------------------------------------------------------
        // Example 2) Modify the settings of the generated PDF pages and attach to an
        // existing PDF document. 

        @try
        {
            // open the existing PDF, and initialize the security handler
        PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/numbered.pdf"];
            [doc InitSecurityHandler];

            // create the PTHTML2PDF converter object and modify the output of the PDF pages
            PTHTML2PDF *converter = [[PTHTML2PDF alloc] init];
            [converter SetImageQuality: 25];
            [converter SetPaperSize: e_11x17];

            // insert the web page to convert
            [converter InsertFromURL: @"http://www.gutenberg.org/wiki/Main_Page"];

            // convert the web page, appending generated PDF pages to doc
            if ( [converter Convert: doc] )
                [doc SaveToFile: @"../../TestFiles/Output/html2pdf_example_02.pdf" flags: e_ptlinearized];
            else
                NSLog(@"Conversion failed. HTTP Code: %d\n%@", [converter GetHTTPErrorCode], [converter GetLog]);
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }

        //--------------------------------------------------------------------------------
        // Example 3) Convert multiple web pages, adding a table of contents, and setting
        // the first page as a cover page, not to be included with the table of contents outline. 

        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] init];

            PTHTML2PDF *converter = [[PTHTML2PDF alloc] init];

            // Add a cover page, which is excluded from the outline, and ignore any errors
            PTWebPageSettings *cover = [[PTWebPageSettings alloc] init];
            [cover SetLoadErrorHandling: e_ptignore];
            [cover SetIncludeInOutline: NO];
            [converter InsertFromURLWithSettings: @"http://www.gutenberg.org/wiki/Gutenberg:The_Sheet_Music_Project" settings: cover];

            // Add a table of contents settings (modifying the settings is optional)
            PTTOCSettings *toc = [[PTTOCSettings alloc] init];
            [toc SetDottedLines: NO];
            [converter InsertTOCWithSettings: toc];

            // Now add the rest of the web pages, disabling external links and 
            // skipping any web pages that fail to load.
            //
            // Note that the order of insertion matters, so these will appear
            // after the cover and table of contents, in the order below.
            PTWebPageSettings *settings = [[PTWebPageSettings alloc] init];
            [settings SetLoadErrorHandling: e_ptskip];
            [settings SetExternalLinks: NO];
            [converter InsertFromURLWithSettings: @"http://www.gutenberg.org/wiki/Main_Page" settings: settings];
            [converter InsertFromURLWithSettings: @"http://www.gutenberg.org/catalog/" settings: settings];
            [converter InsertFromURLWithSettings: @"http://www.gutenberg.org/browse/recent/last1" settings: settings];

            if ([converter Convert: doc] )
                [doc SaveToFile: @"../../TestFiles/Output/html2pdf_example_03.pdf" flags: e_ptlinearized];
            else
                NSLog(@"Conversion failed. HTTP Code: %d\n%@", [converter GetHTTPErrorCode], [converter GetLog]);
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }

        //--------------------------------------------------------------------------------
        // Example 4) Convert HTML string to PDF. 

        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] init];
        
            PTHTML2PDF *converter = [[PTHTML2PDF alloc] init];
        
            // Our HTML data
            NSString *html = @"<html><body><h1>Heading</h1><p>Paragraph.</p></body></html>";
            
            // Add html data
            [converter InsertFromHtmlString: html];
            // Note, InsertFromHtmlString can be mixed with the other Insert methods.
            
            if ( [converter Convert: doc] )
                [doc SaveToFile: @"../../TestFiles/Output/html2pdf_example_04.pdf" flags: e_ptlinearized];
            else
                NSLog(@"Conversion failed. HTTP Code: %d\n%@", [converter GetHTTPErrorCode], [converter GetLog]);
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }

        return ret;
    }
}
