window.LOM = {}

LOM.emptyPage = ->
    body  = $("body")
    body.empty()  
    page = $('<div>')
    body.append page
    page

LOM.loadScript = (url, conf) ->
    $.get url, (data, textStatus, jqxhr) ->
        x = eval data
        x.init conf
    , "text"

LOM.getJSON = (url, callback) ->
    $.getJSON url, (jsonObj) =>
        callback(jsonObj)

$ -> 
    LOM.loadScript 'rest/widget/root', {}
    