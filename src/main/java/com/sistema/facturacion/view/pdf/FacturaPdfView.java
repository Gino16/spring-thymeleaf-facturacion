package com.sistema.facturacion.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sistema.facturacion.models.entity.Factura;
import com.sistema.facturacion.models.entity.ItemFactura;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {
    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Factura factura = (Factura) map.get("factura");

        PdfPTable table1 = new PdfPTable(1);
        table1.setSpacingAfter(20);
        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase("Datos del cliente"));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        table1.addCell(cell);

        table1.addCell(factura.getCliente().toString());
        table1.addCell(factura.getCliente().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table2.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase("Datos de la Factura"));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);
        table2.addCell(cell);

        table2.addCell("Folio: " + factura.getId());
        table2.addCell("Descripcion: " + factura.getDescripcion());
        table2.addCell("Fecha: " + factura.getCreateAt());

        document.add(table1);
        document.add(table2);

        PdfPTable table3 = new PdfPTable(4);
        table3.setWidths(new float[]{3.5f, 1, 1, 1});
        table3.addCell("Producto");
        table3.addCell("Precio");
        table3.addCell("Cantidad");
        table3.addCell("Total");

        for (ItemFactura item : factura.getItems()) {
            table3.addCell(item.getProducto().getNombre());
            table3.addCell(item.getProducto().getPrecio().toString());

            cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table3.addCell(cell);
            table3.addCell(item.calcularImporte().toString());
        }

        cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(cell);
        table3.addCell(factura.getTotal().toString());

        document.add(table3);
    }
}
