<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // ESCUDO TÁCTICO: Solo usuarios logueados
    if (session.getAttribute("usuario_valido") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PAGO SEGURO // VECTORHEART</title>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Space+Mono:wght@400;700&display=swap" rel="stylesheet">
    <style>
        body { margin: 0; padding: 0; overflow-x: hidden; }

        .checkout-layout {
            display: flex;
            min-height: 100vh;
            background: var(--bg);
            padding-top: 35px; /* Espacio exacto para la marquesina */
        }
        /* Panel Izquierdo: Resumen de Compra */
        .checkout-summary {
            flex: 1;
            background: #050505;
            padding: 60px 5%;
            display: flex;
            flex-direction: column;
        }
        .checkout-payment {
            flex: 1.2;
            padding: 60px 8%;
            background: radial-gradient(circle at center, #111 0%, #0A0A0A 100%);
            display: flex;
            flex-direction: column;
            justify-content: center;
        }
        .summary-item { display: flex; align-items: center; gap: 15px; margin-bottom: 20px; }
        .summary-item img { width: 60px; height: 60px; object-fit: cover; border: 1px solid #333; border-radius: 4px; }
        .summary-total { margin-top: auto; border-top: 1px dashed #333; padding-top: 20px; display: flex; justify-content: space-between; align-items: flex-end; }

        .smart-input-group { position: relative; margin-bottom: 20px; }
        .smart-input-group input { width: 100%; background: #000; border: 1px solid #333; color: #FFF; padding: 15px 45px 15px 15px; font-family: var(--font-tech); font-size: 1rem; transition: 0.3s; border-radius: 4px; }
        .smart-input-group input:focus { border-color: var(--primary); box-shadow: 0 0 10px rgba(212,255,0,0.1); outline: none; }
        .card-brand-icon {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            width: 48px;
            height: 30px;
            background: #FFFFFF; /* Fondo blanco oficial */
            border-radius: 4px;
            display: flex;
            align-items: center;
            justify-content: center;
            pointer-events: none;
            padding: 3px 6px; /* Espacio de respiración interno */
            box-sizing: border-box;
            box-shadow: 0 2px 5px rgba(0,0,0,0.3);
        }
        .card-brand-icon img, .card-brand-icon svg {
            width: 100%;
            height: 100%;
            object-fit: contain; /* Evita cualquier estiramiento */
        }
        @media (max-width: 900px) {
            .checkout-layout { flex-direction: column; padding-top: 40px; }
            .checkout-summary { border-bottom: 1px solid #333; padding: 40px 5%; }
        }
    </style>
</head>
<body>

<div class="danger-marquee top-marquee" style="position: fixed; top: 0; left: 0; width: 100vw; z-index: 10000; margin: 0; border-left: none; border-right: none; box-sizing: border-box;">
    <div class="marquee-content">
        ENLACE FINANCIERO SEGURO // VECTORHEART SECURE-PAY // CIFRADO MILITAR ACTIVO // ENLACE FINANCIERO SEGURO // VECTORHEART SECURE-PAY // CIFRADO MILITAR ACTIVO // ENLACE FINANCIERO SEGURO // VECTORHEART SECURE-PAY // CIFRADO MILITAR ACTIVO // ENLACE FINANCIERO SEGURO // VECTORHEART SECURE-PAY //
    </div>
</div>

<div class="checkout-layout">
    <div class="checkout-summary">
        <a href="index.jsp" style="display: inline-block; margin-bottom: 40px; transition: 0.3s;">
            <img src="assets/icons/Vectorheart.png" alt="Logo" style="height: 40px; mix-blend-mode: screen;">
        </a>

        <p style="color: #888; font-family: var(--font-tech); margin: 0 0 5px; font-size: 0.85rem;">PAGANDO A VECTORHEART ARCHIVE</p>
        <h2 style="font-family: var(--font-heading); color: #FFF; font-size: 2.5rem; margin: 0 0 30px;">RESUMEN DE TRANSACCIÓN</h2>

        <div id="checkoutItemsList" style="flex: 1; overflow-y: auto; padding-right: 10px;">
        </div>

        <div class="summary-total">
            <span style="font-family: var(--font-tech); color: #AAA;">TOTAL A DEBITAR</span>
            <span style="font-family: var(--font-heading); color: var(--primary); font-size: 3.5rem; line-height: 1;">
                $<span id="checkoutTotalAmount">0.00</span>
            </span>
        </div>
    </div>

    <div class="checkout-payment">
        <h3 style="font-family: var(--font-tech); color: #FFF; margin-bottom: 25px; border-bottom: 1px solid #333; padding-bottom: 10px;">DATOS BANCARIOS</h3>

        <form id="formCheckout">
            <div style="margin-bottom: 25px;">
                <label style="font-family: var(--font-tech); color: #AAA; font-size: 0.8rem; margin-bottom: 10px; display: block;">MÉTODO DE TRANSFERENCIA</label>
                <div style="display: flex; gap: 15px;">
                    <label style="flex: 1; border: 1px solid var(--primary); background: rgba(212, 255, 0, 0.05); padding: 12px; cursor: pointer; text-align: center; border-radius: 4px;" class="pay-method-label">
                        <input type="radio" name="payMethod" value="TARJETA" checked style="display: none;">
                        <span style="font-family: var(--font-tech); font-size: 0.8rem; color: #FFF; font-weight: bold;">TARJETA CRÉDITO</span>
                    </label>
                    <label style="flex: 1; border: 1px solid #333; background: #000; padding: 12px; cursor: pointer; text-align: center; border-radius: 4px;" class="pay-method-label">
                        <input type="radio" name="payMethod" value="CRIPTO" style="display: none;">
                        <span style="font-family: var(--font-tech); font-size: 0.8rem; color: #888;">PAYPAL / CRIPTO</span>
                    </label>
                </div>
            </div>

            <div id="cardFields">
                <div style="margin-bottom: 20px;">
                    <label style="font-family: var(--font-tech); color: #888; font-size: 0.8rem; margin-bottom: 8px; display: block;">NÚMERO DE TARJETA</label>
                    <div class="smart-input-group" style="margin-bottom: 5px;">
                        <input type="text" id="chkCard" placeholder="0000 0000 0000 0000" maxlength="19" autocomplete="off">
                        <span class="card-brand-icon" id="cardIcon" style="display: flex; align-items: center;">
                            <svg viewBox="0 0 24 24" width="24" height="24" fill="#555"><path d="M20 4H4c-1.11 0-1.99.89-1.99 2L2 18c0 1.11.89 2 2 2h16c1.11 0 2-.89 2-2V6c0-1.11-.89-2-2-2zm0 14H4v-6h16v6zm0-10H4V6h16v2z"/></svg>
                        </span>
                    </div>
                    <div id="errCard" style="display:none; color:#FF003C; font-family:var(--font-tech); font-size:0.75rem; font-weight:bold; letter-spacing: 1px;">
                        <svg viewBox="0 0 24 24" width="12" height="12" fill="currentColor" style="vertical-align: middle; margin-right: 4px;"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/></svg>
                        [SYS.ERR] TARJETA INVÁLIDA O INCOMPLETA.
                    </div>
                </div>

                <div style="display: flex; gap: 15px; margin-bottom: 30px;">
                    <div style="flex: 1;">
                        <label style="font-family: var(--font-tech); color: #888; font-size: 0.8rem; margin-bottom: 8px; display: block;">VENCIMIENTO</label>
                        <div class="smart-input-group" style="margin-bottom: 5px;">
                            <input type="text" id="chkDate" placeholder="MM/AA" maxlength="5">
                        </div>
                        <div id="errDate" style="display:none; color:#FF003C; font-family:var(--font-tech); font-size:0.75rem; font-weight:bold; letter-spacing: 1px;">[SYS.ERR] FECHA INVÁLIDA.</div>
                    </div>
                    <div style="flex: 1;">
                        <label style="font-family: var(--font-tech); color: #888; font-size: 0.8rem; margin-bottom: 8px; display: block;">CÓDIGO (CVC)</label>
                        <div class="smart-input-group" style="margin-bottom: 5px;">
                            <input type="password" id="chkCvc" placeholder="•••" maxlength="4">
                        </div>
                        <div id="errCvc" style="display:none; color:#FF003C; font-family:var(--font-tech); font-size:0.75rem; font-weight:bold; letter-spacing: 1px;">[SYS.ERR] CÓDIGO REQUERIDO.</div>
                    </div>
                </div>

                <h3 style="font-family: var(--font-tech); color: #FFF; margin-bottom: 20px; border-bottom: 1px solid #333; padding-bottom: 10px;">INFORMACIÓN DE FACTURACIÓN</h3>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px 15px; margin-bottom: 15px;">
                    <div>
                        <div class="smart-input-group" style="margin-bottom: 5px;">
                            <input type="text" id="billNombre" placeholder="Nombre(s)">
                        </div>
                    </div>
                    <div>
                        <div class="smart-input-group" style="margin-bottom: 5px;">
                            <input type="text" id="billApellido" placeholder="Apellidos">
                        </div>
                    </div>
                    <div style="grid-column: 1 / -1;">
                        <div class="smart-input-group" style="margin-bottom: 5px;">
                            <input type="text" id="billDireccion" placeholder="Dirección de facturación (Calle, Núm)">
                        </div>
                    </div>
                    <div>
                        <div class="smart-input-group" style="margin-bottom: 5px;">
                            <input type="text" id="billCiudad" placeholder="Localidad / Ciudad">
                        </div>
                    </div>
                    <div>
                        <div class="smart-input-group" style="margin-bottom: 5px;">
                            <input type="text" id="billPostal" placeholder="Código Postal">
                        </div>
                    </div>
                    <div>
                        <div class="smart-input-group" style="margin-bottom: 5px;">
                            <input type="text" id="billPais" placeholder="País" value="Ecuador" readonly style="color: #555; cursor: not-allowed; border-color: #222;">
                        </div>
                    </div>
                    <div>
                        <div class="smart-input-group" style="margin-bottom: 5px;">
                            <input type="text" id="billTelefono" placeholder="Teléfono" maxlength="15">
                        </div>
                    </div>
                </div>

                <div id="errBill" style="display:none; color:#FF003C; font-family:var(--font-tech); font-size:0.75rem; font-weight:bold; letter-spacing: 1px; margin-bottom: 25px;">
                    <svg viewBox="0 0 24 24" width="12" height="12" fill="currentColor" style="vertical-align: middle; margin-right: 4px;"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/></svg>
                    [SYS.ERR] COMPLETE TODOS LOS CAMPOS DE FACTURACIÓN.
                </div>
            </div>

            <button type="button" id="btnConfirmPay" style="width: 100%; background: var(--primary); color: var(--dark); border: none; padding: 18px; font-family: var(--font-tech); font-weight: bold; font-size: 1.1rem; cursor: pointer; transition: 0.3s; border-radius: 4px; text-transform: uppercase;">
                PROCESAR PAGO SEGURO
            </button>
            <a href="index.jsp" style="display: block; text-align: center; color: #666; font-family: var(--font-tech); text-decoration: none; margin-top: 20px; font-size: 0.85rem; transition: 0.3s;">
                ← Cancelar y volver al catálogo
            </a>
            </a>
        </form>
    </div>
</div>

<script src="js/app.js"></script>
</body>
</html>