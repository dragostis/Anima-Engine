#include "btBulletDynamicsCommon.h"
#include "com_ideas_anima_engine_gameplay_physics_Body.h"

JNIEXPORT jlong JNICALL Java_com_ideas_anima_engine_gameplay_physics_Body_getBodyPointer
        (JNIEnv *env, jobject thisObject, jint type, jlong shape, jfloat mass, jfloatArray values) {
   jfloat *floatArray = env->GetFloatArrayElements(values, NULL);

   if (type == 1) // Static
       mass = 0;

   btScalar massScalar = mass;
   btVector3 fallInertia(0, 0, 0);

   if (type == 2) // Dynamic
      ((btCollisionShape*) shape)->calculateLocalInertia(massScalar, fallInertia);

   btDefaultMotionState* motionState = new btDefaultMotionState(btTransform(
       btQuaternion(floatArray[3], floatArray[4], floatArray[5], floatArray[6]),
       btVector3(floatArray[0], floatArray[1], floatArray[2]))
   );
   btRigidBody::btRigidBodyConstructionInfo rigidBodyCI(massScalar, motionState, (btCollisionShape*) shape, fallInertia);
   btRigidBody* rigidBody = new btRigidBody(rigidBodyCI);

   if (type == 1) { // Kinematic
       rigidBody->setCollisionFlags(rigidBody->getCollisionFlags() | btCollisionObject::CF_KINEMATIC_OBJECT);
       rigidBody->setActivationState(DISABLE_DEACTIVATION);
   }

   return (jlong) rigidBody;
}
