(function wkconfig() {
  $(document).on('viewerLoaded', function() {
    $('#printButton').hide();
    $('#fullScreenButton').hide();
    $('#downloadButton').hide();

    window.parent.webviewer.wk.getTranslationFile(function(error, result) {
      if (error) {
          console.warn(error);
      } else {
          var data = JSON.parse(decodeURIComponent(result));
          i18n.addResourceBundle('en', 'translation', data);
          $('body').i18n();
      }
    });

    // override openfile action...
    $('#input-pdf').replaceWith('<span id="input-pdf-span"></span>');
    $('.file-upload').on('click', function() {
      window.parent.webviewer.wk.showOpenFileDialog(function(error, result) {
        if (error) {
          console.warn(error);
        } else {
          console.log(`Opening PDF file: ${result}.`);
          readerControl.loadDocument(result, {
            id: `newDoc${(new Date()).getTime()}`,
            filename: result
          });
        }
      });
    });
  });
})();
