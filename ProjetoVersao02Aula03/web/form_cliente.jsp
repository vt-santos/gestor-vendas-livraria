<%-- 
    Document   : index
    Created on : 12/08/2025, 21:29:37
    Author     : adm_pfelacio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <link rel="stylesheet" href="css/estilo.css"/>
        <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" href="datatables/jquery.dataTables.min.css"/>
        <title>Cadastrar Cliente</title>
    </head>
    <body>
        <div class="banner">
            <%@include file="banner.jsp" %>
        </div>
        <!--Menu de navegação -->
        <%@include file="menu.jsp" %>
        
        <div class="content">
            <h2>Cadastrar Cliente</h2>
            <div class="form-container">
                <form method="POST" action="gerenciar_cliente.do">
                    <legend>Formulário de Cliente</legend>
                    <!-- ID oculto -->
                    <input type="hidden" name="idCliente" id="idCliente" value="${cliente.idCliente}"/>
                    
                    <label for="nome">Nome do Cliente</label>
                    <input type="text" name="nome" id="nome" required="" 
                           maxlength="100" value="${cliente.nome}"/>
                    
                    <label for="login">Telefone</label>
                    <input type="text" name="telefone" id="telefone" required="" 
                           maxlength="100" value="${cliente.telefone}"/>

                    <label for="status">Status</label>
                    <select name="status" required="">
                        <c:if test="${cliente.status==null}">  
                            <option value="0">Escolha uma opção</option>
                            <option value="1">Ativo</option>
                            <option value="2">Inativo</option>
                        </c:if>
                        <c:if test="${cliente.status==1}">
                            <option value="1" selected="">Ativo</option>
                            <option value="2">Inativo</option>
                        </c:if> 
                        <c:if test="${cliente.status==2}">
                            <option value="1">Ativo</option>
                            <option value="2" selected="">Inativo</option>
                        </c:if>    
                    </select>
                    
                    <!-- botoes-->
                    <div class="form button form">
                        <button type="submit" class="btn btn-success">Gravar</button>
                    <a href="gerenciar_cliente.do?acao=listarTodos" class="btn btn-default">Voltar</a>
                </form>
            </div>
        </div> 
        
        <!-- Scripts -->
        <script src="bootstrap/js/jquery.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
