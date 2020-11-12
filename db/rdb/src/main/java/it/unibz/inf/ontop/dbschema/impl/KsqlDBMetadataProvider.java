package it.unibz.inf.ontop.dbschema.impl;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import it.unibz.inf.ontop.dbschema.QuotedID;
import it.unibz.inf.ontop.dbschema.QuotedIDFactory;
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
            defaultSchema = retrieveDefaultSchema("SHOW TABLES");
        }
        catch (SQLException e) {
            throw new MetadataExtractionException(e);
        }
    }

    protected static QuotedIDFactory getQuotedIDFactory(Connection connection) throws MetadataExtractionException {
        try {
            DatabaseMetaData md = connection.getMetaData();
            return new KsqlQuotedIDFactory(false);
        }catch (SQLException e) {
            throw new MetadataExtractionException(e);
        }
    }

    @Override
    protected QuotedID retrieveDefaultSchema(String sql) throws MetadataExtractionException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            /*System.out.println();
            System.out.println("SCHEMA: "+connection.getSchema());*/
            return rawIdFactory.createRelationID(connection.getSchema(), "DUMMY").getSchemaID();
        }
        catch (SQLException e) {
            throw new MetadataExtractionException(e);
        }
    }

    @Override
    public QuotedID getDefaultSchema() { return defaultSchema; }
}
