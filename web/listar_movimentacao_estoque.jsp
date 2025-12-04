<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="datatables/jquery.dataTables.min.css"/>
    <title>Histórico de Movimentações | Livraria Aurora</title>

    <script>
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
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Histórico de Movimentações de Estoque</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>

<section class="content">
    <h2>Histórico de Movimentações de Estoque</h2>
    
    <c:if test="${not empty produtoFiltro}">
        <div class="alert alert-info">
            <strong>Filtro ativo:</strong> Exibindo movimentações do produto: ${produtoFiltro.nome}
            <a href="gerenciar_estoque.do?acao=listarMovimentacoes" class="btn btn-sm btn-default">
                Remover Filtro
            </a>
        </div>
    </c:if>
    
    <p>Visualize todas as movimentações de entrada, saída e ajuste de estoque.</p>
    
    <div class="text-center mb-4">
        <a href="gerenciar_estoque.do?acao=novaMovimentacao" class="btn btn-success btn-lg">
            <i class="glyphicon glyphicon-plus"></i> Nova Movimentação
        </a>
        <a href="gerenciar_estoque.do?acao=relatorio" class="btn btn-info btn-lg">
            <i class="glyphicon glyphicon-stats"></i> Relatório de Estoque
        </a>
    </div>

    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered display" id="listarMovimentacao">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Data/Hora</th>
                    <th>Produto</th>
                    <th>Tipo</th>
                    <th>Quantidade</th>
                    <th>Qtd Anterior</th>
                    <th>Qtd Atual</th>
                    <th>Usuário</th>
                    <th>Motivo</th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <th>ID</th>
                    <th>Data/Hora</th>
                    <th>Produto</th>
                    <th>Tipo</th>
                    <th>Quantidade</th>
                    <th>Qtd Anterior</th>
                    <th>Qtd Atual</th>
                    <th>Usuário</th>
                    <th>Motivo</th>
                </tr>
            </tfoot>
            <tbody>
                <c:forEach var="m" items="${listarTodos}">
                    <tr>
                        <td>${m.idMovimentacao}</td>
                        <td>
                            <fmt:formatDate value="${m.dataMovimentacao}" 
                                          pattern="dd/MM/yyyy HH:mm:ss"/>
                        </td>
                        <td>${m.produto.nome}</td>
                        <td>
                            <c:choose>
                                <c:when test="${m.tipoMovimentacao == 'ENTRADA'}">
                                    <span class="label label-success">ENTRADA</span>
                                </c:when>
                                <c:when test="${m.tipoMovimentacao == 'SAIDA'}">
                                    <span class="label label-warning">SAÍDA</span>
                                </c:when>
                                <c:when test="${m.tipoMovimentacao == 'VENDA'}">
                                    <span class="label label-primary">VENDA</span>
                                </c:when>
                                <c:when test="${m.tipoMovimentacao == 'AJUSTE'}">
                                    <span class="label label-info">AJUSTE</span>
                                </c:when>
                            </c:choose>
                        </td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${m.tipoMovimentacao == 'ENTRADA'}">
                                    <span class="text-success">+${m.quantidade}</span>
                                </c:when>
                                <c:when test="${m.tipoMovimentacao == 'SAIDA' || m.tipoMovimentacao == 'VENDA'}">
                                    <span class="text-danger">-${m.quantidade}</span>
                                </c:when>
                                <c:otherwise>
                                    ${m.quantidade}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right">${m.quantidadeAnterior}</td>
                        <td class="text-right"><strong>${m.quantidadeAtual}</strong></td>
                        <td>${m.usuario.nome}</td>
                        <td>${m.motivo}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</section>

<script src="datatables/jquery.js"></script>
<script src="datatables/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function(){
        $("#listarMovimentacao").DataTable({
            "language":{
                "url": "datatables/portugues.json"
            },
            "pagingType": "full_numbers",
            "pageLength": 10,
            "lengthMenu": [5, 10, 25, 50, 100],
            "order": [[0, "desc"]], // Ordena por ID decrescente (mais recentes primeiro)
            "dom": '<"top"lf>rt<"bottom"ip><"clear">'
        });
    });
</script>

<style>
    .mb-4 {
        margin-bottom: 20px;
    }
    
    .text-right {
        text-align: right;
    }
    
    .label {
        display: inline-block;
        padding: 4px 8px;
        font-size: 11px;
        font-weight: bold;
        line-height: 1;
        color: #fff;
        text-align: center;
        white-space: nowrap;
        vertical-align: baseline;
        border-radius: 3px;
    }
    
    .label-success {
        background-color: #5cb85c;
    }
    
    .label-warning {
        background-color: #f0ad4e;
    }
    
    .label-primary {
        background-color: #337ab7;
    }
    
    .label-info {
        background-color: #5bc0de;
    }
    
    .text-success {
        color: #5cb85c;
        font-weight: bold;
    }
    
    .text-danger {
        color: #d9534f;
        font-weight: bold;
    }
    
    .alert {
        padding: 15px;
        margin-bottom: 20px;
        border: 1px solid transparent;
        border-radius: 4px;
    }
    
    .alert-info {
        color: #31708f;
        background-color: #d9edf7;
        border-color: #bce8f1;
    }
</style>

</body>
</html>
