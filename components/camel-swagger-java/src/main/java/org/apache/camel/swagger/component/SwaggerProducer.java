begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.swagger.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|swagger
operator|.
name|component
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Operation
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Swagger
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|parameters
operator|.
name|Parameter
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|NoSuchBeanException
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
name|NoSuchHeaderException
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
name|impl
operator|.
name|DefaultAsyncProducer
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
name|RestProducerFactory
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
name|AsyncProcessorConverterHelper
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
name|CollectionStringBuffer
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
name|FileUtil
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
name|ServiceHelper
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
name|StringHelper
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
name|URISupport
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
name|Deprecated
DECL|class|SwaggerProducer
specifier|public
class|class
name|SwaggerProducer
extends|extends
name|DefaultAsyncProducer
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
name|SwaggerProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|swagger
specifier|private
name|Swagger
name|swagger
decl_stmt|;
DECL|field|operation
specifier|private
name|Operation
name|operation
decl_stmt|;
DECL|field|producer
specifier|private
name|AsyncProcessor
name|producer
decl_stmt|;
DECL|method|SwaggerProducer (Endpoint endpoint)
specifier|public
name|SwaggerProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SwaggerEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SwaggerEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// TODO: bind to consumes context-type
comment|// TODO: if binding is turned on/off/auto etc
try|try
block|{
if|if
condition|(
name|producer
operator|!=
literal|null
condition|)
block|{
name|prepareExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// some error or there was no producer, so we are done
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|prepareExchange (Exchange exchange)
specifier|protected
name|void
name|prepareExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|hasPath
init|=
literal|false
decl_stmt|;
name|boolean
name|hasQuery
init|=
literal|false
decl_stmt|;
comment|// uri template with path parameters resolved
name|String
name|resolvedUriTemplate
init|=
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
comment|// for query parameters
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|query
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Parameter
name|param
range|:
name|operation
operator|.
name|getParameters
argument_list|()
control|)
block|{
if|if
condition|(
literal|"query"
operator|.
name|equals
argument_list|(
name|param
operator|.
name|getIn
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|param
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|String
name|value
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|hasQuery
operator|=
literal|true
expr_stmt|;
comment|// we need to remove the header as they are sent as query instead
comment|// TODO: we could use a header filter strategy to skip these headers
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|param
operator|.
name|getRequired
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchHeaderException
argument_list|(
name|exchange
argument_list|,
name|name
argument_list|,
name|String
operator|.
name|class
argument_list|)
throw|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
literal|"path"
operator|.
name|equals
argument_list|(
name|param
operator|.
name|getIn
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|value
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|hasPath
operator|=
literal|true
expr_stmt|;
comment|// we need to remove the header as they are sent as path instead
comment|// TODO: we could use a header filter strategy to skip these headers
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|token
init|=
literal|"{"
operator|+
name|param
operator|.
name|getName
argument_list|()
operator|+
literal|"}"
decl_stmt|;
name|resolvedUriTemplate
operator|=
name|StringHelper
operator|.
name|replaceAll
argument_list|(
name|resolvedUriTemplate
argument_list|,
name|token
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|param
operator|.
name|getRequired
argument_list|()
condition|)
block|{
comment|// the parameter is required but we do not have a header
throw|throw
operator|new
name|NoSuchHeaderException
argument_list|(
name|exchange
argument_list|,
name|param
operator|.
name|getName
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|hasQuery
condition|)
block|{
name|String
name|queryParameters
init|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
name|queryParameters
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasPath
condition|)
block|{
name|String
name|scheme
init|=
name|swagger
operator|.
name|getSchemes
argument_list|()
operator|!=
literal|null
operator|&&
name|swagger
operator|.
name|getSchemes
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|?
name|swagger
operator|.
name|getSchemes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toValue
argument_list|()
else|:
literal|"http"
decl_stmt|;
name|String
name|host
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
condition|?
name|getEndpoint
argument_list|()
operator|.
name|getHost
argument_list|()
else|:
name|swagger
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|String
name|basePath
init|=
name|swagger
operator|.
name|getBasePath
argument_list|()
decl_stmt|;
name|basePath
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
name|resolvedUriTemplate
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|resolvedUriTemplate
argument_list|)
expr_stmt|;
comment|// if so us a header for the dynamic uri template so we reuse same endpoint but the header overrides the actual url to use
name|String
name|overrideUri
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%s://%s/%s/%s"
argument_list|,
name|scheme
argument_list|,
name|host
argument_list|,
name|basePath
argument_list|,
name|resolvedUriTemplate
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
name|overrideUri
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getSwagger ()
specifier|public
name|Swagger
name|getSwagger
parameter_list|()
block|{
return|return
name|swagger
return|;
block|}
DECL|method|setSwagger (Swagger swagger)
specifier|public
name|void
name|setSwagger
parameter_list|(
name|Swagger
name|swagger
parameter_list|)
block|{
name|this
operator|.
name|swagger
operator|=
name|swagger
expr_stmt|;
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
name|String
name|verb
init|=
name|getEndpoint
argument_list|()
operator|.
name|getVerb
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|operation
operator|=
name|getSwaggerOperation
argument_list|(
name|verb
argument_list|,
name|path
argument_list|)
expr_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Swagger schema does not contain operation for "
operator|+
name|verb
operator|+
literal|":"
operator|+
name|path
argument_list|)
throw|;
block|}
name|Producer
name|processor
init|=
name|createHttpProducer
argument_list|(
name|operation
argument_list|,
name|verb
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|producer
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
DECL|method|getSwaggerOperation (String verb, String path)
specifier|private
name|Operation
name|getSwaggerOperation
parameter_list|(
name|String
name|verb
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|Path
name|modelPath
init|=
name|swagger
operator|.
name|getPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|modelPath
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// get,put,post,head,delete,patch,options
name|Operation
name|op
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"get"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|op
operator|=
name|modelPath
operator|.
name|getGet
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"put"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|op
operator|=
name|modelPath
operator|.
name|getPut
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"post"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|op
operator|=
name|modelPath
operator|.
name|getPost
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"head"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|op
operator|=
name|modelPath
operator|.
name|getHead
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"delete"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|op
operator|=
name|modelPath
operator|.
name|getDelete
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"patch"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|op
operator|=
name|modelPath
operator|.
name|getPatch
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"options"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|op
operator|=
name|modelPath
operator|.
name|getOptions
argument_list|()
expr_stmt|;
block|}
return|return
name|op
return|;
block|}
DECL|method|createHttpProducer (Operation operation, String verb, String path)
specifier|private
name|Producer
name|createHttpProducer
parameter_list|(
name|Operation
name|operation
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using Swagger operation: {} with {} {}"
argument_list|,
name|operation
argument_list|,
name|verb
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|RestProducerFactory
name|factory
init|=
literal|null
decl_stmt|;
name|String
name|cname
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getComponentName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|comp
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getComponentName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|comp
operator|!=
literal|null
operator|&&
name|comp
operator|instanceof
name|RestProducerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestProducerFactory
operator|)
name|comp
expr_stmt|;
block|}
else|else
block|{
name|comp
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getComponent
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getComponentName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|comp
operator|!=
literal|null
operator|&&
name|comp
operator|instanceof
name|RestProducerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestProducerFactory
operator|)
name|comp
expr_stmt|;
block|}
block|}
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|comp
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Component "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getComponentName
argument_list|()
operator|+
literal|" is not a RestProducerFactory"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getComponentName
argument_list|()
argument_list|,
name|RestProducerFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|cname
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getComponentName
argument_list|()
expr_stmt|;
block|}
comment|// try all components
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getComponentNames
argument_list|()
control|)
block|{
name|Component
name|comp
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getComponent
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|comp
operator|!=
literal|null
operator|&&
name|comp
operator|instanceof
name|RestProducerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestProducerFactory
operator|)
name|comp
expr_stmt|;
name|cname
operator|=
name|name
expr_stmt|;
break|break;
block|}
block|}
block|}
comment|// lookup in registry
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|Set
argument_list|<
name|RestProducerFactory
argument_list|>
name|factories
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|RestProducerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|factories
operator|!=
literal|null
operator|&&
name|factories
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|factory
operator|=
name|factories
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using RestProducerFactory: {}"
argument_list|,
name|factory
argument_list|)
expr_stmt|;
name|CollectionStringBuffer
name|produces
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|operation
operator|.
name|getProduces
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
name|swagger
operator|.
name|getProduces
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|s
range|:
name|list
control|)
block|{
name|produces
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
name|CollectionStringBuffer
name|consumes
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|list
operator|=
name|operation
operator|.
name|getConsumes
argument_list|()
expr_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
name|swagger
operator|.
name|getConsumes
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|s
range|:
name|list
control|)
block|{
name|consumes
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|host
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
condition|?
name|getEndpoint
argument_list|()
operator|.
name|getHost
argument_list|()
else|:
name|swagger
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|String
name|basePath
init|=
name|swagger
operator|.
name|getBasePath
argument_list|()
decl_stmt|;
name|String
name|uriTemplate
init|=
name|path
decl_stmt|;
return|return
name|factory
operator|.
name|createProducer
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|host
argument_list|,
name|verb
argument_list|,
name|basePath
argument_list|,
name|uriTemplate
argument_list|,
operator|(
name|consumes
operator|.
name|isEmpty
argument_list|()
condition|?
literal|""
else|:
name|consumes
operator|.
name|toString
argument_list|()
operator|)
argument_list|,
operator|(
name|produces
operator|.
name|isEmpty
argument_list|()
condition|?
literal|""
else|:
name|produces
operator|.
name|toString
argument_list|()
operator|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot find RestProducerFactory in Registry or as a Component to use"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

