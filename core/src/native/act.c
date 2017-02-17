#include<jni.h>
#include"playerNativeA.h"
#include"nativeHeaders/classSignatures.h"

static Pipe pipe;
static int turn = 0;
static Direction currentDir = DOWN;

JNIEXPORT void JNICALL
Java_com_szakacs_kpi_fei_tuke_game_player_PlayerNativeA_initializeNativeCode
    (JNIEnv *env, jobject thisPlayer, jobject world){
    initializePipe(env, &pipe, thisPlayer);
    initializeDirection(env);
}

JNIEXPORT void JNICALL
Java_com_szakacs_kpi_fei_tuke_game_player_PlayerNativeA_act
    (JNIEnv *env, jobject thisPlayer){
    printf("Hi there\n");
    if (turn < 3){
        jclass playerCls = (*env)->GetObjectClass(env, thisPlayer);
        jfieldID fid = (*env)->GetFieldID(env, playerCls, "pipe", PIPE_CLASS);
        jobject pipeObjJava = (*env)->GetObjectField(
                env, thisPlayer, fid);
        //jclass cls = (*env)->GetObjectClass(env, pipeObjJava);
        printf("movingDown\n");
        jobject pipeSegment = pipe.calculateSegment(&pipe, DOWN, pipeObjJava);
        pipe.push(&pipe, pipe.calculateSegment(&pipe, DOWN, pipeObjJava));
        turn++;
    } else {
        printf("movingLeft\n");
        //pipe.push(&pipe, pipe.calculateSegment(&pipe, LEFT, pipeObjJava));
    }
}

JNIEXPORT void JNICALL
Java_com_szakacs_kpi_fei_tuke_game_player_PlayerNativeA_deallocate
  (JNIEnv *env, jobject thisPlayer){
    deallocatePipe(env, &pipe);
    deallocateDirection(env);
}