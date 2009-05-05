begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ibatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ibatis
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
name|ExchangePattern
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
name|Processor
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
name|ScheduledPollConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  *<pre>  *  Ibatis Camel Component used to read data from a database.  *   *  Example Configuration :  *&lt;route&gt;  *&lt;from uri=&quot;ibatis:selectRecords&quot; /&gt;  *&lt;to uri=&quot;jms:destinationQueue&quot; /&gt;  *&lt;/route&gt;  *   *   *  This also can be configured to treat a table as a logical queue by defining  *  an&quot;onConsume&quot; statement.  *   *  Example Configuration :  *&lt;route&gt;  *&lt;from uri=&quot;ibatis:selectRecords?consumer.onConsume=updateRecord&quot; /&gt;  *&lt;to uri=&quot;jms:destinationQueue&quot; /&gt;  *&lt;/route&gt;  *   *  By default, if the select statement contains multiple rows, it will  *  iterate over the set and deliver each row to the route.  If this is not the  *  desired behavior then set&quot;useIterator=false&quot;.  This will deliver the entire  *  set to the route as a list.  *</pre>  *  *<b>URI Options</b>  *<table border="1">  *<thead>  *<th>Name</th>  *<th>Default Value</th>  *<th>description</th>  *</thead>  *<tbody>  *<tr>  *<td>initialDelay</td>  *<td>1000 ms</td>  *<td>time before polling starts</td>  *</tr>  *<tr>  *<td>delay</td>  *<td>500 ms</td>  *<td>time before the next poll</td>  *</tr>  *<tr>  *<td>timeUnit</td>  *<td>MILLISECONDS</td>  *<td>Time unit to use for delay properties (NANOSECONDS, MICROSECONDS,  * MILLISECONDS, SECONDS)</td>  *</tr>  *<tr>  *<td>useIterator</td>  *<td>true</td>  *<td>If true, processes one exchange per row. If false processes one exchange  * for all rows</td>  *</tr>  *<tr>  *<td>onConsume</td>  *<td>null</td>  *<td>statement to run after data has been processed</td>  *</tr>  *<tbody></table>  *  * @see org.apache.camel.component.ibatis.strategy.IBatisProcessingStrategy  */
end_comment

begin_class
DECL|class|IBatisPollingConsumer
specifier|public
class|class
name|IBatisPollingConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|IBatisPollingConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Statement to run after data has been processed in the route      */
DECL|field|onConsume
specifier|private
name|String
name|onConsume
decl_stmt|;
comment|/**      * Process resultset individually or as a list      */
DECL|field|useIterator
specifier|private
name|boolean
name|useIterator
init|=
literal|true
decl_stmt|;
comment|/**      * Whether allow empty resultset to be routed to the next hop      */
DECL|field|routeEmptyResultSet
specifier|private
name|boolean
name|routeEmptyResultSet
decl_stmt|;
DECL|method|IBatisPollingConsumer (IBatisEndpoint endpoint, Processor processor)
specifier|public
name|IBatisPollingConsumer
parameter_list|(
name|IBatisEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|IBatisEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|IBatisEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Polls the database      */
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|IBatisEndpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Polling: "
operator|+
name|endpoint
argument_list|)
expr_stmt|;
block|}
name|List
name|data
init|=
name|endpoint
operator|.
name|getProcessingStrategy
argument_list|()
operator|.
name|poll
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|useIterator
condition|)
block|{
for|for
control|(
name|Object
name|object
range|:
name|data
control|)
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|process
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|data
operator|.
name|isEmpty
argument_list|()
operator|||
name|routeEmptyResultSet
condition|)
block|{
name|process
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Delivers the content      *      * @param data a single row object if useIterator=true otherwise the entire result set      */
DECL|method|process (final Object data)
specifier|protected
name|void
name|process
parameter_list|(
specifier|final
name|Object
name|data
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|IBatisEndpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|IBatisConstants
operator|.
name|IBATIS_STATEMENT_NAME
argument_list|,
name|endpoint
operator|.
name|getStatement
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Processing exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|onConsume
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getProcessingStrategy
argument_list|()
operator|.
name|commit
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|data
argument_list|,
name|onConsume
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets the statement(s) to run after successful processing.      * Use comma to separate multiple statements.      */
DECL|method|getOnConsume ()
specifier|public
name|String
name|getOnConsume
parameter_list|()
block|{
return|return
name|onConsume
return|;
block|}
comment|/**      * Sets the statement to run after successful processing.      * Use comma to separate multiple statements.      */
DECL|method|setOnConsume (String onConsume)
specifier|public
name|void
name|setOnConsume
parameter_list|(
name|String
name|onConsume
parameter_list|)
block|{
name|this
operator|.
name|onConsume
operator|=
name|onConsume
expr_stmt|;
block|}
comment|/**      * Indicates how resultset should be delivered to the route      */
DECL|method|isUseIterator ()
specifier|public
name|boolean
name|isUseIterator
parameter_list|()
block|{
return|return
name|useIterator
return|;
block|}
comment|/**      * Sets how resultset should be delivered to route.      * Indicates delivery as either a list or individual object.      * defaults to true.      */
DECL|method|setUseIterator (boolean useIterator)
specifier|public
name|void
name|setUseIterator
parameter_list|(
name|boolean
name|useIterator
parameter_list|)
block|{
name|this
operator|.
name|useIterator
operator|=
name|useIterator
expr_stmt|;
block|}
comment|/**      * Indicates whether empty resultset should be allowed to be sent to the next hop or not      */
DECL|method|isRouteEmptyResultSet ()
specifier|public
name|boolean
name|isRouteEmptyResultSet
parameter_list|()
block|{
return|return
name|routeEmptyResultSet
return|;
block|}
comment|/**      * Sets whether empty resultset should be allowed to be sent to the next hop.      * defaults to false. So the empty resultset will be filtered out.      */
DECL|method|setRouteEmptyResultSet (boolean routeEmptyResultSet)
specifier|public
name|void
name|setRouteEmptyResultSet
parameter_list|(
name|boolean
name|routeEmptyResultSet
parameter_list|)
block|{
name|this
operator|.
name|routeEmptyResultSet
operator|=
name|routeEmptyResultSet
expr_stmt|;
block|}
block|}
end_class

end_unit

