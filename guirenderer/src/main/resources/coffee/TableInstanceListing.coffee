class TableInstanceListing

	constructor: (entityId) ->
		
		$.getJSON 'entities/'+entityId+'/instances', (jsonObj) =>
			alert  '>>>>'+ entityId



$ ->
  	new TableInstanceListing('##ENTITY_ID##')
