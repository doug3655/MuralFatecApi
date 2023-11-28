package br.com.MuralFatecApi.DTO;
import lombok.Data;

@Data
public class Notificacao {
	
	private int ID_NOTIFICACAO;
	private TpNotificacao tpNotificacao;
	private Status status;
	private int NR_ENTIDADE_ALVO;
}