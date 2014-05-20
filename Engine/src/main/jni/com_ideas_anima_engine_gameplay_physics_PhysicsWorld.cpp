#include "btBulletDynamicsCommon.h"
#include "com_ideas_anima_engine_gameplay_physics_PhysicsWorld.h"

btDiscreteDynamicsWorld* dynamicsWorld;

JNIEXPORT void JNICALL Java_com_ideas_anima_engine_gameplay_physics_PhysicsWorld_initialize
        (JNIEnv *env, jobject thisObject, jfloat x, jfloat y, jfloat z) {
    ///collision configuration contains default setup for memory, collision setup. Advanced users can create their own configuration.
	btDefaultCollisionConfiguration* collisionConfiguration = new btDefaultCollisionConfiguration();

	///use the default collision dispatcher. For parallel processing you can use a diffent dispatcher (see Extras/BulletMultiThreaded)
	btCollisionDispatcher* dispatcher = new	btCollisionDispatcher(collisionConfiguration);

	///btDbvtBroadphase is a good general purpose broadphase. You can also try out btAxis3Sweep.
	btBroadphaseInterface* overlappingPairCache = new btDbvtBroadphase();

	///the default constraint solver. For parallel processing you can use a different solver (see Extras/BulletMultiThreaded)
	btSequentialImpulseConstraintSolver* solver = new btSequentialImpulseConstraintSolver;

    dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);

	dynamicsWorld->setGravity(btVector3(x, y, z));
}

JNIEXPORT void JNICALL Java_com_ideas_anima_engine_gameplay_physics_PhysicsWorld_loadObject
        (JNIEnv *env, jobject thisObject, jlong body) {
    dynamicsWorld->addRigidBody((btRigidBody*) body);
}

JNIEXPORT void JNICALL Java_com_ideas_anima_engine_gameplay_physics_PhysicsWorld_removeObject
        (JNIEnv *env, jobject thisObject, jlong body) {
    dynamicsWorld->removeRigidBody((btRigidBody*) body);
}

JNIEXPORT void JNICALL Java_com_ideas_anima_engine_gameplay_physics_PhysicsWorld_updateWorld
        (JNIEnv *env, jobject thisObject, jfloat deltaTime) {
    dynamicsWorld->stepSimulation(deltaTime);
}

JNIEXPORT jfloatArray JNICALL Java_com_ideas_anima_engine_gameplay_physics_PhysicsWorld_updateObject
        (JNIEnv *env, jobject thisObject, jlong body) {
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