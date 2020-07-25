package com.github.wautsns.simplevalidator.kernal.extractor.type.builtin;

import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapper;
import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapperForOptionalValue;
import com.github.wautsns.simplevalidator.kernal.extractor.type.basic.AnnotatedTypeExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.type.basic.AnnotatedTypeExtractorForAnnotatedParameterizedType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Annotated type extractor for optional value.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotatedTypeExtractorForOptionalValue extends AnnotatedTypeExtractorForAnnotatedParameterizedType {

    public static final AnnotatedTypeExtractor INSTANCE = new AnnotatedTypeExtractorForOptionalValue();

    @Override
    public String getNameOfExtractedType() {
        return "";
    }

    @Override
    protected TypeParameterMetadata initTypeParameterMetadata() {
        return new TypeParameterMetadata() {
            @Override
            public Class<?> getParameterizedTypeClass() {
                return Optional.class;
            }

            @Override
            public int getIndex() {
                return 0;
            }
        };
    }

    @Override
    public CriterionWrapper getCriterionWrapper() {
        return CriterionWrapperForOptionalValue.INSTANCE;
    }

}
