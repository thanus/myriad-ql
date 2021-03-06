package com.matthewchapman.ql.errorhandling;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Created by matt on 13/03/2017.
 * <p>
 * Error implementation
 */
class Error implements Comparable<Error> {

    private final String message;
    private final int line;
    private final int column;
    private final String id;

    Error(int line, int column, String id, String message) {
        this.message = message;
        this.line = line;
        this.column = column;
        this.id = id;
    }

    int getLine() {
        return this.line;
    }

    int getColumn() {
        return this.column;
    }

    String getId() {
        return this.id;
    }

    String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "Error: " + this.line + ":" + this.column + " - " + this.id + " : " + this.message;
    }

    @Override
    public int compareTo(@NotNull Error o) {
        return this.line - o.line;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Error)) {
            return false;
        }

        Error input = (Error) obj;

        return Objects.equals(this.line, input.line) &&
                Objects.equals(this.column, input.column) &&
                Objects.equals(this.message, input.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.message, this.line, this.column, this.id);
    }
}
