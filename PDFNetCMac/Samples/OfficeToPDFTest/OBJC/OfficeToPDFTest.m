//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//---------------------------------------------------------------------------------------
// The following sample illustrates how to use the PDF::Convert utility class to convert 
// MS office files to PDF
//
// This conversion is performed entirely within the PDFNet and has *no* external or
// system dependencies dependencies -- Conversion results will be the sam whether
// on Windows, Linux or Android.
//
// Please contact us if you have any questions. 
//---------------------------------------------------------------------------------------




@interface OfficeToPDFTest : NSObject {}
+ (void)SimpleConvertWithInputFilename:(NSString*)input_filename
    outputFilename:(NSString*)output_filename;
+ (bool)FlexibleConvertWithInputFilename:(NSString*)input_filename
    outputFilename:(NSString*)output_filename;
@end

@implementation OfficeToPDFTest : NSObject {}

+ (void)SimpleConvertWithInputFilename:(NSString*)input_filename 
    outputFilename:(NSString*)output_filename
{
	NSString *input_path = @"../../TestFiles/";
	NSString *output_path = @"../../TestFiles/Output/";
	
    // Start with a PDFDoc (the conversion destination)
    PTPDFDoc* pdfDoc = [[PTPDFDoc alloc] init];

    // perform the conversion with no optional parameters
    [PTConvert OfficeToPDF:pdfDoc
        in_filename:[NSString stringWithFormat:@"%@/%@", input_path, input_filename]
        options:Nil];

    [pdfDoc SaveToFile: [NSString stringWithFormat:@"%@/%@", output_path, output_filename]
        flags: e_ptremove_unused];

    NSLog(@"Saved: %@/%@\n", output_path, output_filename);
}

+ (bool)FlexibleConvertWithInputFilename:(NSString*)input_filename 
    outputFilename:(NSString*)output_filename
{
	NSString *input_path = @"../../TestFiles/";
	NSString *output_path = @"../../TestFiles/Output/";
	
    // Start with a PDFDoc (the conversion destination)
    PTPDFDoc* pdfDoc = [[PTPDFDoc alloc] init];

    PTOfficeToPDFOptions* options = [[PTOfficeToPDFOptions alloc] init];
    [options SetSmartSubstitutionPluginPath:input_path];

    // create a conversion object with optional parameters
    PTDocumentConversion* conversion = [PTConvert StreamingPDFConversionWithDoc: pdfDoc
        in_filename:[NSString stringWithFormat:@"%@/%@", input_path, input_filename]
        options:options];
    NSLog(@"%@: %.0f%% %@\n", input_filename, [conversion GetProgress]*100.0, [conversion GetProgressLabel]);

    // convert each page, and report progress
    while([conversion GetConversionStatus] == e_ptIncomplete)
    {
        [conversion ConvertNextPage];
        NSLog(@"%@: %.0f%% %@\n", input_filename, [conversion GetProgress]*100.0, [conversion GetProgressLabel]);
    }

    if ([conversion TryConvert] == e_ptSuccess)
    {
        // print out any extra information about the conversion
        int num_warnings = [conversion GetNumWarnings];
        for (int i = 0; i < num_warnings; ++i)
        {
           NSLog(@"Warning: %@\n", [conversion GetWarningString: i]);
        }

        //save the result 
        [pdfDoc SaveToFile: [NSString stringWithFormat:@"%@/%@", output_path, output_filename]
            flags: e_ptremove_unused];

        NSLog(@"Saved: %@/%@\n", output_path, output_filename);

        return true;
    }
    else
    {
        NSLog(@"Encountered an error during conversion: %@\n", [conversion GetErrorString]);
    }

    return false;
}

@end

int main(int argc, char *argv[])
{

    @autoreleasepool {

        [PTPDFNet Initialize: 0];

        // convert using the simple one-line interface
        [OfficeToPDFTest SimpleConvertWithInputFilename:@"simple-word_2007.docx"
            outputFilename:@"simple-word_2007_a.pdf"];

        // convert using the more flexible page-by-page interface
        [OfficeToPDFTest FlexibleConvertWithInputFilename:@"the_rime_of_the_ancient_mariner.docx"
            outputFilename:@"the_rime_of_the_ancient_mariner.pdf"];
        
        return 0;
    }
}
