begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintViolation
import|;
end_import

begin_import
import|import
name|javax
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
name|InvalidPayloadRuntimeException
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
name|component
operator|.
name|cm
operator|.
name|client
operator|.
name|SMSMessage
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
name|component
operator|.
name|cm
operator|.
name|exceptions
operator|.
name|HostUnavailableException
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
name|impl
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
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpHead
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
import|;
end_import

begin_comment
comment|/**  * is the exchange processor. Sends a validated sms message to CM Endpoints.  */
end_comment

begin_class
DECL|class|CMProducer
specifier|public
class|class
name|CMProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|validator
specifier|private
name|Validator
name|validator
decl_stmt|;
comment|/**      * sends a valid message to CM endpoints.      */
DECL|field|sender
specifier|private
name|CMSender
name|sender
decl_stmt|;
DECL|method|CMProducer (final CMEndpoint endpoint, final CMSender sender)
specifier|public
name|CMProducer
parameter_list|(
specifier|final
name|CMEndpoint
name|endpoint
parameter_list|,
specifier|final
name|CMSender
name|sender
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
block|}
comment|/**      * Producer is a exchange processor. This process is built in several steps.      * 1. Validate message receive from client 2. Send validated message to CM      * endpoints. 3. Process response from CM endpoints.      */
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Immutable message receive from clients. Throws camel ' s
comment|// InvalidPayloadException
specifier|final
name|SMSMessage
name|smsMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|SMSMessage
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Validates Payload - SMSMessage
name|log
operator|.
name|trace
argument_list|(
literal|"Validating SMSMessage instance provided: {}"
argument_list|,
name|smsMessage
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|SMSMessage
argument_list|>
argument_list|>
name|constraintViolations
init|=
name|getValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|smsMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|constraintViolations
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
specifier|final
name|StringBuffer
name|msg
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|ConstraintViolation
argument_list|<
name|SMSMessage
argument_list|>
name|cv
range|:
name|constraintViolations
control|)
block|{
name|msg
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"- Invalid value for %s: %s"
argument_list|,
name|cv
operator|.
name|getPropertyPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|cv
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|InvalidPayloadRuntimeException
argument_list|(
name|exchange
argument_list|,
name|SMSMessage
operator|.
name|class
argument_list|)
throw|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"SMSMessage instance is valid: {}"
argument_list|,
name|smsMessage
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// We have a valid (immutable) SMSMessage instance, lets extend to
comment|// CMMessage
comment|// This is the instance we will use to build the XML document to be
comment|// sent to CM SMS GW.
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|smsMessage
operator|.
name|getPhoneNumber
argument_list|()
argument_list|,
name|smsMessage
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"CMMessage instance build from valid SMSMessage instance"
argument_list|)
expr_stmt|;
if|if
condition|(
name|smsMessage
operator|.
name|getFrom
argument_list|()
operator|==
literal|null
operator|||
name|smsMessage
operator|.
name|getFrom
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|df
init|=
name|getConfiguration
argument_list|()
operator|.
name|getDefaultFrom
argument_list|()
decl_stmt|;
name|cmMessage
operator|.
name|setSender
argument_list|(
name|df
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Dynamic sender is set to default dynamic sender: {}"
argument_list|,
name|df
argument_list|)
expr_stmt|;
block|}
comment|// Remember, this can be null.
name|cmMessage
operator|.
name|setIdAsString
argument_list|(
name|smsMessage
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// Unicode and multipart
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getDefaultMaxNumberOfParts
argument_list|()
argument_list|)
expr_stmt|;
comment|// 2. Send a validated sms message to CM endpoints
comment|// throws MessagingException for abnormal situations.
name|sender
operator|.
name|send
argument_list|(
name|cmMessage
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Request accepted by CM Host: {}"
argument_list|,
name|cmMessage
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// log at debug level for singletons, for prototype scoped log at trace
comment|// level to not spam logs
name|log
operator|.
name|debug
argument_list|(
literal|"Starting CMProducer"
argument_list|)
expr_stmt|;
specifier|final
name|CMConfiguration
name|configuration
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isTestConnectionOnStartup
argument_list|()
condition|)
block|{
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Checking connection - {}"
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getCMUrl
argument_list|()
argument_list|)
expr_stmt|;
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
operator|.
name|execute
argument_list|(
operator|new
name|HttpHead
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCMUrl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Connection to {}: OK"
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getCMUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Connection to {}: NOT AVAILABLE"
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getCMUrl
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|HostUnavailableException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|// keep starting
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"CMProducer started"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|CMEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|CMEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|CMConfiguration
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
DECL|method|getValidator ()
specifier|public
name|Validator
name|getValidator
parameter_list|()
block|{
if|if
condition|(
name|validator
operator|==
literal|null
condition|)
block|{
name|validator
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getValidator
argument_list|()
expr_stmt|;
block|}
return|return
name|validator
return|;
block|}
DECL|method|getSender ()
specifier|public
name|CMSender
name|getSender
parameter_list|()
block|{
return|return
name|sender
return|;
block|}
DECL|method|setSender (CMSender sender)
specifier|public
name|void
name|setSender
parameter_list|(
name|CMSender
name|sender
parameter_list|)
block|{
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
block|}
block|}
end_class

end_unit

