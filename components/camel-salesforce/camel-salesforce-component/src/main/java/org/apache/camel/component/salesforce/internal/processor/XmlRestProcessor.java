begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.processor
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
name|processor
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
name|OutputStreamWriter
import|;
end_import

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
name|io
operator|.
name|Writer
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
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|mapper
operator|.
name|CachingMapper
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
name|mapper
operator|.
name|CannotResolveClassException
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
name|Message
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
name|SalesforceEndpoint
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
name|AbstractDTOBase
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
name|CreateSObjectResult
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
name|GlobalObjects
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
name|RestResources
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
name|SObjectBasicInfo
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
name|SObjectDescription
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
name|SearchResults
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
name|Versions
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
name|approval
operator|.
name|ApprovalResult
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
name|internal
operator|.
name|client
operator|.
name|XStreamUtils
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
import|import static
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
operator|.
name|SOBJECT_NAME
import|;
end_import

begin_class
DECL|class|XmlRestProcessor
specifier|public
class|class
name|XmlRestProcessor
extends|extends
name|AbstractRestProcessor
block|{
comment|// although XStream is generally thread safe, because of the way we use aliases
comment|// for GET_BASIC_INFO and GET_DESCRIPTION, we need to use a ThreadLocal
comment|// not very efficient when both JSON and XML are used together with a single Thread pool
comment|// but this will do for now
DECL|field|xStream
specifier|private
specifier|static
name|ThreadLocal
argument_list|<
name|XStream
argument_list|>
name|xStream
init|=
operator|new
name|ThreadLocal
argument_list|<
name|XStream
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|XStream
name|initialValue
parameter_list|()
block|{
comment|// use NoNameCoder to avoid escaping __ in custom field names
comment|// and CompactWriter to avoid pretty printing
name|XStream
name|result
init|=
operator|new
name|XStream
argument_list|(
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
argument_list|)
decl_stmt|;
name|result
operator|.
name|ignoreUnknownElements
argument_list|()
expr_stmt|;
name|XStreamUtils
operator|.
name|addDefaultPermissions
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|result
operator|.
name|registerConverter
argument_list|(
operator|new
name|DateTimeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setMarshallingStrategy
argument_list|(
operator|new
name|TreeMarshallingStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
decl_stmt|;
DECL|field|RESPONSE_ALIAS
specifier|private
specifier|static
specifier|final
name|String
name|RESPONSE_ALIAS
init|=
name|XmlRestProcessor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".responseAlias"
decl_stmt|;
DECL|method|XmlRestProcessor (SalesforceEndpoint endpoint)
specifier|public
name|XmlRestProcessor
parameter_list|(
name|SalesforceEndpoint
name|endpoint
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|processRequest (Exchange exchange)
specifier|protected
name|void
name|processRequest
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SalesforceException
block|{
switch|switch
condition|(
name|operationName
condition|)
block|{
case|case
name|GET_VERSIONS
case|:
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|Versions
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_RESOURCES
case|:
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|RestResources
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_GLOBAL_OBJECTS
case|:
comment|// handle in built response types
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|GlobalObjects
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_BASIC_INFO
case|:
comment|// handle in built response types
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|SObjectBasicInfo
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// need to add alias for Salesforce XML that uses SObject name as root element
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_ALIAS
argument_list|,
name|getParameter
argument_list|(
name|SOBJECT_NAME
argument_list|,
name|exchange
argument_list|,
name|USE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_DESCRIPTION
case|:
comment|// handle in built response types
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|SObjectDescription
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// need to add alias for Salesforce XML that uses SObject name as root element
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_ALIAS
argument_list|,
name|getParameter
argument_list|(
name|SOBJECT_NAME
argument_list|,
name|exchange
argument_list|,
name|USE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_SOBJECT
case|:
comment|// need to add alias for Salesforce XML that uses SObject name as root element
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_ALIAS
argument_list|,
name|getParameter
argument_list|(
name|SOBJECT_NAME
argument_list|,
name|exchange
argument_list|,
name|IGNORE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|CREATE_SOBJECT
case|:
comment|// handle known response type
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|CreateSObjectResult
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_SOBJECT_WITH_ID
case|:
comment|// need to add alias for Salesforce XML that uses SObject name as root element
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_ALIAS
argument_list|,
name|getParameter
argument_list|(
name|SOBJECT_NAME
argument_list|,
name|exchange
argument_list|,
name|IGNORE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|UPSERT_SOBJECT
case|:
comment|// handle known response type
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|CreateSObjectResult
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|QUERY
case|:
case|case
name|QUERY_ALL
case|:
case|case
name|QUERY_MORE
case|:
comment|// need to add alias for Salesforce XML that uses SObject name as root element
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_ALIAS
argument_list|,
literal|"QueryResult"
argument_list|)
expr_stmt|;
break|break;
case|case
name|SEARCH
case|:
comment|// handle known response type
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|SearchResults
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|APEX_CALL
case|:
comment|// need to add alias for Salesforce XML that uses SObject name as root element
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_ALIAS
argument_list|,
literal|"response"
argument_list|)
expr_stmt|;
break|break;
case|case
name|APPROVAL
case|:
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|ApprovalResult
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|APPROVALS
case|:
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Fetching of approvals (as of 18.11.2016) with XML format results in HTTP status 500."
operator|+
literal|" To fetch approvals please use JSON format."
argument_list|,
literal|0
argument_list|)
throw|;
default|default:
comment|// ignore, some operations do not require alias or class exchange properties
block|}
block|}
DECL|method|getRequestStream (Exchange exchange)
specifier|protected
name|InputStream
name|getRequestStream
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SalesforceException
block|{
try|try
block|{
comment|// get request stream from In message
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|InputStream
name|request
init|=
name|in
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|==
literal|null
condition|)
block|{
name|AbstractDTOBase
name|dto
init|=
name|in
operator|.
name|getBody
argument_list|(
name|AbstractDTOBase
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dto
operator|!=
literal|null
condition|)
block|{
comment|// marshall the DTO
name|request
operator|=
name|getRequestStream
argument_list|(
name|dto
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if all else fails, get body as String
specifier|final
name|String
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|body
condition|)
block|{
name|String
name|msg
init|=
literal|"Unsupported request message body "
operator|+
operator|(
name|in
operator|.
name|getBody
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|in
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
operator|)
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
literal|null
argument_list|)
throw|;
block|}
else|else
block|{
name|request
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
operator|.
name|getBytes
argument_list|(
name|StringUtil
operator|.
name|__UTF8
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|request
return|;
block|}
catch|catch
parameter_list|(
name|XStreamException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Error marshaling request: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Error marshaling request: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getRequestStream (final Object object)
specifier|protected
name|InputStream
name|getRequestStream
parameter_list|(
specifier|final
name|Object
name|object
parameter_list|)
throws|throws
name|SalesforceException
block|{
specifier|final
name|XStream
name|localXStream
init|=
name|xStream
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// first process annotations on the class, for things like alias, etc.
name|localXStream
operator|.
name|processAnnotations
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
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
comment|// make sure we write the XML with the right encoding
try|try
block|{
name|localXStream
operator|.
name|toXML
argument_list|(
name|object
argument_list|,
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|,
name|StringUtil
operator|.
name|__UTF8
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Error marshaling request: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
annotation|@
name|Override
DECL|method|processResponse (Exchange exchange, InputStream responseEntity, SalesforceException exception, AsyncCallback callback)
specifier|protected
name|void
name|processResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|responseEntity
parameter_list|,
name|SalesforceException
name|exception
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
specifier|final
name|XStream
name|localXStream
init|=
name|xStream
operator|.
name|get
argument_list|()
decl_stmt|;
try|try
block|{
comment|// do we need to un-marshal a response
if|if
condition|(
name|responseEntity
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseClass
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|response
decl_stmt|;
if|if
condition|(
name|responseClass
operator|!=
literal|null
condition|)
block|{
comment|// its ok to call this multiple times, as xstream ignores duplicate calls
name|localXStream
operator|.
name|processAnnotations
argument_list|(
name|responseClass
argument_list|)
expr_stmt|;
specifier|final
name|String
name|responseAlias
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|RESPONSE_ALIAS
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseAlias
operator|!=
literal|null
condition|)
block|{
comment|// extremely dirty, need to flush entire cache if its holding on to an old alias!!!
specifier|final
name|CachingMapper
name|mapper
init|=
operator|(
name|CachingMapper
operator|)
name|localXStream
operator|.
name|getMapper
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|mapper
operator|.
name|realClass
argument_list|(
name|responseAlias
argument_list|)
operator|!=
name|responseClass
condition|)
block|{
name|mapper
operator|.
name|flushCache
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|CannotResolveClassException
name|ignore
parameter_list|)
block|{
comment|// recent XStream versions add a ClassNotFoundException to cache
name|mapper
operator|.
name|flushCache
argument_list|()
expr_stmt|;
block|}
name|localXStream
operator|.
name|alias
argument_list|(
name|responseAlias
argument_list|,
name|responseClass
argument_list|)
expr_stmt|;
block|}
name|response
operator|=
name|responseClass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|localXStream
operator|.
name|fromXML
argument_list|(
name|responseEntity
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// return the response as a stream, for getBlobField
name|response
operator|=
name|responseEntity
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
comment|// copy headers and attachments
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachmentObjects
argument_list|()
operator|.
name|putAll
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachmentObjects
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XStreamException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Error parsing XML response: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Error creating XML response: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// cleanup temporary exchange headers
name|exchange
operator|.
name|removeProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|removeProperty
argument_list|(
name|RESPONSE_ALIAS
argument_list|)
expr_stmt|;
comment|// consume response entity
if|if
condition|(
name|responseEntity
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|responseEntity
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignored
parameter_list|)
block|{                 }
block|}
comment|// notify callback that exchange is done
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

