#include<stdlib.h>
#include"../headers/classSignatures.h"

static jDirection jLeft;
static jDirection jRight;
static jDirection jUp;
static jDirection jDown;

void initializeDirection(JNIEnv *env){
    jclass cls = (*env)->FindClass(env, DIRECTION_CLASS);
    jLeft.direction = LEFT;
    jLeft.directionJava = (*env)->NewGlobalRef(env,
        (*env)->GetStaticObjectField(env, cls,
            (*env)->GetStaticFieldID(env, cls, "LEFT", DIRECTION_CLASS)
        )
    );
    printf("directionLeft: %p\n", jLeft.directionJava);
    jRight.direction = RIGHT;
    jRight.directionJava = (*env)->NewGlobalRef(env,
        (*env)->GetStaticObjectField(env, cls,
            (*env)->GetStaticFieldID(env, cls, "RIGHT", DIRECTION_CLASS)
        )
    );
    jUp.direction = UP;
    jUp.directionJava = (*env)->NewGlobalRef(env,
        (*env)->GetStaticObjectField(env, cls,
            (*env)->GetStaticFieldID(env, cls, "UP", DIRECTION_CLASS)
        )
    );
    jDown.direction = DOWN;
    jDown.directionJava = (*env)->NewGlobalRef(env,
        (*env)->GetStaticObjectField(env, cls,
            (*env)->GetStaticFieldID(env, cls, "DOWN", DIRECTION_CLASS)
        )
    );
    printf("directionDown: %p\n", jDown.directionJava);
}

void deallocateDirection(JNIEnv *env){
    (*env)->DeleteGlobalRef(env, jLeft.directionJava);
    (*env)->DeleteGlobalRef(env, jRight.directionJava);
    (*env)->DeleteGlobalRef(env, jUp.directionJava);
    (*env)->DeleteGlobalRef(env, jDown.directionJava);
}

jobject getDirectionJava(Direction direction){
    switch (direction){
        case LEFT:
            return jLeft.directionJava;
        case RIGHT:
            return jRight.directionJava;
        case UP:
            return jUp.directionJava;
        case DOWN:
            return jDown.directionJava;
        default:
            return NULL;
    }
}
