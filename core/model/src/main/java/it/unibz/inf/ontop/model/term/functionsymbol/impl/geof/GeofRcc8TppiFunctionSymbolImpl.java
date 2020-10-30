package it.unibz.inf.ontop.model.term.functionsymbol.impl.geof;

import com.google.common.collect.ImmutableList;
import it.unibz.inf.ontop.model.term.ImmutableTerm;
import it.unibz.inf.ontop.model.term.TermFactory;
import it.unibz.inf.ontop.model.type.RDFDatatype;
import org.apache.commons.rdf.api.IRI;

import javax.annotation.Nonnull;

public class GeofRcc8TppiFunctionSymbolImpl  extends AbstractGeofBooleanFunctionSymbolImplUsingRelate {

    public GeofRcc8TppiFunctionSymbolImpl(@Nonnull IRI functionIRI, RDFDatatype wktLiteralType, RDFDatatype xsdBooleanType) {
        super("GEOF_RCC8_TPPI", functionIRI, ImmutableList.of(wktLiteralType, wktLiteralType), xsdBooleanType);
    }

    // Set the matrix pattern for relate here
    final String matrix_pattern = "TTTFTTFFT";

    @Override
    protected ImmutableTerm setMatrixPattern(TermFactory termFactory) {
        return termFactory.getDBStringConstant(matrix_pattern);
    }

    @Override
    public TriFunction<ImmutableTerm, ImmutableTerm, ImmutableTerm, ImmutableTerm> getDBFunction(TermFactory termFactory) {
        return termFactory::getDBRelate;
    }
}
