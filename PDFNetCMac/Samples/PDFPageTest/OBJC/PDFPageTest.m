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

        // Sample 1 - Split a PDF document into multiple pages
        @try
        {
            NSLog(@"_______________________________________________");
            NSLog(@"Sample 1 - Split a PDF document into multiple pages...");
            NSLog(@"Opening the input pdf...");
            PTPDFDoc *in_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [in_doc InitSecurityHandler];

            int page_num = [in_doc GetPageCount];
            int i;
            for (i=1; i<=page_num; ++i)
            {
                PTPDFDoc *new_doc = [[PTPDFDoc alloc] init];
            NSString *output_file = [@"../../TestFiles/Output/" stringByAppendingFormat: @"newsletter_split_page_%d.pdf", i]; 
                [new_doc InsertPages: 0 src_doc: in_doc start_page: i end_page: i flag: e_ptinsert_none];
                [new_doc SaveToFile: output_file flags: e_ptremove_unused];
                NSLog(@"Done. Result saved in newsletter_split_page_%d.pdf", i);
            }
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        // Sample 2 - Merge several PDF documents into one
        @try
        {
            NSLog(@"_______________________________________________");
            NSLog(@"Sample 2 - Merge several PDF documents into one...");
            PTPDFDoc *new_doc = [[PTPDFDoc alloc] init];
            [new_doc InitSecurityHandler];

            int page_num = 15;
            int i;
            for (i=1; i<=page_num; ++i)
            {
            NSString *input_file = [@"../../TestFiles/Output/" stringByAppendingFormat: @"newsletter_split_page_%d.pdf", i]; 
                NSLog(@"Opening newsletter_split_page_%d.pdf", i);
            PTPDFDoc *in_doc = [[PTPDFDoc alloc] initWithFilepath: input_file];
                [new_doc InsertPages: i src_doc: in_doc start_page: 1 end_page: [in_doc GetPageCount] flag: e_ptinsert_none];
            }
            [new_doc SaveToFile: @"../../TestFiles/Output/newsletter_merge_pages.pdf" flags: e_ptremove_unused];
            NSLog(@"Done. Result saved in newsletter_merge_pages.pdf");
            
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }


        // Sample 3 - Delete every second page
        @try	
        {
            NSLog(@"_______________________________________________");
            NSLog(@"Sample 3 - Delete every second page...");
            NSLog(@"Opening the input pdf...");
            PTPDFDoc *in_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [in_doc InitSecurityHandler];
            
            int page_num = [in_doc GetPageCount];
            while (page_num>=1)
            {
                PTPageIterator *itr = [in_doc GetPageIterator: page_num];
                [in_doc PageRemove: itr];
                page_num -= 2;
            }		
            
            [in_doc SaveToFile: @"../../TestFiles/Output/newsletter_page_remove.pdf" flags: 0];
            NSLog(@"Done. Result saved in newsletter_page_remove.pdf...");

        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        // Sample 4 - Inserts a page from one document at different 
        // locations within another document
        @try
        {
            NSLog(@"_______________________________________________");
            NSLog(@"Sample 4 - Insert a page at different locations...");
            NSLog(@"Opening the input pdf...");
            
            PTPDFDoc *in1_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [in1_doc InitSecurityHandler];

            PTPDFDoc *in2_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/fish.pdf"];
            [in2_doc InitSecurityHandler];
            
            PTPageIterator *src_page = [in2_doc GetPageIterator: 1];
            PTPageIterator *dst_page = [in1_doc GetPageIterator: 1];
            int page_num = 1;
            
            while ([dst_page HasNext]) {
                if ((page_num++ % 3) == 0) {
                    [in1_doc PageInsert: dst_page page: [src_page Current]];
            }
                
            [dst_page Next];
        }
            
            [in1_doc SaveToFile: @"../../TestFiles/Output/newsletter_page_insert.pdf" flags: 0];
            NSLog(@"Done. Result saved in newsletter_page_insert.pdf...");
            

        }@catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        // Sample 5 - Replicate pages within a single document
        @try	
        {

            NSLog(@"_______________________________________________");
            NSLog(@"Sample 5 - Replicate pages within a single document...");
            NSLog(@"Opening the input pdf...");
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];
            
            // Replicate the cover page three times (copy page #1 and place it before the 
            // seventh page in the document page sequence)
            PTPage *cover = [doc GetPage: 1];
            PTPageIterator *p7 = [doc GetPageIterator: 7];
            [doc PageInsert: p7 page: cover];
            [doc PageInsert: p7 page: cover];
            [doc PageInsert: p7 page: cover];
            
            // Replicate the cover page two more times by placing it before and after
            // existing pages.
            [doc PagePushFront: cover];
            [doc PagePushBack: cover];
            
            [doc SaveToFile: @"../../TestFiles/Output/newsletter_page_clone.pdf" flags: 0];
            NSLog(@"Done. Result saved in newsletter_page_clone.pdf...");
            
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        // Sample 6 - Use ImportPages() in order to copy multiple pages at once 
        // in order to preserve shared resources between pages (e.g. images, fonts, 
        // colorspaces, etc.)
        @try
        {

            NSLog(@"_______________________________________________");
            NSLog(@"Sample 6 - Preserving shared resources using ImportPages...");
            NSLog(@"Opening the input pdf...");
            PTPDFDoc *in_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [in_doc InitSecurityHandler];

            PTPDFDoc *new_doc = [[PTPDFDoc alloc] init];
            
            PTVectorPage *copy_pages = [[PTVectorPage alloc] init];
            PTPageIterator *itr;
            for (itr=[in_doc GetPageIterator: 1]; [itr HasNext]; [itr Next])
            {
                [copy_pages add: [itr Current]];
            }
            
            PTVectorPage *imported_pages = [new_doc ImportPages: copy_pages import_bookmarks: NO];
            int i;
            for (i=0; i<[imported_pages size]; ++i)
            {
                [new_doc PagePushFront: [imported_pages get: i]]; // Order pages in reverse order. 
                // Use PagePushBack() if you would like to preserve the same order.
            }		
            
            [new_doc SaveToFile: @"../../TestFiles/Output/newsletter_import_pages.pdf" flags: 0];
            NSLog(@"Done. Result saved in newsletter_import_pages.pdf...");
            NSLog(@"\n");
            NSLog(@"Note that the output file size is less than half the size");
            NSLog(@"of the file produced using individual page copy operations");
            NSLog(@"between two documents");

        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        return ret;
    }
}
