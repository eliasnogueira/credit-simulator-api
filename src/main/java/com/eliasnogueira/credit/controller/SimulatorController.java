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

package com.eliasnogueira.credit.controller;

import com.eliasnogueira.credit.dto.MessageDto;
import com.eliasnogueira.credit.dto.SimulatorDto;
import com.eliasnogueira.credit.dto.ValidationDto;
import com.eliasnogueira.credit.entity.Simulator;
import com.eliasnogueira.credit.exception.SimulatorException;
import com.eliasnogueira.credit.repository.SimulatorRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/simulations")
@Api(value = "/simulations", tags = "Simulator")
public class SimulatorController {

    private final SimulatorRepository repository;
    private static final String CPF_NOT_FOUND = "CPF {0} not found";

    public SimulatorController(SimulatorRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    @ApiOperation(value = "Return all recorded simulations")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Simulations found", response = SimulatorDto.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "Name not found")
    })
    List<Simulator> getSimulator(@RequestParam(name = "name", required = false) String name) {
        List<Simulator> simulationsFound;

        Example<Simulator> example =
            Example.of(Simulator.builder().name(name).build(),
                ExampleMatcher.matchingAny().
                    withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains()));

        simulationsFound = repository.findAll(example);

        if (simulationsFound.isEmpty()) {
            throw new SimulatorException("Name not found");
        }

        return simulationsFound;
    }

    @GetMapping("/{cpf}")
    @ApiOperation(value = "Return a simulation for a given CPF")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Simulations found", response = SimulatorDto.class),
        @ApiResponse(code = 404, message = "Simulations not found", response = MessageDto.class)
    })
    ResponseEntity<Simulator> one(@PathVariable String cpf) {
        return repository.findByCpf(cpf).
            map(simulator -> ResponseEntity.ok().body(simulator)).
            orElseThrow(() -> new SimulatorException(MessageFormat.format(CPF_NOT_FOUND, cpf)));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Record a new simulation", code = 201)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Simulation created successfully", response = SimulatorDto.class),
        @ApiResponse(code = 400, message = "Missing information", response = ValidationDto.class),
        @ApiResponse(code = 409, message = "CPF already exists")
    })
    Simulator newSimulator(@Valid @RequestBody SimulatorDto simulator) {
        return repository.save(new ModelMapper().map(simulator, Simulator.class));
    }

    @PutMapping("/{cpf}")
    @ApiOperation(value = "Update a simulation by a given CPF")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Simulation updated successfully", response = SimulatorDto.class),
        @ApiResponse(code = 404, message = "Missing information", response = MessageDto.class),
        @ApiResponse(code = 409, message = "CPF already exists")
    })
    Simulator updateSimulator(@RequestBody SimulatorDto simulator, @PathVariable String cpf) {
        return update(new ModelMapper().
            map(simulator, Simulator.class), cpf).
            orElseThrow(() -> new SimulatorException(MessageFormat.format(CPF_NOT_FOUND, cpf)));
    }

    @DeleteMapping("/{cpf}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a simulation by a given CPF", code = 204)
    @ApiResponses({
        @ApiResponse(code = 204, message = "Simulation deleted successfully"),
        @ApiResponse(code = 404, message = "Simulation not found", response = MessageDto.class)
    })
    void delete(@PathVariable String cpf) {
        if (repository.findByCpf(cpf).isEmpty()) {
            throw new SimulatorException(MessageFormat.format(CPF_NOT_FOUND, cpf));
        } else {
            repository.deleteByCpf(cpf);
        }
    }

    private Optional<Simulator> update(Simulator newSimulator, String cpf) {
        return repository.findByCpf(cpf).map(simulator -> {
            setIfNotNull(simulator::setId, newSimulator.getId());
            setIfNotNull(simulator::setName, newSimulator.getName());
            setIfNotNull(simulator::setCpf, newSimulator.getCpf());
            setIfNotNull(simulator::setEmail, newSimulator.getEmail());
            setIfNotNull(simulator::setInstallments, newSimulator.getInstallments());
            setIfNotNull(simulator::setAmount, newSimulator.getAmount());

            return repository.save(simulator);
        });
    }

    private <T> void setIfNotNull(final Consumer<T> setter, final T value) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
