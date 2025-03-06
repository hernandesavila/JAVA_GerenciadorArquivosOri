#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <conio.h>
#include <dirent.h>
#include <windows.h>

#define TRUE 1
#define FALSE 0
#define STATUS_ATIVO 1
#define STATUS_INATIVO 0

typedef struct
{
    int id;
    int nivel;
    int status;
    char nome[50];
    char usuario[25];
    char senha[10];
}usuario;

void lockFolder();
void unlockFolder();

main()
{
    FILE *arquivo;
    DIR *diretorioPrivate;
    char caractere, opMenu, opSubMenu, verificaUsuario[50] = "", verificaSenha[15] = "", usuarioId[10] = "", usuarioPasta[255] = "", exeThis[255] = "", thisPath[255] = "";
    int count, proxId, consultaUsuario;
    usuario usuarioCadastro;

    /*strcpy(exeThis, "cd ");
    GetCurrentDirectory(255,thisPath);
    strcat(exeThis, thisPath);
    system(exeThis);

    strcpy(exeThis, "");
    strcpy(exeThis, "folderLocker.exe unlock");
    system(exeThis);*/

    diretorioPrivate = opendir("\"Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}\"");

    if(diretorioPrivate == NULL)
    {
        mkdir("Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}");
        lockFolder();
    }

    closedir(diretorioPrivate);

    arquivo = fopen("users.usr","rb+");

    if(arquivo == NULL)
    {
        arquivo = fopen("users.usr","wb+");
    }

    if(arquivo == NULL)
    {
        system("cls");
        printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
        printf("\n\n --> ERRO ao abrir o arquivo de usuarios!");
        printf("\n\n\n Pressione ENTER para encerrar o sitema...");
        getchar();
        exit(1);
    }

    count = 0;

    while(!feof(arquivo))
    {
        fread(&usuarioCadastro, sizeof(usuario), 1, arquivo);

        if((!feof(arquivo) && usuarioCadastro.status != 0) && (usuarioCadastro.nivel == 1))
            count++;
    }

    if(count > 0)
    {

        do
        {
            system("cls");
            printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
            printf("\n\n Digite o NOME DE USUARIO: ");
            gets(verificaUsuario);
            printf("\n Digite a SENHA: ");
            gets(verificaSenha);
            fflush(stdin);

            opMenu = '0';
            fseek(arquivo, 0L, SEEK_SET);

            while(!feof(arquivo))
            {
                fread(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                if((!feof(arquivo) && strcmp(verificaUsuario, usuarioCadastro.usuario) == 0) && (strcmp(verificaSenha, usuarioCadastro.senha) == 0))
                {
                    if(usuarioCadastro.nivel != 1)
                        opMenu = '2';
                    else
                        opMenu = '1';

                    break;
                }
            }

            if(opMenu == '0')
            {
                system("cls");
                printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                printf("\n\n ---> Usuario ou Senha icorretos!");
                printf("\n\n\n Deseja Sair? S/N: ");
                opSubMenu = getche();
                opSubMenu = toupper(opSubMenu);

                if(opSubMenu == 'S')
                    exit(0);
            }
            else if(opMenu == '2')
            {
                system("cls");
                printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                printf("\n\n ---> Voce nao tem permissao para cadastrar usuarios!");
                printf("\n\n\n Deseja Sair? S/N: ");
                opSubMenu = getche();
                opSubMenu = toupper(opSubMenu);

                if(opSubMenu == 'S')
                    exit(0);
            }

        } while(opMenu != '1');
    }
    else
    {
        system("cls");
        printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
        printf("\n\n ---> Nao ha nenhum usuario cadastrado, cadastre algum usuario com nivel 1 para ser o administrador do sistema!");
        printf("\n\n\n Precione ENTER para ira para o Menu Principal...");
        getchar();
    }

    do
    {
        system("cls");
        printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
        printf("\n\n MENU:\n =====\n\n 1- Cadastrar\n 2- Editar\n 3- Excluir\n 4- Listar\n 5- Sair\n\n Digite: [ ]\b\b");
        opMenu = getche();
        fflush(stdin);

        switch(opMenu)
        {
            case '1':
                proxId = 0;
                count = 0;
                fseek(arquivo, 0L, SEEK_SET);

                while(!feof(arquivo))
                {
                    fread(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                    if(!feof(arquivo))
                        count++;
                }

                proxId = count + 1;


                system("cls");
                printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                printf("\n\n CADASTRAR:\n ==========");
                printf("\n\n Digite o NOME: ");
                gets(usuarioCadastro.nome);
                printf("\n\n Digite o USUARIO: ");
                gets(usuarioCadastro.usuario);
                printf("\n\n Digite a SENHA: ");
                gets(usuarioCadastro.senha);
                printf("\n\n Digite o NIVEL do usuario: ");
                scanf("%d", &usuarioCadastro.nivel);
                fflush(stdin);

                usuarioCadastro.id = proxId;
                usuarioCadastro.status = STATUS_ATIVO;

                fseek(arquivo, 0L, SEEK_END);
                fwrite(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                printf("\n --> Usuario inserido...");
                printf("\n\n\n Precione ENTER para retornar ao Menu Principal...");
                getchar();

                fclose(arquivo);
                arquivo = fopen("users.usr","rb+");

                if(arquivo == NULL)
                {
                    system("cls");
                    printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                    printf("\n\n --> ERRO ao abrir o arquivo de usuarios!");
                    printf("\n\n\n Pressione ENTER para encerrar o sitema...");
                    getchar();
                    exit(1);
                }

                strcpy(usuarioId, "");
                strcpy(usuarioPasta, "");

                itoa(usuarioCadastro.id, usuarioId, 10);
                strcat(usuarioPasta, "Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}\\");
                strcat(usuarioPasta, usuarioId);
                unlockFolder();
                mkdir(usuarioPasta);
                lockFolder();

            break;
            case '2':
                system("cls");
                printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                printf("\n\n EDITAR:\n =======");
                printf("\n\n Digite o CODIGO  do usuario: ");
                scanf("%d", &consultaUsuario);

                fseek(arquivo, 0L, SEEK_SET);

                while(!feof(arquivo))
                {
                    fread(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                    if((!feof(arquivo) && consultaUsuario == usuarioCadastro.id) && (usuarioCadastro.status != STATUS_INATIVO))
                    {
                        do
                        {
                            system("cls");
                            printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                            //printf("\n\n EDITAR:\n =======\n\n 1- Nome\n 2- Usuario\n 3- Senha\n 4- Nivel\n 5- Status\n 6- Menu Principal\n\n Digite: [ ]\b\b");
                            printf("\n\n EDITAR:\n =======\n\n 1- Nome\n 2- Usuario\n 3- Senha\n 4- Nivel\n 5- Menu Principal\n\n Digite: [ ]\b\b");
                            opSubMenu = getche();
                            fflush(stdin);

                            switch(opSubMenu)
                            {
                                case '1':
                                    system("cls");
                                    printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                    printf("\n\n EDITAR:\n =======");
                                    printf("\n\n Digite o NOME: ");
                                    gets(usuarioCadastro.nome);
                                    fflush(stdin);

                                    fseek(arquivo,-1*sizeof(usuario),SEEK_CUR);
                                    fwrite(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                                    printf("\n Usuario alterado...");
                                    printf("\n\n\n Pressione ENTER para volar...");
                                    getchar();

                                    fclose(arquivo);
                                    arquivo = fopen("users.usr","rb+");

                                    if(arquivo == NULL)
                                    {
                                        system("cls");
                                        printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                        printf("\n\n --> ERRO ao abrir o arquivo de usuarios!");
                                        printf("\n\n\n Pressione ENTER para encerrar o sitema...");
                                        getchar();
                                        exit(1);
                                    }
                                break;
                                case '2':
                                    system("cls");
                                    printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                    printf("\n\n EDITAR:\n =======");
                                    printf("\n\n Digite o nome de USUARIO: ");
                                    gets(usuarioCadastro.usuario);
                                    fflush(stdin);

                                    fseek(arquivo,-1*sizeof(usuario),SEEK_CUR);
                                    fwrite(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                                    printf("\n Usuario alterado...");
                                    printf("\n\n\n Pressione ENTER para volar...");
                                    getchar();

                                    fclose(arquivo);
                                    arquivo = fopen("users.usr","rb+");

                                    if(arquivo == NULL)
                                    {
                                        system("cls");
                                        printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                        printf("\n\n --> ERRO ao abrir o arquivo de usuarios!");
                                        printf("\n\n\n Pressione ENTER para encerrar o sitema...");
                                        getchar();
                                        exit(1);
                                    }
                                break;
                                case '3':
                                    system("cls");
                                    printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                    printf("\n\n EDITAR:\n =======");
                                    printf("\n\n Digite a SENHA: ");
                                    gets(usuarioCadastro.senha);
                                    fflush(stdin);

                                    fseek(arquivo,-1*sizeof(usuario),SEEK_CUR);
                                    fwrite(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                                    printf("\n Usuario alterado...");
                                    printf("\n\n\n Pressione ENTER para volar...");
                                    getchar();

                                    fclose(arquivo);
                                    arquivo = fopen("users.usr","rb+");

                                    if(arquivo == NULL)
                                    {
                                        system("cls");
                                        printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                        printf("\n\n --> ERRO ao abrir o arquivo de usuarios!");
                                        printf("\n\n\n Pressione ENTER para encerrar o sitema...");
                                        getchar();
                                        exit(1);
                                    }
                                break;
                                case '4':
                                    system("cls");
                                    printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                    printf("\n\n EDITAR:\n =======");
                                    printf("\n\n Digite o NIVEL de usuario: ");
                                    scanf("%d", &usuarioCadastro.nivel);
                                    fflush(stdin);

                                    fseek(arquivo,-1*sizeof(usuario),SEEK_CUR);
                                    fwrite(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                                    printf("\n Usuario alterado...");
                                    printf("\n\n\n Pressione ENTER para volar...");
                                    getchar();

                                    fclose(arquivo);
                                    arquivo = fopen("users.usr","rb+");

                                    if(arquivo == NULL)
                                    {
                                        system("cls");
                                        printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                        printf("\n\n --> ERRO ao abrir o arquivo de usuarios!");
                                        printf("\n\n\n Pressione ENTER para encerrar o sitema...");
                                        getchar();
                                        exit(1);
                                    }
                                break;
                                /* EDITAR STATUS
                                case '5':
                                    system("cls");
                                    printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                    printf("\n\n EDITAR:\n =======");
                                    printf("\n\n Digite o STATUS: ");
                                    scanf("%d", &usuarioCadastro.status);
                                    fflush(stdin);

                                    fseek(arquivo,-1*sizeof(usuario),SEEK_CUR);
                                    fwrite(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                                    printf("\n Usuario alterado...");
                                    printf("\n\n\n Pressione ENTER para volar...");
                                    getchar();

                                    fclose(arquivo);
                                    arquivo = fopen("users.usr","rb+");

                                    if(arquivo == NULL)
                                    {
                                        system("cls");
                                        printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                        printf("\n\n --> ERRO ao abrir o arquivo de usuarios!");
                                        printf("\n\n\n Pressione ENTER para encerrar o sitema...");
                                        getchar();
                                        exit(1);
                                    }
                                break;
                                */
                                case '5':
                                break;
                                default:
                                    system("cls");
                                    printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                                    printf("\n\n EDITAR:\n =======");
                                    printf("\n\n --> Opacao invalida!");
                                    Sleep(1000);
                                break;
                            }
                        } while(opSubMenu != '5');

                        break;
                    }

                    if(feof(arquivo))
                    {
                        printf("\n\n --> Usuario nao encontrado!");
                        printf("\n\n\n Pressione ENTER para volar...");
                        fflush(stdin);
                        getchar();
                    }
                }
            break;
            case '3':
                system("cls");
                printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                printf("\n\n DELETAR:\n ========");
                printf("\n\n Digite o CODIGO do usuario: ");
                scanf("%d", &consultaUsuario);
                getchar();
                fflush(stdin);

                fseek(arquivo, 0L, SEEK_SET);

                while(!feof(arquivo))
                {
                    fread(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                    if((!feof(arquivo) && consultaUsuario == usuarioCadastro.id) && (usuarioCadastro.status != STATUS_INATIVO))
                    {
                        usuarioCadastro.status = STATUS_INATIVO;
                        fseek(arquivo,-1*sizeof(usuario),SEEK_CUR);
                        fwrite(&usuarioCadastro, sizeof(usuario), 1, arquivo);
                        printf("\n --> Cod.: %d - %s excluido!", usuarioCadastro.id, usuarioCadastro.nome);

                        strcpy(usuarioId, "");
                        strcpy(usuarioPasta, "");

                        itoa(usuarioCadastro.id, usuarioId, 10);
                        strcat(usuarioPasta, "Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}\\");
                        strcat(usuarioPasta, usuarioId);
                        unlockFolder();
                        rmdir(usuarioPasta);
                        lockFolder();

                        printf("\n\n\n Pressione ENTER para volar...");
                        getchar();
                        break;
                    }

                    if(feof(arquivo))
                    {
                        printf("\n\n --> Usuario nao encontrado!");
                        printf("\n\n\n Pressione ENTER para volar...");
                        getchar();
                    }
                }
            break;
            case '4':
                system("cls");
                printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                printf("\n\n LISTAR:\n =======\n\n");

                count = 0;
                fseek(arquivo, 0L, SEEK_SET);

                while(!feof(arquivo))
                {
                    fread(&usuarioCadastro, sizeof(usuario), 1, arquivo);

                    if(!feof(arquivo) && usuarioCadastro.status != 0)
                    {
                        printf(" %02d - %-50s\n", usuarioCadastro.id, usuarioCadastro.nome);
                        count ++;
                    }

                }

                if(count == 0)
                    printf(" --> O arquivo esta sem nenhum registro!");
                else if(count == 1)
                    printf("\n --> O arquivo possui %d registro!", count);
                else
                    printf("\n --> O arquivo possui %d registros!", count);

                printf("\n\n\n Pressione ENTER para voltar ao Menu Principal...");
                getchar();
            break;
            case '5':
                system("cls");
                printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                printf("\n\n SAIR:\n =====");
                printf("\n\n --> Saindo...");
                Sleep(500);
                exit(0);
            break;
            default:
                system("cls");
                printf("\n                              Cadastro de Usuarios\n--------------------------------------------------------------------------------");
                printf("\n\n MENU:\n =====");
                printf("\n\n --> Opacao invalida!");
                Sleep(500);
            break;
        }
    } while(opMenu != '5');

    fclose(arquivo);
    exit(0);
}

void lockFolder()
{
    //rename("Private", "Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}");
    system("attrib +h +s \"Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}\"");
}

void unlockFolder()
{
    system("attrib -h -s \"Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}\"");
    //rename("Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}", "Private");
}
