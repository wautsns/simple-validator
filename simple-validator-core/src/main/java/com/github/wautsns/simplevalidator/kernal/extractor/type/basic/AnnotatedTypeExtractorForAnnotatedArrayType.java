package com.github.wautsns.simplevalidator.kernal.extractor.type.basic;

import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapper;
import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapperForArrayComponent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedType;

/**
 * Annotated type extractor for {@code AnnotatedArrayType}.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotatedTypeExtractorForAnnotatedArrayType extends AnnotatedTypeExtractor {

    public static final AnnotatedTypeExtractor INSTANCE = new AnnotatedTypeExtractorForAnnotatedArrayType();

    @Override
    public String getNameOfExtractedType() {
        return "@ARRAY_COMPONENT";
    }

    @Override
    public final AnnotatedType extract(AnnotatedType source) {
        if (!(source instanceof AnnotatedArrayType)) { return null; }
        return ((AnnotatedArrayType) source).getAnnotatedGenericComponentType();
    }

    @Override
    public CriterionWrapper getCriterionWrapper() {
        return CriterionWrapperForArrayComponent.INSTANCE;
    }

}
