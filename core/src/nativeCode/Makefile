JNI_INCL="/usr/lib/jvm/java-8-oracle/include"
SRC_DIR="nativeSources"


all: libplayer.so
libplayer.so:
	gcc -I$(JNI_INCL) -fPIC -std=c11 act.c $(SRC_DIR)/*.c -shared -o $@
