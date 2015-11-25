#include <jni.h>

#include <btBulletDynamicsCommon.h>

JNIEXPORT jlong JNICALL
Java_org_anima_engine_gameplay_physics_CylinderShape_getCylinderShapePointer(JNIEnv *env,
                                                                             jobject instance,
                                                                             jfloat x, jfloat y,
                                                                             jfloat z) {
    return (jlong) new btCylinderShape(btVector3(x, y, z));
}