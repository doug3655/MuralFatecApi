package br.com.MuralFatecApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import br.com.MuralFatecApi.DTO.Grupo;
import br.com.MuralFatecApi.DTO.Usuario;
import br.com.MuralFatecApi.service.ServiceCadastro;

@Controller
public class ControllerCadastro {
	
	@Autowired
	private ServiceCadastro serviceCadastro;

	@ResponseBody
	@PostMapping("/registrar-usuario")
	public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario){
		boolean retorno = true;;
		try {
			retorno = serviceCadastro.registraUsuario(usuario);
		} catch (Exception e) {
			retorno = false;
			e.printStackTrace();
		}
		return retorno ? ResponseEntity.ok("Usuario registrado!") : ResponseEntity.internalServerError().body("Erro ao registrar usuario");
	}
	
	@ResponseBody
	@PostMapping("/registrar-grupo")
	public ResponseEntity<String> registrarUsuario(@RequestBody String grupo){
		Grupo dadosGrupo = new Gson().fromJson(grupo, Grupo.class);
		boolean retorno = true;;
		try {
			retorno = serviceCadastro.registraGrupo(dadosGrupo);
		} catch (Exception e) {
			retorno = false;
			e.printStackTrace();
		}
		return retorno ? ResponseEntity.ok("Grupo registrado!") : ResponseEntity.internalServerError().body("Erro ao registrar grupo");
	}
}