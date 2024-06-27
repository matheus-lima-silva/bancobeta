package com.bancobeta.bancobeta.exception;

public class ExcecoesBancarias {

    public static class ExcecaoBancaria extends RuntimeException {
        public ExcecaoBancaria(String mensagem) {
            super(mensagem);
        }

        public ExcecaoBancaria(String mensagem, Throwable causa) {
            super(mensagem, causa);
        }
    }

    public static class SaldoInsuficienteException extends ExcecaoBancaria {
        public SaldoInsuficienteException(String mensagem) {
            super(mensagem);
        }
    }

    public static class LimiteChequeEspecialExcedidoException extends ExcecaoBancaria {
        public LimiteChequeEspecialExcedidoException(String mensagem) {
            super(mensagem);
        }
    }

    public static class LimiteTransferenciaExcedidoException extends ExcecaoBancaria {
        public LimiteTransferenciaExcedidoException(String mensagem) {
            super(mensagem);
        }
    }

    public static class ContaInvalidaException extends ExcecaoBancaria {
        public ContaInvalidaException(String mensagem) {
            super(mensagem);
        }
    }

    public static class ClienteNaoEncontradoException extends ExcecaoBancaria {
        public ClienteNaoEncontradoException(String mensagem) {
            super(mensagem);
        }
    }
    public static class QuantidadeMaximaDeSaquesExcedidaException extends ExcecaoBancaria {
        public QuantidadeMaximaDeSaquesExcedidaException(String message) {
            super(message);
        }
    }
}
