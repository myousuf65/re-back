//
//  AppDelegate.m
//  WebViewerWK
//
//  Copyright © 2001-2019 PDFTron Systems Inc. All rights reserved.
//
#import "AppDelegate.h"
#import "PTMessageHandler.h"

@import WebKit;

@interface AppDelegate ()

@property (weak) IBOutlet NSWindow *window;
@end

@implementation AppDelegate {
    PTMessageHandler* m_msgHandler;
}

- (void)applicationDidFinishLaunching:(NSNotification *)aNotification
{
    [[self window] setTitle:@"WebViewerWKSample"];
    
    NSBundle* appBundle = [NSBundle mainBundle];
    NSString* htmlPath = [appBundle pathForResource:@"HTML" ofType:nil];
    NSLog(@"[AppDelegate.applicationDidFinishLaunching] HTML directory path is: %@", htmlPath);
    
    NSMutableString* indexPath = [NSMutableString stringWithString:htmlPath];
    [indexPath appendString:@"/index.html"];
    // WKWebView is strict in terms of iFrame and CORS, so we have to load the ReaderControl.html directly.
    // Update: we do the redirect to ReaderControl.html in the HTML side directly instead here...
    /*
    [indexPath appendString:@"/js/html5/ReaderControl.html"];
    NSLog(@"[AppDelegate.applicationDidFinishLaunching] HTML index path is: %@", indexPath);
    
    // Add some querystrings
    NSURLComponents* urlComponents = [NSURLComponents componentsWithString:indexPath];
    NSArray<NSURLQueryItem*>* queries = @[
        [NSURLQueryItem queryItemWithName:@"pdf" value:@"jsworker"],
        [NSURLQueryItem queryItemWithName:@"did" value:@"test"],
        [NSURLQueryItem queryItemWithName:@"config" value:@""],
        [NSURLQueryItem queryItemWithName:@"filepicker" value:@"0"],
        [NSURLQueryItem queryItemWithName:@"preloadWorker" value:@"1"],
        [NSURLQueryItem queryItemWithName:@"pdfnet" value:@"0"],
        [NSURLQueryItem queryItemWithName:@"toolbar" value:@"true"],
        [NSURLQueryItem queryItemWithName:@"pageHistory" value:@"1"]
    ];
    [urlComponents setQueryItems:queries];
    NSLog(@"[AppDelegate.applicationDidFinishLaunching] File to load is: %@", [[urlComponents URL] absoluteString]);
    */

    // Insert code here to initialize your application
    WKWebView* mainView = [[WKWebView alloc] initWithFrame:[[self window] frame]];
    [[self window] setContentView:mainView];
    
    // These operations on WKPreferences might be allowed by the Mac App Store...
    [[[mainView configuration] preferences] setValue:@YES forKey:@"developerExtrasEnabled"];
    // [[[mainView configuration] preferences] setValue:@YES forKey:@"localStorageEnabled"];
    
    m_msgHandler = [[PTMessageHandler alloc] init];
    [m_msgHandler initializeJSWorker];
    [m_msgHandler setHTMLContentPath:htmlPath];
    [[[mainView configuration] userContentController] addScriptMessageHandler:m_msgHandler name:@"native"];
    // [mainView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:@"http://www.pdftron.com"]]];
    
    [mainView loadFileURL:[NSURL fileURLWithPath:indexPath] allowingReadAccessToURL:[NSURL fileURLWithPath:htmlPath isDirectory:TRUE]];
}

- (void)applicationWillTerminate:(NSNotification *)aNotification
{
    // Insert code here to tear down your application
}

@end
