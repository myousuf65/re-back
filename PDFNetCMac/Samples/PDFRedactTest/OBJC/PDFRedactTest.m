//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

#import <OBJC/PDFNetOBJC.h>
#import <Foundation/Foundation.h>

// PDF Redactor is a separately licensable Add-on that offers options to remove 
// (not just covering or obscuring) content within a region of PDF. 
// With printed pages, redaction involves blacking-out or cutting-out areas of 
// the printed page. With electronic documents that use formats such as PDF, 
// redaction typically involves removing sensitive content within documents for 
// safe distribution to courts, patent and government institutions, the media, 
// customers, vendors or any other audience with restricted access to the content. 
//
// The redaction process in PDFNet consists of two steps:
// 
//  a) Content identification: A user applies redact annotations that specify the 
// pieces or regions of content that should be removed. The content for redaction 
// can be identified either interactively (e.g. using 'pdftron.PDF.PDFViewCtrl' 
// as shown in PDFView sample) or programmatically (e.g. using 'pdftron.PDF.TextSearch'
// or 'pdftron.PDF.TextExtractor'). Up until the next step is performed, the user 
// can see, move and redefine these annotations.
//  b) Content removal: Using 'pdftron.PDF.Redactor.Redact()' the user instructs 
// PDFNet to apply the redact regions, after which the content in the area specified 
// by the redact annotations is removed. The redaction function includes number of 
// options to control the style of the redaction overlay (including color, text, 
// font, border, transparency, etc.).
// 
// PDFTron Redactor makes sure that if a portion of an image, text, or vector graphics 
// is contained in a redaction region, that portion of the image or path data is 
// destroyed and is not simply hidden with clipping or image masks. PDFNet API can also 
// be used to review and remove metadata and other content that can exist in a PDF 
// document, including XML Forms Architecture (XFA) content and Extensible Metadata 
// Platform (XMP) content.

void Redact(NSString* input, NSString* output, PTVectorRedaction *vec, PTAppearance *app) {
    PTPDFDoc *doc = [[PTPDFDoc alloc] initWithFilepath: input];
    if ([doc InitSecurityHandler]) {
        [PTRedactor Redact: doc red_arr: vec app: app ext_neg_mode: NO page_coord_sys: YES];
        [doc SaveToFile: output flags: e_ptlinearized ];
    }   
}

int main(int argc, char *argv[])
{
    @autoreleasepool {

        int ret = 0;

        [PTPDFNet Initialize: 0];
        
        PTPDFRect * rect1 = [[PTPDFRect alloc] init];
        [rect1 Set: 100 y1: 100 x2: 550 y2: 600];
        
        PTPDFRect * rect2 = [[PTPDFRect alloc] init];
        [rect2 Set: 30 y1: 30 x2: 450 y2: 450];
        
        PTPDFRect * rect3 = [[PTPDFRect alloc] init];
        [rect3 Set: 0 y1: 0 x2: 100 y2: 100];
        
        PTPDFRect * rect4 = [[PTPDFRect alloc] init];
        [rect4 Set: 100 y1: 100 x2: 200 y2: 200];
        
        PTPDFRect * rect5 = [[PTPDFRect alloc] init];
        [rect5 Set: 300 y1: 300 x2: 400 y2: 400];
        
        PTPDFRect * rect6 = [[PTPDFRect alloc] init];
        [rect6 Set: 500 y1: 500 x2: 600 y2: 600];
        
        PTPDFRect * rect7 = [[PTPDFRect alloc] init];
        [rect7 Set: 0 y1: 0 x2: 700 y2: 20];

        PTVectorRedaction *vec = [[PTVectorRedaction alloc] init];
        [vec add: [[PTRedaction alloc] initWithPage_num: 1 bbox: rect1 negative: NO text: @"Top Secret"]];
        [vec add: [[PTRedaction alloc] initWithPage_num: 2 bbox: rect2 negative: YES text: @"Negative Redaction"]];
        [vec add: [[PTRedaction alloc] initWithPage_num: 2 bbox: rect3 negative: NO text: @"Positive"]];
        [vec add: [[PTRedaction alloc] initWithPage_num: 2 bbox: rect4 negative: NO text: @"Positive"]];
        [vec add: [[PTRedaction alloc] initWithPage_num: 2 bbox: rect5 negative: NO text: @""]];
        [vec add: [[PTRedaction alloc] initWithPage_num: 2 bbox: rect6 negative: NO text: @""]];
        [vec add: [[PTRedaction alloc] initWithPage_num: 3 bbox: rect7 negative: NO text: @""]];
        
        PTAppearance *app = [[PTAppearance alloc] init];
        [app setRedactionOverlay: YES];
        [app setBorder: NO];
        [app setShowRedactedContentRegions: YES];

        Redact(@"../../TestFiles/newsletter.pdf", @"../../TestFiles/Output/redacted.pdf", vec, app);

        NSLog(@"Done...");
            
        return ret;
    }
}

