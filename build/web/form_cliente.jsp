<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <title>Cadastrar Cliente | Livraria Aurora</title>

    <!-- jQuery e máscara de telefone -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.16/jquery.mask.min.js"></script>
    <script>
        $(function(){
            var behavior = function(val){
                return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00000';
            };
            var options = {
                onKeyPress: function(val, e, field, opts){
                    field.mask(behavior(val), opts);
                }
            };
            $('#telefone').mask(behavior, options);
        });
    </script>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Cadastro de Clientes</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>

<section class="content">
    <h2>Cadastrar Cliente</h2>
    <p>Registre os clientes para facilitar as vendas da livraria.</p>

    <form method="POST" action="gerenciar_cliente.do">
        <fieldset>
            <legend>Formulário de Cliente</legend>

            <input type="hidden" name="idCliente" value="${cliente.idCliente}"/>

            <label for="nome">Nome do Cliente</label>
            <input type="text" name="nome" id="nome" required maxlength="100" value="${cliente.nome}"/>

            <label for="telefone">Telefone</label>
            <input type="text" name="telefone" id="telefone" required maxlength="100" value="${cliente.telefone}"/>

            <label for="status">Status</label>
            <select name="status" id="status" required>
                <c:choose>
                    <c:when test="${cliente.status==null}">
                        <option value="0">Escolha uma opção</option>
                        <option value="1">Ativo</option>
                        <option value="2">Inativo</option>
                    </c:when>
                    <c:when test="${cliente.status==1}">
                        <option value="1" selected>Ativo</option>
                        <option value="2">Inativo</option>
                    </c:when>
                    <c:when test="${cliente.status==2}">
                        <option value="1">Ativo</option>
                        <option value="2" selected>Inativo</option>
                    </c:when>
                </c:choose>
            </select>

            <div class="btn-group-acao" style="margin-top: 20px;">
                <button type="submit" class="btn btn-success">Gravar</button>
                <a href="gerenciar_cliente.do?acao=listarTodos" class="btn btn-warning">Voltar</a>
            </div>
        </fieldset>
    </form>
</section>

<script>
    function toggleMenu(){
        document.getElementById("nav-links").classList.toggle("show");
    }
</script>

</body>
</html>
