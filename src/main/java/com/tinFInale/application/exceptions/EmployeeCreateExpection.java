package com.tinFInale.application.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCreateExpection extends Exception {
    private String message;

    public EmployeeCreateExpection(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "getting errors: " + this.message;
    }
}
