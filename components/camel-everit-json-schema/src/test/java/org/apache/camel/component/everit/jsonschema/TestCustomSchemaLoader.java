begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.everit.jsonschema
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|everit
operator|.
name|jsonschema
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|everit
operator|.
name|json
operator|.
name|schema
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|everit
operator|.
name|json
operator|.
name|schema
operator|.
name|loader
operator|.
name|SchemaLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|everit
operator|.
name|json
operator|.
name|schema
operator|.
name|loader
operator|.
name|SchemaLoader
operator|.
name|SchemaLoaderBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|json
operator|.
name|JSONObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|json
operator|.
name|JSONTokener
import|;
end_import

begin_class
DECL|class|TestCustomSchemaLoader
specifier|public
class|class
name|TestCustomSchemaLoader
implements|implements
name|JsonSchemaLoader
block|{
annotation|@
name|Override
DECL|method|createSchema (CamelContext camelContext, InputStream schemaInputStream)
specifier|public
name|Schema
name|createSchema
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|InputStream
name|schemaInputStream
parameter_list|)
throws|throws
name|IOException
block|{
name|SchemaLoaderBuilder
name|schemaLoaderBuilder
init|=
name|SchemaLoader
operator|.
name|builder
argument_list|()
operator|.
name|draftV6Support
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|inputStream
init|=
name|schemaInputStream
init|)
block|{
name|JSONObject
name|rawSchema
init|=
operator|new
name|JSONObject
argument_list|(
operator|new
name|JSONTokener
argument_list|(
name|inputStream
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|schemaLoaderBuilder
operator|.
name|schemaJson
argument_list|(
name|rawSchema
argument_list|)
operator|.
name|addFormatValidator
argument_list|(
operator|new
name|EvenCharNumValidator
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|load
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

