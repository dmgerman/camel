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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|RuntimeExchangeException
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
name|WrappedFile
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
comment|/**  * Holder object for sending an exchange over a remote wire as a serialized object.  * This is usually configured using the<tt>transferExchange=true</tt> option on the endpoint.  *<br/>  *<b>Note:</b> Message body of type {@link File} or {@link WrappedFile} is<b>not</b> supported and  * a {@link RuntimeExchangeException} is thrown.  *<br/>  * As opposed to normal usage where only the body part of the exchange is transferred over the wire,  * this holder object serializes the following fields over the wire:  *<ul>  *<li>exchangeId</li>  *<li>in body</li>  *<li>out body</li>  *<li>fault body</li>  *<li>exception</li>  *</ul>  *<br/>  * The exchange properties are not propagated by default. However you can specify they should be included  * by the {@link DefaultExchangeHolder#marshal(Exchange, boolean)} method.  *<br/>  * And the following headers is transferred if their values are of primitive types, String or Number based.  *<ul>  *<li>in headers</li>  *<li>out headers</li>  *<li>fault headers</li>  *</ul>  * The body is serialized and stored as serialized bytes. The header and exchange properties only include  * primitive, String, and Number types (and Exception types for exchange properties). Any other type is skipped.  * Any message body object that is not serializable will be skipped and Camel will log this at<tt>WARN</tt> level.  * And any message header values that is not a primitive value will be skipped and Camel will log this at<tt>DEBUG</tt> level.  *  * @version   */
end_comment

