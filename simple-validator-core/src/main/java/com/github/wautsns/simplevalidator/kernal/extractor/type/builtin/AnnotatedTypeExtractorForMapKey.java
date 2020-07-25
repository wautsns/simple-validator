package com.github.wautsns.simplevalidator.kernal.extractor.type.builtin;

import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapper;
import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapperForMapKey;
import com.github.wautsns.simplevalidator.kernal.extractor.type.basic.AnnotatedTypeExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.type.basic.AnnotatedTypeExtractorForAnnotatedParameterizedType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Annotated type extractor for map key.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotatedTypeExtractorForMapKey extends AnnotatedTypeExtractorForAnnotatedParameterizedType {

    public static final AnnotatedTypeExtractor INSTANCE = new AnnotatedTypeExtractorForMapKey();

    @Override
    public String getNameOfExtractedType() {
        return "@MAP_KEY";
    }

    @Override
    protected TypeParameterMetadata initTypeParameterMetadata() {
        return new TypeParameterMetadata() {
            @Override
            public Class<?> getParameterizedTypeClass() {
                return Map.class;
            }

            @Override
            public int getIndex() {
                return 0;
            }
        };
    }

    @Override
    public CriterionWrapper getCriterionWrapper() {
        return CriterionWrapperForMapKey.INSTANCE;
    }

}
