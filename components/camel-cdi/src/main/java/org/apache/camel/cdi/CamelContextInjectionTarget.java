begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionTarget
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Producer
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

begin_class
DECL|class|CamelContextInjectionTarget
specifier|final
class|class
name|CamelContextInjectionTarget
parameter_list|<
name|T
extends|extends
name|CamelContext
parameter_list|>
extends|extends
name|DelegateInjectionTarget
argument_list|<
name|T
argument_list|>
implements|implements
name|InjectionTarget
argument_list|<
name|T
argument_list|>
block|{
DECL|method|CamelContextInjectionTarget (InjectionTarget<T> target, Producer<T> producer)
name|CamelContextInjectionTarget
parameter_list|(
name|InjectionTarget
argument_list|<
name|T
argument_list|>
name|target
parameter_list|,
name|Producer
argument_list|<
name|T
argument_list|>
name|producer
parameter_list|)
block|{
name|super
argument_list|(
name|target
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|preDestroy (T instance)
specifier|public
name|void
name|preDestroy
parameter_list|(
name|T
name|instance
parameter_list|)
block|{
name|super
operator|.
name|preDestroy
argument_list|(
name|instance
argument_list|)
expr_stmt|;
name|super
operator|.
name|dispose
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

