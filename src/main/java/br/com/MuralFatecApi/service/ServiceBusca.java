package br.com.MuralFatecApi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.MuralFatecApi.DTO.Curso;
import br.com.MuralFatecApi.DTO.Orientador;
import br.com.MuralFatecApi.DTO.Periodo;

@Service
public class ServiceBusca {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Curso> listarCursos(){
		List<Curso> cursos = new ArrayList<Curso>();
		try {
			String sql = "SELECT * FROM muraldb.dbo.TB_TP_CURSO";
			cursos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Curso>(Curso.class));
		}catch(Exception e) {
			System.out.println("Erro na execução da query do listarCursos:"+e.getMessage());
		}
		return cursos;
	}
	
	public List<Periodo> listarPeriodos(){
		List<Periodo> periodo = new ArrayList<Periodo>();
		try {
			String sql = "SELECT * FROM muraldb.dbo.TB_TP_PERIODO";
			periodo = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Periodo>(Periodo.class));
		}catch(Exception e) {
			System.out.println("Erro na execução da query do listarPeriodos:"+e.getMessage());
		}
		return periodo;
	}
	
	public List<Orientador> listarOrientador(){
		List<Orientador> orientador = new ArrayList<Orientador>();
		try {
			String sql = "SELECT tu.ID_USUARIO,tu.NM_USUARIO FROM muraldb.dbo.TB_USUARIO tu WHERE tu.ID_TP_PERFIL_USUARIO = 2";
			orientador = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Orientador>(Orientador.class));
		}catch(Exception e) {
			System.out.println("Erro na execução da query do listarOrientador:"+e.getMessage());
		}
		return orientador;
	}
}