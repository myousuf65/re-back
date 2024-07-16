//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

char m_buf[4000];

void ProcessElements(PTElementReader *reader);

void ProcessPath(PTElementReader *reader, PTElement *path)
{
	if ([path IsClippingPath])
	{
		NSLog(@"This is a clipping path");
	}

	PTPathData* pathData = [path GetPathData];
	NSMutableArray* data = [pathData GetPoints];
	NSData* opr = [pathData GetOperators];

    NSUInteger opr_index = 0;
    NSUInteger opr_end = opr.length;
    NSUInteger data_index = 0;
    NSUInteger data_end = data.count;
    
    double x1, y1, x2, y2, x3, y3;
    NSString *str = @"";
    
	// Use path.GetCTM() if you are interested in CTM (current transformation matrix).

    unsigned char* opr_data = (unsigned char*)opr.bytes;
	str = [str stringByAppendingFormat: @" Path Data Points := \""];
	for (; opr_index<opr_end; opr_index = opr_index + 1)
	{
		switch(opr_data[opr_index])
		{
		case e_ptmoveto:
			x1 = [data[data_index] doubleValue]; ++data_index;
			y1 = [data[data_index] doubleValue]; ++data_index;
			sprintf(m_buf, "M%.5g %.5g", x1, y1);
			str = [str stringByAppendingFormat: @"%s", m_buf];
			break;
		case e_ptlineto:
			x1 = [data[data_index] doubleValue]; ++data_index;
			y1 = [data[data_index] doubleValue]; ++data_index;
			sprintf(m_buf, " L%.5g %.5g", x1, y1);
			str = [str stringByAppendingFormat: @"%s", m_buf];
			break;
		case e_ptcubicto:
			x1 = [data[data_index] doubleValue]; ++data_index;
			y1 = [data[data_index] doubleValue]; ++data_index;
			x2 = [data[data_index] doubleValue]; ++data_index;
			y2 = [data[data_index] doubleValue]; ++data_index;
			x3 = [data[data_index] doubleValue]; ++data_index;
			y3 = [data[data_index] doubleValue]; ++data_index;
			sprintf(m_buf, " C%.5g %.5g %.5g %.5g %.5g %.5g", x1, y1, x2, y2, x3, y3);
			str = [str stringByAppendingFormat: @"%s", m_buf];
			break;
		case e_ptrect:
			{
				x1 = [data[data_index] doubleValue]; ++data_index;
				y1 = [data[data_index] doubleValue]; ++data_index;
				double w = [data[data_index] doubleValue]; ++data_index;
				double h = [data[data_index] doubleValue]; ++data_index;
				x2 = x1 + w;
				y2 = y1;
				x3 = x2;
				y3 = y1 + h;
				double x4 = x1; 
				double y4 = y3;
				sprintf(m_buf, "M%.5g %.5g L%.5g %.5g L%.5g %.5g L%.5g %.5g Z", 
					x1, y1, x2, y2, x3, y3, x4, y4);
				str = [str stringByAppendingFormat: @"%s", m_buf];
			}
			break;
		case e_ptclosepath:
			str = [str stringByAppendingString: @" Close Path"];
			break;
		default: 
			assert(false);
			break;
		}	
	}

	str = [str stringByAppendingString: @"\" "];

	PTGState *gs = [path GetGState];

	// Set Path State 0 (stroke, fill, fill-rule) -----------------------------------
	if ([path IsStroked]) 
	{
		str = [str stringByAppendingString: @"Stroke path\n"]; 

		if ([[gs GetStrokeColorSpace] GetType] == e_ptpattern)
		{
			str = [str stringByAppendingString: @"Path has associated pattern"]; 
		}
		else
		{
			// Get stroke color (you can use PDFNet color conversion facilities)
			// ColorPt rgb;
			// gs.GetStrokeColorSpace().Convert2RGB(gs.GetStrokeColor(), rgb);
		}
	}
	else 
	{
		// Do not stroke path
	}

	if ([path IsFilled])
	{
		str = [str stringByAppendingString: @"Fill path"]; 

		if ([[gs GetFillColorSpace] GetType] == e_ptpattern)
		{		
			str = [str stringByAppendingString: @"Path has associated pattern"]; 
		}
		else
		{
			// PTColorPt *rgb = [[[PTColorPt alloc] init] autorelease];
			// [[gs GetFillColorSpace] Convert2RGB: [gs GetFillColorWithColorPt: rgb]];
		}        
	}
	else 
	{
		// Do not fill path
	}

	// Process any changes in graphics state  ---------------------------------

	PTGSChangesIterator *gs_itr = [reader GetChangesIterator];
	for (; [gs_itr HasNext]; [gs_itr Next]) 
	{
		switch([gs_itr Current])
		{
		case e_pttransform :
			// Get transform matrix for this element. Unlike path.GetCTM() 
			// that return full transformation matrix gs.GetTransform() return 
			// only the transformation matrix that was installed for this element.
			//
			// gs.GetTransform();
			break;
		case e_ptline_width :
			// gs.GetLineWidth();
			break;
		case e_ptline_cap :
			// gs.GetLineCap();
			break;
		case e_ptline_join :
			// gs.GetLineJoin();
			break;
		case e_ptflatness :
			break;
		case e_ptmiter_limit :
			// gs.GetMiterLimit();
			break;
		case e_ptdash_pattern :
			{
				// std::vector<double> dashes;
				// gs.GetDashes(dashes);
				// gs.GetPhase()
			}
			break;
		case e_ptfill_color:
			{
				if ( [[gs GetFillColorSpace] GetType] == e_ptpattern &&
					[[gs GetFillPattern] GetType] != e_ptshading )
				{	
					//process the pattern data
					[reader PatternBegin: YES reset_ctm_tfm: NO];
					ProcessElements(reader);
					[reader End];
				}
			}
			break;
		default:
			break;
		}
	}
	[reader ClearChangeList];
    NSLog(@"%@", str);
}

