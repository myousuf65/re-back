//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2012 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//---------------------------------------------------------------------------------------
// The sample code shows how to edit the page display list and how to modify graphics state 
// attributes on existing Elements. In particular the sample program strips all images from 
// the page, changes path fill color to red, and changes text color to blue. 
//---------------------------------------------------------------------------------------

static void ProcessElementEditTestElements(PTElementReader *reader, PTElementWriter *writer, NSMutableSet *visited)
{
	PTElement *element;	
	while ((element = [reader Next])) 	// Read page contents
	{
		switch ([element GetType])
		{		
		case e_ptimage: 
		case e_ptinline_image: 
			// remove all images by skipping them			
			break;
		case e_ptpath:
			{
				// Set all paths to red color.
				PTGState *gs = [element GetGState];
				[gs SetFillColorSpace: [PTColorSpace CreateDeviceRGB]];
				PTColorPt *cp = [[PTColorPt alloc] initWithX: 1 y: 0 z: 0 w: 0];
				[gs SetFillColorWithColorPt: cp];
				[writer WriteElement: element];
				break;
			}
		case e_pttext_obj:
			{
				// Set all text to blue color.
				PTGState *gs = [element GetGState];
				[gs SetFillColorSpace: [PTColorSpace CreateDeviceRGB]];
				PTColorPt *cp = [[PTColorPt alloc] initWithX: 0 y: 0 z: 1 w: 0];
				[gs SetFillColorWithColorPt: cp];
				[writer WriteElement: element];
				break;
			}
		case e_ptform:
			{
				[writer WriteElement: element]; // write Form XObject reference to current stream

				PTObj *form_obj = [element GetXObject];

				if (![visited containsObject:@([form_obj GetObjNum])]) // if this XObject has not been processed
				{
					// recursively process the Form XObject
					[visited addObject:@([form_obj GetObjNum])];
					PTElementWriter *new_writer = [[PTElementWriter alloc] init];
					[reader FormBegin];
					[new_writer WriterBeginWithSDFObj:form_obj compress:YES];
					ProcessElementEditTestElements(reader, new_writer, visited);
					[reader End];
					[new_writer End];
				}

				break; 
			}
		default:
                [writer WriteElement: element];
		}
	}
}

int main(int argc, char *argv[])
{
    @autoreleasepool {
	
        int ret = 0;
        [PTPDFNet Initialize: 0];

        @try 
        {
            NSLog(@"Opening the input file...");
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];

            PTElementWriter *writer = [[PTElementWriter alloc] init];
            PTElementReader *reader = [[PTElementReader alloc] init];
            NSMutableSet *visited = [[NSMutableSet alloc] init];

            PTPageIterator *itr = [doc GetPageIterator: 1];
            while ([itr HasNext])
            {
                PTPage *page = [itr Current];
                [visited addObject:@([[page GetSDFObj] GetObjNum])];

                [reader ReaderBeginWithPage: page ocg_context: 0];
                [writer Begin: page placement: e_ptreplacement page_coord_sys: NO];
                
                ProcessElementEditTestElements(reader, writer, visited);

                [writer End];
                [reader End];
                
                [itr Next];
            }

            // Save modified document
        [doc SaveToFile: @"../../TestFiles/Output/newsletter_edited.pdf" flags: e_ptremove_unused];		
        NSLog(@"Done. Result saved in newsletter_edited.pdf...");
        }
        @catch(NSException *e)
        {
            NSLog(@"Caught PDFNet exception: %@", e.reason);
            ret = 1;
        }
        
        return ret;
        
    }
    
}
