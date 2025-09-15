package br.com.application.validator;


import br.com.application.exception.BusinessException;

@FunctionalInterface
public interface Validator<T> {
    void validate(T dto) throws BusinessException;
}
