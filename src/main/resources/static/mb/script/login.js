
$(function() {
    $(".user").bind('click', function(e) {
       /* var $current = e.target;
        if ($($current).hasClass("safe")) {
            $($current).addClass("active");
            $(".bottom_block").animate({
                left: "0px"
            }, 100);
            $(".user").removeClass("active");
            $(".safeBox").addClass("show").removeClass("hide");
            $(".userBox").addClass("hide").removeClass("show");
        } else {*/
            $($current).addClass("active");
            $(".bottom_block").animate({
                left: "135px"
            }, 100);
            $(".safe").removeClass("active");
            $(".safeBox").addClass("hide").removeClass("show");
            $(".userBox").addClass("show").removeClass("hide");

    });



    // 登录
    $(".loginBtn").click(function(e) {
        var $user = $(".userTxt");
        var $pass = $(".passBox");
        if ($user.val() == "") {
            $(".errorPass").css('display', 'none');
            $(".errorUser").css('display', 'block');
        } else if ($pass.val() == "") {
            $(".errorUser").css('display', 'none');
            $(".errorPass").css('display', 'block');
        } else {
            var userName = $user.val();
            var passWord = $pass.val();
            $.ajax({
                type: "GET",
                url: "login.php",
                data: {
                    user: userName,
                    pass: passWord
                },
                success: function(data, status) {
                    if (data == "[]") {
                        alert("用户名或密码有误！")
                    } else {
                        var json = JSON.parse(data);
                        var name = encodeURIComponent(json[0].userName);
                        window.location.href = "22.html";
                        setCookie('userName', name);
                        // document.cookie='userName='+encodeURIComponent("索尼中国");
                    }
                }
            })
        }
    });
    $(".userTxt,.passBox").blur(function(e) {
        var $current = e.target;
        if ($($current).val() != "") {
            $(".errorUser").css('display', 'none');
            $(".errorPass").css('display', 'none');
        }
    });

 
   
});




function openReg(){
    document.querySelector(".register").style.display="block";
}
function closeReg() {
    document.querySelector(".register").style.display="none";
}