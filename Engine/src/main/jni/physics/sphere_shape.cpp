#include <jni.h>

#include <btBulletDynamicsCommon.h>

JNIEXPORT jlong JNICALL
Java_org_anima_engine_gameplay_physics_SphereShape_getSphereShapePointer(JNIEnv *env,
                                                                         jobject instance,
                                                                         jfloat radius) {
    return (jlong) new btSphereShape(btScalar(radius));
}