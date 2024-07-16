// Sadly, WKWebView's (Safari's) JavaScript engine does not support ES6 syntax arrow functions (yet).
(function(exports) {
  var utils = {
    arrayBufferToBase64: function(arrayBuffer) {
      // Code from: https://gist.github.com/jonleighton/958841
      var base64    = '';
      var encodings = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';

      var bytes         = new Uint8Array(arrayBuffer);
      var byteLength    = bytes.byteLength;
      var byteRemainder = byteLength % 3;
      var mainLength    = byteLength - byteRemainder;

      var a, b, c, d;
      var chunk;

      // Main loop deals with bytes in chunks of 3
      for (var i = 0; i < mainLength; i = i + 3) {
        // Combine the three bytes into a single integer
        chunk = (bytes[i] << 16) | (bytes[i + 1] << 8) | bytes[i + 2];

        // Use bitmasks to extract 6-bit segments from the triplet
        a = (chunk & 16515072) >> 18; // 16515072 = (2^6 - 1) << 18
        b = (chunk & 258048)   >> 12; // 258048   = (2^6 - 1) << 12
        c = (chunk & 4032)     >>  6; // 4032     = (2^6 - 1) << 6
        d = chunk & 63;               // 63       = 2^6 - 1

        // Convert the raw binary segments to the appropriate ASCII encoding
        base64 += encodings[a] + encodings[b] + encodings[c] + encodings[d];
      }

      // Deal with the remaining bytes and padding
      if (byteRemainder == 1) {
        chunk = bytes[mainLength];

        a = (chunk & 252) >> 2; // 252 = (2^6 - 1) << 2

        // Set the 4 least significant bits to zero
        b = (chunk & 3)   << 4; // 3   = 2^2 - 1

        base64 += encodings[a] + encodings[b] + '==';
      } else if (byteRemainder == 2) {
        chunk = (bytes[mainLength] << 8) | bytes[mainLength + 1];

        a = (chunk & 64512) >> 10; // 64512 = (2^6 - 1) << 10
        b = (chunk & 1008)  >>  4; // 1008  = (2^6 - 1) << 4

        // Set the 2 least significant bits to zero
        c = (chunk & 15)    <<  2; // 15    = 2^4 - 1

        base64 += encodings[a] + encodings[b] + encodings[c] + '=';
      }
      
      return base64;
    },
    base64ToArrayBuffer: function(base64Str) {
      // code from: https://github.com/danguer/blog-examples/blob/master/js/base64-binary.js
      var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
      var removePaddingChars = function(input) {
        var lkey = keyStr.indexOf(input.charAt(input.length - 1));
        if (lkey == 64) {
          return input.substring(0,input.length - 1);
        }
        return input;
      }

      var inputBytes = (base64Str.length / 4) * 3;
      var arrayBuffer = new ArrayBuffer(inputBytes);

      var input = base64Str;
      input = removePaddingChars(input);
      input = removePaddingChars(input);
      input = input.replace(/[^A-Za-z0-9\+\/\=]/g, '');

      var uarray = new Uint8Array(arrayBuffer);


      var bytes = parseInt((input.length / 4) * 3, 10);
      var chr1, chr2, chr3;
      var enc1, enc2, enc3, enc4;
      var j = 0;
      for (var i = 0; i < bytes; i += 3) {  
        //get the 3 octects in 4 ascii chars
        enc1 = keyStr.indexOf(input.charAt(j++));
        enc2 = keyStr.indexOf(input.charAt(j++));
        enc3 = keyStr.indexOf(input.charAt(j++));
        enc4 = keyStr.indexOf(input.charAt(j++));
    
        chr1 = (enc1 << 2) | (enc2 >> 4);
        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
        chr3 = ((enc3 & 3) << 6) | enc4;
    
        uarray[i] = chr1;     
        if (enc3 != 64) uarray[i+1] = chr2;
        if (enc4 != 64) uarray[i+2] = chr3;
      }

      return arrayBuffer;
    }
  };

  var transformArrayBuffersToObject = function(obj) {
    if (typeof obj === 'undefined') {
      return obj;
    }

    // sadly, using instanceof with ArrayBuffer does not work...
    // we have to use obj.toString() === '[object ArrayBuffer]'
    // if (obj instanceof window.ArrayBuffer) {
    if (obj.toString() === '[object ArrayBuffer]') {
      // Originally, we were transforming ArrayBuffer into an array of numbers. This seems to not working very well so
      // we use base64 transformations instead...
      // var buffer = [];
      // var view = new Uint8Array(obj);
      // for (var i = 0; i < view.length; i++) {
      //   buffer.push(view[i]);
      // }

      var buffer = utils.arrayBufferToBase64(obj);
      return {
        t: 'PJSW_ArrayBuffer',
        b: buffer
      };
    } else if (Array.isArray(obj)) {
      var objArr = [];
      for (var i = 0; i < obj.length; i++) {
        objArr.push(transformArrayBuffersToObject(obj[i]));
      }
      return objArr;
    } else if (typeof obj === 'object') {
      var newObj = { };
      var keys = Object.keys(obj);
      for (var i = 0; i < keys.length; i++) {
        var key = keys[i];
        newObj[key] = transformArrayBuffersToObject(obj[key]);
      }
      return newObj
    }

    return obj;
  };

  var transformArrayBuffersFromObject = function(obj) {
    if (typeof obj === 'undefined') {
      return obj;
    }

    if (Array.isArray(obj)) {
      var objArr = [];
      for (var i = 0; i < obj.length; i++) {
        objArr.push(transformArrayBuffersFromObject(obj[i]));
      }
      return objArr;
    } else if (typeof obj === 'object') {
      if (obj.t === 'PJSW_ArrayBuffer' && typeof obj.b === 'string') {
        // var source = obj.b;
        // var buffer = new ArrayBuffer(source.length);
        // var view = new DataView(buffer);
        // for (var i = 0; i < source.length; i++) {
        //   view.setUint8(i, source[i]);
        // }
        var buffer = obj.b;
        return utils.base64ToArrayBuffer(buffer);
      } else {
        var newObj = { };
        var keys = Object.keys(obj);
        for (var i = 0; i < keys.length; i++) {
          var key = keys[i];
          newObj[key] = transformArrayBuffersFromObject(obj[key]);
        }
        return newObj;
      }
    }
    return obj;
  };

  if (exports.webkit &&
      exports.webkit.messageHandlers &&
      exports.webkit.messageHandlers.native &&
      exports.webkit.messageHandlers.native.postMessage) {
    exports.webviewer = exports.webviewer || { };
    exports.webviewer.wk = { };

    // Note: Because there is no way to return from a native call, we passed a callback function name to the native call
    // then the native function will invoke this JavaScript code for the webview.
    exports.webviewer.wk.callbacks = { };
    exports.webviewer.wk.nativeCallback = function(callbackId, args) {
      if (callbackId in exports.webviewer.wk.callbacks) {
        exports.webviewer.wk.callbacks[callbackId](args);
      }
    };

    exports.webviewer.wk.listeners = { };

    exports.webviewer.wk.worker = {
      addEventListener: function(eventName, callback) {
        exports.webviewer.wk.listeners[eventName] = callback;
        exports.webkit.messageHandlers.native.postMessage({
          action: 'addEventListener',
          arguments: [eventName, `window.webviewer.wk.listeners.${eventName}`]
        });
      },
      removeEventListener: function(eventName) {
        // TODO: ...
      },
      postMessage: function(message) {
        const messageObj = transformArrayBuffersToObject(message);
        const messageStr = JSON.stringify(messageObj);
        exports.webkit.messageHandlers.native.postMessage({
          action: 'postMessage',
          arguments: [messageStr]
        });
      },
      getWorkerType: function() {
        return 'wk';
      },
      useFilePathForImageRenders: function() {
        return true;
      },
      getProcessId: function(callback) {
        exports.webviewer.wk.callbacks['getProcessId'] = function(pid) {
          exports.webviewer.wk.worker.pid = pid;
          callback(pid);
        };
        exports.webkit.messageHandlers.native.postMessage({
          action: 'getProcessId',
          arguments: [],
          callback: 'window.webviewer.wk.nativeCallback',
          callbackArg: 'getProcessId'
        });
      },
      getPlatformFormatFilePath: function(filePath) {
        if (filePath.startsWith('file://')) {
          return filePath.substr('file://'.length);
        }
        return filePath;
      },
      generateImageRenderFilePath: function(extraInfo) {
        return `${exports.webviewer.wk.worker.imageRenderBasePath}/PJSW_p${exports.webviewer.wk.worker.pid}_e${extraInfo}_${exports.webviewer.wk.worker.imageRenderNumber++}.${exports.webviewer.wk.worker.imageRenderExtension}`;
      },
      generateNextImageRenderFilePath: function(sourcePath, nextNumber) {
        return `${sourcePath}_n${nextNumber}.${exports.webviewer.wk.worker.imageRenderExtension}`;
      },
      deleteGeneratedImageRenderFile: function(imageRenderFilePath) {
        exports.webkit.messageHandlers.native.postMessage({
          action: 'deleteGeneratedImageRenderFile',
          arguments: [imageRenderFilePath]
        });
      },
      getHTMLContentPath: function(callback) {
        exports.webviewer.wk.callbacks['getHTMLContentPath'] = function(htmlContentPath) {
          callback(htmlContentPath);
        };
        exports.webkit.messageHandlers.native.postMessage({
          action: 'getHTMLContentPath',
          arguments: [],
          callback: 'window.webviewer.wk.nativeCallback',
          callbackArg: 'getHTMLContentPath'
        });
      }
    };

    exports.webviewer.wk.getTranslationFile = function(callback) {
        exports.webviewer.wk.callbacks['getTranslationFile'] = function(result) {
            if (!result || (typeof result !== 'string') || result.trim() === '') {
                callback(new Error('Failed to get translation file.'));
            } else {
                callback(null, result); 
            }
        };
        exports.webkit.messageHandlers.native.postMessage({
            action: 'getTranslationFile',
            arguments: [],
            callback: 'window.webviewer.wk.nativeCallback',
            callbackArg: 'getTranslationFile'
        }); 
    }
 
    exports.webviewer.wk.showOpenFileDialog = function(callback) {
      exports.webviewer.wk.callbacks['showOpenFileDialog'] = function(result) {
        if (!result || (typeof result !== 'string') || result.trim() === '') {
          callback(new Error('Failed to open file.'));
        } else {
          callback(null, result); 
        }
      };
      exports.webkit.messageHandlers.native.postMessage({
        action: 'showOpenFileDialog',
        arguments: [],
        callback: 'window.webviewer.wk.nativeCallback',
        callbackArg: 'showOpenFileDialog'
      });
    };

    // A couple pre-calls to native that are needed for setup
    exports.webviewer.wk.worker.getProcessId(function(pid) {
      console.log('pid: ' + pid);
      exports.webviewer.wk.callbacks['getImageRenderBasePath'] = function(imageRenderBasePath) {
        console.log('imageRenderBasePath: ' + imageRenderBasePath);
        exports.webviewer.wk.worker.imageRenderBasePath = imageRenderBasePath;
        exports.webviewer.wk.worker.imageRenderNumber = 0;
        exports.webviewer.wk.worker.imageRenderExtension = 'bmp';

        // We are ready...
        if (typeof exports.onPDFNetWorkerLoaded === 'function') {
          exports.onPDFNetWorkerLoaded();
        }
      };
      exports.webkit.messageHandlers.native.postMessage({
        action: 'getImageRenderBasePath',
        arguments: [],
        callback: 'window.webviewer.wk.nativeCallback',
        callbackArg: 'getImageRenderBasePath'
      });
    });
  }
})(window);

