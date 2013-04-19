(function() {
  var TableInstanceListing;

  TableInstanceListing = (function() {

    function TableInstanceListing(entityId) {
      var _this = this;
      $.getJSON('entities/' + entityId + '/instances', function(jsonObj) {
        return alert('>>>>' + entityId);
      });
    }

    return TableInstanceListing;

  })();

  $(function() {
    return new TableInstanceListing('##ENTITY_ID##');
  });

}).call(this);