void ProcessText(PTElementReader* page_reader) 
{
	// Begin text element
	NSLog(@"Begin Text Block:");

	PTElement *element; 
	while ((element = [page_reader Next]) != NULL) 
	{
		switch ([element GetType])
		{
			case e_pttext_end:
				// Finish the text block
				//str = [str stringByAppendingString: @"End Text Block.\n"];
					NSLog(@"End Text Block.");
				return;

			case e_pttext_obj:
			{
				PTGState *gs = [element GetGState];

				PTColorSpace *cs_fill = [gs GetFillColorSpace];
				PTColorPt *fill = [gs GetFillColor];
				
				PTColorPt *outColor = [cs_fill Convert2RGB: fill];

				PTColorSpace *cs_stroke = [gs GetStrokeColorSpace];
				PTColorPt *stroke = [gs GetStrokeColor];

				PTFont *font = [gs GetFont];

				NSLog(@"Font Name: %@\n", [font GetName]);
				
				// font.IsFixedWidth();
				// font.IsSerif();
				// font.IsSymbolic();
				// font.IsItalic();
				// ... 

				// double font_size = gs.GetFontSize();
				// double word_spacing = gs.GetWordSpacing();
				// double char_spacing = gs.GetCharSpacing();
				// const UString* txt = element.GetTextString();

				if ( [font GetType] == e_ptType3 )
				{
					//type 3 font, process its data
					PTCharIterator *itr;
					for (itr = [element GetCharIterator]; [itr HasNext]; [itr Next]) 
					{
						[page_reader Type3FontBegin: [itr Current] resource_dict: 0];
						ProcessElements(page_reader);
						[page_reader End];
					}
				}

				else
				{	
					PTMatrix2D *text_mtx = [element GetTextMatrix];
					double x, y;
					unsigned int char_code;

					PTCharIterator *itr;
					NSString* str = @"";
					for (itr = [element GetCharIterator]; [itr HasNext]; [itr Next]) 
					{
						char_code = [[itr Current] getChar_code];
						if (char_code>=32 || char_code<=255) { // Print if in ASCII range...
							str = [str stringByAppendingFormat: @"%c", char_code];
						}

						x = [[itr Current] getX];		// character positioning information
						y = [[itr Current] getY];

						// Use element.GetCTM() if you are interested in the CTM 
						// (current transformation matrix).
						PTMatrix2D *ctm = [element GetCTM];

						// To get the exact character positioning information you need to 
						// concatenate current text matrix with CTM and then multiply 
						// relative positioning coordinates with the resulting matrix.
						PTMatrix2D *mtx = text_mtx;
						[mtx Concat: [ctm getM_a] b: [ctm getM_b] c: [ctm getM_c] d: [ctm getM_d] h: [ctm getM_h] v: [ctm getM_v]];
						[mtx Mult: [[PTPDFPoint alloc] initWithPx: x py: y]];

						// Get glyph path...
						//vector<UChar> oprs;
						//vector<double> glyph_data;
						//font.GetGlyphPath(char_code, oprs, glyph_data, false, 0);
					}
					NSLog(@"%@", str);
				}

				//str = [str stringByAppendingString: @"\n"];
			}
				break;
			default:
				break;
		}
	}
}

