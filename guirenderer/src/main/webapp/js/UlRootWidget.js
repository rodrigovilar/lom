(function() {
  var UlRootWidget;

  UlRootWidget = (function() {

    UlRootWidget.prototype.draw = function(jsonObj) {
      var ul;
      ul = $("<ul>");
      $("div").append(ul);
      return $.each(jsonObj, function(i, entity) {
        return ul.append($("<li>" + entity.name + "</li>"));
      });
    };

    function UlRootWidget() {
      var _this = this;
      $.getJSON('entities', function(jsonObj) {
        return _this.draw(jsonObj);
      });
    }

    return UlRootWidget;

  })();

  $(function() {
    return new UlRootWidget;
  });

}).call(this);
