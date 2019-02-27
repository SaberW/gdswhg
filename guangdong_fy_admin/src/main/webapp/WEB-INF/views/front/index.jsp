<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title-wxl</title>
    <%--<script src="${basePath}/statics/szwhg/js/default.js"></script>--%>
    <script>
        //Szwhg.init();
    </script>
</head>
<body>
hello world!

<ul>
<c:forEach items="${roles}" var="row" varStatus="vs">
    <li>${row.id}---------${row.name}</li>
</c:forEach>
</ul>

</body>
</html>
