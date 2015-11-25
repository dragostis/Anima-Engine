#include <jni.h>

#include <btBulletDynamicsCommon.h>

JNIEXPORT jlong JNICALL
Java_org_anima_engine_gameplay_physics_ConeShape_getConeShapePointer(JNIEnv *env, jobject instance,
                                                                     jfloat radius, jfloat height) {
    return (jlong) new btConeShape(btScalar(radius), btScalar(height));
}