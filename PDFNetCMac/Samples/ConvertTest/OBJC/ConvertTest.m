//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//---------------------------------------------------------------------------------------
// The following sample illustrates how to use the PDF::Convert utility class to convert 
// documents and files to PDF, XPS, SVG, or EMF.
//
// Certain file formats such as XPS, EMF, PDF, and raster image formats can be directly 
// converted to PDF or XPS. Other formats are converted using a virtual driver. To check 
// if ToPDF (or ToXPS) require that PDFNet printer is installed use Convert::RequiresPrinter(filename). 
// The installing application must be run as administrator. The manifest for this sample 
// specifies appropriate the UAC elevation.
//
// Note: the PDFNet printer is a virtual XPS printer supported on Vista SP1 and Windows 7.
// For Windows XP SP2 or higher, or Vista SP0 you need to install the XPS Essentials Pack (or 
// equivalent redistributables). You can download the XPS Essentials Pack from:
//		http://www.microsoft.com/downloads/details.aspx?FamilyId=B8DCFFDD-E3A5-44CC-8021-7649FD37FFEE&displaylang=en
// Windows XP Sp2 will also need the Microsoft Core XML Services (MSXML) 6.0:
// 		http://www.microsoft.com/downloads/details.aspx?familyid=993C0BCF-3BCF-4009-BE21-27E85E1857B1&displaylang=en
//
// Note: Convert.FromEmf and Convert.ToEmf will only work on Windows and require GDI+.
//
// Please contact us if you have any questions.	
//---------------------------------------------------------------------------------------


NSArray *testfiles;

@interface Testfile : NSObject {
    NSString *inputFile;
    NSString *outputFile;
	bool requiresWindowsPlatform;
}
- (instancetype)init: (NSString*) input output: (NSString*) output win: (bool) win NS_DESIGNATED_INITIALIZER;
@property (NS_NONATOMIC_IOSONLY, readonly, copy) NSString *GetInputFile;
@property (NS_NONATOMIC_IOSONLY, readonly, copy) NSString *GetOutputFile;
@property (NS_NONATOMIC_IOSONLY, readonly) bool RequiresWindowsPlatform;
@end

@implementation Testfile

- (Testfile*)init: (NSString*) input output: (NSString*) output win: (bool) win {
    self = [super init];
    inputFile = input;
    outputFile = output;
    requiresWindowsPlatform = win;
    return self;
}

- (NSString*)GetInputFile {
    return inputFile;
}

- (NSString*)GetOutputFile {
    return outputFile;
}

- (bool)RequiresWindowsPlatform {
    return requiresWindowsPlatform;
}

@end


// convert to/from PDF, XPS, EMF, SVG
int ConvertSpecificFormats() {
    
    int ret = 0;
        
    // Start with a PDFDoc to collect the converted documents
    PTPDFDoc *pdfdoc = [[PTPDFDoc alloc] init];
    NSString *outputFile;
            
    NSString *s1 = @"../../TestFiles/simple-xps.xps";
    NSString *s2 = @"../../TestFiles/simple-emf.emf";
            
    NSLog(@"Converting from XPS %@\n", s1);
    [PTConvert FromXpsWithFilename: pdfdoc in_filename: s1];
    outputFile = @"../../TestFiles/Output/xps2pdf v2.pdf";
    [pdfdoc SaveToFile: outputFile  flags: e_ptremove_unused];
    NSLog(@"Saved %@\n", outputFile);
            
    // Convert the two page PDF document to SVG
    NSLog(@"Converting pdfdoc to SVG\n");
    outputFile = @"../../TestFiles/Output/pdf2svg v2.svg";
    [PTConvert ToSvgWithPDFDoc: pdfdoc in_filename: outputFile in_options: [[PTSVGOutputOptions alloc] init]];
    NSLog(@"Saved %@\n", outputFile);
           
    // Convert the PDF document to XPS
    NSLog(@"Converting pdfdoc to XPS\n");
    outputFile = @"../../TestFiles/Output/pdf2xps v2.xps";
    [PTConvert ToXpsWithPDFDoc: pdfdoc in_filename: outputFile options: [[PTXPSOutputOptions alloc] init]];
    NSLog(@"Saved %@\n", outputFile);
            
    // Convert the PNG image to XPS
    NSLog(@"Converting PNG to XPS\n");
    outputFile = @"../../TestFiles/Output/butterfly.xps";
    [PTConvert ToXpsWithFilename: @"../../TestFiles/butterfly.png" in_outputFilename: outputFile options: [[PTXPSOutputOptions alloc] init]];
    NSLog(@"Saved %@\n", outputFile);
            
    // Convert PDF document to XPS
    NSLog(@"Converting PDF to XPS\n");
    outputFile = @"../../TestFiles/Output/newsletter.xps";
    [PTConvert ToXpsWithFilename: @"../../TestFiles/newsletter.pdf" in_outputFilename: outputFile options: [[PTXPSOutputOptions alloc] init]];
    NSLog(@"Saved %@", outputFile);
    
    // Convert PDF document to HTML
    NSLog(@"Converting PDF to HTML\n");
    outputFile = @"../../TestFiles/Output/newsletter";
    [PTConvert ToHtmlWithFilename: @"../../TestFiles/newsletter.pdf" out_path: outputFile options: [[PTHTMLOutputOptions alloc] init]];
    NSLog(@"Saved %@", outputFile);
    
    // Convert PDF document to EPUB
    NSLog(@"Converting PDF to EPUB\n");
    outputFile = @"../../TestFiles/Output/newsletter.epub";
    [PTConvert ToEpubWithFilename: @"../../TestFiles/newsletter.pdf" out_path: outputFile html_options: [[PTHTMLOutputOptions alloc] init] epub_options: [[PTEPUBOutputOptions alloc] init]];
    NSLog(@"Saved %@", outputFile);
        
    return ret;
}

