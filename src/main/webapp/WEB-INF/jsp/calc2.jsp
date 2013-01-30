<%@page contentType="text/html; charset=utf-8" %><%--
--%><!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8">
  <title>答えのページ</title>
 </head>
 <body>
  <c:out value="${addModel.num1}" /> + <%--
 --%><c:out value="${addModel.num2}" /> = <%--
 --%><c:out value="${addModel.answer}" />
 </body>
</html>
