<%-- 
    Document   : index
    Created on : 12/08/2025, 21:29:37
    Author     : adm_pfelacio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Redirecionamento imediato para a tela de listagem de vendas
    // Assumindo que o vendedor é o usuário principal da aplicação
    response.sendRedirect("gerenciar_venda.do?acao=listarTodos");
%>