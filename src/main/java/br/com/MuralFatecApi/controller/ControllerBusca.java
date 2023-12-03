package br.com.MuralFatecApi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.MuralFatecApi.DTO.Curso;
import br.com.MuralFatecApi.DTO.Orientador;
import br.com.MuralFatecApi.DTO.Periodo;
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
}