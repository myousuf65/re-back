//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

// This sample illustrates the basic text extraction capabilities of PDFNet.

// A utility method used to dump all text content in the console window.
void DumpAllText(PTElementReader *reader) 
{
	PTElement *element; 
	while ((element = [reader Next]) != NULL)
	{
		switch ([element GetType]) 
		{
		case e_pttext_begin: 
			NSLog(@"--> Text Block Begin");
			break;
		case e_pttext_end:
			NSLog(@"--> Text Block End");
			break;
		case e_pttext:
			{
				PTPDFRect * bbox = [element GetBBox];
				NSLog(@"--> BBox: %f, %f, %f, %f", [bbox GetX1], [bbox GetY1], [bbox GetX2], [bbox GetY2]);

				NSLog(@"%@", [element GetTextString]);
			}
			break;
		case e_pttext_new_line:
			NSLog(@"--> New Line");
			break;
		case e_ptform:				// Process form XObjects
			[reader FormBegin]; 
            DumpAllText(reader);
			[reader End]; 
			break;
		default:
			break;
		}
	}
}

// A helper method for ReadTextFromRect
void RectTextSearch(PTElementReader *reader, PTPDFRect * pos, NSString **srch_str)
{			
	PTElement *element; 
	while ((element = [reader Next]) != NULL)
	{
		switch ([element GetType]) 
		{
		case e_pttext:
			{
				PTPDFRect * bbox = [element GetBBox];
				if([bbox IntersectRect: bbox rect2: pos]) 
				{
					NSString *arr = [element GetTextString];
					*srch_str = [*srch_str stringByAppendingString: arr];
					*srch_str = [*srch_str stringByAppendingString: @"\n"]; // add a new line?
				}
				break;
			}
		case e_pttext_new_line:
			{
				break;
			}
		case e_ptform: // Process form XObjects
			{
				[reader FormBegin]; 
				RectTextSearch(reader, pos, &(*srch_str));
				[reader End]; 
				break; 
			}
		default:
			break;
		}
	}
}

// A utility method used to extract all text content from
// a given selection rectangle. The rectangle coordinates are
// expressed in PDF user/page coordinate system.
NSString* ReadTextFromRect(PTPage *page, PTPDFRect * pos, PTElementReader *reader)
{
	NSString *srch_str = @"";
	[reader Begin: page];
	RectTextSearch(reader, pos, &srch_str);
	[reader End];
	return srch_str;
}


void PrintStyle(PTTextExtractorStyle *s)
{
	NSMutableArray *rgb = [s GetColor];
	NSString * name = [s GetFontName ];
	const char* font_family = [name UTF8String];
	double font_size = [s GetFontSize];
	const char * san_serif = ([s IsSerif]) ? " sans-serif;" : "";
	int R = [rgb[0] intValue];
	int G = [rgb[1] intValue];
	int B = [rgb[2] intValue];
	printf(" style=\"font-family:%s; font-size:%g;%s color:#%02X%02X%02X;\"", font_family, font_size, san_serif, [rgb[0] intValue], [rgb[1] intValue], [rgb[2] intValue]);
}

