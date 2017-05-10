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
comment|/**  * An Exchange is the message container holding the information during the entire routing of  * a {@link  Message} received by a {@link Consumer}.   *<p/>  * During processing down the {@link Processor} chain, the {@link Exchange} provides access to the   * current (not the original) request and response {@link Message} messages. The {@link Exchange}   * also holds meta-data during its entire lifetime stored as properties accessible using the   * various {@link #getProperty(String)} methods. The {@link #setProperty(String, Object)} is   * used to store a property. For example you can use this to store security, SLA related   * data or any other information deemed useful throughout processing. If an {@link Exchange}   * failed during routing the {@link Exception} that caused the failure is stored and accessible   * via the {@link #getException()} method.  *<p/>  * An Exchange is created when a {@link Consumer} receives a request. A new {@link Message} is  * created, the request is set as the body of the {@link Message} and depending on the {@link Consumer}  * other {@link Endpoint} and protocol related information is added as headers on the {@link Message}.  * Then an Exchange is created and the newly created {@link Message} is set as the in on the Exchange.  * Therefore an Exchange starts its life in a {@link Consumer}. The Exchange is then sent down the  * {@link Route} for processing along a {@link Processor} chain. The {@link Processor} as the name  * suggests is what processes the {@link Message} in the Exchange and Camel, in addition to   * providing out-of-the-box a large number of useful processors, it also allows you to create your own.   * The rule Camel uses is to take the out {@link Message} produced by the previous {@link Processor}   * and set it as the in for the next {@link Processor}. If the previous {@link Processor} did not  * produce an out, then the in of the previous {@link Processor} is sent as the next in. At the  * end of the processing chain, depending on the {@link ExchangePattern Message Exchange Pattern} (or MEP)  * the last out (or in of no out available) is sent by the {@link Consumer} back to the original caller.  *<p/>  * Camel, in addition to providing out-of-the-box a large number of useful processors, it also allows   * you to implement and use your own. When the Exchange is passed to a {@link Processor}, it always   * contains an in {@link Message} and no out {@link Message}. The {@link Processor}<b>may</b> produce   * an out, depending on the nature of the {@link Processor}. The in {@link Message} can be accessed   * using the {@link #getIn()} method. Since the out message is null when entering the {@link Processor},   * the {@link #getOut()} method is actually a convenient factory method that will lazily instantiate a   * {@link org.apache.camel.impl.DefaultMessage} which you could populate. As an alternative you could   * also instantiate your specialized  {@link Message} and set it on the exchange using the   * {@link #setOut(org.apache.camel.Message)} method. Please note that a {@link Message} contains not only   * the body but also headers and attachments. If you are creating a new {@link Message} the headers and   * attachments of the in {@link Message} are not automatically copied to the out by Camel and you'll have   * to set the headers and attachments you need yourself. If your {@link Processor} is not producing a   * different {@link Message} but only needs to slightly  modify the in, you can simply update the in   * {@link Message} returned by {@link #getIn()}.   *<p/>  * See this<a href="http://camel.apache.org/using-getin-or-getout-methods-on-exchange.html">FAQ entry</a>   * for more details.  *   */
end_comment

