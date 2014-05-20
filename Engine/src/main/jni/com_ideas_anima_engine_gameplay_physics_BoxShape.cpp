#include "com_ideas_anima_engine_gameplay_physics_BoxShape.h"
#include "btBulletDynamicsCommon.h"

JNIEXPORT jlong JNICALL Java_com_ideas_anima_engine_gameplay_physics_BoxShape_getBoxShapePointer
        (JNIEnv *env, jobject thisObject, jfloat x, jfloat y, jfloat z) {
    return (jlong) new btBoxShape(btVector3(x, y, z));
}