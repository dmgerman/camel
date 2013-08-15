begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
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
name|core
operator|.
name|osgi
operator|.
name|OsgiComponentResolver
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
name|CamelContextHelper
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
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|NoSuchComponentException
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|getException
import|;
end_import

begin_class
DECL|class|BlueprintComponentResolver
specifier|public
class|class
name|BlueprintComponentResolver
extends|extends
name|OsgiComponentResolver
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
name|BlueprintComponentResolver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|BlueprintComponentResolver (BundleContext bundleContext)
specifier|public
name|BlueprintComponentResolver
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|super
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
throws|throws
name|Exception
block|{
try|try
block|{
name|Object
name|bean
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|Component
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found component: {} in registry: {}"
argument_list|,
name|name
argument_list|,
name|bean
argument_list|)
expr_stmt|;
return|return
operator|(
name|Component
operator|)
name|bean
return|;
block|}
else|else
block|{
comment|// let's use Camel's type conversion mechanism to convert things like CamelContext
comment|// and other types into a valid Component
name|Component
name|component
init|=
name|CamelContextHelper
operator|.
name|convertTo
argument_list|(
name|context
argument_list|,
name|Component
operator|.
name|class
argument_list|,
name|bean
argument_list|)
decl_stmt|;
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
block|{
return|return
name|component
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|getException
argument_list|(
name|NoSuchComponentException
operator|.
name|class
argument_list|,
name|e
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// if the caused error is NoSuchComponentException then that can be expected so ignore
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Ignored error looking up bean: "
operator|+
name|name
operator|+
literal|" due: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|Object
name|bean
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
literal|".camelBlueprint.componentResolver."
operator|+
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|ComponentResolver
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found component resolver: {} in registry: {}"
argument_list|,
name|name
argument_list|,
name|bean
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|ComponentResolver
operator|)
name|bean
operator|)
operator|.
name|resolveComponent
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
return|;
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
name|trace
argument_list|(
literal|"Ignored error looking up bean: "
operator|+
name|name
operator|+
literal|" due: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|getComponent
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
return|;
block|}
block|}
end_class

end_unit

