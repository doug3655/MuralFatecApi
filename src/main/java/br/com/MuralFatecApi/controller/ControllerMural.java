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

import br.com.MuralFatecApi.DTO.Grupo;
import br.com.MuralFatecApi.DTO.Notificacao;
import br.com.MuralFatecApi.DTO.Status;
import br.com.MuralFatecApi.DTO.Usuario;
import br.com.MuralFatecApi.service.ServiceMural;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ControllerMural {
	
	@Autowired
	private ServiceMural serviceMural;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@PostMapping("/pdf-vinculo-tg-i-ii/{idGrupo}/{isPrimeiroVinculo}")
	public void pdfVinculoTgiII(HttpServletResponse response,@PathVariable Integer idGrupo,@PathVariable Boolean isPrimeiroVinculo){
	       try {
	            Path file = Paths.get(serviceMural.criaPdfVinculoTg(idGrupo,isPrimeiroVinculo).getAbsolutePath());
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
		return new ResponseEntity<Usuario>(usuario,usuario.getId_usuario() != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@ResponseBody
	@PostMapping("/alterar-usuario/{tipo}")
	public ResponseEntity<String> alterarUsuario(@RequestBody Usuario usuario,@PathVariable Integer tipo){
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
	@PostMapping("/alterar-grupo")
	public ResponseEntity<String> alteraGrupo(@RequestBody Grupo grupo){
		boolean retorno = true;
		try {
			retorno = serviceMural.alterarTodosDadosGrupo(grupo);
		} catch (Exception e) {
			retorno = false;
			e.printStackTrace();
		}
		return retorno ? ResponseEntity.ok("Dados alterado com sucesso!") : ResponseEntity.internalServerError().body("Erro ao alterar dados");
	}
	
	@ResponseBody
	@PostMapping("/aluno-sair-grupo/{idAluno}/{idGrupo}")
	public ResponseEntity<String> alunoSairGrupo(@PathVariable Integer idAluno,@PathVariable Integer idGrupo){
		Integer retorno = 0;
		try {
			retorno = serviceMural.alunoSairGrupo(idAluno,idGrupo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno>-1 ? ResponseEntity.ok("Aluno saiu do grupo com sucesso") : ResponseEntity.internalServerError().body("Erro ao sair do grupo");
	}
	
	@ResponseBody
	@PostMapping("/remover-aluno-grupo/{idAluno}/{idGrupo}")
	public ResponseEntity<Integer> removerAlunoGrupo(@PathVariable Integer idAluno,@PathVariable Integer idGrupo){
		Integer retorno = 0;
		try {
			retorno = serviceMural.alunoSairGrupo(idAluno,idGrupo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno>-1 ? ResponseEntity.ok(retorno) : ResponseEntity.internalServerError().body(retorno);
	}
	
	@ResponseBody
	@PostMapping("/resolver-notificacao/{tpPerfil}")
	public ResponseEntity<String> resolverNotificacao(@RequestBody Notificacao notificacao,@PathVariable Integer tpPerfil){
		boolean retorno = true;
		try {
			retorno = serviceMural.resolverNotificacao(notificacao,tpPerfil);
		} catch (Exception e) {
			retorno = false;
			e.printStackTrace();
		}
		return retorno ? ResponseEntity.ok("Notificacao resolvida com sucesso!") : ResponseEntity.internalServerError().body("Erro ao resolver notificacao");
	}
	
	
	@ResponseBody
	@PostMapping("/teste")
	public ResponseEntity<String> teste(@RequestBody String json){
		Map<String, String> mapJson = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
		serviceMural.teste(mapJson.get("nm_email"), mapJson.get("nm_senha"));
		return ResponseEntity.ok("Teste");
	}
}