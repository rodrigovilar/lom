(function() {

  window.LOM = {};

  LOM.emptyPage = function() {
    var body, page;
    body = $("body");
    body.empty();
    page = $('<div>');
    body.append(page);
    return page;
  };

  LOM.loadScript = function(url, conf) {
    return $.get(url, function(data, textStatus, jqxhr) {
      var x;
      x = eval(data);
      return x.init(conf);
    }, "text");
  };

  LOM.getJSON = function(url, callback) {
    var _this = this;
    return $.getJSON(url, function(jsonObj) {
      return callback(jsonObj);
    });
  };

  $(function() {
    return LOM.loadScript('rest/widget/root', {});
  });

}).call(this);
