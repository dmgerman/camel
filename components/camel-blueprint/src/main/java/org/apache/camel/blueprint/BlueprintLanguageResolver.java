begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|core
operator|.
name|osgi
operator|.
name|OsgiLanguageResolver
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
name|Language
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
name|LanguageResolver
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
DECL|class|BlueprintLanguageResolver
specifier|public
class|class
name|BlueprintLanguageResolver
extends|extends
name|OsgiLanguageResolver
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
name|BlueprintLanguageResolver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|BlueprintLanguageResolver (BundleContext bundleContext)
specifier|public
name|BlueprintLanguageResolver
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
DECL|method|resolveLanguage (String name, CamelContext context)
specifier|public
name|Language
name|resolveLanguage
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
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
literal|".camelBlueprint.languageResolver."
operator|+
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|LanguageResolver
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found language resolver: {} in registry: {}"
argument_list|,
name|name
argument_list|,
name|bean
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|LanguageResolver
operator|)
name|bean
operator|)
operator|.
name|resolveLanguage
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
name|super
operator|.
name|resolveLanguage
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

