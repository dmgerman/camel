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
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
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

begin_comment
comment|/**  * URI utilities.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|URISupport
specifier|public
specifier|final
class|class
name|URISupport
block|{
DECL|field|CHARSET
specifier|private
specifier|static
specifier|final
name|String
name|CHARSET
init|=
literal|"UTF-8"
decl_stmt|;
DECL|method|URISupport ()
specifier|private
name|URISupport
parameter_list|()
block|{
comment|// Helper class
block|}
DECL|method|parseQuery (String uri)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parseQuery
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
comment|// must check for trailing& as the uri.split("&") will ignore those
if|if
condition|(
name|uri
operator|!=
literal|null
operator|&&
name|uri
operator|.
name|endsWith
argument_list|(
literal|"&"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|URISyntaxException
argument_list|(
name|uri
argument_list|,
literal|"Invalid uri syntax: Trailing& marker found. "
operator|+
literal|"Check the uri and remove the trailing& marker."
argument_list|)
throw|;
block|}
try|try
block|{
comment|// use a linked map so the parameters is in the same order
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|rc
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parameters
init|=
name|uri
operator|.
name|split
argument_list|(
literal|"&"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|parameter
range|:
name|parameters
control|)
block|{
name|int
name|p
init|=
name|parameter
operator|.
name|indexOf
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|>=
literal|0
condition|)
block|{
name|String
name|name
init|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|parameter
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p
argument_list|)
argument_list|,
name|CHARSET
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|parameter
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|)
argument_list|,
name|CHARSET
argument_list|)
decl_stmt|;
name|rc
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rc
operator|.
name|put
argument_list|(
name|parameter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|rc
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|URISyntaxException
name|se
init|=
operator|new
name|URISyntaxException
argument_list|(
name|e
operator|.
name|toString
argument_list|()
argument_list|,
literal|"Invalid encoding"
argument_list|)
decl_stmt|;
name|se
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|se
throw|;
block|}
block|}
DECL|method|parseParameters (URI uri)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parseParameters
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|query
init|=
name|uri
operator|.
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
name|String
name|schemeSpecificPart
init|=
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|schemeSpecificPart
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
comment|// return an empty map
return|return
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
name|query
operator|=
name|schemeSpecificPart
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|query
operator|=
name|stripPrefix
argument_list|(
name|query
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
return|return
name|parseQuery
argument_list|(
name|query
argument_list|)
return|;
block|}
comment|/**      * Creates a URI with the given query      */
DECL|method|createURIWithQuery (URI uri, String query)
specifier|public
specifier|static
name|URI
name|createURIWithQuery
parameter_list|(
name|URI
name|uri
parameter_list|,
name|String
name|query
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
operator|new
name|URI
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|,
name|uri
operator|.
name|getUserInfo
argument_list|()
argument_list|,
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|uri
operator|.
name|getPort
argument_list|()
argument_list|,
name|uri
operator|.
name|getPath
argument_list|()
argument_list|,
name|query
argument_list|,
name|uri
operator|.
name|getFragment
argument_list|()
argument_list|)
return|;
block|}
DECL|method|stripPrefix (String value, String prefix)
specifier|public
specifier|static
name|String
name|stripPrefix
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return
name|value
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
return|return
name|value
return|;
block|}
DECL|method|createQueryString (Map<Object, Object> options)
specifier|public
specifier|static
name|String
name|createQueryString
parameter_list|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
throws|throws
name|URISyntaxException
block|{
try|try
block|{
if|if
condition|(
name|options
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|rc
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|options
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|rc
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
block|}
name|String
name|key
init|=
operator|(
name|String
operator|)
name|o
decl_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|rc
operator|.
name|append
argument_list|(
name|URLEncoder
operator|.
name|encode
argument_list|(
name|key
argument_list|,
name|CHARSET
argument_list|)
argument_list|)
expr_stmt|;
comment|// only append if value is not null
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|rc
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
name|rc
operator|.
name|append
argument_list|(
name|URLEncoder
operator|.
name|encode
argument_list|(
name|value
argument_list|,
name|CHARSET
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|rc
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|URISyntaxException
name|se
init|=
operator|new
name|URISyntaxException
argument_list|(
name|e
operator|.
name|toString
argument_list|()
argument_list|,
literal|"Invalid encoding"
argument_list|)
decl_stmt|;
name|se
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|se
throw|;
block|}
block|}
comment|/**      * Creates a URI from the original URI and the remaining parameters      *<p/>      * Used by various Camel components      */
DECL|method|createRemainingURI (URI originalURI, Map<Object, Object> params)
specifier|public
specifier|static
name|URI
name|createRemainingURI
parameter_list|(
name|URI
name|originalURI
parameter_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|s
init|=
name|createQueryString
argument_list|(
name|params
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|s
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|createURIWithQuery
argument_list|(
name|originalURI
argument_list|,
name|s
argument_list|)
return|;
block|}
comment|/**      * Normalizes the uri by reordering the parameters so they are sorted and thus      * we can use the uris for endpoint matching.      *      * @param uri the uri      * @return the normalized uri      * @throws URISyntaxException in thrown if the uri syntax is invalid      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|normalizeUri (String uri)
specifier|public
specifier|static
name|String
name|normalizeUri
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|u
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
name|String
name|scheme
init|=
name|u
operator|.
name|getScheme
argument_list|()
decl_stmt|;
comment|// not possible to normalize
if|if
condition|(
name|scheme
operator|==
literal|null
operator|||
name|path
operator|==
literal|null
condition|)
block|{
return|return
name|uri
return|;
block|}
comment|// lets trim off any query arguments
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|int
name|idx
init|=
name|path
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
comment|// in case there are parameters we should reorder them
name|Map
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|u
argument_list|)
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// no parameters then just return
return|return
name|buildUri
argument_list|(
name|scheme
argument_list|,
name|path
argument_list|,
literal|null
argument_list|)
return|;
block|}
else|else
block|{
comment|// reorder parameters a..z
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|parameters
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|sorted
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
name|parameters
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|sorted
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|parameters
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// build uri object with sorted parameters
name|String
name|query
init|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|sorted
argument_list|)
decl_stmt|;
return|return
name|buildUri
argument_list|(
name|scheme
argument_list|,
name|path
argument_list|,
name|query
argument_list|)
return|;
block|}
block|}
DECL|method|buildUri (String scheme, String path, String query)
specifier|private
specifier|static
name|String
name|buildUri
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|query
parameter_list|)
block|{
comment|// must include :// to do a correct URI all components can work with
return|return
name|scheme
operator|+
literal|"://"
operator|+
name|path
operator|+
operator|(
name|query
operator|!=
literal|null
condition|?
literal|"?"
operator|+
name|query
else|:
literal|""
operator|)
return|;
block|}
block|}
end_class

end_unit

