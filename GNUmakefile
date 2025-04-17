# GNUmakefile for ALGOTRAIN project
# author: Bilal Vandenberge

SOURCE_DIR := src/main/java
BUILD_DIR := target
JAVADOC_DIR := doc/javadoc

SOURCES := $(shell find $(SOURCE_DIR) -name "*.java")

all: $(BUILD_DIR)/.classes doc

$(BUILD_DIR)/.classes: $(SOURCES)
	@mkdir -p $(BUILD_DIR)
	javac --enable-preview --source 23 -d $(BUILD_DIR) $(SOURCES)
	@touch $(BUILD_DIR)/.class

doc:
	@mkdir -p $(JAVADOC_DIR)
	javadoc -d doc/javadoc $(SOURCES)

clean:
	rm -rf $(BUILD_DIR)

.PHONY: all clean doc
