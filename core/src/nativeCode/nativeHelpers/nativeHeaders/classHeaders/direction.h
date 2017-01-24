#ifndef DIRECTION_H
#define DIRECTION_H

// Note: do not include this file directly in your sources,
// Instead, just use #include"classSignatures.h"

    #define DIRECTION_CLASS "Lcom/szakacs/kpi/fei/tuke/game/enums/Direction;"

    enum direction {
        LEFT, RIGHT, UP, DOWN
    };

    typedef enum direction Direction;

    struct jdirection {
        Direction direction;
        jobject directionJava;
    };

    typedef struct jdirection jDirection;

    void initializeDirection(JNIEnv *env);
    jobject getDirectionJava(Direction dir);

#endif