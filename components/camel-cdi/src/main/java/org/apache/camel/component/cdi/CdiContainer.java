begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|BeanManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|webbeans
operator|.
name|config
operator|.
name|WebBeansContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|webbeans
operator|.
name|spi
operator|.
name|ContainerLifecycle
import|;
end_import

begin_comment
comment|/**  * OpenWebBeans CDI container. It can be used in a Camel standalone project to start  * and stop container. The container exposes a {@link BeanManager} that we can use to instantiate the  * {@link CdiBeanRegistry} used by Camel  */
end_comment

begin_class
DECL|class|CdiContainer
specifier|public
specifier|final
class|class
name|CdiContainer
block|{
DECL|field|lifecycle
specifier|private
specifier|static
name|ContainerLifecycle
name|lifecycle
decl_stmt|;
DECL|method|CdiContainer ()
specifier|private
name|CdiContainer
parameter_list|()
block|{     }
DECL|method|start ()
specifier|public
specifier|static
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|lifecycle
operator|=
name|WebBeansContext
operator|.
name|currentInstance
argument_list|()
operator|.
name|getService
argument_list|(
name|ContainerLifecycle
operator|.
name|class
argument_list|)
expr_stmt|;
name|lifecycle
operator|.
name|startApplication
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|shutdown ()
specifier|public
specifier|static
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|lifecycle
operator|.
name|stopApplication
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|getBeanManager ()
specifier|public
specifier|static
name|BeanManager
name|getBeanManager
parameter_list|()
block|{
return|return
name|lifecycle
operator|.
name|getBeanManager
argument_list|()
return|;
block|}
block|}
end_class

end_unit

