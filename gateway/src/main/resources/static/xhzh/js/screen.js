/**
 * 用户管理
 */
if (!("xh" in window)) {
    window.xh = {};
};
require.config({
    paths : {
        echarts : 'lib/echarts'
    }
});

var appElement = document.querySelector('[ng-controller=screen]');
var structure;
var responseData;
xh.load = function() {
    var app = angular.module("app", []);

    app.filter('changeValueTwo', function() { //可以注入依赖
        return function(text) {
            var i = parseFloat(text);
            var a = Math.round(i*100);
            return a/100;
        };
    });

    app.controller("screen", function($scope, $http) {
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

        $scope.showSite = function(id){
            $http.get("../../connect/selectAllRTUDraw?site_id="+id).
            success(function(response){
                console.log("^^^^^^^^^^^^^^^^^^")
                console.log(response);
                $scope.siteInfo = response.siteInfo;
                $scope.rtuList = response.items;
                $scope.siteDetail = true;
                $scope.rtuDetail = false;
                $('#detail').modal('show');
            });
        }

        $scope.sendUrl = function(id){
            console.log(id);
            $http.get("../../connect/rtuDraw?id="+id).
            success(function(response){
                console.log("************************")
                console.log(response)
                console.log("************************")
                $scope.rtuData = response.rtuInfo;
                $scope.rtuHealth = response.health.score;
                $scope.rtuRisk = response.risk.score;
                $scope.spdStatus = response.spdStatus;
                $scope.etcrStatus = response.etcrStatus;
                var tempHealth = response.health.detail;
                var resHealth = $scope.analysisMethod(tempHealth);
                var spdHealthList = resHealth.spd;
                var etcrHealthList = resHealth.etcr;
                var tempRisk = response.risk.detail;
                var resRisk = $scope.analysisMethod(tempRisk);
                var spdRiskList = resRisk.spd;
                var etcrRiskList = resRisk.etcr;

                $scope.spdHealthList = spdHealthList.sort(function(a,b){
                    return a.number-b.number})
                $scope.spdRiskList = spdRiskList.sort(function(a,b){
                    return a.number-b.number})
                $scope.etcrHealthList = etcrHealthList.sort(function(a,b){
                    return a.number-b.number})
                $scope.etcrRiskList = etcrRiskList.sort(function(a,b){
                    return a.number-b.number})

                $scope.siteDetail = false;
                $scope.rtuDetail = true;
                $('#detail').modal('show');
            });
        }

        $scope.analysisMethod = function(temp){
            var spdList = [];
            var etcrList = [];
            var detailArr = temp.replace(/\s*/g,"").replace("[","").replace("]","").split(",");
            console.log(detailArr)
            for(var i=0;i<detailArr.length;i++){
                var arrFirst = detailArr[i].split(":");
                var arrSecond = arrFirst[0].split("_");
                if(arrSecond[0] == "spd"){
                    spdList.push({"number":parseInt(arrSecond[2]),"value":arrFirst[1]});
                }else if(arrSecond[0] == "rst"){
                    etcrList.push({"number":parseInt(arrSecond[3]),"value":arrFirst[1]});
                }
            }
            return {"spd":spdList,"etcr":etcrList};
        }

        xh.initTotal();
        //setInterval(xh.initTotal, 30000);

    });
};

