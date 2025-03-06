#include <stdio.h>
#include <string.h>
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
} usuario;

main()
{
    FILE *arquivo;
    char caractere, user[15] = "", senha[10] = "";
    int i, verificaDados;
    usuario usuarioVerifica;

    while((arquivo = fopen("informaDados.txt","r")) == NULL)
    {
    }

    Sleep(1000);

    i = 0;

    do
    {
        caractere = fgetc(arquivo);

        if(caractere != '=' && !feof(arquivo))
        {
            user[i] = caractere;
        }

        i++;
    } while(caractere != '=' && !feof(arquivo));

    i = 0;

    do
    {
        caractere = fgetc(arquivo);

        if(caractere != ';' && !feof(arquivo))
        {
            senha[i] = caractere;
        }

        i++;
    } while(caractere != ';' && !feof(arquivo));

    fclose(arquivo);
    remove("informaDados.txt");

    arquivo = fopen("users.usr","rb");

    if(arquivo == NULL)
    {
        arquivo = fopen("respostaDados.txt","w");

        if(arquivo == NULL)
            exit(1);

        fprintf(arquivo, "erro;");
    }

    verificaDados = FALSE;

    while(!feof(arquivo))
    {
        fread(&usuarioVerifica, sizeof(usuario), 1, arquivo);

        if(strcmp(usuarioVerifica.usuario, user) == 0 && strcmp(usuarioVerifica.senha, senha) == 0 && !feof(arquivo))
        {
            verificaDados = TRUE;
            break;
        }
    }

    if(verificaDados)
    {
        arquivo = fopen("respostaDados.txt","w");

        if(arquivo == NULL)
            exit(1);

        fprintf(arquivo, "true;%d=%s#%d$", usuarioVerifica.id,usuarioVerifica.nome,usuarioVerifica.status);
    }
    else
    {
        arquivo = fopen("respostaDados.txt","w");

        if(arquivo == NULL)
            exit(1);

        fprintf(arquivo, "false;");
    }

    fclose(arquivo);
    exit(0);

}
