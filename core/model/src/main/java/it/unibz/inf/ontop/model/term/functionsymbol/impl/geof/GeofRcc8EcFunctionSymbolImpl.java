package it.unibz.inf.ontop.model.term.functionsymbol.impl.geof;

import com.google.common.collect.ImmutableList;
import it.unibz.inf.ontop.model.term.ImmutableTerm;
import it.unibz.inf.ontop.model.term.TermFactory;
import it.unibz.inf.ontop.model.type.RDFDatatype;
import org.apache.commons.rdf.api.IRI;

import javax.annotation.Nonnull;

public class GeofRcc8EcFunctionSymbolImpl  extends AbstractGeofBooleanFunctionSymbolImpl {

    public GeofRcc8EcFunctionSymbolImpl(@Nonnull IRI functionIRI, RDFDatatype wktLiteralType, RDFDatatype xsdBooleanType) {
        super("GEOF_RCC8_EC", functionIRI, ImmutableList.of(wktLiteralType, wktLiteralType), xsdBooleanType);
    }

    @Override
    protected ImmutableTerm computeDBBooleanTerm(ImmutableList<ImmutableTerm> subLexicalTerms, ImmutableList<ImmutableTerm> typeTerms, TermFactory termFactory) {
        //return termFactory.getDBSTTouches(subLexicalTerms.get(0), subLexicalTerms.get(1));
        final String matrix_pattern = "FFTFTTTTT";
        return termFactory.getDBRelate(subLexicalTerms.get(0), subLexicalTerms.get(1), termFactory.getDBStringConstant(matrix_pattern));
    }
}