begin_interface
DECL|interface|Exchange
specifier|public
interface|interface
name|Exchange
block|{
DECL|field|AUTHENTICATION
name|String
name|AUTHENTICATION
init|=
literal|"CamelAuthentication"
decl_stmt|;
DECL|field|AUTHENTICATION_FAILURE_POLICY_ID
name|String
name|AUTHENTICATION_FAILURE_POLICY_ID
init|=
literal|"CamelAuthenticationFailurePolicyId"
decl_stmt|;
DECL|field|ACCEPT_CONTENT_TYPE
name|String
name|ACCEPT_CONTENT_TYPE
init|=
literal|"CamelAcceptContentType"
decl_stmt|;
DECL|field|AGGREGATED_SIZE
name|String
name|AGGREGATED_SIZE
init|=
literal|"CamelAggregatedSize"
decl_stmt|;
DECL|field|AGGREGATED_TIMEOUT
name|String
name|AGGREGATED_TIMEOUT
init|=
literal|"CamelAggregatedTimeout"
decl_stmt|;
DECL|field|AGGREGATED_COMPLETED_BY
name|String
name|AGGREGATED_COMPLETED_BY
init|=
literal|"CamelAggregatedCompletedBy"
decl_stmt|;
DECL|field|AGGREGATED_CORRELATION_KEY
name|String
name|AGGREGATED_CORRELATION_KEY
init|=
literal|"CamelAggregatedCorrelationKey"
decl_stmt|;
DECL|field|AGGREGATED_COLLECTION_GUARD
name|String
name|AGGREGATED_COLLECTION_GUARD
init|=
literal|"CamelAggregatedCollectionGuard"
decl_stmt|;
DECL|field|AGGREGATION_STRATEGY
name|String
name|AGGREGATION_STRATEGY
init|=
literal|"CamelAggregationStrategy"
decl_stmt|;
DECL|field|AGGREGATION_COMPLETE_CURRENT_GROUP
name|String
name|AGGREGATION_COMPLETE_CURRENT_GROUP
init|=
literal|"CamelAggregationCompleteCurrentGroup"
decl_stmt|;
DECL|field|AGGREGATION_COMPLETE_ALL_GROUPS
name|String
name|AGGREGATION_COMPLETE_ALL_GROUPS
init|=
literal|"CamelAggregationCompleteAllGroups"
decl_stmt|;
DECL|field|AGGREGATION_COMPLETE_ALL_GROUPS_INCLUSIVE
name|String
name|AGGREGATION_COMPLETE_ALL_GROUPS_INCLUSIVE
init|=
literal|"CamelAggregationCompleteAllGroupsInclusive"
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
comment|// do not prefix with Camel and use lower-case starting letter as its a shared key
comment|// used across other Apache products such as AMQ, SMX etc.
DECL|field|BREADCRUMB_ID
name|String
name|BREADCRUMB_ID
init|=
literal|"breadcrumbId"
decl_stmt|;
DECL|field|CHARSET_NAME
name|String
name|CHARSET_NAME
init|=
literal|"CamelCharsetName"
decl_stmt|;
DECL|field|CIRCUIT_BREAKER_STATE
name|String
name|CIRCUIT_BREAKER_STATE
init|=
literal|"CamelCircuitBreakerState"
decl_stmt|;
DECL|field|CREATED_TIMESTAMP
name|String
name|CREATED_TIMESTAMP
init|=
literal|"CamelCreatedTimestamp"
decl_stmt|;
DECL|field|CONTENT_ENCODING
name|String
name|CONTENT_ENCODING
init|=
literal|"Content-Encoding"
decl_stmt|;
DECL|field|CONTENT_LENGTH
name|String
name|CONTENT_LENGTH
init|=
literal|"Content-Length"
decl_stmt|;
DECL|field|CONTENT_TYPE
name|String
name|CONTENT_TYPE
init|=
literal|"Content-Type"
decl_stmt|;
DECL|field|COOKIE_HANDLER
name|String
name|COOKIE_HANDLER
init|=
literal|"CamelCookieHandler"
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
DECL|field|DEFAULT_CHARSET_PROPERTY
name|String
name|DEFAULT_CHARSET_PROPERTY
init|=
literal|"org.apache.camel.default.charset"
decl_stmt|;
DECL|field|DESTINATION_OVERRIDE_URL
name|String
name|DESTINATION_OVERRIDE_URL
init|=
literal|"CamelDestinationOverrideUrl"
decl_stmt|;
DECL|field|DISABLE_HTTP_STREAM_CACHE
name|String
name|DISABLE_HTTP_STREAM_CACHE
init|=
literal|"CamelDisableHttpStreamCache"
decl_stmt|;
DECL|field|DUPLICATE_MESSAGE
name|String
name|DUPLICATE_MESSAGE
init|=
literal|"CamelDuplicateMessage"
decl_stmt|;
DECL|field|DOCUMENT_BUILDER_FACTORY
name|String
name|DOCUMENT_BUILDER_FACTORY
init|=
literal|"CamelDocumentBuilderFactory"
decl_stmt|;
DECL|field|EXCEPTION_CAUGHT
name|String
name|EXCEPTION_CAUGHT
init|=
literal|"CamelExceptionCaught"
decl_stmt|;
DECL|field|EXCEPTION_HANDLED
name|String
name|EXCEPTION_HANDLED
init|=
literal|"CamelExceptionHandled"
decl_stmt|;
DECL|field|EVALUATE_EXPRESSION_RESULT
name|String
name|EVALUATE_EXPRESSION_RESULT
init|=
literal|"CamelEvaluateExpressionResult"
decl_stmt|;
DECL|field|ERRORHANDLER_HANDLED
name|String
name|ERRORHANDLER_HANDLED
init|=
literal|"CamelErrorHandlerHandled"
decl_stmt|;
DECL|field|EXTERNAL_REDELIVERED
name|String
name|EXTERNAL_REDELIVERED
init|=
literal|"CamelExternalRedelivered"
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
DECL|field|FAILURE_ROUTE_ID
name|String
name|FAILURE_ROUTE_ID
init|=
literal|"CamelFailureRouteId"
decl_stmt|;
DECL|field|FATAL_FALLBACK_ERROR_HANDLER
name|String
name|FATAL_FALLBACK_ERROR_HANDLER
init|=
literal|"CamelFatalFallbackErrorHandler"
decl_stmt|;
DECL|field|FILE_CONTENT_TYPE
name|String
name|FILE_CONTENT_TYPE
init|=
literal|"CamelFileContentType"
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
DECL|field|FILE_NAME_CONSUMED
name|String
name|FILE_NAME_CONSUMED
init|=
literal|"CamelFileNameConsumed"
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
DECL|field|FILE_LAST_MODIFIED
name|String
name|FILE_LAST_MODIFIED
init|=
literal|"CamelFileLastModified"
decl_stmt|;
DECL|field|FILE_LENGTH
name|String
name|FILE_LENGTH
init|=
literal|"CamelFileLength"
decl_stmt|;
DECL|field|FILE_LOCK_FILE_ACQUIRED
name|String
name|FILE_LOCK_FILE_ACQUIRED
init|=
literal|"CamelFileLockFileAcquired"
decl_stmt|;
DECL|field|FILE_LOCK_FILE_NAME
name|String
name|FILE_LOCK_FILE_NAME
init|=
literal|"CamelFileLockFileName"
decl_stmt|;
DECL|field|FILE_LOCK_EXCLUSIVE_LOCK
name|String
name|FILE_LOCK_EXCLUSIVE_LOCK
init|=
literal|"CamelFileLockExclusiveLock"
decl_stmt|;
DECL|field|FILE_LOCK_RANDOM_ACCESS_FILE
name|String
name|FILE_LOCK_RANDOM_ACCESS_FILE
init|=
literal|"CamelFileLockRandomAccessFile"
decl_stmt|;
DECL|field|FILTER_MATCHED
name|String
name|FILTER_MATCHED
init|=
literal|"CamelFilterMatched"
decl_stmt|;
DECL|field|FILTER_NON_XML_CHARS
name|String
name|FILTER_NON_XML_CHARS
init|=
literal|"CamelFilterNonXmlChars"
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
DECL|field|HTTP_PROTOCOL_VERSION
name|String
name|HTTP_PROTOCOL_VERSION
init|=
literal|"CamelHttpProtocolVersion"
decl_stmt|;
DECL|field|HTTP_QUERY
name|String
name|HTTP_QUERY
init|=
literal|"CamelHttpQuery"
decl_stmt|;
DECL|field|HTTP_RAW_QUERY
name|String
name|HTTP_RAW_QUERY
init|=
literal|"CamelHttpRawQuery"
decl_stmt|;
DECL|field|HTTP_RESPONSE_CODE
name|String
name|HTTP_RESPONSE_CODE
init|=
literal|"CamelHttpResponseCode"
decl_stmt|;
DECL|field|HTTP_RESPONSE_TEXT
name|String
name|HTTP_RESPONSE_TEXT
init|=
literal|"CamelHttpResponseText"
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
DECL|field|HTTP_SERVLET_REQUEST
name|String
name|HTTP_SERVLET_REQUEST
init|=
literal|"CamelHttpServletRequest"
decl_stmt|;
DECL|field|HTTP_SERVLET_RESPONSE
name|String
name|HTTP_SERVLET_RESPONSE
init|=
literal|"CamelHttpServletResponse"
decl_stmt|;
DECL|field|INTERCEPTED_ENDPOINT
name|String
name|INTERCEPTED_ENDPOINT
init|=
literal|"CamelInterceptedEndpoint"
decl_stmt|;
DECL|field|INTERCEPT_SEND_TO_ENDPOINT_WHEN_MATCHED
name|String
name|INTERCEPT_SEND_TO_ENDPOINT_WHEN_MATCHED
init|=
literal|"CamelInterceptSendToEndpointWhenMatched"
decl_stmt|;
DECL|field|LANGUAGE_SCRIPT
name|String
name|LANGUAGE_SCRIPT
init|=
literal|"CamelLanguageScript"
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
DECL|field|LOG_EIP_NAME
name|String
name|LOG_EIP_NAME
init|=
literal|"CamelLogEipName"
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
DECL|field|MAXIMUM_CACHE_POOL_SIZE
name|String
name|MAXIMUM_CACHE_POOL_SIZE
init|=
literal|"CamelMaximumCachePoolSize"
decl_stmt|;
DECL|field|MAXIMUM_ENDPOINT_CACHE_SIZE
name|String
name|MAXIMUM_ENDPOINT_CACHE_SIZE
init|=
literal|"CamelMaximumEndpointCacheSize"
decl_stmt|;
DECL|field|MAXIMUM_TRANSFORMER_CACHE_SIZE
name|String
name|MAXIMUM_TRANSFORMER_CACHE_SIZE
init|=
literal|"CamelMaximumTransformerCacheSize"
decl_stmt|;
DECL|field|MAXIMUM_VALIDATOR_CACHE_SIZE
name|String
name|MAXIMUM_VALIDATOR_CACHE_SIZE
init|=
literal|"CamelMaximumValidatorCacheSize"
decl_stmt|;
DECL|field|MESSAGE_HISTORY
name|String
name|MESSAGE_HISTORY
init|=
literal|"CamelMessageHistory"
decl_stmt|;
DECL|field|MESSAGE_HISTORY_HEADER_FORMAT
name|String
name|MESSAGE_HISTORY_HEADER_FORMAT
init|=
literal|"CamelMessageHistoryHeaderFormat"
decl_stmt|;
DECL|field|MESSAGE_HISTORY_OUTPUT_FORMAT
name|String
name|MESSAGE_HISTORY_OUTPUT_FORMAT
init|=
literal|"CamelMessageHistoryOutputFormat"
decl_stmt|;
DECL|field|MULTICAST_INDEX
name|String
name|MULTICAST_INDEX
init|=
literal|"CamelMulticastIndex"
decl_stmt|;
DECL|field|MULTICAST_COMPLETE
name|String
name|MULTICAST_COMPLETE
init|=
literal|"CamelMulticastComplete"
decl_stmt|;
DECL|field|NOTIFY_EVENT
name|String
name|NOTIFY_EVENT
init|=
literal|"CamelNotifyEvent"
decl_stmt|;
DECL|field|ON_COMPLETION
name|String
name|ON_COMPLETION
init|=
literal|"CamelOnCompletion"
decl_stmt|;
DECL|field|OVERRULE_FILE_NAME
name|String
name|OVERRULE_FILE_NAME
init|=
literal|"CamelOverruleFileName"
decl_stmt|;
DECL|field|PARENT_UNIT_OF_WORK
name|String
name|PARENT_UNIT_OF_WORK
init|=
literal|"CamelParentUnitOfWork"
decl_stmt|;
DECL|field|STREAM_CACHE_UNIT_OF_WORK
name|String
name|STREAM_CACHE_UNIT_OF_WORK
init|=
literal|"CamelStreamCacheUnitOfWork"
decl_stmt|;
DECL|field|RECIPIENT_LIST_ENDPOINT
name|String
name|RECIPIENT_LIST_ENDPOINT
init|=
literal|"CamelRecipientListEndpoint"
decl_stmt|;
DECL|field|RECEIVED_TIMESTAMP
name|String
name|RECEIVED_TIMESTAMP
init|=
literal|"CamelReceivedTimestamp"
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
DECL|field|REDELIVERY_MAX_COUNTER
name|String
name|REDELIVERY_MAX_COUNTER
init|=
literal|"CamelRedeliveryMaxCounter"
decl_stmt|;
DECL|field|REDELIVERY_EXHAUSTED
name|String
name|REDELIVERY_EXHAUSTED
init|=
literal|"CamelRedeliveryExhausted"
decl_stmt|;
DECL|field|REDELIVERY_DELAY
name|String
name|REDELIVERY_DELAY
init|=
literal|"CamelRedeliveryDelay"
decl_stmt|;
DECL|field|REST_HTTP_URI
name|String
name|REST_HTTP_URI
init|=
literal|"CamelRestHttpUri"
decl_stmt|;
DECL|field|REST_HTTP_QUERY
name|String
name|REST_HTTP_QUERY
init|=
literal|"CamelRestHttpQuery"
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
DECL|field|ROUTE_STOP
name|String
name|ROUTE_STOP
init|=
literal|"CamelRouteStop"
decl_stmt|;
DECL|field|REUSE_SCRIPT_ENGINE
name|String
name|REUSE_SCRIPT_ENGINE
init|=
literal|"CamelReuseScripteEngine"
decl_stmt|;
DECL|field|COMPILE_SCRIPT
name|String
name|COMPILE_SCRIPT
init|=
literal|"CamelCompileScript"
decl_stmt|;
DECL|field|SAXPARSER_FACTORY
name|String
name|SAXPARSER_FACTORY
init|=
literal|"CamelSAXParserFactory"
decl_stmt|;
DECL|field|SCHEDULER_POLLED_MESSAGES
name|String
name|SCHEDULER_POLLED_MESSAGES
init|=
literal|"CamelSchedulerPolledMessages"
decl_stmt|;
DECL|field|SOAP_ACTION
name|String
name|SOAP_ACTION
init|=
literal|"CamelSoapAction"
decl_stmt|;
DECL|field|SKIP_GZIP_ENCODING
name|String
name|SKIP_GZIP_ENCODING
init|=
literal|"CamelSkipGzipEncoding"
decl_stmt|;
DECL|field|SKIP_WWW_FORM_URLENCODED
name|String
name|SKIP_WWW_FORM_URLENCODED
init|=
literal|"CamelSkipWwwFormUrlEncoding"
decl_stmt|;
DECL|field|SLIP_ENDPOINT
name|String
name|SLIP_ENDPOINT
init|=
literal|"CamelSlipEndpoint"
decl_stmt|;
DECL|field|SPLIT_INDEX
name|String
name|SPLIT_INDEX
init|=
literal|"CamelSplitIndex"
decl_stmt|;
DECL|field|SPLIT_COMPLETE
name|String
name|SPLIT_COMPLETE
init|=
literal|"CamelSplitComplete"
decl_stmt|;
DECL|field|SPLIT_SIZE
name|String
name|SPLIT_SIZE
init|=
literal|"CamelSplitSize"
decl_stmt|;
DECL|field|TIMER_COUNTER
name|String
name|TIMER_COUNTER
init|=
literal|"CamelTimerCounter"
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
DECL|field|TO_ENDPOINT
name|String
name|TO_ENDPOINT
init|=
literal|"CamelToEndpoint"
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
DECL|field|TRY_ROUTE_BLOCK
name|String
name|TRY_ROUTE_BLOCK
init|=
literal|"TryRouteBlock"
decl_stmt|;
DECL|field|TRANSFER_ENCODING
name|String
name|TRANSFER_ENCODING
init|=
literal|"Transfer-Encoding"
decl_stmt|;
DECL|field|UNIT_OF_WORK_EXHAUSTED
name|String
name|UNIT_OF_WORK_EXHAUSTED
init|=
literal|"CamelUnitOfWorkExhausted"
decl_stmt|;
comment|/**      * @deprecated UNIT_OF_WORK_PROCESS_SYNC is not in use and will be removed in future Camel release      */
annotation|@
name|Deprecated
DECL|field|UNIT_OF_WORK_PROCESS_SYNC
name|String
name|UNIT_OF_WORK_PROCESS_SYNC
init|=
literal|"CamelUnitOfWorkProcessSync"
decl_stmt|;
DECL|field|XSLT_FILE_NAME
name|String
name|XSLT_FILE_NAME
init|=
literal|"CamelXsltFileName"
decl_stmt|;
DECL|field|XSLT_ERROR
name|String
name|XSLT_ERROR
init|=
literal|"CamelXsltError"
decl_stmt|;
DECL|field|XSLT_FATAL_ERROR
name|String
name|XSLT_FATAL_ERROR
init|=
literal|"CamelXsltFatalError"
decl_stmt|;
DECL|field|XSLT_WARNING
name|String
name|XSLT_WARNING
init|=
literal|"CamelXsltWarning"
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
comment|/**      * Remove all of the properties associated with the exchange matching a specific pattern      *      * @param pattern pattern of names      * @return boolean whether any properties matched      */
DECL|method|removeProperties (String pattern)
name|boolean
name|removeProperties
parameter_list|(
name|String
name|pattern
parameter_list|)
function_decl|;
comment|/**      * Removes the properties from this exchange that match the given<tt>pattern</tt>,       * except for the ones matching one ore more<tt>excludePatterns</tt>      *       * @param pattern pattern of names that should be removed      * @param excludePatterns one or more pattern of properties names that should be excluded (= preserved)      * @return boolean whether any properties matched      */
DECL|method|removeProperties (String pattern, String... excludePatterns)
name|boolean
name|removeProperties
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
modifier|...
name|excludePatterns
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
comment|/**      * Returns the outbound message, lazily creating one if one has not already      * been associated with this exchange.      *<p/>      *<br/><b>Important:</b> If you want to change the current message, then use {@link #getIn()} instead as it will      * ensure headers etc. is kept and propagated when routing continues. Bottom line end users should rarely use      * this method.      *<p/>      *<br/>If you want to test whether an OUT message have been set or not, use the {@link #hasOut()} method.      *<p/>      * See also the class java doc for this {@link Exchange} for more details and this      *<a href="http://camel.apache.org/using-getin-or-getout-methods-on-exchange.html">FAQ entry</a>.      *      * @return the response      * @see #getIn()      */
DECL|method|getOut ()
name|Message
name|getOut
parameter_list|()
function_decl|;
comment|/**      * Returns the outbound request message as the given type      *<p/>      *<br/><b>Important:</b> If you want to change the current message, then use {@link #getIn()} instead as it will      * ensure headers etc. is kept and propagated when routing continues. Bottom line end users should rarely use      * this method.      *<p/>      *<br/>If you want to test whether an OUT message have been set or not, use the {@link #hasOut()} method.      *<p/>      * See also the class java doc for this {@link Exchange} for more details and this      *<a href="http://camel.apache.org/using-getin-or-getout-methods-on-exchange.html">FAQ entry</a>.      *      * @param type the given type      * @return the message as the given type or<tt>null</tt> if not possible to covert to given type      * @see #getIn(Class)      */
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
comment|/**      * Sets the exception associated with this exchange      *<p/>      * Camel will wrap {@link Throwable} into {@link Exception} type to      * accommodate for the {@link #getException()} method returning a plain {@link Exception} type.      *      * @param t  the caused exception      */
DECL|method|setException (Throwable t)
name|void
name|setException
parameter_list|(
name|Throwable
name|t
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
comment|/**      * Returns true if this exchange is an external initiated redelivered message (such as a JMS broker).      *<p/>      *<b>Important:</b> It is not always possible to determine if the message is a redelivery      * or not, and therefore<tt>null</tt> is returned. Such an example would be a JDBC message.      * However JMS brokers provides details if a message is redelivered.      *      * @return<tt>true</tt> if redelivered,<tt>false</tt> if not,<tt>null</tt> if not able to determine      */
DECL|method|isExternalRedelivered ()
name|Boolean
name|isExternalRedelivered
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
comment|/**      * Creates a copy of the current message exchange so that it can be      * forwarded to another destination      *<p/>      * Notice this operation invokes<tt>copy(false)</tt>      */
DECL|method|copy ()
name|Exchange
name|copy
parameter_list|()
function_decl|;
comment|/**      * Creates a copy of the current message exchange so that it can be      * forwarded to another destination      *      * @param safeCopy whether to copy exchange properties and message headers safely to a new map instance,      *                 or allow sharing the same map instances in the returned copy.      */
DECL|method|copy (boolean safeCopy)
name|Exchange
name|copy
parameter_list|(
name|boolean
name|safeCopy
parameter_list|)
function_decl|;
comment|/**      * Returns the endpoint which originated this message exchange if a consumer on an endpoint      * created the message exchange, otherwise this property will be<tt>null</tt>      */
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
comment|/**      * Returns the route id which originated this message exchange if a route consumer on an endpoint      * created the message exchange, otherwise this property will be<tt>null</tt>      */
DECL|method|getFromRouteId ()
name|String
name|getFromRouteId
parameter_list|()
function_decl|;
comment|/**      * Sets the route id which originated this message exchange. This method      * should typically only be called by the internal framework.      *      * @param fromRouteId the from route id      */
DECL|method|setFromRouteId (String fromRouteId)
name|void
name|setFromRouteId
parameter_list|(
name|String
name|fromRouteId
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
comment|/**      * Checks if the passed {@link org.apache.camel.spi.Synchronization} instance is      * already contained on this exchange.      *      * @param onCompletion  the callback instance that is being checked for      * @return<tt>true</tt>, if callback instance is already contained on this exchange, else<tt>false</tt>      */
DECL|method|containsOnCompletion (Synchronization onCompletion)
name|boolean
name|containsOnCompletion
parameter_list|(
name|Synchronization
name|onCompletion
parameter_list|)
function_decl|;
comment|/**      * Handover all the on completions from this exchange to the target exchange.      *      * @param target the target exchange      */
DECL|method|handoverCompletions (Exchange target)
name|void
name|handoverCompletions
parameter_list|(
name|Exchange
name|target
parameter_list|)
function_decl|;
comment|/**      * Handover all the on completions from this exchange      *      * @return the on completions      */
DECL|method|handoverCompletions ()
name|List
argument_list|<
name|Synchronization
argument_list|>
name|handoverCompletions
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

