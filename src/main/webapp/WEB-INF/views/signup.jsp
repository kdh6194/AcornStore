<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AcornStore</title>
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css"
          integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous" />
</head>
<body>
<div class="container">
    <main class="loginMain">
        <!--회원가입섹션-->
        <section class="login">
            <article class="login__form__container">

                <!--회원가입 폼-->
                <div class="login__form">
                    <!--로고-->
                    <h1><img src="/images/logo.jpg" alt=""></h1>
                    <!--로고end-->

                    <!--회원가입 인풋-->
                    <form class="login__input" action="/auth/signup" method="post" enctype="multipart/form-data">
                        <input type="email" name="email" placeholder="이메일" required="required" />
                        <input type="password" name="password" placeholder="비밀번호" required="required" />
                        <input type="text" name="phone" placeholder="전화번호"  />
                        <input type="text" name="address" placeholder="주소"  />
                        <input type="text" name="gender" placeholder="성별"  />
                        <input type="text" name="about_me" placeholder="간단한 소개"  />
                        <input type="file" name="profileImg" placeholder="프로필 이미지"  />
                        <button>가입</button>
                    </form>
                    <!--회원가입 인풋end-->
                </div>
                <!--회원가입 폼end-->

                <!--계정이 있으신가요?-->
                <div class="login__register">
                    <span>계정이 있으신가요?</span>
                    <a href="/auth/signin">로그인</a>
                </div>
                <!--계정이 있으신가요?end-->

            </article>
        </section>
    </main>
</div>

</body>
</html>