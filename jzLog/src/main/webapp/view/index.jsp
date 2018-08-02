<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %> 
    <%String path = request.getContextPath();%>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>宸析日志分析系统</title>
	<!-- <title>中网云安日志审计系统</title> -->
    <!--[if lt IE 8]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
	<link src="../css/layer.css" type="text/javascript" charset="utf-8"></link>
    <link rel="icon" type="image/x-icon" href="../img/cx.ico" />
    <!-- <link rel="icon" type="image/x-icon" href="../img/zwya_icon.ico" /> --> 
    <link href="../hplus/css/bootstrap.min.css?v=3.3.5" rel="stylesheet">
    <link href="../hplus/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="../hplus/css/animate.min.css" rel="stylesheet">
    <link href="../hplus/css/style.css?v=4.0.0" rel="stylesheet">
    <style type="text/css">
    	html{
    		min-width:1366px;
    		overflow:auto;
    		overflow-y:hidden;		
    	}
    	body.canvas-menu .navbar-static-side, body.fixed-sidebar .navbar-static-side{
    		position:absolute;
    	}
        .threshold{
        	float:left;
        	margin-left: 195px;
        	font-weight:600; 
        	display:inline-block;
        	height:36px;
        	line-height:36px;     
        	padding-left:10px;
        }	
        #wrapper{
        	position:fixed;
        }
         #wrapper>nav{
         	overflow:hidden;
         	padding-bottom:86px;
         }
         #wrapper>nav .slimScrollDiv{
         	height: 100%!important;
         }
          #wrapper>nav .slimScrollDiv .sidebar-collapse{
          	height: 100%!important;
          	overflow:auto;
          }
        #page-wrapper{
        	width: calc(100% - 200px);
        	min-width:1166px!important;
        }
        .index_top{
        	height:50px;
        	width:100%;
        	line-height:45px;
        	background:#2f4050;
        	padding-left:10px;
        	
        }
        .jxLogo img{
        	width:180px;
        	margin-left:10px;
        	margin-top:10px;
        }
        .footer{
        	position:fixed;
        	bottom:0px;
        	width:100%;
        	padding-top:0;
        	padding-bottom:0;
        	line-height:36px;
        	min-width:1366px;
        }
        .userLogout{
        	float:right;
        	margin-right: 10px;
        	height:50px;
        	line-height:53px;
        	background:#293846;
        	padding:0 10px;
        }
        .userLogout a{
        	background:none;
        	margin-top:10px;
        }
        .userLogout a:hover{
        	color:#888;
        }
        .nav>li.active {
		    border-left: 4px solid #2c76bd;
		    background: #293846;
		}
		.changeBG{
			display:inline-block;
			float:right;
			height: 50px;
			line-height:54px;
			cursor:pointer;
			width: 80px;
			position:relative;
		}
		.changeBG>span{
			display:inline-block;
			width: 30px;
			text-align: center;
			height:50px;
			line-height: 54px;	
			float: left;
		}
		.colorBox{
			position:absolute;
			top:50px;
			width:80px;
			height:42px;
			z-index:111;
			background:#456666;
			box-shadow:5px 5px 5px #ccc;
		}
		.colorBox>div{
			width:38px;
			height:40px;
			float: left;
			text-align: center;
		}
		.deepColour span{
			display:inline-block;
			width: 20px;
			height:20px;
			background:#2f4050;
			border-radius:5px;
		}
		.changeBG .colorType{
			width:50px;
		}
		.lightColour span{
			display:inline-block;
			width: 20px;
			height:20px;
			background:#eee;
			border-radius:5px;
		}
		.threshold_setting{
			margin-left:10px;
		}
		.rangeBox{
			padding:10px;
		}
		.rangeValue{
			margin:10px;
			font-size:12px;
		}
		.version{
			color:#888;
			margin-left:5px;
		}
		.dropdown-menu{
			left:-80px!important;
			margin-top:0;
		}
		.changePWBox{
			overflow: hidden;
		    padding-top: 30px;
		    padding-left: 16px;
		}
		.dropdown-menu>li>a{
			color:#fff!important;
		}
		.form-control {
		    border: 0;
		    background: 0;
		    color: #56a4ef;
		    box-shadow: 0px 0px 7px 1px #4781bb;
		}
		.form-control:focus, .single-line:focus {
		    box-shadow: 0px 0px 7px 1px #4781bb;
		}
		.errorMenu{
			height:50px;
			color:#5bc0de;
			line-height:50px;
			padding-left:20px;
			text-align: center;
		}
		.errorMenu i{
			color:#e4956d;
			cursor:pointer;
		}
		#side-menu>img{
			margin:10px;
			margin-left:80px;			
		}
		.pass_set{
			display: none;
			height: 22px;
    		padding-left: 102px;
		}
		.pass_set li{
			float: left;
		    text-align: center;
		    width: 50px;
		    border-right: 2px solid #fff;
		    background: #ffd8b4;
		    color: #fff;
		    list-style-type: none;
		}
    </style>
    <link rel="stylesheet" type="text/css" href="../css/indexDeepColor.css" >
