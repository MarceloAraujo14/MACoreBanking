Projeto: Core Banking

Descrição: Um sistema responsável por realizar, manter e disponibilizar registros de movimentações
financeiras entre contas bancárias.

Constraints:
- Contas devem ser únicas
- Todas as movimentações bancárias devem possuir e manter registro com identificador unico, data, hora, valor, origem e destino da movimentação
- Não deve ocorrer retirada de saldo que não exista na conta de origem
- A conta de origem e destino devem existir para que a movimentação se concretize
- Não devem ocorrer movimentações simultaneas para mesma conta de origem ou destino

Funcionalidades:
- Transferência entre conta de origem e destino
- Extrato das movimentações de uma conta
- Extrado de movimentação por identificador unico
- Extrado de movimentações por data


Tecnologias:
- Java 17
- Gradle
- SpringBoot
- Kafka
- AWS Lambda
- AWS SNS
- AWS SQS