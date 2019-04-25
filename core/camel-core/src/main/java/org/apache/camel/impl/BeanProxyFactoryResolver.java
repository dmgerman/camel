begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|BeanProxyFactory
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
name|FactoryFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Factory resolver to find the {@link BeanProxyFactory} from the classpath in camel-bean.  */
end_comment

begin_class
DECL|class|BeanProxyFactoryResolver
specifier|public
class|class
name|BeanProxyFactoryResolver
block|{
DECL|field|RESOURCE_PATH
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_PATH
init|=
literal|"META-INF/services/org/apache/camel/"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BeanProxyFactoryResolver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|factoryFinder
specifier|private
name|FactoryFinder
name|factoryFinder
decl_stmt|;
DECL|method|resolve (CamelContext context)
specifier|public
name|BeanProxyFactory
name|resolve
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
comment|// use factory finder to find a custom implementations
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
literal|null
decl_stmt|;
try|try
block|{
name|type
operator|=
name|findFactory
argument_list|(
literal|"bean-proxy-factory"
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found BeanProxyFactory: {} via: {}{}"
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|factoryFinder
operator|.
name|getResourcePath
argument_list|()
argument_list|,
literal|"bean-proxy-factory"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|BeanProxyFactory
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|BeanProxyFactory
name|answer
init|=
operator|(
name|BeanProxyFactory
operator|)
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Detected and using custom BeanProxyFactory: {}"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Type is not a BeanProxyFactory implementation. Found: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot find BeanProxyFactory. Make sure camel-bean is on the classpath."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|method|findFactory (String name, CamelContext context)
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|findFactory
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
if|if
condition|(
name|factoryFinder
operator|==
literal|null
condition|)
block|{
name|factoryFinder
operator|=
name|context
operator|.
name|getFactoryFinder
argument_list|(
name|RESOURCE_PATH
argument_list|)
expr_stmt|;
block|}
return|return
name|factoryFinder
operator|.
name|findClass
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

