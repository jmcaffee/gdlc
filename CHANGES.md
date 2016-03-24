# GDLC Change Log

## 3.1.3

- Do not rewrite True/False constants; TRUE/true and FALSE/false will not be rewritten to True/False.

## 3.1.2

- Emit 'CRP' instead of 'CRD' for credit type PPMs

## 3.1.1

- [8] Add missing PLK import name to error messages
- Add rule name to missing variable definition error messages
- [7] Move error total count display to end of errors list

## 3.1.0

- [4]Add support for optional argument/parameters in XML Function declarations.
- Include name of parent rule when emitting errors for XML Function arguments.
- [5] Handle NPE when empty PLK file is parsed
- [5] Emit CompilerException with message indicating the file path of the problem file


