begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spark
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
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
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|AbstractJavaRDDLike
import|;
end_import

begin_class
DECL|class|TypedRddCallback
specifier|public
specifier|abstract
class|class
name|TypedRddCallback
parameter_list|<
name|T
parameter_list|>
implements|implements
name|RddCallback
argument_list|<
name|T
argument_list|>
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|payloadsTypes
specifier|private
specifier|final
name|Class
index|[]
name|payloadsTypes
decl_stmt|;
DECL|method|TypedRddCallback (CamelContext camelContext, Class[] payloadsTypes)
specifier|public
name|TypedRddCallback
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Class
index|[]
name|payloadsTypes
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
name|payloadsTypes
operator|=
name|payloadsTypes
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onRdd (AbstractJavaRDDLike rdd, Object... payloads)
specifier|public
name|T
name|onRdd
parameter_list|(
name|AbstractJavaRDDLike
name|rdd
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|payloads
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|payloads
index|[
name|i
index|]
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|payloadsTypes
index|[
name|i
index|]
argument_list|,
name|payloads
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|doOnRdd
argument_list|(
name|rdd
argument_list|,
name|payloads
argument_list|)
return|;
block|}
DECL|method|doOnRdd (AbstractJavaRDDLike rdd, Object... payloads)
specifier|public
specifier|abstract
name|T
name|doOnRdd
parameter_list|(
name|AbstractJavaRDDLike
name|rdd
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
function_decl|;
block|}
end_class

end_unit

