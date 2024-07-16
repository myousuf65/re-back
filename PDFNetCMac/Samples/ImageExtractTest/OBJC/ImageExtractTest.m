//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//-----------------------------------------------------------------------------------
// This sample illustrates one approach to PDF image extraction 
// using PDFNet.
// 
// Note: Besides direct image export, you can also convert PDF images 
// to GDI+ Bitmap, or extract uncompressed/compressed image data directly 
// using element.GetImageData() (e.g. as illustrated in ElementReaderAdv 
// sample project).
//-----------------------------------------------------------------------------------

int image_counter = 0;

void ImageExtract(PTElementReader* reader)
{
	PTElement *element; 
	while ((element = [reader Next]) != NULL)
	{
		switch ([element GetType]) 
		{
		case e_ptimage:
		case e_ptinline_image: 
			{
				NSLog(@"--> Image: %d", ++image_counter);
				NSLog(@"    Width: %d", [element GetImageWidth]);
				NSLog(@"    Height: %d", [element GetImageHeight]);
				NSLog(@"    BPC: %d", [element GetBitsPerComponent]);

				PTMatrix2D *ctm = [element GetCTM];
				double x2=1, y2=1;
				[ctm Mult: [[PTPDFPoint alloc] initWithPx: x2 py: y2]];
				NSLog(@"    Coords: x1=%f, y1=%f, x2=%f, y2=%f", [ctm getM_h], [ctm getM_v], x2, y2);

				if ([element GetType] == e_ptimage) 
				{
					PTImage *image = [[PTImage alloc] initWithImage_xobject: [element GetXObject]];
                    
					NSString *path = [@"../../TestFiles/Output/" stringByAppendingPathComponent:[NSString stringWithFormat:@"image_extract1_%d", image_counter]];
					[image ExportToFile: path];
				}
			}
			break;
		case e_ptform:		// Process form XObjects
			[reader FormBegin]; 
            ImageExtract(reader);
			[reader End]; 
			break;
		default:
			break;
		}
	}
}

int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;

        // Initialize PDFNet
        [PTPDFNet Initialize: 0];

        // Example 1: 
        // Extract images by traversing the display list for 
        // every page. With this approach it is possible to obtain 
        // image positioning information and DPI.
        @try  
        {	 
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];

            PTElementReader *reader = [[PTElementReader alloc] init];
            //  Read every page
        PTPageIterator *itr;
            for (itr=[doc GetPageIterator: 1]; [itr HasNext]; [itr Next]) 
            {				
                [reader Begin: [itr Current]];
                ImageExtract(reader);
                [reader End];
            }

            NSLog(@"Done...");
        }
        @catch(NSException* e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        NSLog(@"----------------------------------------------------------------");

        // Example 2: 
        // Extract images by scanning the low-level document.
        @try  
        {	 
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
        
            [doc InitSecurityHandler];
            image_counter = 0;

            PTSDFDoc *cos_doc=[doc GetSDFDoc];
            int num_objs = [cos_doc XRefSize];
        int i;
            for(i=1; i<num_objs; ++i) 
            {
                PTObj * obj = [cos_doc GetObj: i];
                if((obj != NULL) && (![obj IsFree]) && ([obj IsStream])) 
                {
                    // Process only images
                    PTDictIterator *itr = [obj Find: @"Type"];
                    if((![itr HasNext]) || (![[[itr Value] GetName] isEqualToString: @"XObject"]))
                        continue;

                    itr = [obj Find: @"Subtype"];
                    if((![itr HasNext]) || (![[[itr Value] GetName] isEqualToString: @"Image"]))
                        continue;
                    
                    PTImage *image = [[PTImage alloc] initWithImage_xobject: obj];
                    NSLog(@"-. Image: %d", ++image_counter);
                    NSLog(@"    Width: %d", [image GetImageWidth]);
                    NSLog(@"    Height: %d", [image GetImageHeight]);
                    NSLog(@"    BPC: %d", [image GetBitsPerComponent]);
                
                    NSString *path = [@"../../TestFiles/Output/" stringByAppendingPathComponent:[NSString stringWithFormat:@"image_extract2_%d", image_counter]];
                [image ExportToFile: path];
                }
            }
        }
        @catch(NSException* e)
        {
        NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        return ret;
    }
}

