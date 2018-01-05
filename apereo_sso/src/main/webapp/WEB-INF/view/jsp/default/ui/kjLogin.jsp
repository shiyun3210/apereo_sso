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
<c:set var="imgUrl" value="http://img.gmpmart.com/zk_file/resource/load?path=" />
<c:set var="kuc" value="http://uc.gmpmart.com" />
<meta charset="utf-8">
<meta name="keywords" content="gmpmart,gmpmart,gmpmart,gmpmart,gmpmart">
<title>Sign In-GMPMart.com</title>
<meta name="keywords" content="GMPMart, Global Motor Parts, Motorcycle Parts Purchase, Motorcycle Parts Wholesale, Motorcycle Parts Price & Motorcycle Parts "/>
<meta name="description" content="GMPMart.com adopts “Internet Thinking” to analyze the industrial channels and structures, and serves Chinese manufacturers, traders and global buyers. It devotes to creating a world-leading e-commerce platform for motorcycle parts which has standardized industry, collaborative service and streamline system, to make motorcycle parts industry combine information flow and deal flow. "/>
<title>环球摩配网</title>
<link rel="shortcut icon" href="${ctx}/images/common/favicon.ico" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/kjcss/common.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/kjcss/login.css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/kjLogin.js"></script>
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
    <div class="header01 autowrap clearfix">
        <div class="logo fl">
            <h1><a href="http://www.gmpmart.com" title="环球摩配网">环球摩配网</a></h1>
        </div>
    </div>
</div>
<div class="loginbox wrap clearfix">
    <div class="imgbox fl">
        <a href="http://www.gmpmart.com" target="_blank">
        <img src="${imgUrl }resources/20170526/20170526115301649571_550x370.jpg" alt="" >
        </a>
    </div>
    <div class="login fr">
    	<form:form method="post" id="fm1" commandName="${commandName}" htmlEscape="true">
    		<div id="formMsg" style="display:none;"><form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" /></div>
    		<h2>Sign in<div class="ere"></div></h2>
			<input type="hidden" name="lt" value="${loginTicket}" />
            <input type="hidden" name="execution" value="${flowExecutionKey}" />
            <input type="hidden" name="_eventId" value="submit" />
            
            <div class="same phone">
                <i></i>
                <input type="text" id="username" placeholder="Email" value="" name="username" />
            </div>
            <div class="same password">
                <i></i>
                <input type="password" id="password" placeholder="Password" value="" name="password" onkeydown="return banInputSapce(event);"/>
            </div>
            <input type="button" id="btnLogin" value="Sign In" class="submit"/>
    	</form:form>
        <div class="txtbtn clearfix">
            <span class="fl tdhover"><a href="${kuc }/kjUser/modifypass">Forgot Password? </a></span>
            <span class="fr"><a href="${kuc }/kjUser/register" class="tdhover">Join Free</a></span>
        </div>
    </div>
</div>
<div class="footer">
    <div class="footer02">
        <div class="box_bottom">
            <p><a href="http://www.gmpmart.com">GMPmart.com</a> ｜ <a href="http://seller.gmpmart.com">我是供应商</a></p>
        </div>
        <div class="box_top">
            <p><!--<a href="javascript:;">Product Listing Policy</a> ｜ <a href="javascript:;">Intellectual Property Policy and Infringement Claims</a> ｜--> <a href="http://www.gmpmart.com/help/document/58.html" target="_blank">About Us</a> ｜ <a href="http://www.gmpmart.com/help/document/6.html"  target="_blank" >Terms of Use</a></p>
            <p>© 2016 GMPmart.com.  All rights reserved. <a href="http://www.miitbeian.gov.cn/" class="tdhover">粤ICP备16067887号</a></p>
        </div>
    </div>
</div>

<div style="display:none;width:0px;height:0px;">
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1260764531'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1260764531%26show%3Dpic1' type='text/javascript'%3E%3C/script%3E"));</script>
</div>
</body>
</html>
<script type="text/javascript">
    // 输入框禁止输入空格
function banInputSapce(e){ 
    var keynum;
    if(window.event){ 
        // IE 
        keynum = e.keyCode 
    }else if(e.which){ 
        // Netscape/Firefox/Opera 
        keynum = e.which 
    } 
    if(keynum == 32){ 
        return false; 
    } 
        return true; 
} 
</script>
