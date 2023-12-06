package br.com.MuralFatecApi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.MuralFatecApi.DTO.Aluno;
import br.com.MuralFatecApi.DTO.Curso;
import br.com.MuralFatecApi.DTO.DadosPDFVinculo;
import br.com.MuralFatecApi.DTO.Grupo;
import br.com.MuralFatecApi.DTO.Notificacao;
import br.com.MuralFatecApi.DTO.Orientador;
import br.com.MuralFatecApi.DTO.Periodo;
import br.com.MuralFatecApi.DTO.Usuario;

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
	
	public Grupo buscaDadosGrupoAluno(Integer idUsuario){
		List<Grupo> grupos = new ArrayList<Grupo>();
		Grupo grupo = new Grupo();
		try {
			String sqlValidaPertenceGrupo = "SELECT tg.* FROM muraldb.dbo.TB_GRUPO_COMPONENTE tgc LEFT JOIN muraldb.dbo.TB_GRUPO tg ON tgc.ID_GRUPO = tg.ID_GRUPO WHERE tgc.ID_TP_STATUS = 5 AND tgc.ID_USUARIO = ? AND tg.ID_TP_STATUS = 1";
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
	
	public List<Grupo> buscaDadosGrupoPorNome(Integer tpPerfil, Integer idOrientador, String nomeGrupo){
		List<Grupo> grupos = new ArrayList<Grupo>();
		try {
			StringBuilder queryBaseGrupoPorNome = new StringBuilder();
			queryBaseGrupoPorNome.append("SELECT tg.ID_GRUPO,tg.NM_TEMA FROM muraldb.dbo.TB_GRUPO tg WHERE tg.ID_TP_STATUS = 1 AND tg.NM_TEMA LIKE ?");
			if(tpPerfil == 2) {
				queryBaseGrupoPorNome.append(" AND tg.ID_ORIENTADOR = ?");
				grupos = jdbcTemplate.query(queryBaseGrupoPorNome.toString(), new BeanPropertyRowMapper<Grupo>(Grupo.class),"%"+nomeGrupo+"%",idOrientador);
			}else {
				grupos = jdbcTemplate.query(queryBaseGrupoPorNome.toString(), new BeanPropertyRowMapper<Grupo>(Grupo.class),"%"+nomeGrupo+"%");
			}
		}catch(Exception e) {
			System.out.println("Erro na execução da query do buscaDadosGrupo:"+e.getMessage());
		}
		return grupos;
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
	
	public DadosPDFVinculo buscaDadosGrupoPDF(Integer idGrupo) {
		List<Grupo> grupos = new ArrayList<Grupo>();
		Grupo grupo = new Grupo();
		DadosPDFVinculo dados = new DadosPDFVinculo();
		try {
			String sqlDadosGrupo = "SELECT NM_TEMA, ID_ORIENTADOR, ID_TP_CURSO, ID_TP_PERIODO, FL_TG1, FL_TG2 FROM muraldb.dbo.TB_GRUPO WHERE ID_GRUPO=?";
			grupos = jdbcTemplate.query(sqlDadosGrupo, new BeanPropertyRowMapper<Grupo>(Grupo.class),idGrupo);
			grupo = grupos.get(0);
			String sqlMembrosGrupo = "SELECT tu.NM_USUARIO,tu.NR_RA FROM muraldb.dbo.TB_GRUPO_COMPONENTE tgc LEFT JOIN muraldb.dbo.TB_USUARIO tu ON tgc.ID_USUARIO = tu.ID_USUARIO WHERE tgc.ID_TP_STATUS = 5 AND tgc.ID_GRUPO = ?";
			List<Aluno> alunos =  jdbcTemplate.query(sqlMembrosGrupo,new BeanPropertyRowMapper<Aluno>(Aluno.class),idGrupo);
			grupo.setAlunos(alunos);
			String sqlBuscaPeriodo = "SELECT ttp.NM_PERIODO FROM muraldb.dbo.TB_TP_PERIODO ttp WHERE ttp.ID_TP_PERIODO = ?";
			String periodo = jdbcTemplate.queryForObject(sqlBuscaPeriodo,String.class,grupo.getId_tp_periodo());
			String sqlBuscaCurso = "SELECT ttc.NM_CURSO FROM muraldb.dbo.TB_TP_CURSO ttc WHERE ttc.ID_TP_CURSO = ?";
			String curso = jdbcTemplate.queryForObject(sqlBuscaCurso,String.class,grupo.getId_tp_curso());
			String sqlBuscaOrientador = "SELECT NM_USUARIO FROM muraldb.dbo.TB_USUARIO WHERE ID_USUARIO=?";
			String orientador = jdbcTemplate.queryForObject(sqlBuscaOrientador,String.class,grupo.getId_orientador());
			String verificaExisteNotificacao = "SELECT ISNULL((SELECT ID_NOTIFICACAO FROM muraldb.dbo.TB_NOTIFICACAO WHERE ID_TP_NOTIFICACAO = 3 AND ID_TP_STATUS = 3 AND NR_ENTIDADE_ALVO = ?),0) AS ID_NOTIFICACAO";
			Integer result = jdbcTemplate.queryForObject(verificaExisteNotificacao,Integer.class,idGrupo);
			if(result!=null && result>0) {
				String sqlRegistraNotigicacao = "INSERT INTO muraldb.dbo.TB_NOTIFICACAO (ID_TP_NOTIFICACAO,ID_TP_STATUS,NR_ENTIDADE_ALVO) VALUES (3,3,?)";
				jdbcTemplate.update(sqlRegistraNotigicacao,idGrupo);
			}
			dados.setGrupo(grupo);
			dados.setCurso(curso);
			dados.setPeriodo(periodo);
			dados.setOrientador(orientador);
		}catch(Exception e) {
			System.out.println("Erro na execução da query do buscaDadosGrupo:"+e.getMessage());
		}
		return dados;
	}

	public Grupo buscaDadosGrupoPorId(Integer idGrupo) {
		List<Grupo> grupos = new ArrayList<Grupo>();
		Grupo grupo = new Grupo();
		try {
			String sqlValidaPertenceGrupo = "SELECT * FROM muraldb.dbo.TB_GRUPO tg WHERE tg.ID_GRUPO = ?";
			grupos = jdbcTemplate.query(sqlValidaPertenceGrupo, new BeanPropertyRowMapper<Grupo>(Grupo.class),idGrupo);
			if(!grupos.isEmpty()) {
				grupo = grupos.get(0);
				String sqlDadosGrupo = "SELECT tu.ID_USUARIO,tu.NM_USUARIO FROM muraldb.dbo.TB_GRUPO_COMPONENTE tgc LEFT JOIN muraldb.dbo.TB_USUARIO tu ON tgc.ID_USUARIO = tu.ID_USUARIO WHERE tgc.ID_TP_STATUS = 5 AND tgc.ID_GRUPO = ?";
				List<Aluno> alunos =  jdbcTemplate.query(sqlDadosGrupo,new BeanPropertyRowMapper<Aluno>(Aluno.class),grupo.getId_grupo());
				grupo.setAlunos(alunos);
			}
		}catch(Exception e) {
			System.out.println("Erro na execução da query do buscaDadosGrupoPorId:"+e.getMessage());
		}
		return grupo;
	}

	public List<Usuario> buscaUsuarioPorNome(String nomeUsuario) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			String query = "SELECT ID_USUARIO,NM_USUARIO FROM muraldb.dbo.TB_USUARIO  WHERE ID_TP_STATUS = 1 AND NM_USUARIO LIKE ?";
			usuarios = jdbcTemplate.query(query, new BeanPropertyRowMapper<Usuario>(Usuario.class),"%"+nomeUsuario+"%");
		}catch(Exception e) {
			System.out.println("Erro na execução da query do buscaUsuarioPorNome:"+e.getMessage());
		}
		return usuarios;
	}

	public Usuario buscaDadosUsuarioPorId(Integer idUsuario) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		Usuario usuario = new Usuario();
		try {
			String query = "SELECT ID_USUARIO, NM_USUARIO, NR_RA, NM_EMAIL, NM_TELEFONE, ID_TP_STATUS, ID_TP_PERFIL_USUARIO FROM muraldb.dbo.TB_USUARIO WHERE ID_USUARIO = ?";
			usuarios = jdbcTemplate.query(query, new BeanPropertyRowMapper<Usuario>(Usuario.class),idUsuario);
			if(!usuarios.isEmpty()){
				usuario = usuarios.get(0);
			}
		}catch(Exception e) {
			System.out.println("Erro na execução da query do buscaDadosUsuarioPorId:"+e.getMessage());
		}
		return usuario;
	}

	public List<Usuario> buscaAlunoPorNome(String nomeUsuario) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			String query = "WITH USUARIOS AS (SELECT tu.ID_USUARIO,tu.NM_USUARIO,tgc.ID_GRUPO_COMPONENTE,tgc.ID_TP_STATUS FROM muraldb.dbo.TB_USUARIO tu LEFT JOIN muraldb.dbo.TB_GRUPO_COMPONENTE tgc ON tu.ID_USUARIO = tgc.ID_USUARIO WHERE tu.ID_TP_STATUS = 1 AND tu.ID_TP_PERFIL_USUARIO = 1 AND tu.NM_USUARIO LIKE ?) SELECT ID_USUARIO,NM_USUARIO FROM USUARIOS WHERE ID_TP_STATUS IS NULL OR ID_TP_STATUS NOT IN (5);";
			usuarios = jdbcTemplate.query(query, new BeanPropertyRowMapper<Usuario>(Usuario.class),"%"+nomeUsuario+"%");
		}catch(Exception e) {
			System.out.println("Erro na execução da query do buscaUsuarioPorNome:"+e.getMessage());
		}
		return usuarios;
	}
}