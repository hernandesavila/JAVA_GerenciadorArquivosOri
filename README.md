# 🗂️ File Security Manager

File Security Manager é um aplicativo **Java Swing** desenvolvido como trabalho da disciplina de **Organização e Recuperação da Informação (PUC Minas - Poços de Caldas)**. O sistema fornece uma experiência estilo *explorer* para navegação em pastas privadas, com autenticação externa e ferramentas básicas de segurança para arquivos.

A interface principal apresenta uma árvore de diretórios à esquerda e uma tabela com arquivos à direita, permitindo abrir, renomear, excluir, imprimir e cifrar documentos dentro do cofre pessoal do usuário autenticado.

---

## 🛠️ Tecnologias Utilizadas

<p align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" alt="Java" width="30" height="30"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/apache/apache-original.svg" alt="Ant" width="30" height="30"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/netbeans/netbeans-original.svg" alt="NetBeans" width="30" height="30"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/windows8/windows8-original.svg" alt="Windows" width="30" height="30"/>
</p>

- **Java SE 7/8** – linguagem e runtime utilizados pelo aplicativo desktop
- **Swing** – interface gráfica com `JTree`, `JTable` e diálogos de status
- **NetBeans** – ambiente de desenvolvimento original (estrutura `nbproject` e `build.xml`)
- **Apache Ant** – scripts de compilação e empacotamento fornecidos pelo NetBeans
- **Executáveis em C** – autenticação (`autenticaUsuario.exe`) e cadastro de usuários (`cadastro.exe`)

---

## 📂 Estrutura do Projeto

- `GerenciadorArquivosOri/src/Principal.java` – ponto de entrada, inicializa login, janela principal e diálogo *About*
- `GerenciadorArquivosOri/src/GerenciadorArquivos.java` – janela principal com árvore de diretórios, tabela de arquivos e ações de segurança
- `GerenciadorArquivosOri/src/Login.java` – tela de autenticação que integra com `autenticaUsuario.exe`
- `GerenciadorArquivosOri/src/About.java` – janela com créditos da equipe e informações da disciplina
- `GerenciadorArquivosOri/src/Renderer.java` – renderização de ícones na `JTable`
- `GerenciadorArquivosOri/dist/` – artefatos pré-compilados (`GerenciadorArquivosOri.jar`, executáveis auxiliares e arquivos de dados)
- `GerenciadorArquivosOri/Arquivos Em C/` – código-fonte em C para os utilitários externos
- `GerenciadorArquivosOri/Private/` – pastas privadas (uma por usuário) manipuladas pelo aplicativo

---

## ✅ Pré-requisitos

- **Windows** com permissão para executar aplicativos desktop e scripts `cmd.exe`
- **JDK 7 ou 8** instalado e configurado no `PATH`
- **NetBeans 8.x** (ou IDE compatível com projetos Ant gerados pelo NetBeans)
- Permissão para executar os auxiliares `autenticaUsuario.exe`, `cadastro.exe` e `lockUnlockFolder.exe`

---

## ⚙️ Configuração

1. **Executáveis auxiliares**
   - Os arquivos `autenticaUsuario.exe`, `cadastro.exe`, `lockUnlockFolder.exe`, `informaDados.txt` e `respostaDados.txt` devem permanecer ao lado do `GerenciadorArquivosOri.jar` (pasta `dist/`).
   - Os executáveis em C dependem dos arquivos `.txt` para troca de mensagens com a aplicação Java.

2. **Pastas privadas**
   - Cada usuário autenticado acessa uma pasta em `dist/Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}/<idUsuario>/`.
   - Utilize `lockUnlockFolder.exe` para bloquear/desbloquear o diretório quando necessário.

3. **Cadastro de usuários**
   - Execute `cadastro.exe` para adicionar novos registros ao arquivo `users.usr`.
   - Para testes existe o usuário padrão listado em `GerenciadorArquivosOri/Usuário e Senha para Testar.txt`.

---

## 🛠️ Compilação

### Via NetBeans
1. Abra `GerenciadorArquivosOri/build.xml` no NetBeans.
2. Execute o comando **Clean and Build** para gerar `dist/GerenciadorArquivosOri.jar` e copiar os recursos para `dist/`.

### Via Ant (linha de comando)
```bash
cd GerenciadorArquivosOri
ant clean jar
```
Os artefatos são salvos na pasta `dist/`.

---

## ▶️ Execução

1. Acesse `GerenciadorArquivosOri/dist/` e certifique-se de que os executáveis auxiliares estão disponíveis.
2. Execute `GerenciadorArquivosOri.jar` (duplo clique ou `java -jar GerenciadorArquivosOri.jar`).
3. Informe o usuário e senha válidos (por exemplo `joao` / `123mudar`).
4. Após autenticação, a janela principal exibirá o conteúdo da pasta privada do usuário.

> ⚠️ A autenticação ocorre via `cmd.exe` e interação com `autenticaUsuario.exe`. Certifique-se de que o antivírus não bloqueia a execução.

---

## 🔎 Funcionamento

- **Árvore de diretórios**: exibe a pasta privada com renderização de ícones do sistema (`FileSystemView`).
- **Tabela de arquivos**: lista os itens do diretório selecionado e habilita ações de abrir, imprimir e renomear conforme o tipo.
- **Criptografia simples**: os botões **Criptografar** e **Descriptografar** aplicam uma cifra básica de deslocamento byte a byte no arquivo selecionado.
- **Status bar**: mostra o nome do usuário e o espaço utilizado (total de arquivos, pastas e tamanho em KB).
- **Integração com desktop**: permite abrir arquivos com o aplicativo padrão (`Desktop.getDesktop()`) e exibe diálogos de confirmação para exclusão.
- **Janela About**: apresenta créditos dos autores e da disciplina.

---

## 📌 Observações

- O algoritmo de criptografia é intencionalmente simples (adição/subtração de 1 byte) e serve apenas para fins educacionais.
- A troca de mensagens com os executáveis externos ocorre por meio de arquivos temporários (`informaDados.txt` / `respostaDados.txt`).
- Todos os diálogos do sistema estão em português e podem ser editados diretamente nos arquivos `.java` e `.form`.
- Recomenda-se executar o projeto em um diretório sem espaços para evitar problemas com o `cmd.exe`.

---

## 📄 Licença

Este repositório está licenciado sob os termos da [MIT License](LICENSE).
