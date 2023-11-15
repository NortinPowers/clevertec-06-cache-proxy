package by.clevertec.proxy.validator;

import by.clevertec.proxy.data.ProductDto;
import by.clevertec.proxy.exception.ValidationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDtoValidator {

    public static final String INCORRECT_DESCRIPTION_MESSAGE = "incorrect product description";
    public static final String DESCRIPTION_REG_EX = "^[а-яА-Я\\s]{10,50}$";

    public void validate(ProductDto productDto) {
        List<String> validateErrors = new ArrayList<>();
        validateNameToNull(productDto, validateErrors);
        validateNameContent(productDto, validateErrors);
        validateDescriptionToCorrect(productDto, validateErrors);
        validatePriceToNullAndCorrectValue(productDto, validateErrors);
        throwValidationExceptionInCaseError(validateErrors);
    }

    public void validateUpdated(ProductDto productDto) {
        List<String> validateErrors = new ArrayList<>();
        validateNameContent(productDto, validateErrors);
        validateUpdatedDescriptionToCorrect(productDto, validateErrors);
        validateUpdatedPriceToCorrectValue(productDto, validateErrors);
        throwValidationExceptionInCaseError(validateErrors);
    }

    private static void throwValidationExceptionInCaseError(List<String> validateErrors) {
        if (!validateErrors.isEmpty()) {
            throw new ValidationException(validateErrors);
        }
    }

    private static void validatePriceToNullAndCorrectValue(ProductDto productDto, List<String> validateErrors) {
        if (productDto.price() == null) {
            validateErrors.add("null product price");
        } else {
            validatePriceToCorrectValue(productDto, validateErrors);
        }
    }

    private static void validateUpdatedPriceToCorrectValue(ProductDto productDto, List<String> validateErrors) {
        if (productDto.price() != null) {
            validatePriceToCorrectValue(productDto, validateErrors);
        }
    }

    private static void validatePriceToCorrectValue(ProductDto productDto, List<String> validateErrors) {
        if (productDto.price().compareTo(BigDecimal.ZERO) <= 0) {
            validateErrors.add("product price less or equal than 0");
        }
    }

    private static void validateDescriptionToCorrect(ProductDto productDto, List<String> validateErrors) {
        if (productDto.description() != null && !productDto.description().matches(DESCRIPTION_REG_EX)) {
            validateErrors.add(INCORRECT_DESCRIPTION_MESSAGE);
        }
    }

    private static void validateUpdatedDescriptionToCorrect(ProductDto productDto, List<String> validateErrors) {
        if (productDto.description() != null) {
            if (!productDto.description().matches(DESCRIPTION_REG_EX)) {
                validateErrors.add(INCORRECT_DESCRIPTION_MESSAGE);
            }
        }
    }

    private static void validateNameContent(ProductDto productDto, List<String> validateErrors) {
        if (productDto.name() != null) {
            validateNameToEmpty(productDto, validateErrors);
            validateNameToCorrect(productDto, validateErrors);
        }
    }

    private static void validateNameToCorrect(ProductDto productDto, List<String> validateErrors) {
        if (!productDto.name().matches("^[а-яА-Я\\s]{5,20}$")) {
            validateErrors.add("incorrect product name");
        }
    }

    private static void validateNameToEmpty(ProductDto productDto, List<String> validateErrors) {
        if (productDto.name().trim().isEmpty()) {
            validateErrors.add("empty product name");
        }
    }

    private static void validateNameToNull(ProductDto productDto, List<String> validateErrors) {
        if (productDto.name() == null) {
            validateErrors.add("null productDto name");
        }
    }
}
