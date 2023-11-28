package br.com.MuralFatecApi.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.DocumentException;

import br.com.MuralFatecApi.DTO.Status;
import br.com.MuralFatecApi.service.ServiceMural;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ControllerMural {
	
	@Autowired
	ServiceMural serviceMural;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@PostMapping("/pdf-vinculo-tg-i-ii")
	public void downloadPDFResource(HttpServletResponse response){
	       try {
	            Path file = Paths.get(serviceMural.criaPdfVinculoTg().getAbsolutePath());
	            if (Files.exists(file)) {
	                response.setContentType("application/pdf");
	                response.addHeader("Content-Disposition",
	                        "attachment; filename=" + file.getFileName());
	                Files.copy(file, response.getOutputStream());
	                response.getOutputStream().flush();
	            }
	        } catch (DocumentException | IOException ex) {
	            ex.printStackTrace();
	        }
	}
	
	@ResponseBody
	@PostMapping("/teste-banco")
	public void testeBanco(){
		String sql = "SELECT * FROM muraldb.dbo.TB_TP_STATUS";
		List<Status> status = jdbcTemplate.query(sql,BeanPropertyRowMapper.newInstance(Status.class));
		status.forEach(System.out :: println);
	}
}