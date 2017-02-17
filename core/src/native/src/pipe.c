#include<stdio.h>
#include<unistd.h>
#include"../nativeHeaders/classSignatures.h"

static JNIEnv *JavaEnv;

static void push(Pipe *self, jobject pipeSegment);
static jobject pop(Pipe *self);
static jobject calculateSegment(Pipe *self, Direction dir, jobject Obj);

void initializePipe(JNIEnv *env, Pipe *self, jobject thisPlayer){
    JavaEnv = env;
    jclass playerCls = (*env)->GetObjectClass(env, thisPlayer);
    jfieldID fid = (*env)->GetFieldID(env, playerCls, "pipe", PIPE_CLASS);
    jobject pipeObjJava = (*env)->GetObjectField(
            env, thisPlayer, fid);
    jclass cls = (*env)->GetObjectClass(env, pipeObjJava);
    printf("pipeObjJava: %p\n", pipeObjJava);
    self->pipeClass = (*env)->NewGlobalRef(env, cls);
    self->pipeObject = (*env)->NewGlobalRef(env, pipeObjJava);
    printf("pipeObject assigned: %p\n", self->pipeObject);
    self->pushID = (*env)->GetMethodID(env, cls, PIPE_PUSH_NAME, PIPE_PUSH);
    self->push = push;
    self->popID = (*env)->GetMethodID(env, cls, PIPE_POP_NAME, PIPE_POP);
    self->pop = pop;
    self->calcSegmentID = (*env)->GetMethodID(env, cls, PIPE_CALC_NAME, PIPE_CALC);
    self->calculateSegment = calculateSegment;
}

void deallocatePipe(JNIEnv *env, Pipe *self){
    (*env)->DeleteGlobalRef(env, self->pipeObject);
    (*env)->DeleteGlobalRef(env, self->pipeClass);
}

static void push(Pipe *self, jobject pipeSegment){
    //printf("methodID for push: %d\n", self->pushID);
    printf("returned jobject in push: %p\n", pipeSegment);
    (*JavaEnv)->CallVoidMethod(JavaEnv, self->pipeObject, self->pushID, pipeSegment);
}

static jobject pop(Pipe *self){
    //printf("methodID for pop: %d\n", self->popID);
    return (*JavaEnv)->CallObjectMethod(JavaEnv, self->pipeObject, self->popID);
}

static jobject calculateSegment(Pipe *self, Direction dir, jobject Obj){
    //printf("methodID for calculateSegment: %d\n", self->calcSegmentID);
    //printf("direction: %p\n", getDirectionJava(dir));
    printf("self->pipeObject: %p\n", self->pipeObject);
    printf("pipeObject: %p\n", Obj);
    jobject obj = (*JavaEnv)->CallObjectMethod(JavaEnv, self->pipeObject, self->calcSegmentID, getDirectionJava(dir));
    printf("returned jobject: %p\n", obj);
    return obj;
}
