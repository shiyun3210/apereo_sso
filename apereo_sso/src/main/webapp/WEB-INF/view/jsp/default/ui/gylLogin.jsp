<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="imgUrl" value="http://img.gmpmart.com/zk_file/resource/load?path="/>
<c:set var="guc" value="http://uc.gmpmart.cn"/>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport" content="width=device-width, initial-scale=0.1,maximum-scale=1.0,user-scalable=yes">
<meta name="keywords" content="环球摩配网,环球摩配网,环球摩配网,环球摩配网,环球摩配网">
<title>环球摩配网</title>
<link rel="shortcut icon" href="${ctx}/images/common/favicon.ico" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/common.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/login.css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/gyLogin.js"></script>

<!--[if lt IE 9]>  
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>  
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/IeCode/selectivizr-min.js"></script>
    <script type="text/javascript" src="${ctx}/js/IeCode/cssQuery-p.js"></script>
    <script type="text/javascript" src="${ctx}/js/IeCode/sylvester.js"></script>
    <script type="text/javascript" src="${ctx}/js/IeCode/cssSandpaper-min.js"></script> 
    <script type="text/javascript" src="${ctx}/js/IeCode/PIE.js"></script>
<![endif]-->
<style type="text/css">
    html,body{overflow: hidden;}
</style>
</head>
<body>
<!-- 头部 -->
<div class="header">
    <div class="header02 autowrap clearfix">
        <div class="logo fl">
            <h1><a href="javascript:;" title="环球摩配网">环球摩配网</a></h1>
        </div>
    </div>
</div>
<div class="loginbox wrap clearfix">
    <div class="imgbox fl">
    	<a href="http://seller.gmpmart.com" target="_blank">
        <img src="${imgUrl }resources/20161205/20161205091012111591_550x370.jpg" alt="" >
        </a>
    </div>
    <div class="login fr">
    	<form:form method="post" id="fm1" commandName="${commandName}" htmlEscape="true">
    		<div id="formMsg" style="display:none;"><form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" /></div>
    		<h2>登录<div class="ere"></div></h2>
			<input type="hidden" name="lt" value="${loginTicket}" />
            <input type="hidden" name="execution" value="${flowExecutionKey}" />
            <input type="hidden" name="_eventId" value="submit" />
            
        	<div class="same phone">
                <i></i>
                <input type="text" placeholder="手机号码" maxlength="11" value="" onkeyup='this.value=this.value.replace(/\D/gi,"")' id="username" name="username" />
            </div>
            <div class="same password">
                <i></i>
                <input type="password" id="password" placeholder="输入密码" maxlength="25" value="" name="password" onkeydown="return banInputSapce(event);"/>
            </div>
            <input type="button" id="btnLogin" value="登  录" class="submit" />
    	</form:form>
        <div class="txtbtn clearfix">
            <span class="fl tdhover"><a href="${guc}/gyUser/modifypass">忘记密码？</a></span>
            <span class="fr">还没有帐号？<a href="${guc}/gyUser/register" class="tdhover blue">立即注册</a></span>
        </div>
    </div>
</div>
<!-- 版权 -->
<div class="footer">
    <div class="footer02">
        <div class="box_top">
            <p><span>@2016 global-mototparts.com 版权所有 <a href="http://www.miitbeian.gov.cn/" class="tdhover">粤ICP备16067887号</a></span><a href="javascript:;">著作权与商标声明</a>|<a href="javascript:;">法律声明</a>|<a href="javascript:;">服务条款</a>|<a href="javascript:;">隐私声明</a>|<a href="javascript:;">关于环球摩配网</a>|<a href="javascript:;">联系我们</a></p>
        </div>
        <div class="box_bottom">
            <p><a href="javascript:;">环球摩配网中国站</a> ｜ <a href="javascript:;">环球摩配网国际站</a></p>
        </div>
    </div>
</div>
<div style="display:none;width:0px;height:0px;">
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1260764455'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1260764455%26show%3Dpic1' type='text/javascript'%3E%3C/script%3E"));</script>
</div>
</body>
</html>
<script type="text/javascript">
    // 输入框禁止输入空格

function banInputSapce(e) {
	var keynum;
	if (window.event) {
		// IE 
		keynum = e.keyCode
	} else if (e.which) {
		// Netscape/Firefox/Opera 
		keynum = e.which
	}
	if (keynum == 32) {
		return false;
	}
	return true;
}

</script>