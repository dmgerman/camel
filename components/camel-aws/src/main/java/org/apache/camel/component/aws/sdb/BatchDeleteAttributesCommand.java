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
name|java
operator|.
name|util
operator|.
name|Collection
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
name|BatchDeleteAttributesRequest
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
name|DeletableItem
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
DECL|class|BatchDeleteAttributesCommand
specifier|public
class|class
name|BatchDeleteAttributesCommand
extends|extends
name|AbstractSdbCommand
block|{
DECL|method|BatchDeleteAttributesCommand (AmazonSimpleDB sdbClient, SdbConfiguration configuration, Exchange exchange)
specifier|public
name|BatchDeleteAttributesCommand
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
name|super
argument_list|(
name|sdbClient
argument_list|,
name|configuration
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|BatchDeleteAttributesRequest
name|request
init|=
operator|new
name|BatchDeleteAttributesRequest
argument_list|()
operator|.
name|withDomainName
argument_list|(
name|determineDomainName
argument_list|()
argument_list|)
operator|.
name|withItems
argument_list|(
name|determineDeletableItems
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Sending request [{}] for exchange [{}]..."
argument_list|,
name|request
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|sdbClient
operator|.
name|batchDeleteAttributes
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Request sent"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|determineDeletableItems ()
specifier|protected
name|Collection
argument_list|<
name|DeletableItem
argument_list|>
name|determineDeletableItems
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
name|DELETABLE_ITEMS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

