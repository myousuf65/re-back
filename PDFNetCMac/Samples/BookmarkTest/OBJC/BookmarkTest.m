//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>
#include <stdio.h>

//-----------------------------------------------------------------------------------------
// The sample code illustrates how to read and edit existing outline items and create 
// new bookmarks using the high-level API.
//-----------------------------------------------------------------------------------------

void PrintIndent(PTBookmark *item) 
{
	int ident = [item GetIndent] - 1;
	int i=0;
	for (int i=0; i<ident; ++i) printf("  ");
}

// Prints out the outline tree to the standard output
void PrintOutlineTree(PTBookmark *item)
{
	for (; [item IsValid]; item=[item GetNext])
	{
		PrintIndent(item);
		if ([item IsOpen]) {
			printf("- %s ACTION -> ", [[item GetTitle] UTF8String]);
		}
		else {
			printf("+ %s ACTION -> ", [[item GetTitle] UTF8String]);
		}
		
		// Print Action
		PTAction *action = [item GetAction];
		if ([action IsValid]) {
			if ([action GetType] == e_ptGoTo) {
				PTDestination *dest = [action GetDest];
				if ([dest IsValid]) {
					PTPage *page = [dest GetPage];
					printf("GoTo Page #%d\n", [page GetIndex]);
				}
			}
			else {
				puts("Not a 'GoTo' action");
			}
		} else {
			puts("NULL");
		}

		if ([item HasChildren])	 // Recursively print children sub-trees
		{
			PrintOutlineTree([item GetFirstChild]);
		}
	}
}

