//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>


//---------------------------------------------------------------------------------------
// This sample illustrates basic PDFNet capabilities related to interactive 
// forms (also known as AcroForms). 
//---------------------------------------------------------------------------------------

void RenameAllFields(PTPDFDoc* doc, NSString* name)
{
	PTFieldIterator * itr = [doc GetFieldIteratorWithName: name];
    int counter;
	for (counter=0; [itr HasNext]; itr=[doc GetFieldIteratorWithName: name], ++counter)
	{
		PTField *f = [itr Current];
		[f Rename: [name stringByAppendingFormat: @"%d", counter]];
	}
}

enum CheckStyle {
	e_tick,
	e_circle,
	e_cross,
	e_diamond,
	e_square,
	e_star
}; 

// Note: The visual appearance of check-marks and radio-buttons in PDF documents is 
// not limited to CheckStyle-s. It is possible to create a visual appearance using 
// arbitrary glyph, text, raster image, or path object. Although most PDF producers 
// limit the options to the above 'standard' styles, using PDFNet you can generate 
// arbitrary appearances.
PTObj* CreateCheckmarkAppearance(PTPDFDoc *doc, enum CheckStyle style) 
{
	// Create a checkmark appearance stream ------------------------------------
	PTElementBuilder *build = [[PTElementBuilder alloc] init];
	PTElementWriter *writer = [[PTElementWriter alloc] init];
	[writer WriterBeginWithSDFDoc: [doc GetSDFDoc] compress: YES];
	[writer WriteElement: [build CreateTextBegin]];

	NSString* symbol;
	switch (style) {
		case e_circle: symbol = @"\154"; break;
		case e_diamond: symbol = @"\165"; break;
		case e_cross: symbol = @"\65"; break;
		case e_square: symbol = @"\156"; break;
		case e_star: symbol = @"\110"; break;
			// ...
			// See section D.4 "ZapfDingbats Set and Encoding" in PDF Reference Manual 
			// (http://www.pdftron.com/downloads/PDFReference16.pdf) for the complete 
			// graphical map for ZapfDingbats font. Please note that all character codes
			// are represented using the 'octal' notation.
		default:  // e_tick
			symbol = @"\64";
	}

	PTElement *checkmark = [build CreateTextRunWithFont: symbol font: [PTFont Create: [doc GetSDFDoc] type: e_ptzapf_dingbats embed: NO] font_sz: 1];
	[writer WriteElement: checkmark];
	[writer WriteElement: [build CreateTextEnd]];

	PTObj * stm = [writer End];
	[stm PutRect: @"BBox" x1: -0.2 y1: -0.2 x2: 1 y2: 1]; // Clip
	[stm PutName: @"Subtype" name: @"Form"];
	return stm;
}

PTObj* CreateButtonAppearance(PTPDFDoc* doc, bool button_down) 
{
	// Create a button appearance stream ------------------------------------
	PTElementBuilder *build = [[PTElementBuilder alloc] init];
	PTElementWriter *writer = [[PTElementWriter alloc] init];
	[writer WriterBeginWithSDFDoc: [doc GetSDFDoc] compress: YES];

	// Draw background
	PTElement *element = [build CreateRect: 0 y: 0 width: 101 height: 137];
	[element SetPathFill: YES];
    [element SetPathStroke: NO];
    [[element GetGState] SetFillColorSpace: [PTColorSpace CreateDeviceGray]];
	[[element GetGState] SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 0.75 y: 0 z: 0 w: 0]];
	[writer WriteElement: element]; 

	// Draw 'Submit' text
	[writer WriteElement: [build CreateTextBegin]]; 
	{
		NSString *text = @"Submit";
		element = [build CreateTextRunWithFont: text font: [PTFont Create: [doc GetSDFDoc] type: e_pthelvetica_bold embed: NO] font_sz: 12];
		[[element GetGState] SetFillColorWithColorPt: [[PTColorPt alloc] initWithX: 0 y: 0 z: 0 w: 0]];

		if (button_down) 
			[element SetTextMatrixWithMatrix2D: [[PTMatrix2D alloc] initWithA: 1 b: 0 c: 0 d: 1 h: 33 v: 10]];
		else 
			[element SetTextMatrixWithMatrix2D: [[PTMatrix2D alloc] initWithA: 1 b: 0 c: 0 d: 1 h: 30 v: 13]];
		[writer WriteElement: element];
	}
	[writer WriteElement: [build CreateTextEnd]];

	PTObj * stm = [writer End]; 

	// Set the bounding box
	[stm PutRect: @"BBox" x1: 0 y1: 0 x2: 101 y2: 37];
	[stm PutName: @"Subtype" name: @"Form"];
	return stm;
}

