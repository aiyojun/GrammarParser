TARGET  =expr
LEX     =flex
YACC    =bison
CXX     =gcc

all: build_dir lex yacc
	@echo "[3/3] Compiling lex.yy.c $(TARGET).tab.c"
	@$(CXX) -o $(TARGET) build/$(TARGET).yacc.c build/$(TARGET).lex.c

build_dir:
	@if [ ! -d build ]; then mkdir build; fi

lex:
	@echo "[1/3] Compiling lex file"
	@$(LEX) -o build/$(TARGET).lex.c src/$(TARGET).l

yacc:
	@echo "[2/3] Compiling yacc file"
	@$(YACC) -d -o build/$(TARGET).yacc.c src/$(TARGET).y

clean:
	@rm -rf build $(TARGET)

.PHONY: clean