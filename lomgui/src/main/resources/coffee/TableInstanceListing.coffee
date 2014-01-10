class TableInstanceListing

    init: (conf) ->
        LOM.getJSON "rest/data/class/#{conf.classFullName}/instances", (jsonObj) =>
            @drawTable(jsonObj)
         
    drawTable: (jsonObj) ->
        @page = LOM.emptyPage()
        table = $("<table>")
        @page.append table
        @buildTableHead(jsonObj, table)
        @buildTableBody(jsonObj, table)

    buildTableHead: (jsonObj, table) -> 
        thead = $("<thead>");
        table.append thead
        trHead = $("<tr>");
        thead.append trHead
        $.each jsonObj.attributes, (i, attribute) ->
            thHead = $("<th>#{attribute.name}</th>")                                 
            trHead.append thHead

    buildTableBody: (jsonObj, table) ->
        tbody = $("<tbody>");
        table.append tbody                
        $.each jsonObj.instances, (i, instance) =>
            trbody = $("<tr>")
            tbody.append trbody
            $.each jsonObj.attributes, (i, attribute) =>
                td  = $("<td>#{instance[attribute.name]}</td>" );
                trbody.append td

return new TableInstanceListing