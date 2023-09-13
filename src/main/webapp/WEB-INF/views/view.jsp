<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>케이마켓::대한민국 1등 온라인 쇼핑몰</title>
    <script src="https://kit.fontawesome.com/20962f3e4b.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="layout/css/common.css">
    <link rel="stylesheet" href="layout/css/product.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script>
    	// 페이지 로딩때부터 판매가 * 1
    $(document).ready(function(){
    	var countnum = $('input[name = num]').val();
    	var num = parseInt(countnum);
    	var total = num * ${product.productPrice * ((100 - 15)/100)};
    	$('input[name=total]').attr('value',total);
	});
    $(function(){
    	// 감소 버튼 클릭시----------------------------------------------------------------
    	$('.decrease').click(function(){
    	var countnum = $('input[name = num]').val();
    	if(countnum > 1){
    	countnum--;
    	}
    	var num = parseInt(countnum);
    	$('input[name=num]').attr('value',num);
    	var total =num * ${product.productPrice * ((100 - 15)/100)};
    	$('input[name=total]').attr('value',total);
    	});

    	//증가 버튼 클릭시------------------------------------------------------------------
    	$('.increase').click(function(){
    	var countnum = $('input[name = num]').val();
    	countnum++;
    	var num = parseInt(countnum);
    	$('input[name=num]').attr('value',num);
    	var total = num * ${product.productPrice * ((100 - 15)/100)};
    	$('input[name=total]').attr('value',total);
    	});
    	//장바구니 버튼 클릭시-----------------------------------------------------------------	
    	// view에서 장바구니로 데이터 넘기기
    	$('.cart').click(function(){
    		
    		let uid = $('input[name=uid]').val();
			let prodNo = $('input[name=prodNo]').val();
			let count = $('input[name=num]').val();
			let price = $('input[name=productPrice]').val();
			let discount = $('input[name=discount]').val();
			let point = $('input[name=point]').val();
			let delivery = $('input[name=delivery]').val();
			let total = $('input[name=total]').val();
			let rdate = $('input[name=rdate]').val();
			/*let direct = '0';*/
			
			if(uid == ''){
				alert('로그인 후 이용해주세요');
				location.href= '/Kmarket1/member/login.do';
				}else{
			
    		let jsonData1 = {
    				"uid": uid,
    				"prodNo": prodNo,
    				"count": count,
    				"price": price,
    				"discount": discount,
    				"point": point,
    				"delivery": delivery,
    				"total": total,
    				"rdate": rdate
    				/*"direct": direct*/
    		};
    		$.ajax({
    			url: '/Kmarket1/product/view.do',
    			type: 'post',
    			data: jsonData1,
    			dataType: 'json',
    			success: function(data){
	    				if(data.result > 0){
							alert('장바구니에 추가완료');
							location.href = '/Kmarket1/product/cart.do?uid='+uid;
						}else{
							alert('오류발생! 다시 시도해주세요.')
						}
    			}
    		});
				}
    	});
    	///////////////////////////////////////////////////////////////////////
    	///////////////////////////////////////////////////////////////////////
    	//order버튼 누르면 장바구니랑 다르게 바로 주문으로 넘어가야해서 코드 하나 더 만들어야한다
    	<%--$('.order').click(function(){--%>
		<%--	let count = $('input[name=num]').val();--%>
    	<%--	let uid = "${sessMember.uid}";--%>

		<%--	--%>
		<%--	if(uid == ''){--%>
		<%--		alert('로그인 후 이용해주세요');--%>
		<%--		location.href= '/Kmarket1/member/login.do';--%>
		<%--		}else{--%>
		<%--			location.href= "/Kmarket1/product/order.do?prodNo="+${product.prodNo}+"&count="+count;--%>
		<%--		};--%>
		<%--	--%>
    	<%--});--%>
	//
    });
    </script>
    <style>
    </style>
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
            <section class="view">
                <!-- 상품 전체 내용 -->
                <article class="info">
                    <div class="image">
                        <img src="./img/460x460.png" alt="예시 이미지">
                    </div>
                    <div class="summary">
                        <nav>
                            <h1>${product.categoryName}</h1>
                            <h2>상품번호 : ${product.productId}</h2>
                            <input type="hidden" name="rdate" value="${product.createdAt}">
                        </nav>
                        <nav>
                            <h3>${product.productName}</h3>
                            <p>${product.productDetail}</p>
                            <h5 class="rating star4">
                                <a href="#">상품평보기</a>
                            </h5>
                        </nav>
                        <nav>
                            <div class="original-price">
                                <del><fmt:formatNumber value="${product.productPrice}" pattern="#,###"/></del>
                                <input type="hidden" name="price" value="${product.productPrice}">
                                <span>15%</span>
                            </div>
                            <div class="discount-price">
                                <ins><fmt:formatNumber value="${product.productPrice * ((100 - 15)/100)}" pattern="#,###"/></ins>
                            </div>
                        </nav>
                       
                        <div class="button">
                            <input type="button" class="cart" value="장바구니">
                            <input type="button" class="order" value="구매하기">
                        </div>
                    </div>
                </article>
                <!-- 상품정보 사진 -->
                <article class="detail">
                    <nav>
                        <h1>상품정보</h1>
                    </nav>
                    <img src="./img/860x460.png" alt="상품정보 이미지">
                    <img src="./img/860x460.png" alt="상품정보 이미지">
                    <img src="./img/860x460.png" alt="상품정보 이미지">
                </article>
                <!-- 상품정보 제공 고시내용-->
                <article class="notice">
                    <nav>
                        <h1>상품 정보 제공 고시</h1>
                        <p>[전자상거래에 관한 상품정보 제공에 관한 고시] 항목에 의거 등록된 정보입니다.</p>
                    </nav>

                    <table>
                        <tbody>
                            <tr>
                                <td>제품소재</td>
                                <td>상세정보 직접입력</td>
                            </tr>
                            <tr>
                                <td>색상</td>
                                <td>상세정보 직접입력</td>
                            </tr>
                            <tr>
                                <td>치수</td>
                                <td>상세정보 직접입력</td>
                            </tr>
                            <tr>
                                <td>제조자/수입국</td>
                                <td>상세정보 직접입력</td>
                            </tr>
                            <tr>
                                <td>제조국</td>
                                <td>상세정보 직접입력</td>
                            </tr>
                            <tr>
                                <td>취급시 주의사항</td>
                                <td>상세정보 직접입력</td>
                            </tr>
                            <tr>
                                <td>제조연월</td>
                                <td>상세정보 직접입력</td>
                            </tr>
                            <tr>
                                <td>품질보증기준</td>
                                <td>상세정보 직접입력</td>
                            </tr>
                            <tr>
                                <td>A/S 책임자와 전화번호</td>
                                <td>상세정보 직접입력</td>
                            </tr>
                            <tr>
                                <td>주문후 예상 배송기간</td>
                                <td>상세정보 직접입력</td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    구매, 교환, 반품, 배송, 설치 등과 관련하여 추가비용, 제한조건 등의 특이사항이 있는 경우
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <p class="notice">
                        소비자가 전자상거래등에서 소비자 보호에 관한 법률 제 17조 제1항 또는 제3항에 따라 청약철회를 하고
                        동법 제 18조 제1항 에 따라 청약철회한 물품을 판매자에게 반환하였음에도 불구 하고 결제 대금의
                        환급이 3영업일을 넘게 지연된 경우, 소비자 는 전자상거래등에서 소비자보호에 관한 법률 제18조
                        제2항 및 동법 시행령 제21조 2에 따라 지연일수에 대하여 전상법 시행령으로 정하는 이율을 곱하여
                        산정한 지연이자(“지연배상금”)를 신청할 수 있습니다. 아울러, 교환∙반품∙보증 및 결제대금의
                        환급신청은 [나의쇼핑정보]에서 하실 수 있으며, 자세한 문의는 개별 판매자에게 연락하여 주시기 바랍니다.
                    </p>
                </article>
                <!-- 상품 리뷰-->
                <article class="review">
                    <nav>
                        <h1>상품리뷰</h1>
                    </nav>
                    <ul>
		            <c:forEach var="review" items="${reviews}">
		                <li>
		                    <div>
		                        <h5 class="rating star${review.rating}">상품평</h5>
		                        <span>${review.uid}	${review.rdate}</span>
		                    </div>
		                    <h3>${review.prodName}</h3>
		                    <p>${review.content}</p>
		                </li>
		            </c:forEach>
            		</ul>    
                 <!-- 상품목록 페이지번호 -->
              	<!-- 리뷰 페이지 번호 -->
	            <div class="paging">
		            <span class="prev">
			            <c:if test="${pageGroupStart > 1}">
			                <a href="/Kmarket1/product/view.do?prodNo=${productId}&prodCate1=${categoryName}"><&nbsp;이전</a>
			            </c:if>
			        </span>
			        <span class="num">
			            <c:forEach var="num" begin="${pageGroupStart}" end="${pageGroupEnd}">
			                <a href="/Kmarket1/product/view.do?prodNo=${productId}&prodCate1=${categoryName}" class="num ${num == currentPage ? 'on':'off'}">${num}</a>
			            </c:forEach>
			        </span>
			        <span class="next">
			            <c:if test="${pageGroupEnd < lastPageNum}">
			                <a href="/Kmarket1/product/view.do?prodNo=${productId}&prodCate1=${categoryName}">다음&nbsp;></a>
			            </c:if>
		            </span>
	        	</div>
                </article>
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
                    <img src="./img/footer_logo.png" alt="로고">                </p>
                    <p>
                        <strong>(주)KMARKET</strong><br>
                        부산시 강남구 테헤란로 152 (역삼동 강남파이낸스센터)<br>
                        대표이사 : 홍길동<br>
                        사업자등록번호 : 220-81-83676 사업자정보확인<br>
                        통신판매업신고 : 강남 10630호 Fax : 02-589-8842
                      </p>
                      <p>
                        <strong>고객센터</strong><br>
                        Tel : 1234-5678 (평일 09:00~18:00)<br>
                        스마일클럽/SVIP 전용 : 1522-5700 (365일 09:00~18:00)<br>
                        경기도 부천시 원미구 부일로 223(상동) 투나빌딩 6층<br>
                        Fax : 051-123-4567 | Mail : kmarket@kmarket.co.kr<br>
                      </p>
            </div>
        </footer>
        <!-- 상단이동 버튼 -->
        <button type="button" id="top">상단이동</button>
    </div>    
</body>
</html>