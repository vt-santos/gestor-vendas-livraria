<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/estilo.css">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="datatables/jquery.dataTables.min.css">
    <title>Listar Vendas | Livraria Aurora</title>

    <script>
        function confirmarDesativar(idVenda, nome){
            if(confirm("Deseja realmente cancelar a venda do cliente " + nome + "?")){
                window.location = "gerenciar_venda.do?acao=cancelar&idVenda=" + idVenda;
            }
        }
        function toggleMenu(){
            var menu = document.getElementById("nav-links");
            if (menu) {
                menu.classList.toggle("show");
            }
        }
    </script>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp"%>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Histórico de Vendas</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp"%>
</nav>

<section class="content">
    <h2>Vendas Realizadas</h2>
    <p>Gerencie todas as vendas realizadas pela livraria.</p>

    <div class="text-center mb-4">
        <a href="gerenciar_venda.do?acao=visualizarRelatorio" class="btn btn-info btn-lg">
            <i class="glyphicon glyphicon-stats"></i> Visualizar Relatório
        </a>
        <a href="gerenciar_cliente.do?acao=listarTodos" class="btn btn-success btn-lg">
            <i class="glyphicon glyphicon-plus"></i> Nova Venda
        </a>
    </div>


    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered display" id="listarVenda">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Vendedor</th>
                    <th>Cliente</th>
                    <th>Data</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <th>ID</th>
                    <th>Vendedor</th>
                    <th>Cliente</th>
                    <th>Data</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </tfoot>
            <tbody>
                <c:forEach var="v" items="${listarTodos}">
                    <tr>
                        <td>${v.idVenda}</td>
                        <td>${v.vendedor.nome}</td>
                        <td>${v.cliente.nome}</td>
                        <td><fmt:formatDate value="${v.dataVenda}" pattern="dd/MM/yyyy"/></td>
                        <td>
                            <c:if test="${v.status==1}">Ativa</c:if>
                            <c:if test="${v.status==2}">Cancelada</c:if>
                        </td>
                        <td>
                            <div class="btn-group-acao">
                                <a class="btn btn-warning btn-sm" 
                                   href="gerenciar_venda.do?acao=visualizar&idVenda=${v.idVenda}"
                                   title="Visualizar Venda">
                                    <i class="glyphicon glyphicon-eye-open"></i>
                                </a>
                                <a class="btn btn-primary btn-sm" 
                                   href="gerenciar_venda.do?acao=alterar&idVenda=${v.idVenda}"
                                   title="Editar Venda">
                                    <i class="glyphicon glyphicon-pencil"></i>
                                </a>
                                <button class="btn btn-danger btn-sm"
                                        onclick="confirmarDesativar(${v.idVenda}, '${v.cliente.nome}')"
                                        title="Cancelar Venda">
                                    <i class="glyphicon glyphicon-trash"></i>
                                </button>
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
    $(document).ready(function() {
        $("#listarVenda").DataTable({
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
