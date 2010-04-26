begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

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
name|Producer
import|;
end_import

begin_comment
comment|/**  * A helper class for creating proxies which delegate to Camel  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ProxyHelper
specifier|public
specifier|final
class|class
name|ProxyHelper
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ProxyHelper ()
specifier|private
name|ProxyHelper
parameter_list|()
block|{     }
comment|/**      * Creates a Proxy which sends PojoExchange to the endpoint.      */
DECL|method|createProxyObject (Endpoint endpoint, Producer producer, ClassLoader classLoader, Class[] interfaces, MethodInfoCache methodCache)
specifier|public
specifier|static
name|Object
name|createProxyObject
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|,
name|Class
index|[]
name|interfaces
parameter_list|,
name|MethodInfoCache
name|methodCache
parameter_list|)
block|{
return|return
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|classLoader
argument_list|,
name|interfaces
operator|.
name|clone
argument_list|()
argument_list|,
operator|new
name|CamelInvocationHandler
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|,
name|methodCache
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a Proxy which sends PojoExchange to the endpoint.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createProxy (Endpoint endpoint, ClassLoader cl, Class[] interfaces, MethodInfoCache methodCache)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|createProxy
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ClassLoader
name|cl
parameter_list|,
name|Class
index|[]
name|interfaces
parameter_list|,
name|MethodInfoCache
name|methodCache
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|T
operator|)
name|createProxyObject
argument_list|(
name|endpoint
argument_list|,
name|endpoint
operator|.
name|createProducer
argument_list|()
argument_list|,
name|cl
argument_list|,
name|interfaces
argument_list|,
name|methodCache
argument_list|)
return|;
block|}
comment|/**      * Creates a Proxy which sends PojoExchange to the endpoint.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createProxy (Endpoint endpoint, ClassLoader cl, Class<T>... interfaceClasses)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|createProxy
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ClassLoader
name|cl
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
modifier|...
name|interfaceClasses
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|T
operator|)
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|cl
argument_list|,
name|interfaceClasses
argument_list|,
name|createMethodInfoCache
argument_list|(
name|endpoint
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a Proxy which sends PojoExchange to the endpoint.      */
DECL|method|createProxy (Endpoint endpoint, Class<T>... interfaceClasses)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|createProxy
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
modifier|...
name|interfaceClasses
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|T
operator|)
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|getClassLoader
argument_list|(
name|interfaceClasses
argument_list|)
argument_list|,
name|interfaceClasses
argument_list|)
return|;
block|}
comment|/**      * Creates a Proxy which sends PojoExchange to the endpoint.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createProxy (Endpoint endpoint, Producer producer, Class<T>... interfaceClasses)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|createProxy
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
modifier|...
name|interfaceClasses
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|T
operator|)
name|createProxyObject
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|,
name|getClassLoader
argument_list|(
name|interfaceClasses
argument_list|)
argument_list|,
name|interfaceClasses
argument_list|,
name|createMethodInfoCache
argument_list|(
name|endpoint
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns the class loader of the first interface or throws {@link IllegalArgumentException} if there are no interfaces specified      */
DECL|method|getClassLoader (Class<?>.... interfaces)
specifier|protected
specifier|static
name|ClassLoader
name|getClassLoader
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|interfaces
parameter_list|)
block|{
if|if
condition|(
name|interfaces
operator|==
literal|null
operator|||
name|interfaces
operator|.
name|length
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You must provide at least 1 interface class."
argument_list|)
throw|;
block|}
return|return
name|interfaces
index|[
literal|0
index|]
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
DECL|method|createMethodInfoCache (Endpoint endpoint)
specifier|protected
specifier|static
name|MethodInfoCache
name|createMethodInfoCache
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
operator|new
name|MethodInfoCache
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

