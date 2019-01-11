begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ejb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ejb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

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
name|javax
operator|.
name|naming
operator|.
name|NamingException
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
name|Endpoint
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
name|component
operator|.
name|bean
operator|.
name|BeanComponent
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
name|component
operator|.
name|bean
operator|.
name|BeanHolder
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
name|annotations
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  * EJB component to invoke EJBs like the {@link org.apache.camel.component.bean.BeanComponent}.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"ejb"
argument_list|)
DECL|class|EjbComponent
specifier|public
class|class
name|EjbComponent
extends|extends
name|BeanComponent
block|{
DECL|field|context
specifier|private
name|Context
name|context
decl_stmt|;
DECL|field|properties
specifier|private
name|Properties
name|properties
decl_stmt|;
DECL|method|EjbComponent ()
specifier|public
name|EjbComponent
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|EjbEndpoint
name|answer
init|=
operator|new
name|EjbEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setBeanName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
comment|// plugin registry to lookup in jndi for the EJBs
name|Registry
name|registry
init|=
operator|new
name|JndiRegistry
argument_list|(
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// and register the bean as a holder on the endpoint
name|BeanHolder
name|holder
init|=
operator|new
name|EjbRegistryBean
argument_list|(
name|registry
argument_list|,
name|getCamelContext
argument_list|()
argument_list|,
name|answer
operator|.
name|getBeanName
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setBeanHolder
argument_list|(
name|holder
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getContext ()
specifier|public
name|Context
name|getContext
parameter_list|()
throws|throws
name|NamingException
block|{
return|return
name|context
return|;
block|}
comment|/**      * The Context to use for looking up the EJBs      */
DECL|method|setContext (Context context)
specifier|public
name|void
name|setContext
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/**      * Properties for creating javax.naming.Context if a context has not been configured.      */
DECL|method|setProperties (Properties properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
operator|&&
name|properties
operator|!=
literal|null
condition|)
block|{
name|context
operator|=
operator|new
name|InitialContext
argument_list|(
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

