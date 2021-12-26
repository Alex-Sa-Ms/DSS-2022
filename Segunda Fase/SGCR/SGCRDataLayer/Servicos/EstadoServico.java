package SGCRDataLayer.Servicos;

import java.io.Serializable;

public enum EstadoServico implements Serializable {
	Irreparavel,
	AguardarConfirmacao,
	OrcamentoRecusado,
	EsperandoReparacao,
	EmExecucao,
	Interrompido,
	Concluido,
	Expirado
}