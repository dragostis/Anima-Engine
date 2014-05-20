/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_ideas_anima_engine_gameplay_physics_PhysicsWorld */

#ifndef _Included_com_ideas_anima_engine_gameplay_physics_PhysicsWorld
#define _Included_com_ideas_anima_engine_gameplay_physics_PhysicsWorld
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_ideas_anima_engine_gameplay_physics_PhysicsWorld
 * Method:    initialize
 * Signature: (FFF)V
 */
JNIEXPORT void JNICALL Java_com_ideas_anima_engine_gameplay_physics_PhysicsWorld_initialize
  (JNIEnv *, jobject, jfloat, jfloat, jfloat);

/*
 * Class:     com_ideas_anima_engine_gameplay_physics_PhysicsWorld
 * Method:    loadObject
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ideas_anima_engine_gameplay_physics_PhysicsWorld_loadObject
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_ideas_anima_engine_gameplay_physics_PhysicsWorld
 * Method:    removeObject
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ideas_anima_engine_gameplay_physics_PhysicsWorld_removeObject
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_ideas_anima_engine_gameplay_physics_PhysicsWorld
 * Methos:    updateWorld
 * Signature: (FJ)V
 */
JNIEXPORT void JNICALL Java_com_ideas_anima_engine_gameplay_physics_PhysicsWorld_updateWorld
  (JNIEnv *, jobject, jfloat);

 /*
 * Class:     com_ideas_anima_engine_gameplay_physics_PhysicsWorld
 * Methos:    updateObject
 * Signature: (FJ)[F
 */
JNIEXPORT jfloatArray JNICALL Java_com_ideas_anima_engine_gameplay_physics_PhysicsWorld_updateObject
  (JNIEnv *, jobject, jlong);


#ifdef __cplusplus
}
#endif
#endif
