begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|javax
operator|.
name|activation
operator|.
name|DataHandler
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
name|Attachment
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
name|spi
operator|.
name|HeadersMapFactory
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
name|AttachmentMap
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
name|EndpointHelper
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

begin_comment
comment|/**  * The default implementation of {@link org.apache.camel.Message}  *<p/>  * This implementation uses a {@link org.apache.camel.util.CaseInsensitiveMap} storing the headers.  * This allows us to be able to lookup headers using case insensitive keys, making it easier for end users  * as they do not have to be worried about using exact keys.  * See more details at {@link org.apache.camel.util.CaseInsensitiveMap}.  * The implementation of the map can be configured by the {@link HeadersMapFactory} which can be set  * on the {@link CamelContext}. The default implementation uses the {@link org.apache.camel.util.CaseInsensitiveMap CaseInsensitiveMap}.  *  * @version   */
end_comment

begin_class
DECL|class|DefaultMessage
specifier|public
class|class
name|DefaultMessage
extends|extends
name|MessageSupport
block|{
DECL|field|fault
specifier|private
name|boolean
name|fault
decl_stmt|;
DECL|field|headers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
decl_stmt|;
DECL|field|attachments
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|attachments
decl_stmt|;
DECL|field|attachmentObjects
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|attachmentObjects
decl_stmt|;
comment|/**      * @deprecated use {@link #DefaultMessage(CamelContext)}      */
annotation|@
name|Deprecated
DECL|method|DefaultMessage ()
specifier|public
name|DefaultMessage
parameter_list|()
block|{     }
DECL|method|DefaultMessage (CamelContext camelContext)
specifier|public
name|DefaultMessage
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|isFault ()
specifier|public
name|boolean
name|isFault
parameter_list|()
block|{
return|return
name|fault
return|;
block|}
DECL|method|setFault (boolean fault)
specifier|public
name|void
name|setFault
parameter_list|(
name|boolean
name|fault
parameter_list|)
block|{
name|this
operator|.
name|fault
operator|=
name|fault
expr_stmt|;
block|}
DECL|method|getHeader (String name)
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|hasHeaders
argument_list|()
condition|)
block|{
return|return
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getHeader (String name, Object defaultValue)
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|defaultValue
parameter_list|)
block|{
name|Object
name|answer
init|=
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|answer
operator|!=
literal|null
condition|?
name|answer
else|:
name|defaultValue
return|;
block|}
DECL|method|getHeader (String name, Supplier<Object> defaultValueSupplier)
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Supplier
argument_list|<
name|Object
argument_list|>
name|defaultValueSupplier
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|defaultValueSupplier
argument_list|,
literal|"defaultValueSupplier"
argument_list|)
expr_stmt|;
name|Object
name|answer
init|=
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|answer
operator|!=
literal|null
condition|?
name|answer
else|:
name|defaultValueSupplier
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getHeader (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|value
init|=
name|getHeader
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// lets avoid NullPointerException when converting to boolean for null values
if|if
condition|(
name|boolean
operator|.
name|class
operator|==
name|type
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// eager same instance type test to avoid the overhead of invoking the type converter
comment|// if already same type
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
name|Exchange
name|e
init|=
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
return|return
name|e
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|e
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getHeader (String name, Object defaultValue, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|defaultValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|value
init|=
name|getHeader
argument_list|(
name|name
argument_list|,
name|defaultValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// lets avoid NullPointerException when converting to boolean for null values
if|if
condition|(
name|boolean
operator|.
name|class
operator|==
name|type
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// eager same instance type test to avoid the overhead of invoking the type converter
comment|// if already same type
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
name|Exchange
name|e
init|=
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
return|return
name|e
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|e
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getHeader (String name, Supplier<Object> defaultValueSupplier, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Supplier
argument_list|<
name|Object
argument_list|>
name|defaultValueSupplier
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|type
argument_list|,
literal|"type"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|defaultValueSupplier
argument_list|,
literal|"defaultValueSupplier"
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|getHeader
argument_list|(
name|name
argument_list|,
name|defaultValueSupplier
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// lets avoid NullPointerException when converting to boolean for null values
if|if
condition|(
name|boolean
operator|.
name|class
operator|==
name|type
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// eager same instance type test to avoid the overhead of invoking the type converter
comment|// if already same type
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
name|Exchange
name|e
init|=
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
return|return
name|e
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|e
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
DECL|method|setHeader (String name, Object value)
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
name|createHeaders
argument_list|()
expr_stmt|;
block|}
name|headers
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|removeHeader (String name)
specifier|public
name|Object
name|removeHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
operator|!
name|hasHeaders
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|headers
operator|.
name|remove
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|removeHeaders (String pattern)
specifier|public
name|boolean
name|removeHeaders
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
return|return
name|removeHeaders
argument_list|(
name|pattern
argument_list|,
operator|(
name|String
index|[]
operator|)
literal|null
argument_list|)
return|;
block|}
DECL|method|removeHeaders (String pattern, String... excludePatterns)
specifier|public
name|boolean
name|removeHeaders
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
modifier|...
name|excludePatterns
parameter_list|)
block|{
if|if
condition|(
operator|!
name|hasHeaders
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|matches
init|=
literal|false
decl_stmt|;
comment|// must use a set to store the keys to remove as we cannot walk using entrySet and remove at the same time
comment|// due concurrent modification error
name|Set
argument_list|<
name|String
argument_list|>
name|toRemove
init|=
operator|new
name|HashSet
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
name|Object
argument_list|>
name|entry
range|:
name|headers
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
if|if
condition|(
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|key
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
if|if
condition|(
name|excludePatterns
operator|!=
literal|null
operator|&&
name|isExcludePatternMatch
argument_list|(
name|key
argument_list|,
name|excludePatterns
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|matches
operator|=
literal|true
expr_stmt|;
name|toRemove
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|key
range|:
name|toRemove
control|)
block|{
name|headers
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|matches
return|;
block|}
DECL|method|getHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
block|{
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
name|createHeaders
argument_list|()
expr_stmt|;
block|}
return|return
name|headers
return|;
block|}
DECL|method|setHeaders (Map<String, Object> headers)
specifier|public
name|void
name|setHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|getCamelContext
argument_list|()
operator|.
name|getHeadersMapFactory
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|headers
argument_list|)
condition|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
block|}
else|else
block|{
comment|// create a new map
name|this
operator|.
name|headers
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getHeadersMapFactory
argument_list|()
operator|.
name|newMap
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|hasHeaders ()
specifier|public
name|boolean
name|hasHeaders
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasPopulatedHeaders
argument_list|()
condition|)
block|{
comment|// force creating headers
name|getHeaders
argument_list|()
expr_stmt|;
block|}
return|return
name|headers
operator|!=
literal|null
operator|&&
operator|!
name|headers
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|newInstance ()
specifier|public
name|DefaultMessage
name|newInstance
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultMessage
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * A factory method to lazily create the headers to make it easy to create      * efficient Message implementations which only construct and populate the      * Map on demand      *      * @return return a newly constructed Map possibly containing headers from      *         the underlying inbound transport      */
DECL|method|createHeaders ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createHeaders
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getCamelContext
argument_list|()
operator|.
name|getHeadersMapFactory
argument_list|()
operator|.
name|newMap
argument_list|()
decl_stmt|;
name|populateInitialHeaders
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
comment|/**      * A factory method to lazily create the attachmentObjects to make it easy to      * create efficient Message implementations which only construct and      * populate the Map on demand      *      * @return return a newly constructed Map      */
DECL|method|createAttachments ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|createAttachments
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|populateInitialAttachments
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
comment|/**      * A strategy method populate the initial set of headers on an inbound      * message from an underlying binding      *      * @param map is the empty header map to populate      */
DECL|method|populateInitialHeaders (Map<String, Object> map)
specifier|protected
name|void
name|populateInitialHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
comment|// do nothing by default
block|}
comment|/**      * A strategy method populate the initial set of attachmentObjects on an inbound      * message from an underlying binding      *      * @param map is the empty attachment map to populate      */
DECL|method|populateInitialAttachments (Map<String, Attachment> map)
specifier|protected
name|void
name|populateInitialAttachments
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|map
parameter_list|)
block|{
comment|// do nothing by default
block|}
comment|/**      * A strategy for component specific messages to determine whether the      * message is redelivered or not.      *<p/>      *<b>Important:</b> It is not always possible to determine if the transacted is a redelivery      * or not, and therefore<tt>null</tt> is returned. Such an example would be a JDBC message.      * However JMS brokers provides details if a transacted message is redelivered.      *      * @return<tt>true</tt> if redelivered,<tt>false</tt> if not,<tt>null</tt> if not able to determine      */
DECL|method|isTransactedRedelivered ()
specifier|protected
name|Boolean
name|isTransactedRedelivered
parameter_list|()
block|{
comment|// return null by default
return|return
literal|null
return|;
block|}
DECL|method|addAttachment (String id, DataHandler content)
specifier|public
name|void
name|addAttachment
parameter_list|(
name|String
name|id
parameter_list|,
name|DataHandler
name|content
parameter_list|)
block|{
name|addAttachmentObject
argument_list|(
name|id
argument_list|,
operator|new
name|DefaultAttachment
argument_list|(
name|content
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|addAttachmentObject (String id, Attachment content)
specifier|public
name|void
name|addAttachmentObject
parameter_list|(
name|String
name|id
parameter_list|,
name|Attachment
name|content
parameter_list|)
block|{
if|if
condition|(
name|attachmentObjects
operator|==
literal|null
condition|)
block|{
name|attachmentObjects
operator|=
name|createAttachments
argument_list|()
expr_stmt|;
block|}
name|attachmentObjects
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
DECL|method|getAttachment (String id)
specifier|public
name|DataHandler
name|getAttachment
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Attachment
name|att
init|=
name|getAttachmentObject
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|att
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|att
operator|.
name|getDataHandler
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getAttachmentObject (String id)
specifier|public
name|Attachment
name|getAttachmentObject
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|getAttachmentObjects
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|getAttachmentNames ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getAttachmentNames
parameter_list|()
block|{
if|if
condition|(
name|attachmentObjects
operator|==
literal|null
condition|)
block|{
name|attachmentObjects
operator|=
name|createAttachments
argument_list|()
expr_stmt|;
block|}
return|return
name|attachmentObjects
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|removeAttachment (String id)
specifier|public
name|void
name|removeAttachment
parameter_list|(
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|attachmentObjects
operator|!=
literal|null
operator|&&
name|attachmentObjects
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|attachmentObjects
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getAttachments ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|getAttachments
parameter_list|()
block|{
if|if
condition|(
name|attachments
operator|==
literal|null
condition|)
block|{
name|attachments
operator|=
operator|new
name|AttachmentMap
argument_list|(
name|getAttachmentObjects
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|attachments
return|;
block|}
DECL|method|getAttachmentObjects ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|getAttachmentObjects
parameter_list|()
block|{
if|if
condition|(
name|attachmentObjects
operator|==
literal|null
condition|)
block|{
name|attachmentObjects
operator|=
name|createAttachments
argument_list|()
expr_stmt|;
block|}
return|return
name|attachmentObjects
return|;
block|}
DECL|method|setAttachments (Map<String, DataHandler> attachments)
specifier|public
name|void
name|setAttachments
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|attachments
parameter_list|)
block|{
if|if
condition|(
name|attachments
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|attachmentObjects
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|attachments
operator|instanceof
name|AttachmentMap
condition|)
block|{
comment|// this way setAttachments(getAttachments()) will tunnel attachment headers
name|this
operator|.
name|attachmentObjects
operator|=
operator|(
operator|(
name|AttachmentMap
operator|)
name|attachments
operator|)
operator|.
name|getOriginalMap
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|attachmentObjects
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|entry
range|:
name|attachments
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|this
operator|.
name|attachmentObjects
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
operator|new
name|DefaultAttachment
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|attachments
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|setAttachmentObjects (Map<String, Attachment> attachments)
specifier|public
name|void
name|setAttachmentObjects
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{
name|this
operator|.
name|attachmentObjects
operator|=
name|attachments
expr_stmt|;
name|this
operator|.
name|attachments
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|hasAttachments ()
specifier|public
name|boolean
name|hasAttachments
parameter_list|()
block|{
comment|// optimized to avoid calling createAttachments as that creates a new empty map
comment|// that we 99% do not need (only camel-mail supports attachments), and we have
comment|// then ensure camel-mail always creates attachments to remedy for this
return|return
name|this
operator|.
name|attachmentObjects
operator|!=
literal|null
operator|&&
name|this
operator|.
name|attachmentObjects
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
comment|/**      * Returns true if the headers have been mutated in some way      */
DECL|method|hasPopulatedHeaders ()
specifier|protected
name|boolean
name|hasPopulatedHeaders
parameter_list|()
block|{
return|return
name|headers
operator|!=
literal|null
return|;
block|}
DECL|method|createExchangeId ()
specifier|public
name|String
name|createExchangeId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|isExcludePatternMatch (String key, String... excludePatterns)
specifier|private
specifier|static
name|boolean
name|isExcludePatternMatch
parameter_list|(
name|String
name|key
parameter_list|,
name|String
modifier|...
name|excludePatterns
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|excludePatterns
control|)
block|{
if|if
condition|(
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|key
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

