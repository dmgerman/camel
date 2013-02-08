begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servletlistener
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servletlistener
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
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
name|JndiRegistry
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

begin_comment
comment|/**  * An implementation of {@link CamelServletContextListener} that uses the {@link JndiRegistry}  * as its {@link Registry}.  */
end_comment

begin_class
DECL|class|JndiCamelServletContextListener
specifier|public
class|class
name|JndiCamelServletContextListener
extends|extends
name|CamelServletContextListener
argument_list|<
name|JndiRegistry
argument_list|>
block|{
DECL|field|jndiContext
specifier|private
name|Context
name|jndiContext
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|jndiContext
operator|=
operator|new
name|InitialContext
argument_list|()
expr_stmt|;
return|return
operator|new
name|JndiRegistry
argument_list|(
name|jndiContext
argument_list|)
return|;
block|}
DECL|method|getJndiContext ()
specifier|public
name|Context
name|getJndiContext
parameter_list|()
block|{
return|return
name|jndiContext
return|;
block|}
block|}
end_class

end_unit

