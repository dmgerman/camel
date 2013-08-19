begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|List
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
name|CamelContext
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
name|component
operator|.
name|bean
operator|.
name|ParameterInfo
import|;
end_import

begin_comment
comment|/**  * Method information about the POJO method to call when using the {@link AggregationStrategyBeanAdapter}.  */
end_comment

begin_class
DECL|class|AggregationStrategyMethodInfo
specifier|public
class|class
name|AggregationStrategyMethodInfo
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|method
specifier|private
specifier|final
name|Method
name|method
decl_stmt|;
DECL|field|oldParameters
specifier|private
specifier|final
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|oldParameters
decl_stmt|;
DECL|field|newParameters
specifier|private
specifier|final
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|newParameters
decl_stmt|;
DECL|method|AggregationStrategyMethodInfo (CamelContext camelContext, Class<?> type, Method method, List<ParameterInfo> oldParameters, List<ParameterInfo> newParameters)
specifier|public
name|AggregationStrategyMethodInfo
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Method
name|method
parameter_list|,
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|oldParameters
parameter_list|,
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|newParameters
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|oldParameters
operator|=
name|oldParameters
expr_stmt|;
name|this
operator|.
name|newParameters
operator|=
name|newParameters
expr_stmt|;
block|}
DECL|method|invoke (Object pojo, Exchange oldExchange, Exchange newExchange)
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// evaluate the parameters
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|oldParameters
operator|.
name|size
argument_list|()
operator|+
name|newParameters
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ParameterInfo
name|info
range|:
name|oldParameters
control|)
block|{
if|if
condition|(
name|oldExchange
operator|!=
literal|null
condition|)
block|{
name|Object
name|value
init|=
name|info
operator|.
name|getExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|oldExchange
argument_list|,
name|info
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use a null value as oldExchange is null
name|list
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|ParameterInfo
name|info
range|:
name|newParameters
control|)
block|{
if|if
condition|(
name|newExchange
operator|!=
literal|null
condition|)
block|{
name|Object
name|value
init|=
name|info
operator|.
name|getExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|newExchange
argument_list|,
name|info
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use a null value as newExchange is null
name|list
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
name|Object
index|[]
name|args
init|=
name|list
operator|.
name|toArray
argument_list|()
decl_stmt|;
return|return
name|method
operator|.
name|invoke
argument_list|(
name|pojo
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
end_class

end_unit