</head>

<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
	 <div class="index_top" >
	 	 <img alt="image" src="../img/index_cx.png" />
	 	 <!-- <img alt="image" src="../img/jx_logo.png" /> -->
	 	 <b class="version">V2.0</b>
   		<!--  <img alt="image"  src="../img/zwya_logo.png" />
   		 <b class="version"> NCS-LOG V3.0</b> -->
   		 <div class="userLogout">
          	<a onclick="loginOut()" class="text-muted"><i class="fa fa fa-sign-out"></i>退出</a>
          </div> 
   		<div class="dropdown profile-element" style="float:right;margin-right:10px;">
	         <a data-toggle="dropdown" class="dropdown-toggle" style="" href="#">
	             <span class="clear">
	             <span class="text-muted text-xs block" style="text-align:center;height:50px;line-height:50px;font-weight:900;font-size:15px"><b class="caret"></b></span>
	             </span>
	         </a>
	         <!-- <ul class="dropdown-menu animated fadeInLeft m-t-xs">
	             <li><a class="J_menuItem" href="form_avatar.html">修改头像</a>
	             </li>
	             <li><a class="J_menuItem" href="profile.html">个人资料</a>
	             </li>
	             <li><a class="J_menuItem" href="contacts.html">联系我们</a>
	             </li>
	             <li><a class="J_menuItem" href="mailbox.html">信箱</a>
	             </li>
	             <li class="divider"></li>
	             <li><a href="login.html">安全退出</a>
	             </li>
	         </ul> -->
	         <ul class="dropdown-menu animated fadeInLeft m-t-xs">
	         	<li>
	         		<a class="J_menuItem changePW" >修改密码</a>
	             </li>
	         </ul>
	      </div> 
        <!-- <div class="changeBG" title="点击切换颜色风格">
        	关灯
        </div> -->
   	</div>
    <div id="wrapper">
    	
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation" style="border-top:1px solid #293846;border-right: 1px solid #2f4050;">
        	
        	<!--  <div style="height:50px" class="jxLogo">
        	 	<img alt="image"  src="../img/jx_logo.png" />
            </div> -->
            <!-- <div class="dropdown profile-element">
               <a data-toggle="dropdown" class="dropdown-toggle" style="" href="#">
                   <span class="clear">
                   <span class="text-muted text-xs block" style="text-align:center;height:50px;line-height:50px;font-weight:900;font-size:15px">超级管理员<b class="caret"></b></span>
                   </span>
               </a>
               <ul class="dropdown-menu animated fadeInRight m-t-xs">
                   <li><a class="J_menuItem" href="form_avatar.html">修改头像</a>
                   </li>
                   <li><a class="J_menuItem" href="profile.html">个人资料</a>
                   </li>
                   <li><a class="J_menuItem" href="contacts.html">联系我们</a>
                   </li>
                   <li><a class="J_menuItem" href="mailbox.html">信箱</a>
                   </li>
                   <li class="divider"></li>
                   <li><a href="login.html">安全退出</a>
                   </li>
               </ul>
            </div> -->
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    
				<!--  
                    <li>
                        <a href="#"><i class="fa fa-edit"></i> <span class="nav-label">电商BI</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            
                        </ul>
                    </li>
                    -->
                    <!-- <li>
                        <a href="#"><i class="fa fa-edit"></i> <span class="nav-label">电商业务模型</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level"> -->
                        <!-- 	<li>
	                            <a href="#">
		                            <span class="nav-label">用户属性</span>
		                            <span class="fa arrow"></span>
		                        </a>
		                        <ul class="nav nav-third-level"> 
		                        	<li>
		                                <a class="J_menuItem" href="business/electronicBusiness/userAttr/RegUserDistribution.jsp">注册用户分布</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/userAttr/UserRegSituation.jsp">用户注册情况</a>
		                            </li>
		                        </ul>
							</li> -->
                           <!--  <li>
	                        	<a href="#">
		                            <span class="nav-label">商品属性</span>
		                            <span class="fa arrow"></span>
		                        </a>
		                        <ul class="nav nav-third-level"> 
		                        	<li>
		                                <a class="J_menuItem" href="business/electronicBusiness/goodsAttr/BasicInfo.jsp">商品基本信息</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/goodsAttr/GoodsSales.jsp">商品销量</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/goodsAttr/GoodsEvaluate.jsp">商品评价</a>
		                            </li>
		                        </ul>
                            </li>
                            <li>
	                        	<a href="#">
		                            <span class="nav-label">订单分析</span>
		                            <span class="fa arrow"></span>
		                        </a>
		                        <ul class="nav nav-third-level"> 
		                        	<li>
		                                <a class="J_menuItem" href="business/electronicBusiness/orderAnalyse/BasicInfo.jsp">订单基本信息</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/orderAnalyse/OrderDeal.jsp">全国订单成交情况</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/orderAnalyse/OrderParticulars.jsp">已支付订单信息</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/orderAnalyse/OrderCancelAndReturn.jsp">订单取消&退换货</a>
		                            </li>
		                        </ul>
                            </li>
                            <li>
	                        	<a href="#">
		                            <span class="nav-label">用户行为分析</span>
		                            <span class="fa arrow"></span>
		                        </a>
		                        <ul class="nav nav-third-level"> 
		                        	<li>
		                                <a class="J_menuItem" href="business/electronicBusiness/behaviorAnalyse/SalesDistribution.jsp">全国销售额区域分布</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/behaviorAnalyse/BehaviorFunnel.jsp">用户行为漏斗</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/behaviorAnalyse/PageVisit.jsp">用户访问</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/behaviorAnalyse/UserActive.jsp">用户活跃情况</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/behaviorAnalyse/LoyaltyAndConsumingAbility.jsp">用户忠诚度&消费能力</a>
		                            </li>
		                        </ul>
                            </li>
                            <li>
	                        	<a href="#">
		                            <span class="nav-label">流式分析</span>
		                            <span class="fa arrow"></span>
		                        </a>
		                        <ul class="nav nav-third-level"> 
		                        	<li>
		                                <a class="J_menuItem" href="business/electronicBusiness/kafka/user.jsp">用户</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/kafka/DataTransfor.jsp">数据迁徙</a>
		                            </li>
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/kafka/DataMap.jsp">数据地图</a>
		                            </li>
		                             
		                            <li>
		                                <a class="J_menuItem" href="business/electronicBusiness/kafka/SalesVolume.jsp">流量</a>
		                            </li>
		                           
		                        </ul>
                            </li>
                            
                            <li>
                            	<a class="J_menuItem" href="electronicBusiness/userAttr.jsp">用户自然属性 </a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="electronicBusiness/goodsAttr.jsp">商品自然属性</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="electronicBusiness/behaviorAnalyse.jsp">用户行为分析 </a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="electronicBusiness/orderAnalyse.jsp">订单分析</a>
                            </li>
                           
                        </ul>
                    </li>
                    <li >
                        <a href="#">
                            <i class="fa fa fa-bar-chart-o"></i>
                            <span class="nav-label">学生体检</span>
                            <span class="fa arrow"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <li class="">
                                <a class="J_menuItem active" href="business/health/ophthalmology/ophthalmology.jsp">眼科 <span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li><a class="J_menuItem" href="business/health/ophthalmology/PoorsightChart.jsp">视力</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/ophthalmology/SicknessChart.jsp">眼病</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/ophthalmology/ophthalmologyData.jsp">基础数据查询</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="">
                                <a class="J_menuItem" href="business/health/nutritional/nutritionalsurface.jsp" data-index="0">营养评价 <span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li><a class="J_menuItem" href="business/health/nutritional/FatChart.jsp">肥胖</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/nutritional/DystrophiaChart.jsp">营养不良</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/nutritional/DystrophiaRated.jsp">营养评价状况</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/nutritional/nutritionalData.jsp">基础数据查询</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="">
                                <a class="J_menuItem" href="business/health/tooth/toothIndex.jsp">牙齿 <span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li><a class="J_menuItem" href="business/health/tooth/CariesChars.jsp">龋齿</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/tooth/PeriodontosisChart.jsp">牙周病</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/tooth/ToothData.jsp">基础数据查询</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="">
                                <a class="J_menuItem" href="business/health/commonDisease/commonDiseaseIndex.jsp">常见病 <span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li><a class="J_menuItem" href="business/health/commonDisease/LhemoglobinChart.jsp">低血红蛋白</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/commonDisease/HbloodpressureChart.jsp">血压偏高</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/commonDisease/ExceptionalChart.jsp">其他异常情况</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/commonDisease/commonDiseaseData.jsp">基础数据查询</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="">
                                <a class="J_menuItem" href="business/health/physiqueDevelopment/physiqueDevelopmentIndex.jsp">体型&发育水平 <span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li><a class="J_menuItem" href="business/health/physiqueDevelopment/physiqueChart.jsp">体型</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/physiqueDevelopment/DevelopmentChart.jsp">发育水平</a>
                                    </li>
                                    <li><a class="J_menuItem" href="business/health/physiqueDevelopment/physiqueDevelopmentData.jsp">基础数据查询</a>
                                    </li>
                                </ul>
                            </li>
                            
                        </ul>
                    </li> -->
                   <!--  <li>
                        <a href="#"><i class="fa fa-edit"></i> <span class="nav-label">图书管理</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                        	<li>
                                <a class="J_menuItem" href="book/bookIndex.jsp">任务管理 </a>
                            </li>
                           
                        </ul>
                    </li> -->
                    <!--  <li>
                    	 <a href="#"><i class="fa fa-desktop"></i> <span class="nav-label">首页</span><span class="fa arrow"></span></a>
                    	  <ul class="nav nav-second-level">
                        	<li>
                                <a class="J_menuItem" href="logPro/logIndex.html">首页 </a>
                            </li>
                           
                        </ul>
                    </li>
                    <li>
                        <a href="#"><i class="fa fa-users"></i> <span class="nav-label">用户管理</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                        	<li>
                                <a class="J_menuItem" href="userManage/userManage.html">用户管理 </a>
                            </li>
                           
                        </ul>
                    </li>
                    <li>
                        <a href="#"><i class="fa fa-user"></i> <span class="nav-label">角色管理</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                        	<li>
                                <a class="J_menuItem" href="roleManage/roleManage.html">角色管理</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="#"><i class="fa fa-laptop"></i> <span class="nav-label">资产管理</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                        	<li>
                                <a class="J_menuItem" href="device/device.html">资产管理</a>
                            </li>
                           
                        </ul>
                    </li>
                     <li>
                        <a href="#"><i class="fa fa-database"></i> <span class="nav-label">数据源管理</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                        	<li>
                                <a class="J_menuItem" href="sourceFile/sourceFile.html">数据源管理</a>
                                <a class="J_menuItem" href="device/newProperty.html">资产管理</a>
                            </li>
                           
                        </ul>
                    </li>
                    <li>
                        <a href="#"><i class="fa fa-list-alt"></i> <span class="nav-label">日志管理</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                        	<li>
                                <a class="J_menuItem" href="logPro/searchLogs.html">日志检索 </a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="logPro/searchLogs2.html">精确查询 </a>
                            </li> 
                           
                        </ul>
                    </li>
                    <li>
                        <a href="#"><i class="fa fa-file-text-o"></i> <span class="nav-label">事件管理</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                        	<li>
                                <a class="J_menuItem" href="eventManage/eventSearch.html">事件搜索</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="eventManage/eventAlarm.html">事件告警</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="#"><i class="fa fa-th"></i> <span class="nav-label">平台管理</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                        	<li>
                                <a class="J_menuItem" href="auditLog/auditLog.html">审计中心</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="auditLog/serviceManage.html">控制中心</a>
                            </li>
                        </ul>
                    </li> -->
                     
                   
                </ul>
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
        	<div class="row border-bottom">
            	<div class="navbar-header" style="display:none">
            		<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#">
            			<i class="fa fa-bars"></i> 
            		</a>                        
                </div>
            </div>
            <div class="row border-bottom">
            </div>
            <div class="row content-tabs">
                <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
                </button>
                <nav class="page-tabs J_menuTabs">
                    <div class="page-tabs-content">
                        <!-- <a href="javascript:;" class="active J_menuTab" data-id="business/electronicBusiness/goodsAttr/BasicInfo.jsp">商品基本信息</a> -->
                        <a href="javascript:;" class="active J_menuTab" data-id="logPro/logIndex.html">首页	<i class="fa fa-times-circle"></i></a>
                    </div>
                </nav>
                <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
                </button>
                <div class="btn-group roll-nav roll-right">
                    <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>

                    </button>
                    <ul role="menu" class="dropdown-menu dropdown-menu-right">
                        <li class="J_tabShowActive"><a>定位当前选项卡</a>
                        </li>
                        <li class="divider"></li>
                        <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                        </li>
                        <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                        </li>
                    </ul>
                </div>
                
            </div>
            <div class="row J_mainContent" id="content-main">
                <!-- <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="business/electronicBusiness/goodsAttr/BasicInfo.jsp" frameborder="0" data-id="business/electronicBusiness/goodsAttr/BasicInfo.jsp" seamless></iframe> -->
                <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="logPro/logIndex.html" frameborder="0" data-id="logPro/logIndex.html" seamless></iframe>
            </div>
           
        </div>
        <!--右侧部分结束-->
        <!--右侧边栏开始-->
        <!--右侧边栏结束-->
         <div class="footer">           	
            <div class="pull-right">&nbsp;版权所有 &nbsp;&copy; 2017-2018 &nbsp;山东九州信泰信息科技股份有限公司 &nbsp;</div>
             <!-- <div class="pull-right">&nbsp;版权所有 &nbsp;&copy; 2017-2018 &nbsp;山东中网云安智能科技有限公司 &nbsp;</div> -->
             <span class="threshold"></span>
              <a class="threshold_setting">（阈值设置）</a>
         </div>
    </div>
    <script src="../hplus/js/jquery.min.js?v=2.1.4"></script>
	<script src="../hplus/js/bootstrap.min.js?v=3.3.5"></script>
    <script src="../js/ajax.js"></script>
    <script src="../hplus/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="../hplus/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- <script src="../hplus/js/plugins/layer/layer.min.js"></script> -->
    <script src="../js/layer.js" type="text/javascript" charset="utf-8"></script>
    <script src="../hplus/js/hplus.js?v=4.0.0"></script>
    <script src="../hplus/js/contabs.min.js"></script>
    <script src="../hplus/js/plugins/pace/pace.min.js"></script>
    <script src="../util/globalEchart.js"></script>
    <script src="../js/formCheck.js"></script>
