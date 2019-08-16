
if (!("xh" in window)) {
	window.xh = {};
};
var appElement = document.querySelector('[ng-controller=index]');
var loginUser;
var structure;
xh.load = function() {
	var app = angular.module("app", []);
	app.controller("index", function($scope, $http) {
		$scope.totals=0;
		$scope.mshow=0;
		$scope.voiceTag=0;
		if(xh.getcookie("skin")!=null){
			$('body').attr('id', xh.getcookie("skin"));
		}else{
			$('body').attr('id', "skin-blur-ocean");
		}

        //判断是否登录start
        $.ajax({
            type: 'GET',
            url: "../../getLoginUser",
            async: false,
            dataType: 'json',
            success: function(response){
                console.log("======");
                console.log(response);
                console.log("======");
                $scope.power = response;
                structure = response.structure;
                loginUser = response.username;
                connectWebSocket();
            } ,
            error: function () {
                alert("登录已失效，请重新登录！");
                window.location.href = "../login.html";
                window.parent.location.href = "../login.html";
            }
        });
        //判断是否登录end

        var deviceMap = {0:"接触式接地电阻",1:"非接触式接地电阻",2:"雷电流",3:"温湿度",4:"静电",5:"倾斜度",6:"电气安全",7:"杂散电流",8:"阴极保护",255:"SPD"}
        $scope.getAlarmTop5 = function () {
            $http.get("../../mq/selectAllAlarmInfoNow?start=0&limit="+5+"&structure="+structure).
            success(function(response){
                var dataList = response.items;
                for(var i in dataList){
                    if(dataList[i].type == 1){
                        //设备离线
                        dataList[i]["desc"] = dataList[i].rtu_id+"号RTU"+dataList[i].rtu_channel+"端口下挂的"+dataList[i].devieceId+"号"+deviceMap[dataList[i].deviceType]+"离线";
                    }else if(dataList[i].type == 3){
                        //设备异常
                        dataList[i]["desc"] = dataList[i].rtu_id+"号RTU"+dataList[i].rtu_channel+"端口下挂的"+dataList[i].devieceId+"号"+deviceMap[dataList[i].deviceType]+"异常";
                    }else if(dataList[i].type == 2){
                        //RTU离线
                        dataList[i]["desc"] = dataList[i].rtu_id+"号RTU离线";
                    }
                }
                $scope.alarmList = dataList;
            });
        }
        $scope.getAlarmTop5();

        //查询公告信息
        $scope.getMarquee = function () {
            $http.get("../../selectMarquee").
            success(function(response){
                console.log(response);
                $("#marqueeInfo").val(response.data)
                $("#marqueeShow").text(response.data);
            });
        }
        $scope.getMarquee();

        //修改公告信息
        $scope.updateMarquee = function () {
            var marquee = $("#marqueeInfo").val();
            $http.get("../../updateMarquee?marquee="+marquee).
            success(function(response){
                $("#marqueeShow").text(marquee);
                $("#marqueeInfo").css("display","none");
                $("#marqueeShow").css("display","block");
            });
        }

	});
};

xh.playSound = function() {
    $("#alarmImg").attr('src', 'xhIcon/bell_on.png');
    var audio = document.getElementById("alarm");
    // 重新播放
    audio.currentTime = 0;
    audio.play();
};

xh.stopSound = function() {
    $("#alarmImg").attr('src', 'xhIcon/bell_off.png');
    var audio = document.getElementById("alarm");
    // 停止
    if (audio != null) {
        audio.pause();
        audio.currentTime = 0;
    }
};

/* 获取cookie */
xh.getcookie = function(name) {
	var strcookie = document.cookie;
	var arrcookie = strcookie.split(";");
	for (var i = 0; i < arrcookie.length; i++) {
		var arr = arrcookie[i].split("=");
		if (arr[0].match(name) == name)
			return arr[1];
	}
	return "";
};

xh.showMarqueeEdit = function () {
    $("#marqueeInfo").css("display","block");
    $("#marqueeShow").css("display","none");
}

