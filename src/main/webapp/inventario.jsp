<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.productos.datos.Producto" %>
<%@ page import="com.productos.negocio.OperacionesProducto" %>
<%
    // ESCUDO DE SEGURIDAD MILITAR
    if (session.getAttribute("usuario_valido") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    int rol = (Integer) session.getAttribute("usuario_rol");
    if (rol > 2) {
        response.sendRedirect("perfil.jsp"); // Expulsar clientes
        return;
    }

    String status = request.getParameter("status");
    String colorRol = "#D4FF00"; // AMARILLO NEÓN PARA GESTOR LOGÍSTICO
    OperacionesProducto op = new OperacionesProducto();
    List<Producto> inventarioGeneral = op.obtenerInventario();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>PANEL LOGÍSTICO // VECTORHEART</title>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Space+Mono:wght@400;700&display=swap" rel="stylesheet">
    <style>
        body { margin: 0; padding: 0; overflow-x: hidden; background: var(--bg); }
        .inventario-layout {
            display: flex; flex-direction: column; min-height: 100vh; padding-top: 35px;
            background: radial-gradient(circle at top, #111 0%, #050505 100%);
        }
        .inventario-main {
            padding: 40px 8%; flex: 1;
        }
        .smart-input-group { margin-bottom: 20px; position: relative; }
        .smart-input-group label { font-family: var(--font-tech); color: #888; font-size: 0.8rem; margin-bottom: 8px; display: block; }
        .smart-input-group input, .smart-input-group select {
            width: 100%; background: #000; border: 1px solid #333; color: #FFF;
            padding: 15px; font-family: var(--font-tech); font-size: 1rem; border-radius: 4px; transition: 0.3s;
        }

        /* FORZAR MODO OSCURO EN MENÚS DESPLEGABLES NATIVOS */
        .smart-input-group select { color-scheme: dark; }
        .smart-input-group select option { background: #000; color: #FFF; }

        .smart-input-group input:focus, .smart-input-group select:focus {
            border-color: <%=colorRol%>; outline: none; box-shadow: 0 0 10px rgba(212,255,0,0.1);
        }
        .sys-err {
            display: none; color: #FF003C; font-family: var(--font-tech); font-size: 0.75rem; font-weight: bold; letter-spacing: 1px; margin-top: 5px;
        }
        .status-alert {
            padding: 15px; margin-bottom: 25px; border-left: 4px solid; font-family: var(--font-tech);
        }
        .status-success { background: rgba(212,255,0,0.1); border-color: <%=colorRol%>; color: <%=colorRol%>; }
        .status-error { background: rgba(255,0,60,0.1); border-color: #FF003C; color: #FF003C; }
    </style>
</head>
<body>

<div class="ui-scanner"></div>
<div class="crt-noise"></div>

<div class="danger-marquee top-marquee" style="position: fixed; top: 0; left: 0; width: 100vw; z-index: 10000; margin: 0; border: none; box-sizing: border-box;">
    <div class="marquee-content" style="color: #000; background: <%=colorRol%>;">
        ENLACE LOGÍSTICO // INGRESO DE ARTE AL NÚCLEO // CUIDADO: BASE DE DATOS EN VIVO // ENLACE LOGÍSTICO // INGRESO DE ARTE AL NÚCLEO // ENLACE LOGÍSTICO // INGRESO DE ARTE AL NÚCLEO //
    </div>
</div>

<div class="inventario-layout">

    <div style="background: #050505; border-bottom: 1px solid #222; padding: 25px 8%; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 20px;">
        <div style="display: flex; align-items: center; gap: 25px;">
            <a href="perfil.jsp" data-target="true">
                <img src="assets/icons/Vectorheart.png" alt="Logo" style="height: 55px; mix-blend-mode: screen;">
            </a>
            <div>
                <span style="color: <%=colorRol%>; font-family: var(--font-tech); font-weight: bold; letter-spacing: 2px; font-size: 0.85rem;">[ GESTOR LOGÍSTICO ACTIVO ]</span>
                <h2 style="font-family: var(--font-heading); color: #FFF; font-size: 3.2rem; margin: 0; line-height: 1;">CARGA DE NUEVO ARTE</h2>
                <p style="color: #888; font-family: var(--font-tech); font-size: 0.9rem; margin: 5px 0 0 0;">
                    Operador <%= session.getAttribute("usuario_nombre") %>, estás accediendo directamente al núcleo de PostgreSQL.
                </p>
            </div>
        </div>
        <div>
            <a href="perfil.jsp" data-target="true" style="color: #000; background: <%=colorRol%>; font-family: var(--font-tech); font-weight: bold; text-decoration: none; padding: 12px 25px; text-transform: uppercase; border-radius: 2px; transition: 0.3s; box-shadow: 0 0 10px rgba(212,255,0,0.2);">
                ← VOLVER AL MANDO
            </a>
        </div>
    </div>

    <div class="inventario-main">
        <% if ("success".equals(status)) { %>
        <div class="status-alert status-success">[SYS.OK] ARTE INYECTADO EN LA BASE DE DATOS EXITOSAMENTE.</div>
        <% } else if ("updated".equals(status)) { %>
        <div class="status-alert status-success">[SYS.OK] ESTADO DE LA OBRA ACTUALIZADO.</div>
        <% } else if ("error".equals(status) || "critical".equals(status)) { %>
        <div class="status-alert status-error">[SYS.ERR] FALLA DE CONTENCIÓN. NO SE PUDO GUARDAR LA OBRA.</div>
        <% } %>

        <form id="formInventario" action="InventarioServlet" method="POST" enctype="multipart/form-data">

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0 15px;">
                <div class="smart-input-group" style="grid-column: 1 / -1;">
                    <label>TÍTULO DE LA OBRA</label>
                    <input type="text" name="titulo" id="invTitulo" placeholder="Ej: NIGHT IN HONGKONG">
                    <div class="sys-err" id="errTitulo">[SYS.ERR] TÍTULO REQUERIDO.</div>
                </div>

                <div class="smart-input-group">
                    <label>ARTISTA / OPERADOR</label>
                    <input type="text" name="artista" id="invArtista" placeholder="Ej: V-HEART SYSTEM">
                    <div class="sys-err" id="errArtista">[SYS.ERR] ARTISTA REQUERIDO.</div>
                </div>

                <div class="smart-input-group">
                    <label>PRECIO DE DESPLIEGUE ($) [MÁX 200]</label>
                    <input type="number" step="0.01" min="0.01" max="200.00" name="precio" id="invPrecio" placeholder="Ej: 147.00">
                    <div class="sys-err" id="errPrecio">[SYS.ERR] PRECIO INVÁLIDO (ENTRE $0.01 Y $200).</div>
                </div>

                <div class="smart-input-group">
                    <label>CATEGORÍA VECTORIAL</label>
                    <select name="categoria" id="invCategoria">
                        <option value="cyberpunk neon">Cyberpunk Neon</option>
                        <option value="dark synth">Dark Synth</option>
                        <option value="abstract glitch">Abstract Glitch</option>
                    </select>
                </div>

                <div class="smart-input-group">
                    <label>NIVEL DE AMENAZA (ESTRELLAS)</label>
                    <select name="amenaza" id="invAmenaza">
                        <option value="5">Nivel 5 (Máximo)</option>
                        <option value="4">Nivel 4</option>
                        <option value="3">Nivel 3</option>
                        <option value="2">Nivel 2</option>
                        <option value="1">Nivel 1</option>
                    </select>
                </div>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0 15px;">
                <div class="smart-input-group">
                    <label>FORMATO FÍSICO</label>
                    <input type="text" name="formato" id="invFormato" placeholder="Ej: squarish, landscape">
                    <div class="sys-err" id="errFormato">[SYS.ERR] FORMATO REQUERIDO.</div>
                </div>

                <div class="smart-input-group">
                    <label>ESPECTRO DE COLOR BASE</label>
                    <input type="text" name="colorBase" id="invColor" placeholder="Ej: magenta, cyan, oscuro">
                    <div class="sys-err" id="errColor">[SYS.ERR] COLOR REQUERIDO.</div>
                </div>
            </div>

            <div class="smart-input-group" style="margin-top: 10px; border: 1px dashed <%=colorRol%>; padding: 30px; text-align: center; background: rgba(212,255,0,0.02); transition: 0.3s;" id="boxImagen">
                <label style="color: <%=colorRol%>; font-size: 1.1rem; cursor: pointer; display: block; margin-bottom: 10px;">[ SELECCIONAR ARCHIVO DE IMAGEN .JPG / .PNG ]</label>
                <input type="file" name="imagenObra" id="invImagen" accept="image/*" style="border: none; background: transparent; padding: 0; width: auto; color: #AAA;">
                <div class="sys-err" id="errImagen">[SYS.ERR] DEBE ADJUNTAR UNA IMAGEN.</div>
            </div>

            <button type="button" id="btnProcesarInventario" data-target="true" style="width: 100%; background: <%=colorRol%>; color: #000; border: none; padding: 18px; font-family: var(--font-tech); font-weight: bold; font-size: 1.1rem; cursor: pointer; transition: 0.3s; border-radius: 4px; margin-top: 10px; text-transform: uppercase;">
                CONFIRMAR INYECCIÓN EN POSTGRESQL
            </button>
        </form>

        <div style="margin-top: 60px; border-top: 1px dashed #333; padding-top: 40px;">
            <h3 style="font-family: var(--font-heading); color: #FFF; font-size: 2.2rem; margin-bottom: 20px;">REGISTRO CENTRAL DE ARTE</h3>

            <div style="overflow-x: auto; background: #050505; border: 1px solid #222; border-radius: 4px;">
                <table style="width: 100%; border-collapse: collapse; font-family: var(--font-tech); text-align: left; min-width: 800px;">
                    <thead>
                    <tr style="background: rgba(212,255,0,0.05); border-bottom: 2px solid <%=colorRol%>;">
                        <th style="padding: 15px; color: <%=colorRol%>; font-size: 0.85rem; text-align: center; width: 8%;">ID</th>
                        <th style="padding: 15px; color: <%=colorRol%>; font-size: 0.85rem; text-align: center; width: 10%;">PREVIEW</th>
                        <th style="padding: 15px; color: <%=colorRol%>; font-size: 0.85rem; width: 38%;">TÍTULO / METADATOS</th>
                        <th style="padding: 15px; color: <%=colorRol%>; font-size: 0.85rem; width: 12%;">PRECIO</th>
                        <th style="padding: 15px; color: <%=colorRol%>; font-size: 0.85rem; text-align: center; width: 16%;">ESTADO</th>
                        <th style="padding: 15px; color: <%=colorRol%>; font-size: 0.85rem; text-align: center; width: 16%;">ACCIÓN</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (inventarioGeneral != null && !inventarioGeneral.isEmpty()) {
                        for (Producto prod : inventarioGeneral) {
                            boolean isOculto = "OCULTO".equals(prod.getEstadoObra());
                    %>
                    <tr style="border-bottom: 1px solid #1a1a1a; transition: 0.3s; background: <%= isOculto ? "rgba(255,0,60,0.03)" : "transparent" %>;">

                        <td style="padding: 20px 15px; text-align: center; vertical-align: middle; color: #555; font-size: 1.1rem; font-weight: bold; white-space: nowrap;">
                            [ <%= String.format("%03d", prod.getIdProducto()) %> ]
                        </td>

                        <td style="padding: 20px 15px; text-align: center; vertical-align: middle;">
                            <div style="width: 60px; height: 60px; margin: 0 auto; overflow: hidden; border: 1px solid <%= isOculto ? "#FF003C" : "#333" %>; opacity: <%= isOculto ? "0.4" : "1" %>; border-radius: 2px;">
                                <img src="<%= prod.getRutaImagen() %>" alt="Preview" style="width: 100%; height: 100%; object-fit: cover;">
                            </div>
                        </td>

                        <td style="padding: 20px 15px; vertical-align: middle;">
                            <strong style="color: <%= isOculto ? "#888" : "#FFF" %>; display: block; font-size: 1.15rem; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 6px;">
                                <%= prod.getTitulo() %>
                            </strong>
                            <span style="color: #666; font-size: 0.8rem; display: block; letter-spacing: 0.5px;">
                                OP: <span style="color: #AAA;"><%= prod.getArtista() %></span> // CLASE: <span style="color: #AAA;"><%= prod.getCategoria() %></span>
                            </span>
                        </td>

                        <td style="padding: 20px 15px; vertical-align: middle; color: <%= isOculto ? "#666" : "#FFF" %>; font-size: 1.1rem; font-weight: bold; white-space: nowrap;">
                            $<%= String.format("%.2f", prod.getPrecio()) %>
                        </td>

                        <td style="padding: 20px 15px; text-align: center; vertical-align: middle;">
                            <span style="padding: 6px 12px; font-size: 0.75rem; font-weight: bold; letter-spacing: 1px; border-radius: 2px; display: inline-block;
                                  <%= isOculto ? "background: rgba(255,0,60,0.1); color: #FF003C; border: 1px solid #FF003C;" : "background: rgba(212,255,0,0.1); color: "+colorRol+"; border: 1px solid "+colorRol+";" %>">
                                <%= prod.getEstadoObra() %>
                            </span>
                        </td>

                        <td style="padding: 20px 15px; text-align: center; vertical-align: middle;">
                            <form action="EstadoObraServlet" method="POST" style="margin: 0;">
                                <input type="hidden" name="id_obra" value="<%= prod.getIdProducto() %>">
                                <% if (isOculto) { %>
                                <input type="hidden" name="accion" value="ACTIVAR">
                                <button type="submit" data-target="true" style="background: rgba(212,255,0,0.05); color: <%=colorRol%>; border: 1px solid <%=colorRol%>; padding: 10px 15px; cursor: pointer; font-family: var(--font-tech); font-weight: bold; font-size: 0.75rem; width: 110px; transition: 0.3s; border-radius: 2px;">ACTIVAR</button>
                                <% } else { %>
                                <input type="hidden" name="accion" value="OCULTAR">
                                <button type="submit" data-target="true" style="background: rgba(255,0,60,0.05); color: #FF003C; border: 1px solid #FF003C; padding: 10px 15px; cursor: pointer; font-family: var(--font-tech); font-weight: bold; font-size: 0.75rem; width: 110px; transition: 0.3s; border-radius: 2px;">OCULTAR</button>
                                <% } %>
                            </form>
                        </td>
                    </tr>
                    <%  }
                    } else { %>
                    <tr><td colspan="6" style="padding: 60px 20px; text-align: center; color: #444; font-size: 1.2rem; letter-spacing: 2px;">[ BASE DE DATOS VACÍA // NO HAY ARTE REGISTRADO ]</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const btnProcesar = document.getElementById('btnProcesarInventario');
        const form = document.getElementById('formInventario');

        if(btnProcesar && form) {
            btnProcesar.addEventListener('click', () => {
                let hasError = false;

                // Limpiar errores visuales
                document.querySelectorAll('.smart-input-group input').forEach(inp => inputReset(inp));
                document.getElementById('boxImagen').style.borderColor = '<%=colorRol%>';

                // Validación estricta
                if(!validarCampo('invTitulo', 'errTitulo')) hasError = true;
                if(!validarCampo('invArtista', 'errArtista')) hasError = true;
                if(!validarCampo('invFormato', 'errFormato')) hasError = true;
                if(!validarCampo('invColor', 'errColor')) hasError = true;

                // Validación estricta de Precio (0.01 a 200)
                const precio = document.getElementById('invPrecio');
                const valPrecio = parseFloat(precio.value);
                if(precio.value.trim() === '' || isNaN(valPrecio) || valPrecio <= 0 || valPrecio > 200) {
                    marcarError(precio, 'errPrecio');
                    hasError = true;
                }

                const img = document.getElementById('invImagen');
                if(img.files.length === 0) {
                    document.getElementById('errImagen').style.display = 'block';
                    document.getElementById('boxImagen').style.borderColor = '#FF003C';
                    hasError = true;
                }

                if(!hasError) {
                    btnProcesar.textContent = 'ENVIANDO A POSTGRESQL...';
                    btnProcesar.style.background = '#FFF';
                    form.submit();
                }
            });
        }

        function validarCampo(inputId, errId) {
            const input = document.getElementById(inputId);
            if(input.value.trim() === '') {
                marcarError(input, errId);
                return false;
            }
            return true;
        }

        function marcarError(input, errId) {
            input.style.borderColor = '#FF003C';
            document.getElementById(errId).style.display = 'block';
        }

        function inputReset(input) {
            input.style.borderColor = '#333';
            const errDiv = input.nextElementSibling;
            if(errDiv && errDiv.classList.contains('sys-err')) errDiv.style.display = 'none';
        }
    });
</script>

<script src="js/app.js"></script>

</body>
</html>