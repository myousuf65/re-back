//
//  PTMessageHandler.h
//  WebViewerWK
//
//  Copyright © 2001-2019 PDFTron Systems Inc. All rights reserved.
//
#pragma once

#import <WebKit/WebKit.h>

@interface PTMessageHandler : NSObject <WKScriptMessageHandler>
- (void) initializeJSWorker;
- (void) setHTMLContentPath:(NSString*)htmlPath;
- (void) userContentController:(WKUserContentController*)userContentController didReceiveScriptMessage:(WKScriptMessage*)message;
@end
