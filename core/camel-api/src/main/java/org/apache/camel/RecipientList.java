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
name|lang
operator|.
name|annotation
operator|.
name|Documented
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * Indicates that this method is to be used as a   *<a href="http://camel.apache.org/recipient-list.html">Dynamic Recipient List</a> routing the incoming message  * to one or more endpoints.  *  * When a message {@link org.apache.camel.Exchange} is received from an {@link org.apache.camel.Endpoint} then the  *<a href="http://camel.apache.org/bean-integration.html">Bean Integration</a>  * mechanism is used to map the incoming {@link org.apache.camel.Message} to the method parameters.  *  * The return value of the method is then converted to either a {@link java.util.Collection} or array of objects where each  * element is converted to an {@link Endpoint} or a {@link String}, or if it is not a collection/array then it is converted  * to an {@link Endpoint} or {@link String}.  *  * Then for each endpoint or URI the message is forwarded a separate copy.  */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Documented
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|FIELD
block|,
name|ElementType
operator|.
name|METHOD
block|,
name|ElementType
operator|.
name|CONSTRUCTOR
block|}
argument_list|)
DECL|annotation|RecipientList
specifier|public
annotation_defn|@interface
name|RecipientList
block|{
comment|/**      * Id of {@link CamelContext} to use      */
DECL|method|context ()
name|String
name|context
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Delimiter used if the Expression returned multiple endpoints. Can be turned off using the value<tt>false</tt>.      *<p/>      * The default value is ,      */
DECL|method|delimiter ()
name|String
name|delimiter
parameter_list|()
default|default
literal|","
function_decl|;
comment|/**      * If enabled then sending messages to the recipients occurs concurrently.      * Note the caller thread will still wait until all messages has been fully processed, before it continues.      * Its only the sending and processing the replies from the recipients which happens concurrently.      */
DECL|method|parallelProcessing ()
DECL|field|false
name|boolean
name|parallelProcessing
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * If enabled then the aggregate method on AggregationStrategy can be called concurrently.      * Notice that this would require the implementation of AggregationStrategy to be implemented as thread-safe.      * By default this is false meaning that Camel synchronizes the call to the aggregate method.      * Though in some use-cases this can be used to archive higher performance when the AggregationStrategy is implemented as thread-safe.      */
DECL|method|parallelAggregate ()
DECL|field|false
name|boolean
name|parallelAggregate
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Will now stop further processing if an exception or failure occurred during processing of an      * {@link org.apache.camel.Exchange} and the caused exception will be thrown.      *<p/>      * Will also stop if processing the exchange failed (has a fault message) or an exception      * was thrown and handled by the error handler (such as using onException). In all situations      * the recipient list will stop further processing. This is the same behavior as in pipeline, which      * is used by the routing engine.      *<p/>      * The default behavior is to<b>not</b> stop but continue processing till the end      */
DECL|method|stopOnException ()
DECL|field|false
name|boolean
name|stopOnException
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * If enabled, unwind exceptions occurring at aggregation time to the error handler when parallelProcessing is used.      * Currently, aggregation time exceptions do not stop the route processing when parallelProcessing is used.      * Enabling this option allows to work around this behavior.      *      * The default value is<code>false</code> for the sake of backward compatibility.      */
DECL|method|stopOnAggregateException ()
DECL|field|false
name|boolean
name|stopOnAggregateException
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * If enabled then Camel will process replies out-of-order, eg in the order they come back.      * If disabled, Camel will process replies in the same order as defined by the recipient list.      */
DECL|method|streaming ()
DECL|field|false
name|boolean
name|streaming
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Whether to ignore the invalidate endpoint exception when try to create a producer with that endpoint      */
DECL|method|ignoreInvalidEndpoints ()
DECL|field|false
name|boolean
name|ignoreInvalidEndpoints
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Sets a reference to the AggregationStrategy to be used to assemble the replies from the recipients, into a single outgoing message from the RecipientList.      * By default Camel will use the last reply as the outgoing message. You can also use a POJO as the AggregationStrategy      */
DECL|method|strategyRef ()
name|String
name|strategyRef
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Refers to a custom Thread Pool to be used for parallel processing.      * Notice if you set this option, then parallel processing is automatic implied, and you do not have to enable that option as well.      */
DECL|method|executorServiceRef ()
name|String
name|executorServiceRef
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Sets a total timeout specified in millis, when using parallel processing.      * If the Recipient List hasn't been able to send and process all replies within the given timeframe,      * then the timeout triggers and the Recipient List breaks out and continues.      * Notice if you provide a TimeoutAwareAggregationStrategy then the timeout method is invoked before breaking out.      * If the timeout is reached with running tasks still remaining, certain tasks for which it is difficult for Camel      * to shut down in a graceful manner may continue to run. So use this option with a bit of care.      */
DECL|method|timeout ()
name|long
name|timeout
parameter_list|()
default|default
literal|0
function_decl|;
comment|/**      * Uses the {@link Processor} when preparing the {@link org.apache.camel.Exchange} to be send.      * This can be used to deep-clone messages that should be send, or any custom logic needed before      * the exchange is send.      */
DECL|method|onPrepareRef ()
name|String
name|onPrepareRef
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Shares the {@link org.apache.camel.spi.UnitOfWork} with the parent and each of the sub messages.      * Recipient List will by default not share unit of work between the parent exchange and each recipient exchange.      * This means each sub exchange has its own individual unit of work.      */
annotation|@
name|Deprecated
DECL|method|shareUnitOfWork ()
DECL|field|false
name|boolean
name|shareUnitOfWork
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

