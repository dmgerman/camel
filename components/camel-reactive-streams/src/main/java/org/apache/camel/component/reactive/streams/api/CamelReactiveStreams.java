begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ObjectHelper
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
comment|/**  * This is the main entry-point for getting Camel streams associate to reactive-streams endpoints.  *  * It allows to retrieve the {@link CamelReactiveStreamsService} to access Camel streams.  * This class returns the default implementation of the service unless the client requests a named service,  */
end_comment

begin_class
DECL|class|CamelReactiveStreams
specifier|public
specifier|final
class|class
name|CamelReactiveStreams
block|{
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
name|CamelReactiveStreams
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|serviceNames
specifier|private
specifier|static
name|Map
argument_list|<
name|CamelContext
argument_list|,
name|String
argument_list|>
name|serviceNames
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|CamelReactiveStreams ()
specifier|private
name|CamelReactiveStreams
parameter_list|()
block|{     }
DECL|method|get (CamelContext context)
specifier|public
specifier|static
name|CamelReactiveStreamsService
name|get
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
name|get
argument_list|(
name|context
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|get (CamelContext context, String serviceName)
specifier|public
specifier|static
name|CamelReactiveStreamsService
name|get
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|serviceName
parameter_list|)
block|{
if|if
condition|(
name|serviceName
operator|!=
literal|null
operator|&&
name|serviceName
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"the service name cannot be an empty String"
argument_list|)
throw|;
block|}
name|String
name|lookupName
init|=
name|serviceName
operator|!=
literal|null
condition|?
name|serviceName
else|:
literal|""
decl_stmt|;
name|serviceNames
operator|.
name|computeIfAbsent
argument_list|(
name|context
argument_list|,
name|ctx
lambda|->
block|{
name|CamelReactiveStreamsService
name|service
init|=
name|context
operator|.
name|hasService
argument_list|(
name|CamelReactiveStreamsService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|service
operator|=
name|resolveReactiveStreamsService
argument_list|(
name|context
argument_list|,
name|serviceName
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|addService
argument_list|(
name|service
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot add the CamelReactiveStreamsService to the Camel context"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|lookupName
return|;
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|serviceNames
operator|.
name|get
argument_list|(
name|context
argument_list|)
argument_list|,
name|lookupName
argument_list|)
condition|)
block|{
comment|// only a single implementation of the CamelReactiveStreamService can be present per Camel context
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot use two different implementations of CamelReactiveStreamsService in the same CamelContext: "
operator|+
literal|"existing service name ["
operator|+
name|serviceNames
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|+
literal|"] - requested ["
operator|+
name|lookupName
operator|+
literal|"]"
argument_list|)
throw|;
block|}
return|return
name|context
operator|.
name|hasService
argument_list|(
name|CamelReactiveStreamsService
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|resolveReactiveStreamsService (CamelContext context, String serviceName)
specifier|private
specifier|static
name|CamelReactiveStreamsService
name|resolveReactiveStreamsService
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|serviceName
parameter_list|)
block|{
name|CamelReactiveStreamsService
name|service
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|serviceName
operator|!=
literal|null
condition|)
block|{
comment|// lookup in the registry
name|service
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|serviceName
argument_list|,
name|CamelReactiveStreamsService
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|service
operator|=
name|resolveServiceUsingFactory
argument_list|(
name|context
argument_list|,
name|serviceName
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Set
argument_list|<
name|CamelReactiveStreamsService
argument_list|>
name|set
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|CamelReactiveStreamsService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|set
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|service
operator|=
name|set
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Using default reactive stream service"
argument_list|)
expr_stmt|;
name|service
operator|=
name|resolveServiceUsingFactory
argument_list|(
name|context
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|service
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|resolveServiceUsingFactory (CamelContext context, String name)
specifier|private
specifier|static
name|CamelReactiveStreamsService
name|resolveServiceUsingFactory
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
literal|"default-service"
expr_stmt|;
block|}
name|String
name|path
init|=
literal|"META-INF/services/org/apache/camel/reactive-streams/"
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|CamelReactiveStreamsService
argument_list|>
name|serviceClass
init|=
literal|null
decl_stmt|;
try|try
block|{
name|FactoryFinder
name|finder
init|=
name|context
operator|.
name|getFactoryFinder
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using FactoryFinder: {}"
argument_list|,
name|finder
argument_list|)
expr_stmt|;
name|serviceClass
operator|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|CamelReactiveStreamsService
argument_list|>
operator|)
name|finder
operator|.
name|findClass
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|serviceClass
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Class referenced in '"
operator|+
name|path
operator|+
name|name
operator|+
literal|"' not found"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to create the reactive stream service defined in '"
operator|+
name|path
operator|+
name|name
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

