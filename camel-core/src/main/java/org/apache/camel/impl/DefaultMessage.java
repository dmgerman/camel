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
name|HashMap
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
name|util
operator|.
name|CaseInsensitiveMap
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
name|MessageHelper
import|;
end_import

begin_comment
comment|/**  * The default implementation of {@link org.apache.camel.Message}  *<p/>  * This implementation uses a {@link org.apache.camel.util.CaseInsensitiveMap} storing the headers.  * This allows us to be able to lookup headers using case insensitive keys, making it easier for end users  * as they do not have to be worried about using exact keys.  * See more details at {@link org.apache.camel.util.CaseInsensitiveMap}.  *  * @version $Revision$  */
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|MessageHelper
operator|.
name|extractBodyForLogging
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|copyFrom (Message that)
specifier|public
name|void
name|copyFrom
parameter_list|(
name|Message
name|that
parameter_list|)
block|{
name|super
operator|.
name|copyFrom
argument_list|(
name|that
argument_list|)
expr_stmt|;
name|fault
operator|=
name|that
operator|.
name|isFault
argument_list|()
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
if|if
condition|(
name|headers
operator|instanceof
name|CaseInsensitiveMap
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
comment|// wrap it in a case insensitive map
name|this
operator|.
name|headers
operator|=
operator|new
name|CaseInsensitiveMap
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
return|return
operator|new
name|DefaultMessage
argument_list|()
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|CaseInsensitiveMap
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
comment|/**      * A factory method to lazily create the attachments to make it easy to      * create efficient Message implementations which only construct and      * populate the Map on demand      *      * @return return a newly constructed Map      */
DECL|method|createAttachments ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|createAttachments
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
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
comment|/**      * A strategy method populate the initial set of attachments on an inbound      * message from an underlying binding      *      * @param map is the empty attachment map to populate      */
DECL|method|populateInitialAttachments (Map<String, DataHandler> map)
specifier|protected
name|void
name|populateInitialAttachments
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|map
parameter_list|)
block|{
comment|// do nothing by default
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
if|if
condition|(
name|attachments
operator|==
literal|null
condition|)
block|{
name|attachments
operator|=
name|createAttachments
argument_list|()
expr_stmt|;
block|}
name|attachments
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
return|return
name|getAttachments
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
name|attachments
operator|==
literal|null
condition|)
block|{
name|attachments
operator|=
name|createAttachments
argument_list|()
expr_stmt|;
block|}
return|return
name|attachments
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
name|attachments
operator|!=
literal|null
operator|&&
name|attachments
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|attachments
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
name|createAttachments
argument_list|()
expr_stmt|;
block|}
return|return
name|attachments
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
name|this
operator|.
name|attachments
operator|=
name|attachments
expr_stmt|;
block|}
DECL|method|hasAttachments ()
specifier|public
name|boolean
name|hasAttachments
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
name|createAttachments
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|attachments
operator|!=
literal|null
operator|&&
name|this
operator|.
name|attachments
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
block|}
end_class

end_unit

