begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Component
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
name|NoFactoryAvailableException
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
name|ComponentResolver
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ResolverHelper
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
comment|/**  * The default implementation of {@link ComponentResolver} which tries to find  * components by using the URI scheme prefix and searching for a file of the URI  * scheme name in the<b>META-INF/services/org/apache/camel/component/</b>  * directory on the classpath.  */
end_comment

begin_class
DECL|class|DefaultComponentResolver
specifier|public
class|class
name|DefaultComponentResolver
implements|implements
name|ComponentResolver
block|{
DECL|field|RESOURCE_PATH
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_PATH
init|=
literal|"META-INF/services/org/apache/camel/component/"
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
name|DefaultComponentResolver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|factoryFinder
specifier|private
name|FactoryFinder
name|factoryFinder
decl_stmt|;
DECL|method|resolveComponent (String name, CamelContext context)
specifier|public
name|Component
name|resolveComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
comment|// lookup in registry first
name|Component
name|componentReg
init|=
name|ResolverHelper
operator|.
name|lookupComponentInRegistryWithFallback
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|componentReg
operator|!=
literal|null
condition|)
block|{
return|return
name|componentReg
return|;
block|}
comment|// not in registry then use component factory
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
try|try
block|{
name|type
operator|=
name|findComponent
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
comment|// not found
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoFactoryAvailableException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URI, no Component registered for scheme: "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|getLog
argument_list|()
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Found component: {} via type: {} via: {}{}"
argument_list|,
name|name
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
name|name
argument_list|)
expr_stmt|;
block|}
comment|// create the component
if|if
condition|(
name|Component
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|(
name|Component
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
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Type is not a Component implementation. Found: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|findComponent (String name, CamelContext context)
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|findComponent
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
DECL|method|getLog ()
specifier|protected
name|Logger
name|getLog
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
block|}
end_class

end_unit

