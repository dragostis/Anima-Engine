#include "com_ideas_anima_engine_gameplay_physics_CapsuleShape.h"
#include "btBulletDynamicsCommon.h"

JNIEXPORT jlong JNICALL Java_com_ideas_anima_engine_gameplay_physics_CapsuleShape_getCapsuleShapePointer
        (JNIEnv *env, jobject thisObject, jfloat radius, jfloat height) {
    return (jlong) new btCapsuleShape(btScalar(radius), btScalar(height));
}