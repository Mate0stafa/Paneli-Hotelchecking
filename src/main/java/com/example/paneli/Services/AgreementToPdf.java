package com.example.paneli.Services;


import com.example.paneli.Models.Contract.Agreement;
import com.example.paneli.Models.Property;
import com.example.paneli.Repositories.AgreementRepository;
import com.example.paneli.Repositories.PropertyRepository;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class AgreementToPdf {

    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    AgreementRepository agreementRepository;

    public void pdfConverter(List<String> HTML, Property property, Agreement agreement) throws IOException {
        HtmlConverter.convertToPdf(HTML.get(0)+HTML.get(1)+HTML.get(2)+HTML.get(3)+HTML.get(4)+HTML.get(5)+HTML.get(6) ,new FileOutputStream("/home/allbookersusr/home/BookersDesk/data/pdf/"+property.getName().replaceAll(" ", "")+""+property.getId()+".pdf"));
    }

}
