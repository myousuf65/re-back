//
//  PTMessageHandler.m
//  WebViewerWK
//
//  Copyright © 2001-2019 PDFTron Systems Inc. All rights reserved.
//
#import "PTMessageHandler.h"

#include <PDFNetJSWorker/PDFNetJSWorker.h>

@interface PTCallbackData : NSObject
- (instancetype) initWithWebView:(WKWebView*)webView;
- (void) runCallbackScriptWithArg:(NSString*)arg;
@property WKWebView* WebView;
@property NSString* JSCallbackFunction;
@end

@implementation PTCallbackData
- (instancetype) initWithWebView:(WKWebView*)webView
{
    self = [super init];
    if (self != nil) {
        [self setWebView:webView];
        [self setJSCallbackFunction:nil];
    }
    return self;
}
- (void) runCallbackScriptWithArg:(NSString*)arg
{
    if ([self WebView] != nil && [self JSCallbackFunction] != nil) {
        NSMutableString* jsScript = [NSMutableString stringWithString:[self JSCallbackFunction]];
        [jsScript appendFormat:@"(%@);", arg];
        NSLog(@"[PTCallbackData.runCallbackScript] Invoking JS code: %@", jsScript);
        WKWebView* webView = [self WebView];
        dispatch_async( dispatch_get_main_queue(), ^{
            [webView evaluateJavaScript:jsScript completionHandler:^(id _Nullable handlerId, NSError * _Nullable error) {
                NSLog(@"[PTCallbackData.runCallbackScript] JavaScript callback function executed!");
            }];
        });
        
    }
}
@end


// This is the C callback function passed to PDFNetJSWorker
void PTOnMessageCallback(const char* const arg, void* data)
{
    NSLog(@"[PTOnMessageCallback] OnMessageCallback handler is invoked!");
    if (data != NULL) {
        PTCallbackData* cbData = (__bridge PTCallbackData*) data;
        [cbData runCallbackScriptWithArg:[NSString stringWithUTF8String:arg]];
    }
}

@implementation PTMessageHandler {
    NSString* m_tempPath;
    NSString* m_imageRenderBasePath;
    NSString* m_htmlContentPath;
    
    // Ideally this should be a mapping of EventName:PTCallbackData, but for now, because JSWorker only supports
    // one event "onMessage", we keep a single instance of PTCallbackData.
    PTCallbackData* m_callbackData;
}

- (void) initializeJSWorker
{
    @try {
        // Get current paths
        m_tempPath = NSTemporaryDirectory();
        m_imageRenderBasePath = m_tempPath;
        m_callbackData = nil; // delayed initialize until addEventListener message is posted.
        
        const char* tempPathCStr = [m_tempPath UTF8String];
        PJSW_InitializeWithPaths(tempPathCStr, tempPathCStr);
    }
    @catch (NSException* e) {
        NSLog(@"[initializeJSWorker] Error: %@", [e description]);
    }
}

- (void) setHTMLContentPath:(NSString*)htmlPath
{
    m_htmlContentPath = [NSString stringWithString:htmlPath];
}

