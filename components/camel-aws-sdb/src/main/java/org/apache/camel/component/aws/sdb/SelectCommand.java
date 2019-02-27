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
name|SelectRequest
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
name|SelectResult
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
DECL|class|SelectCommand
specifier|public
class|class
name|SelectCommand
extends|extends
name|AbstractSdbCommand
block|{
DECL|method|SelectCommand (AmazonSimpleDB sdbClient, SdbConfiguration configuration, Exchange exchange)
specifier|public
name|SelectCommand
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
name|SelectRequest
name|request
init|=
operator|new
name|SelectRequest
argument_list|()
operator|.
name|withSelectExpression
argument_list|(
name|determineSelectExpression
argument_list|()
argument_list|)
operator|.
name|withConsistentRead
argument_list|(
name|determineConsistentRead
argument_list|()
argument_list|)
operator|.
name|withNextToken
argument_list|(
name|determineNextToken
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
name|SelectResult
name|result
init|=
name|this
operator|.
name|sdbClient
operator|.
name|select
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Received result [{}]"
argument_list|,
name|result
argument_list|)
expr_stmt|;
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
name|SdbConstants
operator|.
name|ITEMS
argument_list|,
name|result
operator|.
name|getItems
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|NEXT_TOKEN
argument_list|,
name|result
operator|.
name|getNextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|determineSelectExpression ()
specifier|protected
name|String
name|determineSelectExpression
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
name|SELECT_EXPRESSION
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
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

