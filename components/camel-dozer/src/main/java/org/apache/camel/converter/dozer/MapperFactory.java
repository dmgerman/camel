begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|dozer
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
name|component
operator|.
name|dozer
operator|.
name|DozerEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|DozerBeanMapperBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|Mapper
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
name|SettingsKeys
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|el
operator|.
name|DefaultELEngine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|el
operator|.
name|ELEngine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|el
operator|.
name|ELExpressionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|el
operator|.
name|NoopELEngine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|el
operator|.
name|TcclELEngine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|util
operator|.
name|RuntimeUtils
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
DECL|class|MapperFactory
specifier|public
class|class
name|MapperFactory
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
name|MapperFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|DozerBeanMapperConfiguration
name|configuration
decl_stmt|;
DECL|method|MapperFactory (CamelContext camelContext, DozerBeanMapperConfiguration configuration)
specifier|public
name|MapperFactory
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DozerBeanMapperConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|create ()
specifier|public
name|Mapper
name|create
parameter_list|()
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"CamelContext is null"
argument_list|)
throw|;
block|}
return|return
name|parseConfiguration
argument_list|(
name|configuration
argument_list|)
return|;
block|}
DECL|method|parseConfiguration (DozerBeanMapperConfiguration configuration)
specifier|private
name|Mapper
name|parseConfiguration
parameter_list|(
name|DozerBeanMapperConfiguration
name|configuration
parameter_list|)
block|{
name|configureSettings
argument_list|()
expr_stmt|;
name|Mapper
name|mapper
decl_stmt|;
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|mapper
operator|=
name|DozerBeanMapperBuilder
operator|.
name|buildDefault
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|mapper
operator|=
name|DozerBeanMapperBuilder
operator|.
name|create
argument_list|()
operator|.
name|withMappingFiles
argument_list|(
name|configuration
operator|.
name|getMappingFiles
argument_list|()
argument_list|)
operator|.
name|withCustomConverters
argument_list|(
name|configuration
operator|.
name|getCustomConverters
argument_list|()
argument_list|)
operator|.
name|withEventListeners
argument_list|(
name|configuration
operator|.
name|getEventListeners
argument_list|()
argument_list|)
operator|.
name|withCustomConvertersWithIds
argument_list|(
name|configuration
operator|.
name|getCustomConvertersWithId
argument_list|()
argument_list|)
operator|.
name|withMappingBuilders
argument_list|(
name|configuration
operator|.
name|getBeanMappingBuilders
argument_list|()
argument_list|)
operator|.
name|withCustomFieldMapper
argument_list|(
name|configuration
operator|.
name|getCustomFieldMapper
argument_list|()
argument_list|)
operator|.
name|withELEngine
argument_list|(
name|createELEngine
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
name|mapper
operator|.
name|getMappingMetadata
argument_list|()
expr_stmt|;
return|return
name|mapper
return|;
block|}
DECL|method|configureSettings ()
specifier|private
name|void
name|configureSettings
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|SettingsKeys
operator|.
name|CLASS_LOADER_BEAN
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
DECL|method|createELEngine ()
specifier|private
name|ELEngine
name|createELEngine
parameter_list|()
block|{
name|ELEngine
name|answer
decl_stmt|;
name|ClassLoader
name|appcl
init|=
name|camelContext
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
name|ClassLoader
name|auxcl
init|=
name|appcl
operator|==
literal|null
condition|?
name|DozerEndpoint
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
else|:
name|appcl
decl_stmt|;
if|if
condition|(
name|ELExpressionFactory
operator|.
name|isSupported
argument_list|(
name|auxcl
argument_list|)
condition|)
block|{
if|if
condition|(
name|RuntimeUtils
operator|.
name|isOSGi
argument_list|()
condition|)
block|{
name|answer
operator|=
operator|new
name|TcclELEngine
argument_list|(
name|ELExpressionFactory
operator|.
name|newInstance
argument_list|(
name|auxcl
argument_list|)
argument_list|,
name|auxcl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|DefaultELEngine
argument_list|(
name|ELExpressionFactory
operator|.
name|newInstance
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
literal|"Expressions are not supported by Dozer. Are you missing javax.el dependency?"
argument_list|)
expr_stmt|;
name|answer
operator|=
operator|new
name|NoopELEngine
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

