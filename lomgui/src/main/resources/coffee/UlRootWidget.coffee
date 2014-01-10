class UlRootWidget
    constructor: ->
        @page = LOM.emptyPage()
        LOM.getJSON 'rest/data/class', (jsonObj) =>
            @drawList(jsonObj)

    drawList: (jsonObj) ->
        ul = $("<ul>");
        @page.append ul
        $.each jsonObj, (i, clazz) =>
            @drawLine(ul, clazz)

    drawLine: (ul, clazz) ->
        li = $("<li>" + clazz.name + "</li>")
        ul.append li
        li.click => 
            alert "Loading widget for #{clazz.name}"
###        
            LOM.loadScript 'rest/widget/clazz/'+ clazz.fullName   
###

new UlRootWidget