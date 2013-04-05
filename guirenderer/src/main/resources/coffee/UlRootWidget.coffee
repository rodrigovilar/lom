class UlRootWidget
  draw: (jsonObj) ->
    ul = $("<ul>");
    $("div").append ul
    $.each jsonObj, (i, entity) ->
      ul.append $("<li>" + entity.name + "</li>")

  constructor: ->
    $.getJSON 'entities', (jsonObj) =>
      this.draw(jsonObj)

$ ->
  new UlRootWidget
