<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <title>Cadastrar Perfil | Livraria Aurora</title>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Gestão de Perfis de Usuário</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>

<section class="content">
    <h2>Cadastrar Perfil</h2>
    <p>Defina os perfis de acesso dos colaboradores da Livraria Aurora.</p>

    <form method="POST" action="gerenciar_perfil.do">
        <fieldset>
            <legend>Formulário de Perfil</legend>

            <input type="hidden" name="idPerfil" id="idPerfil" value="${perfil.idPerfil}"/>

            <label for="nome">Nome do Perfil</label>
            <input type="text" name="nome" id="nome" required maxlength="100" value="${perfil.nome}"/>

            <label for="status">Status</label>
            <select name="status" required>
                <c:choose>
                    <c:when test="${perfil.status==null}">
                        <option value="0">Escolha uma opção</option>
                        <option value="1">Ativo</option>
                        <option value="2">Inativo</option>
                    </c:when>
                    <c:when test="${perfil.status==1}">
                        <option value="1" selected>Ativo</option>
                        <option value="2">Inativo</option>
                    </c:when>
                    <c:when test="${perfil.status==2}">
                        <option value="1">Ativo</option>
                        <option value="2" selected>Inativo</option>
                    </c:when>
                </c:choose>
            </select>

            <!-- Botões -->
            <div class="btn-group-acao">
                <button type="submit" class="btn btn-success">Gravar</button>
                <a href="gerenciar_perfil.do?acao=listarTodos" class="btn btn-warning">Voltar</a>
            </div>
        </fieldset>
    </form>
</section>

<script src="datatables/jquery.js"></script>
<script src="datatables/jquery.dataTables.min.js"></script>
<script>
    function toggleMenu() {
        var menu = document.getElementById("nav-links");
        if (menu) {
            menu.classList.toggle("show");
        }
    }
</script>
</body>
</html>
