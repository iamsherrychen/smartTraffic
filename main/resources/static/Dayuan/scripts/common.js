
var pagesat=new Array("東匝","橫湳路","平安路","民安路","民生南路");
var pagesa=new Array("DongZha","HengNan","PingAn","MinAn","MinShengS");

var ipa=new Array("172.16.190.100","172.16.190.100","172.16.190.100","172.16.190.100","172.16.190.100");

var porta=new Array("8888","8889","8890","8891","8892");

var mjpgpatha=new Array("video3.mjpg","video3.mjpg","video3.mjpg","video3.mjpg","video3.mjpg");

var DongZha_deviceida=new Array();
DongZha_deviceida[0]="東匝 路口東側(中正東路) 朝西";       //H33700401-A-FE01
DongZha_deviceida[1]="東匝 路口北側(東匝) 朝南";           //H33700401-D-FE02
DongZha_deviceida[2]="東匝 路口東側(中正東路) 朝西";       //H33700401-A-BL03
DongZha_deviceida[3]="東匝 路口東側(中正東路) 朝西";       //H33700401-A-BL04
DongZha_deviceida[4]="東匝 路口北側(東匣) 朝南";           //H33700401-D-BL05
DongZha_deviceida[5]="東匝 路口南側(444巷) 朝北";          //H33700401-B-FE06
DongZha_deviceida[6]="東匝 路口西側(中正東路) 朝東";       //H33700401-C-FE07
DongZha_deviceida[7]="東匝 路口南側(444巷) 朝北";          //H33700401-B-BL08
DongZha_deviceida[8]="東匝 路口西側(中正東路) 朝東";       //H33700401-C-BL09

var DongZha_patha=new Array("11","13","15","17","19","21","23","25","27");

var HengNan_deviceida=new Array();
HengNan_deviceida[0]="橫湳路 路口東側(中正東路) 朝西";      //H33700402-A-FE01
HengNan_deviceida[1]="橫湳路 路口南側(橫湳路) 朝北";        //H33700402-B-FE02
HengNan_deviceida[2]="橫湳路 路口東側(中正東路) 朝西";      //H33700402-A-BL03
HengNan_deviceida[3]="橫湳路 路口東側(中正東路) 朝西";      //H33700402-A-BL04
HengNan_deviceida[4]="橫湳路 路口南側(橫湳路) 朝北";        //H33700402-B-BL05
HengNan_deviceida[5]="橫湳路 路口西側(中正東路) 朝東";      //H33700402-C-FE06
HengNan_deviceida[6]="橫湳路 路口西側(中正東路) 朝東";      //H33700402-C-BL07
HengNan_deviceida[7]="橫湳路 路口西側(中正東路) 朝東";      //H33700402-C-BL08

var HengNan_patha=new Array("41","43","45","47","49","51","53","55");

var PingAn_deviceida=new Array();
PingAn_deviceida[0]="平安路 路口東側(中正東路) 朝西";       //H33701501-A-FE01
PingAn_deviceida[1]="平安路 路口東側(中正東路 )朝西";       //H33701501-A-BL02
PingAn_deviceida[2]="平安路 路口東側(中正東路) 朝西";       //H33701501-A-BL03
PingAn_deviceida[3]="平安路 路口北側(平安路) 朝南";         //H33701501-C-FE04
PingAn_deviceida[4]="平安路 路口西側(中正東路) 朝東";       //H33701501-B-FE05
PingAn_deviceida[5]="平安路 路口北側(平安路) 朝南";         //H33701501-C-BL06
PingAn_deviceida[6]="平安路 路口西側(中正東路) 朝東";       //H33701501-B-BL07
PingAn_deviceida[7]="平安路 路口西側(中正東路) 朝東";       //H33701501-B-BL08

var PingAn_patha=new Array("71","73","75","77","79","81","83","85");

var MinAn_deviceida=new Array();
MinAn_deviceida[0]="民安路 路口東側(中正東路) 朝西";        //H33701502-A-FE01
MinAn_deviceida[1]="民安路 路口東側(中正東路) 朝西";        //H33701502-A-BL02
MinAn_deviceida[2]="民安路 路口北側(民安路) 朝西";          //H33701502-C-BL03
MinAn_deviceida[3]="民安路 路口西側(中正東路) 朝東";        //H33701502-B-FE04
MinAn_deviceida[4]="民安路 路口西側(中正東路) 朝東";        //H33701502-B-BL05
MinAn_deviceida[5]="民安路 路口西側(中正東路) 朝東";        //H33701502-B-BL06

var MinAn_patha=new Array("101","103","105","107","109","111");

var MinShengS_deviceida=new Array();
MinShengS_deviceida[0]="民生南路 路口東側(中正東路) 朝西";  //H33701801-A-FE01
MinShengS_deviceida[1]="民生南路 路口東側(中正東路) 朝西";  //H33701801-A-BL02
MinShengS_deviceida[2]="民生南路 路口北側(民生南路) 朝南";  //H33701801-C-FE03
MinShengS_deviceida[3]="民生南路 路口西側(中正東路) 朝東";  //H33701801-B-FE04
MinShengS_deviceida[4]="民生南路 路口北側(民生南路) 朝南";  //H33701801-C-BL05
MinShengS_deviceida[5]="民生南路 路口西側(中正東路) 朝東";  //H33701801-B-BL06

var MinShengS_patha=new Array("141","143","145","147","149","151");
		
var dida=new Array("deviceid1","deviceid2","deviceid3","deviceid4","deviceid5","deviceid6","deviceid7","deviceid8","deviceid9");

var imga=new Array("mjpg1","mjpg2","mjpg3","mjpg4","mjpg5","mjpg6","mjpg7","mjpg8","mjpg9");

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