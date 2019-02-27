begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.kms
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
name|kms
package|;
end_package

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
name|kms
operator|.
name|AWSKMS
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
name|kms
operator|.
name|model
operator|.
name|CreateKeyRequest
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
name|kms
operator|.
name|model
operator|.
name|CreateKeyResult
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
name|kms
operator|.
name|model
operator|.
name|DescribeKeyRequest
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
name|kms
operator|.
name|model
operator|.
name|DescribeKeyResult
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
name|kms
operator|.
name|model
operator|.
name|DisableKeyRequest
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
name|kms
operator|.
name|model
operator|.
name|DisableKeyResult
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
name|kms
operator|.
name|model
operator|.
name|EnableKeyRequest
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
name|kms
operator|.
name|model
operator|.
name|EnableKeyResult
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
name|kms
operator|.
name|model
operator|.
name|ListKeysRequest
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
name|kms
operator|.
name|model
operator|.
name|ListKeysResult
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
name|kms
operator|.
name|model
operator|.
name|ScheduleKeyDeletionRequest
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
name|kms
operator|.
name|model
operator|.
name|ScheduleKeyDeletionResult
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

begin_comment
comment|/**  * A Producer which sends messages to the Amazon KMS Service  *<a href="http://aws.amazon.com/kms/">AWS KMS</a>  */
end_comment

begin_class
DECL|class|KMSProducer
specifier|public
class|class
name|KMSProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|kmsProducerToString
specifier|private
specifier|transient
name|String
name|kmsProducerToString
decl_stmt|;
DECL|method|KMSProducer (Endpoint endpoint)
specifier|public
name|KMSProducer
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
name|listKeys
case|:
name|listKeys
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKmsClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|createKey
case|:
name|createKey
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKmsClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|disableKey
case|:
name|disableKey
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKmsClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|enableKey
case|:
name|enableKey
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKmsClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|scheduleKeyDeletion
case|:
name|scheduleKeyDeletion
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKmsClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|describeKey
case|:
name|describeKey
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKmsClient
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
name|KMSOperations
name|determineOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|KMSOperations
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|OPERATION
argument_list|,
name|KMSOperations
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
name|KMSConfiguration
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
name|kmsProducerToString
operator|==
literal|null
condition|)
block|{
name|kmsProducerToString
operator|=
literal|"KMSProducer["
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
name|kmsProducerToString
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|KMSEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|KMSEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|listKeys (AWSKMS kmsClient, Exchange exchange)
specifier|private
name|void
name|listKeys
parameter_list|(
name|AWSKMS
name|kmsClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|ListKeysRequest
name|request
init|=
operator|new
name|ListKeysRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|LIMIT
argument_list|)
argument_list|)
condition|)
block|{
name|int
name|limit
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|LIMIT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|withLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
name|ListKeysResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|kmsClient
operator|.
name|listKeys
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
literal|"List Keys command returned the error code {}"
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
argument_list|)
expr_stmt|;
block|}
DECL|method|createKey (AWSKMS kmsClient, Exchange exchange)
specifier|private
name|void
name|createKey
parameter_list|(
name|AWSKMS
name|kmsClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|CreateKeyRequest
name|request
init|=
operator|new
name|CreateKeyRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|DESCRIPTION
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|description
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|DESCRIPTION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|withDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
name|CreateKeyResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|kmsClient
operator|.
name|createKey
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
literal|"Create Key command returned the error code {}"
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
argument_list|)
expr_stmt|;
block|}
DECL|method|disableKey (AWSKMS kmsClient, Exchange exchange)
specifier|private
name|void
name|disableKey
parameter_list|(
name|AWSKMS
name|kmsClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|DisableKeyRequest
name|request
init|=
operator|new
name|DisableKeyRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|KEY_ID
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|keyId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|KEY_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|withKeyId
argument_list|(
name|keyId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Key Id must be specified"
argument_list|)
throw|;
block|}
name|DisableKeyResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|kmsClient
operator|.
name|disableKey
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
literal|"Disable Key command returned the error code {}"
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
argument_list|)
expr_stmt|;
block|}
DECL|method|scheduleKeyDeletion (AWSKMS kmsClient, Exchange exchange)
specifier|private
name|void
name|scheduleKeyDeletion
parameter_list|(
name|AWSKMS
name|kmsClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|ScheduleKeyDeletionRequest
name|request
init|=
operator|new
name|ScheduleKeyDeletionRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|KEY_ID
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|keyId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|KEY_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|withKeyId
argument_list|(
name|keyId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Key Id must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|PENDING_WINDOW_IN_DAYS
argument_list|)
argument_list|)
condition|)
block|{
name|int
name|pendingWindows
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|PENDING_WINDOW_IN_DAYS
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|withPendingWindowInDays
argument_list|(
name|pendingWindows
argument_list|)
expr_stmt|;
block|}
name|ScheduleKeyDeletionResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|kmsClient
operator|.
name|scheduleKeyDeletion
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
literal|"Schedule Key Deletion command returned the error code {}"
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
argument_list|)
expr_stmt|;
block|}
DECL|method|describeKey (AWSKMS kmsClient, Exchange exchange)
specifier|private
name|void
name|describeKey
parameter_list|(
name|AWSKMS
name|kmsClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|DescribeKeyRequest
name|request
init|=
operator|new
name|DescribeKeyRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|KEY_ID
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|keyId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|KEY_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|withKeyId
argument_list|(
name|keyId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Key Id must be specified"
argument_list|)
throw|;
block|}
name|DescribeKeyResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|kmsClient
operator|.
name|describeKey
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
literal|"Describe Key command returned the error code {}"
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
argument_list|)
expr_stmt|;
block|}
DECL|method|enableKey (AWSKMS kmsClient, Exchange exchange)
specifier|private
name|void
name|enableKey
parameter_list|(
name|AWSKMS
name|kmsClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|EnableKeyRequest
name|request
init|=
operator|new
name|EnableKeyRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|KEY_ID
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|keyId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KMSConstants
operator|.
name|KEY_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|withKeyId
argument_list|(
name|keyId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Key Id must be specified"
argument_list|)
throw|;
block|}
name|EnableKeyResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|kmsClient
operator|.
name|enableKey
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
literal|"Enable Key command returned the error code {}"
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

