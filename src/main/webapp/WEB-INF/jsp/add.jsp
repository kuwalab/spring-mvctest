<%@page contentType="text/html; charset=utf-8" %><%--
--%><!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8">
  <title>足し算のページ</title>
 </head>
 <body>
  <c:url value="/calc" var="calc" />
  <form:form modelAttribute="addModel" action="${calc}" method="POST">
   <form:input path="num1" size="3" /> + <form:input path="num2" size="3" /> <%--
--%><input type="submit" value="＝">
  </form:form>
 </body>
</html>