void ProcessImage(PTElement *image)  
{
	bool image_mask = [image IsImageMask];
	bool interpolate = [image IsImageInterpolate];
	int width = [image GetImageWidth];
	int height = [image GetImageHeight];
	int out_data_sz = width * height * 3;

	NSLog(@"Image:  width=\"%d\" height=\"%d\"", width, height);

	// Matrix2D& mtx = image->GetCTM(); // image matrix (page positioning info)

	// You can use GetImageData to read the raw (decoded) image data
	//image->GetBitsPerComponent();	
	//image->GetImageData();	// get raw image data
	// .... or use Image2RGB filter that converts every image to RGB format,
	// This should save you time since you don't need to deal with color conversions, 
	// image up-sampling, decoding etc.

	PTImage2RGB *img_conv = [[PTImage2RGB alloc] initWithImage_element: image];	// Extract and convert image to RGB 8-bpc format
	PTFilterReader *reader = [[PTFilterReader alloc] initWithFilter: img_conv];

	// A buffer used to keep image data.
	NSData *image_data_out = [reader Read: out_data_sz];
	// &image_data_out.front() contains RGB image data.

	// Note that you don't need to read a whole image at a time. Alternatively
	// you can read a chuck at a time by repeatedly calling reader.Read(buf, buf_sz) 
	// until the function returns 0. 
}

void ProcessElements(PTElementReader *reader) 
{
	PTElement *element;
	while ((element = [reader Next]) != NULL) 	// Read page contents
	{
		switch ([element GetType])
		{
		case e_ptpath:						// Process path data...
			{
				ProcessPath(reader, element);
			}
			break; 
		case e_pttext_begin: 				// Process text block...
			{
				ProcessText(reader);
			}
			break;
		case e_ptform:						// Process form XObjects
			{
				[reader FormBegin]; 
				ProcessElements(reader);
				[reader End];
			}
			break; 
		case e_ptimage:						// Process Images
			{
				ProcessImage(element);
			}	
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
        [PTPDFNet Initialize: 0];

        @try	// Extract text data from all pages in the document
        {
            NSLog(@"__________________________________________________");
            NSLog(@"Extract page element information from all ");
            NSLog(@"pages in the document.");

            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
            [doc InitSecurityHandler];

            int pgnum = [doc GetPageCount];
            PTPageIterator *page_begin = [doc GetPageIterator: 1];

            PTElementReader *page_reader = [[PTElementReader alloc] init];

            PTPageIterator *itr;
            for (itr = page_begin; [itr HasNext]; [itr Next])		//  Read every page
            {				
                NSLog(@"Page %d----------------------------------------", [[itr Current] GetIndex]);
                [page_reader Begin: [itr Current]];
                ProcessElements(page_reader);
                [page_reader End];
            }

            NSLog(@"Done.");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        return ret;
    }
	
}
