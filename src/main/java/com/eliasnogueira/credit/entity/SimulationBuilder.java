/*
 * MIT License
 *
 * Copyright (c) today.year Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.eliasnogueira.credit.entity;

import java.math.BigDecimal;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SimulationBuilder {

    private @NotNull(message = "Name cannot be empty") String name;
    private @NotNull(message = "CPF cannot be empty") String cpf;
    private @NotNull(message = "E-mail cannot be empty") @Email @Pattern(regexp = ".+@.+\\.[a-z]+", message = "E-mail must be valid") String email;
    private @NotNull(message = "Amount cannot be empty") @Min(value = 1000, message = "Amount must be equal or greater than $ 1.000") @Max(value = 40000, message = "Amount must be equal or less than than $ 40.000") BigDecimal amount;
    private @NotNull(message = "Installments cannot be empty") @Min(value = 2, message = "Installments must be equal or greater than 2") @Max(value = 48, message = "Installments must be equal or less than 48") Integer installments;
    private @NotNull(message = "One of the insurance options must be selected") Boolean insurance;

    public SimulationBuilder name(@NotNull(message = "Name cannot be empty") String name) {
        this.name = name;
        return this;
    }

    public SimulationBuilder cpf(@NotNull(message = "CPF cannot be empty") String cpf) {
        this.cpf = cpf;
        return this;
    }

    public SimulationBuilder email(
        @NotNull(message = "E-mail cannot be empty") @Email @Pattern(regexp = ".+@.+\\.[a-z]+", message = "E-mail must be valid") String email) {
        this.email = email;
        return this;
    }

    public SimulationBuilder amount(
        @NotNull(message = "Amount cannot be empty") @Min(value = 1000, message = "Amount must be equal or greater than $ 1.000") @Max(value = 40000, message = "Amount must be equal or less than than $ 40.000") BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public SimulationBuilder installments(
        @NotNull(message = "Installments cannot be empty") @Min(value = 2, message = "Installments must be equal or greater than 2") @Max(value = 48, message = "Installments must be equal or less than 48") Integer installments) {
        this.installments = installments;
        return this;
    }

    public SimulationBuilder insurance(
        @NotNull(message = "One of the insurance options must be selected") Boolean insurance) {
        this.insurance = insurance;
        return this;
    }

    public Simulation build() {
        return new Simulation(name, cpf, email, amount, installments, insurance);
    }
}