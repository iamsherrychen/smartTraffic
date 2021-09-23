
var pagesat=new Array("中正東路二段","大竹南路599巷");
var pagesa=new Array("ZhongzhengE_s2","DazhuS_ln599");

var ipa=new Array("172.23.49.132","172.23.49.132");

var porta=new Array("8888","8889");

var mjpgpatha=new Array("video3.mjpg","video3.mjpg");

var ZhongzhengE_s2=new Array();
ZhongzhengE_s2[0]="匝道北向國道2(視角往北)";					//H24313701-C1-BL01
ZhongzhengE_s2[1]="匝道北向國道2(視角往南)";					//H24313701-C2-BL02
ZhongzhengE_s2[2]="匝道南向(視角往北)";							//H24313701-A-BL03
ZhongzhengE_s2[3]="中正東二段路口 台31南向(視角往北)";			//H33700301-A-BL04
ZhongzhengE_s2[4]="中正東二段路口前 台31北向快車道(視角往南)";	//H33700301-C1-BL05
ZhongzhengE_s2[5]="中正東二段路口前 台31北向慢車道(視角往南)";	//H33700301-C2-BL06
ZhongzhengE_s2[6]="中正東二段北向路口前(視角往南)";				//H33700301-B-BL07
ZhongzhengE_s2[7]="中正東二段南向路口前(視角往北)";				//H33700301-D-BL08
ZhongzhengE_s2[8]="中正東二段路口 台31南向(路口轉向)";			//H33700301-A-FE01
ZhongzhengE_s2[9]="中正東二段路口 台31北向(路口轉向)";			//H33700301-C-FE02
ZhongzhengE_s2[10]="中正東二段北向路口(路口轉向)";				//H33700301-B1-BL07-1
ZhongzhengE_s2[11]="中正東二段南向路口(路口轉向)";				//H33700301-D-BL08-1

var ZhongzhengE_s2_patha=new Array("11","13","15","21","23","25","27","29","31","33","37","45");

var DazhuS_ln599=new Array();
DazhuS_ln599[0]="大竹南599巷口 台31南向(路口轉向)";				//H33700302-A-FE05
DazhuS_ln599[1]="大竹南599巷口 台31北向(路口轉向)";				//H33700302-C-FE06
DazhuS_ln599[2]="大竹南599巷口 台31南向(視角往北)";				//H33700302-A-BL09
DazhuS_ln599[3]="大竹南599巷口 台31北向快車道(視角往南)";		//H33700302-C1-BL10
DazhuS_ln599[4]="大竹南599巷口 台31北向慢車道(視角往南)";		//H33700302-C2-BL11

var DazhuS_ln599_patha=new Array("51","53","55","57","59");
		
var dida=new Array("deviceid1","deviceid2","deviceid3","deviceid4","deviceid5","deviceid6");

var imga=new Array("mjpg1","mjpg2","mjpg3","mjpg4","mjpg5","mjpg6");

var ch=4;

var rw=2;
var rh=2;

var t=60;  //sec
var ts=60; //sec

var tmps=0;

var ow=40;
var oh=80;

var pid=0;

var agent=navigator.userAgent.toLowerCase(); 
var isEdge = (agent.indexOf("edge/") !=-1);   // Microsoft Edge
var isChrome = (agent.indexOf("chrome") !=-1);   // Chrome

function resizes()
{
   var winwidth=window.innerWidth-ow;
   var winheight=window.innerHeight-oh;
   
   for (var i = 0; i < ch; i++) {
	
    var obj2 = document.getElementById(imga[i]);

	obj2.style.width=winwidth/rw+ 'px';
	obj2.style.height=winheight/rh+ 'px';            
	}
}

function reloads()
{
 document.location.reload();
}