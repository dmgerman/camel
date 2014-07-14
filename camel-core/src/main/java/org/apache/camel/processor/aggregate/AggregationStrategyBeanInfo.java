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
name|annotation
operator|.
name|Annotation
import|;
end_import

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
name|Expression
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
name|builder
operator|.
name|ExpressionBuilder
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

begin_comment
comment|/**  * Class information about the POJO method to call when using the {@link AggregationStrategyBeanAdapter}.  */
end_comment

begin_class
DECL|class|AggregationStrategyBeanInfo
specifier|public
class|class
name|AggregationStrategyBeanInfo
block|{
comment|// TODO: We could potential merge this logic into AggregationStrategyMethodInfo and only have 1 class
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AggregationStrategyBeanInfo
operator|.
name|class
argument_list|)
decl_stmt|;
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
DECL|method|AggregationStrategyBeanInfo (CamelContext camelContext, Class<?> type, Method method)
specifier|public
name|AggregationStrategyBeanInfo
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
block|}
DECL|method|createMethodInfo ()
specifier|protected
name|AggregationStrategyMethodInfo
name|createMethodInfo
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|parameterTypes
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating MethodInfo for class: {} method: {} having {} parameters"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|type
block|,
name|method
block|,
name|size
block|}
argument_list|)
expr_stmt|;
block|}
comment|// must have equal number of parameters
if|if
condition|(
name|size
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The method "
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|" must have at least two parameters, has: "
operator|+
name|size
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|size
operator|%
literal|2
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The method "
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|" must have equal number of parameters, has: "
operator|+
name|size
argument_list|)
throw|;
block|}
comment|// must not have annotations as they are not supported (yet)
name|Annotation
index|[]
index|[]
name|parameterAnnotations
init|=
name|method
operator|.
name|getParameterAnnotations
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parameterAnnotations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Annotation
index|[]
name|annotations
init|=
name|parameterAnnotations
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|annotations
operator|.
name|length
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method parameter annotation: "
operator|+
name|annotations
index|[
literal|0
index|]
operator|+
literal|" at index: "
operator|+
name|i
operator|+
literal|" is not supported on method: "
operator|+
name|method
argument_list|)
throw|;
block|}
block|}
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|oldParameters
init|=
operator|new
name|ArrayList
argument_list|<
name|ParameterInfo
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ParameterInfo
argument_list|>
name|newParameters
init|=
operator|new
name|ArrayList
argument_list|<
name|ParameterInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
operator|/
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|oldType
init|=
name|parameterTypes
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|oldParameters
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// the first parameter is the body
name|Expression
name|oldBody
init|=
name|ExpressionBuilder
operator|.
name|mandatoryBodyExpression
argument_list|(
name|oldType
argument_list|)
decl_stmt|;
name|ParameterInfo
name|info
init|=
operator|new
name|ParameterInfo
argument_list|(
name|i
argument_list|,
name|oldType
argument_list|,
literal|null
argument_list|,
name|oldBody
argument_list|)
decl_stmt|;
name|oldParameters
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|oldParameters
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// the 2nd parameter is the headers
name|Expression
name|oldHeaders
init|=
name|ExpressionBuilder
operator|.
name|headersExpression
argument_list|()
decl_stmt|;
name|ParameterInfo
name|info
init|=
operator|new
name|ParameterInfo
argument_list|(
name|i
argument_list|,
name|oldType
argument_list|,
literal|null
argument_list|,
name|oldHeaders
argument_list|)
decl_stmt|;
name|oldParameters
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|oldParameters
operator|.
name|size
argument_list|()
operator|==
literal|2
condition|)
block|{
comment|// the 3rd parameter is the properties
name|Expression
name|oldProperties
init|=
name|ExpressionBuilder
operator|.
name|propertiesExpression
argument_list|()
decl_stmt|;
name|ParameterInfo
name|info
init|=
operator|new
name|ParameterInfo
argument_list|(
name|i
argument_list|,
name|oldType
argument_list|,
literal|null
argument_list|,
name|oldProperties
argument_list|)
decl_stmt|;
name|oldParameters
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
name|size
operator|/
literal|2
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|newType
init|=
name|parameterTypes
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|newParameters
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// the first parameter is the body
name|Expression
name|newBody
init|=
name|ExpressionBuilder
operator|.
name|mandatoryBodyExpression
argument_list|(
name|newType
argument_list|)
decl_stmt|;
name|ParameterInfo
name|info
init|=
operator|new
name|ParameterInfo
argument_list|(
name|i
argument_list|,
name|newType
argument_list|,
literal|null
argument_list|,
name|newBody
argument_list|)
decl_stmt|;
name|newParameters
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|newParameters
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// the 2nd parameter is the headers
name|Expression
name|newHeaders
init|=
name|ExpressionBuilder
operator|.
name|headersExpression
argument_list|()
decl_stmt|;
name|ParameterInfo
name|info
init|=
operator|new
name|ParameterInfo
argument_list|(
name|i
argument_list|,
name|newType
argument_list|,
literal|null
argument_list|,
name|newHeaders
argument_list|)
decl_stmt|;
name|newParameters
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|newParameters
operator|.
name|size
argument_list|()
operator|==
literal|2
condition|)
block|{
comment|// the 3rd parameter is the properties
name|Expression
name|newProperties
init|=
name|ExpressionBuilder
operator|.
name|propertiesExpression
argument_list|()
decl_stmt|;
name|ParameterInfo
name|info
init|=
operator|new
name|ParameterInfo
argument_list|(
name|i
argument_list|,
name|newType
argument_list|,
literal|null
argument_list|,
name|newProperties
argument_list|)
decl_stmt|;
name|newParameters
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|AggregationStrategyMethodInfo
argument_list|(
name|camelContext
argument_list|,
name|type
argument_list|,
name|method
argument_list|,
name|oldParameters
argument_list|,
name|newParameters
argument_list|)
return|;
block|}
block|}
end_class

end_unit

