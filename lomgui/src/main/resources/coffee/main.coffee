window.LOM = {}

LOM.emptyPage = ->
    body  = $("body")
    body.empty()  
    page = $('<div>')
    body.append page
    page

LOM.loadScript = (url) ->
    $.getScript url, (data, textStatus, jqxhr) ->   

LOM.getJSON = (url, callback) ->
    $.getJSON url, (jsonObj) =>
        callback(jsonObj)

$ -> 
    LOM.loadScript 'rest/widget/root'
    