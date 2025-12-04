<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <title>Cadastrar Usuário | Livraria Aurora</title>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Cadastro de Usuários do Sistema</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>
    
<section class="content">
    <h2>Cadastrar Usuário</h2>
    <p>Defina os usuários que terão acesso ao sistema da livraria.</p>

    <form method="POST" action="gerenciar_usuario.do">
        <fieldset>
            <legend>Formulário de Usuário</legend>

            <!-- ID oculto -->
            <input type="hidden" name="idUsuario" id="idUsuario" value="${usuario.idUsuario}"/>

            <label for="nome">Nome do Usuário</label>
            <input type="text" name="nome" id="nome" required maxlength="100" value="${usuario.nome}"/>

            <label for="login">Login</label>
            <input type="text" name="login" id="login" required maxlength="100" value="${usuario.login}"/>

            <label for="senha">Senha</label>
            <input type="password" name="senha" id="senha" required maxlength="100" value="${usuario.senha}"/>

            <label for="dataNasc">Data de Nascimento</label>
            <input type="date" name="dataNasc" id="dataNasc" value="${usuario.dataNasc}"/>

            <label for="idPerfil">Perfil</label>
            <select name="idPerfil" id="idPerfil" required>
                <option value="">Selecione o Perfil</option>
                <jsp:useBean class="modelo.PerfilDAO" id="perfil"/>
                <c:forEach var="p" items="${perfil.lista}">
                    <option value="${p.idPerfil}"
                        <c:if test="${p.idPerfil==usuario.perfil.idPerfil}">
                            selected
                        </c:if>>${p.nome}</option> 
                </c:forEach>
            </select>

            <label for="status">Status</label>
            <select name="status" required>
                <c:choose>
                    <c:when test="${usuario.status==null}">
                        <option value="0">Escolha uma opção</option>
                        <option value="1">Ativo</option>
                        <option value="2">Inativo</option>
                    </c:when>
                    <c:when test="${usuario.status==1}">
                        <option value="1" selected>Ativo</option>
                        <option value="2">Inativo</option>
                    </c:when>
                    <c:when test="${usuario.status==2}">
                        <option value="1">Ativo</option>
                        <option value="2" selected>Inativo</option>
                    </c:when>
                </c:choose>
            </select>

            <!-- Botões -->
            <div class="btn-group-acao">
                <button type="submit" class="btn btn-success">Gravar</button>
                <a href="gerenciar_usuario.do?acao=listarTodos" class="btn btn-warning">Voltar</a>
            </div>
        </fieldset>
    </form>
</section>

<script src="datatables/jquery.js"></script>
<script src="datatables/jquery.dataTables.min.js"></script>
<script>
    function toggleMenu(){
        var menu = document.getElementById("nav-links");
        if (menu) {
            menu.classList.toggle("show");
        }
    }
</script>    
</body>
</html>
