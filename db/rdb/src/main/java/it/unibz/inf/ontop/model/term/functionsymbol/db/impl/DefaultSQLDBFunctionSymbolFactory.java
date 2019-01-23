package it.unibz.inf.ontop.model.term.functionsymbol.db.impl;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import it.unibz.inf.ontop.model.term.ImmutableTerm;
import it.unibz.inf.ontop.model.term.TermFactory;
import it.unibz.inf.ontop.model.term.functionsymbol.db.DBBooleanFunctionSymbol;
import it.unibz.inf.ontop.model.term.functionsymbol.db.DBFunctionSymbol;
import it.unibz.inf.ontop.model.type.*;

import java.util.function.Function;

public class DefaultSQLDBFunctionSymbolFactory extends AbstractSQLDBFunctionSymbolFactory {

    private static final String UNSUPPORTED_MSG = "Not supported in the Default SQL factory since no-one uses " +
            "the old official standard function.\n" +
            "Please specific it in your dialect factory";

    @Inject
    private DefaultSQLDBFunctionSymbolFactory(TypeFactory typeFactory) {
        super(createDefaultNormalizationTable(typeFactory), createDefaultDenormalizationTable(typeFactory),
                createDefaultRegularFunctionTable(typeFactory), typeFactory);
    }

    @Override
    protected String serializeContains(ImmutableList<? extends ImmutableTerm> immutableTerms,
                                       Function<ImmutableTerm, String> immutableTermStringFunction, TermFactory termFactory) {
        throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    protected String serializeStrBefore(ImmutableList<? extends ImmutableTerm> terms,
                                        Function<ImmutableTerm, String> termConverter, TermFactory termFactory) {
        throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    protected String serializeStrAfter(ImmutableList<? extends ImmutableTerm> terms,
                                       Function<ImmutableTerm, String> termConverter, TermFactory termFactory) {
        throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    protected String serializeMD5(ImmutableList<? extends ImmutableTerm> terms,
                                  Function<ImmutableTerm, String> termConverter, TermFactory termFactory) {
        throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    protected String serializeSHA1(ImmutableList<? extends ImmutableTerm> terms,
                                   Function<ImmutableTerm, String> termConverter, TermFactory termFactory) {
        throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    protected String serializeSHA256(ImmutableList<? extends ImmutableTerm> terms,
                                     Function<ImmutableTerm, String> termConverter, TermFactory termFactory) {
        throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    protected String serializeSHA512(ImmutableList<? extends ImmutableTerm> terms,
                                     Function<ImmutableTerm, String> termConverter, TermFactory termFactory) {
        throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public DBFunctionSymbol getDBUUIDFunctionSymbol() {
        throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public DBBooleanFunctionSymbol getDBRegexpMatches2() {
        throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }

    @Override
    public DBBooleanFunctionSymbol getDBRegexpMatches3() {
        throw new UnsupportedOperationException(UNSUPPORTED_MSG);
    }
}