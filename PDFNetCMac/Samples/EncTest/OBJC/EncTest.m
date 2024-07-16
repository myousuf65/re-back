//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

//---------------------------------------------------------------------------------------
// This sample shows encryption support in PDFNet. The sample reads an encrypted document and 
// sets a new SecurityHandler. The sample also illustrates how password protection can 
// be removed from an existing PDF document.
//---------------------------------------------------------------------------------------
int main(int argc, char *argv[])
{
    @autoreleasepool {
        int ret = 0;
        [PTPDFNet Initialize: 0];

        // Example 1: 
        // secure a PDF document with password protection and adjust permissions 
        
        @try
        {
            // Open the test file
            NSLog(@"Securing an existing document ...");
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/fish.pdf"];
            [doc InitSecurityHandler];
            
            // Perform some operation on the document. In this case we use low level SDF API
            // to replace the content stream of the first page with contents of file 'my_stream.txt'
            if (NO)  // Optional
            {
                NSLog(@"Replacing the content stream, use Flate compression...");

                // Get the page dictionary using the following path: trailer/Root/Pages/Kids/0
                PTObj *page_dict = [[[[[[[[doc GetTrailer] Get: @"Root"] Value] Get: @"Pages"] Value ] Get: @"Kids"] Value] GetAt: 0];

                // Embed a custom stream (file mystream.txt) using Flate compression.
                PTMappedFile *embed_file = [[PTMappedFile alloc] initWithFilename: @"../../TestFiles/mystream.txt"];
                PTFilterReader *mystm = [[PTFilterReader alloc] initWithFilter: embed_file];
            PTFlateEncode *encode = [[PTFlateEncode alloc] initWithInput_filter: [[PTFilter alloc] init] compression_level: -1 buf_sz: 256];
                [page_dict Put: @"Contents" obj: [doc CreateIndirectStream: mystm filter_chain: encode]];
            }

            //encrypt the document


            // Apply a new security handler with given security settings. 
            // In order to open saved PDF you will need a user password 'test'.
            PTSecurityHandler *new_handler = [[PTSecurityHandler alloc] init];

            // Set a new password required to open a document
            NSString* user_password=@"test";
            [new_handler ChangeUserPassword: user_password];

            // Set Permissions
            [new_handler SetPermission: e_ptprint value: YES];
            [new_handler SetPermission: e_ptextract_content value: NO];

            // Note: document takes the ownership of new_handler.
            [doc SetSecurityHandler: new_handler];

            // Save the changes.
            NSLog(@"Saving modified file...");
            [doc SaveToFile: @"../../TestFiles/Output/secured.pdf" flags: 0];

        }
        @catch(NSException *e)
        {
            NSLog(@"%@", e.reason);
            ret = 1;
        }

        // Example 2:
        // Opens an encrypted PDF document and removes its security.

        @try
        {
            PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: @"../../TestFiles/Output/secured.pdf"];

            //If the document is encrypted prompt for the password
            if (![doc InitSecurityHandler]) 
            {
                bool success=NO;
                NSLog(@"The password is: test");
                int count;
                for(count=0; count<3;count++)
                {
                    char input[255];
                    NSLog(@"A password required to open the document.");
                NSLog(@"Please enter the password:");
                    //scanf("%c", &input);
                NSString *password = @"test";
    //                int i = 0;
    //                for (; i < 254; i++) {
    //                    if (input[i] == '\0') {
    //                        break;
    //                    }
    //                    [password stringByAppendingFormat: @"%c", input[i]];
    //                }
                
                    if([doc InitStdSecurityHandler: password])
                    {
                        success=true;
                        NSLog(@"The password is correct.");
                        break;
                    }
                    else if(count<3)
                    {
                        NSLog(@"The password is incorrect, please try again.");
                    }
                }
                if(!success)
                {
                    NSLog(@"Document authentication error....");
                    ret = 1;
                    return ret;
                }

                PTSecurityHandler *hdlr = [doc GetSecurityHandler]; 
                NSLog(@"Document Open Password: %d", [hdlr IsUserPasswordRequired]);
                NSLog(@"Permissions Password: %d", [hdlr IsMasterPasswordRequired]);
                NSLog(@"Permissions: ");
                NSLog(@"    Has 'owner' permissions: %d", [hdlr GetPermission: e_ptowner]);
                NSLog(@"    Open and decrypt the document: %d", [hdlr GetPermission: e_ptdoc_open]);
                NSLog(@"    Allow content extraction: %d", [hdlr GetPermission: e_ptextract_content]); 
                NSLog(@"    Allow full document editing: %d", [hdlr GetPermission: e_ptdoc_modify]);
                NSLog(@"    Allow printing: %d", [hdlr GetPermission: e_ptprint]); 
                NSLog(@"    Allow high resolution printing: %d", [hdlr GetPermission: e_ptprint_high]); 
                NSLog(@"    Allow annotation editing: %d", [hdlr GetPermission: e_ptmod_annot]);
                NSLog(@"    Allow form fill: %d", [hdlr GetPermission: e_ptfill_forms]);
                NSLog(@"    Allow content extraction for accessibility: %d", [hdlr GetPermission: e_ptaccess_support]);
                NSLog(@"    Allow document assembly: %d", [hdlr GetPermission: e_ptassemble_doc]);   
            }

            //remove all security on the document
            [doc RemoveSecurity];
            [doc SaveToFile: @"../../TestFiles/Output/not_secured.pdf" flags: 0];

            }
            @catch(NSException *e)
            {
                NSLog(@"%@", e.reason);
            ret = 1;
        }

        return ret;
    }
}