</body>
<script>
/* //判断是否为手机端
var ua = navigator.userAgent;
var ipad = ua.match(/(iPad).*OS\s([\d_]+)/),
    isIphone = !ipad && ua.match(/(iPhone\sOS)\s([\d_]+)/),
    isAndroid = ua.match(/(Android)\s+([\d.]+)/),
    isMobile = isIphone || isAndroid;
if(isMobile) {
	//显示左边栏开关
	$(".navbar-header").css({"display":"block"});
}  */
//刷新页面时调用，验证用户信息session
$(function(){
	$.ajax({
		url:'../users/checkLogin.do',
		type:"get",  //提交方式  
        dataType:"json", //数据类型  
		success:function(data){
			if(data.success=="false"){
				layer.open({
					  content: '您的登陆信息已经超时，请重新登陆！',
					  closeBtn: 0, //取消关闭按钮
					  yes: function(index, layero){
						  window.location.href="../Login.jsp";
					  }
				}); 
			}
		}
	});
});
/* function checkSession(){
	console.log("D")
	$.ajax({
		url:'../users/checkLogin.do',
		type:"get",  //提交方式  
        dataType:"json", //数据类型  
		success:function(data){
			if(data.success=="false"){
				layer.open({
					  content: '您的登陆信息已经超时，请重新登陆！',
					  closeBtn: 0, //取消关闭按钮
					  yes: function(index, layero){
						  window.location.href="../Login.jsp";
					  }
				}); 
			}
		}
	});
}
checkSession();
//定时查看阈值情况  100分钟 查看一次
setInterval(checkSession,120000); */
//系统登出
function loginOut(){
	$.ajax({
		url:'../users/logout.do',
		data:"",
		type:"get",  //提交方式  
        dataType:"json", //数据类型  
		success:function(data){
			if(data.success=="true"){
				window.location.href="../Login.jsp";
			}
		}
	});
}
//定义阈值
var thresholdValue = 92;
//系统平台阈值检测
function thresholdWarning(){
	//清除显示的数据
	$(".threshold").html('状态获取中...');
	//参数
	var obj = {};
	obj.user = "root";
	obj.passwd = "hs.1234";
	obj.host = "192.168.100.101";
	$.ajax({
		url:'../manage/getDiskUsage.do',
		data:obj,
		type:"get",  //提交方式  
        dataType:"json", //数据类型  
		success:function(data){
			/* if(data.error!=undefined){
				$(".threshold").html(data.error);
				$(".threshold").css("color","#d9534f");
			}else{
				var sizeNum = Number(data.size);
				var usedNum = Number(data.used);
				var percentage = usedNum/sizeNum;
				if(percentage <= 0.8 ){
					$(".threshold").html("存储空间充足");
					$(".threshold").css("color","#1ab394");
				}else if(0.8 < percentage && percentage < 0.92){
					$(".threshold").html("存储空间即将饱和  剩余"+(sizeNum-usedNum)+"G");
					$(".threshold").css("color","#f0ad4e");
				}else if( 0.92 < percentage && percentage <= 1){
					$(".threshold").html("存储空间告警");
					$(".threshold").css("color","#d9534f");
				}
			} */
			if(data.error!=undefined){
				$(".threshold").html(data.error);
				$(".threshold").css("color","#d9534f");
			}else{
				var sizeNum = Number(data.size);
				var usedNum = Number(data.used);
				var percentage = (usedNum/sizeNum)*100;
				if(percentage < thresholdValue){
					$(".threshold").html("存储空间充足");
					$(".threshold").css("color","#1ab394");
				}else{
					$(".threshold").html("存储空间告警  剩余"+(sizeNum-usedNum)+"G");
					$(".threshold").css("color","#d9534f");
				}
			}
		}
	});
}

