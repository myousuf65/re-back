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

        @try // Test  - Adjust the position of content within the page.
        {
            NSLog(@"_______________________________________________");
            NSLog(@"Opening the input pdf...");

            PTPDFDoc *input_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/tiger.pdf"];
            [input_doc InitSecurityHandler];

            PTPageIterator *pg_itr1 = [input_doc GetPageIterator: 1];

            PTPDFRect * media_box = [[pg_itr1 Current] GetMediaBox]; 

            [media_box SetX1: [media_box GetX1] - 200];	// translate the page 200 units (1 uint = 1/72 inch)
            [media_box SetX2: [media_box GetX2] - 200];

            [media_box Update: [[PTObj alloc] init]];		
        
            [input_doc SaveToFile: @"../../TestFiles/Output/tiger_shift.pdf" flags: 0];

            NSLog(@"Done. Result saved in tiger_shift...");
        }
        @catch(NSException *e)
        {
            NSLog(@"Caught PDFNet exception: %@", e.reason);
            ret = 1;
        }
        
        return ret;
    }
}
