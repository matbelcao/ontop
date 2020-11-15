package it.unibz.inf.ontop.model.type.impl;

import com.google.common.collect.ImmutableMap;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import it.unibz.inf.ontop.model.type.*;
import it.unibz.inf.ontop.model.vocabulary.XSD;

import java.util.Map;

import static it.unibz.inf.ontop.model.type.DBTermType.Category.FLOAT_DOUBLE;
import static it.unibz.inf.ontop.model.type.DBTermType.Category.INTEGER;

public class KsqlDBTypeFactory extends DefaultSQLDBTypeFactory {

    /**
     * KSQL-DB datatypes description : https://docs.ksqldb.io/en/latest/developer-guide/syntax-reference/#data-types
     *
     * SQL to XML mappings : https://www.w3.org/2001/sw/rdb2rdf/wiki/Mapping_SQL_datatypes_to_XML_Schema_datatypes
     */

    protected static final String BYTE_STR = "BYTE";
    protected static final String SHORT_STR = "SHORT";
    protected static final String LONG_STR = "LONG";
    protected static final String STRING_STR = "STRING";
    protected static final String DEC_STR = "DEC";
    protected static final String INTERVAL_STR = "INTERVAL"; //TODO
    protected static final String ARRAY_STR = "ARRAY"; //TODO
    protected static final String STRUCT_STR = "STRUCT"; //TODO
    protected static final String MAP_STR = "MAP"; //TODO

    @AssistedInject
    protected KsqlDBTypeFactory(@Assisted TermType rootTermType, @Assisted TypeFactory typeFactory) {
        super(createKSQLTypeMap(rootTermType, typeFactory), createKSQLCodeMap());
    }

    private static Map<String, DBTermType> createKSQLTypeMap(TermType rootTermType, TypeFactory typeFactory) {

        TermTypeAncestry rootAncestry = rootTermType.getAncestry();

        // Redefine the datatypes for numerical values
        RDFDatatype xsdInt = typeFactory.getDatatype(XSD.INT);
        RDFDatatype xsdLong = typeFactory.getDatatype(XSD.LONG);

        DBTermType intType = new NumberDBTermType(INT_STR, rootAncestry, xsdInt, INTEGER);
        DBTermType longType = new NumberDBTermType(BIGINT_STR, rootAncestry, xsdLong, INTEGER);
        DBTermType stringType = new StringDBTermType(STRING_STR, rootAncestry, typeFactory.getXsdStringDatatype());

        Map<String, DBTermType> map = createDefaultSQLTypeMap(rootTermType, typeFactory);
        map.put(STRING_STR, stringType);
        map.put(INT_STR,intType);
        map.put(INTEGER_STR,intType);
        map.put(BIGINT_STR,longType);
        return map;
    }

    private static ImmutableMap<DefaultTypeCode, String> createKSQLCodeMap() {
        Map<DefaultTypeCode, String> map = createDefaultSQLCodeMap();
        map.put(DefaultTypeCode.STRING, STRING_STR);
        return ImmutableMap.copyOf(map);
    }

}
