document.write("<script src=\"https://maps.nlsc.gov.tw/O09/js/api_layer.js\"></"+"script>");

jQuery.support.cors = true;  
var map, tw_00A, tw_96, tw_003B,tw_00B, placemark ,proj4326,mapid,mp,tw_urban;
var placemark_select_control;
var measuredarwcontrol;
var measureControls;
var mapheight; 
var mapwidth; 
var buttons ;
var Lon = 120.9591; 
var Lat = 23.6825; 
var fromProjection = new OpenLayers.Projection("EPSG:4326"); // transform from WGS 1984
var toProjection = new OpenLayers.Projection("EPSG:3857"); // to Spherical Mercator Projection
var serverUrl = "http://maps.nlsc.gov.tw/S_Maps/wmts"
var matrixIds = new Array(20);
    for (var i=0; i<20; ++i) {
        matrixIds[i] = "" + i;
    }

    var language= window.navigator.userLanguage || window.navigator.language;
	language=language.toUpperCase();
	


  //��嘥�见�硋𧑐���,
  function initMap(id,lange) { // id = map div id ,  lange 隤䂿頂嚗� ZH-TW 銝剜���袏N-US �㘚���
    console.log('initMap');
  	mapid = id;
  	mapheight =   $("#"+mapid).height();
  	mapwidth  =   $("#"+mapid).width();
//  	$('#msg_panel').css('left',mapwidth/2);
//  	$('#mapchange').css('left',mapwidth/2.2);
  	OpenLayers.Lang.setCode("zh-TW");
  	var options = { // ��鞱身�綉�����
  		controls : [ new OpenLayers.Control.Navigation(),new OpenLayers.Control.LayerSwitcher()]
  			 ,  projection: toProjection,maxResolution:1222.99245239 , numZoomLevels: 20

  	};
  	map = new OpenLayers.Map(mapid, options);


  	placemark = new OpenLayers.Layer.Vector("位置標記");	
  	map.addLayers( [ placemark ]);
  	mapsadd(map);
    measureTool(); 
    map.setCenter(new OpenLayers.LonLat(Lon,Lat).transform(fromProjection,toProjection), 8);
    map.addControl(new OpenLayers.Control.nlsclink());
  	map.addControl(new OpenLayers.Control.ScaleLine());
  	map.addControl(new OpenLayers.Control.nlscButton());
	if (isObj($("#msg_panel")))
  	{
  		$("#msg_panel").remove();
  	}

	
  	map.addControl(new OpenLayers.Control.nlscPanel());
  	//map.addControl(new OpenLayers.Control.nlscEnChange());
  	mp=new OpenLayers.Control.MousePosition({alwaysZoom:true,displayProjection:fromProjection});
  	mp.position=new OpenLayers.Pixel(1,mapheight-15);
  	map.addControl(mp);
	var pan = new OpenLayers.Control.PanZoomBar();
	pan.position=new OpenLayers.Pixel(0,10);
	map.addControl(pan);
  	vectorCtrlTool()
  	changeMapToolImg('map_png');

  	if (isObj(lange))
  	{
  		language = lange;
  	}
  	if(language.indexOf("ZH") < 0 && language.indexOf("JA") < 0 )
  	{
  		lange = "EN-US";
  		map.setBaseLayer(EMAP8_B);
  		$(".olControlnlscButton").hide();
  		$(".olControlScaleLine").hide();
  		$("#change_but").text(">>銝剜����");
  	}

  	
//  	if(textfilename!="")
//  		createPlacemarkFromText(textfilename);
  }

var pos_id = 0;
//撱箇�见�帋�滚�𣇉內
function createPlacemark(type,lon,lat,txt,icon,vcolor,url,tip_txt){
  console.log('createPlacemark');
	var	posIconStyle = {	//icon璅���	
		externalGraphic: './img/'+icon,
		pointRadius: 14 ,
		label: txt,
		fontSize: "16px",
		fontColor:trim(vcolor),
		fontFamily: "Courier New, monospace",
		fontWeight: "bold",
		labelYOffset: 23
	};
	if (!isObj(url))
	{
		url = "";
	}
	var posIconAttribute ={
		'pos_id':pos_id,	//憛怠�亙惇�扳����
		'tip_txt':tip_txt,	//憛怠�亙惇�扳����
		'pos_url':url	//憛怠�亙惇�扳����
	};
	var pointGeometry = new OpenLayers.Geometry.Point(lon, lat).transform(fromProjection,toProjection);
	var point = new OpenLayers.Feature.Vector(pointGeometry, posIconAttribute, posIconStyle);
	placemark.addFeatures([point]);
	pos_id++;

}
//�綉��暺墧綉��
function vectorCtrlTool(){
	placemark_select_control = new OpenLayers.Control.SelectFeature(
    		placemark, {
				multiple: false,
				toggle: true,
				hover: true,
				toggleKey: 'ctrlKey',
				clickFeature: function (e) { 
							vectorsTitleLink(e);
						} 	
			}
		);
   
	map.addControls([placemark_select_control]);
	placemark.events.register('featureselected', this, placemark_selected_feature);
	placemark.events.register('featureunselected', this, onFeatureUnselect);

}


