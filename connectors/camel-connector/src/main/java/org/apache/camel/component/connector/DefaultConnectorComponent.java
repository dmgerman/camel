begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.connector
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|connector
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|LineNumberReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Enumeration
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
name|Matcher
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
name|catalog
operator|.
name|CamelCatalog
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
name|catalog
operator|.
name|DefaultCamelCatalog
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
name|DefaultComponent
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
name|IntrospectionSupport
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

begin_comment
comment|/**  * Base class for Camel Connector components.  */
end_comment

begin_class
DECL|class|DefaultConnectorComponent
specifier|public
specifier|abstract
class|class
name|DefaultConnectorComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|JAVA_TYPE_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|JAVA_TYPE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"javaType\"\\s?:\\s?\"([\\w|.]+)\".*"
argument_list|)
decl_stmt|;
DECL|field|BASE_JAVA_TYPE_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|BASE_JAVA_TYPE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"baseJavaType\"\\s?:\\s?\"([\\w|.]+)\".*"
argument_list|)
decl_stmt|;
DECL|field|BASE_SCHEMA_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|BASE_SCHEMA_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"baseScheme\"\\s?:\\s?\"([\\w|.]+)\".*"
argument_list|)
decl_stmt|;
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|catalog
specifier|private
specifier|final
name|CamelCatalog
name|catalog
init|=
operator|new
name|DefaultCamelCatalog
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|componentName
specifier|private
specifier|final
name|String
name|componentName
decl_stmt|;
DECL|field|className
specifier|private
specifier|final
name|String
name|className
decl_stmt|;
DECL|field|lines
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|lines
decl_stmt|;
DECL|method|DefaultConnectorComponent (String componentName, String className)
specifier|public
name|DefaultConnectorComponent
parameter_list|(
name|String
name|componentName
parameter_list|,
name|String
name|className
parameter_list|)
block|{
name|this
operator|.
name|componentName
operator|=
name|componentName
expr_stmt|;
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
comment|// add to catalog
name|catalog
operator|.
name|addComponent
argument_list|(
name|componentName
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|String
name|scheme
init|=
name|extractBaseScheme
argument_list|(
name|lines
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|defaultOptions
init|=
name|extractEndpointDefaultValues
argument_list|(
name|lines
argument_list|)
decl_stmt|;
comment|// gather all options to use when building the delegate uri
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// default options from connector json
if|if
condition|(
operator|!
name|defaultOptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|options
operator|.
name|putAll
argument_list|(
name|defaultOptions
argument_list|)
expr_stmt|;
block|}
comment|// options from query parameters
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
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
literal|null
decl_stmt|;
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
name|value
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|options
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|parameters
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// add extra options from remaining (context-path)
if|if
condition|(
name|remaining
operator|!=
literal|null
condition|)
block|{
name|String
name|targetUri
init|=
name|scheme
operator|+
literal|":"
operator|+
name|remaining
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extra
init|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
name|targetUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|extra
operator|!=
literal|null
operator|&&
operator|!
name|extra
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|options
operator|.
name|putAll
argument_list|(
name|extra
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|delegateUri
init|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
name|scheme
argument_list|,
name|options
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Connector resolved: {} -> {}"
argument_list|,
name|uri
argument_list|,
name|delegateUri
argument_list|)
expr_stmt|;
name|Endpoint
name|delegate
init|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|delegateUri
argument_list|)
decl_stmt|;
return|return
operator|new
name|DefaultConnectorEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|delegate
argument_list|)
return|;
block|}
DECL|method|findCamelConnectorJSonSchema ()
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|findCamelConnectorJSonSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResources
argument_list|(
literal|"camel-connector.json"
argument_list|)
decl_stmt|;
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|urls
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|loadFile
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|javaType
init|=
name|extractJavaType
argument_list|(
name|lines
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Found camel-connector.json in classpath with javaType: {}"
argument_list|,
name|javaType
argument_list|)
expr_stmt|;
if|if
condition|(
name|className
operator|.
name|equals
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
return|return
name|lines
return|;
block|}
block|}
block|}
return|return
literal|null
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
name|this
operator|.
name|lines
operator|=
name|findCamelConnectorJSonSchema
argument_list|()
expr_stmt|;
if|if
condition|(
name|lines
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find camel-connector.json in classpath for connector "
operator|+
name|componentName
argument_list|)
throw|;
block|}
comment|// it may be a custom component so we need to register this in the camel catalog also
name|String
name|scheme
init|=
name|extractBaseScheme
argument_list|(
name|lines
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|catalog
operator|.
name|findComponentNames
argument_list|()
operator|.
name|contains
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|String
name|javaType
init|=
name|extractBaseJavaType
argument_list|(
name|lines
argument_list|)
decl_stmt|;
name|catalog
operator|.
name|addComponent
argument_list|(
name|scheme
argument_list|,
name|javaType
argument_list|)
expr_stmt|;
block|}
comment|// the connector may have default values for the component level also
comment|// and if so we need to prepare these values and set on this component before we can start
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|defaultOptions
init|=
name|extractComponentDefaultValues
argument_list|(
name|lines
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|defaultOptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|defaultOptions
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
comment|// also support {{ }} placeholders so resolve those first
name|value
operator|=
name|getCamelContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Using component option: {}={}"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|this
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Starting connector: {}"
argument_list|,
name|componentName
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
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
name|log
operator|.
name|debug
argument_list|(
literal|"Stopping connector: {}"
argument_list|,
name|componentName
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|// --------------------------------------------------------------
DECL|method|extractComponentDefaultValues (List<String> lines)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extractComponentDefaultValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// extract the default options
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"\"componentValues\":"
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|found
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|found
condition|)
block|{
name|int
name|pos
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|line
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|endsWith
argument_list|(
literal|","
argument_list|)
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|key
operator|=
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|value
operator|=
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|extractEndpointDefaultValues (List<String> lines)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extractEndpointDefaultValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// extract the default options
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"\"endpointValues\":"
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|found
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|found
condition|)
block|{
name|int
name|pos
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|line
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|endsWith
argument_list|(
literal|","
argument_list|)
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|key
operator|=
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|value
operator|=
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|loadFile (InputStream fis)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|loadFile
parameter_list|(
name|InputStream
name|fis
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|LineNumberReader
name|reader
init|=
operator|new
name|LineNumberReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|fis
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
do|do
block|{
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
do|while
condition|(
name|line
operator|!=
literal|null
condition|)
do|;
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|lines
return|;
block|}
DECL|method|extractJavaType (List<String> json)
specifier|private
name|String
name|extractJavaType
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|json
parameter_list|)
block|{
for|for
control|(
name|String
name|line
range|:
name|json
control|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
name|Matcher
name|matcher
init|=
name|JAVA_TYPE_PATTERN
operator|.
name|matcher
argument_list|(
name|line
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|extractBaseJavaType (List<String> json)
specifier|private
name|String
name|extractBaseJavaType
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|json
parameter_list|)
block|{
for|for
control|(
name|String
name|line
range|:
name|json
control|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
name|Matcher
name|matcher
init|=
name|BASE_JAVA_TYPE_PATTERN
operator|.
name|matcher
argument_list|(
name|line
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|extractBaseScheme (List<String> json)
specifier|private
name|String
name|extractBaseScheme
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|json
parameter_list|)
block|{
for|for
control|(
name|String
name|line
range|:
name|json
control|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
name|Matcher
name|matcher
init|=
name|BASE_SCHEMA_PATTERN
operator|.
name|matcher
argument_list|(
name|line
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

