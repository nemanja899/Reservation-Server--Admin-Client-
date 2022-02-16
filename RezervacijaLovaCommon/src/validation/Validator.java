/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validation;

import static com.sun.source.util.DocTrees.instance;
import static com.sun.source.util.DocTrees.instance;
import static com.sun.source.util.JavacTask.instance;
import static com.sun.source.util.Trees.instance;
import domain.InvoiceItem;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author User
 */
public class Validator {

    private List<String> validationErrors;

    private Validator() {
        this.validationErrors = new ArrayList();
    }

    public static Validator startValidate() {
        return new Validator();
    }

    public Validator validateValueIsInteger(String value, String errorMessage) throws ValidationException {
        try {
            if (value != null) {
                Integer.parseInt(value);
            } else {
                this.validationErrors.add(errorMessage);
            }
        } catch (NumberFormatException nfe) {
            this.validationErrors.add(errorMessage);
            return this;
        }
        return this;
    }

    public Validator validateBigDecimal(BigDecimal value, String error) throws ValidationException {

        try {

            if (value.compareTo(new BigDecimal(0)) <= 0) {
                this.validationErrors.add(error);
            }
            return this;
        } catch (Exception e) {
            this.validationErrors.add(error);
            return this;
        }

    }

    public Validator validateStringToBigDecimal(String value, String error) throws ValidationException {
        try {
            BigDecimal bigDecimal = new BigDecimal(value);
            if (bigDecimal.compareTo(BigDecimal.ZERO) == -1) {
                this.validationErrors.add(error);
            }
        } catch (NumberFormatException e) {
            this.validationErrors.add(error);
            return this;
        }
        return this;
    }

    public Validator emptyList(List<?> value, String error) throws ValidationException {
        if (value == null || value.isEmpty()) {
            this.validationErrors.add(error);
        }
        return this;
    }

    public Validator validateInputPattern(String value, String patern, String errorMessage) throws ValidationException {

        if (!Pattern.matches(patern, value)) {
            this.validationErrors.add(errorMessage);

        }
        return this;
    }

    public Validator validateIsSelected(boolean selected, String error) throws ValidationException {
        if (!selected) {
            this.validationErrors.add(error);
        }
        return this;
    }

    public Validator validateEmptyString(String value, String errorMessage) throws ValidationException {
        if (value.isEmpty()) {
            this.validationErrors.add(errorMessage);
        }
        return this;
    }

    public Validator validateNullObject(Object value, String obj) throws ValidationException {
        if (value == null) {
            this.validationErrors.add(obj + " can't be null");
        }
        return this;
    }

    public Validator validateDatePattern(String value) throws ValidationException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            sdf.parse(value);
        } catch (ParseException ex) {
            this.validationErrors.add("Datum nije u formatu dd-MM-yyyy");
        }
        return this;

    }

    public Validator validateEmptyList(List<Object> objs, String error) throws ValidationException {
        if (objs.isEmpty()) {
            this.validationErrors.add(error);
        }
        return this;
    }

    public Validator validateInvoiceItems(List<InvoiceItem> items, BigDecimal sum) throws ValidationException {
    
        BigDecimal sums = BigDecimal.ZERO;
        for (InvoiceItem it : items) {
           sums= sums.add(it.getTotalCost());
            if (it.getAnimalNo() <= 0) {
                this.validationErrors.add("Broj ulovljene zivotinje ne moze biti nula");
            }
            if (it.getPrices().getAnimal() == null) {
                this.validationErrors.add("Ulovljena zivotinja ne moze biti prazna");
            }
           

        }
        System.out.println(sums+"="+sum);
        if (sums.compareTo(sum) != 0) {
            this.validationErrors.add("Ukupna cena Racuna se ne poklapa sa cenom stavkama");
        }
        return this;
    }

    public Validator validateSeasonString(String value) throws ValidationException {
        if (!Pattern.matches("^[0-9]{4}/[0-9]{4}$", value)) {
            this.validationErrors.add("Sezona mora biti u formatu xxxx/xxxx i popunjeni brojevima i moraju biti konsegutivni");
            return this;
        }

        return validateConsegutiveNumbers(value);

    }

    public Validator validateStringMinLength(String value, int length, String errorMessage) throws ValidationException {
        if (value.length() < length) {
            this.validationErrors.add(errorMessage);
        }
        return this;
    }

    public Validator validateConsegutiveNumbers(String value) throws ValidationException {

        try {
            int year1 = Integer.parseInt(value.split("/")[0]);
            int year2 = Integer.parseInt(value.split("/")[1]);
            if (year1 + 1 != year2) {
                this.validationErrors.add("Brojevi u sezoni nisu konsegutivni");
            }
        } catch (NumberFormatException e) {
        }
        return this;
    }

    public Validator objectInstance(Object object, Class<?> comp, String error) {
        try {
            comp.cast(object);
        } catch (ClassCastException e) {
            this.validationErrors.add(error);
        }

        return this;
    }

    public void throwIfInvalide() throws ValidationException {
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(this.validationErrors.stream().collect(Collectors.joining("\n")));
        }
    }
}
