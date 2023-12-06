package br.com.MuralFatecApi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.MuralFatecApi.DTO.Curso;
import br.com.MuralFatecApi.DTO.Grupo;
import br.com.MuralFatecApi.DTO.Notificacao;
import br.com.MuralFatecApi.DTO.Orientador;
import br.com.MuralFatecApi.DTO.Periodo;
import br.com.MuralFatecApi.DTO.Usuario;
import br.com.MuralFatecApi.service.ServiceBusca;

@Controller
public class ControllerBusca {
	
	@Autowired
	ServiceBusca serviceBusca;
	
	@ResponseBody
	@GetMapping("/busca-cursos")
	public ResponseEntity<List<Curso>> listarCursos(){
		List<Curso> cursos = new ArrayList<Curso>();
		try {
			cursos = serviceBusca.listarCursos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Curso>>(cursos,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping("/busca-periodos")
	public ResponseEntity<List<Periodo>> listarPeriodos(){
		List<Periodo> periodo = new ArrayList<Periodo>();
		try {
			periodo = serviceBusca.listarPeriodos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Periodo>>(periodo,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping("/busca-orientadores")
	public ResponseEntity<List<Orientador>> listarOrientadores(){
		List<Orientador> orientador = new ArrayList<Orientador>();
		try {
			orientador = serviceBusca.listarOrientador();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Orientador>>(orientador,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping("/busca-dado-grupo-aluno/{idUsuario}")
	public ResponseEntity<Grupo> buscaDadosGrupoAluno(@PathVariable Integer idUsuario){
		Grupo grupo = new Grupo();
		try {
			grupo = serviceBusca.buscaDadosGrupoAluno(idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grupo.getId_grupo() != null ? new ResponseEntity<Grupo>(grupo,HttpStatus.OK) : new ResponseEntity<Grupo>(grupo,HttpStatus.NOT_FOUND);
	}
	
	@ResponseBody
	@GetMapping("/busca-dado-grupo-por-nome/{tpPerfil}/{idOrientador}/{nomeGrupo}")
	public ResponseEntity<List<Grupo>> buscaDadosGrupoPorNome(@PathVariable Integer tpPerfil,@PathVariable Integer idOrientador,@PathVariable String nomeGrupo){
		List<Grupo> grupos = new ArrayList<Grupo>();
		try {
			grupos = serviceBusca.buscaDadosGrupoPorNome(tpPerfil,idOrientador,nomeGrupo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !grupos.isEmpty() ? new ResponseEntity<List<Grupo>>(grupos,HttpStatus.OK) : new ResponseEntity<List<Grupo>>(grupos,HttpStatus.NOT_FOUND);
	}
	
	@ResponseBody
	@GetMapping("/busca-usuario-nome/{nomeUsuario}")
	public ResponseEntity<List<Usuario>> buscaUsuarioPorNome(@PathVariable String nomeUsuario){
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			usuarios = serviceBusca.buscaUsuarioPorNome(nomeUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !usuarios.isEmpty() ? new ResponseEntity<List<Usuario>>(usuarios,HttpStatus.OK) : new ResponseEntity<List<Usuario>>(usuarios,HttpStatus.NOT_FOUND);
	}
	
	@ResponseBody
	@GetMapping("/busca-aluno-nome/{nomeUsuario}")
	public ResponseEntity<List<Usuario>> buscaAlunoPorNome(@PathVariable String nomeUsuario){
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			usuarios = serviceBusca.buscaAlunoPorNome(nomeUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !usuarios.isEmpty() ? new ResponseEntity<List<Usuario>>(usuarios,HttpStatus.OK) : new ResponseEntity<List<Usuario>>(usuarios,HttpStatus.NOT_FOUND);
	}
	
	@ResponseBody
	@GetMapping("/busca-dado-grupo-id/{idGrupo}")
	public ResponseEntity<Grupo> buscaDadosGrupoPorId(@PathVariable Integer idGrupo){
		Grupo grupo = new Grupo();
		try {
			grupo = serviceBusca.buscaDadosGrupoPorId(idGrupo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grupo.getId_grupo() != null ? new ResponseEntity<Grupo>(grupo,HttpStatus.OK) : new ResponseEntity<Grupo>(grupo,HttpStatus.NOT_FOUND);
	}
	
	@ResponseBody
	@GetMapping("/busca-dado-usuario-id/{idUsuario}")
	public ResponseEntity<Usuario> buscaDadosUsuarioPorId(@PathVariable Integer idUsuario){
		Usuario usuario = new Usuario();
		try {
			usuario = serviceBusca.buscaDadosUsuarioPorId(idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario.getId_usuario() != null ? new ResponseEntity<Usuario>(usuario,HttpStatus.OK) : new ResponseEntity<Usuario>(usuario,HttpStatus.NOT_FOUND);
	}
	
	@ResponseBody
	@GetMapping("/busca-notificacoes/{tpNotificacao}")
	public ResponseEntity<List<Notificacao>> buscaNotificacoes(@PathVariable Integer tpNotificacao){
		List<Notificacao> notificacoes = new ArrayList<Notificacao>();
		try {
			notificacoes = serviceBusca.buscaNotificacoes(tpNotificacao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Notificacao>>(notificacoes,HttpStatus.OK);
	}
}