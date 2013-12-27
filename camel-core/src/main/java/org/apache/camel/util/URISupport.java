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
name|Iterator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * URI utilities.  *  * @version   */
end_comment

begin_class
DECL|class|URISupport
specifier|public
specifier|final
class|class
name|URISupport
block|{
DECL|field|RAW_TOKEN_START
specifier|public
specifier|static
specifier|final
name|String
name|RAW_TOKEN_START
init|=
literal|"RAW("
decl_stmt|;
DECL|field|RAW_TOKEN_END
specifier|public
specifier|static
specifier|final
name|String
name|RAW_TOKEN_END
init|=
literal|")"
decl_stmt|;
comment|// Match any key-value pair in the URI query string whose key contains
comment|// "passphrase" or "password" or secret key (case-insensitive).
comment|// First capture group is the key, second is the value.
DECL|field|SECRETS
specifier|private
specifier|static
specifier|final
name|Pattern
name|SECRETS
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"([?&][^=]*(?:passphrase|password|secretKey)[^=]*)=([^&]*)"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
comment|// Match the user password in the URI as second capture group
comment|// (applies to URI with authority component and userinfo token in the form "user:password").
DECL|field|USERINFO_PASSWORD
specifier|private
specifier|static
specifier|final
name|Pattern
name|USERINFO_PASSWORD
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(.*://.*:)(.*)(@)"
argument_list|)
decl_stmt|;
comment|// Match the user password in the URI path as second capture group
comment|// (applies to URI path with authority component and userinfo token in the form "user:password").
DECL|field|PATH_USERINFO_PASSWORD
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATH_USERINFO_PASSWORD
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(.*:)(.*)(@)"
argument_list|)
decl_stmt|;
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
comment|/**      * Removes detected sensitive information (such as passwords) from the URI and returns the result.      *      * @param uri The uri to sanitize.      * @see #SECRETS for the matched pattern      *      * @return Returns null if the uri is null, otherwise the URI with the passphrase, password or secretKey sanitized.      */
DECL|method|sanitizeUri (String uri)
specifier|public
specifier|static
name|String
name|sanitizeUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
comment|// use xxxxx as replacement as that works well with JMX also
name|String
name|sanitized
init|=
name|uri
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|sanitized
operator|=
name|SECRETS
operator|.
name|matcher
argument_list|(
name|sanitized
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"$1=xxxxxx"
argument_list|)
expr_stmt|;
name|sanitized
operator|=
name|USERINFO_PASSWORD
operator|.
name|matcher
argument_list|(
name|sanitized
argument_list|)
operator|.
name|replaceFirst
argument_list|(
literal|"$1xxxxxx$3"
argument_list|)
expr_stmt|;
block|}
return|return
name|sanitized
return|;
block|}
comment|/**      * Removes detected sensitive information (such as passwords) from the      *<em>path part</em> of an URI (that is, the part without the query      * parameters or component prefix) and returns the result.      *       * @param path the URI path to sanitize      * @return null if the path is null, otherwise the sanitized path      */
DECL|method|sanitizePath (String path)
specifier|public
specifier|static
name|String
name|sanitizePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|String
name|sanitized
init|=
name|path
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|sanitized
operator|=
name|PATH_USERINFO_PASSWORD
operator|.
name|matcher
argument_list|(
name|sanitized
argument_list|)
operator|.
name|replaceFirst
argument_list|(
literal|"$1xxxxxx$3"
argument_list|)
expr_stmt|;
block|}
return|return
name|sanitized
return|;
block|}
comment|/**      * Parses the query part of the uri (eg the parameters).      *<p/>      * The URI parameters will by default be URI encoded. However you can define a parameter      * values with the syntax:<tt>key=RAW(value)</tt> which tells Camel to not encode the value,      * and use the value as is (eg key=value) and the value has<b>not</b> been encoded.      *      * @param uri the uri      * @return the parameters, or an empty map if no parameters (eg never null)      * @throws URISyntaxException is thrown if uri has invalid syntax.      * @see #RAW_TOKEN_START      * @see #RAW_TOKEN_END      */
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
return|return
name|parseQuery
argument_list|(
name|uri
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Parses the query part of the uri (eg the parameters).      *<p/>      * The URI parameters will by default be URI encoded. However you can define a parameter      * values with the syntax:<tt>key=RAW(value)</tt> which tells Camel to not encode the value,      * and use the value as is (eg key=value) and the value has<b>not</b> been encoded.      *      * @param uri the uri      * @param useRaw whether to force using raw values      * @return the parameters, or an empty map if no parameters (eg never null)      * @throws URISyntaxException is thrown if uri has invalid syntax.      * @see #RAW_TOKEN_START      * @see #RAW_TOKEN_END      */
DECL|method|parseQuery (String uri, boolean useRaw)
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
parameter_list|,
name|boolean
name|useRaw
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|uri
argument_list|)
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
comment|// need to parse the uri query parameters manually as we cannot rely on splitting by&,
comment|// as& can be used in a parameter value as well.
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
name|boolean
name|isKey
init|=
literal|true
decl_stmt|;
name|boolean
name|isValue
init|=
literal|false
decl_stmt|;
name|boolean
name|isRaw
init|=
literal|false
decl_stmt|;
name|StringBuilder
name|key
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringBuilder
name|value
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// parse the uri parameters char by char
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|uri
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
comment|// current char
name|char
name|ch
init|=
name|uri
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// look ahead of the next char
name|char
name|next
decl_stmt|;
if|if
condition|(
name|i
operator|<
name|uri
operator|.
name|length
argument_list|()
operator|-
literal|2
condition|)
block|{
name|next
operator|=
name|uri
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|next
operator|=
literal|'\u0000'
expr_stmt|;
block|}
comment|// are we a raw value
name|isRaw
operator|=
name|value
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
name|RAW_TOKEN_START
argument_list|)
expr_stmt|;
comment|// if we are in raw mode, then we keep adding until we hit the end marker
if|if
condition|(
name|isRaw
condition|)
block|{
if|if
condition|(
name|isKey
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isValue
condition|)
block|{
name|value
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
comment|// we only end the raw marker if its )& or at the end of the value
name|boolean
name|end
init|=
name|ch
operator|==
name|RAW_TOKEN_END
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|&&
operator|(
name|next
operator|==
literal|'&'
operator|||
name|next
operator|==
literal|'\u0000'
operator|)
decl_stmt|;
if|if
condition|(
name|end
condition|)
block|{
comment|// raw value end, so add that as a parameter, and reset flags
name|addParameter
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|,
name|rc
argument_list|,
name|useRaw
operator|||
name|isRaw
argument_list|)
expr_stmt|;
name|key
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|value
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|isKey
operator|=
literal|true
expr_stmt|;
name|isValue
operator|=
literal|false
expr_stmt|;
name|isRaw
operator|=
literal|false
expr_stmt|;
comment|// skip to next as we are in raw mode and have already added the value
name|i
operator|++
expr_stmt|;
block|}
continue|continue;
block|}
comment|// if its a key and there is a = sign then the key ends and we are in value mode
if|if
condition|(
name|isKey
operator|&&
name|ch
operator|==
literal|'='
condition|)
block|{
name|isKey
operator|=
literal|false
expr_stmt|;
name|isValue
operator|=
literal|true
expr_stmt|;
name|isRaw
operator|=
literal|false
expr_stmt|;
continue|continue;
block|}
comment|// the& denote parameter is ended
if|if
condition|(
name|ch
operator|==
literal|'&'
condition|)
block|{
comment|// parameter is ended, as we hit& separator
name|addParameter
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|,
name|rc
argument_list|,
name|useRaw
operator|||
name|isRaw
argument_list|)
expr_stmt|;
name|key
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|value
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|isKey
operator|=
literal|true
expr_stmt|;
name|isValue
operator|=
literal|false
expr_stmt|;
name|isRaw
operator|=
literal|false
expr_stmt|;
continue|continue;
block|}
comment|// regular char so add it to the key or value
if|if
condition|(
name|isKey
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isValue
condition|)
block|{
name|value
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
block|}
comment|// any left over parameters, then add that
if|if
condition|(
name|key
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|addParameter
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|,
name|rc
argument_list|,
name|useRaw
operator|||
name|isRaw
argument_list|)
expr_stmt|;
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
DECL|method|addParameter (String name, String value, Map<String, Object> map, boolean isRaw)
specifier|private
specifier|static
name|void
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|boolean
name|isRaw
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|name
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|name
argument_list|,
name|CHARSET
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isRaw
condition|)
block|{
comment|// need to replace % with %25
name|value
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|value
operator|.
name|replaceAll
argument_list|(
literal|"%"
argument_list|,
literal|"%25"
argument_list|)
argument_list|,
name|CHARSET
argument_list|)
expr_stmt|;
block|}
comment|// does the key already exist?
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// yes it does, so make sure we can support multiple values, but using a list
comment|// to hold the multiple values
name|Object
name|existing
init|=
name|map
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
decl_stmt|;
if|if
condition|(
name|existing
operator|instanceof
name|List
condition|)
block|{
name|list
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|existing
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// create a new list to hold the multiple values
name|list
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|String
name|s
init|=
name|existing
operator|!=
literal|null
condition|?
name|existing
operator|.
name|toString
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
name|list
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Parses the query parameters of the uri (eg the query part).      *      * @param uri the uri      * @return the parameters, or an empty map if no parameters (eg never null)      * @throws URISyntaxException is thrown if uri has invalid syntax.      */
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
comment|/**      * Traverses the given parameters, and resolve any parameter values which uses the RAW token      * syntax:<tt>key=RAW(value)</tt>. This method will then remove the RAW tokens, and replace      * the content of the value, with just the value.      *      * @param parameters the uri parameters      * @see #parseQuery(String)      * @see #RAW_TOKEN_START      * @see #RAW_TOKEN_END      */
DECL|method|resolveRawParameterValues (Map<String, Object> parameters)
specifier|public
specifier|static
name|void
name|resolveRawParameterValues
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
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
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
name|RAW_TOKEN_START
argument_list|)
operator|&&
name|value
operator|.
name|endsWith
argument_list|(
name|RAW_TOKEN_END
argument_list|)
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|4
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Creates a URI with the given query      *      * @param uri the uri      * @param query the query to append to the uri      * @return uri with the query appended      * @throws URISyntaxException is thrown if uri has invalid syntax.      */
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
comment|// assemble string as new uri and replace parameters with the query instead
name|String
name|s
init|=
name|uri
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|before
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|s
argument_list|,
literal|"?"
argument_list|)
decl_stmt|;
if|if
condition|(
name|before
operator|!=
literal|null
condition|)
block|{
name|s
operator|=
name|before
expr_stmt|;
block|}
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|s
operator|=
name|s
operator|+
literal|"?"
operator|+
name|query
expr_stmt|;
block|}
if|if
condition|(
operator|(
operator|!
name|s
operator|.
name|contains
argument_list|(
literal|"#"
argument_list|)
operator|)
operator|&&
operator|(
name|uri
operator|.
name|getFragment
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
name|s
operator|=
name|s
operator|+
literal|"#"
operator|+
name|uri
operator|.
name|getFragment
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|URI
argument_list|(
name|s
argument_list|)
return|;
block|}
comment|/**      * Strips the prefix from the value.      *<p/>      * Returns the value as-is if not starting with the prefix.      *      * @param value  the value      * @param prefix the prefix to remove from value      * @return the value without the prefix      */
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
operator|!=
literal|null
operator|&&
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
comment|/**      * Assembles a query from the given map.      *      * @param options  the map with the options (eg key/value pairs)      * @return a query string with<tt>key1=value&key2=value2&...</tt>, or an empty string if there is no options.      * @throws URISyntaxException is thrown if uri has invalid syntax.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createQueryString (Map<String, Object> options)
specifier|public
specifier|static
name|String
name|createQueryString
parameter_list|(
name|Map
argument_list|<
name|String
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
name|Object
name|value
init|=
name|options
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
comment|// the value may be a list since the same key has multiple values
if|if
condition|(
name|value
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|value
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|list
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|s
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|appendQueryStringParameter
argument_list|(
name|key
argument_list|,
name|s
argument_list|,
name|rc
argument_list|)
expr_stmt|;
comment|// append& separator if there is more in the list to append
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|rc
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// use the value as a String
name|String
name|s
init|=
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
decl_stmt|;
name|appendQueryStringParameter
argument_list|(
name|key
argument_list|,
name|s
argument_list|,
name|rc
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
DECL|method|appendQueryStringParameter (String key, String value, StringBuilder rc)
specifier|private
specifier|static
name|void
name|appendQueryStringParameter
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|StringBuilder
name|rc
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
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
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
name|RAW_TOKEN_START
argument_list|)
operator|&&
name|value
operator|.
name|endsWith
argument_list|(
name|RAW_TOKEN_END
argument_list|)
condition|)
block|{
comment|// do not encode RAW parameters
name|rc
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
block|}
comment|/**      * Creates a URI from the original URI and the remaining parameters      *<p/>      * Used by various Camel components      */
DECL|method|createRemainingURI (URI originalURI, Map<String, Object> params)
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
name|String
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
comment|/**      * Normalizes the uri by reordering the parameters so they are sorted and thus      * we can use the uris for endpoint matching.      *<p/>      * The URI parameters will by default be URI encoded. However you can define a parameter      * values with the syntax:<tt>key=RAW(value)</tt> which tells Camel to not encode the value,      * and use the value as is (eg key=value) and the value has<b>not</b> been encoded.      *      * @param uri the uri      * @return the normalized uri      * @throws URISyntaxException in thrown if the uri syntax is invalid      * @throws UnsupportedEncodingException is thrown if encoding error      * @see #RAW_TOKEN_START      * @see #RAW_TOKEN_END      */
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
throws|,
name|UnsupportedEncodingException
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
comment|// when the path has ?
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
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
if|if
condition|(
name|u
operator|.
name|getScheme
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
condition|)
block|{
name|path
operator|=
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|=
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
comment|// okay if we have user info in the path and they use @ in username or password,
comment|// then we need to encode them (but leave the last @ sign before the hostname)
comment|// this is needed as Camel end users may not encode their user info properly, but expect
comment|// this to work out of the box with Camel, and hence we need to fix it for them
name|String
name|userInfoPath
init|=
name|path
decl_stmt|;
if|if
condition|(
name|userInfoPath
operator|.
name|contains
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|userInfoPath
operator|=
name|userInfoPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|userInfoPath
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringHelper
operator|.
name|countChar
argument_list|(
name|userInfoPath
argument_list|,
literal|'@'
argument_list|)
operator|>
literal|1
condition|)
block|{
name|int
name|max
init|=
name|userInfoPath
operator|.
name|lastIndexOf
argument_list|(
literal|'@'
argument_list|)
decl_stmt|;
name|String
name|before
init|=
name|userInfoPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|max
argument_list|)
decl_stmt|;
comment|// after must be from original path
name|String
name|after
init|=
name|path
operator|.
name|substring
argument_list|(
name|max
argument_list|)
decl_stmt|;
comment|// replace the @ with %40
name|before
operator|=
name|StringHelper
operator|.
name|replaceAll
argument_list|(
name|before
argument_list|,
literal|"@"
argument_list|,
literal|"%40"
argument_list|)
expr_stmt|;
name|path
operator|=
name|before
operator|+
name|after
expr_stmt|;
block|}
comment|// in case there are parameters we should reorder them
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
name|String
argument_list|,
name|Object
argument_list|>
name|sorted
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
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

