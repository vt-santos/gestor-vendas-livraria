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
    <title>Relatório de Estoque | Livraria Aurora</title>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>
    <script>
        function toggleMenu(){
            var menu = document.getElementById("nav-links");
            if (menu) {
                menu.classList.toggle("show");
            }
        }
        
        function gerarPDFEstoque() {
            const { jsPDF } = window.jspdf;
            const element = document.querySelector('.content'); // Elemento que contém o relatório
            
            // Oculta elementos que não devem aparecer no PDF (menus, botões de ação, etc.)
            const buttons = element.querySelectorAll('.text-center a, .text-center button, .btn-xs');
            buttons.forEach(btn => btn.style.display = 'none');
            
            html2canvas(element, { scale: 2 }).then(canvas => {
                const imgData = canvas.toDataURL('image/png');
                const pdf = new jsPDF('p', 'mm', 'a4');
                const imgProps= pdf.getImageProperties(imgData);
                const pdfWidth = pdf.internal.pageSize.getWidth();
                const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
                
                pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
                pdf.save("relatorio_estoque.pdf");
                
                // Restaura a exibição dos botões
                buttons.forEach(btn => btn.style.display = 'inline-block');
            });
        }
    </script>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Relatório de Estoque</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>

<section class="content">
    <h2>Relatório de Estoque</h2>
    <p>Visualize o status atual do estoque de produtos.</p>
    
    <div class="text-center" style="display: flex; justify-content: center; gap: 15px; margin-bottom: 40px;">
        <a href="gerenciar_estoque.do?acao=novaMovimentacao" class="btn btn-success" style="width: 33%;">
            <i class="glyphicon glyphicon-plus"></i> Nova Movimentação
        </a>
        <a href="gerenciar_estoque.do?acao=listarMovimentacoes" class="btn btn-info" style="width: 33%;">
            <i class="glyphicon glyphicon-list"></i> Histórico de Movimentações
        </a>
        <button onclick="gerarPDFEstoque()" class="btn btn-danger" style="width: 33%;">
            <i class="glyphicon glyphicon-file"></i> Gerar PDF
        </button>
    </div>
    
    <!-- Cards de Estatísticas -->
    <div class="row stats-cards">
        <div class="col-md-3">
            <div class="stats-card bg-primary">
                <div class="stats-icon">
                    <i class="glyphicon glyphicon-th-large"></i>
                </div>
                <div class="stats-info">
                    <h3>${totalProdutos}</h3>
                    <p>Total de Produtos</p>
                </div>
            </div>
        </div>
        
        <div class="col-md-3">
            <div class="stats-card bg-success">
                <div class="stats-icon">
                    <i class="glyphicon glyphicon-ok-circle"></i>
                </div>
                <div class="stats-info">
                    <h3>${produtosAtivos}</h3>
                    <p>Produtos Ativos</p>
                </div>
            </div>
        </div>
        
        <div class="col-md-3">
            <div class="stats-card bg-danger">
                <div class="stats-icon">
                    <i class="glyphicon glyphicon-exclamation-sign"></i>
                </div>
                <div class="stats-info">
                    <h3>${produtosSemEstoque}</h3>
                    <p>Sem Estoque</p>
                </div>
            </div>
        </div>
        
        <div class="col-md-3">
            <div class="stats-card bg-info">
                <div class="stats-icon">
                    <i class="glyphicon glyphicon-usd"></i>
                </div>
                <div class="stats-info">
                    <h3>R$ <fmt:formatNumber value="${valorTotalEstoque}" pattern="#,##0.00"/></h3>
                    <p>Valor Total em Estoque</p>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Alerta de Produtos com Estoque Baixo -->
    <c:if test="${not empty produtosEstoqueBaixo}">
        <div class="alert alert-warning">
            <h4><i class="glyphicon glyphicon-warning-sign"></i> Atenção: Produtos com Estoque Baixo</h4>
            <p>Os seguintes produtos estão com estoque abaixo de 10 unidades:</p>
            <ul>
                <c:forEach var="p" items="${produtosEstoqueBaixo}">
                    <li>
                        <strong>${p.nome}</strong> - 
                        <span class="text-danger">Estoque: ${p.qtd} unidades</span>
                        <a href="gerenciar_estoque.do?acao=listarPorProduto&idProduto=${p.idProduto}" 
                           class="btn btn-xs btn-info">Ver Histórico</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
    
    <!-- Tabela de Produtos -->
    <h3>Estoque de Produtos</h3>
    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered display" id="tabelaEstoque">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Produto</th>
                    <th>Quantidade</th>
                    <th>Valor Unitário</th>
                    <th>Valor Total</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <th>ID</th>
                    <th>Produto</th>
                    <th>Quantidade</th>
                    <th>Valor Unitário</th>
                    <th>Valor Total</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </tfoot>
            <tbody>
                <c:forEach var="p" items="${produtos}">
                    <tr class="${p.qtd == 0 ? 'danger' : (p.qtd <= 10 ? 'warning' : '')}">
                        <td>${p.idProduto}</td>
                        <td>${p.nome}</td>
                        <td class="text-right">
                            <strong>${p.qtd}</strong>
                            <c:if test="${p.qtd == 0}">
                                <span class="label label-danger">SEM ESTOQUE</span>
                            </c:if>
                            <c:if test="${p.qtd > 0 && p.qtd <= 10}">
                                <span class="label label-warning">BAIXO</span>
                            </c:if>
                        </td>
                        <td class="text-right">R$ <fmt:formatNumber value="${p.valor}" pattern="#,##0.00"/></td>
                        <td class="text-right">R$ <fmt:formatNumber value="${p.qtd * p.valor}" pattern="#,##0.00"/></td>
                        <td>
                            <c:if test="${p.status==1}">
                                <span class="label label-success">Ativo</span>
                            </c:if>
                            <c:if test="${p.status==2}">
                                <span class="label label-default">Inativo</span>
                            </c:if>
                        </td>
                        <td>
                            <a href="gerenciar_estoque.do?acao=listarPorProduto&idProduto=${p.idProduto}" 
                               class="btn btn-info btn-xs" title="Ver Histórico">
                                <i class="glyphicon glyphicon-list"></i> Histórico
                            </a>
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
    $(document).ready(function(){
        $("#tabelaEstoque").DataTable({
            "language":{
                "url": "datatables/portugues.json"
            },
            "pagingType": "full_numbers",
            "pageLength": 10,
            "lengthMenu": [5, 10, 25, 50],
            "order": [[2, "asc"]], // Ordena por quantidade (produtos com menos estoque primeiro)
            "dom": '<"top"lf>rt<"bottom"ip><"clear">'
        });
    });
</script>



</body>
</html>
