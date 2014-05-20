#include "com_ideas_anima_engine_gameplay_physics_CylinderShape.h"
#include "btBulletDynamicsCommon.h"

JNIEXPORT jlong JNICALL Java_com_ideas_anima_engine_gameplay_physics_CylinderShape_getCylinderShapePointer
        (JNIEnv *env, jobject thisObject, jfloat x, jfloat y, jfloat z) {
    return (jlong) new btCylinderShape(btVector3(x, y, z));
}
