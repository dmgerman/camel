begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ddb
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
name|ddb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|dynamodbv2
operator|.
name|AmazonDynamoDB
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
name|dynamodbv2
operator|.
name|model
operator|.
name|AttributeValue
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
name|dynamodbv2
operator|.
name|model
operator|.
name|ExpectedAttributeValue
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

begin_class
DECL|class|AbstractDdbCommand
specifier|public
specifier|abstract
class|class
name|AbstractDdbCommand
block|{
DECL|field|configuration
specifier|protected
name|DdbConfiguration
name|configuration
decl_stmt|;
DECL|field|exchange
specifier|protected
name|Exchange
name|exchange
decl_stmt|;
DECL|field|ddbClient
specifier|protected
name|AmazonDynamoDB
name|ddbClient
decl_stmt|;
DECL|method|AbstractDdbCommand (AmazonDynamoDB ddbClient, DdbConfiguration configuration, Exchange exchange)
specifier|public
name|AbstractDdbCommand
parameter_list|(
name|AmazonDynamoDB
name|ddbClient
parameter_list|,
name|DdbConfiguration
name|configuration
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|ddbClient
operator|=
name|ddbClient
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
DECL|method|determineTableName ()
specifier|protected
name|String
name|determineTableName
parameter_list|()
block|{
name|String
name|tableName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DdbConstants
operator|.
name|TABLE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|tableName
operator|!=
literal|null
condition|?
name|tableName
else|:
name|configuration
operator|.
name|getTableName
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|determineUpdateCondition ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|ExpectedAttributeValue
argument_list|>
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
name|DdbConstants
operator|.
name|UPDATE_CONDITION
argument_list|,
name|Map
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|determineItem ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
name|determineItem
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
name|DdbConstants
operator|.
name|ITEM
argument_list|,
name|Map
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|determineReturnValues ()
specifier|protected
name|String
name|determineReturnValues
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
name|DdbConstants
operator|.
name|RETURN_VALUES
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|addAttributesToResult (Map<String, AttributeValue> attributes)
specifier|protected
name|void
name|addAttributesToResult
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
name|attributes
parameter_list|)
block|{
name|Message
name|msg
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|DdbConstants
operator|.
name|ATTRIBUTES
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
block|}
DECL|method|addToResults (Map<String, Object> map)
specifier|protected
name|void
name|addToResults
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|Message
name|msg
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|en
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|msg
operator|.
name|setHeader
argument_list|(
name|en
operator|.
name|getKey
argument_list|()
argument_list|,
name|en
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|determineKey ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
name|determineKey
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
name|DdbConstants
operator|.
name|KEY
argument_list|,
name|Map
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|determineAttributeNames ()
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|determineAttributeNames
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
name|DdbConstants
operator|.
name|ATTRIBUTE_NAMES
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|determineConsistentRead ()
specifier|protected
name|Boolean
name|determineConsistentRead
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
name|DdbConstants
operator|.
name|CONSISTENT_READ
argument_list|,
name|configuration
operator|.
name|isConsistentRead
argument_list|()
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit
