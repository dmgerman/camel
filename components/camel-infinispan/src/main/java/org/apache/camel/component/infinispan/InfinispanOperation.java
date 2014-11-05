begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
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
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCache
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
DECL|class|InfinispanOperation
specifier|public
class|class
name|InfinispanOperation
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|InfinispanOperation
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cache
specifier|private
specifier|final
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|InfinispanConfiguration
name|configuration
decl_stmt|;
DECL|method|InfinispanOperation (BasicCache<Object, Object> cache, InfinispanConfiguration configuration)
specifier|public
name|InfinispanOperation
parameter_list|(
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|,
name|InfinispanConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
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
block|{
name|Operation
name|operation
init|=
name|getOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|operation
operator|.
name|execute
argument_list|(
name|cache
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getOperation (Exchange exchange)
specifier|private
name|Operation
name|getOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|OPERATION
argument_list|,
name|String
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
if|if
condition|(
name|configuration
operator|.
name|getCommand
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|operation
operator|=
name|InfinispanConstants
operator|.
name|OPERATION
operator|+
name|configuration
operator|.
name|getCommand
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|operation
operator|=
name|InfinispanConstants
operator|.
name|PUT
expr_stmt|;
block|}
block|}
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Operation: [{}]"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|Operation
operator|.
name|valueOf
argument_list|(
name|operation
operator|.
name|substring
argument_list|(
name|InfinispanConstants
operator|.
name|OPERATION
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|toUpperCase
argument_list|()
argument_list|)
return|;
block|}
DECL|enum|Operation
enum|enum
name|Operation
block|{
DECL|enumConstant|PUT
name|PUT
block|{
annotation|@
name|Override
name|void
name|execute
parameter_list|(
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|result
init|=
name|cache
operator|.
name|put
argument_list|(
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getValue
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|setResult
argument_list|(
name|result
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|enumConstant|GET
block|}
block|,
name|GET
block|{
annotation|@
name|Override
name|void
name|execute
parameter_list|(
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|result
init|=
name|cache
operator|.
name|get
argument_list|(
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|setResult
argument_list|(
name|result
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|enumConstant|REMOVE
block|}
block|,
name|REMOVE
block|{
annotation|@
name|Override
name|void
name|execute
parameter_list|(
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|result
init|=
name|cache
operator|.
name|remove
argument_list|(
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|setResult
argument_list|(
name|result
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|enumConstant|CLEAR
block|}
block|,
name|CLEAR
block|{
annotation|@
name|Override
name|void
name|execute
parameter_list|(
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|;
DECL|method|setResult (Object result, Exchange exchange)
name|void
name|setResult
parameter_list|(
name|Object
name|result
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|RESULT
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|getKey (Exchange exchange)
name|Object
name|getKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|)
return|;
block|}
DECL|method|getValue (Exchange exchange)
name|Object
name|getValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|VALUE
argument_list|)
return|;
block|}
DECL|method|execute (BasicCache<Object, Object> cache, Exchange exchange)
specifier|abstract
name|void
name|execute
parameter_list|(
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