- (void) userContentController:(WKUserContentController*)userContentController didReceiveScriptMessage:(WKScriptMessage*)message
{
    NSLog(@"[PTMessageHandler.userContentController] WebView posted a message to native code.");
    NSDictionary* messageDict = (NSDictionary*)[message body];
    
    // NOTE: webView can be access from
    // [message webView];
    
    // message must contain the following format:
    // {
    //   "action": "string of the action worker must perform, e.g. postMessage, addEventListener",
    //   "arguments": ["array", "of", "arguments"],
    //   "callback": "window.function.to.call.in.stringified.name",
    //   "callbackArg": "stringified object passed as the first argument to the callback"
    // }
    // "arguments", "callback", and "callbackArg" are required only if the action requires them.
    if (messageDict != nil) {
        NSString* action = (NSString*)[messageDict objectForKey:@"action"];
        NSArray* arguments = (NSArray*)[messageDict objectForKey:@"arguments"];
        NSString* callback = (NSString*)[messageDict objectForKey:@"callback"];
        NSMutableString* callbackString = nil;

        if (callback != nil) {
            callbackString = [[NSMutableString alloc] initWithString:callback];
            NSString* callbackArg = (NSString*)[messageDict objectForKey:@"callbackArg"];
            if (callbackArg == nil) {
                [callbackString appendString:@"(null"];
            }
            else {
                if ([callbackArg length] < 1) {
                    [callbackString appendString:@"''"];
                }
                else {
                    [callbackString appendFormat:@"('%@'", callbackArg];
                }
            }
        }

        if (action != nil) {
            NSLog(@"[PTMessageHandler.userContentController] WebView requested action: \"%@\".", action);
            @try {
                if ([action compare:@"addEventListener"] == NSOrderedSame) {
                    if (m_callbackData == nil && [message webView] != nil) {
                        m_callbackData = [[PTCallbackData alloc] initWithWebView:[message webView]];
                    }
                    
                    // Get the two arguments for addEventListener
                    if ([arguments count] >= 2) {
                        NSString* eventName = (NSString*)[arguments objectAtIndex:0];
                        NSString* callbackName = (NSString*)[arguments objectAtIndex:1];
                        
                        if (m_callbackData != nil) { // This check is needed because the WKWebView instance might be nil.
                            if (eventName != nil && callbackName != nil && [eventName length] > 0 && [callbackName length] > 0) {
                                m_callbackData.JSCallbackFunction = [NSString stringWithString:callbackName];
                                void* cbData = (__bridge void*) m_callbackData; // No need to transfer ownership, PTMessageHandler will deal with it.
                                PJSW_AddEventListenerWithUserData([eventName UTF8String], &PTOnMessageCallback, cbData);
                            }
                        }
                    }
                    else {
                        // This call is missing an argument...
                        NSLog(@"[PTMessageHandler.userContentController] addEventListener message is missing some arguments.");
                    }
                }
                else if ([action compare:@"removeEventListener"] == NSOrderedSame) {
                    // TODO: currently not supported.
                }
                else if ([action compare:@"postMessage"] == NSOrderedSame) {
                    if ([arguments count] >= 1) {
                        NSString* message = (NSString*)[arguments objectAtIndex:0];
                        PJSW_PostMessage([message UTF8String]);
                    }
                }
                else if ([action compare:@"getProcessId"] == NSOrderedSame) {
                    int pid = [[NSProcessInfo processInfo] processIdentifier];
                    if (callbackString != nil) {
                        [callbackString appendFormat:@",%d", pid];
                    }
                }
                else if ([action compare:@"getHTMLContentPath"] == NSOrderedSame) {
                    if (callbackString != nil) {
                        [callbackString appendFormat:@",'%@'", m_htmlContentPath];
                    }
                }
                else if ([action compare:@"getImageRenderBasePath"] == NSOrderedSame) {
                    if (callbackString != nil) {
                        [callbackString appendFormat:@",'%@'", m_imageRenderBasePath];
                    }
                }
                else if ([action compare:@"deleteGeneratedImageRenderFile"] == NSOrderedSame) {
                    if ([m_imageRenderBasePath compare:m_tempPath] == NSOrderedSame) {
                        return;
                    }
                    
                    if ([arguments count] >= 1) {
                        NSString* imageRenderFilePath = (NSString*)[arguments objectAtIndex:0];
                        if (imageRenderFilePath != nil && [imageRenderFilePath length] > 0) {
                            PJSW_DeleteGeneratedImageRenderFileAsync([imageRenderFilePath UTF8String], NULL);
                        }
                    }
                }
                else if ([action compare:@"getTranslationFile"] == NSOrderedSame) {
                    NSString *filepath = [[NSBundle mainBundle] pathForResource:@"HTML/js/html5/Resources/i18n/translation-en" ofType:@"json"];
                    NSString *fileContents = [NSString stringWithContentsOfFile:filepath encoding:NSUTF8StringEncoding error:nil];
                    // need to url encode the translation data so that quotes are encoded
                    fileContents = [fileContents stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
                    
                    [callbackString appendFormat:@",'%@'", fileContents];
                }
                else if ([action compare:@"showOpenFileDialog"] == NSOrderedSame) {
                    // We only do it if a proper callback is provided
                    if ([message webView] != nil && callbackString != nil) {
                        NSOpenPanel* openPanel = [NSOpenPanel openPanel];
                        [openPanel setCanChooseFiles: TRUE];
                        [openPanel setCanChooseDirectories: FALSE];
                        [openPanel setResolvesAliases: TRUE];
                        [openPanel setAllowsMultipleSelection: FALSE];
                        [openPanel setAllowedFileTypes: [NSArray arrayWithObject:@"pdf"]];
                        
                        [openPanel beginWithCompletionHandler:^(NSInteger result) {
                            // We need to capture some variables... all referred capture variables are prefixed with 'b'.
                            WKWebView* bWebView = [message webView];
                            NSMutableString* bCallbackString = [NSMutableString stringWithString:callbackString];
                            if (result == NSFileHandlingPanelOKButton) {
                                NSURL* filePath = [[openPanel URLs] objectAtIndex:0];
                                [bCallbackString appendFormat:@",'%@');", [filePath absoluteString]];
                            }
                            else {
                                // We return an empty result... (or we can send a cancel command to indicate WebViewer of the event).
                                [bCallbackString appendString:@",'');"];
                            }
                            NSLog(@"[PTMessageHandler.userContentController] Invoking callback: %@", bCallbackString);
                            [bWebView evaluateJavaScript:bCallbackString completionHandler:^(id _Nullable handlerId, NSError * _Nullable error) {
                                // Do nothing...
                                NSLog(@"[PTMessageHandler.userContentController] JavaScript function was invoked, and this is a block notify.");
                            }];
                            
                        }];
                        return; // we have to end the execution here because we cannot return anything until
                                // the open panel selects a file...
                    }
                }
                
                WKWebView* webView = [message webView];
                if (webView != nil && callbackString != nil) {
                    // call the javascript function
                    [callbackString appendString:@");"];
                    NSLog(@"[PTMessageHandler.userContentController] Invoking callback: %@", callbackString);
                    [webView evaluateJavaScript:callbackString completionHandler:^(id _Nullable handlerId, NSError * _Nullable error) {
                        NSLog(@"[PTMessageHandler.userContentController] JavaScript function was invoked, and this is a block notify.");
                    }];
                }
            } @catch (NSException* e) {
                NSLog(@"[PTMessageHandler.userContentController] Error: %@", [e description]);
            }
        }
    }
}
@end