//阈值设置按钮功能
$(".threshold_setting").click(function(){
	//弹窗html代码
	var layerHtml = '<div class="rangeBox">'
				  + 	'<p class="rangeValue">当前阈值大小：'+thresholdValue+'%</p>'
				  +  	'<input type = "range" value="'+thresholdValue+'" class="range_control" min = "0" step=1" max = "100" oninput="rangeChange()" onchange="rangeChange()"></input>'				  
				  + '</div>'
	//弹窗
	layer.open({ 
 		type: 1,
 		title:"阈值告警数值大小选择",//标题
			area: ['300px', '200px'], //宽高
			btn: ['确定','取消'], //按钮
			btn1:function(index){
				//重新获得数据
				thresholdWarning();
				layer.close(index);
			},
			content:layerHtml
	})
	
})
//当拖拽按钮时 数值发生改变 执行函数
function rangeChange(){
	//获取数值
	var rangeVal = $(".range_control").val();
	//显示改变
	$(".rangeValue").html("当前阈值大小："+rangeVal+"%");
	thresholdValue = rangeVal;
}
thresholdWarning();
//定时查看阈值情况  100分钟 查看一次
setInterval(thresholdWarning,6000000);

//获取角色信息
$.ajax({
	url:"../users/selectUserRole.do",
	data:"",
	type:"get",  //提交方式  
    dataType:"json", //数据类型  
	success:function(data){
		var roleName = data[0][0].roleName;
		$(".block").html(roleName+'<b class="caret"></b>');
		//保存在本地
		localStorage.setItem("LoginUser", JSON.stringify(data[0]));
	}
}); 
/* //本地存储系统颜色风格
localStorage.setItem("systemColor","deepColour");

//点击切换颜色风格
$(".changeBG").click(function(){
	//获取现在的系统颜色风格
	var sysColor = localStorage.getItem("systemColor");
	//更改应经显示的页面
	//获取每个已显示的页面的iframe
	var iframes = $("iframe");
	//判断现颜色风格  lightColour为浅色 deepColour为深色
	if(sysColor == "deepColour"){ //深 -》 浅
		//循环更改页面颜色
		for(var i = 0;i<iframes.length;i++){
			$(iframes[i]).contents().find('head').children("#deepColour").remove();;
		}
		//更改首页颜色样式
		$("#indexDeepColour").remove();
		//替换按钮文字
		$(".changeBG").html("关灯");
		//更改本地存储的颜色风格
		localStorage.setItem("systemColor","lightColour");
	}else if(sysColor == "lightColour"){//浅 -》深
		//循环更改页面颜色
		for(var i = 0;i<iframes.length;i++){
			$("<link>").attr({ rel: "stylesheet",type: "text/css",href: "../../css/deepColor.css",id:"deepColour"}).appendTo($(iframes[i]).contents().find('head'));
		}
		//更给首页样式
		$("<link>").attr({ rel: "stylesheet",type: "text/css",href: "../css/indexDeepColor.css",id:"indexDeepColour"}).appendTo($("head"));
		//替换按钮文字
		$(".changeBG").html("开灯");
		//更改本地存储的颜色风格
		localStorage.setItem("systemColor","deepColour");
		
	}
	
	
}) */

