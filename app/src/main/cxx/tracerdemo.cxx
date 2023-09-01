#include <jni.h>

void crash() {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-pragmas"
#pragma ide diagnostic ignored "NullDereference"
    volatile int *a = reinterpret_cast<volatile int *>(NULL);
    *a = 1;
#pragma clang diagnostic pop
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_tracer_MainActivity_nativeDereferenceNull(
        JNIEnv *,
        jobject) {

    crash();
}
