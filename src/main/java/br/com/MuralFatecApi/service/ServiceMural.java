package br.com.MuralFatecApi.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import br.com.MuralFatecApi.DTO.AlunoVinculo;

@Service
public class ServiceMural {

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	public File criaPdfVinculoTg() throws IOException, DocumentException {
		Context context = new Context();
        String html = "";
        List<AlunoVinculo> alunosVinculo = new ArrayList<AlunoVinculo>();
        AlunoVinculo alunoVinculo1 = new AlunoVinculo("teste1");
        AlunoVinculo alunoVinculo2 = new AlunoVinculo("teste2");
        AlunoVinculo alunoVinculo3 = new AlunoVinculo("teste3");
        alunosVinculo.add(alunoVinculo1);
        alunosVinculo.add(alunoVinculo2);
        alunosVinculo.add(alunoVinculo3);
        context.setVariable("alunos", alunosVinculo);
        html = loadAndFillTemplate("VinculoTG",context);
        return renderPdfVinculoTg(html);
	}
	
	private String loadAndFillTemplate(String templapeNome,Context context) {
		return templateEngine.process(templapeNome, context);
	}
	
	private File renderPdfVinculoTg(String html) throws IOException, DocumentException {
		File file = File.createTempFile("students", ".pdf");
		OutputStream outputStream = new FileOutputStream(file);
		ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
		renderer.setDocumentFromString(html, new ClassPathResource("/templates/").getURL().toExternalForm());
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
	}
	
}