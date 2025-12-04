<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="datatables/jquery.dataTables.min.css"/>
    <title>Vincular e Desvincular Menus | Livraria Aurora</title>
    <script>
        function confirmarExclusao(idMenu, nome, idPerfil) {
            if (confirm('Deseja realmente desvincular o menu ' + nome + '?')) {
                window.location = "gerenciar_menu_perfil.do?acao=desvincular&idMenu=" + idMenu + "&idPerfil=" + idPerfil;
            }
        }

        function toggleMenu() {
            var menu = document.getElementById("nav-links");
            if (menu) {
                menu.classList.toggle("show");
            }
        }
    </script>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Gerenciamento de Menus por Perfil</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>

<section class="content">
    <h2>Vincular Menu ao Perfil</h2>
    <p>Associe menus de navegação ao perfil selecionado.</p>

    <form method="POST" action="gerenciar_menu_perfil.do">
        <fieldset>
            <legend>Formulário de Menu Perfil</legend>

            <input type="hidden" name="idPerfil" id="idPerfil" value="${perfil.idPerfil}"/>

            <label>Perfil: ${perfil.nome}</label>

            <label for="idMenu">Menus Desvinculados</label>
            <select name="idMenu" required>
                <option value="">Selecione o menu</option>
                <c:forEach items="${perfil.menus.menusNaoVinculados}" var="m">
                    <option value="${m.idMenu}">${m.nome}</option>
                </c:forEach>
            </select>

            <div class="btn-group-acao" style="margin-top: 20px;">
                <button type="submit" class="btn btn-success">Vincular</button>
                <a href="gerenciar_perfil.do?acao=listarTodos" class="btn btn-warning">Voltar</a>
            </div>
        </fieldset>
    </form>

    <div style="margin-top: 50px;">
        <h2>Menus Vinculados</h2>
        <p>Esses são os menus atualmente associados ao perfil <strong>${perfil.nome}</strong>.</p>

        <div class="table-responsive">
            <table class="table table-hover table-striped table-bordered display" id="listarMenu">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome do Menu</th>
                        <th>Link</th>
                        <th>Ação</th>
                    </tr>
                </thead>
                <tfoot>
                    <tr>
                        <th>ID</th>
                        <th>Nome do Menu</th>
                        <th>Link</th>
                        <th>Ação</th>
                    </tr>
                </tfoot>
                <tbody>
                    <c:if test="${not empty perfil.menus.menusVinculados}">
                        <c:forEach var="m" items="${perfil.menus.menusVinculados}">
                            <tr>
                                <td>${m.idMenu}</td>
                                <td>${m.nome}</td>
                                <td>${m.link}</td>
                                <td>
                                    <button class="btn btn-danger btn-sm"
                                            onclick="confirmarExclusao(${m.idMenu}, '${m.nome}', ${perfil.idPerfil})"
                                            title="Desvincular menu">
                                        <i class="glyphicon glyphicon-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</section>

<script src="datatables/jquery.js"></script>
<script src="datatables/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function () {
        $("#listarMenu").DataTable({
            "language": {
                "url": "datatables/portugues.json"
            },
            "pagingType": "full_numbers",
            "pageLength": 10,
            "lengthMenu": [5, 10, 25, 50],
            "dom": '<"top"lf>rt<"bottom"ip><"clear">'
        });
    });
</script>

</body>
</html>
