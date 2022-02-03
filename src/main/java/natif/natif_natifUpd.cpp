#define _CRT_SECURE_NO_WARNINGS
#include "natif_natifUpd.h"
#include <iostream>
#include <fstream>
#include "windows.h"
//typedef wchar_t TCHAR;
//g++ -I"%JAVA_HOME%/include" -I"%JAVA_HOME%/include/win32"
 //-fPIC src\main\java\natif\natif_natifUpd.cpp -shared -o src\main\java\natif\natif_natifUpd.so

 // afl-fuzz.exe -i IN -o OUT -t 5000 -m none -Y -- -- c:\winafl\DumbWinAFL\x64\Release\DumpWinAFL.exe "java -jar c:\winafl\TestAsm\ModifyClasses\JavaTest-1.0.jar @@"

#define AFL_STATIC_CONFIG_ENV TEXT("AFL_STATIC_CONFIG")
#define MAP_SIZE 65536
#define env_size 100

#ifdef __cplusplus
extern "C" {
#endif

TCHAR envbuff[env_size];
TCHAR shm[env_size];
TCHAR* fuzzid;
TCHAR* b;

int firstrun = 0;
int prev = 0;
int mai = -1;
char* __afl_area_ptr;//[MAP_SIZE];
/*
JNIEXPORT jobject JNICALL Java_natif_natifUpd_get_1shared_1bytes
 (JNIEnv *env, jclass, jstring javaString){
    void* myBuffer;
    int bufferLength;

    const char *nativeString = env->GetStringUTFChars(javaString, 0);

    bufferLength = 1024; // assuming your buffer is 1024 bytes big
    HANDLE mem = OpenFileMapping(FILE_MAP_READ, // assuming you only want to read
               false, nativeString); // assuming your file mapping is called "MyBuffer"
    myBuffer = MapViewOfFile(mem, FILE_MAP_READ, 0, 0, 0);
        // don't forget to do UnmapViewOfFile when you're finished
    return env->NewDirectByteBuffer(myBuffer, bufferLength);
}
*/
JNIEXPORT void JNICALL Java_natif_natifUpd_mapUp
  (JNIEnv *, jclass, int i){
         std::wofstream out;
         out.open("testjavaclog.txt", std::ios::app);
         unsigned int j;
         j = (prev ^ i);
         j %= MAP_SIZE;
      //   out << "instrumentation value modify: "<< i << " to "<< j << std::endl;
      //   out << "instrumentation value prev: "<< prev << std::endl;

         if(__afl_area_ptr == NULL){
             mai = i;
             out << "on prend __afl_area_ptr" << std::endl;
             PVOID areaPtr = NULL;

             GetEnvironmentVariable(TEXT("AFL_STATIC_CONFIG"), envbuff, env_size);
             if(GetLastError() == ERROR_ENVVAR_NOT_FOUND){
                 out << "afl env static not found" << std::endl;
                 out.close();
             }
             out << "afl env: " << envbuff << std::endl;
             fuzzid = strtok(envbuff, ":");
             strcpy_s(shm, sizeof("afl_shm_"), "afl_shm_");
             strcat_s(shm, fuzzid);
             out << "afl shm: " << shm << std::endl;
             HANDLE mem = OpenFileMapping(FILE_MAP_ALL_ACCESS, false, shm);
             areaPtr = MapViewOfFile(mem, FILE_MAP_ALL_ACCESS, 0, 0, 0);
             if(areaPtr == NULL){
                out << "shm value failed" << std::endl;
                out.close();
             }
             __afl_area_ptr = (char*)areaPtr;
         }

      //   out << "map modification" << std::endl;
         __afl_area_ptr[j]++;

         prev = i >> 1; // pour contruire bien l'itiniraire
         out.close();
 }

#ifdef __cplusplus
}
#endif
