#include "com_ideas_anima_engine_gameplay_physics_CompoundShape.h"
#include "btBulletDynamicsCommon.h"

JNIEXPORT jlong JNICALL Java_com_ideas_anima_engine_gameplay_physics_CompoundShape_getCompoundShapePointer
        (JNIEnv *env, jobject thisObject, jlongArray shapes, jfloatArray positions) {
    jlong *shapesArray = env->GetLongArrayElements(shapes, NULL);
    jsize shapesNo = env->GetArrayLength(shapes);

    jfloat *positionsArray = env->GetFloatArrayElements(positions, NULL);
    jsize positionsNo = env->GetArrayLength(positions);

    btVector3 localInertia(0, 0, 0);
    btTransform trans;

    btCompoundShape* compoundShape = new btCompoundShape();

    for(int i = 0; i < shapesNo; i++) {
        trans.setIdentity();
        trans.setOrigin(btVector3(positionsArray[i * 3],
                                  positionsArray[i * 3 + 1],
                                  positionsArray[i * 3 + 2]));
        compoundShape->addChildShape(trans, (btCompoundShape*) shapesArray[i]);
    }

    return (jlong) compoundShape;
}

