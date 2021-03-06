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
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "cpf_unique", columnNames = "cpf")
})
public class Simulation {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "CPF cannot be empty")
    private String cpf;

    @NotNull(message = "E-mail cannot be empty")
    @Email
    @Pattern(regexp = ".+@.+\\.[a-z]+", message = "E-mail must be valid")
    private String email;

    @NotNull(message = "Amount cannot be empty")
    @Min(value = 1000, message = "Amount must be equal or greater than $ 1.000")
    @Max(value = 40000, message = "Amount must be equal or less than than $ 40.000")
    private BigDecimal amount;

    @NotNull(message = "Installments cannot be empty")
    @Min(value = 2, message = "Installments must be equal or greater than 2")
    @Max(value = 48, message = "Installments must be equal or less than 48")
    private Integer installments;

    @NotNull(message = "One of the insurance options must be selected")
    private Boolean insurance;

    public Simulation() {}

    public Simulation(Long id, @NotNull(message = "Name cannot be empty") String name,
        @NotNull(message = "CPF cannot be empty") String cpf,
        @NotNull(message = "E-mail cannot be empty") @Email @Pattern(regexp = ".+@.+\\.[a-z]+", message = "E-mail must be valid") String email,
        @NotNull(message = "Amount cannot be empty") @Min(value = 1000, message = "Amount must be equal or greater than $ 1.000") @Max(value = 40000, message = "Amount must be equal or less than than $ 40.000") BigDecimal amount,
        @NotNull(message = "Installments cannot be empty") @Min(value = 2, message = "Installments must be equal or greater than 2") @Max(value = 48, message = "Installments must be equal or less than 48") Integer installments,
        @NotNull(message = "One of the insurance options must be selected") Boolean insurance) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.amount = amount;
        this.installments = installments;
        this.insurance = insurance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    @Override
    public String toString() {
        return "Simulation{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", cpf='" + cpf + '\'' +
            ", email='" + email + '\'' +
            ", amount=" + amount +
            ", installments=" + installments +
            ", insurance=" + insurance +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Simulation that = (Simulation) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(email, that.email) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(installments, that.installments) &&
            Objects.equals(insurance, that.insurance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cpf, email, amount, installments, insurance);
    }
}
