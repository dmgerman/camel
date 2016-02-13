begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sdb
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
name|sdb
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
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * A Producer which sends messages to the Amazon SimpleDB Service  *<a href="http://aws.amazon.com/simpledb/">AWS SDB</a>  */
end_comment

begin_class
DECL|class|SdbProducer
specifier|public
class|class
name|SdbProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|sdbProducerToString
specifier|private
specifier|transient
name|String
name|sdbProducerToString
decl_stmt|;
DECL|method|SdbProducer (Endpoint endpoint)
specifier|public
name|SdbProducer
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
name|BatchDeleteAttributes
case|:
operator|new
name|BatchDeleteAttributesCommand
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSdbClient
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|exchange
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
break|break;
case|case
name|BatchPutAttributes
case|:
operator|new
name|BatchPutAttributesCommand
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSdbClient
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|exchange
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
break|break;
case|case
name|DeleteAttributes
case|:
operator|new
name|DeleteAttributesCommand
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSdbClient
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|exchange
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
break|break;
case|case
name|DeleteDomain
case|:
operator|new
name|DeleteDomainCommand
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSdbClient
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|exchange
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
break|break;
case|case
name|DomainMetadata
case|:
operator|new
name|DomainMetadataCommand
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSdbClient
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|exchange
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
break|break;
case|case
name|GetAttributes
case|:
operator|new
name|GetAttributesCommand
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSdbClient
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|exchange
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
break|break;
case|case
name|ListDomains
case|:
operator|new
name|ListDomainsCommand
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSdbClient
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|exchange
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
break|break;
case|case
name|PutAttributes
case|:
operator|new
name|PutAttributesCommand
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSdbClient
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|exchange
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
break|break;
case|case
name|Select
case|:
operator|new
name|SelectCommand
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSdbClient
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|exchange
argument_list|)
operator|.
name|execute
argument_list|()
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
name|SdbOperations
name|determineOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|SdbOperations
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
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
name|SdbConfiguration
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
name|sdbProducerToString
operator|==
literal|null
condition|)
block|{
name|sdbProducerToString
operator|=
literal|"SdbProducer["
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
name|sdbProducerToString
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SdbEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SdbEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

