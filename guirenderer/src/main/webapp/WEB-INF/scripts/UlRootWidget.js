(function() {
  var UlRootWidget;

  UlRootWidget = (function() {

    UlRootWidget.prototype.itemClicked = function(entity) {
      return $.getScript('widgets/entity/' + entity.id, function(data, textStatus, jqxhr) {});
    };

    UlRootWidget.prototype.draw = function(jsonObj) {
      var ul,
        _this = this;
      ul = $("<ul class='alt'>");
      $("#rootdiv").append(ul);
      return $.each(jsonObj, function(i, entity) {
        var li;
        li = $("<li>" + entity.name + "</li>");
        ul.append(li);
        return li.bind('click', function(event) {
          return _this.itemClicked(entity);
        });
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
