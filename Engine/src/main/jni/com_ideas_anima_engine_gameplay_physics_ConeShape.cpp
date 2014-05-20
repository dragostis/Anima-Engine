#include "com_ideas_anima_engine_gameplay_physics_ConeShape.h"
#include "btBulletDynamicsCommon.h"

JNIEXPORT jlong JNICALL Java_com_ideas_anima_engine_gameplay_physics_ConeShape_getConeShapePointer
        (JNIEnv *env, jobject thisObject, jfloat radius, jfloat height) {
    return (jlong) new btConeShape(btScalar(radius), btScalar(height));
}