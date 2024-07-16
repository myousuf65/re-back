//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

// This sample illustrates how to use basic SDF API (also known as Cos) to edit an 
// existing document.

int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        @try
        {
            NSLog(@"Opening the test file...");

            // Here we create a SDF/Cos document directly from PDF file. In case you have 
            // PDFDoc you can always access SDF/Cos document using PDFDoc.GetSDFDoc() method.
            PTSDFDoc *doc = [[PTSDFDoc alloc] initWithFilepath: @"../../TestFiles/fish.pdf"];
            [doc InitSecurityHandler];

            NSLog(@"Modifying info dictionary, adding custom properties, embedding a stream...");
            PTObj * trailer = [doc GetTrailer];			// Get the trailer

            // Now we will change PDF document information properties using SDF API

            // Get the Info dictionary. 
            PTDictIterator *itr = [trailer Find: @"Info"];	
            PTObj * info = [[PTObj alloc] init];
            if ([itr HasNext]) 
            {
                info = [itr Value];
                // Modify 'Producer' entry.
                [info PutString: @"Producer" value: @"PDFTron PDFNet"];

                // Read title entry (if it is present)
            itr = [info Find: @"Author"]; 
                if ([itr HasNext]) 
                {
                    NSString *oldstr = [[itr Value] GetAsPDFText];
                    [info PutText: @"Author" value: [oldstr stringByAppendingString: @"- Modified"]];
                }
                else 
                {
                    [info PutString: @"Author" value: @"Me, myself, and I"];
                }
            }
            else 
            {
                // Info dict is missing. 
                info = [trailer PutDict: @"Info"];
                [info PutString: @"Producer" value: @"PDFTron PDFNet"];
            [info PutString: @"Title" value: @"My document"];
            }

            // Create a custom inline dictionary within Info dictionary
            PTObj * custom_dict = [info PutDict: @"My Direct Dict"];
            [custom_dict PutNumber: @"My Number" value: 100];	 // Add some key/value pairs
            [custom_dict PutArray: @"My Array"];

            // Create a custom indirect array within Info dictionary
            PTObj * custom_array = [doc CreateIndirectArray];	
            [info Put: @"My Indirect Array" obj: custom_array];	// Add some entries
            
            // Create indirect link to root
            [custom_array PushBack: [[trailer Get: @"Root"] Value]];

            // Embed a custom stream (file mystream.txt).
            PTMappedFile *embed_file = [[PTMappedFile alloc] initWithFilename: @"../../TestFiles/my_stream.txt"];
            PTFilterReader *mystm = [[PTFilterReader alloc] initWithFilter: embed_file];
            [custom_array PushBack: [doc CreateIndirectStream: mystm]];

            // Save the changes.
            NSLog(@"Saving modified test file...");
            [doc SaveSDFDocToFile: @"../../TestFiles/Output/sdftest_out.pdf" flags:0 header: @"%PDF-1.4"];

            NSLog(@"Test completed.");
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }

        return ret;
    }
}
