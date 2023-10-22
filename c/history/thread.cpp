//
// Created by tangbu on 7/26/22.
//

#include "Poco/ThreadPool.h"
#include "Poco/Runnable.h"
#include <iostream>
class HelloRunnable: public Poco::Runnable
{
    void run() override
    {
        std::cout << "Hello, bingzhe" << std::endl;
    }
};
int main(int argc, char** argv)
{
    HelloRunnable runnable;
    Poco::ThreadPool::defaultPool().start(runnable);
    Poco::ThreadPool::defaultPool().joinAll();
    return 0;
}
