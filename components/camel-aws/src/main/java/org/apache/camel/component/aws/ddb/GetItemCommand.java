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
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|dynamodb
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
name|dynamodb
operator|.
name|model
operator|.
name|GetItemRequest
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
name|dynamodb
operator|.
name|model
operator|.
name|GetItemResult
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
DECL|class|GetItemCommand
specifier|public
class|class
name|GetItemCommand
extends|extends
name|AbstractDdbCommand
block|{
DECL|method|GetItemCommand (AmazonDynamoDB ddbClient, DdbConfiguration configuration, Exchange exchange)
specifier|public
name|GetItemCommand
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
name|GetItemResult
name|result
init|=
name|ddbClient
operator|.
name|getItem
argument_list|(
operator|new
name|GetItemRequest
argument_list|()
operator|.
name|withKey
argument_list|(
name|determineKey
argument_list|()
argument_list|)
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
argument_list|)
decl_stmt|;
name|addAttributesToResult
argument_list|(
name|result
operator|.
name|getItem
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

