//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//-----------------------------------------------------------------------------------
// The sample illustrates how to work with PDF page labels.
//
// PDF page labels can be used to describe a page. This is used to 
// allow for non-sequential page numbering or the addition of arbitrary 
// labels for a page (such as the inclusion of Roman numerals at the 
// beginning of a book). PDFNet PTPageLabel object can be used to specify 
// the numbering style to use (for example, upper- or lower-case Roman, 
// decimal, and so forth), the starting number for the first page,
// and an arbitrary prefix to be pre-appended to each number (for 
// example, "A-" to generate "A-1", "A-2", "A-3", and so forth.)
//-----------------------------------------------------------------------------------
int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        @try  
        {	
            //-----------------------------------------------------------
            // Example 1: Add page labels to an existing or newly created PDF
            // document.
            //-----------------------------------------------------------
            {
                PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
                [doc InitSecurityHandler];

                // Create a page labeling scheme that starts with the first page in 
                // the document (page 1) and is using uppercase roman numbering 
                // style. 
                PTPageLabel *L1 = [PTPageLabel Create: [doc GetSDFDoc] style: e_ptroman_uppercase prefix: @"My Prefix " start_at: 1];
            [doc SetPageLabel: 1 label: L1];

                // Create a page labeling scheme that starts with the fourth page in 
                // the document and is using decimal Arabic numbering style. 
                // Also the numeric portion of the first label should start with number 
                // 4 (otherwise the first label would be "My Prefix 1"). 
            PTPageLabel *L2 = [PTPageLabel Create: [doc GetSDFDoc] style: e_ptdecimal prefix: @"My Prefix " start_at: 4];
            [doc SetPageLabel: 4 label: L2];

                // Create a page labeling scheme that starts with the seventh page in 
                // the document and is using alphabetic numbering style. The numeric 
                // portion of the first label should start with number 1. 
                PTPageLabel *L3 = [PTPageLabel Create: [doc GetSDFDoc] style: e_ptalphabetic_uppercase prefix: @"My Prefix " start_at: 1];
                [doc SetPageLabel: 7 label: L3];

                [doc SaveToFile: @"../../TestFiles/Output/newsletter_with_pagelabels.pdf" flags: e_ptlinearized];
                NSLog(@"Done. Result saved in newsletter_with_pagelabels.pdf...");
            }

            //-----------------------------------------------------------
            // Example 2: Read page labels from an existing PDF document.
            //-----------------------------------------------------------
            {
                PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/newsletter_with_pagelabels.pdf"];
                [doc InitSecurityHandler];

                PTPageLabel *label;
                int page_num = [doc GetPageCount];
            int i=1;
                for (; i<=page_num; ++i) 
                {
                    NSLog(@"Page number: %d", i); 
                    label = [doc GetPageLabel: i];
                    if ([label IsValid]) {
                        NSLog(@" Label: %@", [label GetLabelTitle: i]); 
                    }
                    else {
                        NSLog(@" No Label."); 
                    }
                }
            }

            //-----------------------------------------------------------
            // Example 3: Modify page labels from an existing PDF document.
            //-----------------------------------------------------------
            {
                PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/newsletter_with_pagelabels.pdf"];
                [doc InitSecurityHandler];

                // Remove the alphabetic labels from example 1.
                [doc RemovePageLabel: 7]; 

                // Replace the Prefix in the decimal labels (from example 1).
                PTPageLabel *label = [doc GetPageLabel: 4];
                if ([label IsValid]) {
                    [label SetPrefix: @"A"];
                    [label SetStart: 1];
                }

                // Add a new label
                PTPageLabel *new_label = [PTPageLabel Create: [doc GetSDFDoc] style: e_ptdecimal prefix: @"B" start_at: 1];
                [doc SetPageLabel: 10 label: new_label];  // starting from page 10.

                [doc SaveToFile: @"../../TestFiles/Output/newsletter_with_pagelabels_modified.pdf" flags: e_ptlinearized];
                NSLog(@"Done. Result saved in newsletter_with_pagelabels_modified.pdf...");

                int page_num = [doc GetPageCount];
            int i=1;
                for (; i<=page_num; ++i) 
                {
                    NSLog(@"Page number: %d", i); 
                    label = [doc GetPageLabel: i];
                    if ([label IsValid]) {
                        NSLog(@" Label: %@", [label GetLabelTitle: i]);
                    }
                    else {
                        NSLog(@" No Label."); 
                    }
                }
            }

            //-----------------------------------------------------------
            // Example 4: Delete all page labels in an existing PDF document.
            //-----------------------------------------------------------
            {
                PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/newsletter_with_pagelabels.pdf"];
                [[doc GetRoot] EraseDictElementWithKey: @"PageLabels"];
                
            [doc SaveToFile: @"../../TestFiles/Output/newsletter_with_pagelabels_removed.pdf" flags: e_ptlinearized];
                NSLog(@"Done. Result saved in newsletter_with_pagelabels_removed.pdf...");
            }
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
            
        return ret;
    }
}
