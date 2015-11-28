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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|DozerTypeConverterLoader
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
name|DefaultEndpoint
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
name|UriEndpoint
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
name|UriParam
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
name|IOHelper
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
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|CustomConverter
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
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"dozer"
argument_list|,
name|title
operator|=
literal|"Dozer"
argument_list|,
name|syntax
operator|=
literal|"dozer:name"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"transformation"
argument_list|)
DECL|class|DozerEndpoint
specifier|public
class|class
name|DozerEndpoint
extends|extends
name|DefaultEndpoint
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
name|DozerEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// IDs for built-in custom converters used with the Dozer component
DECL|field|CUSTOM_MAPPING_ID
specifier|private
specifier|static
specifier|final
name|String
name|CUSTOM_MAPPING_ID
init|=
literal|"_customMapping"
decl_stmt|;
DECL|field|VARIABLE_MAPPING_ID
specifier|private
specifier|static
specifier|final
name|String
name|VARIABLE_MAPPING_ID
init|=
literal|"_variableMapping"
decl_stmt|;
DECL|field|EXPRESSION_MAPPING_ID
specifier|private
specifier|static
specifier|final
name|String
name|EXPRESSION_MAPPING_ID
init|=
literal|"_expressionMapping"
decl_stmt|;
DECL|field|mapper
specifier|private
name|DozerBeanMapper
name|mapper
decl_stmt|;
DECL|field|variableMapper
specifier|private
name|VariableMapper
name|variableMapper
decl_stmt|;
DECL|field|customMapper
specifier|private
name|CustomMapper
name|customMapper
decl_stmt|;
DECL|field|expressionMapper
specifier|private
name|ExpressionMapper
name|expressionMapper
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|DozerConfiguration
name|configuration
decl_stmt|;
DECL|method|DozerEndpoint (String endpointUri, Component component, DozerConfiguration configuration)
specifier|public
name|DozerEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|DozerConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|variableMapper
operator|=
operator|new
name|VariableMapper
argument_list|()
expr_stmt|;
name|customMapper
operator|=
operator|new
name|CustomMapper
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|)
expr_stmt|;
name|expressionMapper
operator|=
operator|new
name|ExpressionMapper
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DozerProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Consumer not supported for Dozer endpoints"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getMapper ()
specifier|public
name|DozerBeanMapper
name|getMapper
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|mapper
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|DozerConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (DozerConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|DozerConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getCustomMapper ()
name|CustomMapper
name|getCustomMapper
parameter_list|()
block|{
return|return
name|customMapper
return|;
block|}
DECL|method|getVariableMapper ()
name|VariableMapper
name|getVariableMapper
parameter_list|()
block|{
return|return
name|variableMapper
return|;
block|}
DECL|method|getExpressionMapper ()
name|ExpressionMapper
name|getExpressionMapper
parameter_list|()
block|{
return|return
name|expressionMapper
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|mapper
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|configuration
operator|.
name|getMappingConfiguration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mapper
operator|=
name|DozerTypeConverterLoader
operator|.
name|createDozerBeanMapper
argument_list|(
name|configuration
operator|.
name|getMappingConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mapper
operator|=
name|createDozerBeanMapper
argument_list|()
expr_stmt|;
block|}
name|configureMapper
argument_list|(
name|mapper
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
comment|// noop
block|}
DECL|method|createDozerBeanMapper ()
specifier|private
name|DozerBeanMapper
name|createDozerBeanMapper
parameter_list|()
throws|throws
name|Exception
block|{
name|DozerBeanMapper
name|answer
init|=
operator|new
name|DozerBeanMapper
argument_list|()
decl_stmt|;
name|InputStream
name|mapStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Loading Dozer mapping file {}."
argument_list|,
name|configuration
operator|.
name|getMappingFile
argument_list|()
argument_list|)
expr_stmt|;
comment|// create the mapper instance and add the mapping file
name|mapStream
operator|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|configuration
operator|.
name|getMappingFile
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|addMapping
argument_list|(
name|mapStream
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|mapStream
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|configureMapper (DozerBeanMapper mapper)
specifier|private
name|void
name|configureMapper
parameter_list|(
name|DozerBeanMapper
name|mapper
parameter_list|)
throws|throws
name|Exception
block|{
comment|// add our built-in converters
name|Map
argument_list|<
name|String
argument_list|,
name|CustomConverter
argument_list|>
name|converters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|CustomConverter
argument_list|>
argument_list|()
decl_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|CUSTOM_MAPPING_ID
argument_list|,
name|customMapper
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|VARIABLE_MAPPING_ID
argument_list|,
name|variableMapper
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|EXPRESSION_MAPPING_ID
argument_list|,
name|expressionMapper
argument_list|)
expr_stmt|;
name|converters
operator|.
name|putAll
argument_list|(
name|mapper
operator|.
name|getCustomConvertersWithId
argument_list|()
argument_list|)
expr_stmt|;
name|mapper
operator|.
name|setCustomConvertersWithId
argument_list|(
name|converters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

