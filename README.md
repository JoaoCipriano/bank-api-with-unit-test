# Bank API

Este projeto tem o intuito de apresentar testes unitários utilizando as principais ferramentas disponíveis para a linguagem Java.

### Casos de uso
* Recuperar a carteira do cliente
  * Deve consultar pelo código do cliente
  * Caso o cliente não exista deve lançar uma exceção
* Depositar na carteira cliente
  * Deve consultar pelo código do cliente
  * Caso o cliente não exista deve lançar uma exceção
  * Deve depositar na carteira
* Sacar saldo da carteira
  * Deve consultar pelo código do cliente
  * Caso o cliente não exista deve lançar uma exceção
  * Caso não tenha saldo suficiente deve lançar uma exceção
* Transferir saldo da carteira
  * Deve consultar pelo código do cliente que irá transferir
  * Caso o cliente não exista deve lançar uma exceção
  * Caso não tenha saldo suficiente deve lançar uma exceção
  * Deve consultar pelo código do cliente que irá receber
  * Caso o cliente que irá receber não exista deve lançar uma exceção

Utilize o comando abaixo para subir o serviço localmente

cmd:

```
mvnw spring-boot:run
```

bash:

```
$ ./mvnw spring-boot:run
```

Utilize o comando abaixo para executar todos os testes

cmd:

```
mvnw test
```

bash:

```
$ ./mvnw test
```