int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        //----------------------------------------------------------------------------------
        // Example 1: Programatically create new Form Fields and Widget Annotations.
        //----------------------------------------------------------------------------------
        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] init];		
            PTPage *blank_page = [doc PageCreate: [[PTPDFRect alloc] initWithX1: 0 y1: 0 x2: 612 y2: 792]]; // Create a blank new page and add some form fields.

            // Create new fields.
            PTField *emp_first_name = [doc FieldCreateWithString: @"employee.name.first" type: e_pttext field_value: @"John" def_field_value: @""];
            PTField *emp_last_name = [doc FieldCreateWithString: @"employee.name.last" type: e_pttext field_value: @"Doe" def_field_value: @""];
            PTField *emp_last_check1 = [doc FieldCreateWithString: @"employee.name.check1" type: e_ptcheck field_value: @"Yes" def_field_value: @""];

            PTField *submit = [doc FieldCreate: @"submit" type: e_ptbutton field_value: [[PTObj alloc] init]];

            // Create page annotations for the above fields.

            // Create text annotations
            PTWidget *annot1 = [PTWidget Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 50 y1: 550 x2: 350 y2: 600] field: emp_first_name];
            PTWidget *annot2 = [PTWidget Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 50 y1: 450 x2: 350 y2: 500] field: emp_last_name];

            // Create a check-box annotation
            PTWidget *annot3 = [PTWidget Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 64 y1: 356 x2: 120 y2: 410] field: emp_last_check1];
            // Set the annotation appearance for the "Yes" state...
            [annot3 SetAppearance: CreateCheckmarkAppearance(doc, e_tick) annot_state: e_ptnormal app_state: @"Yes"];
            
            // Create button annotation
            PTWidget *annot4 = [PTWidget Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 64 y1: 284 x2: 163 y2: 320] field: submit];
            // Set the annotation appearances for the down and up state...
            [annot4 SetAppearance: CreateButtonAppearance(doc, NO) annot_state: e_ptnormal app_state: @""];
        [annot4 SetAppearance: CreateButtonAppearance(doc, YES) annot_state: e_ptdown app_state: @""];
            
            // Create 'SubmitForm' action. The action will be linked to the button.
            PTFileSpec *url = [PTFileSpec CreateURL: [doc GetSDFDoc] url: @"http://www.pdftron.com"];
            PTAction *button_action = [PTAction CreateSubmitForm: url];

            // Associate the above action with 'Down' event in annotations action dictionary.
            PTObj * annot_action = [[annot4 GetSDFObj] PutDict: @"AA"];
            [annot_action Put: @"D" obj: [button_action GetSDFObj]];

            [blank_page AnnotPushBack: annot1];  // Add annotations to the page
        [blank_page AnnotPushBack: annot2];
            [blank_page AnnotPushBack: annot3];
            [blank_page AnnotPushBack: annot4];

            [doc PagePushBack: blank_page];	// Add the page as the last page in the document.

            // If you are not satisfied with the look of default auto-generated appearance 
            // streams you can delete "AP" entry from the Widget annotation and set 
            // "NeedAppearances" flag in AcroForm dictionary:
            //    doc.GetAcroForm().PutBool("NeedAppearances", true);
            // This will force the viewer application to auto-generate new appearance streams 
            // every time the document is opened.
            //
            // Alternatively you can generate custom annotation appearance using ElementWriter 
            // and then set the "AP" entry in the widget dictionary to the new appearance
            // stream.
            //
            // Yet another option is to pre-populate field entries with dummy text. When 
            // you edit the field values using PDFNet the new field appearances will match 
            // the old ones.

            //doc.GetAcroForm().PutBool("NeedAppearances", true);
            [doc RefreshFieldAppearances];

            [doc SaveToFile: @"../../TestFiles/Output/forms_test1.pdf" flags: 0];
            NSLog(@"Done.");
        }
        @catch (NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;		
        }

        //----------------------------------------------------------------------------------
        // Example 2: 
        // Fill-in forms / Modify values of existing fields.
        // Traverse all form fields in the document (and print out their names). 
        // Search for specific fields in the document.
        //----------------------------------------------------------------------------------
        @try  
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/forms_test1.pdf"];
            [doc InitSecurityHandler];

            PTFieldIterator * itr = [doc GetFieldIterator];
            for(; [itr HasNext]; [itr Next]) 
            {
                NSLog(@"Field name: %@", [[itr Current] GetName]);
                NSLog(@"Field partial name: %@", [[itr Current] GetPartialName]);

                NSLog(@"Field type: ");
                PTFieldType type = [[itr Current] GetType];
                NSString *str_val = [[itr Current] GetValueAsString];

                switch(type)
                {
				case e_ptcheck:
					[[itr Current] SetValueWithBool: YES];
					NSLog(@"Check box: Value = %@", str_val); 
					break;
                case e_ptbutton: 
                    NSLog(@"Button"); 
                    break;
                case e_ptradio: 
                    NSLog(@"Radio button: Value = %@", str_val); 
                    break;
                case e_pttext: 
                    {
                        NSLog(@"Text");
                        // Edit all variable text in the document
                        [[itr Current] SetValue: [@"This is a new value. The old one was: " stringByAppendingString: str_val]];
                    }
                    break;
				case e_ptchoice: NSLog(@"Choice"); break;
				case e_ptsignature: NSLog(@"Signature"); break;
				default: break;
                }

                NSLog(@"------------------------------");
            }

            // Search for a specific field
            PTField *f = [doc GetField: @"employee.name.first"];
            if (f) 
            {
                NSLog(@"Field search for %@ was successful", [f GetName]);
            }
            else 
            {
                NSLog(@"Field search failed");
            }

            // Regenerate field appearances.
            [doc RefreshFieldAppearances];
            [doc SaveToFile: @"../../TestFiles/Output/forms_test_edit.pdf" flags: 0];
            NSLog(@"Done.");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        //----------------------------------------------------------------------------------
        // Sample: Form templating
        // Replicate pages and form data within a document. Then rename field names to make 
        // them unique.
        //----------------------------------------------------------------------------------
        @try  
        {
            // Sample: Copying the page with forms within the same document
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/forms_test1.pdf"];
            [doc InitSecurityHandler];

            PTPage *src_page = [doc GetPage: 1];
            [doc PagePushBack: src_page];  // Append several copies of the first page
            [doc PagePushBack: src_page];	 // Note that forms are successfully copied
            [doc PagePushBack: src_page];
            [doc PagePushBack: src_page];

            // Now we rename fields in order to make every field unique.
            // You can use this technique for dynamic template filling where you have a 'master'
            // form page that should be replicated, but with unique field names on every page. 
            RenameAllFields(doc, @"employee.name.first");
            RenameAllFields(doc, @"employee.name.last");
            RenameAllFields(doc, @"employee.name.check1");
            RenameAllFields(doc, @"submit");

            [doc SaveToFile: @"../../TestFiles/Output/forms_test1_cloned.pdf" flags: 0];
            NSLog(@"Done.");
        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        //----------------------------------------------------------------------------------
        // Sample: 
        // Flatten all form fields in a document.
        // Note that this sample is intended to show that it is possible to flatten
        // individual fields. PDFNet provides a utility function PDFDoc.FlattenAnnotations()
        // that will automatically flatten all fields.
        //----------------------------------------------------------------------------------
        @try  
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/forms_test1.pdf"];
            [doc InitSecurityHandler];

            // Traverse all pages
            if (true) {
                [doc FlattenAnnotations: NO];
            }
            else // Manual flattening
            {			
                PTPageIterator *pitr;
                for (pitr = [doc GetPageIterator: 1]; 
                    [pitr HasNext]; [pitr Next])  
                {
                    PTPage *page = [pitr Current];
                    PTObj * annots = [page GetAnnots];
                    if (annots)
                    {	// Look for all widget annotations (in reverse order)
                    int i;
                        for (i = (int)[annots Size]-1; i>=0; --i)
                        {
                            if ([[[[[annots GetAt: i] Get: @"Subtype"] Value] GetName]  isEqualToString: @"Widget"])
                            {
                                PTField *field = [[PTField alloc] initWithField_dict: [annots GetAt: i]];
                                [field Flatten: page];

                                // Another way of making a read only field is by modifying 
                                // field's e_read_only flag: 
                                //    field.SetFlag(Field::e_read_only, true);
                            }
                        }
                    }
                }
            }

            [doc SaveToFile: @"../../TestFiles/Output/forms_test1_flattened.pdf" flags: 0];
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


