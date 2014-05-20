#include "com_ideas_anima_engine_gameplay_physics_SphereShape.h"
#include "btBulletDynamicsCommon.h"

JNIEXPORT jlong JNICALL Java_com_ideas_anima_engine_gameplay_physics_SphereShape_getSphereShapePointer
        (JNIEnv *env, jobject thisObject, jfloat radius) {
    return (jlong) new btSphereShape(btScalar(radius));
}
