begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.service.lra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|service
operator|.
name|lra
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLEncoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|Optional
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|service
operator|.
name|lra
operator|.
name|LRAConstants
operator|.
name|URL_COMPENSATION_KEY
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
name|service
operator|.
name|lra
operator|.
name|LRAConstants
operator|.
name|URL_COMPLETION_KEY
import|;
end_import

begin_class
DECL|class|LRAUrlBuilder
specifier|public
class|class
name|LRAUrlBuilder
block|{
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|path
specifier|private
name|String
name|path
init|=
literal|""
decl_stmt|;
DECL|field|query
specifier|private
name|String
name|query
init|=
literal|""
decl_stmt|;
DECL|method|LRAUrlBuilder ()
specifier|public
name|LRAUrlBuilder
parameter_list|()
block|{     }
DECL|method|LRAUrlBuilder (String host, String path, String query)
specifier|public
name|LRAUrlBuilder
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|host (String host)
specifier|public
name|LRAUrlBuilder
name|host
parameter_list|(
name|String
name|host
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|host
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Host already set"
argument_list|)
throw|;
block|}
name|LRAUrlBuilder
name|copy
init|=
name|copy
argument_list|()
decl_stmt|;
name|copy
operator|.
name|host
operator|=
name|host
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|path (String path)
specifier|public
name|LRAUrlBuilder
name|path
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|LRAUrlBuilder
name|copy
init|=
name|copy
argument_list|()
decl_stmt|;
name|copy
operator|.
name|path
operator|=
name|joinPath
argument_list|(
name|this
operator|.
name|path
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|compensation (Optional<Endpoint> endpoint)
specifier|public
name|LRAUrlBuilder
name|compensation
parameter_list|(
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|compensation
argument_list|(
name|endpoint
operator|.
name|get
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
return|return
name|this
return|;
block|}
DECL|method|compensation (String uri)
specifier|public
name|LRAUrlBuilder
name|compensation
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|this
operator|.
name|query
argument_list|(
name|URL_COMPENSATION_KEY
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|completion (Optional<Endpoint> endpoint)
specifier|public
name|LRAUrlBuilder
name|completion
parameter_list|(
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|completion
argument_list|(
name|endpoint
operator|.
name|get
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
return|return
name|this
return|;
block|}
DECL|method|completion (String uri)
specifier|public
name|LRAUrlBuilder
name|completion
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|this
operator|.
name|query
argument_list|(
name|URL_COMPLETION_KEY
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|options (Map<String, ?> options)
specifier|public
name|LRAUrlBuilder
name|options
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
parameter_list|)
block|{
name|LRAUrlBuilder
name|result
init|=
name|this
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|options
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|result
operator|=
name|result
operator|.
name|query
argument_list|(
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
return|return
name|result
return|;
block|}
DECL|method|query (String key, Object value)
specifier|public
name|LRAUrlBuilder
name|query
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|LRAUrlBuilder
name|copy
init|=
name|copy
argument_list|()
decl_stmt|;
try|try
block|{
name|key
operator|=
name|URLEncoder
operator|.
name|encode
argument_list|(
name|toNonnullString
argument_list|(
name|key
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|value
operator|=
name|URLEncoder
operator|.
name|encode
argument_list|(
name|toNonnullString
argument_list|(
name|value
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|copy
operator|.
name|query
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|copy
operator|.
name|query
operator|+=
literal|"?"
expr_stmt|;
block|}
else|else
block|{
name|copy
operator|.
name|query
operator|+=
literal|"&"
expr_stmt|;
block|}
name|copy
operator|.
name|query
operator|+=
name|key
operator|+
literal|"="
operator|+
name|value
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|copy
return|;
block|}
DECL|method|build ()
specifier|public
name|String
name|build
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|host
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Host not set"
argument_list|)
throw|;
block|}
return|return
name|joinPath
argument_list|(
name|this
operator|.
name|host
argument_list|,
name|this
operator|.
name|path
argument_list|)
operator|+
name|query
return|;
block|}
DECL|method|joinPath (String first, String second)
specifier|private
name|String
name|joinPath
parameter_list|(
name|String
name|first
parameter_list|,
name|String
name|second
parameter_list|)
block|{
name|first
operator|=
name|toNonnullString
argument_list|(
name|first
argument_list|)
expr_stmt|;
name|second
operator|=
name|toNonnullString
argument_list|(
name|second
argument_list|)
expr_stmt|;
while|while
condition|(
name|first
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|first
operator|=
name|first
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|first
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|second
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|second
operator|=
name|second
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|first
operator|+
literal|"/"
operator|+
name|second
return|;
block|}
DECL|method|toNonnullString (Object obj)
specifier|private
name|String
name|toNonnullString
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|!=
literal|null
condition|?
name|obj
operator|.
name|toString
argument_list|()
else|:
literal|""
return|;
block|}
DECL|method|copy ()
specifier|private
name|LRAUrlBuilder
name|copy
parameter_list|()
block|{
return|return
operator|new
name|LRAUrlBuilder
argument_list|(
name|this
operator|.
name|host
argument_list|,
name|this
operator|.
name|path
argument_list|,
name|this
operator|.
name|query
argument_list|)
return|;
block|}
block|}
end_class

end_unit

