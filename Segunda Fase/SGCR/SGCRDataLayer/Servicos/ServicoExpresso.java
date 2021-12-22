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

	@Override
	public int compareTo(Object o) {
		if(o instanceof Servico){
			return super.getDataConclusao().compareTo(((Servico) o).getDataConclusao());
		}
		return -1;
	}
}