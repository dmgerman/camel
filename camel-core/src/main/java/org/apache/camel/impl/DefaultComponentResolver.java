begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Exchange
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
name|util
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The default implementation of {@link ComponentResolver}  * which tries to find components by using the URI scheme prefix and searching for a file of the URI  * scheme name in the<b>META-INF/services/org/apache/camel/component/</b>  * directory on the classpath.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultComponentResolver
specifier|public
class|class
name|DefaultComponentResolver
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
implements|implements
name|ComponentResolver
argument_list|<
name|E
argument_list|>
block|{
DECL|field|componentFactory
specifier|protected
specifier|static
specifier|final
name|FactoryFinder
name|componentFactory
init|=
operator|new
name|FactoryFinder
argument_list|(
literal|"META-INF/services/org/apache/camel/component/"
argument_list|)
decl_stmt|;
DECL|method|resolveComponent (String uri, CamelContext context)
specifier|public
name|Component
argument_list|<
name|E
argument_list|>
name|resolveComponent
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|String
name|splitURI
index|[]
init|=
name|ObjectHelper
operator|.
name|splitOnCharacter
argument_list|(
name|uri
argument_list|,
literal|":"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitURI
index|[
literal|1
index|]
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URI, it did not contain a scheme: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|String
name|scheme
init|=
name|splitURI
index|[
literal|0
index|]
decl_stmt|;
name|Class
name|type
decl_stmt|;
try|try
block|{
name|type
operator|=
name|componentFactory
operator|.
name|findClass
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
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
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URI, no EndpointResolver registered for scheme : "
operator|+
name|scheme
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
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
name|Component
argument_list|<
name|E
argument_list|>
name|answer
init|=
operator|(
name|Component
argument_list|<
name|E
argument_list|>
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
comment|// lets add the component using the prefix
name|context
operator|.
name|addComponent
argument_list|(
name|scheme
argument_list|,
name|answer
argument_list|)
expr_stmt|;
comment|// TODO should we start it?
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
block|}
end_class

end_unit

