#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <windows.h>

void lockFolder();
void unlockFolder();

main(int argc, char *argv[])
{
    if(strcmp(argv[1], "lockFolder") == 0)
    {
        lockFolder();
    }
    else if(strcmp(argv[1], "openFolder") == 0)
    {
        unlockFolder();
    }

    exit(0);
}

void lockFolder()
{
    system("attrib +h +s \"Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}\"");
}

void unlockFolder()
{
    system("attrib -h -s \"Control Panel.{21EC2020-3AEA-1069-A2DD-08002B30309D}\"");
}
