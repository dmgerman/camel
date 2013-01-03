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
name|servlet
operator|.
name|ServletContext
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

begin_comment
comment|/**  * A servlet based {@link org.apache.camel.CamelContext}.  */
end_comment

begin_class
DECL|class|ServletCamelContext
specifier|public
class|class
name|ServletCamelContext
extends|extends
name|DefaultCamelContext
block|{
DECL|field|jndiContext
specifier|private
specifier|final
name|Context
name|jndiContext
decl_stmt|;
DECL|field|servletContext
specifier|private
specifier|final
name|ServletContext
name|servletContext
decl_stmt|;
DECL|method|ServletCamelContext (Context jndiContext, ServletContext servletContext)
specifier|public
name|ServletCamelContext
parameter_list|(
name|Context
name|jndiContext
parameter_list|,
name|ServletContext
name|servletContext
parameter_list|)
block|{
name|super
argument_list|(
name|jndiContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|jndiContext
operator|=
name|jndiContext
expr_stmt|;
name|this
operator|.
name|servletContext
operator|=
name|servletContext
expr_stmt|;
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
DECL|method|getServletContext ()
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
name|servletContext
return|;
block|}
block|}
end_class

end_unit