xh.initTotal = function(){
    console.log(structure);
    $.ajax({
        url : '../../total/selectIndexData?structure='+structure,
        type : 'GET',
        dataType : "json",
        async : false,
        success : function(response) {
            var siteNum = response.siteNum;
            var siteOffLine = response.siteOffLine;
            var siteOnNum = response.siteOnNum;
            var rtuNum = response.rtuNum;
            var rtuOnNum = response.rtuOnNum;
            var rtuOffLine = response.rtuOffLine;
            var spdNum = response.spdNum;
            var spdOff = response.spdOff;
            var etcrNum = response.etcrNum;
            var etcrOff = response.etcrOff;
            var deviceTotalNum = response.deviceTotalNum;
            var siteWarningNum =response.siteWarningNum;
            var rtuWarningNum = response.rtuWarningNum;
            var etcrWarningNum = response.etcrWarningNum;
            var data = response.num;
            var alarmNowNum = response.alarmNowNum;
            var etcrBottomNum = response.etcrBottomNum;

            $("#siteNum").html(siteOnNum+" / "+siteNum);
            $("#rtuNum").html(rtuOnNum+" / "+rtuNum);
            $("#deviceNum").html(deviceTotalNum+" / "+deviceTotalNum);

            $("#siteOffNum").html(siteOffLine+" - "+(siteNum-siteOffLine-siteWarningNum));
            $("#rtuOffNum").html(rtuOffLine+" - "+(rtuNum-rtuOffLine-rtuWarningNum));
            $("#spdOffNum").html(spdOff+" - "+(spdNum-spdOff));
            $("#etcrOffNum").html(etcrOff+" - "+(etcrNum-etcrOff-etcrWarningNum));

            $("#siteWarningNum").html(siteWarningNum+" - "+(siteNum-siteWarningNum));
            $("#rtuWarningNum").html(rtuWarningNum+" - "+(rtuNum-rtuWarningNum));
            $("#spdWarningNum").html(spdOff+" - "+(spdNum-spdOff));
            $("#etcrWarningNum").html(etcrWarningNum+" - "+(etcrNum-etcrWarningNum));

            $("#alarmNum").html(alarmNowNum);

            $("#spdNum").html(spdNum);
            $("#etcrNum").html(etcrNum);
            $("#otherNum").html(0);
            $("#etcrBottomNum").html(etcrBottomNum);
            $("#strayBottomNum").html(0);

            xh.call(data);

            $.ajax({
                url : '../../data/healthTop5',
                type : 'GET',
                dataType : "json",
                async : false,
                success : function(response) {
                    responseData = response;
                    xh.systemSource(response);
                    xh.deviceWarningTop5(response);
                    xh.deviceOffTop5(response);
                }
            });

            $.ajax({
                url : '../../connect/selectAllSite?start=&limit=&structure='+structure,
                type : 'GET',
                dataType : "json",
                async : false,
                success : function(response) {
                    xh.map(response.items);
                }
            });
        }
    });
}

