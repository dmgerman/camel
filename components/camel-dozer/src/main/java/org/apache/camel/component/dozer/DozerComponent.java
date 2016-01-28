begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dozer
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
name|Field
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
name|Endpoint
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
name|converter
operator|.
name|dozer
operator|.
name|DozerBeanMapperConfiguration
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
name|converter
operator|.
name|dozer
operator|.
name|DozerThreadContextClassLoader
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
name|impl
operator|.
name|UriEndpointComponent
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
name|ReflectionHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|DozerBeanMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|config
operator|.
name|GlobalSettings
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
DECL|class|DozerComponent
specifier|public
class|class
name|DozerComponent
extends|extends
name|UriEndpointComponent
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
name|DozerComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|DozerComponent ()
specifier|public
name|DozerComponent
parameter_list|()
block|{
name|super
argument_list|(
name|DozerEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|DozerComponent (CamelContext context)
specifier|public
name|DozerComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|DozerEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|DozerConfiguration
name|config
init|=
operator|new
name|DozerConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMappingConfiguration
argument_list|(
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"mappingConfiguration"
argument_list|,
name|DozerBeanMapperConfiguration
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// Validate endpoint parameters
if|if
condition|(
name|config
operator|.
name|getTargetModel
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The targetModel parameter is required for dozer endpoints"
argument_list|)
throw|;
block|}
return|return
operator|new
name|DozerEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
return|;
block|}
DECL|method|createDozerBeanMapper (List<String> mappingFiles)
specifier|public
specifier|static
name|DozerBeanMapper
name|createDozerBeanMapper
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|mappingFiles
parameter_list|)
block|{
name|GlobalSettings
name|settings
init|=
name|GlobalSettings
operator|.
name|getInstance
argument_list|()
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Configuring GlobalSettings to use Camel classloader: {}"
argument_list|,
name|DozerThreadContextClassLoader
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Field
name|field
init|=
name|settings
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"classLoaderBeanName"
argument_list|)
decl_stmt|;
name|ReflectionHelper
operator|.
name|setField
argument_list|(
name|field
argument_list|,
name|settings
argument_list|,
name|DozerThreadContextClassLoader
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Cannot configure Dozer GlobalSettings to use DozerThreadContextClassLoader as classloader due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Configuring GlobalSettings to enable EL"
argument_list|)
expr_stmt|;
name|Field
name|field
init|=
name|settings
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"elEnabled"
argument_list|)
decl_stmt|;
name|ReflectionHelper
operator|.
name|setField
argument_list|(
name|field
argument_list|,
name|settings
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|nsfEx
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Failed to enable EL in global Dozer settings"
argument_list|,
name|nsfEx
argument_list|)
throw|;
block|}
return|return
operator|new
name|DozerBeanMapper
argument_list|(
name|mappingFiles
argument_list|)
return|;
block|}
block|}
end_class

end_unit

