begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.directvm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|directvm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|support
operator|.
name|ExpressionAdapter
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
name|AntPathMatcher
import|;
end_import

begin_comment
comment|/**  * The expression to select direct-vm consumers based on ant-like path pattern matching.  */
end_comment

begin_class
DECL|class|DirectVmConsumerExpression
specifier|public
class|class
name|DirectVmConsumerExpression
extends|extends
name|ExpressionAdapter
block|{
DECL|field|matcher
specifier|private
specifier|final
name|AntPathMatcher
name|matcher
decl_stmt|;
DECL|field|pattern
specifier|private
specifier|final
name|String
name|pattern
decl_stmt|;
DECL|method|DirectVmConsumerExpression (String pattern)
specifier|public
name|DirectVmConsumerExpression
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|matcher
operator|=
operator|new
name|AntPathMatcher
argument_list|()
expr_stmt|;
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|DirectVmComponent
operator|.
name|getConsumerEndpoints
argument_list|()
control|)
block|{
if|if
condition|(
name|matcher
operator|.
name|match
argument_list|(
name|pattern
argument_list|,
name|endpoint
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
condition|)
block|{
name|endpoints
operator|.
name|add
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|endpoints
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
return|return
literal|"DirectVmConsumerExpression["
operator|+
name|pattern
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

