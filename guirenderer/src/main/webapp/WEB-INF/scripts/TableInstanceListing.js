(function() {
  var TableInstanceListing;

  TableInstanceListing = (function() {

    TableInstanceListing.prototype.cleanDiv = function(div) {
      return $(div).empty();
    };

    TableInstanceListing.prototype.buildTableHead = function(jsonObj, table) {
      var thead, trHead;
      thead = $("<thead>");
      trHead = $("<tr>");
      $("#datadiv").append(table);
      $.each(jsonObj.properties, function(i, object) {
        var thHead;
        thHead = $("<th>" + object.name + "</th>");
        return trHead.append(thHead);
      });
      thead.append(trHead);
      return table.append(thead);
    };

    TableInstanceListing.prototype.buildTableBody = function(jsonObj, table) {
      var tbody;
      tbody = $("<tbody>");
      $.each(jsonObj.instances, function(i, values) {
        var trbody;
        trbody = $('<tr   bgcolor="#EEEEEE" onMouseOver="this.bgColor=\'gold\';" onMouseOut="this.bgColor=\'#EEEEEE\';" >').bind('click', function(event) {
          var row;
          row = $("#tbl tbody tr #" + i).map(function() {
            var $row;
            return $row = $(this);
          });
          return $.each(row, function(i, columns) {
            var rowName;
            rowName = columns[i];
            return alert(row[rowName] = $(this).text());
          });
        });
        trbody.attr('id', i);
        return $.each(values.values, function(ii, object) {
          var td;
          td = $("<td>" + object.value + "</td>");
          td.attr('id', i);
          trbody.append(td);
          return tbody.append(trbody);
        });
      });
      return table.append(tbody);
    };

    TableInstanceListing.prototype.draw = function(jsonObj) {
      var div, table;
      div = "#datadiv";
      table = $("<table id='tbl' class='striped tight sortable' border=1 cellspacing='0' cellpadding='0'>");
      this.cleanDiv(div);
      this.buildTableHead(jsonObj, table);
      return this.buildTableBody(jsonObj, table);
    };

    function TableInstanceListing(entityId) {
      var _this = this;
      $.getJSON('entities/' + entityId + '/instances', function(jsonObj) {
        return _this.draw(jsonObj);
      });
    }

    return TableInstanceListing;

  })();

  $(function() {
    return new TableInstanceListing('##ENTITY_ID##');
  });

}).call(this);
