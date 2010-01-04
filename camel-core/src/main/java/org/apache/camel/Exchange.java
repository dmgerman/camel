begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

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
name|spi
operator|.
name|Synchronization
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
name|UnitOfWork
import|;
end_import

begin_comment
comment|/**  * The base message exchange interface providing access to the request, response  * and fault {@link Message} instances. Different providers such as JMS, JBI,  * CXF and HTTP can provide their own derived API to expose the underlying  * transport semantics to avoid the leaky abstractions of generic APIs.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Exchange
specifier|public
interface|interface
name|Exchange
block|{
DECL|field|ACCEPT_CONTENT_TYPE
name|String
name|ACCEPT_CONTENT_TYPE
init|=
literal|"CamelAcceptContentType"
decl_stmt|;
DECL|field|AGGREGATED_INDEX
name|String
name|AGGREGATED_INDEX
init|=
literal|"CamelAggregatedIndex"
decl_stmt|;
DECL|field|AGGREGATED_SIZE
name|String
name|AGGREGATED_SIZE
init|=
literal|"CamelAggregatedSize"
decl_stmt|;
DECL|field|ASYNC_WAIT
name|String
name|ASYNC_WAIT
init|=
literal|"CamelAsyncWait"
decl_stmt|;
DECL|field|BATCH_INDEX
name|String
name|BATCH_INDEX
init|=
literal|"CamelBatchIndex"
decl_stmt|;
DECL|field|BATCH_SIZE
name|String
name|BATCH_SIZE
init|=
literal|"CamelBatchSize"
decl_stmt|;
DECL|field|BATCH_COMPLETE
name|String
name|BATCH_COMPLETE
init|=
literal|"CamelBatchComplete"
decl_stmt|;
DECL|field|BEAN_METHOD_NAME
name|String
name|BEAN_METHOD_NAME
init|=
literal|"CamelBeanMethodName"
decl_stmt|;
annotation|@
name|Deprecated
DECL|field|BEAN_HOLDER
name|String
name|BEAN_HOLDER
init|=
literal|"CamelBeanHolder"
decl_stmt|;
DECL|field|BEAN_MULTI_PARAMETER_ARRAY
name|String
name|BEAN_MULTI_PARAMETER_ARRAY
init|=
literal|"CamelBeanMultiParameterArray"
decl_stmt|;
DECL|field|BINDING
name|String
name|BINDING
init|=
literal|"CamelBinding"
decl_stmt|;
DECL|field|CHARSET_NAME
name|String
name|CHARSET_NAME
init|=
literal|"CamelCharsetName"
decl_stmt|;
DECL|field|CONTENT_ENCODING
name|String
name|CONTENT_ENCODING
init|=
literal|"Content-Encoding"
decl_stmt|;
DECL|field|CONTENT_TYPE
name|String
name|CONTENT_TYPE
init|=
literal|"Content-Type"
decl_stmt|;
DECL|field|CORRELATION_ID
name|String
name|CORRELATION_ID
init|=
literal|"CamelCorrelationId"
decl_stmt|;
DECL|field|DATASET_INDEX
name|String
name|DATASET_INDEX
init|=
literal|"CamelDataSetIndex"
decl_stmt|;
DECL|field|EXCEPTION_CAUGHT
name|String
name|EXCEPTION_CAUGHT
init|=
literal|"CamelExceptionCaught"
decl_stmt|;
DECL|field|ERRORHANDLER_HANDLED
name|String
name|ERRORHANDLER_HANDLED
init|=
literal|"CamelErrorHandlerHandled"
decl_stmt|;
DECL|field|FAILURE_HANDLED
name|String
name|FAILURE_HANDLED
init|=
literal|"CamelFailureHandled"
decl_stmt|;
DECL|field|FAILURE_ENDPOINT
name|String
name|FAILURE_ENDPOINT
init|=
literal|"CamelFailureEndpoint"
decl_stmt|;
DECL|field|FILE_LOCAL_WORK_PATH
name|String
name|FILE_LOCAL_WORK_PATH
init|=
literal|"CamelFileLocalWorkPath"
decl_stmt|;
DECL|field|FILE_NAME
name|String
name|FILE_NAME
init|=
literal|"CamelFileName"
decl_stmt|;
DECL|field|FILE_NAME_ONLY
name|String
name|FILE_NAME_ONLY
init|=
literal|"CamelFileNameOnly"
decl_stmt|;
DECL|field|FILE_NAME_PRODUCED
name|String
name|FILE_NAME_PRODUCED
init|=
literal|"CamelFileNameProduced"
decl_stmt|;
DECL|field|FILE_PATH
name|String
name|FILE_PATH
init|=
literal|"CamelFilePath"
decl_stmt|;
DECL|field|FILE_PARENT
name|String
name|FILE_PARENT
init|=
literal|"CamelFileParent"
decl_stmt|;
DECL|field|GROUPED_EXCHANGE
name|String
name|GROUPED_EXCHANGE
init|=
literal|"CamelGroupedExchange"
decl_stmt|;
DECL|field|HTTP_BASE_URI
name|String
name|HTTP_BASE_URI
init|=
literal|"CamelHttpBaseUri"
decl_stmt|;
DECL|field|HTTP_CHARACTER_ENCODING
name|String
name|HTTP_CHARACTER_ENCODING
init|=
literal|"CamelHttpCharacterEncoding"
decl_stmt|;
DECL|field|HTTP_METHOD
name|String
name|HTTP_METHOD
init|=
literal|"CamelHttpMethod"
decl_stmt|;
DECL|field|HTTP_PATH
name|String
name|HTTP_PATH
init|=
literal|"CamelHttpPath"
decl_stmt|;
DECL|field|HTTP_QUERY
name|String
name|HTTP_QUERY
init|=
literal|"CamelHttpQuery"
decl_stmt|;
DECL|field|HTTP_RESPONSE_CODE
name|String
name|HTTP_RESPONSE_CODE
init|=
literal|"CamelHttpResponseCode"
decl_stmt|;
DECL|field|HTTP_URI
name|String
name|HTTP_URI
init|=
literal|"CamelHttpUri"
decl_stmt|;
DECL|field|HTTP_URL
name|String
name|HTTP_URL
init|=
literal|"CamelHttpUrl"
decl_stmt|;
DECL|field|HTTP_CHUNKED
name|String
name|HTTP_CHUNKED
init|=
literal|"CamelHttpChunked"
decl_stmt|;
DECL|field|INTERCEPTED_ENDPOINT
name|String
name|INTERCEPTED_ENDPOINT
init|=
literal|"CamelInterceptedEndpoint"
decl_stmt|;
DECL|field|TO_ENDPOINT
name|String
name|TO_ENDPOINT
init|=
literal|"CamelToEndpoint"
decl_stmt|;
DECL|field|LOG_DEBUG_BODY_MAX_CHARS
name|String
name|LOG_DEBUG_BODY_MAX_CHARS
init|=
literal|"CamelLogDebugBodyMaxChars"
decl_stmt|;
DECL|field|LOG_DEBUG_BODY_STREAMS
name|String
name|LOG_DEBUG_BODY_STREAMS
init|=
literal|"CamelLogDebugStreams"
decl_stmt|;
DECL|field|LOOP_INDEX
name|String
name|LOOP_INDEX
init|=
literal|"CamelLoopIndex"
decl_stmt|;
DECL|field|LOOP_SIZE
name|String
name|LOOP_SIZE
init|=
literal|"CamelLoopSize"
decl_stmt|;
DECL|field|MULTICAST_INDEX
name|String
name|MULTICAST_INDEX
init|=
literal|"CamelMulticastIndex"
decl_stmt|;
DECL|field|ON_COMPLETION
name|String
name|ON_COMPLETION
init|=
literal|"CamelOnCompletion"
decl_stmt|;
DECL|field|ROUTE_STOP
name|String
name|ROUTE_STOP
init|=
literal|"CamelRouteStop"
decl_stmt|;
DECL|field|REDELIVERED
name|String
name|REDELIVERED
init|=
literal|"CamelRedelivered"
decl_stmt|;
DECL|field|REDELIVERY_COUNTER
name|String
name|REDELIVERY_COUNTER
init|=
literal|"CamelRedeliveryCounter"
decl_stmt|;
DECL|field|REDELIVERY_EXHAUSTED
name|String
name|REDELIVERY_EXHAUSTED
init|=
literal|"CamelRedeliveryExhausted"
decl_stmt|;
DECL|field|ROLLBACK_ONLY
name|String
name|ROLLBACK_ONLY
init|=
literal|"CamelRollbackOnly"
decl_stmt|;
DECL|field|ROLLBACK_ONLY_LAST
name|String
name|ROLLBACK_ONLY_LAST
init|=
literal|"CamelRollbackOnlyLast"
decl_stmt|;
DECL|field|SPLIT_INDEX
name|String
name|SPLIT_INDEX
init|=
literal|"CamelSplitIndex"
decl_stmt|;
DECL|field|SPLIT_SIZE
name|String
name|SPLIT_SIZE
init|=
literal|"CamelSplitSize"
decl_stmt|;
DECL|field|TIMER_FIRED_TIME
name|String
name|TIMER_FIRED_TIME
init|=
literal|"CamelTimerFiredTime"
decl_stmt|;
DECL|field|TIMER_NAME
name|String
name|TIMER_NAME
init|=
literal|"CamelTimerName"
decl_stmt|;
DECL|field|TIMER_PERIOD
name|String
name|TIMER_PERIOD
init|=
literal|"CamelTimerPeriod"
decl_stmt|;
DECL|field|TIMER_TIME
name|String
name|TIMER_TIME
init|=
literal|"CamelTimerTime"
decl_stmt|;
DECL|field|TRANSACTED
name|String
name|TRANSACTED
init|=
literal|"CamelTransacted"
decl_stmt|;
DECL|field|TRACE_EVENT
name|String
name|TRACE_EVENT
init|=
literal|"CamelTraceEvent"
decl_stmt|;
DECL|field|TRACE_EVENT_NODE_ID
name|String
name|TRACE_EVENT_NODE_ID
init|=
literal|"CamelTraceEventNodeId"
decl_stmt|;
DECL|field|TRACE_EVENT_TIMESTAMP
name|String
name|TRACE_EVENT_TIMESTAMP
init|=
literal|"CamelTraceEventTimestamp"
decl_stmt|;
DECL|field|TRACE_EVENT_EXCHANGE
name|String
name|TRACE_EVENT_EXCHANGE
init|=
literal|"CamelTraceEventExchange"
decl_stmt|;
comment|/**      * Returns the {@link ExchangePattern} (MEP) of this exchange.      *      * @return the message exchange pattern of this exchange      */
DECL|method|getPattern ()
name|ExchangePattern
name|getPattern
parameter_list|()
function_decl|;
comment|/**      * Allows the {@link ExchangePattern} (MEP) of this exchange to be customized.      *      * This typically won't be required as an exchange can be created with a specific MEP      * by calling {@link Endpoint#createExchange(ExchangePattern)} but it is here just in case      * it is needed.      *      * @param pattern  the pattern       */
DECL|method|setPattern (ExchangePattern pattern)
name|void
name|setPattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
function_decl|;
comment|/**      * Returns a property associated with this exchange by name      *      * @param name the name of the property      * @return the value of the given property or<tt>null</tt> if there is no property for      *         the given name      */
DECL|method|getProperty (String name)
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns a property associated with this exchange by name      *      * @param name the name of the property      * @param defaultValue the default value to return if property was absent      * @return the value of the given property or<tt>defaultValue</tt> if there is no      *         property for the given name      */
DECL|method|getProperty (String name, Object defaultValue)
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|defaultValue
parameter_list|)
function_decl|;
comment|/**      * Returns a property associated with this exchange by name and specifying      * the type required      *      * @param name the name of the property      * @param type the type of the property      * @return the value of the given property or<tt>null</tt> if there is no property for      *         the given name or<tt>null</tt> if it cannot be converted to the given type      */
DECL|method|getProperty (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getProperty
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
function_decl|;
comment|/**      * Returns a property associated with this exchange by name and specifying      * the type required      *      * @param name the name of the property      * @param defaultValue the default value to return if property was absent      * @param type the type of the property      * @return the value of the given property or<tt>defaultValue</tt> if there is no property for      *         the given name or<tt>null</tt> if it cannot be converted to the given type      */
DECL|method|getProperty (String name, Object defaultValue, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getProperty
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
function_decl|;
comment|/**      * Sets a property on the exchange      *      * @param name  of the property      * @param value to associate with the name      */
DECL|method|setProperty (String name, Object value)
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Removes the given property on the exchange      *      * @param name of the property      * @return the old value of the property      */
DECL|method|removeProperty (String name)
name|Object
name|removeProperty
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns all of the properties associated with the exchange      *      * @return all the headers in a Map      */
DECL|method|getProperties ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
function_decl|;
comment|/**      * Returns whether any properties has been set      *      * @return<tt>true</tt> if any properties has been set      */
DECL|method|hasProperties ()
name|boolean
name|hasProperties
parameter_list|()
function_decl|;
comment|/**      * Returns the inbound request message      *      * @return the message      */
DECL|method|getIn ()
name|Message
name|getIn
parameter_list|()
function_decl|;
comment|/**      * Returns the inbound request message as the given type      *      * @param type the given type      * @return the message as the given type or<tt>null</tt> if not possible to covert to given type      */
DECL|method|getIn (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getIn
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sets the inbound message instance      *      * @param in the inbound message      */
DECL|method|setIn (Message in)
name|void
name|setIn
parameter_list|(
name|Message
name|in
parameter_list|)
function_decl|;
comment|/**      * Returns the outbound message, lazily creating one if one has not already      * been associated with this exchange.      *<p/>      * If you want to test whether an OUT message have been set or not, use the {@link #hasOut()} method.      *      * @return the response      */
DECL|method|getOut ()
name|Message
name|getOut
parameter_list|()
function_decl|;
comment|/**      * Returns the outbound request message as the given type      *      * @param type the given type      * @return the message as the given type or<tt>null</tt> if not possible to covert to given type      */
DECL|method|getOut (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getOut
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Returns whether an OUT message has been set or not.      *      * @return<tt>true</tt> if an OUT message exists,<tt>false</tt> otherwise.      */
DECL|method|hasOut ()
name|boolean
name|hasOut
parameter_list|()
function_decl|;
comment|/**      * Sets the outbound message      *      * @param out the outbound message      */
DECL|method|setOut (Message out)
name|void
name|setOut
parameter_list|(
name|Message
name|out
parameter_list|)
function_decl|;
comment|/**      * Returns the exception associated with this exchange      *      * @return the exception (or null if no faults)      */
DECL|method|getException ()
name|Exception
name|getException
parameter_list|()
function_decl|;
comment|/**      * Returns the exception associated with this exchange.      *<p/>      * Is used to get the caused exception that typically have been wrapped in some sort      * of Camel wrapper exception      *<p/>      * The strategy is to look in the exception hierarchy to find the first given cause that matches the type.      * Will start from the bottom (the real cause) and walk upwards.      *      * @param type the exception type      * @return the exception (or<tt>null</tt> if no caused exception matched)      */
DECL|method|getException (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getException
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sets the exception associated with this exchange      *      * @param e  the caused exception      */
DECL|method|setException (Exception e)
name|void
name|setException
parameter_list|(
name|Exception
name|e
parameter_list|)
function_decl|;
comment|/**      * Returns true if this exchange failed due to either an exception or fault      *      * @return true if this exchange failed due to either an exception or fault      * @see Exchange#getException()      * @see Message#setFault(boolean)      * @see Message#isFault()      */
DECL|method|isFailed ()
name|boolean
name|isFailed
parameter_list|()
function_decl|;
comment|/**      * Returns true if this exchange is transacted      */
DECL|method|isTransacted ()
name|boolean
name|isTransacted
parameter_list|()
function_decl|;
comment|/**      * Returns true if this exchange is marked for rollback      */
DECL|method|isRollbackOnly ()
name|boolean
name|isRollbackOnly
parameter_list|()
function_decl|;
comment|/**      * Returns the container so that a processor can resolve endpoints from URIs      *      * @return the container which owns this exchange      */
DECL|method|getContext ()
name|CamelContext
name|getContext
parameter_list|()
function_decl|;
comment|/**      * Creates a copy of the current message exchange so that it can be      * forwarded to another destination      */
DECL|method|copy ()
name|Exchange
name|copy
parameter_list|()
function_decl|;
comment|/**      * Returns the endpoint which originated this message exchange if a consumer on an endpoint created the message exchange      * otherwise this property will be null      */
DECL|method|getFromEndpoint ()
name|Endpoint
name|getFromEndpoint
parameter_list|()
function_decl|;
comment|/**      * Sets the endpoint which originated this message exchange. This method      * should typically only be called by {@link org.apache.camel.Endpoint} implementations      *      * @param fromEndpoint the endpoint which is originating this message exchange      */
DECL|method|setFromEndpoint (Endpoint fromEndpoint)
name|void
name|setFromEndpoint
parameter_list|(
name|Endpoint
name|fromEndpoint
parameter_list|)
function_decl|;
comment|/**      * Returns the unit of work that this exchange belongs to; which may map to      * zero, one or more physical transactions      */
DECL|method|getUnitOfWork ()
name|UnitOfWork
name|getUnitOfWork
parameter_list|()
function_decl|;
comment|/**      * Sets the unit of work that this exchange belongs to; which may map to      * zero, one or more physical transactions      */
DECL|method|setUnitOfWork (UnitOfWork unitOfWork)
name|void
name|setUnitOfWork
parameter_list|(
name|UnitOfWork
name|unitOfWork
parameter_list|)
function_decl|;
comment|/**      * Returns the exchange id (unique)      */
DECL|method|getExchangeId ()
name|String
name|getExchangeId
parameter_list|()
function_decl|;
comment|/**      * Set the exchange id      */
DECL|method|setExchangeId (String id)
name|void
name|setExchangeId
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Adds a {@link org.apache.camel.spi.Synchronization} to be invoked as callback when      * this exchange is completed.      *      * @param onCompletion  the callback to invoke on completion of this exchange      */
DECL|method|addOnCompletion (Synchronization onCompletion)
name|void
name|addOnCompletion
parameter_list|(
name|Synchronization
name|onCompletion
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

