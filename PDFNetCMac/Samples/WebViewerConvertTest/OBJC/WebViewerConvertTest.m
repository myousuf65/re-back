//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

void BulkConvertRandomFilesToXod();

//---------------------------------------------------------------------------------------
// The following sample illustrates how to convert PDF, XPS, image, MS Office, and 
// other image document formats to XOD format.
//
// Certain file formats such as PDF, generic XPS, EMF, and raster image formats can 
// be directly converted to XOD. Other formats such as MS Office 
// (Word, Excel, Publisher, Powerpoint, etc) can be directly converted via interop. 
// These types of conversions guarantee optimal output, while preserving important 
// information such as document metadata, intra document links and hyper-links, 
// bookmarks etc. 
//
// In case there is no direct conversion available, PDFNet can still convert from 
// any printable document to XOD using a virtual printer driver. To check 
// if a virtual printer is required use Convert::RequiresPrinter(filename). In this 
// case the installing application must be run as administrator. The manifest for this 
// sample specifies appropriate the UAC elevation. The administrator privileges are 
// not required for direct or interop conversions. 
//
// Please note that PDFNet Publisher (i.e. 'pdftron.PDF.Convert.ToXod') is an
// optionally licensable add-on to PDFNet Core SDK. For details, please see
// http://www.pdftron.com/webviewer/licensing.html.
//---------------------------------------------------------------------------------------

int main(int argc, char *argv[])
{
    @autoreleasepool {

        int err = 0;
        @try 
        {
            [PTPDFNet Initialize: 0];
            {
                // Sample 1:
                // Directly convert from PDF to XOD.
                [PTConvert ToXod: @"../../TestFiles/newsletter.pdf" out_filename: @"../../TestFiles/Output/from_pdf.xod"];

                // Sample 2:
                // Directly convert from generic XPS to XOD.
                [PTConvert ToXod: @"../../TestFiles/simple-xps.xps" out_filename: @"../../TestFiles/Output/from_xps.xod"];

                // Sample 3:
                // Convert from MS Office (does not require printer driver for Office 2007+)
                // and other document formats to XOD.
                BulkConvertRandomFilesToXod();
            }
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            err = 1;
        }
        
        NSLog(@"Done.");
        return err;
    }
}



void BulkConvertRandomFilesToXod()
{
    
    NSArray* testFiles = @[@"butterfly.png", @"numbered.pdf", @"dice.jpg", @"simple-xps.xps"];
	
	NSString *inputPath = @"../../TestFiles/";
	NSString *outputPath = @"../../TestFiles/Output/";
	
	int err = 0;

	for(NSString* testFile in testFiles)
	{
        @try
		{
            NSString* outputFileName = [testFile.lastPathComponent stringByAppendingPathExtension:@"xod"];
			NSString *inputFilePath = [inputPath stringByAppendingPathComponent: testFile];
            NSString *outputFilePath = [outputPath stringByAppendingPathComponent:outputFileName];
			[PTConvert ToXod: inputFilePath out_filename: outputFilePath];
			NSLog(@"Converted file: %@\n            to: %@", inputFilePath, outputFilePath);
		}
		@catch(NSException *e)
		{
			NSLog(@"Unable to convert file %@", testFile);
			NSLog(@"%@", e.reason);
			err = 1;
		}
	}

	if( err ) {
		NSLog(@"ConvertFile failed");
	}
	else {
		NSLog(@"ConvertFile succeeded");
	}
}
