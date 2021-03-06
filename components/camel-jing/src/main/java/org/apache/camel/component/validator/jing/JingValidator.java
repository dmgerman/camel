begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator.jing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
operator|.
name|jing
package|;
end_package

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
name|SAXSource
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
name|InputSource
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
name|XMLReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thaiopensource
operator|.
name|util
operator|.
name|PropertyMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thaiopensource
operator|.
name|util
operator|.
name|PropertyMapBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thaiopensource
operator|.
name|validate
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thaiopensource
operator|.
name|validate
operator|.
name|ValidateProperty
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thaiopensource
operator|.
name|validate
operator|.
name|Validator
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thaiopensource
operator|.
name|xml
operator|.
name|sax
operator|.
name|Jaxp11XMLReaderCreator
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
name|Endpoint
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
name|Message
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
name|support
operator|.
name|DefaultProducer
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
name|support
operator|.
name|ExchangeHelper
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
name|support
operator|.
name|processor
operator|.
name|validation
operator|.
name|DefaultValidationErrorHandler
import|;
end_import

begin_comment
comment|/**  * A validator which uses the<a  * href="http://www.thaiopensource.com/relaxng/jing.html">Jing</a> library to  * validate XML against RelaxNG  */
end_comment

begin_class
DECL|class|JingValidator
specifier|public
class|class
name|JingValidator
extends|extends
name|DefaultProducer
block|{
DECL|field|schema
specifier|private
name|Schema
name|schema
decl_stmt|;
DECL|method|JingValidator (Endpoint endpoint)
specifier|public
name|JingValidator
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|Jaxp11XMLReaderCreator
name|xmlCreator
init|=
operator|new
name|Jaxp11XMLReaderCreator
argument_list|()
decl_stmt|;
name|DefaultValidationErrorHandler
name|errorHandler
init|=
operator|new
name|DefaultValidationErrorHandler
argument_list|()
decl_stmt|;
name|PropertyMapBuilder
name|mapBuilder
init|=
operator|new
name|PropertyMapBuilder
argument_list|()
decl_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|ValidateProperty
operator|.
name|XML_READER_CREATOR
argument_list|,
name|xmlCreator
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|ValidateProperty
operator|.
name|ERROR_HANDLER
argument_list|,
name|errorHandler
argument_list|)
expr_stmt|;
name|PropertyMap
name|propertyMap
init|=
name|mapBuilder
operator|.
name|toPropertyMap
argument_list|()
decl_stmt|;
name|Validator
name|validator
init|=
name|getSchema
argument_list|()
operator|.
name|createValidator
argument_list|(
name|propertyMap
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|SAXSource
name|saxSource
init|=
name|in
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
name|saxSource
operator|==
literal|null
condition|)
block|{
name|Source
name|source
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|Source
operator|.
name|class
argument_list|)
decl_stmt|;
name|saxSource
operator|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|SAXSource
operator|.
name|class
argument_list|,
name|source
argument_list|)
expr_stmt|;
block|}
name|InputSource
name|bodyInput
init|=
name|saxSource
operator|.
name|getInputSource
argument_list|()
decl_stmt|;
comment|// now lets parse the body using the validator
name|XMLReader
name|reader
init|=
name|xmlCreator
operator|.
name|createXMLReader
argument_list|()
decl_stmt|;
name|reader
operator|.
name|setContentHandler
argument_list|(
name|validator
operator|.
name|getContentHandler
argument_list|()
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setDTDHandler
argument_list|(
name|validator
operator|.
name|getDTDHandler
argument_list|()
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setErrorHandler
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
name|reader
operator|.
name|parse
argument_list|(
name|bodyInput
argument_list|)
expr_stmt|;
name|errorHandler
operator|.
name|handleErrors
argument_list|(
name|exchange
argument_list|,
name|schema
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getSchema ()
specifier|public
name|Schema
name|getSchema
parameter_list|()
block|{
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
block|}
end_class

end_unit