int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        // The following example illustrates how to create and edit the outline tree 
        // using high-level Bookmark methods.
        @try  
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/numbered.pdf"];
            [doc InitSecurityHandler];
            
            // Lets first create the root bookmark items. 
            PTBookmark *red = [PTBookmark Create: doc in_title: @"Red"];
            PTBookmark *green = [PTBookmark Create: doc in_title: @"Green"];
            PTBookmark *blue = [PTBookmark Create: doc in_title: @"Blue"];

            [doc AddRootBookmark: red];
            [doc AddRootBookmark: green];
            [doc AddRootBookmark: blue];

            // You can also add new root bookmarks using Bookmark.AddNext("...")
            [blue AddNextWithTitle: @"foo"];
            [blue AddNextWithTitle: @"bar"];

            // We can now associate new bookmarks with page destinations:

            // The following example creates an 'explicit' destination (see 
            // section '8.2.1 Destinations' in PDF Reference for more details)
            PTDestination *red_dest = [PTDestination CreateFit: [[doc GetPageIterator: 1] Current]];
            [red SetAction: [PTAction CreateGoto: red_dest]];

            // Create an explicit destination to the first green page in the document
            [green SetAction: [PTAction CreateGoto: [PTDestination CreateFit: [doc GetPage: 10]]]];

            // The following example creates a 'named' destination (see 
            // section '8.2.1 Destinations' in PDF Reference for more details)
            // Named destinations have certain advantages over explicit destinations.
            char* buf = "blue1";
            NSData* key = [NSData dataWithBytes: (const void*)buf length: 5];
            PTAction *blue_action = [PTAction CreateGotoWithNamedDestination: key key_sz: 5 dest: [PTDestination CreateFit: [doc GetPage: 19]]];
            
            [blue SetAction: blue_action];

            // We can now add children Bookmarks
            PTBookmark *sub_red1 = [red AddChildWithTitle: @"Red - Page 1"];
            [sub_red1 SetAction: [PTAction CreateGoto: [PTDestination CreateFit: [doc GetPage: 1]]]];
            PTBookmark *sub_red2 = [red AddChildWithTitle: @"Red - Page 2"];
            [sub_red2 SetAction: [PTAction CreateGoto: [PTDestination CreateFit: [doc GetPage: 2]]]];
            PTBookmark *sub_red3 = [red AddChildWithTitle: @"Red - Page 3"];
            [sub_red3 SetAction: [PTAction CreateGoto: [PTDestination CreateFit: [doc GetPage: 3]]]];
            PTBookmark *sub_red4 = [sub_red3 AddChildWithTitle: @"Red - Page 4"];
            [sub_red4 SetAction: [PTAction CreateGoto: [PTDestination CreateFit: [doc GetPage: 4]]]];
            PTBookmark *sub_red5 = [sub_red3 AddChildWithTitle: @"Red - Page 5"];
            [sub_red5 SetAction: [PTAction CreateGoto: [PTDestination CreateFit: [doc GetPage: 5]]]];
            PTBookmark *sub_red6 = [sub_red3 AddChildWithTitle: @"Red - Page 6"];
            [sub_red6 SetAction: [PTAction CreateGoto: [PTDestination CreateFit: [doc GetPage: 6]]]];
            
            // Example of how to find and delete a bookmark by title text.
            PTBookmark *foo = [[doc GetFirstBookmark] Find: @"foo"];
            if ([foo IsValid]) 
            {
                [foo Delete];
            }
            else 
            {
                assert(FALSE);
            }

            PTBookmark *bar = [[doc GetFirstBookmark] Find: @"bar"];
            if ([bar IsValid]) 
            {
                [bar Delete];
            }
            else 
            {
                assert(FALSE);
            }

            // Adding color to Bookmarks. Color and other formatting can help readers 
            // get around more easily in large PDF documents.
            [red SetColor: 1 in_g: 0 in_b: 0];
            [green SetColor: 0 in_g: 1 in_b: 0];
            [green SetFlags: 2];			// set bold font
            [blue SetColor: 0 in_g: 0 in_b: 1];
            [blue SetFlags: 3];			// set bold and italic

            [doc SaveToFile: @"../../TestFiles/Output/bookmark.pdf" flags: 0];
            puts("Done. Result saved in bookmark.pdf");
        }
        @catch(NSException *e)
        {
            printf("%s\n", [e.reason UTF8String]);
            ret = 1;
        }

        
        // The following example illustrates how to traverse the outline tree using 
        // Bookmark navigation methods: Bookmark.GetNext(), Bookmark.GetPrev(), 
        // Bookmark.GetFirstChild () and Bookmark.GetLastChild ().
        @try  
        {
            // Open the document that was saved in the previous code sample
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/bookmark.pdf"];
            [doc InitSecurityHandler];
            
            PTBookmark *root = [doc GetFirstBookmark];
            PrintOutlineTree(root);

            puts("Done.");
        }
        @catch(NSException *e)
        {
            printf("%s\n", [e.reason UTF8String]);
            ret = 1;
        }
        
        // The following example illustrates how to create a Bookmark to a page 
        // in a remote document. A remote go-to action is similar to an ordinary 
        // go-to action, but jumps to a destination in another PDF file instead 
        // of the current file. See Section 8.5.3 'Remote Go-To Actions' in PDF 
        // Reference Manual for details.
        @try  
        {
            // Open the document that was saved in the previous code sample
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/bookmark.pdf"];
            [doc InitSecurityHandler];

            // Create file specification (the file referred to by the remote bookmark)
            PTObj * file_spec = [doc CreateIndirectDict]; 
            [file_spec PutName: @"Type" name: @"Filespec"];
            [file_spec PutString: @"F" value: @"bookmark.pdf"];
            PTFileSpec *spec = [[PTFileSpec alloc] initWithF: file_spec];
            PTAction *goto_remote = [PTAction CreateGotoRemoteWithNewWindow: spec page_num: 5 new_window: TRUE];

            PTBookmark *remoteBookmark1 = [PTBookmark Create: doc in_title: @"REMOTE BOOKMARK 1"];
            [remoteBookmark1 SetAction: goto_remote];
            [doc AddRootBookmark: remoteBookmark1];

            // Create another remote bookmark, but this time using the low-level SDF/Cos API.
            // Create a remote action
            PTBookmark *remoteBookmark2 = [PTBookmark Create: doc in_title: @"REMOTE BOOKMARK 2"];
            [doc AddRootBookmark: remoteBookmark2];
            
            PTObj * gotoR = [[remoteBookmark2 GetSDFObj] PutDict: @"A"];
            {
                [gotoR PutName: @"S" name: @"GoToR"]; // Set action type
                [gotoR PutBool: @"NewWindow" value: TRUE];

                // Set the file specification
                [gotoR Put: @"F" obj: file_spec];

                // jump to the first page. Note that pages are indexed from 0.
                PTObj * dest = [gotoR PutArray: @"D"];  // Set the destination
                [dest PushBackNumber: 9]; 
                [dest PushBackName: @"Fit"];
            }

            [doc SaveToFile: @"../../TestFiles/Output/bookmark_remote.pdf" flags: e_ptlinearized];
            
            puts("Done. Result saved in bookmark_remote.pdf");
        }
        @catch(NSException *e)
        {
            printf("%s\n", [e.reason UTF8String]);
            ret = 1;
        }
            
        return ret;
    }
}


