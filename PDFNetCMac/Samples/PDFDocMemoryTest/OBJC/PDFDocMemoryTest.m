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

        // Relative path to the folder containing test files.
        NSString *input_path = @"../../TestFiles/";
        NSString *output_path = @"../../TestFiles/Output/";


        // The following sample illustrates how to read/write a PDF document from/to 
        // a memory buffer.  This is useful for applications that work with dynamic PDF
        // documents that don't need to be saved/read from a disk.
        @try  
        {
            // Read a PDF document in a memory buffer.
            PTMappedFile *file = [[PTMappedFile alloc] initWithFilename: @"../../TestFiles/tiger.pdf"];
            unsigned long file_sz = [file FileSize];
        
            PTFilterReader *file_reader = [[PTFilterReader alloc] initWithFilter: file];

        NSData *mem = [file_reader Read: file_sz];
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithBuf: mem buf_size: file_sz];

            [doc InitSecurityHandler];
            int num_pages = [doc GetPageCount];

            PTElementWriter *writer = [[PTElementWriter alloc] init];
            PTElementReader *reader = [[PTElementReader alloc] init];
            PTElement *element;

            // Create a duplicate of every page but copy only path objects
        int i;
            for(i=1; i<=num_pages; ++i)
            {
                PTPageIterator *itr = [doc GetPageIterator: 2*i-1];

                [reader Begin: [itr Current]];
            PTPage *new_page = [doc PageCreate: [[itr Current] GetMediaBox]];
                PTPageIterator *next_page = itr;
                [next_page Next]; 
            [doc PageInsert: next_page page: new_page];

                [writer WriterBeginWithPage: new_page placement: e_ptoverlay page_coord_sys: YES compress: YES];
                while ((element = [reader Next]) != NULL) 	// Read page contents
                {
                    //if ([element GetType] == e_ptpath)
                [writer WriteElement: element];
                }

                [writer End];
                [reader End];
            }

        [doc SaveToFile: @"../../TestFiles/Output/doc_memory_edit.pdf" flags: e_ptremove_unused];
        //[doc SaveToFile: @"../../TestFiles/Output/doc_memory_edit.pdf" flags: e_ptlinearized];
        
            // Save the document to a memory buffer.
            NSData *buf = [doc SaveToBuf: e_ptremove_unused];
            // NSData *buf = [doc SaveToBuf: e_ptlinearized];

            // Write the contents of the buffer to the disk
            {
            [buf writeToFile: @"../../TestFiles/Output/doc_memory_edit.txt" atomically: NO];
            }

            // Read some data from the file stored in memory
            [reader Begin: [doc GetPage: 1]];
            while ((element = [reader Next]) !=0) {
                if ([element GetType] == e_ptpath) printf("%s", "Path, ");
            }
            [reader End];

            NSLog(@"Done. Result saved in doc_memory_edit.pdf and doc_memory_edit.txt ...");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
            
        return ret;

    }
}
