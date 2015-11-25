#include <jni.h>

#include <btBulletDynamicsCommon.h>

JNIEXPORT jlong JNICALL
Java_org_anima_engine_gameplay_physics_CompoundShape_getCompoundShapePointer(JNIEnv *env,
                                                                             jobject instance,
                                                                             jlongArray shapes,
                                                                             jfloatArray positions) {
    jlong *shapesArray = env->GetLongArrayElements(shapes, NULL);
    jsize shapesNo = env->GetArrayLength(shapes);

    jfloat *positionsArray = env->GetFloatArrayElements(positions, NULL);

    btTransform trans;

    btCompoundShape* compoundShape = new btCompoundShape();

    for (int i = 0; i < shapesNo; i++) {
        trans.setIdentity();
        trans.setOrigin(btVector3(positionsArray[i * 3],
                                  positionsArray[i * 3 + 1],
                                  positionsArray[i * 3 + 2]));
        compoundShape->addChildShape(trans, (btCompoundShape*) shapesArray[i]);
    }

    env->ReleaseLongArrayElements(shapes, shapesArray, 0);
    env->ReleaseFloatArrayElements(positions, positionsArray, 0);

    return (jlong) compoundShape;
}