//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

void ProcessReaderTestElements(PTElementReader *reader)
{
    PTElement *element;
	for (element=[reader Next]; element != NULL; element = [reader Next]) 	// Read page contents
	{
		switch ([element GetType])
		{
		case e_ptpath:						// Process path data...
			{
				PTPathData *data = [element GetPathData];
				NSArray *points = [data GetPoints];
			}
 			break;
		case e_pttext_obj: 				// Process text strings...
			{
                NSString* nsdata = [element GetTextString];
                unsigned char* data = [nsdata UTF8String]; 
                printf("%s\n", data);
			}
			break;
		case e_ptform:				// Process form XObjects
			{
				[reader FormBegin]; 
                ProcessReaderTestElements(reader);
				[reader End]; 
			}
			break;
		default:
                ;
		}
	}
}

int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];
        
        @try	// Extract text data from all pages in the document
        {
            printf("-------------------------------------------------\n");
            printf("Sample 1 - Extract text data from all pages in the document.\n");
            printf("Opening the input pdf...\n");

            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];
            
            PTPageIterator *itr;
            PTElementReader *page_reader = [[PTElementReader alloc] init];

            for (itr = [doc GetPageIterator: 1]; [itr HasNext]; [itr Next])		//  Read every page
            {				
                [page_reader Begin: [itr Current]];
            ProcessReaderTestElements(page_reader);
                [page_reader End];
            }

            printf("Done.\n");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        return ret;
    }

}
