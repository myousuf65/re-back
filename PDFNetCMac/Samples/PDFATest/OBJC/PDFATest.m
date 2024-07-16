//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

void PrintResults(PTPDFACompliance *pdf_a, NSString *filename) {
    unsigned long err_cnt = [pdf_a GetErrorCount];
	if (err_cnt == 0) 
	{
		NSLog(@"%@: OK.", filename);
	}
	else 
	{
		NSLog(@"%@ is NOT a valid PDFA.", filename);
        int i = 0;
		for (; i<err_cnt; ++i) 
		{
			int c = [pdf_a GetError: i];
			NSLog(@" - e_PDFA %d: %@.", c, [PTPDFACompliance GetPDFAErrorMessage: c]);
			if (true) 
			{
				unsigned long num_refs = [pdf_a GetRefObjCount: c];
				if (num_refs > 0)  
				{
                    NSString *str = @"   Objects: ";
                    int j=0;
					for (; j<num_refs; ++j) 
					{
						str = [str stringByAppendingFormat: @"%lu", [pdf_a GetRefObj: c err_idx: j]];
						if (j<num_refs-1) 
							str = [str stringByAppendingString: @", "];
					}
                    NSLog(@"%@", str);
				}
			}
		}
        NSLog(@"\n");
	}
}

//---------------------------------------------------------------------------------------
// The following sample illustrates how to parse and check if a PDF document meets the
//	PDFA standard, using the PTPDFACompliance class object. 
//---------------------------------------------------------------------------------------
int main(int argc, char *argv[])
{
    @autoreleasepool {	
       
        int ret = 0;
        
        [PTPDFNet Initialize: 0];
        [PTPDFNet SetColorManagement: e_ptlcms];  // Enable color management (required for PDFA validation).

        //-----------------------------------------------------------
        // Example 1: PDF/A Validation
        //-----------------------------------------------------------
        @try
        {
            NSString *filename = @"newsletter.pdf";
			// The max_ref_objs parameter to the PDFACompliance constructor controls the maximum number 
			// of object numbers that are collected for particular error codes. The default value is 10 
			// in order to prevent spam. If you need all the object numbers, pass 0 for max_ref_objs.
            PTPDFACompliance *pdf_a = [[PTPDFACompliance alloc] initWithConvert: NO file_path: @"../../TestFiles/newsletter.pdf" password: @"" conf: e_ptLevel1B exceptions: 0 num_exceptions: 10 max_ref_objs: 10 first_stop: NO];
            PrintResults(pdf_a, filename);
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        //-----------------------------------------------------------
        // Example 2: PDF/A Conversion
        //-----------------------------------------------------------
        @try
        {
            NSString *filename = @"fish.pdf";
            PTPDFACompliance *pdf_a = [[PTPDFACompliance alloc] initWithConvert: YES file_path: @"../../TestFiles/fish.pdf" password: @"" conf: e_ptLevel1B exceptions: 0 num_exceptions: 10 max_ref_objs: 10 first_stop: NO];
            filename = @"../../TestFiles/Output/pdfa.pdf";
            [pdf_a SaveAsFile: filename linearized: YES];

            // Re-validate the document after the conversion...
            PTPDFACompliance *comp =[[PTPDFACompliance alloc] initWithConvert: NO file_path: filename password: @"" conf: e_ptLevel1B exceptions: 0 num_exceptions: 10 max_ref_objs: 10 first_stop: NO];
            filename = @"pdfa.pdf";
            PrintResults(comp, filename);
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        NSLog(@"PDFACompliance test completed.");
        return ret;
    }
}
