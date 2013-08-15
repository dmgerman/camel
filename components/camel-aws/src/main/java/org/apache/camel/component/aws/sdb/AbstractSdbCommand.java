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
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|AmazonSimpleDB
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
name|simpledb
operator|.
name|model
operator|.
name|UpdateCondition
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

begin_class
DECL|class|AbstractSdbCommand
specifier|public
specifier|abstract
class|class
name|AbstractSdbCommand
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|sdbClient
specifier|protected
name|AmazonSimpleDB
name|sdbClient
decl_stmt|;
DECL|field|configuration
specifier|protected
name|SdbConfiguration
name|configuration
decl_stmt|;
DECL|field|exchange
specifier|protected
name|Exchange
name|exchange
decl_stmt|;
DECL|method|AbstractSdbCommand (AmazonSimpleDB sdbClient, SdbConfiguration configuration, Exchange exchange)
specifier|public
name|AbstractSdbCommand
parameter_list|(
name|AmazonSimpleDB
name|sdbClient
parameter_list|,
name|SdbConfiguration
name|configuration
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|sdbClient
operator|=
name|sdbClient
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
DECL|method|execute ()
specifier|public
specifier|abstract
name|void
name|execute
parameter_list|()
function_decl|;
DECL|method|getMessageForResponse (Exchange exchange)
specifier|protected
name|Message
name|getMessageForResponse
parameter_list|(
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
DECL|method|determineDomainName ()
specifier|protected
name|String
name|determineDomainName
parameter_list|()
block|{
name|String
name|domainName
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
name|DOMAIN_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|domainName
operator|!=
literal|null
condition|?
name|domainName
else|:
name|configuration
operator|.
name|getDomainName
argument_list|()
return|;
block|}
DECL|method|determineItemName ()
specifier|protected
name|String
name|determineItemName
parameter_list|()
block|{
name|String
name|key
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
name|ITEM_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"AWS SDB Item Name header is missing."
argument_list|)
throw|;
block|}
return|return
name|key
return|;
block|}
DECL|method|determineConsistentRead ()
specifier|protected
name|Boolean
name|determineConsistentRead
parameter_list|()
block|{
name|Boolean
name|consistentRead
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
name|CONSISTENT_READ
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|consistentRead
operator|==
literal|null
condition|)
block|{
name|consistentRead
operator|=
name|this
operator|.
name|configuration
operator|.
name|getConsistentRead
argument_list|()
expr_stmt|;
block|}
return|return
name|consistentRead
return|;
block|}
DECL|method|determineUpdateCondition ()
specifier|protected
name|UpdateCondition
name|determineUpdateCondition
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|UPDATE_CONDITION
argument_list|,
name|UpdateCondition
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|determineNextToken ()
specifier|protected
name|String
name|determineNextToken
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|NEXT_TOKEN
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

