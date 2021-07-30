TARGET  =expr
LEX     =flex
YACC    =bison
CXX     =gcc

all: lex yacc
	@echo "[3/3] Compiling lex.yy.c $(TARGET).tab.c"
	@$(CXX) -o $(TARGET) lex.yy.c $(TARGET).tab.c

lex:
	@echo "[1/3] Compiling lex file"
	@$(LEX) *.l

yacc:
	@echo "[2/3] Compiling yacc file"
	@$(YACC) -d *.y

clean:
	@rm -rf lex.yy.c $(TARGET).tab.c $(TARGET).tab.h $(TARGET)

.PHONY: clean