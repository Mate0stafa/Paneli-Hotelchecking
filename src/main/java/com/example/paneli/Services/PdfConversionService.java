package com.example.paneli.Services;
import com.itextpdf.io.source.ByteArrayOutputStream;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class PdfConversionService {
    public byte[] convertHtmlToPdf(String htmlContent) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        return outputStream.toByteArray();
    }
}
