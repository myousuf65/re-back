//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//---------------------------------------------------------------------------------------
// PDFNet includes a full support for FDF (Forms Data Format) and capability to merge/extract 
// forms data (FDF) with/from PDF. This sample illustrates basic FDF merge/extract functionality 
// available in PDFNet.
//---------------------------------------------------------------------------------------
int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        // Example 1)
        // Iterate over all form fields in the document. Display all field names.
        @try  
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/form1.pdf"];
            [doc InitSecurityHandler];

        PTFieldIterator * itr;
            for(itr = [doc GetFieldIterator]; [itr HasNext]; [itr Next]) 
            {
                NSLog(@"Field name: %@", [[itr Current] GetName]);
                NSLog(@"Field partial name: %@", [[itr Current] GetPartialName]);

                //NSLog(@"Field type: ");
                int type = [[itr Current] GetType];
                switch(type)
                {
                case e_ptbutton: NSLog(@"Field type: Button"); NSLog(@"------------------------------");break;
                case e_pttext: NSLog(@"Field type: Text"); NSLog(@"------------------------------");break;
                case e_ptchoice: NSLog(@"Field type: Choice"); NSLog(@"------------------------------");break;
                case e_ptsignature: NSLog(@"Field type: Signature"); NSLog(@"------------------------------");break;
                default: NSLog(@"Field type: ------------------------------");
                        
                }

                //NSLog(@"------------------------------");
            }

            NSLog(@"Done.");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        // Example 2) Import XFDF into FDF, then merge data from FDF into PDF
        @try  
        {
            // XFDF to FDF
            // form fields
            NSLog(@"Import form field data from XFDF to FDF.");
            
            PTFDFDoc *fdf_doc1 = [PTFDFDoc CreateFromXFDF: @"../../TestFiles/form1_data.xfdf"];
            [fdf_doc1 SaveFDFDocToFile: @"../../TestFiles/Output/form1_data.fdf"];
            
            // annotations
            NSLog(@"Import annotations from XFDF to FDF.");
            
            PTFDFDoc *fdf_doc2 = [PTFDFDoc CreateFromXFDF: @"../../TestFiles/form1_annots.xfdf"];
            [fdf_doc2 SaveFDFDocToFile: @"../../TestFiles/Output/form1_annots.fdf"];
            
            // FDF to PDF
            // form fields
            NSLog(@"Merge form field data from FDF.");
            
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/form1.pdf"];
            [doc InitSecurityHandler];
            [doc FDFMerge: fdf_doc1];
            
            // To use PDFNet form field appearance generation instead of relying on 
            // Acrobat, uncomment the following two lines:
            // doc.RefreshFieldAppearances();
            // doc.GetAcroForm()->Put("NeedAppearances", new Bool(false));

            [doc SaveToFile: @"../../TestFiles/Output/form1_filled.pdf" flags: e_ptlinearized];
            
            // annotations
            NSLog(@"Merge annotations from FDF.");
            
            [doc FDFMerge: fdf_doc2];
            [doc SaveToFile: @"../../TestFiles/Output/form1_filled_with_annots.pdf" flags: e_ptlinearized];
            NSLog(@"Done.");	
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        // Example 3) Extract data from PDF to FDF, then export FDF as XFDF
        @try  
        {
            // PDF to FDF
            PTPDFDoc *in_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/form1_filled_with_annots.pdf"];
            [in_doc InitSecurityHandler];
            
            // form fields only
            NSLog(@"Extract form fields data to FDF.");
            
            PTFDFDoc *doc_fields = [in_doc FDFExtract: e_ptforms_only];
            [doc_fields SetPDFFileName: @"../form1_filled_with_annots.pdf"];
            [doc_fields SaveFDFDocToFile: @"../../TestFiles/Output/form1_filled_data.fdf"];
            
            // annotations only
            NSLog(@"Extract annotations to FDF.");
            
            PTFDFDoc *doc_annots = [in_doc FDFExtract: e_ptannots_only];
            [doc_annots SetPDFFileName: @"../form1_filled_with_annots.pdf"];
            [doc_annots SaveFDFDocToFile: @"../../TestFiles/Output/form1_filled_annot.fdf"];
            
            // both form fields and annotations
            NSLog(@"Extract both form fields and annotations to FDF.");
            
            PTFDFDoc *doc_both = [in_doc FDFExtract: e_ptboth];
            [doc_both SetPDFFileName: @"../form1_filled_with_annots.pdf"];
            [doc_both SaveFDFDocToFile: @"../../TestFiles/Output/form1_filled_both.fdf"];
            
            // FDF to XFDF
            // form fields
            NSLog(@"Export form field data from FDF to XFDF.");
            
            [doc_fields SaveAsXFDF: @"../../TestFiles/Output/form1_filled_data.xfdf"];
            
            // annotations
            NSLog(@"Export annotations from FDF to XFDF.");
            
            [doc_annots SaveAsXFDF: @"../../TestFiles/Output/form1_filled_annot.xfdf"];
            
            // both form fields and annotations
            NSLog(@"Export both form fields and annotations from FDF to XFDF.");
            
            [doc_both SaveAsXFDF: @"../../TestFiles/Output/form1_filled_both.xfdf"];
            
            NSLog(@"Done.");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        // Example 4) Merge/Extract XFDF into/from PDF
        @try  
        {
            // Merge XFDF from string
            PTPDFDoc *in_doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/numbered.pdf"];
            [in_doc InitSecurityHandler];
            
            NSLog(@"Merge XFDF string into PDF.");
            NSString *str = @"<?xml version=\"1.0\" encoding=\"UTF-8\" ?><xfdf xmlns=\"http://ns.adobe.com/xfdf\" xml:space=\"preserve\"><square subject=\"Rectangle\" page=\"0\" name=\"cf4d2e58-e9c5-2a58-5b4d-9b4b1a330e45\" title=\"user\" creationdate=\"D:20120827112326-07'00'\" date=\"D:20120827112326-07'00'\" rect=\"227.7814207650273,597.6174863387978,437.07103825136608,705.0491803278688\" color=\"#000000\" interior-color=\"#FFFF00\" flags=\"print\" width=\"1\"><popup flags=\"print,nozoom,norotate\" open=\"no\" page=\"0\" rect=\"0,792,0,792\" /></square></xfdf>";
            
            PTFDFDoc *fdoc = [PTFDFDoc CreateFromXFDF: str];
            [in_doc FDFMerge: fdoc];
            [in_doc SaveToFile: @"../../TestFiles/Output/numbered_modified.pdf" flags: e_ptlinearized];
            
            NSLog(@"Merge complete.");
            
            // Extract XFDF as string
            NSLog(@"Extract XFDF as a string.");
            PTFDFDoc *fdoc_new = [in_doc FDFExtract: e_ptboth];
            NSString *XFDF_str = [fdoc_new SaveAsXFDFToString];
            NSLog(@"Extracted XFDF: ");
            NSLog(@"%@", XFDF_str);
            NSLog(@"Extract complete.");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        // Example 5) Read FDF files directly
        @try  
        {
            PTFDFDoc *doc = [[PTFDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/form1_filled_data.fdf"];

        PTFDFFieldIterator *itr;
            for(itr = [doc GetFieldIterator]; [itr HasNext]; [itr Next]) 
            {
                NSLog(@"Field name: %@", [[itr Current] GetName]);
                NSLog(@"Field partial name: %@", [[itr Current] GetPartialName]);

                NSLog(@"------------------------------");
            }

            NSLog(@"Done.");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        // Example 6) Direct generation of FDF.
        @try  
        {
            PTFDFDoc *doc = [[PTFDFDoc alloc] init];
            // Create new fields (i.e. key/value pairs).
            [doc FieldCreateWithString: @"Company" type: e_pttext field_value: @"PDFTron Systems"];
            [doc FieldCreateWithString: @"First Name" type: e_pttext field_value: @"John"];
            [doc FieldCreateWithString: @"Last Name"  type: e_pttext field_value: @"Doe"];
            // ...		

            // [doc SetPdfFileName: @"mydoc.pdf"];

            [doc SaveFDFDocToFile: @"../../TestFiles/Output/sample_output.fdf"];
            NSLog(@"Done. Results saved in sample_output.fdf");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }
        
        return ret;
    }
}


