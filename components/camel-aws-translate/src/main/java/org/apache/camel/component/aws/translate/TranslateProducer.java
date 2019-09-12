begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.translate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|translate
package|;
end_package

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
name|URISupport
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonServiceException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|translate
operator|.
name|AmazonTranslate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|translate
operator|.
name|model
operator|.
name|TranslateTextRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|translate
operator|.
name|model
operator|.
name|TranslateTextResult
import|;
end_import

begin_comment
comment|/**  * A Producer which sends messages to the Amazon Translate Service  *<a href="http://aws.amazon.com/translate/">AWS Translate</a>  */
end_comment

begin_class
DECL|class|TranslateProducer
specifier|public
class|class
name|TranslateProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|translateProducerToString
specifier|private
specifier|transient
name|String
name|translateProducerToString
decl_stmt|;
DECL|method|TranslateProducer (Endpoint endpoint)
specifier|public
name|TranslateProducer
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
switch|switch
condition|(
name|determineOperation
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
case|case
name|translateText
case|:
name|translateText
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getTranslateClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported operation"
argument_list|)
throw|;
block|}
block|}
DECL|method|determineOperation (Exchange exchange)
specifier|private
name|TranslateOperations
name|determineOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|TranslateOperations
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TranslateConstants
operator|.
name|OPERATION
argument_list|,
name|TranslateOperations
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
name|operation
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getOperation
argument_list|()
expr_stmt|;
block|}
return|return
name|operation
return|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|TranslateConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|translateProducerToString
operator|==
literal|null
condition|)
block|{
name|translateProducerToString
operator|=
literal|"TranslateProducer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|translateProducerToString
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|TranslateEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|TranslateEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|translateText (AmazonTranslate translateClient, Exchange exchange)
specifier|private
name|void
name|translateText
parameter_list|(
name|AmazonTranslate
name|translateClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|TranslateTextRequest
name|request
init|=
operator|new
name|TranslateTextRequest
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|getConfiguration
argument_list|()
operator|.
name|isAutodetectSourceLanguage
argument_list|()
condition|)
block|{
name|String
name|source
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TranslateConstants
operator|.
name|SOURCE_LANGUAGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|target
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TranslateConstants
operator|.
name|TARGET_LANGUAGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|source
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|target
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Source and target language must be specified"
argument_list|)
throw|;
block|}
name|request
operator|.
name|setSourceLanguageCode
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|request
operator|.
name|setTargetLanguageCode
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|source
init|=
literal|"auto"
decl_stmt|;
name|String
name|target
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TranslateConstants
operator|.
name|TARGET_LANGUAGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|source
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|target
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Target language must be specified when autodetection of source language is enabled"
argument_list|)
throw|;
block|}
name|request
operator|.
name|setSourceLanguageCode
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|request
operator|.
name|setTargetLanguageCode
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
name|request
operator|.
name|setText
argument_list|(
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|TranslateTextResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|translateClient
operator|.
name|translateText
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Translate Text command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|result
operator|.
name|getTranslatedText
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getMessageForResponse (final Exchange exchange)
specifier|public
specifier|static
name|Message
name|getMessageForResponse
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
block|}
end_class

end_unit

