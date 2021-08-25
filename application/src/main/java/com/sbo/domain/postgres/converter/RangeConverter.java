package com.sbo.domain.postgres.converter;

import com.google.common.collect.Range;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;

/**
 * @author viktar hraskou
 */
@Converter
public class RangeConverter implements AttributeConverter<Range<LocalDateTime>, String> {

    @Override
    public String convertToDatabaseColumn(Range<LocalDateTime> attribute) {
        return attribute.lowerEndpoint() + " " + attribute.upperEndpoint();
    }

    @Override
    public Range<LocalDateTime> convertToEntityAttribute(String dbData) {
        String[] dates = dbData.split(" ");

        var lower = LocalDateTime.parse(dates[0]);
        var upper = LocalDateTime.parse(dates[1]);
        return Range.closedOpen(lower, upper);
    }
}
