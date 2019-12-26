public class Movimento {

    private String operacao;
    private String descricao;
    private float valor;
    private float saldoApos;

    public Movimento(String operacao, String descricao, float valor, float saldoApos) {
        this.operacao = operacao;
        this.descricao = descricao;
        this.valor = valor;
        this.saldoApos = saldoApos;
    }

    public Movimento(String[] args){
        this.operacao = args[0];
        this.descricao = args[1];
        this.valor = Float.parseFloat(args[2]);
        this.saldoApos = Float.parseFloat(args[3]);
    }

    public String toString(){

        return ("Operacao: " + this.operacao +
                "; Descricao: " + this.descricao +
                "; Valor: " + this.valor +
                "; Saldo apos movimento: " + this.saldoApos);
    }
}