var provinceJson = {"北京市":11,"天津市":12,"河北省":13,"山西省":14,"内蒙古自治区":15,"辽宁省":21,"吉林省":22,"黑龙江省":23,"上海市":31,"江苏省":32,"浙江省":33,"安徽省":34,"福建省":35,"江西省":36,"山东省":37,"河南省":41,"湖北省":42,"湖南省":43,"广东省":44,"广西壮族自治区":45,"海南省":46,"重庆市":50,"四川省":51,"贵州省":52,"云南省":53,"西藏自治区":54,"陕西省":61,"甘肃省":62,"青海省":63,"宁夏回族自治区":64,"新疆维吾尔自治区":65,"台湾省":71,"香港特别行政区":81,"澳门特别行政区":82,"南海诸岛":460300};
var cityPosition = {"北京市":["116.405289","39.904987"],"天津市":["117.190186","39.125595"],"河北省":["114.502464","38.045475"],"山西省":["112.549248","37.857014"],"内蒙古自治区":["111.751990","40.841490"],"辽宁省":["123.429092","41.796768"],"吉林省":["125.324501","43.886841"],"黑龙江省":["126.642464","45.756966"],"上海市":["121.472641","31.231707"],"江苏省":["118.76741","32.041546"],"浙江省":["120.15358","30.287458"],"安徽省":["117.283043","31.861191"],"福建省":["119.306236","26.075302"],"江西省":["115.892151","28.676493"],"山东省":["117.000923","36.675808"],"河南省":["113.665413","34.757977"],"湖北省":["114.298569","30.584354"],"湖南省":["112.982277","28.19409"],"广东省":["113.28064","23.125177"],"广西壮族自治区":["108.320007","22.82402"],"海南省":["110.199890","20.044220"],"重庆市":["106.504959","29.533155"],"四川省":["104.065735","30.659462"],"贵州省":["106.713478","26.578342"],"云南省":["102.71225","25.040609"],"西藏自治区":["91.11450","29.644150"],"陕西省":["108.948021","34.263161"],"甘肃省":["103.834170","36.061380"],"青海省":["101.777820","36.617290"],"宁夏回族自治区":["106.232480","38.486440"],"新疆维吾尔自治区":["87.616880","43.826630"],"台湾省":["121.50","25.03"],"香港特别行政区":["114.165460","22.275340"],"澳门特别行政区":["113.549130","22.198750"]};
xh.map=function(data){
    var $scope = angular.element(appElement).scope();
    //console.log(data);
    // 设置容器宽高
    var height=document.documentElement.clientHeight;
    var width=document.documentElement.clientWidth;
    var resizeBarContainer = function() {
        $("#map").width(847);
        $("#map").height(773);
    };
    resizeBarContainer();

    // 基于准备好的dom，初始化echarts实例
    var chart = null;
    if (chart != null) {
        chart.clear();
        chart.dispose();
    }

    require([ 'echarts', 'echarts/chart/map' ], function(ec) {
        chart = ec.init(document.getElementById('map'));
        require('echarts/util/mapData/params').params.CN = {
            getGeoJson: function (callback) {
                $.getJSON('lib/echarts/util/mapData/params/china.json',callback);
            }
        }
        var option = {
            backgroundColor: '',
            color: ['gold','aqua','lime'],
            textStyle:{
                color:'#fff'
            },
            tooltip : {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    //console.log(params)
                    if(params.value != ""){
                        return params.name+'<br />'+"站点数量:"+params.value;
                    }else{
                        return params.name;
                    }
                },
                textStyle:{
                    color:"#fff",
                    fontSize:18,
                    fontWeight:"bold"
                }
            },
            series : [
                {
                    name: '全国',
                    type: 'map',
                    roam: false,
                    hoverable: true,
                    mapType: 'CN',
                    itemStyle:{
                        normal:{
                            borderColor:"#fff",
                            label:{
                                show:true,
                                textStyle:{
                                    color:'#fff',
                                    fontSize:11
                                }
                            } ,
                            borderWidth:1,
                            areaStyle:{
                                /*color: '#1b1b1b',*/
                                color:'#072631',
                                visibility: 'off'
                            },

                            emphasis:{label:{show:true}}
                        }
                    },
                    geoCoord: {

                    },
                    data:[],
                    markPoint:{
                        clickable: true,
                        symbol:'pin',//'image://images/标注_l.png',
                        symbolSize : function (v){
                            return 3
                        },
                        effect : {
                            show: false,
                            type: "bounce"
                        },
                        itemStyle:{
                            normal:{
                                color:"#25deff",
                                areaStyle:{
                                    color:"#fff",
                                    type:"default"
                                },
                                label:{
                                    show:true,
                                    textStyle:{
                                        color:"#fff",
                                        baseline:"bottom",
                                        fontSize:16,
                                        fontWeight:"bold"
                                    }
                                }
                            },
                            emphasis: {
                                color:"#25deff",
                                areaStyle:{
                                    color:"#fff",
                                    type:"default"
                                },
                                label:{
                                    show:true,
                                    textStyle:{
                                        color:"#fff",
                                        baseline:"bottom",
                                        fontSize:16,
                                        fontWeight:"bold"
                                    }
                                }
                            }
                        },
                        data : []
                    }

                }
            ]
        };
        var provinceInit = [];
        var cityInit = [];
        for(var i in data){
            option.series[0].geoCoord[data[i].site_name] = [data[i].site_lng, data[i].site_lat];
            option.series[0].markPoint.data.push({name:data[i].site_name,site_id:data[i].site_id});
            provinceInit.push(data[i].site_province);
            cityInit.push(data[i].site_city);
        }

        var provinceTotal = totalMapEl(provinceInit);
        var cityTotal = totalMapEl(cityInit);

        for(var key in provinceTotal){
            option.series[0].data.push({name:key,value:provinceTotal[key]});
        }

        for(var key in cityTotal){
            option.series[0].data.push({name:key,value:cityTotal[key]});
        }

        chart.setOption(option);

        //点击事件,根据点击某个省份计算出这个省份的数据
        chart.on('dblclick', function (params) {
            option.series[0].data = [];
            for(var key in provinceTotal){
                option.series[0].data.push({name:key,value:provinceTotal[key]});
            }
            for(var key in cityTotal){
                option.series[0].data.push({name:key,value:cityTotal[key]});
            }
            var area = provinceJson[params.data.name];
            var chooseArea;
            if(!area){
                chooseArea = "china";
            }else{
                chooseArea = area;
            }
            for(var i in data){
                option.series[0].geoCoord[data[i].site_name] = [data[i].site_lng, data[i].site_lat];
                option.series[0].markPoint.data.push({name:data[i].site_name});
            }
            //逻辑控制
            chart.clear();
            require('echarts/util/mapData/params').params.CN = {
                getGeoJson: function (callback) {
                    $.getJSON('lib/echarts/util/mapData/params/'+chooseArea+'.json',callback);
                }
            }
            chart.setOption(option);
        });

        //点击事件,根据点击某个省份计算出这个省份的数据
        chart.on('click', function (params) {
            if(params.data.x){
                console.log("*****************************")
                console.log(params);
                console.log("*****************************")
                $scope.showSite(params.data.site_id);
            }

        });

    });

}

