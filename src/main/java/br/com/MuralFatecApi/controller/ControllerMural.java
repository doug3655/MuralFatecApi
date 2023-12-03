package br.com.MuralFatecApi.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lowagie.text.DocumentException;

import br.com.MuralFatecApi.DTO.Status;
import br.com.MuralFatecApi.DTO.Usuario;
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
	
	@ResponseBody
	@PostMapping("/realiza-login")
	public ResponseEntity<Usuario> realizaLogin(@RequestBody String json){
		Map<String, String> mapJson = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
		Usuario usuario = new Usuario();
		try {
			usuario = serviceMural.realizaLogin(mapJson.get("nm_email"), mapJson.get("nm_senha"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Usuario>(usuario,HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping("/alterar-usuario/{tipo}")
	public ResponseEntity<String> realizarLogin(@RequestBody Usuario usuario,@PathVariable Integer tipo){
		boolean retorno = true;
		try {
			retorno = serviceMural.alterarDadosUsuario(usuario, tipo);
		} catch (Exception e) {
			retorno = false;
			e.printStackTrace();
		}
		return retorno ? ResponseEntity.ok("Dados alterado com sucesso!") : ResponseEntity.internalServerError().body("Erro ao alterar dados");
	}
	
	
	@ResponseBody
	@PostMapping("/teste")
	public ResponseEntity<String> teste(@RequestBody String json){
		Map<String, String> mapJson = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
		serviceMural.teste(mapJson.get("nm_email"), mapJson.get("nm_senha"));
		return ResponseEntity.ok("Teste");
	}
}