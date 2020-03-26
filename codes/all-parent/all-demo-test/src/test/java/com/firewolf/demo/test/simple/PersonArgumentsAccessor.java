package com.firewolf.demo.test.simple;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

import java.time.LocalDate;

public class PersonArgumentsAccessor implements ArgumentsAggregator {
        @Override
        public Person aggregateArguments(ArgumentsAccessor arguments, ParameterContext parameterContext) throws ArgumentsAggregationException {
            Person person = new Person(arguments.getString(0),
                    arguments.getString(1),
                    arguments.get(2, String.class),
                    arguments.get(3, LocalDate.class));

            return person;
        }
    }