begin_class
DECL|class|DefaultExchangeHolder
specifier|public
class|class
name|DefaultExchangeHolder
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2L
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
name|DefaultExchangeHolder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|exchangeId
specifier|private
name|String
name|exchangeId
decl_stmt|;
DECL|field|inBody
specifier|private
name|Object
name|inBody
decl_stmt|;
DECL|field|outBody
specifier|private
name|Object
name|outBody
decl_stmt|;
DECL|field|inFaultFlag
specifier|private
name|Boolean
name|inFaultFlag
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
DECL|field|outFaultFlag
specifier|private
name|Boolean
name|outFaultFlag
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
DECL|field|inHeaders
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inHeaders
decl_stmt|;
DECL|field|outHeaders
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outHeaders
decl_stmt|;
DECL|field|properties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
DECL|field|exception
specifier|private
name|Exception
name|exception
decl_stmt|;
comment|/**      * Creates a payload object with the information from the given exchange.      *      * @param exchange the exchange, must<b>not</b> be<tt>null</tt>      * @return the holder object with information copied form the exchange      */
DECL|method|marshal (Exchange exchange)
specifier|public
specifier|static
name|DefaultExchangeHolder
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|marshal
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Creates a payload object with the information from the given exchange.      *      * @param exchange the exchange, must<b>not</b> be<tt>null</tt>      * @param includeProperties whether or not to include exchange properties      * @return the holder object with information copied form the exchange      */
DECL|method|marshal (Exchange exchange, boolean includeProperties)
specifier|public
specifier|static
name|DefaultExchangeHolder
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|includeProperties
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|exchange
argument_list|,
literal|"exchange"
argument_list|)
expr_stmt|;
comment|// we do not support files
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|WrappedFile
operator|||
name|body
operator|instanceof
name|File
condition|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Message body of type "
operator|+
name|body
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" is not supported by this marshaller."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|DefaultExchangeHolder
name|payload
init|=
operator|new
name|DefaultExchangeHolder
argument_list|()
decl_stmt|;
name|payload
operator|.
name|exchangeId
operator|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
expr_stmt|;
name|payload
operator|.
name|inBody
operator|=
name|checkSerializableBody
argument_list|(
literal|"in body"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|payload
operator|.
name|safeSetInHeaders
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|payload
operator|.
name|outBody
operator|=
name|checkSerializableBody
argument_list|(
literal|"out body"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|payload
operator|.
name|outFaultFlag
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
expr_stmt|;
name|payload
operator|.
name|safeSetOutHeaders
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|payload
operator|.
name|inFaultFlag
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|isFault
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|includeProperties
condition|)
block|{
name|payload
operator|.
name|safeSetProperties
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|payload
operator|.
name|exception
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
return|return
name|payload
return|;
block|}
comment|/**      * Creates a payload object with the information from the given exchange.      *      * @param exchange the exchange, must<b>not</b> be<tt>null</tt>      * @param includeProperties whether or not to include exchange properties      * @param allowSerializedHeaders whether or not to include serialized headers      * @return the holder object with information copied form the exchange      */
DECL|method|marshal (Exchange exchange, boolean includeProperties, boolean allowSerializedHeaders)
specifier|public
specifier|static
name|DefaultExchangeHolder
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|includeProperties
parameter_list|,
name|boolean
name|allowSerializedHeaders
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|exchange
argument_list|,
literal|"exchange"
argument_list|)
expr_stmt|;
comment|// we do not support files
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|WrappedFile
operator|||
name|body
operator|instanceof
name|File
condition|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Message body of type "
operator|+
name|body
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" is not supported by this marshaller."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|DefaultExchangeHolder
name|payload
init|=
operator|new
name|DefaultExchangeHolder
argument_list|()
decl_stmt|;
name|payload
operator|.
name|exchangeId
operator|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
expr_stmt|;
name|payload
operator|.
name|inBody
operator|=
name|checkSerializableBody
argument_list|(
literal|"in body"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|payload
operator|.
name|safeSetInHeaders
argument_list|(
name|exchange
argument_list|,
name|allowSerializedHeaders
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|payload
operator|.
name|outBody
operator|=
name|checkSerializableBody
argument_list|(
literal|"out body"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|payload
operator|.
name|outFaultFlag
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
expr_stmt|;
name|payload
operator|.
name|safeSetOutHeaders
argument_list|(
name|exchange
argument_list|,
name|allowSerializedHeaders
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|payload
operator|.
name|inFaultFlag
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|isFault
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|includeProperties
condition|)
block|{
name|payload
operator|.
name|safeSetProperties
argument_list|(
name|exchange
argument_list|,
name|allowSerializedHeaders
argument_list|)
expr_stmt|;
block|}
name|payload
operator|.
name|exception
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
return|return
name|payload
return|;
block|}
comment|/**      * Transfers the information from the payload to the exchange.      *      * @param exchange the exchange to set values from the payload, must<b>not</b> be<tt>null</tt>      * @param payload  the payload with the values, must<b>not</b> be<tt>null</tt>      */
DECL|method|unmarshal (Exchange exchange, DefaultExchangeHolder payload)
specifier|public
specifier|static
name|void
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|DefaultExchangeHolder
name|payload
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|exchange
argument_list|,
literal|"exchange"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|payload
argument_list|,
literal|"payload"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setExchangeId
argument_list|(
name|payload
operator|.
name|exchangeId
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
operator|.
name|inBody
argument_list|)
expr_stmt|;
if|if
condition|(
name|payload
operator|.
name|inHeaders
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|payload
operator|.
name|inHeaders
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|payload
operator|.
name|inFaultFlag
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setFault
argument_list|(
name|payload
operator|.
name|inFaultFlag
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|payload
operator|.
name|outBody
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
operator|.
name|outBody
argument_list|)
expr_stmt|;
if|if
condition|(
name|payload
operator|.
name|outHeaders
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|payload
operator|.
name|outHeaders
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|payload
operator|.
name|outFaultFlag
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setFault
argument_list|(
name|payload
operator|.
name|outFaultFlag
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|payload
operator|.
name|properties
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|key
range|:
name|payload
operator|.
name|properties
operator|.
name|keySet
argument_list|()
control|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|payload
operator|.
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|exchange
operator|.
name|setException
argument_list|(
name|payload
operator|.
name|exception
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a property to the payload.      *<p/>      * This can be done in special situations where additional information must be added which was not provided      * from the source.      *      * @param payload the serialized payload      * @param key the property key to add      * @param property the property value to add      */
DECL|method|addProperty (DefaultExchangeHolder payload, String key, Serializable property)
specifier|public
specifier|static
name|void
name|addProperty
parameter_list|(
name|DefaultExchangeHolder
name|payload
parameter_list|,
name|String
name|key
parameter_list|,
name|Serializable
name|property
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
operator|||
name|property
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|payload
operator|.
name|properties
operator|==
literal|null
condition|)
block|{
name|payload
operator|.
name|properties
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|payload
operator|.
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|property
argument_list|)
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"DefaultExchangeHolder[exchangeId="
argument_list|)
operator|.
name|append
argument_list|(
name|exchangeId
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"inBody="
argument_list|)
operator|.
name|append
argument_list|(
name|inBody
argument_list|)
operator|.
name|append
argument_list|(
literal|", outBody="
argument_list|)
operator|.
name|append
argument_list|(
name|outBody
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", inHeaders="
argument_list|)
operator|.
name|append
argument_list|(
name|inHeaders
argument_list|)
operator|.
name|append
argument_list|(
literal|", outHeaders="
argument_list|)
operator|.
name|append
argument_list|(
name|outHeaders
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", properties="
argument_list|)
operator|.
name|append
argument_list|(
name|properties
argument_list|)
operator|.
name|append
argument_list|(
literal|", exception="
argument_list|)
operator|.
name|append
argument_list|(
name|exception
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|safeSetInHeaders (Exchange exchange, boolean allowSerializedHeaders)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|safeSetInHeaders
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|allowSerializedHeaders
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|checkValidHeaderObjects
argument_list|(
literal|"in headers"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|inHeaders
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|safeSetOutHeaders (Exchange exchange, boolean allowSerializedHeaders)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|safeSetOutHeaders
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|allowSerializedHeaders
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
operator|&&
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|checkValidHeaderObjects
argument_list|(
literal|"out headers"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|outHeaders
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|safeSetProperties (Exchange exchange, boolean allowSerializedHeaders)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|safeSetProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|allowSerializedHeaders
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasProperties
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|checkValidExchangePropertyObjects
argument_list|(
literal|"properties"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getProperties
argument_list|()
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|properties
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|checkSerializableBody (String type, Exchange exchange, Object object)
specifier|private
specifier|static
name|Object
name|checkSerializableBody
parameter_list|(
name|String
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Serializable
name|converted
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Serializable
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|converted
operator|!=
literal|null
condition|)
block|{
return|return
name|converted
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exchange {} containing object: {} of type: {} cannot be serialized, it will be excluded by the holder."
argument_list|,
name|type
argument_list|,
name|object
argument_list|,
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|method|checkValidHeaderObjects (String type, Exchange exchange, Map<String, Object> map, boolean allowSerializedHeaders)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|checkValidHeaderObjects
parameter_list|(
name|String
name|type
parameter_list|,
name|Exchange
name|exchange
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
name|allowSerializedHeaders
parameter_list|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|result
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
name|Object
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// silently skip any values which is null
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|Object
name|value
init|=
name|getValidHeaderValue
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
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|Serializable
name|converted
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Serializable
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|converted
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|converted
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logCannotSerializeObject
argument_list|(
name|type
argument_list|,
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
block|}
else|else
block|{
name|logInvalidHeaderValue
argument_list|(
name|type
argument_list|,
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
block|}
return|return
name|result
return|;
block|}
DECL|method|checkValidExchangePropertyObjects (String type, Exchange exchange, Map<String, Object> map, boolean allowSerializedHeaders)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|checkValidExchangePropertyObjects
parameter_list|(
name|String
name|type
parameter_list|,
name|Exchange
name|exchange
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
name|allowSerializedHeaders
parameter_list|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|result
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
name|Object
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// silently skip any values which is null
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|Object
name|value
init|=
name|getValidExchangePropertyValue
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
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|Serializable
name|converted
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Serializable
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|converted
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|converted
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logCannotSerializeObject
argument_list|(
name|type
argument_list|,
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
block|}
else|else
block|{
name|logInvalidExchangePropertyValue
argument_list|(
name|type
argument_list|,
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
block|}
return|return
name|result
return|;
block|}
comment|/**      * We only want to store header values of primitive and String related types.      *<p/>      * This default implementation will allow:      *<ul>      *<li>any primitives and their counter Objects (Integer, Double etc.)</li>      *<li>String and any other literals, Character, CharSequence</li>      *<li>Boolean</li>      *<li>Number</li>      *<li>java.util.Date</li>      *</ul>      *       * We make possible store serialized headers by the boolean field allowSerializedHeaders      *       * @param headerName   the header name      * @param headerValue  the header value      * @param allowSerializedHeaders  the header value      * @return  the value to use,<tt>null</tt> to ignore this header      */
DECL|method|getValidHeaderValue (String headerName, Object headerValue, boolean allowSerializedHeaders)
specifier|protected
specifier|static
name|Object
name|getValidHeaderValue
parameter_list|(
name|String
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|boolean
name|allowSerializedHeaders
parameter_list|)
block|{
if|if
condition|(
name|headerValue
operator|instanceof
name|String
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|BigInteger
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|BigDecimal
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Number
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Character
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|CharSequence
condition|)
block|{
return|return
name|headerValue
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Boolean
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Date
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|allowSerializedHeaders
condition|)
block|{
if|if
condition|(
name|headerValue
operator|instanceof
name|Serializable
condition|)
block|{
return|return
name|headerValue
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * We only want to store exchange property values of primitive and String related types, and      * as well any caught exception that Camel routing engine has caught.      *<p/>      * This default implementation will allow the same values as {@link #getValidHeaderValue(String, Object, boolean)}      * and in addition any value of type {@link Throwable}.      *      * @param propertyName   the property name      * @param propertyValue  the property value      * @return  the value to use,<tt>null</tt> to ignore this header      */
DECL|method|getValidExchangePropertyValue (String propertyName, Object propertyValue, boolean allowSerializedHeaders)
specifier|protected
specifier|static
name|Object
name|getValidExchangePropertyValue
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|,
name|boolean
name|allowSerializedHeaders
parameter_list|)
block|{
comment|// for exchange properties we also allow exception to be transferred so people can store caught exception
if|if
condition|(
name|propertyValue
operator|instanceof
name|Throwable
condition|)
block|{
return|return
name|propertyValue
return|;
block|}
return|return
name|getValidHeaderValue
argument_list|(
name|propertyName
argument_list|,
name|propertyValue
argument_list|,
name|allowSerializedHeaders
argument_list|)
return|;
block|}
DECL|method|logCannotSerializeObject (String type, String key, Object value)
specifier|private
specifier|static
name|void
name|logCannotSerializeObject
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"Camel"
argument_list|)
condition|)
block|{
comment|// log Camel at DEBUG level
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Exchange {} containing key: {} with object: {} of type: {} cannot be serialized, it will be excluded by the holder."
argument_list|,
operator|new
name|Object
index|[]
block|{
name|type
block|,
name|key
block|,
name|value
block|,
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|value
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// log regular at WARN level
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exchange {} containing key: {} with object: {} of type: {} cannot be serialized, it will be excluded by the holder."
argument_list|,
operator|new
name|Object
index|[]
block|{
name|type
block|,
name|key
block|,
name|value
block|,
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|value
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|logInvalidHeaderValue (String type, String key, Object value)
specifier|private
specifier|static
name|void
name|logInvalidHeaderValue
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Exchange {} containing key: {} with object: {} of type: {} is not valid header type, it will be excluded by the holder."
argument_list|,
operator|new
name|Object
index|[]
block|{
name|type
block|,
name|key
block|,
name|value
block|,
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|value
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|logInvalidExchangePropertyValue (String type, String key, Object value)
specifier|private
specifier|static
name|void
name|logInvalidExchangePropertyValue
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Exchange {} containing key: {} with object: {} of type: {} is not valid exchange property type, it will be excluded by the holder."
argument_list|,
operator|new
name|Object
index|[]
block|{
name|type
block|,
name|key
block|,
name|value
block|,
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|value
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

