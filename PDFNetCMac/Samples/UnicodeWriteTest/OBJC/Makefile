TESTNAME=UnicodeWriteTest
CC=clang
TARGET=$(TESTNAME)
INCLUDES=-I../../../Headers
CFLAGS=-fobjc-exceptions -fobjc-arc
LIBS=-L../../../Lib -lPDFNetC -framework Foundation
LDFLAGS=-Wl,-rpath,../../../Lib

$(TARGET): $(TARGET).m.o
	@echo "Linking target: $(TARGET)"
	$(CC) -o $@ $< $(LIBS) $(LDFLAGS)

$(TARGET).m.o: $(TARGET).m
	@echo "Compiling target: $(TARGET)"
	$(CC) -c $< -o $@ $(CFLAGS) $(INCLUDES)

all: $(TARGET)

clean:
	rm -fr $(TARGET) *.o

rebuild: clean all

.PHONY: all clean rebuild
