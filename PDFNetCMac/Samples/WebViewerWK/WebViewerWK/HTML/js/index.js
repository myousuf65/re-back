(function run(exports) {
  exports.onPDFNetWorkerLoaded = function() {
    console.log('[index.js] Creating a new WebViewer UI control.');
    exports.webviewer.wk.worker.getHTMLContentPath(function(htmlPath) {
      console.log(`[index.js] HTML path is: "${htmlPath}"`);
      var readerControlFile = `${htmlPath}${htmlPath.endsWith('/') ? '' : '/'}js/html5/ReaderControl.html`;
      console.log(`[index.js] ReaderControl.html is at: "${readerControlFile}"`);

      readerControlFile = `${readerControlFile}#d=${encodeURIComponent('newsletter.pdf')}`;
      readerControlFile = `${readerControlFile}&did=test`;
      readerControlFile = `${readerControlFile}&a=1`;
      readerControlFile = `${readerControlFile}&pdf=jsworker`;
      readerControlFile = `${readerControlFile}&filepicker=1`;
      readerControlFile = `${readerControlFile}&preloadWorker=1`;
      readerControlFile = `${readerControlFile}&pdfnet=0`;
      readerControlFile = `${readerControlFile}&toolbar=true`;
      readerControlFile = `${readerControlFile}&pageHistory=1`;
      readerControlFile = `${readerControlFile}&config=../config.js`;
      console.log(`[index.js] Redirecting to: "${readerControlFile}"`);
      window.location.href = readerControlFile;
    });
  }
})(window);
