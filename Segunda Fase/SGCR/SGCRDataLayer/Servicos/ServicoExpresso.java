package SGCRDataLayer.Servicos;

public class ServicoExpresso extends Servico {

	private float custo;

	public float getCusto() {
		return this.custo;
	}

	@Override
	public boolean mudaEstado(EstadoServico estado) {
		//TODO
		return false;
	}
}