// convert from a file to PDF automatically
int ConvertToPdfFromFile() {
	
	NSString *inputPath = @"../../TestFiles/";
	NSString *outputPath = @"../../TestFiles/Output/";
	
	int ret = 0;
	
    unsigned int count = testfiles.count;//sizeof (testfiles) / sizeof (Testfile);
       
    unsigned int i = 0;
    for (; i < count; i++)
    {
        Testfile *file = testfiles[i];

        if ([file RequiresWindowsPlatform])
        {
            continue;
        }
		
        PTPDFDoc *pdfdoc = [[PTPDFDoc alloc] init];
        NSString *inputFile = [inputPath stringByAppendingString: [file GetInputFile]];
        NSString *outputFile = [outputPath stringByAppendingString: [file GetOutputFile]];

        [PTConvert ToPdf: pdfdoc in_filename: inputFile];
        [pdfdoc SaveToFile: outputFile flags: e_ptlinearized];
        NSLog(@"Converted file: %@\n            to: %@\n", inputFile, outputFile);
    }
    return ret;
}

int main(int argc, char *argv[])
{
    @autoreleasepool {

    testfiles = @[[[Testfile alloc] init: @"simple-word_2007.docx" output: @"docx2pdf.pdf" win: true],
                 [[Testfile alloc] init: @"simple-powerpoint_2007.pptx" output: @"pptx2pdf.pdf" win: true],
                 [[Testfile alloc] init: @"simple-excel_2007.xlsx" output: @"xlsx2pdf.pdf" win: true],
                 [[Testfile alloc] init: @"simple-publisher.pub" output: @"pub2pdf.pdf" win: true],
                 // [[Testfile alloc] init: @"simple-visio.vsd" output: @"vsd2pdf.pdf" win: true],	// requires Microsoft Office Visio
                 [[Testfile alloc] init: @"simple-text.txt" output: @"txt2pdf.pdf" win: true],
                 [[Testfile alloc] init: @"simple-rtf.rtf" output: @"rtf2pdf.pdf" win: true],
                 [[Testfile alloc] init: @"butterfly.png" output: @"png2pdf.pdf" win: false],
                 [[Testfile alloc] init: @"simple-emf.emf" output: @"emf2pdf.pdf" win: true],
                 [[Testfile alloc] init: @"simple-xps.xps" output: @"xps2pdf.pdf" win: false],
                 // [[Testfile alloc] init: @"simple-webpage.mht" output: @"mht2pdf.pdf" win: true],
                 [[Testfile alloc] init: @"simple-webpage.html" output: @"html2pdf.pdf" win: true]];

    // The first step in every application using PDFNet is to initialize the
    // library. The library is usually initialized only once, but calling 
    // Initialize() multiple times is also fine.
    int err = 0;

    [PTPDFNet Initialize: 0];
        
    // Demonstrate Convert::ToPdf and Convert::Printer
    err = ConvertToPdfFromFile();
    if (err)
    {
        NSLog(@"ConvertFile failed\n");
    }
    else
    {
        NSLog(@"ConvertFile succeeded\n");
    }

    // Demonstrate Convert::[FromEmf, FromXps, ToEmf, ToSVG, ToXPS]
    err = ConvertSpecificFormats();
    if (err)
    {
        NSLog(@"ConvertSpecificFormats failed\n");
    }
    else
    {
        NSLog(@"ConvertSpecificFormats succeeded\n");
    }

    NSLog(@"Done.\n");
    return err;
    }
	
}
