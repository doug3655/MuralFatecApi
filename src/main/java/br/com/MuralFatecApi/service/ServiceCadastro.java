package br.com.MuralFatecApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.MuralFatecApi.DTO.Usuario;

@Service
public class ServiceCadastro {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean registraUsuario(Usuario usuario) {
		String sql = "INSERT INTO muraldb.dbo.TB_USUARIO (NM_USUARIO,NR_RA,NM_EMAIL,NM_TELEFONE,NM_SENHA,ID_TP_STATUS) " + " VALUES (?,?,?,?,?,?);";
		int status = jdbcTemplate.update(sql,usuario.getNm_usuario(),usuario.getNr_ra(),usuario.getNm_email(),usuario.getNm_telefone(),usuario.getNm_senha(),3);
		
		return status != 0 ? true : false;
	}
}
