#ifndef PIPE_H
#define PIPE_H

// Note: do not include this file directly in your sources,
// Instead, just use #include"classSignatures.h"

    #include"direction.h"

    #define PIPE_CLASS "Lcom/szakacs/kpi/fei/tuke/game/arena/pipe/Pipe;"
    #define PIPE_SEGMENT_CLASS "Lcom/szakacs/kpi/fei/tuke/game/arena/pipe/PipeSegment;"
    #define PIPE_HEAD_CLASS "Lcom/szakacs/kpi/fei/tuke/game/arena/pipe/PipeHead;"

    //method signatures (derived by macro concatenation)

    #define PIPE_PUSH         "(" PIPE_SEGMENT_CLASS ")V"
    #define PIPE_PUSH_NAME    "push"
    #define PIPE_POP          "()" PIPE_SEGMENT_CLASS
    #define PIPE_POP_NAME     "pop"
    #define PIPE_CALC         "(" DIRECTION_CLASS ")" PIPE_SEGMENT_CLASS
    #define PIPE_CALC_NAME    "calculateNextSegment"

    struct pipe {
        jobject pipeObject;
        jclass pipeClass;

        // JNI method IDs

        jmethodID pushID;
        jmethodID popID;
        jmethodID calcSegmentID;

        // function pointers

        void (*push)(struct pipe *self, jobject pipeSegment);
        jobject (*pop)(struct pipe *self);
        jobject (*calculateSegment)(struct pipe *self, Direction dir, jobject Obj);
    };

    typedef struct pipe Pipe;

    void initializePipe(JNIEnv *env, Pipe *self, jobject thisPlayer);
    void deallocatePipe(JNIEnv *env, Pipe *self);
#endif