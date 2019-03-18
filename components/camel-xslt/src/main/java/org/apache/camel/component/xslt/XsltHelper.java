begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
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
name|Method
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
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
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
DECL|class|XsltHelper
specifier|final
class|class
name|XsltHelper
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
name|XsltHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|SAXON_CONFIGURATION_CLASS_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SAXON_CONFIGURATION_CLASS_NAME
init|=
literal|"net.sf.saxon.Configuration"
decl_stmt|;
DECL|field|SAXON_EXTENDED_FUNCTION_DEFINITION_CLASS_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SAXON_EXTENDED_FUNCTION_DEFINITION_CLASS_NAME
init|=
literal|"net.sf.saxon.lib.ExtensionFunctionDefinition"
decl_stmt|;
DECL|method|XsltHelper ()
specifier|private
name|XsltHelper
parameter_list|()
block|{     }
DECL|method|registerSaxonConfiguration ( CamelContext camelContext, Class<?> factoryClass, TransformerFactory factory, Object saxonConfiguration)
specifier|public
specifier|static
name|void
name|registerSaxonConfiguration
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|factoryClass
parameter_list|,
name|TransformerFactory
name|factory
parameter_list|,
name|Object
name|saxonConfiguration
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|saxonConfiguration
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|configurationClass
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|SAXON_CONFIGURATION_CLASS_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurationClass
operator|!=
literal|null
condition|)
block|{
name|Method
name|method
init|=
name|factoryClass
operator|.
name|getMethod
argument_list|(
literal|"setConfiguration"
argument_list|,
name|configurationClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|method
operator|.
name|invoke
argument_list|(
name|factory
argument_list|,
name|configurationClass
operator|.
name|cast
argument_list|(
name|saxonConfiguration
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|registerSaxonConfigurationProperties ( CamelContext camelContext, Class<?> factoryClass, TransformerFactory factory, Map<String, Object> saxonConfigurationProperties)
specifier|public
specifier|static
name|void
name|registerSaxonConfigurationProperties
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|factoryClass
parameter_list|,
name|TransformerFactory
name|factory
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|saxonConfigurationProperties
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|saxonConfigurationProperties
operator|!=
literal|null
operator|&&
operator|!
name|saxonConfigurationProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Method
name|method
init|=
name|factoryClass
operator|.
name|getMethod
argument_list|(
literal|"getConfiguration"
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|Object
name|configuration
init|=
name|method
operator|.
name|invoke
argument_list|(
name|factory
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|method
operator|=
name|configuration
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setConfigurationProperty"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|saxonConfigurationProperties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|method
operator|.
name|invoke
argument_list|(
name|configuration
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|registerSaxonExtensionFunctions ( CamelContext camelContext, Class<?> factoryClass, TransformerFactory factory, List<Object> saxonExtensionFunctions)
specifier|public
specifier|static
name|void
name|registerSaxonExtensionFunctions
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|factoryClass
parameter_list|,
name|TransformerFactory
name|factory
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|saxonExtensionFunctions
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|saxonExtensionFunctions
operator|!=
literal|null
operator|&&
operator|!
name|saxonExtensionFunctions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Method
name|method
init|=
name|factoryClass
operator|.
name|getMethod
argument_list|(
literal|"getConfiguration"
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|Object
name|configuration
init|=
name|method
operator|.
name|invoke
argument_list|(
name|factory
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|extensionClass
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|SAXON_EXTENDED_FUNCTION_DEFINITION_CLASS_NAME
argument_list|,
name|XsltComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|method
operator|=
name|configuration
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"registerExtensionFunction"
argument_list|,
name|extensionClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|extensionFunction
range|:
name|saxonExtensionFunctions
control|)
block|{
if|if
condition|(
name|extensionClass
operator|.
name|isInstance
argument_list|(
name|extensionFunction
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Saxon.registerExtensionFunction {}"
argument_list|,
name|extensionFunction
argument_list|)
expr_stmt|;
name|method
operator|.
name|invoke
argument_list|(
name|configuration
argument_list|,
name|extensionFunction
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to get reference to method registerExtensionFunction on {}"
argument_list|,
name|configuration
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to get Saxon configuration ({}) on {}"
argument_list|,
name|SAXON_CONFIGURATION_CLASS_NAME
argument_list|,
name|factory
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to get reference to method getConfiguration on {}"
argument_list|,
name|factoryClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

