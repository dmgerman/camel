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
comment|/**  * A helper class for creating proxies which delegate to Camel  *   * @version $Revision: 519973 $  */
end_comment

begin_class
DECL|class|ProxyHelper
specifier|public
class|class
name|ProxyHelper
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ProxyHelper ()
specifier|private
name|ProxyHelper
parameter_list|()
block|{             }
comment|/**      * Creates a Proxy which sends PojoExchange to the endpoint.      *       * @throws Exception      */
DECL|method|createProxy (final Endpoint endpoint, ClassLoader cl, Class interfaces[])
specifier|public
specifier|static
name|Object
name|createProxy
parameter_list|(
specifier|final
name|Endpoint
name|endpoint
parameter_list|,
name|ClassLoader
name|cl
parameter_list|,
name|Class
name|interfaces
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
return|return
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|cl
argument_list|,
name|interfaces
argument_list|,
operator|new
name|CamelInvocationHandler
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a Proxy which sends PojoExchange to the endpoint.      *       * @throws Exception      */
DECL|method|createProxy (Endpoint endpoint, Class interfaces[])
specifier|public
specifier|static
name|Object
name|createProxy
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Class
name|interfaces
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
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
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|interfaces
index|[
literal|0
index|]
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|interfaces
argument_list|)
return|;
block|}
comment|/**      * Creates a Proxy which sends PojoExchange to the endpoint.      *       * @throws Exception      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createProxy (Endpoint endpoint, ClassLoader cl, Class<T> interfaceClass)
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
name|interfaceClass
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
operator|new
name|Class
index|[]
block|{
name|interfaceClass
block|}
argument_list|)
return|;
block|}
comment|/**      * Creates a Proxy which sends PojoExchange to the endpoint.      *       * @throws Exception      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createProxy (Endpoint endpoint, Class<T> interfaceClass)
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
name|interfaceClass
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
operator|new
name|Class
index|[]
block|{
name|interfaceClass
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

