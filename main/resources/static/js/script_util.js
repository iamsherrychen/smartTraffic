
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
function MM_showHideLayers() { //v9.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) 
  with (document) if (getElementById && ((obj=getElementById(args[i]))!=null)) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
function MM_effectAppearFade(targetElement, duration, from, to, toggle)
{
	Spry.Effect.DoFade(targetElement, {duration: duration, from: from, to: to, toggle: toggle});
}
function MM_effectSlide(targetElement, duration, from, to, toggle)
{
	Spry.Effect.DoSlide(targetElement, {duration: duration, from: from, to: to, toggle: toggle});
}

function isObj(obj){
	return (!((obj==null)||(obj==undefined)));
}
 

 var progId, progIds = ['MSXML2.XMLHTTP.5.0', 'MSXML2.XMLHTTP.4.0', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP', 'Microsoft.XMLHTTP'];   
   function trim(stringToTrim){ return stringToTrim.replace(/^\s+|\s+$/g,"");}
   function getXMLHttpRequest()  
   {  
    if (!window.ActiveXObject) {  
     return new XMLHttpRequest();  
    } else if (progId != null) {  
     return new ActiveXObject(progId);  
    } else {  
     for (var i = 0; i < progIds.length; i++)  
     {  
      try {  
       return new ActiveXObject(progId = progIds[i]);  
      } catch (ex) {  
       progId = null;  
      }  
     }  
    }  
   }  
   
 function loadClass(src){    
    
	var xmlHttp = getXMLHttpRequest();  
	var s  ="";
	xmlHttp.open("GET",src,false);  
    xmlHttp.onreadystatechange=function()
    {
	 if(xmlHttp.readyState == 4) 
	 {     
		var stus = xmlHttp.status;
		if (stus == 200 || stus == 0 || stus == 304)  
		 {  
			s = trim(xmlHttp.responseText);
		 } 
	 }
	}       
	xmlHttp.send(""); 
	return s;
  } 
//�𢰧�游�硋耦撌亙�瑕�𣇉��綉��
var tmpchoosetool="normal_tools_img_hand";
function changeToolImg(nowchoosetool){
	if(nowchoosetool=="normal_tools_img_hand")
		$("#"+nowchoosetool).attr("src","./img/hand.png");
	if(nowchoosetool=="normal_tools_img_area")
		$("#"+nowchoosetool).attr("src","./img/area.png");
	if(nowchoosetool=="normal_tools_img_distance")
		$("#"+nowchoosetool).attr("src","./img/distance.png");
	
	if (nowchoosetool == tmpchoosetool)
		return;	
				
	if(tmpchoosetool=="normal_tools_img_hand")
		$("#"+tmpchoosetool).attr("src","./img/hand_choose.png");
	if(tmpchoosetool=="normal_tools_img_area")
		$("#"+tmpchoosetool).attr("src","./img/area_choose.png");
	if(tmpchoosetool=="normal_tools_img_distance")
		$("#"+tmpchoosetool).attr("src","./img/distance_choose.png");


	tmpchoosetool = nowchoosetool;

}


//�𢰧�游�硋惜撌亙�瑕�𣇉��綉��
var tmpmaptool="map_png";
function changeMapToolImg(nowmaptool){
	if(nowmaptool=="map_png")
		$("#"+nowmaptool).attr("src","./img/map.png");
	if(nowmaptool=="img_png")
		$("#"+nowmaptool).attr("src","./img/image.png");
	if(nowmaptool=="mix_png")
		$("#"+nowmaptool).attr("src","./img/mix.png");
	
	if (nowmaptool == tmpmaptool)
		return;	
				
	if(tmpmaptool=="map_png")
		$("#"+tmpmaptool).attr("src","./img/map_2.png");
	if(tmpmaptool=="img_png")
		$("#"+tmpmaptool).attr("src","./img/image_2.png");
	if(tmpmaptool=="mix_png")
		$("#"+tmpmaptool).attr("src","./img/mix_2.png");


	tmpmaptool = nowmaptool;

}
//憿舐內閮𦠜��
function showMessage(text, target , time){
	$("#"+target).show();
	$("#rc_msg").html(text);
	$("#"+target).stop();
//	$("#"+target).css("position", "absolute");
//	$("#"+target).css("left",  $("#iTaiwanMaps").width()/2.2);
//	$("#"+target).css("top", $("#iTaiwanMaps").height() );
	$("#"+target).css("width", "auto" );

	$("#"+target).animate({
	    opacity: 1
	  }, 500).animate({
	    opacity: 0
	  }, time, function(){
		  $("#"+target).stop();
		  $("#"+target).hide();
		  $("#"+target).css("opacity", "1" );
	  });
}

function showMessureResult(text, target){
	$("#"+target).css("opacity", "1" );
	$("#"+target).show();
	$("#rc_msg").html(text);
	$("#"+target).stop();
	$("#"+target).css("width", "auto" );

//	$("#"+target).css("position", "absolute");
//	$("#"+target).css("left",$("#iTaiwanMaps").width()/2.2);
//	$("#"+target).css("top", $("#iTaiwanMaps").height() );
//	$("#"+target).css("z-index","1000");
}
//靽格㺿�綉��暺墧�煾�䭾芋撘�
function changeQryMouse(layername,mousetype){
	for (var i=0;i<layername.features.length;i++){	
			layername.features[i].style.cursor = mousetype;		
	}
	layername.redraw();
}
