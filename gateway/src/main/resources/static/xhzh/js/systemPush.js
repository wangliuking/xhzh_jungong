
if (!("xh" in window)) {
	window.xh = {};
};

toastr.options = {
    "debug" : false,
    "newestOnTop" : false,
    "positionClass" : "toast-top-center",
    "closeButton" : true,
    /* 动态效果 */
    "toastClass" : "animated fadeInRight",
    "showDuration" : "300",
    "hideDuration" : "1000",
    /* 消失时间 */
    "timeOut" : "1000",
    "extendedTimeOut" : "1000",
    "showMethod" : "fadeIn",
    "hideMethod" : "fadeOut",
    "progressBar" : true,
};

var frist = 0;
var appElement = document.querySelector('[ng-controller=xhcontroller]');
var structure;
xh.load = function() {
	var app = angular.module("app", []);
    var pageSize = $("#page-limit").val();
    app.filter('upp', function() { //可以注入依赖
        return function(text) {
            if(text=="" || text==null)
                return "";
            else
                return parseFloat(text);
        };
    });
	
	var pageSize = $("#page-limit").val();
    app.config(['$locationProvider', function ($locationProvider) {
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });
    }]);
	app.controller("xhcontroller", function($scope,$http,$location) {
		$scope.count = "15";//每页数据显示默认值
		$scope.businessMenu=true; //菜单变色

        //判断是否登录start
        $.ajax({
            type: 'GET',
            url: "../../getLoginUser",
            async: false,
            dataType: 'json',
            success: function(response){
                structure = response.structure;
            } ,
            error: function () {
                alert("登录已失效，请重新登录！");
                window.location.href = "../login.html";
                window.parent.location.href = "../login.html";
            }
        });
        //判断是否登录end

        $http.get("../../selectAlarmConf").
        success(function(response){
            $scope.windowMap = response.window;
            $scope.voiceMap = response.voice;
        });

        $scope.save = function (x) {
            var formId = "";
            if(x == 1){
                formId = "windowForm";
            }else if(x == 2){
                formId = "voiceForm";
            }
            var fields = $("#"+formId).serializeArray();
            var f = {};//声明一个对象
            $.each(fields,function(index,field){
                f[field.name] = field.value;//通过变量，将属性值，属性一起放到对象中
            });
            console.log(f);
            var str = JSON.stringify(f);
            $.ajax({
                url : "../../saveAlarmConf",
                contentType : "application/json;charset=utf-8",
                type : 'POST',
                dataType : "json",
                async : true,
                data : str,
                success: function (json) {
                    toastr.success("保存成功", '提示');
                }
            });
        }

	});


};