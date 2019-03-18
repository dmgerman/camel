begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|HashMap
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
name|QueryRequest
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
name|QueryResult
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

begin_class
DECL|class|QueryCommand
specifier|public
class|class
name|QueryCommand
extends|extends
name|AbstractDdbCommand
block|{
DECL|method|QueryCommand (AmazonDynamoDB ddbClient, DdbConfiguration configuration, Exchange exchange)
specifier|public
name|QueryCommand
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
name|super
argument_list|(
name|ddbClient
argument_list|,
name|configuration
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|QueryRequest
name|query
init|=
operator|new
name|QueryRequest
argument_list|()
operator|.
name|withTableName
argument_list|(
name|determineTableName
argument_list|()
argument_list|)
operator|.
name|withAttributesToGet
argument_list|(
name|determineAttributeNames
argument_list|()
argument_list|)
operator|.
name|withConsistentRead
argument_list|(
name|determineConsistentRead
argument_list|()
argument_list|)
operator|.
name|withExclusiveStartKey
argument_list|(
name|determineStartKey
argument_list|()
argument_list|)
operator|.
name|withKeyConditions
argument_list|(
name|determineKeyConditions
argument_list|()
argument_list|)
operator|.
name|withExclusiveStartKey
argument_list|(
name|determineStartKey
argument_list|()
argument_list|)
operator|.
name|withLimit
argument_list|(
name|determineLimit
argument_list|()
argument_list|)
operator|.
name|withScanIndexForward
argument_list|(
name|determineScanIndexForward
argument_list|()
argument_list|)
decl_stmt|;
comment|// Check if we have set an Index Name
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DdbConstants
operator|.
name|INDEX_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|withIndexName
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DdbConstants
operator|.
name|INDEX_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|QueryResult
name|result
init|=
name|ddbClient
operator|.
name|query
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|Map
name|tmp
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|tmp
operator|.
name|put
argument_list|(
name|DdbConstants
operator|.
name|ITEMS
argument_list|,
name|result
operator|.
name|getItems
argument_list|()
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|put
argument_list|(
name|DdbConstants
operator|.
name|LAST_EVALUATED_KEY
argument_list|,
name|result
operator|.
name|getLastEvaluatedKey
argument_list|()
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|put
argument_list|(
name|DdbConstants
operator|.
name|CONSUMED_CAPACITY
argument_list|,
name|result
operator|.
name|getConsumedCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|put
argument_list|(
name|DdbConstants
operator|.
name|COUNT
argument_list|,
name|result
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|addToResults
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
block|}
DECL|method|determineStartKey ()
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
name|determineStartKey
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
name|START_KEY
argument_list|,
name|Map
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|determineScanIndexForward ()
specifier|private
name|Boolean
name|determineScanIndexForward
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
name|SCAN_INDEX_FORWARD
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|determineKeyConditions ()
specifier|private
name|Map
name|determineKeyConditions
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
name|KEY_CONDITIONS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|determineLimit ()
specifier|private
name|Integer
name|determineLimit
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
name|LIMIT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

