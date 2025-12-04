<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <title>Cadastrar Menu | Livraria Aurora</title>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Configuração do Menu de Navegação</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>

<section class="content">
    <h2>Cadastrar Menu</h2>
    <p>Defina os itens do menu que serão exibidos para os usuários da livraria.</p>

    <form method="POST" action="gerenciar_menu.do">
        <fieldset>
            <legend>Formulário de Menu</legend>

            <input type="hidden" name="idMenu" id="idMenu" value="${menu.idMenu}"/>

            <label for="nome">Nome do Menu</label>
            <input type="text" name="nome" id="nome" required maxlength="100" value="${menu.nome}"/>

            <label for="link">Link do Menu</label>
            <input type="text" name="link" id="link" required maxlength="100" value="${menu.link}"/>

            <label for="icone">Ícone do Menu</label>
            <input type="text" name="icone" id="icone" maxlength="100" value="${menu.icone}"/>

            <label for="exibir">Exibir</label>
            <select name="exibir" required>
                <c:choose>
                    <c:when test="${menu.exibir == null}">
                        <option value="0">Escolha uma opção</option>
                        <option value="1">Sim</option>
                        <option value="2">Não</option>
                    </c:when>
                    <c:when test="${menu.exibir == 1}">
                        <option value="1" selected>Sim</option>
                        <option value="2">Não</option>
                    </c:when>
                    <c:when test="${menu.exibir == 2}">
                        <option value="1">Sim</option>
                        <option value="2" selected>Não</option>
                    </c:when>
                </c:choose>
            </select>

            <div class="btn-group-acao" style="margin-top: 20px;">
                <button type="submit" class="btn btn-success">Gravar</button>
                <a href="gerenciar_menu.do?acao=listarTodos" class="btn btn-warning">Voltar</a>
            </div>
        </fieldset>
    </form>
</section>

<script src="datatables/jquery.js"></script>
<script src="datatables/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function () {
        $("#listarMenu").DataTable({
            "language": {
                "url": "datatables/portugues.json"
            }
        });
    });

    function toggleMenu() {
        var menu = document.getElementById("nav-links");
        if (menu) {
            menu.classList.toggle("show");
        }
    }
</script>
</body>
</html>
