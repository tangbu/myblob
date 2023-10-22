
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <fcntl.h>


int main(int argc,  char* argv[])
{
    //pid_t int类型, a代表子进程号
    pid_t a;
    //创建子进程， 父进程中a是子进程号，子进程中a=0
    a = fork();
    if(a > 0)
    {
        // 父进程退出，子进程继承父进程代码块继续运行，a就是子进程号
        exit(0);
    }
//    子进程忽略终端退出SIGHUP信号
    signal(SIGHUP, SIG_IGN);

//    创建新会话，脱离原会话，脱离控制终端。
    setsid();
    //脱离原进程组，创建并进入只包含自身的进程组
    setpgrp();

    //关闭父辈继承下来的所有文件
    int max_fd = sysconf(_SC_OPEN_MAX);
    for (int i = 0; i < max_fd && i > 2; i++)
    {
        close(i);
    }

    //设置掩码位
    umask(0);

  /*  int drop_fd=open( "/dev/null", O_RDWR );
    dup2(0, drop_fd);
    dup2(1, drop_fd);
    dup2(2, drop_fd);*/

    //切换工作路径
    chdir("/");

// deamon postman arg1 args 变成了postman arg1 arg2

    if (argc > 2) {
        char *params[argc - 2];
        memcpy(params, argv + 1, sizeof params);
        params[argc - 3] = NULL;
// execvp支持在环境变量PATH中寻找postman命令并替换代码段，运行新进程的代码段，
        execvp(*params, params);
    } else {
        execvp(argv[1], NULL);

    }
    return 0;
}

