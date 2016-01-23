#include <jni.h>

#include <btBulletDynamicsCommon.h>

extern "C" {
    JNIEXPORT jlong JNICALL
    Java_org_anima_engine_gameplay_physics_BoxShape_getBoxShapePointer(JNIEnv *env, jobject instance,
                                                                       jfloat x, jfloat y, jfloat z) {
        return (jlong) new btBoxShape(btVector3(x, y, z));
    }
}
