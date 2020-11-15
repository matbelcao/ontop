package it.unibz.inf.ontop.dbschema.impl;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import it.unibz.inf.ontop.dbschema.*;
import it.unibz.inf.ontop.exception.MetadataExtractionException;
import it.unibz.inf.ontop.model.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class KsqlDBMetadataProvider extends  DefaultDBMetadataProvider{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDBMetadataProvider.class);

    private final QuotedID defaultSchema;
    private final DatabaseMetaData metadata;

    @AssistedInject
    KsqlDBMetadataProvider(@Assisted Connection connection, TypeFactory typeFactory) throws MetadataExtractionException {
        super(connection, getQuotedIDFactory(connection), typeFactory);
        try {
            this.metadata = connection.getMetaData();
            defaultSchema = rawIdFactory.createRelationID(connection.getSchema(), "DUMMY").getSchemaID();
        }
        catch (SQLException e) {
            throw new MetadataExtractionException(e);
        }
    }

    protected static QuotedIDFactory getQuotedIDFactory(Connection connection) throws MetadataExtractionException {
        try {
            DatabaseMetaData md = connection.getMetaData();
            return new KsqlQuotedIDFactory(true);
        }catch (SQLException e) {
            throw new MetadataExtractionException(e);
        }
    }

    @Override
    public QuotedID getDefaultSchema() { return defaultSchema; }

    @Override
    public void insertIntegrityConstraints(DatabaseRelationDefinition relation, MetadataLookup metadataLookup) throws MetadataExtractionException {
        // TODO: CHECK KSQLDB DOCUMENTATION for primary keys and unique identifiers!!!!
    }




    @Override
    protected String getRelationSchema(RelationID relationID) { return null; }

    @Override
    protected RelationID getRelationID(ResultSet rs) throws SQLException {
        return getRelationID_KSQLDB(rs,"TABLE_NAME");
    }

    @Override
    protected RelationID getPKRelationID(ResultSet rs) throws SQLException {
        return getRelationID_KSQLDB(rs,"PKTABLE_NAME");
    }

    @Override
    protected RelationID getFKRelationID(ResultSet rs) throws SQLException {
        return getRelationID_KSQLDB(rs,"FKTABLE_NAME");
    }

    // KSQL-DB doesn't have a schema!!
    protected final RelationID getRelationID_KSQLDB(ResultSet rs, String tableNameColumn) throws SQLException {
        return rawIdFactory.createRelationID(null, rs.getString(tableNameColumn));
    }

}
