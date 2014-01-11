class UlRootWidget

    init: (conf) ->
        @page = LOM.emptyPage()
        LOM.getJSON 'rest/data/class', (jsonObj) =>
            @drawList(jsonObj)

    drawList: (jsonObj) ->
        ul = $("<ul>");
        @page.append ul
        $.each jsonObj, (i, clazz) =>
            @drawLine(ul, clazz)

    drawLine: (ul, clazz) ->
        li = $("<li>#{clazz.name}</li>")
        li.attr "id", "class_" + clazz.fullName
        ul.append li
        li.click => 
            LOM.loadScript 'rest/widget/class/'+ clazz.fullName,
                classFullName: clazz.fullName

return new UlRootWidget