package com.ejemplo.legacy.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/** Módulo que todavía vive en el legacy (ej.: facturación). */
public class ModuloLegacyAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        List<String[]> facturas = Arrays.asList(
                new String[]{"F-0001", "Comercial Andes", "$ 120.000"},
                new String[]{"F-0002", "Distribuidora Sur", "$ 87.500"},
                new String[]{"F-0003", "Tecnología Norte", "$ 310.990"});
        request.setAttribute("facturas", facturas);
        return mapping.findForward("ok");
    }
}