//修改密码
$(".changePW").click(function(){
	//显示弹窗
	//弹窗html代码
	var layerHtml = '<div class="changePWBox">'
				  +		'<form class="form-horizontal">'
				  +			'<div class="form-group passw">'
		 		  +				'<label for="inputEmail3" class="col-sm-3 control-label">旧的密码：</label>'
				  +				'<div class="col-sm-8">'
				  +					'<input type="password" class="form-control old_pw" placeholder="输入原来的密码">'
				  +				'</div>'
				  +			'</div>'
				  +			'<div class="form-group passw">'
		 		  +				'<label for="inputEmail3" class="col-sm-3 control-label">新的密码：</label>'
				  +				'<div class="col-sm-8">'
				  +					'<input type="password" class="form-control new_pw" maxlength="18" mixlength="6" onkeyup="pwStrength(this.value)" placeholder="6-18位数字、字母、字符">'
				  +				'</div>'
				  +			'</div>'
				  +			'<ul class="pass_set">'
				  +			    '<li id="strength_L">弱</li>'
				  +			    '<li id="strength_M">中</li>'
				  +			  	  '<li id="strength_H">强</li>'
				  +			 '</ul>'
				  +			'<div class="form-group passw">'
		 		  +				'<label for="inputEmail3" class="col-sm-3 control-label">确认密码：</label>'
				  +				'<div class="col-sm-8">'
				  +					'<input type="password" class="form-control new_pw2" maxlength="20" placeholder="再次输入密码">'
				  +				'</div>'
				  +			'</div>'
				  +		'</form>'
				  + '</div>'
	//弹窗
	layer.open({ 
 		type: 1,
 		title:"修改用户密码",//标题
			area: ['400px', '320px'], //宽高
			btn: ['确定','取消'], //按钮
			btn1:function(index){
				//获得输入框的信息
				 //旧密码
				var oldPw = $(".old_pw").val();
				//新的密码
				var newPw = $(".new_pw").val();
				//新的密码2
				var newPw2 = $(".new_pw2").val(); 
				//判断合法性
				 if(oldPw == ''){
					layer.msg('请填写原来密码',{icon: 5});
				}else if(newPw == ''){
					layer.msg('未填写新的密码',{icon: 5});
				}else if(newPw.length < 6 || newPw.length>18){
					layer.msg('密码长度不正确(6-18位)',{icon: 5});
				}else if(newPw != newPw2){
					layer.msg('两次密码输入不一致',{icon: 5});
				}else{
					//传参对象
					var obj = {};
					//获取用户id
					var userObj = JSON.parse(localStorage.getItem("LoginUser"));
					obj.id = userObj[0].id;
					obj.oldPassword = oldPw;
					obj.password = newPw;
					//成功函数
					var sfunc = function(data){
						if(data.success == "true"){
							layer.msg(data.message,{icon: 1});
							//关闭弹窗
					  		layer.close(index);
						}else if(data.success == "false"){
							layer.msg(data.message,{icon: 5});
						}					
					}
					//发送请求
					ajaxPost("../users/updatePasswordById.do",obj,sfunc);
				} 
			},
			content:layerHtml
	})
})
//加载菜单
getMenuData();
function getMenuData(){
  //添加加载动画
  $("#side-menu").html('<img src="../img/loading.gif">')
  //发送请求
  $.ajax({
  	type:"post",
  	url:"../menu/selectAll.do",
  	/* url:'menuData.json', */
  	data:{},
  	async:true,
  	success:function(data){
  		//清空加载动画
 		$("#side-menu").html('');
  		//判断请求是否正确
  		if(typeof data == 'string'){
  			
  		}else{
  			for(var i in data){
  	  			var menuHtml = '';//菜单树
  	  			var childMenuHtml = '';
  	  			var menuObj = data[i];
  	  			menuHtml = '<li>'
  	  					 + 	  '<a href="#"><i class="'+menuObj.icon+'"></i>'
  	  					 + 	  '<span class="nav-label">'+menuObj.menuName+'</span><span class="fa arrow"></span></a>'
  	  					 + 	  '<ul class="nav nav-second-level">'
  	  			var childMenuArr = data[i].menus;//子菜单数据数组
  	  			//拼接字菜单
  	  			for(var j in childMenuArr){
  	  				childMenuHtml += '<li>'
  	  							  +		'<a class="J_menuItem" data-href="'+childMenuArr[j].url+'">'+childMenuArr[j].menuName+'</a>'
  	  							  +	 '</li>'
  	  			}
  	  			//添加到页面上
  	  			$("#side-menu").append(menuHtml+childMenuHtml+'</ul></li>')
  	  		}
  	  		//绑定点击事件
  	  		$("#side-menu").on("click",".J_menuItem",function(){
  	  			//获得页面路径
  	  			var htmlHref = $(this).attr("data-href");
  	  			//获得页面标题
  	  			var htmlTitle = $(this).html();
  	  			//判断页面是否已经加载
  	  			if($('.page-tabs-content a[data-id="'+htmlHref+'"]').length == 0){//不存在
  	  				//拼接导航
  	  	  			var html ='<a href="javascript:;" class="active J_menuTab" data-id="'+htmlHref+'">'+htmlTitle+'<i class="fa fa-times-circle"></i></a>'
  	  	  			//移除导航菜单选中属性
  	  	  			$('.page-tabs-content').children("a").removeClass("active");
  	  	  			//添加导航菜单
  	  	  			$('.page-tabs-content').append(html);
  	  	  			var iframe = '<iframe class="J_iframe" width="100%" height="100%" src="'+htmlHref+'" frameborder="0" data-id="'+htmlHref+'" seamless="" style="display: inline;"></iframe>'
  	  	  			//移除其他页面
  	  	  			$('#content-main').children("iframe").hide();
  	  	  			$('#content-main').append(iframe);
  	  			}else{//已存在
  	  				//加载
  	  				$('.page-tabs-content a[data-id="'+htmlHref+'"]').click()
  	  			}
  	  			
  	  			
  	  		})
  	  		//添加点击展开 隐藏列表功能
  	  		$("#side-menu").metisMenu({ toggle: false });
  		}		
  	},
  	error:function(){
  		var errorHtml = "<p class='errorMenu'>获取菜单失败,请  <i onClick='getMenuData()'>重试</i></p>";
  		$("#side-menu").html(errorHtml)
  	}
  })
}
$(window).scroll(function(){//开始监听滚动条
	var leftVal = ($(document).scrollLeft());
	$("#wrapper").css("left",-leftVal+'px')
})
</script>

</html>