<%@ page language="java"  contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<%@ page import="com.ipinfo.main.Country"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.stream.Collectors" %>
<% 
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss") ;
Country country = (Country) request.getAttribute("country");
String languages = country.getLanguagesForPrint();
String currenciesValues = (String) request.getAttribute("rates"); 
Double[] coord = country.getCooridinates();
%>

<!DOCTYPE html>
<html>
<body>
<h2> IP <%=request.getParameter("ip") %></h2>
<span> Fecha y hora: <%=sdf.format(new Date()) %></span>
<ul>
<li><strong>Pais:</strong> <%=country.getName() %></li>
<li><strong>ISO Code:</strong> <%=country.getAlpha2Code() %></li>
<li><strong>Idiomas:</strong> <%= languages %></li>
<li><strong>Monedas:</strong> <%=currenciesValues %></li>
<li><strong>Hora:</strong> <%=country.getTimesForPrint() %></li>
<li><strong>Distancia:</strong> <%=country.getDistanceFromBA()+" (["+coord[0]+", "+coord[1]+"] a [-34.0, -64.0])" %></li>

</ul>
</body>
</html>