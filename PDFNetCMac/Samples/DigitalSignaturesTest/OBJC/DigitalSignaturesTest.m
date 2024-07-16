//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------------------------
// This sample demonstrates the basic usage of high-level digital signature API in PDFNet.
//
// The following steps are typically used to add a digital signature to a PDF:
//
//     1. Extend and implement a new SignatureHandler. The SignatureHandler will be used to add or validate/check a
//        digital signature.
//     2. Create an instance of the implemented SignatureHandler and register it with PDFDoc with
//        pdfdoc.AddSignatureHandler(). The method returns an ID that can be used later to associate a SignatureHandler
//        with a field.
//     3. Find the required 'e_ptsignature' field in the existing document or create a new field.
//     4. Call field.UseSignatureHandler() with the ID of your handler.
//     5. Call pdfdoc.Save()
//
// Additional processing can be done before document is signed. For example, UseSignatureHandler() returns an instance
// of SDF dictionary which represents the signature dictionary (or the /V entry of the form field). This can be used to
// add additional information to the signature dictionary (e.g. Name, Reason, Location, etc.).
//
// Although the steps above describes extending the SignatureHandler class, this sample demonstrates the use of
// StdSignatureHandler (a built-in SignatureHandler in PDFNet) to sign a PDF file.
//----------------------------------------------------------------------------------------------------------------------

// To build and run this sample, please specify OpenSSL include & lib paths to the Makefile
//
// If OpenSSL is installed elsewhere, it may be necessary to add the path to the headers in the $(INCLUDE) variable as
// well as the location of either libcrypto.a or libcrypto.so/libcrypto.dylib.

// Note for iOS development: This code can be used to digitally sign PDFs in iOS devices. When using the code signing
// part of this code, it will be necessary to compile OpenSSL for iOS.

#define USE_STD_SIGNATURE_HANDLER 1 // Comment this line if using the OpenSSLSignatureHandler.

#import <CoreFoundation/CoreFoundation.h>

#import <OBJC/PDFNetOBJC.h>

#if (!USE_STD_SIGNATURE_HANDLER)

// OpenSSL includes
#include <openssl/err.h>
#include <openssl/evp.h>
#include <openssl/pkcs12.h>
#include <openssl/pkcs7.h>
#include <openssl/rsa.h>
#include <openssl/sha.h>

// Override SignatureHandler
@interface OpenSSLSignatureHandler : SignatureHandler
{
    SHA_CTX m_sha_ctx;
    EVP_PKEY* mp_pkey;      // private key
    X509* mp_x509;          // signing certificate
    STACK_OF(X509)* mp_ca;  // certificate chain up to the CA
}
- (NSString*) GetName;
- (void) AppendData: (NSData*)data;
- (BOOL) Reset;
- (NSData*) CreateSignature;
- (SignatureHandler*) Clone;
- (id) init: (NSString*) pfxfile password: (NSString*) password;
- (void) dealloc;
@end // interface OpenSSLSignatureHandler

