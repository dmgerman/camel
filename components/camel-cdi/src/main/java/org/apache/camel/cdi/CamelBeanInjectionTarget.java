begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|context
operator|.
name|spi
operator|.
name|CreationalContext
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
name|InjectionException
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
name|BeanManager
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
name|InjectionTarget
import|;
end_import

begin_class
DECL|class|CamelBeanInjectionTarget
specifier|final
class|class
name|CamelBeanInjectionTarget
parameter_list|<
name|T
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
DECL|field|delegate
specifier|private
specifier|final
name|InjectionTarget
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|CdiCamelBeanPostProcessor
name|processor
decl_stmt|;
DECL|method|CamelBeanInjectionTarget (InjectionTarget<T> delegate, BeanManager manager)
name|CamelBeanInjectionTarget
parameter_list|(
name|InjectionTarget
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|BeanManager
name|manager
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|processor
operator|=
operator|new
name|CdiCamelBeanPostProcessor
argument_list|(
name|manager
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|inject (T instance, CreationalContext<T> ctx)
specifier|public
name|void
name|inject
parameter_list|(
name|T
name|instance
parameter_list|,
name|CreationalContext
argument_list|<
name|T
argument_list|>
name|ctx
parameter_list|)
block|{
name|super
operator|.
name|inject
argument_list|(
name|instance
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
try|try
block|{
comment|// TODO: see how to retrieve the bean name
name|processor
operator|.
name|postProcessBeforeInitialization
argument_list|(
name|instance
argument_list|,
name|instance
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|processor
operator|.
name|postProcessAfterInitialization
argument_list|(
name|instance
argument_list|,
name|instance
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InjectionException
argument_list|(
literal|"Camel annotations post processing of ["
operator|+
name|delegate
operator|+
literal|"] failed!"
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

