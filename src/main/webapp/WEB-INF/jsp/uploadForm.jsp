<%@page contentType="text/html; charset=utf-8" %><%--
--%><!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8">
  <title>indexページ</title>
 </head>
 <body> 
  <c:url value="/upload" var="upload" />
  <c:out value="${saveLocation}" /><br>
  <form action="${upload}" method="post" enctype="multipart/form-data">
   <input type="file" name="uploadFile"><br>
   <input type="submit" value="アップロード">
  </form>
 </body>
</html>
