//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

// This sample project illustrates how to recompress bi-tonal images in an 
// existing PDF document using JBIG2 compression. The sample is not intended 
// to be a generic PDF optimization tool.
//
// You can download the entire document using the following link:
//   http://www.pdftron.com/net/samplecode/data/US061222892.pdf
//
int main(int argc, char *argv[]) 
{
    @autoreleasepool {

        [PTPDFNet Initialize: 0];
        
        @try 
        {
            PTPDFDoc *pdf_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/US061222892-a.pdf"];
            [pdf_doc InitSecurityHandler];

            PTSDFDoc *cos_doc = [pdf_doc GetSDFDoc];
            int num_objs = [cos_doc XRefSize];
        int i=1;
            for(; i<num_objs; ++i) 
            {
                PTObj * obj = [cos_doc GetObj: i];
                if(obj && ![obj IsFree] && [obj IsStream]) 
                {
                    // Process only images
                    PTDictIterator *itr = [obj Find: @"Subtype"];
                    if(![itr HasNext] || !([[[itr Value] GetName] isEqualToString:@"Image"]))
                        continue;
                    
                    PTImage *input_image = [[PTImage alloc] initWithImage_xobject: obj];
                    // Process only gray-scale images
                    if([input_image GetComponentNum] != 1)
                        continue;
                    int bpc = [input_image GetBitsPerComponent];
                    if(bpc != 1)	// Recompress only 1 BPC images
                        continue;

                    // Skip images that are already compressed using JBIG2
                    itr = [obj Find: @"Filter"];
                    if ([itr HasNext] && [[itr Value] IsName] && 
                        [[[itr Value] GetName] isEqualToString:@"JBIG2Decode"]) continue;

                    PTFilter *filter=[obj GetDecodedStream];
                    PTFilterReader *reader = [[PTFilterReader alloc] initWithFilter: filter];


                    PTObjSet *hint_set = [[PTObjSet alloc] init]; 	// A hint to image encoder to use JBIG2 compression
                    PTObj * hint=[hint_set CreateArray];
                
                    [hint PushBackName: @"JBIG2"];
                    [hint PushBackName: @"Lossless"];

                    PTImage *new_image = [PTImage CreateWithFilterData: cos_doc image_data: reader width: [input_image GetImageWidth] height: [input_image GetImageHeight] bpc: 1 color_space: [PTColorSpace CreateDeviceGray] encoder_hints: hint];

                    PTObj * new_img_obj = [new_image GetSDFObj];
                    itr = [obj Find: @"Decode"];
                    if([itr HasNext])
                        [new_img_obj Put: @"Decode" obj: [itr Value]];
                    itr = [obj Find: @"ImageMask"];
                    if ([itr HasNext])
                        [new_img_obj Put: @"ImageMask" obj: [itr Value]];
                    itr = [obj Find: @"Mask"];
                    if ([itr HasNext])
                        [new_img_obj Put: @"Mask" obj: [itr Value]];

                    [cos_doc Swap: i obj_num2: [new_img_obj GetObjNum]];
                }
            }

            [pdf_doc SaveToFile: @"../../TestFiles/Output/US061222892_JBIG2.pdf" flags: e_ptremove_unused];
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            NSLog(@"Please make sure that the pathname to the test file is correct.");
        }
        
        NSLog(@"Done.");

        return 0;
    }
}
