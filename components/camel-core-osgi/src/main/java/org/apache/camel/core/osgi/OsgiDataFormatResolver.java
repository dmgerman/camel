begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
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
name|spi
operator|.
name|DataFormat
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
name|DataFormatResolver
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
name|InvalidSyntaxException
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

begin_class
DECL|class|OsgiDataFormatResolver
specifier|public
class|class
name|OsgiDataFormatResolver
implements|implements
name|DataFormatResolver
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
name|OsgiDataFormatResolver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|bundleContext
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|OsgiDataFormatResolver (BundleContext bundleContext)
specifier|public
name|OsgiDataFormatResolver
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
DECL|method|resolveDataFormat (String name, CamelContext context)
specifier|public
name|DataFormat
name|resolveDataFormat
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
comment|// lookup in registry first
name|DataFormat
name|dataFormatReg
init|=
name|ResolverHelper
operator|.
name|lookupDataFormatInRegistryWithFallback
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataFormatReg
operator|!=
literal|null
condition|)
block|{
return|return
name|dataFormatReg
return|;
block|}
return|return
name|getDataFormat
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
return|;
block|}
DECL|method|getDataFormat (String name, CamelContext context)
specifier|protected
name|DataFormat
name|getDataFormat
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Finding DataFormat: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
try|try
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|refs
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|DataFormatResolver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"(dataformat="
operator|+
name|name
operator|+
literal|")"
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
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
name|ref
range|:
name|refs
control|)
block|{
name|Object
name|service
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|DataFormatResolver
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|service
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|DataFormatResolver
name|resolver
init|=
operator|(
name|DataFormatResolver
operator|)
name|service
decl_stmt|;
return|return
name|resolver
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

