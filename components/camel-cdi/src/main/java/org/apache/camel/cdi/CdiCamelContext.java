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
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
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
name|Instance
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|impl
operator|.
name|DefaultCamelContext
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
name|spi
operator|.
name|Registry
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * CDI {@link org.apache.camel.CamelContext} class.  */
end_comment

begin_class
DECL|class|CdiCamelContext
specifier|public
class|class
name|CdiCamelContext
extends|extends
name|DefaultCamelContext
block|{
DECL|method|CdiCamelContext ()
specifier|public
name|CdiCamelContext
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|CdiBeanRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|setInjector
argument_list|(
operator|new
name|CdiInjector
argument_list|(
name|getInjector
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|setRegistry (Instance<Registry> instance)
specifier|public
name|void
name|setRegistry
parameter_list|(
name|Instance
argument_list|<
name|Registry
argument_list|>
name|instance
parameter_list|)
block|{
if|if
condition|(
name|isSingular
argument_list|(
name|instance
argument_list|)
condition|)
block|{
name|setRegistry
argument_list|(
name|instance
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Inject
DECL|method|setInjector (Instance<Injector> instance)
specifier|public
name|void
name|setInjector
parameter_list|(
name|Instance
argument_list|<
name|Injector
argument_list|>
name|instance
parameter_list|)
block|{
if|if
condition|(
name|isSingular
argument_list|(
name|instance
argument_list|)
condition|)
block|{
name|setInjector
argument_list|(
name|instance
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isSingular (Instance<T> instance)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|boolean
name|isSingular
parameter_list|(
name|Instance
argument_list|<
name|T
argument_list|>
name|instance
parameter_list|)
block|{
return|return
operator|!
name|instance
operator|.
name|isUnsatisfied
argument_list|()
operator|&&
operator|!
name|instance
operator|.
name|isAmbiguous
argument_list|()
return|;
block|}
annotation|@
name|PostConstruct
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|PreDestroy
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

