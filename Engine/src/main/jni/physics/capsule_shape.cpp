#include <jni.h>

#include <btBulletDynamicsCommon.h>

JNIEXPORT jlong JNICALL
Java_org_anima_engine_gameplay_physics_CapsuleShape_getCapsuleShapePointer(JNIEnv *env,
                                                                           jobject instance,
                                                                           jfloat radius,
                                                                           jfloat height) {
    return (jlong) new btCapsuleShape(btScalar(radius), btScalar(height));
}