function onFeatureUnselect() {
	map.removePopup(popup);
	popup = null;
} 


//��碶�𢠃�鮋�豢綉��暺硺�衤辣
function placemark_selected_feature(event){
	var pos_url = event.feature.attributes.pos_url;
	var lonlat = new OpenLayers.LonLat(event.feature.geometry.x,event.feature.geometry.y).transform(toProjection,fromProjection);
	var pos_id = event.feature.attributes.pos_id;
	var selectedText = event.feature.attributes.tip_txt;
	addMapPopup("Popup_"+pos_id,lonlat.transform(fromProjection,toProjection),selectedText);
	
}

function vectorsTitleLink(event){
	var pos_url = event.attributes.pos_url;
	if (pos_url!="")
	{
		window.open(pos_url);
	}
	
}




//暺鮋�豢䰻閰ａ�钅��
function mapPointQuery(type){
	if(type == "on"){
		placemark_select_control.activate();
		changeQryMouse(placemark,"pointer");
	}
	if(type == "off"){
		placemark_select_control.deactivate();
		changeQryMouse(placemark,"");
	}
}


function addMapPopup(mappopupid,mappopuplonlat,mappopuptxt){
   popup = new OpenLayers.Popup.FramedCloud(mappopupid,
		mappopuplonlat,
	    null,
	    mappopuptxt,
	    null, true, "");
	popup.closeOnMove = false;
	popup.panMapIfOutOfView = false; 
	popup.autoSize = true;
	map.addPopup(popup);
}



//霈���𩣱xt瑼𥪜遣蝡见�帋�滚�𣇉內
function createPlacemarkFromText(textfilename){
  $("#msg_panel").animate({
	opacity: 1
  }, 500).animate({
	opacity: 0
  }, 1000, function(){
		 var data =  loadClass("./"+textfilename);   
		if (data==null||data=="undefined")
			return;
		else{
			var lines = data.split("\n");
			for (var i=0;i<lines.length;i++){
				var pointcontent=lines[i].split(",");
				if(pointcontent.length==6){
					createPlacemark(pointcontent[0],pointcontent[1],pointcontent[2],pointcontent[3],pointcontent[4],pointcontent[5]);
				}
				if(pointcontent.length==5){
					createPlacemark(pointcontent[0],pointcontent[1],pointcontent[2],pointcontent[3],pointcontent[4],"#DC7100");
				}

			};
		}
//		if(type=='Center')
//			map.setCenter(new OpenLayers.LonLat(lon, lat).transform(fromProjection,toProjection), 18);
		map.zoomToExtent(placemark.getDataExtent());
		mapPointQuery("on")

  });
}

//霈���馛son瑼𥪜遣蝡见�帋�滚�𣇉內
function createPlacemarkFromJSon(textfilename){
  console.log('createPlacemarkFromJSon');
  $("#msg_panel").animate({
	opacity: 1
  }, 0).animate({
	opacity: 0
  }, 500, function(){
		$.each(markdata.list, function(i,item){
			createPlacemark(item.id,item.lon,item.lat,item.label,item.pic,item.color,item.url,item.tooltips);
		});
		// map.zoomToExtent(placemark.getDataExtent());
		mapPointQuery("on")

  });
}

