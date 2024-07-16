//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//-----------------------------------------------------------------------------------------
// The sample code illustrates how to use the ContentReplacer class to make using 
// 'template' pdf documents easier.
//-----------------------------------------------------------------------------------------

int main(int argc, char *argv[])
{
    @autoreleasepool {

        NSString* input_path = @"../../TestFiles/";
        NSString* output_path = @"../../TestFiles/Output/";
        
        int ret = 0;
        [PTPDFNet Initialize: 0];

        // The following example illustrates how to replace an image in a certain region,
        // and how to change template text.
        @try  
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: [input_path stringByAppendingPathComponent: @"BusinessCardTemplate.pdf"]];
            [doc InitSecurityHandler];
            
            // first, replace the image on the first page
            PTContentReplacer *replacer = [[PTContentReplacer alloc] init];
            PTPage *page = [doc GetPage: 1];
            PTImage *img = [PTImage Create: [doc GetSDFDoc] filename: [input_path stringByAppendingPathComponent: @"peppers.jpg"]];
            [replacer AddImage: [page GetMediaBox] replacement_image: [img GetSDFObj]];
            // next, replace the text place holders on the second page
            [replacer AddString: @"NAME" replacement_text: @"John Smith"];
            [replacer AddString: @"QUALIFICATIONS" replacement_text: @"Philosophy Doctor"]; 
            [replacer AddString: @"JOB_TITLE" replacement_text: @"Software Developer"]; 
            [replacer AddString: @"ADDRESS_LINE1" replacement_text: @"#100 123 Software Rd"]; 
            [replacer AddString: @"ADDRESS_LINE2" replacement_text: @"Vancouver, BC"]; 
            [replacer AddString: @"PHONE_OFFICE" replacement_text: @"604-730-8989"]; 
            [replacer AddString: @"PHONE_MOBILE" replacement_text: @"604-765-4321"]; 
            [replacer AddString: @"EMAIL" replacement_text: @"info@pdftron.com"]; 
            [replacer AddString: @"WEBSITE_URL" replacement_text: @"http://www.pdftron.com"]; 
            // finally, apply
            [replacer Process: page];

            [doc SaveToFile: [output_path stringByAppendingPathComponent: @"BusinessCard.pdf"] flags: 0];
            NSLog(@"Done. Result saved in BusinessCard.pdf");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        // The following example illustrates how to replace text in a given region
        @try  
        {
            // Open the document that was saved in the previous code sample
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: [input_path stringByAppendingPathComponent: @"newsletter.pdf"]];
            [doc InitSecurityHandler];
            
            PTContentReplacer *replacer = [[PTContentReplacer alloc] init];
            PTPage *page = [doc GetPage: 1];
            PTPDFRect * target_region = [page GetMediaBox];
            [replacer AddText: target_region replacement_text: @"hello hello hello hello hello hello hello hello hello hello"];
            [replacer Process: page];

            [doc SaveToFile: [output_path stringByAppendingPathComponent: @"ContentReplaced.pdf"] flags: 0];

            NSLog(@"Done. Result saved in ContentReplaced.pdf");
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


