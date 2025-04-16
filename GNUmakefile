SOURCE_DIR := src/main/java
BUILD_DIR := target

SOURCES := $(shell find $(SOURCE_DIR) -name "*.java")

all: $(BUILD_DIR)/.classes

$(BUILD_DIR)/.classes: $(SOURCES)
	@mkdir -p $(BUILD_DIR)
	javac --enable-preview --source 23 -d $(BUILD_DIR) $(SOURCES)
#	@touch $(BUILD_DIR)/.class

clean:
	rm -rf $(BUILD_DIR)

.PHONY: all clean
