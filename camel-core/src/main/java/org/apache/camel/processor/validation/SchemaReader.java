begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.validation
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|SchemaFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|ls
operator|.
name|LSResourceResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|IOConverter
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
name|util
operator|.
name|IOHelper
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
name|util
operator|.
name|ObjectHelper
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
name|util
operator|.
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Reads the schema used in the processor {@link ValidatingProcessor}.  * A schema re-reading could be forced using {@link org.apache.camel.component.validator.ValidatorEndpoint#clearCachedSchema()}.  */
end_comment

begin_class
DECL|class|SchemaReader
specifier|public
class|class
name|SchemaReader
block|{
comment|/** Key of the global option to switch either off or on  the access to external DTDs in the XML Validator for StreamSources.       * Only effective, if not a custom schema factory is used.*/
DECL|field|ACCESS_EXTERNAL_DTD
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_EXTERNAL_DTD
init|=
literal|"CamelXmlValidatorAccessExternalDTD"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SchemaReader
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|schemaLanguage
specifier|private
name|String
name|schemaLanguage
init|=
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
decl_stmt|;
comment|// must be volatile because is accessed from different threads see ValidatorEndpoint.clearCachedSchema
DECL|field|schema
specifier|private
specifier|volatile
name|Schema
name|schema
decl_stmt|;
DECL|field|schemaSource
specifier|private
name|Source
name|schemaSource
decl_stmt|;
comment|// must be volatile because is accessed from different threads see ValidatorEndpoint.clearCachedSchema
DECL|field|schemaFactory
specifier|private
specifier|volatile
name|SchemaFactory
name|schemaFactory
decl_stmt|;
DECL|field|schemaUrl
specifier|private
name|URL
name|schemaUrl
decl_stmt|;
DECL|field|schemaFile
specifier|private
name|File
name|schemaFile
decl_stmt|;
DECL|field|schemaAsByteArray
specifier|private
name|byte
index|[]
name|schemaAsByteArray
decl_stmt|;
DECL|field|schemaResourceUri
specifier|private
specifier|final
name|String
name|schemaResourceUri
decl_stmt|;
DECL|field|resourceResolver
specifier|private
name|LSResourceResolver
name|resourceResolver
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|SchemaReader ()
specifier|public
name|SchemaReader
parameter_list|()
block|{
name|this
operator|.
name|camelContext
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|schemaResourceUri
operator|=
literal|null
expr_stmt|;
block|}
comment|/** Specify a camel context and a schema resource URI in order to read the schema via the class resolver specified in the Camel context. */
DECL|method|SchemaReader (CamelContext camelContext, String schemaResourceUri)
specifier|public
name|SchemaReader
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|schemaResourceUri
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|schemaResourceUri
argument_list|,
literal|"schemaResourceUri"
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|schemaResourceUri
operator|=
name|schemaResourceUri
expr_stmt|;
block|}
DECL|method|loadSchema ()
specifier|public
name|void
name|loadSchema
parameter_list|()
throws|throws
name|Exception
block|{
comment|// force loading of schema
name|schema
operator|=
name|createSchema
argument_list|()
expr_stmt|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getSchema ()
specifier|public
name|Schema
name|getSchema
parameter_list|()
throws|throws
name|IOException
throws|,
name|SAXException
block|{
if|if
condition|(
name|schema
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|schema
operator|==
literal|null
condition|)
block|{
name|schema
operator|=
name|createSchema
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|schema
return|;
block|}
DECL|method|setSchema (Schema schema)
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
DECL|method|getSchemaLanguage ()
specifier|public
name|String
name|getSchemaLanguage
parameter_list|()
block|{
return|return
name|schemaLanguage
return|;
block|}
DECL|method|setSchemaLanguage (String schemaLanguage)
specifier|public
name|void
name|setSchemaLanguage
parameter_list|(
name|String
name|schemaLanguage
parameter_list|)
block|{
name|this
operator|.
name|schemaLanguage
operator|=
name|schemaLanguage
expr_stmt|;
block|}
DECL|method|getSchemaSource ()
specifier|public
name|Source
name|getSchemaSource
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|schemaSource
operator|==
literal|null
condition|)
block|{
name|schemaSource
operator|=
name|createSchemaSource
argument_list|()
expr_stmt|;
block|}
return|return
name|schemaSource
return|;
block|}
DECL|method|setSchemaSource (Source schemaSource)
specifier|public
name|void
name|setSchemaSource
parameter_list|(
name|Source
name|schemaSource
parameter_list|)
block|{
name|this
operator|.
name|schemaSource
operator|=
name|schemaSource
expr_stmt|;
block|}
DECL|method|getSchemaUrl ()
specifier|public
name|URL
name|getSchemaUrl
parameter_list|()
block|{
return|return
name|schemaUrl
return|;
block|}
DECL|method|setSchemaUrl (URL schemaUrl)
specifier|public
name|void
name|setSchemaUrl
parameter_list|(
name|URL
name|schemaUrl
parameter_list|)
block|{
name|this
operator|.
name|schemaUrl
operator|=
name|schemaUrl
expr_stmt|;
block|}
DECL|method|getSchemaFile ()
specifier|public
name|File
name|getSchemaFile
parameter_list|()
block|{
return|return
name|schemaFile
return|;
block|}
DECL|method|setSchemaFile (File schemaFile)
specifier|public
name|void
name|setSchemaFile
parameter_list|(
name|File
name|schemaFile
parameter_list|)
block|{
name|this
operator|.
name|schemaFile
operator|=
name|schemaFile
expr_stmt|;
block|}
DECL|method|getSchemaAsByteArray ()
specifier|public
name|byte
index|[]
name|getSchemaAsByteArray
parameter_list|()
block|{
return|return
name|schemaAsByteArray
return|;
block|}
DECL|method|setSchemaAsByteArray (byte[] schemaAsByteArray)
specifier|public
name|void
name|setSchemaAsByteArray
parameter_list|(
name|byte
index|[]
name|schemaAsByteArray
parameter_list|)
block|{
name|this
operator|.
name|schemaAsByteArray
operator|=
name|schemaAsByteArray
expr_stmt|;
block|}
DECL|method|getSchemaFactory ()
specifier|public
name|SchemaFactory
name|getSchemaFactory
parameter_list|()
block|{
if|if
condition|(
name|schemaFactory
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|schemaFactory
operator|==
literal|null
condition|)
block|{
name|schemaFactory
operator|=
name|createSchemaFactory
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|schemaFactory
return|;
block|}
DECL|method|setSchemaFactory (SchemaFactory schemaFactory)
specifier|public
name|void
name|setSchemaFactory
parameter_list|(
name|SchemaFactory
name|schemaFactory
parameter_list|)
block|{
name|this
operator|.
name|schemaFactory
operator|=
name|schemaFactory
expr_stmt|;
block|}
DECL|method|getResourceResolver ()
specifier|public
name|LSResourceResolver
name|getResourceResolver
parameter_list|()
block|{
return|return
name|resourceResolver
return|;
block|}
DECL|method|setResourceResolver (LSResourceResolver resourceResolver)
specifier|public
name|void
name|setResourceResolver
parameter_list|(
name|LSResourceResolver
name|resourceResolver
parameter_list|)
block|{
name|this
operator|.
name|resourceResolver
operator|=
name|resourceResolver
expr_stmt|;
block|}
DECL|method|createSchemaFactory ()
specifier|protected
name|SchemaFactory
name|createSchemaFactory
parameter_list|()
block|{
name|SchemaFactory
name|factory
init|=
name|SchemaFactory
operator|.
name|newInstance
argument_list|(
name|schemaLanguage
argument_list|)
decl_stmt|;
if|if
condition|(
name|getResourceResolver
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setResourceResolver
argument_list|(
name|getResourceResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|camelContext
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|get
argument_list|(
name|ACCESS_EXTERNAL_DTD
argument_list|)
argument_list|)
condition|)
block|{
try|try
block|{
name|factory
operator|.
name|setProperty
argument_list|(
name|XMLConstants
operator|.
name|ACCESS_EXTERNAL_DTD
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|factory
return|;
block|}
DECL|method|createSchemaSource ()
specifier|protected
name|Source
name|createSchemaSource
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You must specify either a schema, schemaFile, schemaSource, schemaUrl, or schemaUri property"
argument_list|)
throw|;
block|}
DECL|method|createSchema ()
specifier|protected
name|Schema
name|createSchema
parameter_list|()
throws|throws
name|SAXException
throws|,
name|IOException
block|{
name|SchemaFactory
name|factory
init|=
name|getSchemaFactory
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|getSchemaUrl
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
return|return
name|factory
operator|.
name|newSchema
argument_list|(
name|url
argument_list|)
return|;
block|}
block|}
name|File
name|file
init|=
name|getSchemaFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
return|return
name|factory
operator|.
name|newSchema
argument_list|(
name|file
argument_list|)
return|;
block|}
block|}
name|byte
index|[]
name|bytes
init|=
name|getSchemaAsByteArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|bytes
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
return|return
name|factory
operator|.
name|newSchema
argument_list|(
operator|new
name|StreamSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|schemaAsByteArray
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|schemaResourceUri
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|bytes
operator|=
name|readSchemaResource
argument_list|()
expr_stmt|;
return|return
name|factory
operator|.
name|newSchema
argument_list|(
operator|new
name|StreamSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
name|Source
name|source
init|=
name|getSchemaSource
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
return|return
name|factory
operator|.
name|newSchema
argument_list|(
name|source
argument_list|)
return|;
block|}
block|}
DECL|method|readSchemaResource ()
specifier|protected
name|byte
index|[]
name|readSchemaResource
parameter_list|()
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"reading schema resource: {}"
argument_list|,
name|schemaResourceUri
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
argument_list|,
name|schemaResourceUri
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bytes
operator|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// and make sure to close the input stream after the schema has been
comment|// loaded
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
return|return
name|bytes
return|;
block|}
block|}
end_class

end_unit

