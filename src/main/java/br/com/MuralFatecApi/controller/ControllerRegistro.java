package br.com.MuralFatecApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.MuralFatecApi.DTO.Usuario;
import br.com.MuralFatecApi.service.ServiceCadastro;

@Controller
public class ControllerRegistro {
	
	@Autowired
	private ServiceCadastro serviceCadastro;

	@ResponseBody
	@PostMapping("/registrar-usuario")
	public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario){
		boolean retorno = serviceCadastro.registraUsuario(usuario);
		return retorno ? ResponseEntity.ok("Usuario registrado!") : ResponseEntity.internalServerError().body("Erro ao registrar usuario");
	}
}