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
name|ArrayList
import|;
end_import

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
name|Comparator
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
name|Locale
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
name|Response
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
name|BodyParameter
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
name|FormParameter
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
name|HeaderParameter
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
name|models
operator|.
name|parameters
operator|.
name|PathParameter
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
name|QueryParameter
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
name|model
operator|.
name|rest
operator|.
name|RestDefinition
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
name|model
operator|.
name|rest
operator|.
name|RestOperationParamDefinition
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
name|model
operator|.
name|rest
operator|.
name|RestOperationResponseMsgDefinition
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
name|model
operator|.
name|rest
operator|.
name|RestParamType
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
name|model
operator|.
name|rest
operator|.
name|VerbDefinition
import|;
end_import

begin_class
DECL|class|RestSwaggerReader
specifier|public
class|class
name|RestSwaggerReader
block|{
DECL|method|read (RestDefinition rest, BeanConfig config)
specifier|public
name|Swagger
name|read
parameter_list|(
name|RestDefinition
name|rest
parameter_list|,
name|BeanConfig
name|config
parameter_list|)
block|{
name|Swagger
name|swagger
init|=
operator|new
name|Swagger
argument_list|()
decl_stmt|;
name|config
operator|.
name|configure
argument_list|(
name|swagger
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|VerbDefinition
argument_list|>
name|verbs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|rest
operator|.
name|getVerbs
argument_list|()
argument_list|)
decl_stmt|;
comment|// must sort the verbs by uri so we group them together when an uri has multiple operations
name|Collections
operator|.
name|sort
argument_list|(
name|verbs
argument_list|,
operator|new
name|VerbOrdering
argument_list|()
argument_list|)
expr_stmt|;
comment|// used during gathering of apis
name|List
argument_list|<
name|Path
argument_list|>
name|paths
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|basePath
init|=
name|rest
operator|.
name|getPath
argument_list|()
decl_stmt|;
for|for
control|(
name|VerbDefinition
name|verb
range|:
name|verbs
control|)
block|{
comment|// the method must be in lower case
name|String
name|method
init|=
name|verb
operator|.
name|asVerb
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
comment|// operation path is a key
name|String
name|opPath
init|=
name|getPath
argument_list|(
name|basePath
argument_list|,
name|verb
operator|.
name|getUri
argument_list|()
argument_list|)
decl_stmt|;
name|Operation
name|op
init|=
operator|new
name|Operation
argument_list|()
decl_stmt|;
name|Path
name|path
init|=
name|swagger
operator|.
name|getPath
argument_list|(
name|opPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|path
operator|=
operator|new
name|Path
argument_list|()
expr_stmt|;
name|paths
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
name|path
operator|=
name|path
operator|.
name|set
argument_list|(
name|method
argument_list|,
name|op
argument_list|)
expr_stmt|;
if|if
condition|(
name|verb
operator|.
name|getConsumes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|op
operator|.
name|consumes
argument_list|(
name|verb
operator|.
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|rest
operator|.
name|getConsumes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|op
operator|.
name|consumes
argument_list|(
name|rest
operator|.
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|verb
operator|.
name|getProduces
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|op
operator|.
name|produces
argument_list|(
name|verb
operator|.
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|rest
operator|.
name|getProduces
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|op
operator|.
name|produces
argument_list|(
name|rest
operator|.
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|verb
operator|.
name|getDescriptionText
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|op
operator|.
name|summary
argument_list|(
name|verb
operator|.
name|getDescriptionText
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|RestOperationParamDefinition
name|param
range|:
name|verb
operator|.
name|getParams
argument_list|()
control|)
block|{
name|Parameter
name|parameter
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|param
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|RestParamType
operator|.
name|body
argument_list|)
condition|)
block|{
name|parameter
operator|=
operator|new
name|BodyParameter
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|param
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|RestParamType
operator|.
name|form
argument_list|)
condition|)
block|{
name|parameter
operator|=
operator|new
name|FormParameter
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|param
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|RestParamType
operator|.
name|header
argument_list|)
condition|)
block|{
name|parameter
operator|=
operator|new
name|HeaderParameter
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|param
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|RestParamType
operator|.
name|path
argument_list|)
condition|)
block|{
name|parameter
operator|=
operator|new
name|PathParameter
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|param
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|RestParamType
operator|.
name|query
argument_list|)
condition|)
block|{
name|parameter
operator|=
operator|new
name|QueryParameter
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|parameter
operator|!=
literal|null
condition|)
block|{
name|parameter
operator|.
name|setName
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|parameter
operator|.
name|setAccess
argument_list|(
name|param
operator|.
name|getAccess
argument_list|()
argument_list|)
expr_stmt|;
name|parameter
operator|.
name|setDescription
argument_list|(
name|param
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|parameter
operator|.
name|setRequired
argument_list|(
name|param
operator|.
name|getRequired
argument_list|()
argument_list|)
expr_stmt|;
name|op
operator|.
name|addParameter
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|RestOperationResponseMsgDefinition
name|msg
range|:
name|verb
operator|.
name|getResponseMsgs
argument_list|()
control|)
block|{
name|Response
name|response
init|=
operator|new
name|Response
argument_list|()
decl_stmt|;
name|response
operator|.
name|setDescription
argument_list|(
name|msg
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|op
operator|.
name|addResponse
argument_list|(
literal|""
operator|+
name|msg
operator|.
name|getCode
argument_list|()
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|// add path
name|swagger
operator|.
name|path
argument_list|(
name|opPath
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
return|return
name|swagger
return|;
block|}
DECL|method|getPath (String basePath, String uri)
specifier|private
name|String
name|getPath
parameter_list|(
name|String
name|basePath
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
comment|// TODO: slash check and avoid double slash and all that
return|return
name|basePath
operator|+
literal|"/"
operator|+
name|uri
return|;
block|}
comment|/**      * To sort the rest operations      */
DECL|class|VerbOrdering
specifier|private
specifier|static
class|class
name|VerbOrdering
implements|implements
name|Comparator
argument_list|<
name|VerbDefinition
argument_list|>
block|{
annotation|@
name|Override
DECL|method|compare (VerbDefinition a, VerbDefinition b)
specifier|public
name|int
name|compare
parameter_list|(
name|VerbDefinition
name|a
parameter_list|,
name|VerbDefinition
name|b
parameter_list|)
block|{
name|String
name|u1
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|a
operator|.
name|getUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// replace { with _ which comes before a when soring by char
name|u1
operator|=
name|a
operator|.
name|getUri
argument_list|()
operator|.
name|replace
argument_list|(
literal|"{"
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
block|}
name|String
name|u2
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|b
operator|.
name|getUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// replace { with _ which comes before a when soring by char
name|u2
operator|=
name|b
operator|.
name|getUri
argument_list|()
operator|.
name|replace
argument_list|(
literal|"{"
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
block|}
name|int
name|num
init|=
name|u1
operator|.
name|compareTo
argument_list|(
name|u2
argument_list|)
decl_stmt|;
if|if
condition|(
name|num
operator|==
literal|0
condition|)
block|{
comment|// same uri, so use http method as sorting
name|num
operator|=
name|a
operator|.
name|asVerb
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|asVerb
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|num
return|;
block|}
block|}
block|}
end_class

end_unit

