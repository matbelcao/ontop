package it.unibz.inf.ontop.dbschema.impl;

import com.google.common.collect.ImmutableSet;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import it.unibz.inf.ontop.dbschema.RelationID;
import it.unibz.inf.ontop.exception.MetadataExtractionException;
import it.unibz.inf.ontop.model.type.TypeFactory;

import java.sql.Connection;

public class DB2DBMetadataProvider extends DefaultDBMetadataProvider {

    @AssistedInject
    DB2DBMetadataProvider(@Assisted Connection connection, TypeFactory typeFactory) throws MetadataExtractionException {
        super(connection, new QueryBasedDefaultSchemaProvider(
                connection,
                "select NULL AS TABLE_CAT, CURRENT SCHEMA AS TABLE_SCHEM from SYSIBM.SYSDUMMY1"), typeFactory);
        // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_11.5.0/com.ibm.db2.luw.sql.ref.doc/doc/r0005881.html
        // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_11.5.0/com.ibm.db2.luw.sql.ref.doc/doc/r0000720.html
    }

    private static final ImmutableSet<String> IGNORED_SCHEMAS = ImmutableSet.of("SYSTOOLS", "SYSCAT", "SYSIBM", "SYSIBMADM", "SYSSTAT");

    @Override
    protected boolean isRelationExcluded(RelationID id) {
        return IGNORED_SCHEMAS.contains(getRelationSchema(id));
    }

    /*
    // Alternative solution for DB2 to print column names
        String sqlQuery = String.format("SELECT colname, typename \n FROM SysCat.Columns \n" +
                "WHERE tabname = '%s' AND tabschema = '%s'", tableName, tableSchema);

			return "SELECT TABSCHEMA, TABNAME " +
			       "FROM SYSCAT.TABLES " +
			       "WHERE OWNERTYPE='U' AND (TYPE='T' OR TYPE='V') " +
			       "     AND TBSPACEID IN (SELECT TBSPACEID FROM SYSCAT.TABLESPACES WHERE TBSPACE LIKE 'USERSPACE%')";
    */
}
