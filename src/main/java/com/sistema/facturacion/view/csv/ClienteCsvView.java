package com.sistema.facturacion.view.csv;

import com.sistema.facturacion.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component("listar-todo")
public class ClienteCsvView extends AbstractView {

    public ClienteCsvView() {
        setContentType("text/csv");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"clientes.csv\"");
        httpServletResponse.setContentType(getContentType());
        List<Cliente> clientes = (List<Cliente>) map.get("clientes");

        ICsvBeanWriter beanWriter = new CsvBeanWriter(httpServletResponse.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] header = {"id", "nombre", "apellido", "email", "createAt"};
        beanWriter.writeHeader(header);

        for (Cliente cliente : clientes) {
            beanWriter.write(cliente, header);
        }
        beanWriter.close();
    }
}
