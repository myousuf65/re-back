(function(exports) {

  var rootDOM = exports.parent || exports;
  if (typeof rootDOM.isWK === 'undefined') {
    rootDOM.isWK = function() {
      return rootDOM.webviewer &&
        rootDOM.webviewer.wk &&
        rootDOM.webviewer.wk.worker &&
        rootDOM.webviewer.wk.worker.addEventListener &&
        rootDOM.webviewer.wk.worker.removeEventListener &&
        rootDOM.webviewer.wk.worker.postMessage;
    };
  }

  if (!rootDOM.isWK()) {
    console.log('No WKWebView support, exiting.');
    return;
  }

  console.log('WKWebView support detected, loading workers');

  exports.utils = exports.utils || { };
  exports.utils.isJSWorker = true;
  exports.utils.isWK = true;
  exports.jsworker = {
    loadWorker : function() {
      return rootDOM.webviewer.wk.worker;
    },
    worker: rootDOM.webviewer.wk.worker,
    utils: {
      getHTMLContentPath: rootDOM.webviewer.wk.worker.getHTMLContentPath,
      getFileContents: rootDOM.webviewer.wk.worker.getFileContents
    }
  };

})(window);
