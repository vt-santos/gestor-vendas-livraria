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
    <title>Listar Perfis | Livraria Aurora</title>

    <script>
        function confirmarExclusao(idPerfil, nome) {
            if (confirm('Deseja realmente desativar o perfil ' + nome + '?')) {
                window.open("gerenciar_perfil.do?acao=desativar&idPerfil=" + idPerfil, "_self");
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
        <p>Perfis & Acessos</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>

<section class="content">
    <h2>Listar Perfis</h2>
    <p>Gerencie os perfis de acesso dos usuários da livraria.</p>
    
    <div class="text-center mb-4">
        <a href="form_perfil.jsp" class="btn btn-success btn-lg">
            <i class="glyphicon glyphicon-plus"></i> Novo Cadastro
        </a>
    </div>

    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered display" id="listarPerfil">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome do Perfil</th>
                    <th>Status</th>
                    <th>Opções</th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <th>ID</th>
                    <th>Nome do Perfil</th>
                    <th>Status</th>
                    <th>Opções</th>
                </tr>
            </tfoot>
            <tbody>
                <c:forEach var="p" items="${listarTodos}">
                    <tr>
                        <td>${p.idPerfil}</td>
                        <td>${p.nome}</td>
                        <td>
                            <c:if test="${p.status==1}">Ativo</c:if>
                            <c:if test="${p.status==2}">Inativo</c:if>
                        </td>
                        <td>
                            <div class="btn-group-acao">
                                <a class="btn btn-primary btn-sm"
                                   href="gerenciar_perfil.do?acao=alterar&idPerfil=${p.idPerfil}"
                                   title="Editar Perfil">
                                    <i class="glyphicon glyphicon-pencil"></i>
                                </a>
                                <button class="btn btn-danger btn-sm"
                                        onclick="confirmarExclusao(${p.idPerfil}, '${p.nome}')"
                                        title="Desativar Perfil">
                                    <i class="glyphicon glyphicon-trash"></i>
                                </button>
                                <a href="gerenciar_menu_perfil.do?acao=gerenciar&idPerfil=${p.idPerfil}"
                                       class="btn btn-default btn-sm">
                                        Acessos
                                    </a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</section>

<script src="datatables/jquery.js"></script>
<script src="datatables/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function () {
        $("#listarPerfil").DataTable({
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
