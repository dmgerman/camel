begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|spi
operator|.
name|Injector
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A simple implementation of {@link Injector} which just uses reflection to  * instantiate new objects using their zero argument constructor. For more  * complex implementations try the Spring or CDI implementations.  */
end_comment

begin_class
DECL|class|ReflectionInjector
specifier|public
class|class
name|ReflectionInjector
implements|implements
name|Injector
block|{
annotation|@
name|Override
DECL|method|newInstance (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|newInstance
argument_list|(
name|type
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance (Class<T> type, boolean postProcessBean)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|boolean
name|postProcessBean
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
return|;
comment|// does not support post processing
block|}
annotation|@
name|Override
DECL|method|supportsAutoWiring ()
specifier|public
name|boolean
name|supportsAutoWiring
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

