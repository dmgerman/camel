begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|DataFormatFactory
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
comment|/**  * Some helper methods for new resolvers (like {@link org.apache.camel.spi.ComponentResolver}, {@link org.apache.camel.spi.DataFormatResolver}, etc.).  *  * @version  */
end_comment

begin_class
DECL|class|ResolverHelper
specifier|public
specifier|final
class|class
name|ResolverHelper
block|{
DECL|field|COMPONENT_FALLBACK_SUFFIX
specifier|public
specifier|static
specifier|final
name|String
name|COMPONENT_FALLBACK_SUFFIX
init|=
literal|"-component"
decl_stmt|;
DECL|field|DATA_FORMAT_FALLBACK_SUFFIX
specifier|public
specifier|static
specifier|final
name|String
name|DATA_FORMAT_FALLBACK_SUFFIX
init|=
literal|"-dataformat"
decl_stmt|;
DECL|field|DATA_FORMAT_FACTORY_FALLBACK_SUFFIX
specifier|public
specifier|static
specifier|final
name|String
name|DATA_FORMAT_FACTORY_FALLBACK_SUFFIX
init|=
literal|"-dataformat-factory"
decl_stmt|;
DECL|field|LANGUAGE_FALLBACK_SUFFIX
specifier|public
specifier|static
specifier|final
name|String
name|LANGUAGE_FALLBACK_SUFFIX
init|=
literal|"-language"
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
name|ResolverHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|EXCEPTION_HANDLER
specifier|private
specifier|static
specifier|final
name|LookupExceptionHandler
name|EXCEPTION_HANDLER
init|=
operator|new
name|LookupExceptionHandler
argument_list|()
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ResolverHelper ()
specifier|private
name|ResolverHelper
parameter_list|()
block|{     }
DECL|method|lookupComponentInRegistryWithFallback (CamelContext context, String name)
specifier|public
specifier|static
name|Component
name|lookupComponentInRegistryWithFallback
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|lookupComponentInRegistryWithFallback
argument_list|(
name|context
argument_list|,
name|name
argument_list|,
name|EXCEPTION_HANDLER
argument_list|)
return|;
block|}
DECL|method|lookupComponentInRegistryWithFallback (CamelContext context, String name, LookupExceptionHandler exceptionHandler)
specifier|public
specifier|static
name|Component
name|lookupComponentInRegistryWithFallback
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|LookupExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|Object
name|bean
init|=
name|lookupInRegistry
argument_list|(
name|context
argument_list|,
name|Component
operator|.
name|class
argument_list|,
literal|false
argument_list|,
name|exceptionHandler
argument_list|,
name|name
argument_list|,
name|name
operator|+
name|COMPONENT_FALLBACK_SUFFIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|bean
operator|instanceof
name|Component
condition|)
block|{
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
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found Component with incompatible class: {}"
argument_list|,
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|lookupDataFormatInRegistryWithFallback (CamelContext context, String name)
specifier|public
specifier|static
name|DataFormat
name|lookupDataFormatInRegistryWithFallback
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|lookupDataFormatInRegistryWithFallback
argument_list|(
name|context
argument_list|,
name|name
argument_list|,
name|EXCEPTION_HANDLER
argument_list|)
return|;
block|}
DECL|method|lookupDataFormatInRegistryWithFallback (CamelContext context, String name, LookupExceptionHandler exceptionHandler)
specifier|public
specifier|static
name|DataFormat
name|lookupDataFormatInRegistryWithFallback
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|LookupExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|Object
name|bean
init|=
name|lookupInRegistry
argument_list|(
name|context
argument_list|,
name|DataFormat
operator|.
name|class
argument_list|,
literal|false
argument_list|,
name|exceptionHandler
argument_list|,
name|name
argument_list|,
name|name
operator|+
name|DATA_FORMAT_FALLBACK_SUFFIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|DataFormat
condition|)
block|{
return|return
operator|(
name|DataFormat
operator|)
name|bean
return|;
block|}
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found DataFormat with incompatible class: {}"
argument_list|,
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|lookupDataFormatFactoryInRegistryWithFallback (CamelContext context, String name)
specifier|public
specifier|static
name|DataFormatFactory
name|lookupDataFormatFactoryInRegistryWithFallback
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|lookupDataFormatFactoryInRegistryWithFallback
argument_list|(
name|context
argument_list|,
name|name
argument_list|,
name|EXCEPTION_HANDLER
argument_list|)
return|;
block|}
DECL|method|lookupDataFormatFactoryInRegistryWithFallback (CamelContext context, String name, LookupExceptionHandler exceptionHandler)
specifier|public
specifier|static
name|DataFormatFactory
name|lookupDataFormatFactoryInRegistryWithFallback
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|LookupExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|Object
name|bean
init|=
name|lookupInRegistry
argument_list|(
name|context
argument_list|,
name|DataFormatFactory
operator|.
name|class
argument_list|,
literal|false
argument_list|,
name|exceptionHandler
argument_list|,
name|name
argument_list|,
name|name
operator|+
name|DATA_FORMAT_FACTORY_FALLBACK_SUFFIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|DataFormatFactory
condition|)
block|{
return|return
operator|(
name|DataFormatFactory
operator|)
name|bean
return|;
block|}
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found DataFormatFactory with incompatible class: {}"
argument_list|,
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|lookupLanguageInRegistryWithFallback (CamelContext context, String name)
specifier|public
specifier|static
name|Language
name|lookupLanguageInRegistryWithFallback
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|lookupLanguageInRegistryWithFallback
argument_list|(
name|context
argument_list|,
name|name
argument_list|,
name|EXCEPTION_HANDLER
argument_list|)
return|;
block|}
DECL|method|lookupLanguageInRegistryWithFallback (CamelContext context, String name, LookupExceptionHandler exceptionHandler)
specifier|public
specifier|static
name|Language
name|lookupLanguageInRegistryWithFallback
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|LookupExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|Object
name|bean
init|=
name|lookupInRegistry
argument_list|(
name|context
argument_list|,
name|Language
operator|.
name|class
argument_list|,
literal|false
argument_list|,
name|exceptionHandler
argument_list|,
name|name
argument_list|,
name|name
operator|+
name|LANGUAGE_FALLBACK_SUFFIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|Language
condition|)
block|{
return|return
operator|(
name|Language
operator|)
name|bean
return|;
block|}
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found Language with incompatible class: {}"
argument_list|,
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|class|LookupExceptionHandler
specifier|public
specifier|static
class|class
name|LookupExceptionHandler
block|{
DECL|method|handleException (Exception e, Logger log, String name)
specifier|public
name|void
name|handleException
parameter_list|(
name|Exception
name|e
parameter_list|,
name|Logger
name|log
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Ignored error looking up bean: {}"
argument_list|,
name|name
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|lookupInRegistry (CamelContext context, Class<?> type, boolean lookupByNameAndType, LookupExceptionHandler exceptionHandler, String... names)
specifier|private
specifier|static
name|Object
name|lookupInRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|boolean
name|lookupByNameAndType
parameter_list|,
name|LookupExceptionHandler
name|exceptionHandler
parameter_list|,
name|String
modifier|...
name|names
parameter_list|)
block|{
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
try|try
block|{
name|Object
name|bean
decl_stmt|;
if|if
condition|(
name|lookupByNameAndType
condition|)
block|{
name|bean
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bean
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Lookup {} with name {} in registry. Found: {}"
argument_list|,
name|type
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|name
argument_list|,
name|bean
argument_list|)
expr_stmt|;
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
return|return
name|bean
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exceptionHandler
operator|.
name|handleException
argument_list|(
name|e
argument_list|,
name|LOG
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

