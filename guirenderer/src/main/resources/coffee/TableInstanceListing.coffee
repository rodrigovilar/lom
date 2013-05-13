class TableInstanceListing

	 

	
 
	cleanDiv: (div) ->
		$(div).empty(); 
	 
	buildTableHead: (jsonObj, table) -> 
	 	 
	 	thead = $("<thead>");
	 	trHead = $("<tr>");
	 	$("#datadiv").append table

	 	$.each jsonObj.properties, (i, object) ->
	 		thHead = $("<th>"+object.name+"</th>")	 			
	 		trHead.append thHead
	 		
	 	thead.append trHead
	 	table.append thead

	buildTableBody: (jsonObj, table) ->
	 	 
	 	tbody = $("<tbody>");
	 	
	 	 

	 	$.each jsonObj.instances, (i, values) ->
	 		trbody = $('''<tr   bgcolor="#EEEEEE" onMouseOver="this.bgColor='gold';" onMouseOut="this.bgColor='#EEEEEE';" >''').bind 'click', (event) ->	 		 	

	 			row = $("#tbl tbody tr #"+i ).map(->
	 			 	$row = $(this)) 
	 			 	 
	 			$.each row, (i, columns) ->
	 				
	 				rowName = columns[i]
	 				alert row[rowName] = $(this).text()

	 			
	 		
	 		trbody.attr('id', i)
	 		
	 		$.each values.values, (ii, object) ->
	 			 
	 			td  = $("<td>"+object.value+"</td>" );
	 			td.attr('id', i  )
	 			trbody.append td
	 			tbody.append trbody


	 	table.append tbody		

	draw: (jsonObj) ->
		
		 div = "#datadiv"
		 table = $("<table id='tbl' class='striped tight sortable' border=1 cellspacing='0' cellpadding='0'>");	 	
		
		 @cleanDiv(div) 
	 	@buildTableHead(jsonObj, table)
	 	@buildTableBody(jsonObj, table)


	constructor: (entityId) ->
		 
		$.getJSON 'entities/'+entityId+'/instances', (jsonObj) =>
			@draw(jsonObj)
		  	 


$ ->
  	new TableInstanceListing('##ENTITY_ID##')
