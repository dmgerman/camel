begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|Writer
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
name|Optional
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonProcessingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStreamException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|reflection
operator|.
name|FieldDictionary
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|reflection
operator|.
name|PureJavaReflectionProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|core
operator|.
name|TreeMarshallingStrategy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|naming
operator|.
name|NoNameCoder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|xml
operator|.
name|CompactWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|xml
operator|.
name|XppDriver
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
name|component
operator|.
name|salesforce
operator|.
name|SalesforceEndpointConfig
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
name|component
operator|.
name|salesforce
operator|.
name|SalesforceHttpClient
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|NoSuchSObjectException
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|SalesforceException
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|AnnotationFieldKeySorter
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|RestError
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|composite
operator|.
name|SObjectBatch
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|composite
operator|.
name|SObjectBatchResponse
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|composite
operator|.
name|SObjectTree
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|composite
operator|.
name|SObjectTreeResponse
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|DateTimeConverter
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|JsonUtils
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|Version
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
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|PayloadFormat
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
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|SalesforceSession
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|ContentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|util
operator|.
name|InputStreamContentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|StringUtil
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
DECL|class|DefaultCompositeApiClient
specifier|public
class|class
name|DefaultCompositeApiClient
extends|extends
name|AbstractClientBase
implements|implements
name|CompositeApiClient
block|{
DECL|field|ADDITIONAL_TYPES
specifier|private
specifier|static
specifier|final
name|Class
index|[]
name|ADDITIONAL_TYPES
init|=
operator|new
name|Class
index|[]
block|{
name|SObjectTree
operator|.
name|class
block|,
name|SObjectTreeResponse
operator|.
name|class
block|,
name|SObjectBatchResponse
operator|.
name|class
block|}
decl_stmt|;
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
name|DefaultCompositeApiClient
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|format
specifier|private
specifier|final
name|PayloadFormat
name|format
decl_stmt|;
DECL|field|mapper
specifier|private
name|ObjectMapper
name|mapper
decl_stmt|;
DECL|field|readers
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ObjectReader
argument_list|>
name|readers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|writters
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ObjectWriter
argument_list|>
name|writters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|xStream
specifier|private
specifier|final
name|XStream
name|xStream
decl_stmt|;
DECL|method|DefaultCompositeApiClient (final SalesforceEndpointConfig configuration, final PayloadFormat format, final String version, final SalesforceSession session, final SalesforceHttpClient httpClient)
specifier|public
name|DefaultCompositeApiClient
parameter_list|(
specifier|final
name|SalesforceEndpointConfig
name|configuration
parameter_list|,
specifier|final
name|PayloadFormat
name|format
parameter_list|,
specifier|final
name|String
name|version
parameter_list|,
specifier|final
name|SalesforceSession
name|session
parameter_list|,
specifier|final
name|SalesforceHttpClient
name|httpClient
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|super
argument_list|(
name|version
argument_list|,
name|session
argument_list|,
name|httpClient
argument_list|)
expr_stmt|;
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getObjectMapper
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mapper
operator|=
name|configuration
operator|.
name|getObjectMapper
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|mapper
operator|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
expr_stmt|;
block|}
name|xStream
operator|=
name|configureXStream
argument_list|()
expr_stmt|;
block|}
DECL|method|configureXStream ()
specifier|static
name|XStream
name|configureXStream
parameter_list|()
block|{
specifier|final
name|PureJavaReflectionProvider
name|reflectionProvider
init|=
operator|new
name|PureJavaReflectionProvider
argument_list|(
operator|new
name|FieldDictionary
argument_list|(
operator|new
name|AnnotationFieldKeySorter
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|XppDriver
name|hierarchicalStreamDriver
init|=
operator|new
name|XppDriver
argument_list|(
operator|new
name|NoNameCoder
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|HierarchicalStreamWriter
name|createWriter
parameter_list|(
specifier|final
name|Writer
name|out
parameter_list|)
block|{
return|return
operator|new
name|CompactWriter
argument_list|(
name|out
argument_list|,
name|getNameCoder
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|XStream
name|xStream
init|=
operator|new
name|XStream
argument_list|(
name|reflectionProvider
argument_list|,
name|hierarchicalStreamDriver
argument_list|)
decl_stmt|;
name|xStream
operator|.
name|aliasSystemAttribute
argument_list|(
literal|null
argument_list|,
literal|"class"
argument_list|)
expr_stmt|;
name|xStream
operator|.
name|ignoreUnknownElements
argument_list|()
expr_stmt|;
name|XStreamUtils
operator|.
name|addDefaultPermissions
argument_list|(
name|xStream
argument_list|)
expr_stmt|;
name|xStream
operator|.
name|registerConverter
argument_list|(
operator|new
name|DateTimeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|xStream
operator|.
name|setMarshallingStrategy
argument_list|(
operator|new
name|TreeMarshallingStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|xStream
operator|.
name|processAnnotations
argument_list|(
name|ADDITIONAL_TYPES
argument_list|)
expr_stmt|;
return|return
name|xStream
return|;
block|}
annotation|@
name|Override
DECL|method|submitCompositeBatch (final SObjectBatch batch, final Map<String, List<String>> headers, final ResponseCallback<SObjectBatchResponse> callback)
specifier|public
name|void
name|submitCompositeBatch
parameter_list|(
specifier|final
name|SObjectBatch
name|batch
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
specifier|final
name|ResponseCallback
argument_list|<
name|SObjectBatchResponse
argument_list|>
name|callback
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|checkCompositeBatchVersion
argument_list|(
name|version
argument_list|,
name|batch
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|url
init|=
name|versionUrl
argument_list|()
operator|+
literal|"composite/batch"
decl_stmt|;
specifier|final
name|Request
name|post
init|=
name|createRequest
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|url
argument_list|,
name|headers
argument_list|)
decl_stmt|;
specifier|final
name|ContentProvider
name|content
init|=
name|serialize
argument_list|(
name|batch
argument_list|,
name|batch
operator|.
name|objectTypes
argument_list|()
argument_list|)
decl_stmt|;
name|post
operator|.
name|content
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|post
argument_list|,
parameter_list|(
name|response
parameter_list|,
name|responseHeaders
parameter_list|,
name|exception
parameter_list|)
lambda|->
name|callback
operator|.
name|onResponse
argument_list|(
name|tryToReadResponse
argument_list|(
name|SObjectBatchResponse
operator|.
name|class
argument_list|,
name|response
argument_list|)
argument_list|,
name|responseHeaders
argument_list|,
name|exception
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|submitCompositeTree (final SObjectTree tree, final Map<String, List<String>> headers, final ResponseCallback<SObjectTreeResponse> callback)
specifier|public
name|void
name|submitCompositeTree
parameter_list|(
specifier|final
name|SObjectTree
name|tree
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
specifier|final
name|ResponseCallback
argument_list|<
name|SObjectTreeResponse
argument_list|>
name|callback
parameter_list|)
throws|throws
name|SalesforceException
block|{
specifier|final
name|String
name|url
init|=
name|versionUrl
argument_list|()
operator|+
literal|"composite/tree/"
operator|+
name|tree
operator|.
name|getObjectType
argument_list|()
decl_stmt|;
specifier|final
name|Request
name|post
init|=
name|createRequest
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|url
argument_list|,
name|headers
argument_list|)
decl_stmt|;
specifier|final
name|ContentProvider
name|content
init|=
name|serialize
argument_list|(
name|tree
argument_list|,
name|tree
operator|.
name|objectTypes
argument_list|()
argument_list|)
decl_stmt|;
name|post
operator|.
name|content
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|post
argument_list|,
parameter_list|(
name|response
parameter_list|,
name|responseHeaders
parameter_list|,
name|exception
parameter_list|)
lambda|->
name|callback
operator|.
name|onResponse
argument_list|(
name|tryToReadResponse
argument_list|(
name|SObjectTreeResponse
operator|.
name|class
argument_list|,
name|response
argument_list|)
argument_list|,
name|responseHeaders
argument_list|,
name|exception
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|checkCompositeBatchVersion (final String configuredVersion, final Version batchVersion)
specifier|static
name|void
name|checkCompositeBatchVersion
parameter_list|(
specifier|final
name|String
name|configuredVersion
parameter_list|,
specifier|final
name|Version
name|batchVersion
parameter_list|)
throws|throws
name|SalesforceException
block|{
if|if
condition|(
name|Version
operator|.
name|create
argument_list|(
name|configuredVersion
argument_list|)
operator|.
name|compareTo
argument_list|(
name|batchVersion
argument_list|)
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Component is configured with Salesforce API version "
operator|+
name|configuredVersion
operator|+
literal|", but the payload of the Composite API batch operation requires at least "
operator|+
name|batchVersion
argument_list|,
literal|0
argument_list|)
throw|;
block|}
block|}
DECL|method|createRequest (final HttpMethod method, final String url, Map<String, List<String>> headers)
name|Request
name|createRequest
parameter_list|(
specifier|final
name|HttpMethod
name|method
parameter_list|,
specifier|final
name|String
name|url
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
block|{
specifier|final
name|Request
name|request
init|=
name|getRequest
argument_list|(
name|method
argument_list|,
name|url
argument_list|,
name|headers
argument_list|)
decl_stmt|;
comment|// setup authorization
name|setAccessToken
argument_list|(
name|request
argument_list|)
expr_stmt|;
if|if
condition|(
name|format
operator|==
name|PayloadFormat
operator|.
name|JSON
condition|)
block|{
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|CONTENT_TYPE
argument_list|,
name|APPLICATION_JSON_UTF8
argument_list|)
expr_stmt|;
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|ACCEPT
argument_list|,
name|APPLICATION_JSON_UTF8
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// must be XML
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|CONTENT_TYPE
argument_list|,
name|APPLICATION_XML_UTF8
argument_list|)
expr_stmt|;
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|ACCEPT
argument_list|,
name|APPLICATION_XML_UTF8
argument_list|)
expr_stmt|;
block|}
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|ACCEPT_CHARSET
argument_list|,
name|StringUtil
operator|.
name|__UTF8
argument_list|)
expr_stmt|;
return|return
name|request
return|;
block|}
DECL|method|fromJson (final Class<T> expectedType, final InputStream responseStream)
parameter_list|<
name|T
parameter_list|>
name|T
name|fromJson
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|expectedType
parameter_list|,
specifier|final
name|InputStream
name|responseStream
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|jsonReaderFor
argument_list|(
name|expectedType
argument_list|)
operator|.
name|readValue
argument_list|(
name|responseStream
argument_list|)
return|;
block|}
DECL|method|fromXml (final InputStream responseStream)
parameter_list|<
name|T
parameter_list|>
name|T
name|fromXml
parameter_list|(
specifier|final
name|InputStream
name|responseStream
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|T
name|read
init|=
operator|(
name|T
operator|)
name|xStream
operator|.
name|fromXML
argument_list|(
name|responseStream
argument_list|)
decl_stmt|;
return|return
name|read
return|;
block|}
DECL|method|jsonReaderFor (final Class<?> type)
name|ObjectReader
name|jsonReaderFor
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|readers
operator|.
name|get
argument_list|(
name|type
argument_list|)
argument_list|)
operator|.
name|orElseGet
argument_list|(
parameter_list|()
lambda|->
name|mapper
operator|.
name|readerFor
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
DECL|method|jsonWriterFor (final Object obj)
name|ObjectWriter
name|jsonWriterFor
parameter_list|(
specifier|final
name|Object
name|obj
parameter_list|)
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|obj
operator|.
name|getClass
argument_list|()
decl_stmt|;
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|writters
operator|.
name|get
argument_list|(
name|type
argument_list|)
argument_list|)
operator|.
name|orElseGet
argument_list|(
parameter_list|()
lambda|->
name|mapper
operator|.
name|writerFor
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
DECL|method|serialize (final Object body, final Class<?>... additionalTypes)
name|ContentProvider
name|serialize
parameter_list|(
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|additionalTypes
parameter_list|)
throws|throws
name|SalesforceException
block|{
specifier|final
name|InputStream
name|stream
decl_stmt|;
if|if
condition|(
name|format
operator|==
name|PayloadFormat
operator|.
name|JSON
condition|)
block|{
name|stream
operator|=
name|toJson
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// must be XML
name|stream
operator|=
name|toXml
argument_list|(
name|body
argument_list|,
name|additionalTypes
argument_list|)
expr_stmt|;
block|}
comment|// input stream as entity content is needed for authentication retries
return|return
operator|new
name|InputStreamContentProvider
argument_list|(
name|stream
argument_list|)
return|;
block|}
DECL|method|servicesDataUrl ()
name|String
name|servicesDataUrl
parameter_list|()
block|{
return|return
name|instanceUrl
operator|+
literal|"/services/data/"
return|;
block|}
DECL|method|toJson (final Object obj)
name|InputStream
name|toJson
parameter_list|(
specifier|final
name|Object
name|obj
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|byte
index|[]
name|jsonBytes
decl_stmt|;
try|try
block|{
name|jsonBytes
operator|=
name|jsonWriterFor
argument_list|(
name|obj
argument_list|)
operator|.
name|writeValueAsBytes
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|JsonProcessingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Unable to serialize given SObjectTree to JSON"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|jsonBytes
argument_list|)
return|;
block|}
DECL|method|toXml (final Object obj, final Class<?>... additionalTypes)
name|InputStream
name|toXml
parameter_list|(
specifier|final
name|Object
name|obj
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|additionalTypes
parameter_list|)
block|{
name|xStream
operator|.
name|processAnnotations
argument_list|(
name|additionalTypes
argument_list|)
expr_stmt|;
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|xStream
operator|.
name|toXML
argument_list|(
name|obj
argument_list|,
name|out
argument_list|)
expr_stmt|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
DECL|method|tryToReadResponse (final Class<T> expectedType, final InputStream responseStream)
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|tryToReadResponse
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|expectedType
parameter_list|,
specifier|final
name|InputStream
name|responseStream
parameter_list|)
block|{
if|if
condition|(
name|responseStream
operator|==
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
try|try
block|{
if|if
condition|(
name|format
operator|==
name|PayloadFormat
operator|.
name|JSON
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|fromJson
argument_list|(
name|expectedType
argument_list|,
name|responseStream
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
comment|// must be XML
return|return
name|Optional
operator|.
name|of
argument_list|(
name|fromXml
argument_list|(
name|responseStream
argument_list|)
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|XStreamException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to read response from the Composite API"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|responseStream
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|versionUrl ()
name|String
name|versionUrl
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|version
argument_list|,
literal|"version"
argument_list|)
expr_stmt|;
return|return
name|servicesDataUrl
argument_list|()
operator|+
literal|"v"
operator|+
name|version
operator|+
literal|"/"
return|;
block|}
annotation|@
name|Override
DECL|method|createRestException (final Response response, final InputStream responseContent)
specifier|protected
name|SalesforceException
name|createRestException
parameter_list|(
specifier|final
name|Response
name|response
parameter_list|,
specifier|final
name|InputStream
name|responseContent
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
decl_stmt|;
try|try
block|{
name|errors
operator|=
name|readErrorsFrom
argument_list|(
name|responseContent
argument_list|,
name|format
argument_list|,
name|mapper
argument_list|,
name|xStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
operator|new
name|SalesforceException
argument_list|(
literal|"Unable to read error response"
argument_list|,
name|e
argument_list|)
return|;
block|}
specifier|final
name|int
name|status
init|=
name|response
operator|.
name|getStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|status
operator|==
name|HttpStatus
operator|.
name|NOT_FOUND_404
condition|)
block|{
return|return
operator|new
name|NoSuchSObjectException
argument_list|(
name|errors
argument_list|)
return|;
block|}
specifier|final
name|String
name|reason
init|=
name|response
operator|.
name|getReason
argument_list|()
decl_stmt|;
return|return
operator|new
name|SalesforceException
argument_list|(
literal|"Unexpected error: "
operator|+
name|reason
argument_list|,
name|status
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setAccessToken (final Request request)
specifier|protected
name|void
name|setAccessToken
parameter_list|(
specifier|final
name|Request
name|request
parameter_list|)
block|{
name|request
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
literal|"Authorization"
argument_list|,
literal|"Bearer "
operator|+
name|accessToken
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

