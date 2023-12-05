package br.com.MuralFatecApi.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import br.com.MuralFatecApi.DTO.Aluno;
import br.com.MuralFatecApi.DTO.Grupo;
import br.com.MuralFatecApi.DTO.Usuario;

@Service
public class ServiceCadastro {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String sqlInsertVariosAlunos = "INSERT INTO muraldb.dbo.TB_GRUPO_COMPONENTE (ID_GRUPO,ID_USUARIO,ID_TP_STATUS) VALUES(?,?,?)";

	public boolean registraUsuario(Usuario usuario) throws Exception{
		int status = 0;
		try {
			SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("TB_USUARIO").usingGeneratedKeyColumns("ID_USUARIO");
			Map<String, Object> parameters = gerarParametersInsertUsuario(usuario);
			Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
			String sqlRegistraNotigicacao = "INSERT INTO muraldb.dbo.TB_NOTIFICACAO (ID_TP_NOTIFICACAO,ID_TP_STATUS,NR_ENTIDADE_ALVO) VALUES (1,3,?)";
			status = jdbcTemplate.update(sqlRegistraNotigicacao,id.intValue());
		}catch(Exception e) {
			System.out.println("Erro na execução da query do registraUsuario:"+e.getMessage());
		}
		return status != 0 ? true : false;
	}
	
	public boolean registraGrupo(Grupo grupo) {
		Integer status = 0;
		try {
			SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("TB_GRUPO").usingGeneratedKeyColumns("ID_GRUPO");
			Map<String, Object> parameters = gerarParametersInsertGrupo(grupo);
			Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
			Aluno aluno = grupo.getAlunos().get(0);
			status = jdbcTemplate.update(sqlInsertVariosAlunos,id.intValue(),aluno.getId_usuario(),5);
			//grupo.getAlunos().forEach((item) ->jdbcTemplate.update(sqlInsertVariosAlunos,id.intValue(),item.getId_usuario())); -> insert all data of array list
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Erro na execução da query do registraUsuario:"+e.getMessage());
		}
		
		return status != 0 ? true : false;
	}
	
	private Map<String, Object> gerarParametersInsertGrupo(Grupo grupo){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("NM_TEMA",grupo.getNm_tema());
		parameters.put("ID_ORIENTADOR",grupo.getId_orientador());
		parameters.put("ID_TP_CURSO",grupo.getId_tp_curso());
		parameters.put("ID_TP_PERIODO",grupo.getId_tp_periodo());
		parameters.put("ID_TP_STATUS_VINCULO",grupo.getId_tp_status_vinculo());
		parameters.put("ID_TP_STATUS",grupo.getId_tp_status());
		parameters.put("FL_TG1",grupo.getFl_tg1());
		parameters.put("FL_TG2",grupo.getFl_tg2());
		return parameters;
	}
	
	private Map<String, Object> gerarParametersInsertUsuario(Usuario usuario){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("NM_USUARIO",usuario.getNm_usuario());
		parameters.put("NR_RA",usuario.getNr_ra());
		parameters.put("NM_EMAIL",usuario.getNm_email());
		parameters.put("NM_TELEFONE",usuario.getNm_telefone());
		parameters.put("NM_SENHA",usuario.getNm_senha());
		parameters.put("ID_TP_STATUS",3);
		return parameters;
	}
}
