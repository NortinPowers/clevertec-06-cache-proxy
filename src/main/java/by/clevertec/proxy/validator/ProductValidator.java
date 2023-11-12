package by.clevertec.proxy.validator;

import by.clevertec.proxy.entity.Product;
import by.clevertec.proxy.exception.ValidationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductValidator {

    public void validate(Product product) {
        List<String> validateErrors = new ArrayList<>();
        validateNameToNull(product, validateErrors);
        validateNameContent(product, validateErrors);
        validateDescriptionToCorrect(product, validateErrors);
        validatePriceToNullAndCorrectValue(product, validateErrors);
        validateCreatedToNull(product, validateErrors);
        throwValidationExceptionInCaseError(validateErrors);
    }

    private static void throwValidationExceptionInCaseError(List<String> validateErrors) {
        if (!validateErrors.isEmpty()) {
            throw new ValidationException(validateErrors);
        }
    }

    private static void validateCreatedToNull(Product product, List<String> validateErrors) {
        if (product.getCreated() == null) {
            validateErrors.add("null product created time");
        }
    }

    private static void validatePriceToNullAndCorrectValue(Product product, List<String> validateErrors) {
        if (product.getPrice() == null) {
            validateErrors.add("null product price");
        } else {
            if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                validateErrors.add("product price less or equal than 0");
            }
        }
    }

    private static void validateDescriptionToCorrect(Product product, List<String> validateErrors) {
        if (product.getDescription() != null && !product.getDescription().matches("^[а-яА-Я\\s]{10,30}$")) {
            validateErrors.add("incorrect product description");
        }
    }

    private static void validateNameContent(Product product, List<String> validateErrors) {
        if (product.getName() != null) {
            validateNameToEmpty(product, validateErrors);
            validateNameToCorrect(product, validateErrors);
        }
    }

    private static void validateNameToCorrect(Product product, List<String> validateErrors) {
        if (!product.getName().matches("^[а-яА-Я\\s]{5,10}$")) {
            validateErrors.add("incorrect product name");
        }
    }

    private static void validateNameToEmpty(Product product, List<String> validateErrors) {
        if (product.getName().trim().isEmpty()) {
            validateErrors.add("empty product name");
        }
    }

    private static void validateNameToNull(Product product, List<String> validateErrors) {
        if (product.getName() == null) {
            validateErrors.add("null product name");
        }
    }
}
