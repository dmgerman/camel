begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.karaf.commands.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|karaf
operator|.
name|commands
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|api
operator|.
name|management
operator|.
name|ManagedCamelContext
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
name|commands
operator|.
name|AbstractLocalCamelController
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
name|support
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
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
comment|/**  * Implementation of<code>CamelController</code>.  */
end_comment

begin_class
DECL|class|CamelControllerImpl
specifier|public
class|class
name|CamelControllerImpl
extends|extends
name|AbstractLocalCamelController
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
name|CamelControllerImpl
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Reference
DECL|field|bundleContext
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|setBundleContext (BundleContext bundleContext)
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLocalCamelContexts ()
specifier|public
name|List
argument_list|<
name|CamelContext
argument_list|>
name|getLocalCamelContexts
parameter_list|()
block|{
name|List
argument_list|<
name|CamelContext
argument_list|>
name|camelContexts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|CamelContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|references
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|reference
range|:
name|references
control|)
block|{
if|if
condition|(
name|reference
operator|!=
literal|null
condition|)
block|{
name|CamelContext
name|camelContext
init|=
operator|(
name|CamelContext
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|camelContexts
operator|.
name|add
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot retrieve the list of Camel contexts. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// sort the list
name|camelContexts
operator|.
name|sort
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|CamelContext
operator|::
name|getName
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContexts
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelContexts ()
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getCamelContexts
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CamelContext
argument_list|>
name|camelContexts
init|=
name|getLocalCamelContexts
argument_list|()
decl_stmt|;
for|for
control|(
name|CamelContext
name|camelContext
range|:
name|camelContexts
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"state"
argument_list|,
name|camelContext
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"uptime"
argument_list|,
name|camelContext
operator|.
name|getUptime
argument_list|()
argument_list|)
expr_stmt|;
name|ManagedCamelContext
name|mcc
init|=
name|camelContext
operator|.
name|getExtension
argument_list|(
name|ManagedCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mcc
operator|!=
literal|null
operator|&&
name|mcc
operator|.
name|getManagedCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"exchangesTotal"
argument_list|,
literal|""
operator|+
name|mcc
operator|.
name|getManagedCamelContext
argument_list|()
operator|.
name|getExchangesTotal
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"exchangesInflight"
argument_list|,
literal|""
operator|+
name|mcc
operator|.
name|getManagedCamelContext
argument_list|()
operator|.
name|getExchangesInflight
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"exchangesFailed"
argument_list|,
literal|""
operator|+
name|mcc
operator|.
name|getManagedCamelContext
argument_list|()
operator|.
name|getExchangesFailed
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|row
operator|.
name|put
argument_list|(
literal|"exchangesTotal"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"exchangesInflight"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"exchangesFailed"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|startContext (String camelContextName)
specifier|public
name|void
name|startContext
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
name|getLocalCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|callWithTCCL
argument_list|(
parameter_list|()
lambda|->
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
name|getClassLoader
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|resumeContext (String camelContextName)
specifier|public
name|void
name|resumeContext
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
name|getLocalCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|callWithTCCL
argument_list|(
parameter_list|()
lambda|->
block|{
name|context
operator|.
name|resume
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
name|getClassLoader
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|startRoute (String camelContextName, final String routeId)
specifier|public
name|void
name|startRoute
parameter_list|(
name|String
name|camelContextName
parameter_list|,
specifier|final
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
name|getLocalCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|callWithTCCL
argument_list|(
parameter_list|()
lambda|->
block|{
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
name|getClassLoader
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|resumeRoute (String camelContextName, final String routeId)
specifier|public
name|void
name|resumeRoute
parameter_list|(
name|String
name|camelContextName
parameter_list|,
specifier|final
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
name|getLocalCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|callWithTCCL
argument_list|(
parameter_list|()
lambda|->
block|{
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|resumeRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
name|getClassLoader
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets classloader associated with {@link CamelContext}      */
DECL|method|getClassLoader (CamelContext context)
specifier|private
name|ClassLoader
name|getClassLoader
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|getApplicationContextClassLoader
argument_list|()
return|;
block|}
block|}
end_class

end_unit

