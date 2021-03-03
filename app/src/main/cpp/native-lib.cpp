//
// Created by loanp on 03/03/2021.
//

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_loan_bankapplication_AccountsListActivity_stringFromJNI(JNIEnv *env, jobject object) {
    std::string hello = "https://60102f166c21e10017050128.mockapi.io/";
    return env->NewStringUTF(hello.c_str());
}