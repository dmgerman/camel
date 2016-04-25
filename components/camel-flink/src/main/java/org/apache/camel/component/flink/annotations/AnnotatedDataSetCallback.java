begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flink.annotations
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flink
operator|.
name|annotations
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
name|InvocationTargetException
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
name|Arrays
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
name|flink
operator|.
name|api
operator|.
name|java
operator|.
name|DataSet
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|findMethodsWithAnnotation
import|;
end_import

begin_comment
comment|/**  * Provides facade for working with annotated DataSet callbacks i.e. POJO classes with an appropriate annotations on  * selected methods.  */
end_comment

begin_class
DECL|class|AnnotatedDataSetCallback
specifier|public
class|class
name|AnnotatedDataSetCallback
implements|implements
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flink
operator|.
name|DataSetCallback
block|{
DECL|field|objectWithCallback
specifier|private
specifier|final
name|Object
name|objectWithCallback
decl_stmt|;
DECL|field|dataSetCallbacks
specifier|private
specifier|final
name|List
argument_list|<
name|Method
argument_list|>
name|dataSetCallbacks
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|AnnotatedDataSetCallback (Object objectWithCallback, CamelContext camelContext)
specifier|public
name|AnnotatedDataSetCallback
parameter_list|(
name|Object
name|objectWithCallback
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|objectWithCallback
operator|=
name|objectWithCallback
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|dataSetCallbacks
operator|=
name|findMethodsWithAnnotation
argument_list|(
name|objectWithCallback
operator|.
name|getClass
argument_list|()
argument_list|,
name|DataSetCallback
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|dataSetCallbacks
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Can't find methods annotated with @DataSetCallback"
argument_list|)
throw|;
block|}
block|}
DECL|method|AnnotatedDataSetCallback (Object objectWithCallback)
specifier|public
name|AnnotatedDataSetCallback
parameter_list|(
name|Object
name|objectWithCallback
parameter_list|)
block|{
name|this
argument_list|(
name|objectWithCallback
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onDataSet (DataSet ds, Object... payloads)
specifier|public
name|Object
name|onDataSet
parameter_list|(
name|DataSet
name|ds
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
try|try
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|payloads
operator|.
name|length
operator|+
literal|1
argument_list|)
decl_stmt|;
name|arguments
operator|.
name|add
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|arguments
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|payloads
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|arguments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|==
literal|null
condition|)
block|{
name|arguments
operator|.
name|remove
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|Method
name|callbackMethod
init|=
name|dataSetCallbacks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|callbackMethod
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|arguments
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|arguments
operator|.
name|set
argument_list|(
name|i
argument_list|,
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|callbackMethod
operator|.
name|getParameterTypes
argument_list|()
index|[
name|i
index|]
argument_list|,
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|callbackMethod
operator|.
name|invoke
argument_list|(
name|objectWithCallback
argument_list|,
name|arguments
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
name|arguments
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

