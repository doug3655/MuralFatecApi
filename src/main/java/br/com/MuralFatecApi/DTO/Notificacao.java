package br.com.MuralFatecApi.DTO;
import lombok.Data;

@Data
public class Notificacao {
	
	private int id_notificacao;
	private Integer id_tp_notificacao;
	private Integer id_tp_status;
	private Integer nr_entidade_alvo;
}