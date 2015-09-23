begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|swagger
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|io
operator|.
name|swagger
operator|.
name|jaxrs
operator|.
name|config
operator|.
name|BeanConfig
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
name|Exchange
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
name|util
operator|.
name|EndpointHelper
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
DECL|class|RestSwaggerProcessor
specifier|public
class|class
name|RestSwaggerProcessor
implements|implements
name|Processor
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
name|RestSwaggerProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|swaggerConfig
specifier|private
specifier|final
name|BeanConfig
name|swaggerConfig
decl_stmt|;
DECL|field|support
specifier|private
specifier|final
name|RestSwaggerSupport
name|support
decl_stmt|;
DECL|field|contextIdPattern
specifier|private
specifier|final
name|String
name|contextIdPattern
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|RestSwaggerProcessor (String contextIdPattern, Map<String, Object> parameters)
specifier|public
name|RestSwaggerProcessor
parameter_list|(
name|String
name|contextIdPattern
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|contextIdPattern
operator|=
name|contextIdPattern
expr_stmt|;
name|this
operator|.
name|support
operator|=
operator|new
name|RestSwaggerSupport
argument_list|()
expr_stmt|;
name|this
operator|.
name|swaggerConfig
operator|=
operator|new
name|BeanConfig
argument_list|()
expr_stmt|;
if|if
condition|(
name|parameters
operator|==
literal|null
condition|)
block|{
name|parameters
operator|=
name|Collections
operator|.
name|EMPTY_MAP
expr_stmt|;
block|}
name|support
operator|.
name|initSwagger
argument_list|(
name|swaggerConfig
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|contextId
decl_stmt|;
name|String
name|route
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|RestApiResponseAdapter
name|adapter
init|=
operator|new
name|ExchangeRestApiResponseAdapter
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
try|try
block|{
comment|// render list of camel contexts as root
if|if
condition|(
name|route
operator|==
literal|null
operator|||
name|route
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|||
name|route
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|support
operator|.
name|renderCamelContexts
argument_list|(
name|adapter
argument_list|,
name|contextIdPattern
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// first part is the camel context
if|if
condition|(
name|route
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|route
operator|=
name|route
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// the remainder is the route part
name|contextId
operator|=
name|route
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
index|[
literal|0
index|]
expr_stmt|;
if|if
condition|(
name|route
operator|.
name|startsWith
argument_list|(
name|contextId
argument_list|)
condition|)
block|{
name|route
operator|=
name|route
operator|.
name|substring
argument_list|(
name|contextId
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|match
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|contextIdPattern
operator|!=
literal|null
condition|)
block|{
name|match
operator|=
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|contextId
argument_list|,
name|contextIdPattern
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Match contextId: {} with pattern: {} -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|contextId
block|,
name|contextIdPattern
block|,
name|match
block|}
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
name|adapter
operator|.
name|noContent
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|support
operator|.
name|renderResourceListing
argument_list|(
name|adapter
argument_list|,
name|swaggerConfig
argument_list|,
name|contextId
argument_list|,
name|route
argument_list|)
expr_stmt|;
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
literal|"Error rendering Swagger API due "
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
block|}
end_class

end_unit

