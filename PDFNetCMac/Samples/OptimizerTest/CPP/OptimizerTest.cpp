//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#include <PDF/PDFNet.h>
#include <PDF/Optimizer.h>
#include <iostream>
#include <PDF/Flattener.h>

using namespace std;
using namespace pdftron;
using namespace Common;
using namespace SDF;
using namespace PDF;

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
//
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
	int ret = 0;
	std::string input_path = "../../TestFiles/";
	std::string output_path = "../../TestFiles/Output/";
	std::string input_filename = "newsletter";	

	// The first step in every application using PDFNet is to initialize the 
	// library and set the path to common PDF resources. The library is usually 
	// initialized only once, but calling Initialize() multiple times is also fine.
	PDFNet::Initialize();

	//--------------------------------------------------------------------------------
	// Example 1) Simple optimization of a pdf with default settings. 
	//
	try
	{
		pdftron::PDF::PDFDoc doc(input_path + input_filename + ".pdf");
		doc.InitSecurityHandler();

		Optimizer::Optimize(doc);

		doc.Save(output_path + input_filename + "_opt1.pdf", SDFDoc::e_linearized, NULL);
	}
	catch (Common::Exception& e)
	{
		std::cout << e << endl;
		ret = 1;		
	}
	catch (...)
	{
		cout << "Unknown Exception" << endl;
		ret = 1;
	}

	//--------------------------------------------------------------------------------
	// Example 2) Reduce image quality and use jpeg compression for
	// non monochrome images.
	try
	{
		PDFDoc doc(input_path + input_filename + ".pdf");
		doc.InitSecurityHandler();

		Optimizer::ImageSettings image_settings;

		// low quality jpeg compression
		image_settings.SetCompressionMode(Optimizer::ImageSettings::e_jpeg);
		image_settings.SetQuality(1);

		// Set the output dpi to be standard screen resolution
		image_settings.SetImageDPI(144,96);

		// this option will recompress images not compressed with
		// jpeg compression and use the result if the new image
		// is smaller.
		image_settings.ForceRecompression(true);

		// this option is not commonly used since it can 
		// potentially lead to larger files.  It should be enabled
		// only if the output compression specified should be applied
		// to every image of a given type regardless of the output image size
		//image_settings.ForceChanges(true);


		Optimizer::OptimizerSettings opt_settings;
		opt_settings.SetColorImageSettings(image_settings);
		opt_settings.SetGrayscaleImageSettings(image_settings);

		// use the same settings for both color and grayscale images
		Optimizer::Optimize(doc, opt_settings);

		doc.Save(output_path + input_filename + "_opt2.pdf", SDFDoc::e_linearized, NULL);
	}
	catch (Common::Exception& e)
	{
		std::cout << e << endl;
		ret = 1;		
	}
	catch (...)
	{
		cout << "Unknown Exception" << endl;
		ret = 1;
	}

	//--------------------------------------------------------------------------------
	// Example 3) Use monochrome image settings and default settings
	// for color and grayscale images. 
	try
	{
		PDFDoc doc(input_path + input_filename + ".pdf");
		doc.InitSecurityHandler();

		Optimizer::MonoImageSettings mono_image_settings;

		mono_image_settings.SetCompressionMode(Optimizer::MonoImageSettings::e_jbig2);
		mono_image_settings.ForceRecompression(true);

		Optimizer::OptimizerSettings opt_settings;
		opt_settings.SetMonoImageSettings(mono_image_settings);

		Optimizer::Optimize(doc, opt_settings);

		doc.Save(output_path + input_filename + "_opt3.pdf", SDFDoc::e_linearized, NULL);
	}
	catch (Common::Exception& e)
	{
		std::cout << e << endl;
		ret = 1;		
	}
	catch (...)
	{
		cout << "Unknown Exception" << endl;
		ret = 1;
	}

	// ----------------------------------------------------------------------
	// Example 4) Use Flattener to simplify content in this document
	// using default settings
	try
	{
		PDFDoc doc(input_path + "TigerText.pdf");
		doc.InitSecurityHandler();

		Flattener fl;

		// The following lines can increase the resolution of background
		// images.
		//fl.SetDPI(300);
		//fl.SetMaximumImagePixels(5000000);

		// This line can be used to output Flate compressed background
		// images rather than DCTDecode compressed images which is the default
		//fl.SetPreferJPG(false);

		// In order to adjust thresholds for when text is Flattened
		// the following function can be used.
		//fl.SetThreshold(Flattener::e_keep_most);

		// We use e_fast option here since it is usually preferable
		// to avoid Flattening simple pages in terms of size and 
		// rendering speed. If the desire is to simplify the 
		// document for processing such that it contains only text and
		// a background image e_simple should be used instead.
		fl.Process(doc,Flattener::e_fast);

		doc.Save(output_path + "TigerText_flatten.pdf", SDFDoc::e_linearized, NULL);

	}
	catch (Common::Exception& e)
	{
		std::cout << e << endl;
		ret = 1;		
	}
	catch (...)
	{
		cout << "Unknown Exception" << endl;
		ret = 1;
	}


	PDFNet::Terminate();
	return ret;
}
