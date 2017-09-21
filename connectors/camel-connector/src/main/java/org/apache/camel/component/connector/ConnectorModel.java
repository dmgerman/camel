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
name|IOException
import|;
end_import

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
name|Collections
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
name|function
operator|.
name|Supplier
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|function
operator|.
name|Suppliers
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
DECL|class|ConnectorModel
specifier|final
class|class
name|ConnectorModel
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ConnectorModel
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|NAME_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|NAME_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"name\"\\s?:\\s?\"([\\w|.-]+)\".*"
argument_list|)
decl_stmt|;
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
DECL|field|BASE_SCHEME_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|BASE_SCHEME_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"baseScheme\"\\s?:\\s?\"([\\w|.-]+)\".*"
argument_list|)
decl_stmt|;
DECL|field|SCHEDULER_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|SCHEDULER_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"scheduler\"\\s?:\\s?\"([\\w|.-]+)\".*"
argument_list|)
decl_stmt|;
DECL|field|INPUT_DATA_TYPE_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|INPUT_DATA_TYPE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"inputDataType\"\\s?:\\s?\"(\\*|[\\w|.:*]+)\".*"
argument_list|)
decl_stmt|;
DECL|field|OUTPUT_DATA_TYPE_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|OUTPUT_DATA_TYPE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"outputDataType\"\\s?:\\s?\"([\\w|.:*]+)\".*"
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
specifier|final
name|Supplier
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|lines
decl_stmt|;
DECL|field|baseScheme
specifier|private
name|String
name|baseScheme
decl_stmt|;
DECL|field|baseJavaType
specifier|private
name|String
name|baseJavaType
decl_stmt|;
DECL|field|scheduler
specifier|private
name|String
name|scheduler
decl_stmt|;
DECL|field|connectorJSon
specifier|private
name|String
name|connectorJSon
decl_stmt|;
DECL|field|connectorName
specifier|private
name|String
name|connectorName
decl_stmt|;
DECL|field|inputDataType
specifier|private
name|DataType
name|inputDataType
decl_stmt|;
DECL|field|outputDataType
specifier|private
name|DataType
name|outputDataType
decl_stmt|;
DECL|field|defaultComponentOptions
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|defaultComponentOptions
decl_stmt|;
DECL|field|defaultEndpointOptions
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|defaultEndpointOptions
decl_stmt|;
DECL|field|endpointOptions
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|endpointOptions
decl_stmt|;
DECL|field|componentOptions
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|componentOptions
decl_stmt|;
DECL|field|connectorOptions
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|connectorOptions
decl_stmt|;
DECL|method|ConnectorModel (String componentName, Class<?> componentClass)
name|ConnectorModel
parameter_list|(
name|String
name|componentName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|componentClass
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
name|componentClass
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|lines
operator|=
name|Suppliers
operator|.
name|memorize
argument_list|(
parameter_list|()
lambda|->
name|findCamelConnectorJSonSchema
argument_list|(
name|componentClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getComponentName ()
specifier|public
name|String
name|getComponentName
parameter_list|()
block|{
return|return
name|componentName
return|;
block|}
DECL|method|getClassName ()
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|className
return|;
block|}
DECL|method|getBaseScheme ()
specifier|public
name|String
name|getBaseScheme
parameter_list|()
block|{
if|if
condition|(
name|baseScheme
operator|==
literal|null
condition|)
block|{
name|baseScheme
operator|=
name|extractBaseScheme
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|baseScheme
return|;
block|}
DECL|method|getBaseJavaType ()
specifier|public
name|String
name|getBaseJavaType
parameter_list|()
block|{
if|if
condition|(
name|baseJavaType
operator|==
literal|null
condition|)
block|{
name|baseJavaType
operator|=
name|extractBaseJavaType
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|baseJavaType
return|;
block|}
DECL|method|getScheduler ()
specifier|public
name|String
name|getScheduler
parameter_list|()
block|{
if|if
condition|(
name|scheduler
operator|==
literal|null
condition|)
block|{
name|scheduler
operator|=
name|extractScheduler
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|scheduler
return|;
block|}
DECL|method|getConnectorName ()
specifier|public
name|String
name|getConnectorName
parameter_list|()
block|{
if|if
condition|(
name|connectorName
operator|==
literal|null
condition|)
block|{
name|connectorName
operator|=
name|extractName
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|connectorName
return|;
block|}
DECL|method|getConnectorJSon ()
specifier|public
name|String
name|getConnectorJSon
parameter_list|()
block|{
if|if
condition|(
name|connectorJSon
operator|==
literal|null
condition|)
block|{
name|connectorJSon
operator|=
name|lines
operator|.
name|get
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|connectorJSon
return|;
block|}
DECL|method|getDefaultComponentOptions ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDefaultComponentOptions
parameter_list|()
block|{
if|if
condition|(
name|defaultComponentOptions
operator|==
literal|null
condition|)
block|{
name|defaultComponentOptions
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|extractComponentDefaultValues
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|defaultComponentOptions
return|;
block|}
DECL|method|getDefaultEndpointOptions ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDefaultEndpointOptions
parameter_list|()
block|{
if|if
condition|(
name|defaultEndpointOptions
operator|==
literal|null
condition|)
block|{
name|defaultEndpointOptions
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|extractEndpointDefaultValues
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|defaultEndpointOptions
return|;
block|}
DECL|method|getEndpointOptions ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getEndpointOptions
parameter_list|()
block|{
if|if
condition|(
name|endpointOptions
operator|==
literal|null
condition|)
block|{
name|endpointOptions
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|extractEndpointOptions
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|endpointOptions
return|;
block|}
DECL|method|getComponentOptions ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getComponentOptions
parameter_list|()
block|{
if|if
condition|(
name|endpointOptions
operator|==
literal|null
condition|)
block|{
name|endpointOptions
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|extractComponentOptions
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|endpointOptions
return|;
block|}
DECL|method|getConnectorOptions ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getConnectorOptions
parameter_list|()
block|{
if|if
condition|(
name|connectorOptions
operator|==
literal|null
condition|)
block|{
name|connectorOptions
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|extractConnectorOptions
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|connectorOptions
return|;
block|}
DECL|method|getInputDataType ()
specifier|public
name|DataType
name|getInputDataType
parameter_list|()
block|{
if|if
condition|(
name|inputDataType
operator|==
literal|null
condition|)
block|{
name|String
name|line
init|=
name|extractInputDataType
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|inputDataType
operator|=
operator|new
name|DataType
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|inputDataType
return|;
block|}
DECL|method|getOutputDataType ()
specifier|public
name|DataType
name|getOutputDataType
parameter_list|()
block|{
if|if
condition|(
name|outputDataType
operator|==
literal|null
condition|)
block|{
name|String
name|line
init|=
name|extractOutputDataType
argument_list|(
name|lines
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|outputDataType
operator|=
operator|new
name|DataType
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|outputDataType
return|;
block|}
comment|// ***************************************
comment|// Helpers
comment|// ***************************************
DECL|method|findCamelConnectorJSonSchema (Class<?> componentClass)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|findCamelConnectorJSonSchema
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|componentClass
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Finding camel-connector.json in classpath for connector: {}"
argument_list|,
name|componentName
argument_list|)
expr_stmt|;
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
decl_stmt|;
try|try
block|{
name|urls
operator|=
name|componentClass
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResources
argument_list|(
literal|"camel-connector.json"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot open camel-connector.json in classpath for connector "
operator|+
name|componentName
argument_list|)
throw|;
block|}
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|urls
operator|.
name|nextElement
argument_list|()
operator|.
name|openStream
argument_list|()
init|)
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
name|String
name|javaType
init|=
name|extractJavaType
argument_list|(
name|lines
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot read camel-connector.json in classpath for connector "
operator|+
name|componentName
argument_list|)
throw|;
block|}
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
DECL|method|loadFile (InputStream fis)
specifier|private
specifier|static
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
DECL|method|extractName (List<String> json)
specifier|private
specifier|static
name|String
name|extractName
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
name|NAME_PATTERN
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
DECL|method|extractJavaType (List<String> json)
specifier|private
specifier|static
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
specifier|static
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
DECL|method|extractScheduler (List<String> json)
specifier|private
specifier|static
name|String
name|extractScheduler
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
name|SCHEDULER_PATTERN
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
specifier|static
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
name|BASE_SCHEME_PATTERN
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
DECL|method|extractInputDataType (List<String> json)
specifier|private
specifier|static
name|String
name|extractInputDataType
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
name|INPUT_DATA_TYPE_PATTERN
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
DECL|method|extractOutputDataType (List<String> json)
specifier|private
specifier|static
name|String
name|extractOutputDataType
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
name|OUTPUT_DATA_TYPE_PATTERN
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
literal|"\"componentValues\""
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
name|value
operator|=
name|value
operator|.
name|trim
argument_list|()
expr_stmt|;
name|key
operator|=
name|key
operator|.
name|trim
argument_list|()
expr_stmt|;
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
literal|"\"endpointValues\""
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
name|value
operator|=
name|value
operator|.
name|trim
argument_list|()
expr_stmt|;
name|key
operator|=
name|key
operator|.
name|trim
argument_list|()
expr_stmt|;
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
DECL|method|extractComponentOptions (List<String> lines)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|extractComponentOptions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// extract the default options
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
literal|"\"componentOptions\""
argument_list|)
condition|)
block|{
name|int
name|start
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|'['
argument_list|)
decl_stmt|;
if|if
condition|(
name|start
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Malformed camel-connector.json"
argument_list|)
throw|;
block|}
name|int
name|end
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|']'
argument_list|,
name|start
argument_list|)
decl_stmt|;
if|if
condition|(
name|end
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Malformed camel-connector.json"
argument_list|)
throw|;
block|}
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
name|start
operator|+
literal|1
argument_list|,
name|end
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|option
range|:
name|line
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
name|option
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|extractEndpointOptions (List<String> lines)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|extractEndpointOptions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// extract the default options
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
literal|"\"endpointOptions\""
argument_list|)
condition|)
block|{
name|int
name|start
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|'['
argument_list|)
decl_stmt|;
if|if
condition|(
name|start
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Malformed camel-connector.json"
argument_list|)
throw|;
block|}
name|int
name|end
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|']'
argument_list|,
name|start
argument_list|)
decl_stmt|;
if|if
condition|(
name|end
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Malformed camel-connector.json"
argument_list|)
throw|;
block|}
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
name|start
operator|+
literal|1
argument_list|,
name|end
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|option
range|:
name|line
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
name|option
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|extractConnectorOptions (List<String> lines)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|extractConnectorOptions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
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
literal|"\"connectorProperties\": {"
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
name|answer
operator|.
name|add
argument_list|(
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
name|key
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

