package br.com.MuralFatecApi.DTO;

import lombok.Data;

@Data
public class Usuario {
	
	private Integer id_usuario;
	private String nm_usuario;
	private String nr_ra;
	private String nm_email;
	private String nm_telefone;
	private String nm_senha;
	private Integer id_tp_status;
	private Integer id_tp_perfil_usuario;
}