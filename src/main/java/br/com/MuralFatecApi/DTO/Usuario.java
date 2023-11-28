package br.com.MuralFatecApi.DTO;
import lombok.Data;

@Data
public class Usuario {
	
	private int ID_USUARIO;
	private String NM_USUARIO;
	private String NR_RA;
	private String NM_EMAIL;
	private String NM_TELEFONE;
	private String NM_SENHA;
	private Status status;
	private Perfil perfil;
}
