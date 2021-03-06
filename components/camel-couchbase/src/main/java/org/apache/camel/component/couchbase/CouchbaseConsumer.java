begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchbase
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
package|;
end_package

begin_import
import|import
name|com
operator|.
name|couchbase
operator|.
name|client
operator|.
name|CouchbaseClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|couchbase
operator|.
name|client
operator|.
name|protocol
operator|.
name|views
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|com
operator|.
name|couchbase
operator|.
name|client
operator|.
name|protocol
operator|.
name|views
operator|.
name|View
import|;
end_import

begin_import
import|import
name|com
operator|.
name|couchbase
operator|.
name|client
operator|.
name|protocol
operator|.
name|views
operator|.
name|ViewResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|couchbase
operator|.
name|client
operator|.
name|protocol
operator|.
name|views
operator|.
name|ViewRow
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
name|support
operator|.
name|DefaultScheduledPollConsumer
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
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|HEADER_DESIGN_DOCUMENT_NAME
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
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|HEADER_ID
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
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|HEADER_KEY
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
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|HEADER_VIEWNAME
import|;
end_import

begin_class
DECL|class|CouchbaseConsumer
specifier|public
class|class
name|CouchbaseConsumer
extends|extends
name|DefaultScheduledPollConsumer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|CouchbaseEndpoint
name|endpoint
decl_stmt|;
DECL|field|client
specifier|private
specifier|final
name|CouchbaseClient
name|client
decl_stmt|;
DECL|field|view
specifier|private
specifier|final
name|View
name|view
decl_stmt|;
DECL|field|query
specifier|private
specifier|final
name|Query
name|query
decl_stmt|;
DECL|method|CouchbaseConsumer (CouchbaseEndpoint endpoint, CouchbaseClient client, Processor processor)
specifier|public
name|CouchbaseConsumer
parameter_list|(
name|CouchbaseEndpoint
name|endpoint
parameter_list|,
name|CouchbaseClient
name|client
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|view
operator|=
name|client
operator|.
name|getView
argument_list|(
name|endpoint
operator|.
name|getDesignDocumentName
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getViewName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|query
operator|=
operator|new
name|Query
argument_list|()
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doInit ()
specifier|protected
name|void
name|doInit
parameter_list|()
block|{
name|query
operator|.
name|setIncludeDocs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|int
name|limit
init|=
name|endpoint
operator|.
name|getLimit
argument_list|()
decl_stmt|;
if|if
condition|(
name|limit
operator|>
literal|0
condition|)
block|{
name|query
operator|.
name|setLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
name|int
name|skip
init|=
name|endpoint
operator|.
name|getSkip
argument_list|()
decl_stmt|;
if|if
condition|(
name|skip
operator|>
literal|0
condition|)
block|{
name|query
operator|.
name|setSkip
argument_list|(
name|skip
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|setDescending
argument_list|(
name|endpoint
operator|.
name|isDescending
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|rangeStartKey
init|=
name|endpoint
operator|.
name|getRangeStartKey
argument_list|()
decl_stmt|;
name|String
name|rangeEndKey
init|=
name|endpoint
operator|.
name|getRangeEndKey
argument_list|()
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|rangeStartKey
argument_list|)
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|rangeEndKey
argument_list|)
condition|)
block|{
return|return;
block|}
name|query
operator|.
name|setRange
argument_list|(
name|rangeStartKey
argument_list|,
name|rangeEndKey
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Starting Couchbase consumer"
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Stopping Couchbase consumer"
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
specifier|synchronized
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|ViewResponse
name|result
init|=
name|client
operator|.
name|query
argument_list|(
name|view
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Received result set from Couchbase"
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"ViewResponse = {}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
name|String
name|consumerProcessedStrategy
init|=
name|endpoint
operator|.
name|getConsumerProcessedStrategy
argument_list|()
decl_stmt|;
for|for
control|(
name|ViewRow
name|row
range|:
name|result
control|)
block|{
name|String
name|id
init|=
name|row
operator|.
name|getId
argument_list|()
decl_stmt|;
name|Object
name|doc
init|=
name|row
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|row
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|designDocumentName
init|=
name|endpoint
operator|.
name|getDesignDocumentName
argument_list|()
decl_stmt|;
name|String
name|viewName
init|=
name|endpoint
operator|.
name|getViewName
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_DESIGN_DOCUMENT_NAME
argument_list|,
name|designDocumentName
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_VIEWNAME
argument_list|,
name|viewName
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"delete"
operator|.
name|equalsIgnoreCase
argument_list|(
name|consumerProcessedStrategy
argument_list|)
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Deleting doc with ID {}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|delete
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"filter"
operator|.
name|equalsIgnoreCase
argument_list|(
name|consumerProcessedStrategy
argument_list|)
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Filtering out ID {}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
comment|// add filter for already processed docs
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"No strategy set for already processed docs, beware of duplicates!"
argument_list|)
expr_stmt|;
block|}
name|logDetails
argument_list|(
name|id
argument_list|,
name|doc
argument_list|,
name|key
argument_list|,
name|designDocumentName
argument_list|,
name|viewName
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|logDetails (String id, Object doc, String key, String designDocumentName, String viewName, Exchange exchange)
specifier|private
name|void
name|logDetails
parameter_list|(
name|String
name|id
parameter_list|,
name|Object
name|doc
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|designDocumentName
parameter_list|,
name|String
name|viewName
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Created exchange = {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Added Document in body = {}"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Adding to Header"
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"ID = {}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Key = {}"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Design Document Name = {}"
argument_list|,
name|designDocumentName
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"View Name = {}"
argument_list|,
name|viewName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

