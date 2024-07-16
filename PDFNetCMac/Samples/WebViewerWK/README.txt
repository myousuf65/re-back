This is a demo showing how to create a native Mac OS PDFViewCtrl using WKWebView. 

Simply open the XCode project (located at ./WebViewerWK.xcodeproj) and click the run button. 

If you get linker errors when you attempt to build it, make sure that the following files are present in WebViewerWK/Libraries/lib:
	- libPDFNetJSWorker.dylib
	- libPDFNetC.dylib (this file is supposed to be copied from ../../Lib by a script that gets run automatically by XCode when you try to build)

If the application runs but the window is blank, check that the following are present in ./WebViewerWK/HTML/js:
	- html5
	- WebViewer.js
	- WebViewer.min.js
	
If you get a PDFError, note that ./WebViewerWK/HTML/js/index.js contains a line which reads:
	readerControlFile = `${readerControlFile}#d=${encodeURIComponent('newsletter.pdf')}`;
This points to the destination, relative to the directory containing the compiled executable, where the input file to the viewer is sought by the viewer at runtime. In order that this file may be found by the viewer, there is a script in the XCode project which will copy the file "../TestFiles/newsletter.pdf" to the executable's working directory at build-time.