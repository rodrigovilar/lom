(function() {
  var TableInstanceListing;

  TableInstanceListing = (function() {

    function TableInstanceListing() {}

    TableInstanceListing.prototype.init = function(conf) {
      var _this = this;
      return LOM.getJSON("rest/data/class/" + conf.classFullName + "/instances", function(jsonObj) {
        return _this.drawTable(jsonObj);
      });
    };

    TableInstanceListing.prototype.drawTable = function(jsonObj) {
      var table;
      this.page = LOM.emptyPage();
      table = $("<table>");
      this.page.append(table);
      this.buildTableHead(jsonObj, table);
      return this.buildTableBody(jsonObj, table);
    };

    TableInstanceListing.prototype.buildTableHead = function(jsonObj, table) {
      var thead, trHead;
      thead = $("<thead>");
      table.append(thead);
      trHead = $("<tr>");
      thead.append(trHead);
      return $.each(jsonObj.attributes, function(i, attribute) {
        var thHead;
        thHead = $("<th>" + attribute.name + "</th>");
        return trHead.append(thHead);
      });
    };

    TableInstanceListing.prototype.buildTableBody = function(jsonObj, table) {
      var tbody,
        _this = this;
      tbody = $("<tbody>");
      table.append(tbody);
      return $.each(jsonObj.instances, function(i, instance) {
        var trbody;
        trbody = $("<tr>");
        tbody.append(trbody);
        return $.each(jsonObj.attributes, function(i, attribute) {
          var td;
          td = $("<td>" + instance[attribute.name] + "</td>");
          return trbody.append(td);
        });
      });
    };

    return TableInstanceListing;

  })();

  return new TableInstanceListing;

}).call(this);
