//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//---------------------------------------------------------------------------------------
// This sample explores the structure and content of a tagged PDF document and dumps 
// the structure information to the console window.
//
// In tagged PDF documents StructTree acts as a central repository for information 
// related to a PDF document's logical structure. The tree consists of StructElement-s
// and ContentItem-s which are leaf nodes of the structure tree.
//
// The sample can be extended to access and extract the marked-content elements such 
// as text and images.
//---------------------------------------------------------------------------------------


NSString *PrintIdent(int ident) { 
    int i;
    NSString * str= @"\n";
    for (i=0; i<ident; ++i) {
        [str stringByAppendingString: @"  "]; 
    }
    return str;
}

// Used in code snippet 1.
NSString* ProcessStructElement(PTSElement *element, int ident)
{
	if (![element IsValid]) {
		return @"";
	}

    NSString *result = @"";
	// Print out the type and title info, if any.
	result = [result stringByAppendingFormat: @"%@Type: %@", PrintIdent(ident++), [element GetType]];
    if ([element HasTitle]) {
		result = [result stringByAppendingFormat: @". Title: %@", [element GetTitle]];
	}

	int num = [element GetNumKids];
    int i;
	for (i=0; i<num; ++i) 
	{
		// Check is the kid is a leaf node (i.e. it is a ContentItem).
		if ([element IsContentItem: i]) { 
			PTContentItem *cont = [element GetAsContentItem: i]; 
			PTContentItemType type = [cont GetType];

			PTPage *page = [cont GetPage];

			result = [result stringByAppendingFormat: @"%@Content Item. Part of page #%d%@", PrintIdent(ident), [page GetIndex], PrintIdent(ident)];
            
			switch (type) {
				case e_ptMCID:
				case e_ptMCR:
					result = [result stringByAppendingFormat: @"MCID: %d", [cont GetMCID]];
					break;
				case e_ptOBJR:
					{
						result = [result stringByAppendingString: @"OBJR "];
                        PTObj *ref_obj;
						if ((ref_obj = [cont GetRefObj]) != NULL)
							result = [result stringByAppendingFormat: @"- Referenced Object#: %u", [ref_obj GetObjNum]];
					}
					break;
				default: 
					break;
			}
		}
		else {  // the kid is another StructElement node.
			result = [result stringByAppendingString: ProcessStructElement([element GetAsStructElem: i], ident)];
		}
	}
    return result;
}

// Used in code snippet 2.
NSString* ProcessLogicalStructureTestElements(PTElementReader *reader)
{
    PTElement *element;
    NSString *result = @"";
	while ((element = [reader Next]) != NULL) 	// Read page contents
	{
		// In this sample we process only paths & text, but the code can be 
		// extended to handle any element type.
		PTElementType type = [element GetType];
		if (type == e_ptpath || type == e_pttext_obj || type == e_ptpath) 
		{   
			switch (type)	{
			case e_ptpath:				// Process path ...
				result = [result stringByAppendingString: @"\nPATH: "];
				break; 
			case e_pttext_obj: 				// Process text ...
                result = [result stringByAppendingFormat: @"\nTEXT: %@\n ", [element GetTextString]];
				break;
			case e_ptform:				// Process form XObjects
				result = [result stringByAppendingString: @"\nFORM XObject: "];
				//reader.FormBegin(); 
				//ProcessLogicalStructureTestElements(reader);
				//reader.End(); 
				break;
			default:
				break;
			}

			// Check if the element is associated with any structural element.
			// Content items are leaf nodes of the structure tree.
			PTSElement *struct_parent = [element GetParentStructElement];
			if ([struct_parent IsValid]) {
				// Print out the parent structural element's type, title, and object number.
				result = [result stringByAppendingFormat: @" Type: %@, MCID: %d", [struct_parent GetType], [element GetStructMCID]];
				if ([struct_parent HasTitle]) {
					result = [result stringByAppendingFormat: @". Title: %@", [struct_parent GetTitle]];
				}
				result = [result stringByAppendingFormat: @", Obj#: %u", [[struct_parent GetSDFObj] GetObjNum]];
			}
		}
	}
    return result;
}

// Used in code snippet 3.
//typedef map<int, string> MCIDPageMap;
NSMutableDictionary *MCIDPageMap;
NSMutableDictionary *MCIDDocMap;
//typedef map<int, MCIDPageMap> MCIDDocMap;

// Used in code snippet 3.
void ProcessLogicalStructureTestElements2(PTElementReader *reader, NSMutableDictionary *mcid_page_map)
{
   PTElement *element;
	while ((element = [reader Next]) != NULL) // Read page contents
	{
		// In this sample we process only text, but the code can be extended 
		// to handle paths, images, or any other Element type.
		int mcid = [element GetStructMCID];
		if (mcid>= 0 && [element GetType] == e_pttext_obj) {
			NSString *val = [element GetTextString];
            id key = @(mcid);
            BOOL exist = [mcid_page_map.allKeys containsObject: key];
			if (exist) {
                NSString *str = mcid_page_map[key];
                mcid_page_map[key] = [str stringByAppendingString: val];
            } 
			else {
                mcid_page_map[key] = val;
            }
		}
	}
}

