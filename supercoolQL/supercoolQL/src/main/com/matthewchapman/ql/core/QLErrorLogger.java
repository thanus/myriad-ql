package com.matthewchapman.ql.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by matt on 13/03/2017.
 */
public class QLErrorLogger {

    private final List<QLError> errors;

    public QLErrorLogger() {
        errors = new ArrayList<>();
    }

    public void addError(int line, int column, String id, String message) {
        errors.add(new QLError(line, column, id, message));
    }

    public void addMultipleErrors(QLErrorLogger logger) {
        this.errors.addAll(logger.getErrors());
    }

    public int getErrorNumber() {
        return this.errors.size();
    }

    private List<QLError> getErrors() {
        return this.errors;
    }

    @Override
    public String toString() {
        String result = "";
        Collections.sort(this.errors);

        for (QLError error : this.errors) {
            result = result.concat(error.toString() + "\n");
        }

        return result;
    }

}