@implementation OpenSSLSignatureHandler
- (NSString*) GetName
{
    return (@"Adobe.PPKLite");
}
- (void) AppendData: (NSData*)data
{
    SHA1_Update(&m_sha_ctx, [data bytes], [data length]);
    return;
}
- (BOOL) Reset
{
    SHA1_Init(&m_sha_ctx);
    return (YES);
}
- (NSData*) CreateSignature
{
    unsigned char sha_buffer[SHA_DIGEST_LENGTH];
    memset((void*) sha_buffer, 0, SHA_DIGEST_LENGTH);
    SHA1_Final(sha_buffer, &m_sha_ctx);
    
    PKCS7* p7 = PKCS7_new();
    PKCS7_set_type(p7, NID_pkcs7_signed);
    
    PKCS7_SIGNER_INFO* p7Si = PKCS7_add_signature(p7, mp_x509, mp_pkey, EVP_sha1());
    PKCS7_add_attrib_content_type(p7Si, OBJ_nid2obj(NID_pkcs7_data));
    PKCS7_add0_attrib_signing_time(p7Si, NULL);
    PKCS7_add1_attrib_digest(p7Si, (const unsigned char*) sha_buffer, SHA_DIGEST_LENGTH);
    PKCS7_add_certificate(p7, mp_x509);    

    int c = 0;
    for ( ; c < sk_X509_num(mp_ca); c++) {
        X509* cert = sk_X509_value(mp_ca, c);
        PKCS7_add_certificate(p7, cert);
    }
    PKCS7_set_detached(p7, 1);
    PKCS7_content_new(p7, NID_pkcs7_data);
    
    PKCS7_SIGNER_INFO_sign(p7Si);
    
    int p7Len = i2d_PKCS7(p7, NULL);
    NSMutableData* signature = [NSMutableData data];
    unsigned char* p7Buf = (unsigned char*) malloc(p7Len);
    if (p7Buf != NULL) {
        unsigned char* pP7Buf = p7Buf;
        i2d_PKCS7(p7, &pP7Buf);
        [signature appendBytes: (const void*) p7Buf length: p7Len];
        free(p7Buf);
    }
    PKCS7_free(p7);
    
    return (signature);
}
- (SignatureHandler*) Clone
{
    return (self);
}
- (id) init: (NSString*) pfxfile password: (NSString*) password;
{
    self = [super init];

    FILE* fp = fopen([pfxfile cStringUsingEncoding: NSASCIIStringEncoding], "rb");
    if (fp == NULL)
        @throw ([NSException exceptionWithName: @"PDFNet Exception" reason: @"Cannot open private key." userInfo: nil]);
    
    PKCS12* p12 = d2i_PKCS12_fp(fp, NULL);
    fclose(fp);
    
    if (p12 == NULL) 
        @throw ([NSException exceptionWithName: @"PDFNet Exception" reason: @"Cannot parse private key." userInfo: nil]);

    mp_pkey = NULL;
    mp_x509 = NULL;
    mp_ca = NULL;
    int parseResult = PKCS12_parse(p12, [password cStringUsingEncoding: NSASCIIStringEncoding], &mp_pkey, &mp_x509, &mp_ca);
    PKCS12_free(p12);
    
    if (parseResult == 0)
        @throw ([NSException exceptionWithName: @"PDFNet Exception" reason: @"Cannot parse private key." userInfo: nil]);

    [self Reset];
    
    return (self);
}
- (void) dealloc
{
    sk_X509_free(mp_ca);
    X509_free(mp_x509);
    EVP_PKEY_free(mp_pkey);
    [super dealloc];
}
@end // implementation OpenSSLSignatureHandler

#endif // (!USE_STD_SIGNATURE_HANDLER)