// Used in code snippet 3.
NSString* ProcessStructElement2(PTSElement *element, NSMutableDictionary *mcid_doc_map, int ident) 
{
	if (![element IsValid]) {
		return @"";
	}
    NSString *result = @"";
	// Print out the type and title info, if any.
	result = [result stringByAppendingString: PrintIdent(ident)];
	result = [result stringByAppendingFormat: @"<%@", [element GetType]];
	if ([element HasTitle]) {
		result = [result stringByAppendingFormat: @" title=\"%@\"", [element GetTitle]];
	}
    result = [result stringByAppendingString: @">"];

	int num = [element GetNumKids];
    int i;
	for (i=0; i<num; ++i) 
	{		
		if ([element IsContentItem: i]) { 
			PTContentItem *cont = [element GetAsContentItem: i]; 
			if ([cont GetType] == e_ptMCID) {
				int page_num = [[cont GetPage] GetIndex];
                id key = @(page_num);
                BOOL exist = [mcid_doc_map.allKeys containsObject: key];
                
				if (exist) {
					NSMutableDictionary *mcid_page_map = mcid_doc_map[key];
					id key2 = @([cont GetMCID]);
                    BOOL exist2 = [mcid_page_map.allKeys containsObject: key2];
					if (exist2) {
                        NSString *str = mcid_page_map[key2];
						result = [result stringByAppendingString: str]; 
					}                    
				}
			}
		}
		else {  // the kid is another StructElement node.
			result = [result stringByAppendingString: ProcessStructElement2([element GetAsStructElem :i], mcid_doc_map, ident+1)];
		}
	}

	result = [result stringByAppendingString: PrintIdent(ident)];
	result = [result stringByAppendingFormat: @"</%@>", [element GetType]];
    return result;
}


int main(int argc, char *argv[])
{
    @autoreleasepool {
		int ret = 0;
		[PTPDFNet Initialize: 0];

		@try	// Extract logical structure from a PDF document
		{
			PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/tagged.pdf"];
			[doc InitSecurityHandler];

			NSLog(@"____________________________________________________________");
			NSLog(@"Sample 1 - Traverse logical structure tree...");
			{
				PTSTree *tree = [doc GetStructTree];
				if ([tree IsValid]) {
					NSLog(@"Document has a StructTree root.");

                int i;
					for (i=0; i<[tree GetNumKids]; ++i) {
						// Recursively get structure info for all child elements.
						NSLog(@"%@", ProcessStructElement([tree GetKid: i], 0));
					}
				}
				else {
					NSLog(@"This document does not contain any logical structure.");
				}
			}
			NSLog(@"Done 1.");

			NSLog(@"____________________________________________________________");
			NSLog(@"Sample 2 - Get parent logical structure elements from");
			NSLog(@"layout elements.");
			{
				PTElementReader *reader = [[PTElementReader alloc] init];
            PTPageIterator *itr;
				for (itr = [doc GetPageIterator: 1]; [itr HasNext]; [itr Next]) {
					[reader Begin: [itr Current]];
					NSLog(@"%@", ProcessLogicalStructureTestElements(reader));
					[reader End];
				}
			}
			NSLog(@"Done 2.");

			NSLog(@"____________________________________________________________");
			NSLog(@"Sample 3 - 'XML style' extraction of PDF logical structure and page content.");
			{
				NSMutableDictionary *mcid_doc_map = [[NSMutableDictionary alloc] init];
				PTElementReader *reader = [[PTElementReader alloc] init];
            PTPageIterator *itr;
				for (itr = [doc GetPageIterator: 1]; [itr HasNext]; [itr Next]) {				
					[reader Begin: [itr Current]];
					NSMutableDictionary *arr = [[NSMutableDictionary alloc] init];
                id key = @([[itr Current] GetIndex]);
                mcid_doc_map[key] = arr;
					ProcessLogicalStructureTestElements2(reader, mcid_doc_map[key]);
					[reader End];
				}

				PTSTree *tree = [doc GetStructTree];
				if ([tree IsValid]) {
                int i;
					for (i=0; i<[tree GetNumKids]; ++i) {
						NSLog(@"%@", ProcessStructElement2([tree GetKid: i], mcid_doc_map, 0));
					}
				}
			}
			NSLog(@"Done 3.");
		}
		@catch(NSException *e) 
		{
			NSLog(@"%@", e.reason);
			ret = 1;
		}
    
		return ret;
    }
}