//��𤩺葫撌亙��
function measureTool(){
	
	var measureSymbolizers = {	// 摰𡁶儔��𤩺葫蝺𠾼��憭𡁻�𠰴耦璅���
		"Point": {
		    pointRadius: 4,
		    graphicName: "square",
		    fillColor: "white",
		    fillOpacity: 1,
		    strokeWidth: 1,
		    strokeOpacity: 1,
		    strokeColor: "red"
		},	
		"Line": {
			strokeWidth: 3,
			strokeOpacity: 1,
			strokeColor: "red",
			strokeDashstyle: "dash"
		},
		"Polygon": {
			strokeWidth: 3,
			strokeOpacity: 1,
			strokeColor: "red",
			strokeDashstyle: "dash",
			fillColor: "red",
			fillOpacity: 0.3
		}
	};
	var drawSymbolizers = {	// �䰻閰Ｙ���滚�𡁻�𠰴耦璅���
		"Polygon": {
			strokeWidth: 2,
			strokeOpacity: 1,
			strokeColor: "red",
			strokeDashstyle: "solid",
			fillColor: "red",
			fillOpacity: 0.3
		}	
	};
	//��𤩺葫��璅���
    var stylemeasure = new OpenLayers.Style();
    stylemeasure.addRules([ new OpenLayers.Rule({symbolizer: measureSymbolizers})]);
    var stylemeasureMap = new OpenLayers.StyleMap({"default": stylemeasure}); 
    //蝜芾ˊ�䰻閰Ｙ���齿見撘�
    var styledraw = new OpenLayers.Style();
    styledraw.addRules([ new OpenLayers.Rule({symbolizer: drawSymbolizers})]);
    var styledrawMap = new OpenLayers.StyleMap({"default": styledraw}); 
    measureControls = {
        line: new OpenLayers.Control.Measure(
            OpenLayers.Handler.Path, {
                persist: true,
                handlerOptions: {
                    layerOptions: {styleMap: stylemeasureMap}
                }
            }
        ),
        polygon: new OpenLayers.Control.Measure(
            OpenLayers.Handler.Polygon, {
                persist: true,
                handlerOptions: {
                    layerOptions: {styleMap: stylemeasureMap}
                }
            }
        ),
        RegularPolygon: new OpenLayers.Control.Measure(
                OpenLayers.Handler.RegularPolygon, {
                    persist: true,
                    handlerOptions: {
                        layerOptions: {styleMap: styledrawMap},
                        sides: 4,
                        irregular: true,
                        angle: 0
                    }
                }
            ),
        Circle: new OpenLayers.Control.Measure(
	        OpenLayers.Handler.RegularPolygon,{
                    persist: true,
                    handlerOptions: {
                        layerOptions: {styleMap: styledrawMap},
                        sides: 35,
                        snapAngle: parseFloat("90&deg;"),
                        angle: 0
                    }
                }
            )

    };
    for(var key in measureControls) {	//��𤩺葫�綉�����
    	measuredarwcontrol = measureControls[key];
    	measuredarwcontrol.events.on({
            "measure": handleMeasurements,
            "measurepartial": handleMeasurements
        });
        map.addControl(measuredarwcontrol);

    }
    

}

//��𤩺葫璅∪�誩ế�𪃾
function toggleControl(element) {
    for(key in measureControls) {
        control = measureControls[key];   

        if(element == key) {
            control.activate();
        } else {
            control.deactivate();
        }
    }
}

var query_box;	//�脣�䀹��貊����
//憿舐內��𤩺葫蝯鞉�� 


function handleMeasurements(event) {	
    var geometry = event.geometry;
	
    query_box = geometry;

	var units = event.units;   
    var order = event.order;
    var measure = event.measure;
    var out = "";
    if(order == 1) {
    	if (units == "m"){
    		out += "����頝嗪𣪧: " + measure.toFixed(3) + " �砍偕";
		}
    	if (units == "km"){
    		out += "����頝嗪𣪧: " + measure.toFixed(3)*1000 + " �砍偕";
		}
    } else {
    	if (units == "m"){
    		out += "�����𢒰蝛�: " + measure.toFixed(3) + " 撟單䲮�砍偕";
			select_Area  =  measure.toFixed(3);
		}
    	if (units == "km"){
    		out += "�����𢒰蝛�: " + measure.toFixed(3)*1000000 + " 撟單䲮�砍偕";
			select_Area  =   measure.toFixed(3)*1000000 ;
		}
		
    }
    	showMessureResult(out,"msg_panel");
}

//����𥕦𧑐���
function checkmapmode(mapmode,mapname){	
	var len = map.layers.length;
	var isadd = false;
	for(var s=0; s< len; s++){
		if (mapname==map.layers[s])
		{
			isadd = true;
			break;
		}
	}
	if (!isadd)
	{
		map.addLayers([mapmode]);
	}
	if (isObj(EMAP2))
	{
			EMAP2.setVisibility(mapname == "EMAP2");

	}
	if (mapname == "EMAP2"){
		if (isObj(PHOTO2_B))
		{
		map.setBaseLayer(PHOTO2_B);
		}
		
	}else{
		if (isObj(PHOTO2_B))
		{
		map.setBaseLayer(mapmode);
		}
	}
		
}

function chk_layertype(layerid,layername){
		for(var s=0; s< len; s++){
			if (mapname==map.layers[s])
			{
				isadd = true;
				break;
			}
		}
		if (!isadd)
		{
			map.addLayers([layerid]);
		}
		if(document.getElementById(layername).checked)
		{
			layerid.setVisibility(true);
		}
		else{
			layerid.setVisibility(false);
		}
}

function chk_language(){
	if(language=="ZH-TW")
	{
		language = "EN-US";
		map.setBaseLayer(EMAP8_B);
		$(".olControlnlscButton").hide();
		$(".olControlScaleLine").hide();
		$("#change_but").text(">>銝剜����");
	}
	else{
		language = "ZH-TW";
		map.setBaseLayer(EMAP_B);
		$(".olControlnlscButton").show();
		$(".olControlScaleLine").show();
		$("#change_but").text(">>English");
	}
}
