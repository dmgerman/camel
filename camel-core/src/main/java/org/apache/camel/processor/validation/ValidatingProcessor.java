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
name|sax
operator|.
name|SAXResult
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
name|sax
operator|.
name|SAXSource
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
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Validator
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
name|Exchange
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
name|Processor
import|;
end_import

begin_comment
comment|/**  * A processor which validates the XML version of the inbound message body  * against some schema either in XSD or RelaxNG  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|ValidatingProcessor
specifier|public
class|class
name|ValidatingProcessor
implements|implements
name|Processor
block|{
comment|// for lazy creation of the Schema
DECL|field|schemaLanguage
specifier|private
name|String
name|schemaLanguage
init|=
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
decl_stmt|;
DECL|field|schema
specifier|private
name|Schema
name|schema
decl_stmt|;
DECL|field|schemaSource
specifier|private
name|Source
name|schemaSource
decl_stmt|;
DECL|field|schemaFactory
specifier|private
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
DECL|field|errorHandler
specifier|private
name|ValidatorErrorHandler
name|errorHandler
init|=
operator|new
name|DefaultValidationErrorHandler
argument_list|()
decl_stmt|;
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Schema
name|schema
init|=
name|getSchema
argument_list|()
decl_stmt|;
name|Validator
name|validator
init|=
name|schema
operator|.
name|newValidator
argument_list|()
decl_stmt|;
name|Source
name|source
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SAXSource
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoXmlBodyValidationException
argument_list|(
name|exchange
argument_list|)
throw|;
block|}
comment|// create a new errorHandler and set it on the validator
comment|// must be a local instance to avoid problems with concurrency (to be thread safe)
name|ValidatorErrorHandler
name|handler
init|=
name|errorHandler
operator|.
name|getClass
argument_list|()
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|validator
operator|.
name|setErrorHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|SAXResult
name|result
init|=
operator|new
name|SAXResult
argument_list|()
decl_stmt|;
name|validator
operator|.
name|validate
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleErrors
argument_list|(
name|exchange
argument_list|,
name|schema
argument_list|,
name|result
argument_list|)
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
name|schema
operator|=
name|createSchema
argument_list|()
expr_stmt|;
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
name|schemaFactory
operator|=
name|createSchemaFactory
argument_list|()
expr_stmt|;
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
DECL|method|getErrorHandler ()
specifier|public
name|ValidatorErrorHandler
name|getErrorHandler
parameter_list|()
block|{
return|return
name|errorHandler
return|;
block|}
DECL|method|setErrorHandler (ValidatorErrorHandler errorHandler)
specifier|public
name|void
name|setErrorHandler
parameter_list|(
name|ValidatorErrorHandler
name|errorHandler
parameter_list|)
block|{
name|this
operator|.
name|errorHandler
operator|=
name|errorHandler
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
DECL|method|createSchemaFactory ()
specifier|protected
name|SchemaFactory
name|createSchemaFactory
parameter_list|()
block|{
return|return
name|SchemaFactory
operator|.
name|newInstance
argument_list|(
name|schemaLanguage
argument_list|)
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
literal|"You must specify either a schema, schemaFile, schemaSource or schemaUrl property"
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
return|return
name|factory
operator|.
name|newSchema
argument_list|(
name|url
argument_list|)
return|;
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
return|return
name|factory
operator|.
name|newSchema
argument_list|(
name|file
argument_list|)
return|;
block|}
return|return
name|factory
operator|.
name|newSchema
argument_list|(
name|getSchemaSource
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