//
// This functions add an approval signature to the PDF document. The original PDF document contains a blank form field
// that is prepared for a user to sign. The following code demonstrate how to sign this document using PDFNet.
//
BOOL SignPDF()
{
    BOOL result = YES;
    NSString* infile = @"../../TestFiles/doc_to_sign.pdf";
    NSString* outfile = @"../../TestFiles/Output/signed_doc.pdf";
    NSString* certfile = @"../../TestFiles/pdftron.pfx";
    NSString* imagefile = @"../../TestFiles/signature.jpg";
    
    @try {
        NSLog(@"Signing PDF document: '%@'.", infile);
        
        // Open an existing PDF
        PTPDFDoc* doc = [[PTPDFDoc alloc] initWithFilepath: infile];

        // Add an StdSignatureHandler instance to PDFDoc, making sure to keep track of it using the ID returned.
        SignatureHandlerId sigHandlerId = [doc AddStdSignatureHandlerFromFile: certfile pkcs12_keypass: @"password"];
        // When using OpenSSLSignatureHandler class, uncomment the following lines and comment the line above.
        // Create a new instance of the SignatureHandler.
        // OpenSSLSignatureHandler* sigHandler = [[OpenSSLSignatureHandler alloc] init: certfile password: @"password"];
        // Add the SignatureHandler instance to PDFDoc, making sure to keep track of it using the ID returned.
        // SignatureHandlerId sigHandlerId = [doc AddSignatureHandler: sigHandler];
        
        // Obtain the signature form field from the PDFDoc via Annotation.
        PTField* sigField = [doc GetField: @"Signature1"];
        PTWidget* widgetAnnot = [[PTWidget alloc] initWithD: [sigField GetSDFObj]];
        
        // Tell PDFNetC to use the SignatureHandler created to sign the new signature form field.
        PTObj* sigDict = [sigField UseSignatureHandler: sigHandlerId];
        
        // Add more information to the signature dictionary.
        [sigDict PutName: @"SubFilter" name: @"adbe.pkcs7.detached"];
        [sigDict PutString: @"Name" value: @"PDFTron"];
        [sigDict PutString: @"Location" value: @"Vancouver, BC"];
        [sigDict PutString: @"Reason" value: @"Document verification."];
        
        // Add the signature appearance
        PTElementWriter* apWriter = [[PTElementWriter alloc] init];
        PTElementBuilder* apBuilder = [[PTElementBuilder alloc] init];
        [apWriter WriterBeginWithSDFDoc: [doc GetSDFDoc] compress: YES];
        PTImage* sigImg = [PTImage CreateWithFile: [doc GetSDFDoc] filename: imagefile encoder_hints: [[PTObj alloc]init]];
        double w = [sigImg GetImageWidth], h = [sigImg GetImageHeight];

        PTElement* apElement = [apBuilder CreateImageWithCornerAndScale: sigImg x: 0 y: 0 hscale: w vscale: h];
        [apWriter WritePlacedElement: apElement];
        PTObj* apObj = [apWriter End];
        [apObj PutRect: @"BBox" x1: 0 y1: 0 x2: w y2: h];
        [apObj PutName: @"Subtype" name: @"Form"];
        [apObj PutName: @"Type" name: @"XObject"];
        [apWriter WriterBeginWithSDFDoc: [doc GetSDFDoc] compress: YES];
        apElement = [apBuilder CreateFormWithObj: apObj];
        [apWriter WritePlacedElement: apElement];
        apObj = [apWriter End];
        [apObj PutRect: @"BBox" x1: 0 y1: 0 x2: w y2: h];
        [apObj PutName: @"Subtype" name: @"Form"];
        [apObj PutName: @"Type" name: @"XObject"];
        [widgetAnnot SetAppearance: apObj annot_state: e_ptnormal app_state: nil];
        [widgetAnnot RefreshAppearance];
        
        PTObj* widgetObj = [widgetAnnot GetSDFObj];
        [widgetObj PutNumber: @"F" value: 132];
        [widgetObj PutName: @"Type" name: @"Annot"];
        
        // Save the PDFDoc. Once the method below is called, PDFNetC will also sign the document using the information
        // provided.
        [doc SaveToFile: outfile flags: 0];
        
        NSLog(@"Finished signing PDF document.");
    }
    @catch (NSException* e) {
        NSLog(@"Exception: %@ - %@\n", e.name, e.reason);
        result = NO;
    }
    
    return result;
}

