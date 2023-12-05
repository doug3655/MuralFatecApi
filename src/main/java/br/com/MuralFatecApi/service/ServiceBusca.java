package br.com.MuralFatecApi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.MuralFatecApi.DTO.Aluno;
import br.com.MuralFatecApi.DTO.Curso;
import br.com.MuralFatecApi.DTO.Grupo;
import br.com.MuralFatecApi.DTO.Notificacao;
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
	
	public Grupo buscaDadosGrupo(Integer idUsuario){
		List<Grupo> grupos = new ArrayList<Grupo>();
		Grupo grupo = new Grupo();
		try {
			String sqlValidaPertenceGrupo = "SELECT tg.* FROM muraldb.dbo.TB_GRUPO_COMPONENTE tgc LEFT JOIN muraldb.dbo.TB_GRUPO tg ON tgc.ID_GRUPO = tg.ID_GRUPO WHERE tgc.ID_TP_STATUS = 5 AND tgc.ID_USUARIO = ?";
			grupos = jdbcTemplate.query(sqlValidaPertenceGrupo, new BeanPropertyRowMapper<Grupo>(Grupo.class),idUsuario);
			if(!grupos.isEmpty()) {
				grupo = grupos.get(0);
				String sqlDadosGrupo = "SELECT tu.ID_USUARIO,tu.NM_USUARIO FROM muraldb.dbo.TB_GRUPO_COMPONENTE tgc LEFT JOIN muraldb.dbo.TB_USUARIO tu ON tgc.ID_USUARIO = tu.ID_USUARIO WHERE tgc.ID_TP_STATUS = 5 AND tgc.ID_GRUPO = ?";
				List<Aluno> alunos =  jdbcTemplate.query(sqlDadosGrupo,new BeanPropertyRowMapper<Aluno>(Aluno.class),grupo.getId_grupo());
				grupo.setAlunos(alunos);
			}
		}catch(Exception e) {
			System.out.println("Erro na execução da query do buscaDadosGrupo:"+e.getMessage());
		}
		return grupo;
	}
	
	public List<Notificacao> buscaNotificacoes(Integer tpNotificacao){
		List<Notificacao> notificacoes = new ArrayList<Notificacao>();
		try {
			if(tpNotificacao==1) {
				String sqlNotificacaoUsuario = "SELECT tn.*, tu.NM_USUARIO AS NM_NOTIFICACAO FROM muraldb.dbo.TB_NOTIFICACAO tn LEFT JOIN muraldb.dbo.TB_USUARIO tu ON tn.NR_ENTIDADE_ALVO = tu.ID_USUARIO WHERE tn.ID_TP_NOTIFICACAO = ? AND tn.ID_TP_STATUS = 3";
				notificacoes = jdbcTemplate.query(sqlNotificacaoUsuario, new BeanPropertyRowMapper<Notificacao>(Notificacao.class),tpNotificacao);
			}else {
				String sqlNotificaoGrupo = "SELECT tn.*, tg.NM_TEMA AS NM_NOTIFICACAO FROM muraldb.dbo.TB_NOTIFICACAO tn LEFT JOIN muraldb.dbo.TB_GRUPO tg ON tn.NR_ENTIDADE_ALVO = tg.ID_GRUPO WHERE tn.ID_TP_NOTIFICACAO = ? AND tn.ID_TP_STATUS = 3";
				notificacoes = jdbcTemplate.query(sqlNotificaoGrupo, new BeanPropertyRowMapper<Notificacao>(Notificacao.class),tpNotificacao);
			}			
		}catch(Exception e) {
			System.out.println("Erro na execução da query do buscaNotificacoes:"+e.getMessage());
		}
		return notificacoes;
	}
}