begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|swagger
operator|.
name|parser
operator|.
name|SwaggerParser
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
name|IOHelper
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
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
import|;
end_import

begin_class
DECL|class|SwaggerRestProducerFactory
specifier|public
class|class
name|SwaggerRestProducerFactory
implements|implements
name|RestProducerFactory
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
name|SwaggerRestProducerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|createProducer (CamelContext camelContext, String host, String verb, String basePath, String uriTemplate, String queryParameters, String consumes, String produces, Map<String, Object> parameters)
specifier|public
name|Producer
name|createProducer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|host
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|queryParameters
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
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
name|String
name|apiDoc
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"apiDoc"
argument_list|)
decl_stmt|;
comment|// load json model
if|if
condition|(
name|apiDoc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Swagger api-doc must be configured using the apiDoc option"
argument_list|)
throw|;
block|}
name|String
name|path
init|=
name|uriTemplate
operator|!=
literal|null
condition|?
name|uriTemplate
else|:
name|basePath
decl_stmt|;
comment|// path must start with a leading slash
if|if
condition|(
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
name|Swagger
name|swagger
init|=
name|loadSwaggerModel
argument_list|(
name|camelContext
argument_list|,
name|apiDoc
argument_list|)
decl_stmt|;
name|Operation
name|operation
init|=
name|getSwaggerOperation
argument_list|(
name|swagger
argument_list|,
name|verb
argument_list|,
name|path
argument_list|)
decl_stmt|;
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
literal|"Swagger api-doc does not contain operation for "
operator|+
name|verb
operator|+
literal|":"
operator|+
name|path
argument_list|)
throw|;
block|}
comment|// validate if we have the query parameters also
if|if
condition|(
name|queryParameters
operator|!=
literal|null
condition|)
block|{
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
operator|&&
name|param
operator|.
name|getRequired
argument_list|()
condition|)
block|{
comment|// check if we have the required query parameter defined
name|String
name|key
init|=
name|param
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|token
init|=
name|key
operator|+
literal|"="
decl_stmt|;
name|boolean
name|hasQuery
init|=
name|queryParameters
operator|.
name|contains
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|hasQuery
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Swagger api-doc does not contain query parameter "
operator|+
name|key
operator|+
literal|" for "
operator|+
name|verb
operator|+
literal|":"
operator|+
name|path
argument_list|)
throw|;
block|}
block|}
block|}
block|}
name|String
name|componentName
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"componentName"
argument_list|)
decl_stmt|;
name|Producer
name|producer
init|=
name|createHttpProducer
argument_list|(
name|camelContext
argument_list|,
name|swagger
argument_list|,
name|operation
argument_list|,
name|host
argument_list|,
name|verb
argument_list|,
name|path
argument_list|,
name|queryParameters
argument_list|,
name|produces
argument_list|,
name|consumes
argument_list|,
name|componentName
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
return|return
name|producer
return|;
block|}
DECL|method|loadSwaggerModel (CamelContext camelContext, String apiDoc)
specifier|private
name|Swagger
name|loadSwaggerModel
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|apiDoc
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
argument_list|,
name|apiDoc
argument_list|)
decl_stmt|;
try|try
block|{
name|SwaggerParser
name|parser
init|=
operator|new
name|SwaggerParser
argument_list|()
decl_stmt|;
name|String
name|json
init|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loaded swagger api-doc:\n{}"
argument_list|,
name|json
argument_list|)
expr_stmt|;
return|return
name|parser
operator|.
name|parse
argument_list|(
name|json
argument_list|)
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getSwaggerOperation (Swagger swagger, String verb, String path)
specifier|private
name|Operation
name|getSwaggerOperation
parameter_list|(
name|Swagger
name|swagger
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|path
parameter_list|)
block|{
comment|// path may include base path so skip that
name|String
name|basePath
init|=
name|swagger
operator|.
name|getBasePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|basePath
operator|!=
literal|null
operator|&&
name|path
operator|.
name|startsWith
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|basePath
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
DECL|method|createHttpProducer (CamelContext camelContext, Swagger swagger, Operation operation, String host, String verb, String path, String queryParameters, String consumes, String produces, String componentName, Map<String, Object> parameters)
specifier|private
name|Producer
name|createHttpProducer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Swagger
name|swagger
parameter_list|,
name|Operation
name|operation
parameter_list|,
name|String
name|host
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|queryParameters
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|String
name|componentName
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
operator|(
name|RestProducerFactory
operator|)
name|parameters
operator|.
name|remove
argument_list|(
literal|"restProducerFactory"
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|produces
operator|==
literal|null
condition|)
block|{
name|CollectionStringBuffer
name|csb
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
name|csb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
name|produces
operator|=
name|csb
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|csb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|consumes
operator|==
literal|null
condition|)
block|{
name|CollectionStringBuffer
name|csb
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
name|getConsumes
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
name|csb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
name|consumes
operator|=
name|csb
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|csb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|String
name|basePath
decl_stmt|;
name|String
name|uriTemplate
decl_stmt|;
if|if
condition|(
name|host
operator|==
literal|null
condition|)
block|{
comment|// if no explicit host has been configured then use host and base path from the swagger api-doc
name|host
operator|=
name|swagger
operator|.
name|getHost
argument_list|()
expr_stmt|;
name|basePath
operator|=
name|swagger
operator|.
name|getBasePath
argument_list|()
expr_stmt|;
name|uriTemplate
operator|=
name|path
expr_stmt|;
block|}
else|else
block|{
comment|// path includes also uri template
name|basePath
operator|=
name|path
expr_stmt|;
name|uriTemplate
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|factory
operator|.
name|createProducer
argument_list|(
name|camelContext
argument_list|,
name|host
argument_list|,
name|verb
argument_list|,
name|basePath
argument_list|,
name|uriTemplate
argument_list|,
name|queryParameters
argument_list|,
name|consumes
argument_list|,
name|produces
argument_list|,
name|parameters
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