//
// Adds a certification signature to the PDF document. Certifying a document is like notarizing a document. Unlike
// approval signatures, there can be only one certification per PDF document. Only the first signature in the PDF
// document can be used as the certification signature. The process of certifying a document is almost exactly the same
// as adding approval signatures with the exception of certification signatures requires an entry in the "Perms"
// dictionary.
//
BOOL CertifyPDF()
{
    BOOL result = YES;
    NSString* infile = @"../../TestFiles/newsletter.pdf";
    NSString* outfile = @"../../TestFiles/Output/newsletter_certified.pdf";
    NSString* certfile = @"../../TestFiles/pdftron.pfx";
    
    @try {
        NSLog(@"Certifying PDF document: '%@',", infile);
        
        // Open an existing PDF
        PTPDFDoc* doc = [[PTPDFDoc alloc] initWithFilepath: infile];

        // Add an StdSignatureHandler instance to PDFDoc, making sure to keep track of it using the ID returned.
        SignatureHandlerId sigHandlerId = [doc AddStdSignatureHandlerFromFile: certfile pkcs12_keypass: @"password"];
        // When using OpenSSLSignatureHandler class, uncomment the following lines and comment the line above.
        // Create a new instance of the SignatureHandler.
        // OpenSSLSignatureHandler* sigHandler = [[OpenSSLSignatureHandler alloc] init: certfile password: @"password"];
        // Add the SignatureHandler instance to PDFDoc, making sure to keep track of it using the ID returned.
        // SignatureHandlerId sigHandlerId = [doc AddSignatureHandler: sigHandler];
        
        // Create new signature form field in the PDFDoc.
        PTField* sigField = [doc FieldCreateWithString: @"Signature1" type: e_ptsignature field_value: @"" def_field_value: @""];
        
        // Assign the form field as an annotation widget to the PDFDoc so that a signature appearance can be added.        
        PTPage* page1 = [doc GetPage: 1];
        PTWidget* widgetAnnot = [PTWidget Create: [doc GetSDFDoc] pos: [[PTPDFRect alloc] initWithX1: 20 y1: 600 x2: 220 y2: 640] field: sigField];
        [page1 AnnotPushBack: widgetAnnot];
        [widgetAnnot SetPage: page1];
        PTObj* widgetObj = [widgetAnnot GetSDFObj];
        [widgetObj PutNumber: @"F" value: 132];
        [widgetObj PutName: @"Type" name: @"Annot"];
        
        // Tell PDFNetC to use the SignatureHandler created to sign the new signature form field.
        PTObj* sigDict = [sigField UseSignatureHandler: sigHandlerId];
        
        // Add more information to the signature dictionary.
        [sigDict PutName: @"SubFilter" name: @"adbe.pkcs7.detached"];
        [sigDict PutString: @"Name" value: @"PDFTron"];
        [sigDict PutString: @"Location" value: @"Vancouver, BC"];
        [sigDict PutString: @"Reason" value: @"Document verification."];

        // Appearance can be added to the widget annotation. Please see the "SignPDF()" function for details.

        // Add this sigDict as DocMDP in Perms dictionary from root
        PTObj* root = [doc GetRoot];
        PTObj* perms = [root PutDict: @"Perms"];
        // add the sigDict as DocMDP (indirect) in Perms
        [perms Put: @"DocMDP" obj: sigDict];
        
        // add the additional DocMDP transform params
        PTObj* refObj = [sigDict PutArray: @"Reference"];
        PTObj* transform = [refObj PushBackDict];
        [transform PutName: @"TransformMethod" name: @"DocMDP"];
        [transform PutName: @"Type" name: @"SigRef"];
        PTObj* transformParams = [transform PutDict: @"TransformParams"];
        [transformParams PutNumber: @"P" value: 1]; // Set permissions as necessary.
        [transformParams PutName: @"Type" name: @"TransformParams"];
        [transformParams PutName: @"V" name: @"1.2"];
        
        // Save the PDFDoc. Once the method below is called, PDFNetC will also sign the document using the information
        // provided.
        [doc SaveToFile: outfile flags: 0];
        
        NSLog(@"Finished certifying PDF document.");
    }
    @catch (NSException* e) {
        NSLog(@"Exception: %@ - %@\n", e.name, e.reason);
        result = NO;
    }

    return result;
}

int main(int argc, const char * argv[])
{
    @autoreleasepool {

        // Initialize PDFNetC
        [PTPDFNet Initialize: 0];

    #if (!USE_STD_SIGNATURE_HANDLER)
        // Initialize OpenSSL library
        CRYPTO_malloc_init();
        ERR_load_crypto_strings();
        OpenSSL_add_all_algorithms();
    #endif // (!USE_STD_SIGNATURE_HANDLER)

        BOOL result = YES;

        if (!SignPDF())
            result = NO;

        if (!CertifyPDF())
            result = NO;

    #if (!USE_STD_SIGNATURE_HANDLER)
        // Release OpenSSL resource usage
        ERR_free_strings();
        EVP_cleanup();
    #endif // (!USE_STD_SIGNATURE_HANDLER)


        if (!result) {
            NSLog(@"Tests failed.");
            return 1;
        }

        NSLog(@"All tests passed.");
        
        return 0;
    }
}

