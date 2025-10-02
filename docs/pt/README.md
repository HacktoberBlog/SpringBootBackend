<!-- Translated by @phquartin — status: final -->

# 🌐 HacktoberBlog - Spring Boot Backend

Bem-vindo ao repositório backend do **HacktoberBlog** – uma plataforma de blog simples construída com **Spring Boot** e **Firebase Firestore**.  Esse projeto amigável para iniciantes e ideal para sua primeira contribuição open-source no **Hacktoberfest**! 🎉

---

## 📌 Sobre o Projeto

Esse é o serviço **backend** do HacktoberBlog. Fornece APIs para:

- 👤 Criar, atualizar, deletar e listar usuários
- 📝 Criar e recuperar posts
- ❤️ Dar like em blogs
- 💬 Comentar em posts
- 📧 Enviar e-mails de boas-vindas

Construído com:
- Java + Spring Boot
- Firebase Firestore (NoSQL DB)
- Firebase Admin SDK
- Spring Boot Actuator & Mail
- Lombok para um código limpo

---

## 🗂️ Estrutura do Projeto

```
src/
└── main/
├── java/com/hacktober/blog/
│ ├── blog/ → Lógica de serviço do blog
│ ├── user/ → Lógica do serviço do usuário
│ ├── email/ → Lógica do serviço de e-mail
│ ├── config/ → Inicialização do Firebase
│ └── utils/ → Funções ultilitárias (ex: password encoding)
└── resources/
└── application.properties
```

---

## 📘 Documentação da API

A documentação interativa da API está disponível nativamente graças ao Swagger UI e à especificação OpenAPI gerada.

1.  Execute a aplicação localmente:

    ```bash
    ./mvnw spring-boot:run
    ```

2.  Abra [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) no seu navegador para explorar e testar os endpoints.

O documento OpenAPI bruto pode ser baixado de [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) para integração com outras ferramentas.

---

## 🚀 Começando

### 1. Pré-requisitos

-   Java 17+
-   Maven
-   SMTP de Email (Gmail ou outros)
-   Projeto Firebase (Firestore + Conta de Serviço)

### 2. Clone o repositório

```bash
git clone [https://github.com/HacktoberBlog/SpringBootBackend.git](https://github.com/HacktoberBlog/SpringBootBackend.git)
cd SpringBootBackend
```

### 3. Configuração do Firebase

- Acesse o Console do Firebase

- Crie um projeto

- Habilite o Firestore

- Gere um arquivo JSON de Chave de Conta de Serviço (Service Account Key)

- Salve o arquivo em um caminho seguro, ex: /etc/secrets/firebaseServiceAccountKey.json

### 4. Configure o Email

Edite src/main/resources/application.properties:

```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu_email@gmail.com
spring.mail.password= chave de aplicativo de 16 caracteres, como abcd efgh ijkl mnop
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
redis.password= sua senha do banco de dados redis
```

#### Obtendo Chaves/Senhas para Email, BD, etc.
Chave de App de 16 caracteres do Gmail: Você pode obter a chave de aplicativo navegando para as configurações da sua conta Google->pesquise por "senhas de app"->dê um nome ao aplicativo->crie e copie a chave.

Senha do Redis: Você pode criar um banco de dados gratuito no [Redis](https://redis.io/). Após a criação do seu BD, clique no botão "Conectar usando Redis CLI, Cliente ou Insight". Lá você poderá visualizar sua senha (visualizar/ocultar usando o ícone de olho).


### 🔑 Configure os Segredos (Variáveis de Ambiente)

Para executar o projeto, você deve fornecer seus segredos como variáveis de ambiente.
Elas são usadas em application.properties através do sistema de placeholder do Spring:

| Nome da Variável       | Descrição                                                       | Valor de Exemplo       |
|------------------------|-----------------------------------------------------------------|------------------------|
| `spring.mail.username` | Seu Email para enviar emails de boas-vindas.                    | `seu_email@gmail.com`  |
| `spring.mail.password` | Sua chave de aplicativo de 16 caracteres para a conta de email. | `abcd efgh ijkl mnop`  |
| `redis.password`       | A senha para o seu banco de dados Redis.                        | `suaSenhaDoRedisDB123` |


## ⚠️ **NOTA:**
**🚫 NÃO FAÇA UM PUSH** de mudanças feitas no `application.properties`, `RedisConfig`, e `FirestoreService`.  
🔒 _Deixe seus segredos seguros!_ 🔒

## 🚀 Demo Live
🎉 O projeto está agora AO VIVO! Confira aqui: [HacktoberBlog Deployment](https://springbootbackend-onuz.onrender.com) 🌟  