begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
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
DECL|class|BodyConverter
specifier|public
specifier|final
class|class
name|BodyConverter
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Function
argument_list|<
name|Exchange
argument_list|,
name|T
argument_list|>
block|{
DECL|field|CACHE
specifier|private
specifier|static
specifier|final
name|ConcurrentMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|BodyConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|CACHE
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|type
decl_stmt|;
DECL|method|BodyConverter (Class<T> type)
name|BodyConverter
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (Exchange exchange)
specifier|public
name|T
name|apply
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|T
name|answer
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|answer
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|forType (Class<C> type)
specifier|public
specifier|static
parameter_list|<
name|C
parameter_list|>
name|BodyConverter
argument_list|<
name|C
argument_list|>
name|forType
parameter_list|(
name|Class
argument_list|<
name|C
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|BodyConverter
operator|.
name|class
operator|.
name|cast
argument_list|(
name|CACHE
operator|.
name|computeIfAbsent
argument_list|(
name|type
argument_list|,
name|BodyConverter
operator|::
operator|new
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

