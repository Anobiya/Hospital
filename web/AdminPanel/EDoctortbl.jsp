<%-- 
    Document   : E_Channel
    Created on : Sep 17, 2015, 6:34:56 PM
    Author     : User
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="Header.jsp"></jsp:include>

 <div class="ui grid">
        <div class="two wide column">                
        </div>
        <div class="twelve wide column">
            <div>
                <table class="ui celled table">
                    <thead>
                        <tr><th>Doctor Name</th>
                            <th>Special In</th>
                            <th>Status</th>
                            <th>Click Here To Channel</th>
                        </tr>
                    </thead>
                    <tbody>

                    <c:forEach items="${doctors}" var="doctor">
                        <tr>
                            <td class="center aligned">${doctor.userName}</td>
                            <td class="center aligned">${doctor.speciality}</td>
                            <td class="center aligned"><a href="">Click</td>
                            
                        </tr>
                    </c:forEach>

                </tbody>
                <tfoot>
                    <tr><th colspan="4">
                <div class="ui right floated pagination menu">
                    <a class="icon item">
                        <i class="left chevron icon"></i>
                    </a>
                    <a class="item">1</a>
                    <a class="item">2</a>
                    <a class="item">3</a>
                   
                    <a class="icon item">
                        <i class="right chevron icon"></i>
                    </a>
                </div>
                </th>
                </tr></tfoot>
            </table>
        </div>
    </div>

</div>




<jsp:include page="Footer.jsp"></jsp:include>
</body>
</html>