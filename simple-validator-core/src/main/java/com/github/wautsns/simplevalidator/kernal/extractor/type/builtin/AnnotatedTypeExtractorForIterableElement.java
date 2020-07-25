package com.github.wautsns.simplevalidator.kernal.extractor.type.builtin;

import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapper;
import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapperForIterableElement;
import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapperForMapKey;
import com.github.wautsns.simplevalidator.kernal.extractor.type.basic.AnnotatedTypeExtractor;
import com.github.wautsns.simplevalidator.kernal.extractor.type.basic.AnnotatedTypeExtractorForAnnotatedParameterizedType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Annotated type extractor for iterable element.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotatedTypeExtractorForIterableElement extends AnnotatedTypeExtractorForAnnotatedParameterizedType {

    public static final AnnotatedTypeExtractor INSTANCE = new AnnotatedTypeExtractorForIterableElement();

    @Override
    public String getNameOfExtractedType() {
        return "@ITERABLE_ELEMENT";
    }

    @Override
    protected TypeParameterMetadata initTypeParameterMetadata() {
        return new TypeParameterMetadata() {
            @Override
            public Class<?> getParameterizedTypeClass() {
                return Iterable.class;
            }

            @Override
            public int getIndex() {
                return 0;
            }
        };
    }

    @Override
    public CriterionWrapper getCriterionWrapper() {
        return CriterionWrapperForIterableElement.INSTANCE;
    }

}