/**
 * 统计地图上的省份和城市站点数量
 * @param param
 * @returns {*}
 */
function totalMapEl(param){
    var temp = param.reduce(function (allNames, name) {
        if (name in allNames) {
            allNames[name]++;
        }
        else {
            allNames[name] = 1;
        }
        return allNames;
    }, {});
    return temp;
}
xh.systemSource=function(data){
    var $scope = angular.element(appElement).scope();
    console.log(data);

    // 设置容器宽高
    var height=document.documentElement.clientHeight;
    var width=document.documentElement.clientWidth;
    var resizeBarContainer = function() {
        $("#siteSource").width(447);
        $("#siteSource").height(282);
        $("#rtuSource").width(447);
        $("#rtuSource").height(282);
        $("#deviceSource").width(447);
        $("#deviceSource").height(242);
        $("#off").width(447);
        $("#off").height(352);
        $("#warning").width(447);
        $("#warning").height(352);
        $("#alarm").width(447);
        $("#alarm").height(162);
    };
    resizeBarContainer();

}
xh.deviceWarningTop5=function(data){
    var $scope = angular.element(appElement).scope();
    console.log(data);

    // 设置容器宽高
    var height=document.documentElement.clientHeight;
    var width=document.documentElement.clientWidth;
    var resizeBarContainer = function() {
        $("#deviceWarning-top5").width(447);
        $("#deviceWarning-top5").height(420);
    };
    resizeBarContainer();

    // 基于准备好的dom，初始化echarts实例
    var chart = null;
    if (chart != null) {
        chart.clear();
        chart.dispose();
    }

    require([ 'echarts', 'echarts/chart/pie' ], function(ec) {
        chart = ec.init(document.getElementById('deviceWarning-top5'));
        var values = [];
        if(data.list1.length > 0){
            for(var i=0;i<data.list1.length;i++){
                values.push({value:data.list1[i].score,name:data.list1[i].rtu_id,detail:data.list1[i].detail});
            }
        }
        if(data.list2.length > 0){
            for(var i=data.list2.length-1;i>=0;i--){
                values.push({value:data.list2[i].score,name:data.list2[i].rtu_id,detail:data.list2[i].detail});
            }
        }

        console.log("values");
        console.log(values);

        var option = {
            title : {
                text : 'RTU健康度TOP5',
                x : 20,
                y : 30,
                textStyle : {
                    color : '#fff',
                    fontSize : 16
                }
            },
            tooltip : {
                trigger: 'item',
                padding: 10,
                backgroundColor: 'rgba(9, 131, 195, 0.83)',
                position: function(a){
                    //console.log(a);
                    return [a[0]*0.5,a[1]*0.5];
                },
                formatter: "{a} <br/>{b} : {c}",
                textStyle: {
                    color: "#fff",
                    fontSize: 14,
                    fontWeight: "bold"
                }
            },
            calculable : false,
            series : [
                {
                    name:'健康度分析值',
                    type:'pie',
                    radius : [30, 100],
                    center : ['50%', '50%'],
                    roseType : 'area',
                    data: values
                }
            ]
        };

        if(data.list1.length > 0 && data.list2.length > 0){
            chart.setOption(option);
        }

        chart.on('click', function (params) {
            $scope.sendUrl(params.name);
        });

    });
    /*window.onresize = function() {
        // 重置容器高宽
        chart.resize();
    };*/

}
xh.deviceOffTop5=function(data){
    var $scope = angular.element(appElement).scope();
    //data = [{name:"管线1",value:100},{name:"管线2",value:150},{name:"管线3",value:180}]
    // 设置容器宽高
    var height=document.documentElement.clientHeight;
    var width=document.documentElement.clientWidth;
    var resizeBarContainer = function() {
        $("#deviceOff-top5").width(447);
        $("#deviceOff-top5").height(420);
    };
    resizeBarContainer();

    // 基于准备好的dom，初始化echarts实例
    var chart = null;
    if (chart != null) {
        chart.clear();
        chart.dispose();
    }

    require([ 'echarts', 'echarts/chart/pie' ], function(ec) {
        chart = ec.init(document.getElementById('deviceOff-top5'));
        var values = [];
        if(data.l1.length > 0){
            for(var i=0;i<data.l1.length;i++){
                values.push({value:data.l1[i].score,name:data.l1[i].rtu_id,detail:data.l1[i].detail});
            }
        }
        if(data.l2.length > 0){
            for(var i=data.l2.length-1;i>=0;i--){
                values.push({value:data.l2[i].score,name:data.l2[i].rtu_id,detail:data.l2[i].detail});
            }
        }

        var option = {
            title : {
                text : 'RTU风险度TOP5',
                x : 20,
                y : 30,
                textStyle : {
                    color : '#fff',
                    fontSize : 16
                }
            },
            tooltip : {
                trigger: 'item',
                padding: 10,
                backgroundColor: 'rgba(9, 131, 195, 0.83)',
                position: function(a){
                    console.log(a);
                    return [a[0]*0.5,a[1]*0.5];
                },
                formatter: "{a} <br/>{b} : {c}",
                textStyle: {
                    color: "#fff",
                    fontSize: 14,
                    fontWeight: "bold"
                }
            },
            calculable : false,
            series : [
                {
                    name:'风险度分析值',
                    type:'pie',
                    radius : [30, 100],
                    center : ['50%', '50%'],
                    roseType : 'area',
                    data: values,
                    labelLine: {
                        normal: {
                            lineStyle: {
                                color: 'rgba(255, 255, 255, 0.3)'
                            },
                            smooth: 0.2,
                            length: (width/12)*2 / 20,
                            length2: (width/12)*2 / 40,
                        }
                    }
                }
            ]
        };

        if(data.list1.length > 0 && data.list2.length > 0){
            chart.setOption(option);
        }

        chart.on('click', function (params) {
            $scope.sendUrl(params.name);
        });

    });
    /*window.onresize = function() {
        // 重置容器高宽
        chart.resize();
    };*/

}
xh.call = function(data) {
    // 设置容器宽高
    var height=document.documentElement.clientHeight;
    var width=document.documentElement.clientWidth;
    var resizeBarContainer = function() {
        $("#call-bar").width(445);
        $("#call-bar").height(782);
    };
    resizeBarContainer();

    // 基于准备好的dom，初始化echarts实例
    var chart = null;
    if (chart != null) {
        chart.clear();
        chart.dispose();
    }
    require([ 'echarts', 'echarts/chart/bar','echarts/chart/line' ], function(ec) {
        chart = ec.init(document.getElementById('call-bar'));
        var option = {
            tooltip : {
                trigger: 'axis'
            },
            /*legend: {
                data:['上报数据'],
                textStyle:{
                    color:'#fff'
                }
            },*/

            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    splitLine:{show: false},//去除网格线
                    splitArea : {show : false},//去除网格区域
                    data : ["00","01","02","03","04","05",
                        "06","07","08","09","10","11",
                        "12","13","14","15","16","17",
                        "18","19","20","21","22","23"]
                }
            ],
            yAxis : [{
                type: 'value',
                name: '上报数据',
                min: 0,

                position: 'left',
                axisLabel: {
                    formatter: '{value} （条）',
                    textStyle:{
                        color:'#fff'
                    }
                }
            }],
            series : [{
                name:'上报数据',
                type:'line',
                yAxisIndex:0,
                itemStyle:{normal:{color:'yellow'}},
                data:[]
            }]
        };

        //start
        //var response = {"num":[{"num":1246,"label":"00"},{"num":1003,"label":"01"},{"num":453,"label":"02"},{"num":277,"label":"03"},{"num":287,"label":"04"},{"num":352,"label":"05"},{"num":542,"label":"06"},{"num":5538,"label":"07"},{"num":11388,"label":"08"},{"num":8998,"label":"09"},{"num":8441,"label":"10"},{"num":480,"label":"11"},{"num":0,"label":"12"},{"num":0,"label":"13"},{"num":0,"label":"14"},{"num":0,"label":"15"},{"num":0,"label":"16"},{"num":0,"label":"17"},{"num":0,"label":"18"},{"num":0,"label":"19"},{"num":0,"label":"20"},{"num":0,"label":"21"},{"num":0,"label":"22"},{"num":0,"label":"23"}]};
        var num = data;
        var xData=[],yData=[];

        for(var i=0;i<num.length;i++){
            xData.push(num[i].label);
            yData.push(num[i].num);
        }
        option.series[0].data = yData;
        option.xAxis[0].data = xData;
        chart.setOption(option);
        //end
        /*$.ajax({
            url : 'call/chart',
            type : 'POST',
            dataType : "json",
            async : false,
            data:{
                bsId:0,
                time:xh.getOneDay(),
                type:'hour'
            },
            success : function(response) {
                var data = response.time;
                var num = response.num;
                var xData=[],yData=[],yData2=[];

                for(var i=0;i<data.length;i++){
                    xData.push(data[i].label);
                    yData.push(data[i].time);
                    yData2.push(num[i].num);
                }
                var bsId=parseInt($("select[name='bsId']").val());
                var text="";

                option.series[0].data = yData;
                option.series[1].data = yData2;
                option.xAxis[0].data = xData;
                chart.setOption(option);
                xh.maskHide();

            },
            failure : function(response) {
                xh.maskHide();
            }
        });*/

        chart.setOption(option);

    });

    // 用于使chart自适应高度和宽度
    window.onresize = function() {
        // 重置容器高宽
        resizeBarContainer();
    };
};
/*xh.waterstatus=function(id,totals){
    var vaterColor="blue";
    var tColor;
    if(id==1){
        vaterColor="#28beff";
        tColor="#fff";
    }else if(id==2){
        vaterColor="#28beff";
        tColor="#fff";
    }else if(id==3){
        vaterColor="#28beff";
        tColor="yellow";
    }
    $('#access'+id).waterbubble({
        radius : 40,
        lineWidth : 2,
        data : 0.7,
        waterColor : vaterColor,
        textColor : tColor,
        txt : totals.toString(),
        font : 'bold 20px "Microsoft YaHei"',
        wave : true,
        animation : true
    });
}*/
xh.getOneDay=function() {
    var   today=new Date();
    var   yesterday_milliseconds=today.getTime();    //-1000*60*60*24

    var   yesterday=new   Date();
    yesterday.setTime(yesterday_milliseconds);

    var strYear=yesterday.getFullYear();

    var strDay=yesterday.getDate();
    var strMonth=yesterday.getMonth()+1;

    if(strMonth<10)
    {
        strMonth="0"+strMonth;
    }
    if(strDay<10){
        strDay="0"+strDay;
    }
    var strYesterday=strYear+"-"+strMonth+"-"+strDay;
    return  strYesterday;
}

xh.formatNum=function(text){
    var str="";
    var a=text.toString();
    for(var i=0;i<a.length;i++){
        str+="<span>"+a.charAt(i)+"</span>";
    }
    return str;

}