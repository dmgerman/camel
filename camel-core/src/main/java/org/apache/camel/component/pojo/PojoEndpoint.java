begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pojo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pojo
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
name|camel
operator|.
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_comment
comment|/**  * Represents a pojo endpoint that uses reflection  * to send messages around.  *  * @version $Revision: 519973 $  */
end_comment

begin_class
DECL|class|PojoEndpoint
specifier|public
class|class
name|PojoEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|PojoExchange
argument_list|>
block|{
DECL|field|pojo
specifier|private
specifier|final
name|Object
name|pojo
decl_stmt|;
DECL|field|component
specifier|private
specifier|final
name|PojoComponent
name|component
decl_stmt|;
DECL|method|PojoEndpoint (String uri, CamelContext container, PojoComponent component, Object pojo)
specifier|public
name|PojoEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContext
name|container
parameter_list|,
name|PojoComponent
name|component
parameter_list|,
name|Object
name|pojo
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|container
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|pojo
operator|=
name|pojo
expr_stmt|;
block|}
comment|/** 	 *  This causes us to invoke the endpoint Pojo using reflection. 	 */
DECL|method|onExchange (PojoExchange exchange)
specifier|public
name|void
name|onExchange
parameter_list|(
name|PojoExchange
name|exchange
parameter_list|)
block|{
name|PojoInvocation
name|invocation
init|=
name|exchange
operator|.
name|getInvocation
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|response
init|=
name|invocation
operator|.
name|getMethod
argument_list|()
operator|.
name|invoke
argument_list|(
name|pojo
argument_list|,
name|invocation
operator|.
name|getArgs
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createExchange ()
specifier|public
name|PojoExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|PojoExchange
argument_list|(
name|getContext
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doActivate ()
specifier|protected
name|void
name|doActivate
parameter_list|()
block|{
name|component
operator|.
name|registerActivation
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doDeactivate ()
specifier|protected
name|void
name|doDeactivate
parameter_list|()
block|{
name|component
operator|.
name|unregisterActivation
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Proxy object that can be used to deliver inbound PojoExchanges.      *       * @param interfaces      * @return      */
DECL|method|createInboundProxy (Class interfaces[])
specifier|public
name|Object
name|createInboundProxy
parameter_list|(
name|Class
name|interfaces
index|[]
parameter_list|)
block|{
specifier|final
name|PojoEndpoint
name|endpoint
init|=
name|component
operator|.
name|lookupActivation
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The endpoint has not been activated yet: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
return|return
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|pojo
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|interfaces
argument_list|,
operator|new
name|InvocationHandler
argument_list|()
block|{
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
operator|!
name|activated
operator|.
name|get
argument_list|()
condition|)
block|{
name|PojoInvocation
name|invocation
init|=
operator|new
name|PojoInvocation
argument_list|(
name|proxy
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
decl_stmt|;
name|PojoExchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInvocation
argument_list|(
name|invocation
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getInboundProcessor
argument_list|()
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Throwable
name|fault
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|fault
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvocationTargetException
argument_list|(
name|fault
argument_list|)
throw|;
block|}
return|return
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The endpoint is not active: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

