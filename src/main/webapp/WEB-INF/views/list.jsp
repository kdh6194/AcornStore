<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>AS 쇼핑몰</title>
    <script src="https://kit.fontawesome.com/20962f3e4b.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="layout/css/common.css">
    <link rel="stylesheet" href="layout/css/product.css">
</head>
<body>
<div id="wrapper">
    <header>
        <div class="top">
            <c:choose>
                <c:when test="${null eq sessMember }">
                    <div>
                        <a href="/Kmarket1/member/login.do">로그인</a>
                        <a href="/Kmarket1/member/join.do">회원가입</a>
                    </div>
                </c:when>
                <c:when test="${null ne sessMember}">
                    <div>
                        <a href="/Kmarket1/member/logout.do?uid=${sessMember.uid}">로그아웃</a>
                        <a href="#">${sessMember.name}님</a>
                        <a href="/Kmarket1/member/myPage.do?uid=${sessMember.uid}">마이페이지</a>
                        <a href="/Kmarket1/product/cart.do?uid=${sessMember.uid}">
                            <i class="fa fa-shopping-cart" aria-hidden="true"></i>&nbsp;장바구니
                        </a>
                    </div>
                </c:when>
            </c:choose>
        </div>
    </header>
    <main id="product">
        <section class="list">
            <form action="/api/list" method="get">
                <label for="name">Search:</label>
                <input type="text" id="name" name="name" />

                <label for="category">Category:</label>
                <select name="category" id="category">
                    <option value="">All</option> <!-- Option for showing all products -->
                    <option value="electronics">Electronics</option>
                    <option value="clothing">Clothing</option>
                    <!-- Add more options for other categories as needed -->
                </select>

                <button type="submit">Search and Filter</button>
            </form>
            <ul class="sort">

            </ul>
<h1>Product List</h1>
<table border="0">
    <tbody>
    <tr>
        <th>Image</th>
        <th>Name</th>
        <th>Price</th>
    </tr>

    <c:forEach var="products" items="${products}">
    <tr>
        <td><a href="/Kmarket1/product/view.do?prodNo=${products.productId}&prodCate1=${products.categoryName}" class="goods">
            <img src="/resources/static/lazycat.png" alt="Product Image" width="100"/></a></td>
        <td><h3 class="name">${products.productName}</h3></td>
        <td>
            <ul>
                <li>a
                    <!-- del태그는 텍스트 한가운데 라인을 추가하여 문서에서 삭제된 텍스트 표현-->
                    <del class="original-price">${products.productPrice}</del>
                    <span class="discount">15%</span>
                </li>
                <li><ins class="discount-price"><fmt:formatNumber value="${products.productPrice * ((100 - 15)/100)}" pattern="#,###"/></ins></li>
            </ul>
        </td>
    </tr>
    </c:forEach>
    </tbody>
</table>

            <div class="paging"> <c:if test="${!page.isFirst()}">
                <span class="prev"> <a href="?page=${page.number - 1}">이전</a></span> </c:if>
                <c:forEach begin="0" end="${page.totalPages}" varStatus="i">
                    <c:choose>
                        <c:when test="${i.index == page.number}">
                            <!-- 현재 페이지 번호는 링크를 걸지 않음 -->
                            ${i.index + 1}
                        </c:when>
                        <c:otherwise>
                            <!-- 다른 페이지 번호에는 링크를 걸음 -->
                            <a href="?page=${i.index}">${i.index + 1}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${!page.isLast()}">
                    <span class="next"><a href="?page=${page.number + 1}">다음</a></span>
                </c:if>

            </div>

        </section>
    </main>
    <footer>
        <ul>
            <li>
                <a href="#">회사소개</a>
            </li>
            <li>
                <a href="#">서비스이용약관</a>
            </li>
            <li>
                <a href="#">개인정보처리방침</a>
            </li>
            <li>
                <a href="#">전자금융거래약관</a>
            </li>
        </ul>
        <div>
            <p>
                <strong>(주)AcornStore</strong><br>
                제주특별자치도 제주시 사라봉동길 74 (건입동)<br>
                대표이사 : Cat Lazy<br>
                사업자등록번호 : 220-81-83676 사업자정보확인<br>
                통신판매업신고 : 강남 10630호 Fax : 064-123-4567
            </p>
            <p>
                <strong>고객센터</strong><br>
                Tel : 1234-5678 (평일 09:00~18:00)<br>
                스마일클럽/SVIP 전용 : 1522-5700 (365일 09:00~18:00)<br>
                제주특별자치도 제주시 사라봉동길 74 (건입동)<br>
                Fax : 064-123-4567 | Mail : aws123@nate.com<br>
            </p>
        </div>
    </footer>
    <!-- 상단이동 버튼 -->
    <button type="button" id="top">상단이동</button>
</div>
    <!-- 페이징 처리 등... -->
</body>
</html>