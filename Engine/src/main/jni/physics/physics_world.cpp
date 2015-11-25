#include <jni.h>

#include <btBulletDynamicsCommon.h>

btDiscreteDynamicsWorld* dynamicsWorld;

JNIEXPORT void JNICALL
Java_org_anima_engine_gameplay_physics_PhysicsWorld_initialize(JNIEnv *env, jobject instance,
        jfloat x, jfloat y, jfloat z) {
    btDefaultCollisionConfiguration* collisionConfiguration = new btDefaultCollisionConfiguration();
    btCollisionDispatcher* dispatcher = new	btCollisionDispatcher(collisionConfiguration);

    btBroadphaseInterface* overlappingPairCache = new btDbvtBroadphase();
    btSequentialImpulseConstraintSolver* solver = new btSequentialImpulseConstraintSolver;

    dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);
    dynamicsWorld->setGravity(btVector3(x, y, z));
}

JNIEXPORT void JNICALL
Java_org_anima_engine_gameplay_physics_PhysicsWorld_loadObject(JNIEnv *env, jobject instance,
                                                               jlong body) {
    dynamicsWorld->addRigidBody((btRigidBody*) body);
}

JNIEXPORT void JNICALL
Java_org_anima_engine_gameplay_physics_PhysicsWorld_removeObject__J(JNIEnv *env, jobject instance,
                                                                    jlong body) {
    dynamicsWorld->removeRigidBody((btRigidBody*) body);
}

JNIEXPORT void JNICALL
Java_org_anima_engine_gameplay_physics_PhysicsWorld_updateWorld(JNIEnv *env, jobject instance,
                                                                jfloat deltaTime) {
    dynamicsWorld->stepSimulation(deltaTime);
}

JNIEXPORT jfloatArray JNICALL
Java_org_anima_engine_gameplay_physics_PhysicsWorld_updateObject(JNIEnv *env, jobject instance,
                                                                 jlong body) {
    jfloatArray result;
    result = env->NewFloatArray(7);

    jfloat temp[7];
    btTransform trans;
    ((btRigidBody*) body)->getMotionState()->getWorldTransform(trans);

    temp[0] = trans.getOrigin().getX();
    temp[1] = trans.getOrigin().getY();
    temp[2] = trans.getOrigin().getZ();
    temp[3] = trans.getRotation().getX();
    temp[4] = trans.getRotation().getY();
    temp[5] = trans.getRotation().getZ();
    temp[6] = trans.getRotation().getW();

    env->SetFloatArrayRegion(result, 0, 7, temp);

    return result;
}