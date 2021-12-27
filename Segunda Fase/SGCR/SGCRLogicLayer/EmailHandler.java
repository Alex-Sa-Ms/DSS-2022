package SGCRLogicLayer;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class EmailHandler {

	public static void emailExcesso(String email) {
		try {
			HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandbox5b0cd547ca7745e6925e828a839d8fe7.mailgun.org" + "/messages")
					.basicAuth("api", "487b641651372aff037e27f95d0e33ed-1831c31e-9a85e1c3")
					.field("from", "Uminho@sandbox5b0cd547ca7745e6925e828a839d8fe7.mailgun.org.com")
					.field("to", email)
					.field("subject", "Orçamento excedido")
					.field("text", "Caro Cliente" +
							"\nA reparação do seu equipamento excedeou o orçamento previsto em 20%." +
							"\nDeseja processiguir a reparação?")
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	public static void emailPronto(String email) {
		try {
			HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandbox5b0cd547ca7745e6925e828a839d8fe7.mailgun.org" + "/messages")
					.basicAuth("api", "487b641651372aff037e27f95d0e33ed-1831c31e-9a85e1c3")
					.field("from", "Uminho@sandbox5b0cd547ca7745e6925e828a839d8fe7.mailgun.org.com")
					.field("to", email)
					.field("subject", "Equipamento pronto para recolha")
					.field("text", "Caro Cliente" +
							"\nA reparação do seu equipamento encontra-se concluida." +
							"\nPoderá recolher o seu equipamento assim que possivel.")
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	public static void emailIrreparavel(String email) {
		try {
			HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandbox5b0cd547ca7745e6925e828a839d8fe7.mailgun.org" + "/messages")
					.basicAuth("api", "487b641651372aff037e27f95d0e33ed-1831c31e-9a85e1c3")
					.field("from", "Uminho@sandbox5b0cd547ca7745e6925e828a839d8fe7.mailgun.org.com")
					.field("to", email)
					.field("subject", "Equipamento irreparavel")
					.field("text", "Caro Cliente" +
							"\nA reparação do seu equipamento não e possivel de ser efetuada." +
							"\nPoderá recolher o seu equipamento assim que possivel.")
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	public static void emailOrcamento(String email, String orcamento) {
		try {
			HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandbox5b0cd547ca7745e6925e828a839d8fe7.mailgun.org" + "/messages")
					.basicAuth("api", "487b641651372aff037e27f95d0e33ed-1831c31e-9a85e1c3")
					.field("from", "Uminho@sandbox5b0cd547ca7745e6925e828a839d8fe7.mailgun.org.com")
					.field("to", email)
					.field("subject", "Orçamento de reparação")
					.field("text", "Caro Cliente" +
							"\nOrçamento de reparação do seu equipamento." +
							"\n" + orcamento +
							"\nDeseja prosseguir a reparação?")
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	public static void emailCancelado(String email) {
		try {
			HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandbox5b0cd547ca7745e6925e828a839d8fe7.mailgun.org" + "/messages")
					.basicAuth("api", "487b641651372aff037e27f95d0e33ed-1831c31e-9a85e1c3")
					.field("from", "Uminho@sandbox5b0cd547ca7745e6925e828a839d8fe7.mailgun.org.com")
					.field("to", email)
					.field("subject", "Confirmação de cancelamento")
					.field("text", "Caro Cliente" +
							"\nEsta confirmado o cancelamento da reparação do seu equipamento.")
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

}