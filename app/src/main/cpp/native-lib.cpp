#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_agi_dede_mobile_android_ddcrypto_CommandActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
