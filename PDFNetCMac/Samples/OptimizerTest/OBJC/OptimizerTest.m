//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//---------------------------------------------------------------------------------------
// The following sample illustrates how to reduce PDF file size using 'pdftron.PDF.Optimizer'.
// The sample also shows how to simplify and optimize PDF documents for viewing on mobile devices 
// and on the Web using 'pdftron.PDF.Flattener'.
//
// @note Both 'Optimizer' and 'Flattener' are separately licensable add-on options to the core PDFNet license.
//
// ----
//
// 'pdftron.PDF.Optimizer' can be used to optimize PDF documents by reducing the file size, removing 
// redundant information, and compressing data streams using the latest in image compression technology. 
//
// PDF Optimizer can compress and shrink PDF file size with the following operations:
// - Remove duplicated fonts, images, ICC profiles, and any other data stream. 
// - Optionally convert high-quality or print-ready PDF files to small, efficient and web-ready PDF. 
// - Optionally down-sample large images to a given resolution. 
// - Optionally compress or recompress PDF images using JBIG2 and JPEG2000 compression formats. 
// - Compress uncompressed streams and remove unused PDF objects.
// ----
//
// 'pdftron.PDF.Flattener' can be used to speed-up PDF rendering on mobile devices and on the Web by 
// simplifying page content (e.g. flattening complex graphics into images) while maintaining vector text 
// whenever possible.
//
// Flattener can also be used to simplify process of writing custom converters from PDF to other formats. 
// In this case, Flattener can be used as first step in the conversion pipeline to reduce any PDF to a 
// very simple representation (e.g. vector text on top of a background image). 
//---------------------------------------------------------------------------------------
int main(int argc, char * argv[])
{
    @autoreleasepool {

		int ret = 0;

		// The first step in every application using PDFNet is to initialize the 
		// library and set the path to common PDF resources. The library is usually 
		// initialized only once, but calling Initialize() multiple times is also fine.
		[PTPDFNet Initialize: 0];

		//--------------------------------------------------------------------------------
		// Example 1) Simple optimization of a pdf with default settings. 
		//
		@try
		{
			PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
			[doc InitSecurityHandler];

			[PTOptimizer Optimize: doc settings: [[PTOptimizerSettings alloc] init]];

			[doc SaveToFile: @"../../TestFiles/Output/newsletter_opt1.pdf" flags: e_ptlinearized];
		}
		@catch (NSException *e)
		{
			NSLog(@"%@", e.reason);
        ret = 1;
		}

		//--------------------------------------------------------------------------------
		// Example 2) Reduce image quality and use jpeg compression for
		// non monochrome images.
		@try
		{
			PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
			[doc InitSecurityHandler];

			PTImageSettings *image_settings = [[PTImageSettings alloc] init];

			// low quality jpeg compression
			[image_settings SetCompressionMode: e_ptjpeg];
			[image_settings SetQuality: 1];

			// Set the output dpi to be standard screen resolution
			[image_settings SetImageDPI: 144 resampling: 96];

			// this option will recompress images not compressed with
			// jpeg compression and use the result if the new image
			// is smaller.
			[image_settings ForceRecompression: YES];

			// this option is not commonly used since it can 
			// potentially lead to larger files.  It should be enabled
			// only if the output compression specified should be applied
			// to every image of a given type regardless of the output image size
			//image_settings.ForceChanges(true);


			PTOptimizerSettings *opt_settings = [[PTOptimizerSettings alloc] init];
			[opt_settings SetColorImageSettings: image_settings];
			[opt_settings SetGrayscaleImageSettings: image_settings];

			// use the same settings for both color and grayscale images
			[PTOptimizer Optimize: doc settings: opt_settings];

			[doc SaveToFile: @"../../TestFiles/Output/newsletter_opt2.pdf" flags: e_ptlinearized];
		}
		@catch (NSException *e)
		{
			NSLog(@"%@", e.reason);	
        ret = 1;
		}
    

		//--------------------------------------------------------------------------------
		// Example 3) Use monochrome image settings and default settings
		// for color and grayscale images. 
		@try
		{
			PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
			[doc InitSecurityHandler];

			PTMonoImageSettings *mono_image_settings = [[PTMonoImageSettings alloc] init];

			[mono_image_settings SetCompressionMode: e_ptmn_jbig2];
			[mono_image_settings ForceRecompression: true];

			PTOptimizerSettings *opt_settings = [[PTOptimizerSettings alloc] init];
			[opt_settings SetMonoImageSettings: mono_image_settings];

			[PTOptimizer Optimize: doc settings: opt_settings];

			[doc SaveToFile: @"../../TestFiles/Output/newsletter_opt3.pdf" flags: e_ptlinearized];
		}
		@catch (NSException *e)
		{
			NSLog(@"%@", e.reason);	
        ret = 1;		
		}
		
		// ----------------------------------------------------------------------
		// Example 4) Use Flattener to simplify content in this document
		// using default settings
		@try
		{
			PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/TigerText.pdf"];
			[doc InitSecurityHandler];
			
			PTFlattener *fl = [[PTFlattener alloc] init];
			// The following lines can increase the resolution of background
			// images.
			//[fl SetDPI: 300];
			//[fl SetMaximumImagePixels: 5000000];

			// This line can be used to output Flate compressed background
			// images rather than DCTDecode compressed images which is the default
			//[fl SetPreferJPG: false];

			// In order to adjust thresholds for when text is Flattened
			// the following function can be used.
			//[fl SetThreshold: e_threshold_keep_most];

			// We use e_ptfast option here since it is usually preferable
			// to avoid Flattening simple pages in terms of size and 
			// rendering speed. If the desire is to simplify the 
			// document for processing such that it contains only text and
			// a background image e_simple should be used instead.
			[fl Process: doc mode: e_ptfast];
			[doc SaveToFile: @"../../TestFiles/Output/TigerText_flatten.pdf" flags: e_ptlinearized];
		}
		@catch (NSException *e)
		{
			NSLog(@"%@", e.reason);
			ret = 1;
		}
		
		//NSLog(@"Done");
		return ret;
    }
}
