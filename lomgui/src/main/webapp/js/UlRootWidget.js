(function() {
  var UlRootWidget;

  UlRootWidget = (function() {

    function UlRootWidget() {
      var _this = this;
      this.page = LOM.emptyPage();
      LOM.getJSON('rest/data/class', function(jsonObj) {
        return _this.drawList(jsonObj);
      });
    }

    UlRootWidget.prototype.drawList = function(jsonObj) {
      var ul,
        _this = this;
      ul = $("<ul>");
      this.page.append(ul);
      return $.each(jsonObj, function(i, clazz) {
        return _this.drawLine(ul, clazz);
      });
    };

    UlRootWidget.prototype.drawLine = function(ul, clazz) {
      var li,
        _this = this;
      li = $("<li>" + clazz.name + "</li>");
      ul.append(li);
      return li.click(function() {
        return alert("Loading widget for " + clazz.name);
      });
    };

    return UlRootWidget;

  })();

  /*        
              LOM.loadScript 'rest/widget/clazz/'+ clazz.fullName
  */


  new UlRootWidget;

}).call(this);
