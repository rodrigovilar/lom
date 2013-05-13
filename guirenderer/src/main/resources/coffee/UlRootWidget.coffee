class UlRootWidget
 
      
  

  itemClicked:(entity) ->
  	$.getScript 'widgets/entity/'+ entity.id, (data, textStatus, jqxhr) ->   
  	 
  draw: (jsonObj) ->


           
    ul = $("<ul class='alt'>");
    $("#rootdiv").append ul
    $.each jsonObj, (i, entity) =>
      li = $("<li>" + entity.name + "</li>")
      
      ul.append li
      
      li.bind 'click', (event) => 
      	
      	this.itemClicked (entity)


  constructor: ->
    $.getJSON 'entities', (jsonObj) =>
      @draw(jsonObj)
      
       
       
$ ->
  new UlRootWidget


