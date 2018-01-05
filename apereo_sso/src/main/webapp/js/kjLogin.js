function getCookie(c_name) {
	if (document.cookie.length > 0) {
		c_start = document.cookie.indexOf(c_name + "=")
		if (c_start != -1) {
			c_start = c_start + c_name.length + 1
			c_end = document.cookie.indexOf(";", c_start)
			if (c_end == -1)
				c_end = document.cookie.length
			return unescape(document.cookie.substring(c_start, c_end))
		}
	}
	return ""
}
function setCookie(name,value)
{
var Days = 7;
var exp = new Date();
exp.setTime(exp.getTime() + Days*24*60*60*1000);
document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
function login(){
	document.getElementById("fm1").submit();
}
$(function() {
	//回车登录
	document.onkeydown=function(event){
        var e = event || window.event || arguments.callee.caller.arguments[0];
         if(e && e.keyCode==13){ // enter 键
        	 loginBefore();
        }
    }; 
	
	$("#btnLogin").click(function(event) {
		loginBefore();
	});
	function loginBefore(){
		var username = $("#username").val();
		var password = $("#password").val();
		if (username == '') {
			$(".ere").fadeIn(150).text("Please enter your account.");
            $(".login h2").animate({"margin":"36px 0 50px"},150);
			return false;
		}
		if (password == '') {
			$(".ere").fadeIn(150).text("Please enter your password.");
            $(".login h2").animate({"margin":"36px 0 50px"},150);
			return false;
		}
		setCookie("tempusername", username);
		login();
	}
	$("#fm1").on('keyup change',"#password",function(event){
		$(".ere").hide();
		$(".login h2").animate({"margin":"36px 0 20px"},150);
	});
	$(window).bind('ready', function() {
		$("#username").val(getCookie("username"));
		var aa = $("#formMsg").text();
		if(aa.indexOf("无效")!=-1){
			$(".ere").fadeIn(150).text("Your email or password is incorrect.");
			$(".login h2").animate({"margin":"36px 0 50px"},150);
		}
	});
	
	// 脚部定位
    function positionFooter(){
        var footer_H = $(".footer").height();
        var footer_T = ($(window).scrollTop()+$(window).height()-footer_H);
        var win_H = $(window).height();
        if(($(document.body).height()+footer_H-23)<win_H){
            $(".footer").css({"position":"absolute","left":"0","top":""+(footer_T-23)+"px"});
        }else{
            $(".footer").css({"position":"static"});
        }
    }
    positionFooter();
    $(window).scroll(positionFooter).resize(positionFooter);
});