int main(int argc, char *argv[])
{
    @autoreleasepool {
		int ret = 0;
		[PTPDFNet Initialize: 0];

		bool example1_basic     = false;
		bool example2_xml       = false;
		bool example3_wordlist  = false;
		bool example4_advanced  = true;
		bool example5_low_level = false;

		// Sample code showing how to use high-level text extraction APIs.
		@try
		{
			PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
			[doc InitSecurityHandler];

			PTPage *page = [doc GetPage: 1];
			if (!page){
				NSLog(@"Page not found.");
				return 1;
			}

			PTTextExtractor *txt = [[PTTextExtractor alloc] init];
			[txt Begin: page clip_ptr: 0 flags: 0]; // Read the page.
			// Other options you may want to consider...
			// txt.Begin(*itr, 0, TextExtractor::e_no_dup_remove);
			// txt.Begin(*itr, 0, TextExtractor::e_remove_hidden_text);


			// Example 1. Get all text on the page in a single string.
			// Words will be separated with space or new line characters.
			if (example1_basic) 
			{
				// Get the word count.
				NSLog(@"Word Count: %d", [txt GetWordCount]);

				NSString *text = [txt GetAsText: YES];
				NSLog(@"\n\n- GetAsText --------------------------\n%@", text);
				NSLog(@"-----------------------------------------------------------");
			}

			// Example 2. Get XML logical structure for the page.
			if (example2_xml) 
			{
				NSString *text = [txt GetAsXML: e_ptwords_as_elements | e_ptoutput_bbox | e_ptoutput_style_info];
				NSLog(@"\n\n- GetAsXML  --------------------------\n %@", text);
				NSLog(@"-----------------------------------------------------------");
			}

			// Example 3. Extract words one by one.
			if (example3_wordlist) 
			{
				PTTextExtractorLine *line = [txt GetFirstLine];
				PTWord *word;
				for (; [line IsValid]; line=[line GetNextLine])	{
					for (word=[line GetFirstWord]; [word IsValid]; word=[word GetNextWord]) {
						NSLog(@"%@", [word GetString]);
					}
				}
				NSLog(@"-----------------------------------------------------------");
			}

			// Example 4. A more advanced text extraction example. 
			// The output is XML structure containing paragraphs, lines, words, 
			// as well as style and positioning information.
			if (example4_advanced) 
			{
				PTPDFRect * b, *q;
				int cur_flow_id=-1, cur_para_id=-1;

				NSString *uni_str;
				PTTextExtractorLine *line;
				PTWord *word;
				PTTextExtractorStyle *s, *line_style;

				printf("<PDFText>\n");
				// For each line on the page...
				for (line=[txt GetFirstLine]; [line IsValid]; line=[line GetNextLine])
				{
					if ( [line GetNumWords] == 0 )
					{
						continue;
					}
					
					if (cur_flow_id != [line GetFlowID]) {
						if (cur_flow_id != -1) {
							if (cur_para_id != -1) {
								cur_para_id = -1;
								printf("</Para>\n");
							}
							printf("</Flow>\n");
						}
						cur_flow_id = [line GetFlowID];
						printf("<Flow id=\"%d\">\n", cur_flow_id);
					}

					if (cur_para_id != [line GetParagraphID]) {
						if (cur_para_id != -1)
							printf("</Para>\n");
						cur_para_id = [line GetParagraphID];
						printf("<Para id=\"%d\">\n", cur_para_id);
					}	

					b = [line GetBBox];
					line_style = [line GetStyle];
					printf("<Line box=\"%.2f, %.2f, %.2f, %.2f\"", [b GetX1], [b GetY1], [b GetX2], [b GetY2]);
					PrintStyle(line_style);
					printf(" cur_num=\"%d\"", [line GetCurrentNum]);
					printf(">\n");

					// For each word in the line...
					for (word=[line GetFirstWord]; [word IsValid]; word=[word GetNextWord])
					{
						// Output the bounding box for the word.
						q = [word GetBBox];
						printf("<Word box=\"%.2f, %.2f, %.2f, %.2f\"", [q GetX1], [q GetY1], [q GetX2], [q GetY2]);
						printf(" cur_num=\"%d\"", [word GetCurrentNum]);
						
						int sz = [word GetStringLen];
						if (sz == 0) continue;

						// If the word style is different from the parent style, output the new style.
					
						s = [word GetStyle];	
						if(![s isEqualTo:line_style]){
							PrintStyle(s);
						}

						uni_str = [word GetString];
						printf(">%s", [uni_str UTF8String]);
						printf("</Word>\n");
					}
					printf("</Line>\n");
				}

				if (cur_flow_id != -1) {
					if (cur_para_id != -1) {
						cur_para_id = -1;
						printf("</Para>\n");
					}
					printf("</Flow>\n");
				}
			printf("</PDFText>\n");
			}
		}
		@catch(NSException *e)
		{
			NSLog(@"%@", e.reason);
			ret = 1;
		}

		if(example5_low_level)
		{
            @try
            {
                PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/newsletter.pdf"];
                [doc InitSecurityHandler];

                // Example 1. Extract all text content from the document

                PTElementReader *reader = [[PTElementReader alloc] init];
                //  Read every page
                PTPageIterator *itr;
                for (itr=[doc GetPageIterator: 1]; [itr HasNext]; [itr Next]) 
                {				
                    [reader Begin: [itr Current]];
                    DumpAllText(reader);
                    [reader End];
                }

                // Example 2. Extract text content based on the 
                // selection rectangle.
                NSLog(@"\n----------------------------------------------------");
                NSLog(@"\nExtract text based on the selection rectangle.");
                NSLog(@"\n----------------------------------------------------\n");

                PTPage *first_page = [[doc GetPageIterator: 1] Current];
                PTPDFRect * rect1 = [[PTPDFRect alloc] initWithX1: 27 y1: 392 x2: 563 y2: 534];
                NSString *s1 = ReadTextFromRect(first_page, rect1, reader);
                NSLog(@"\nField 1: %@", s1);

                PTPDFRect * rect2 = [[PTPDFRect alloc] initWithX1: 28 y1: 551 x2: 106 y2: 623];
                s1 = ReadTextFromRect(first_page, rect2, reader);
                NSLog(@"\nField 2: %@", s1);

                PTPDFRect * rect3 = [[PTPDFRect alloc] initWithX1: 208 y1: 550 x2: 387 y2: 621];
                s1 = ReadTextFromRect(first_page, rect3, reader);
                NSLog(@"\nField 3: %@", s1);

                // ... 
                NSLog(@"Done.");
            }
            @catch(NSException *e)
            {
                NSLog(@"%@", e.reason);
                ret = 1;
            }
        }
        
        return ret;
    }
}

