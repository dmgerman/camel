begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.javaspace
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|javaspace
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledThreadPoolExecutor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|entry
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|lease
operator|.
name|Lease
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|transaction
operator|.
name|CannotAbortException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|transaction
operator|.
name|CannotCommitException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|transaction
operator|.
name|Transaction
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|transaction
operator|.
name|UnknownTransactionException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|space
operator|.
name|JavaSpace
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
name|RuntimeCamelException
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
name|DefaultConsumer
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
name|DefaultExchange
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
name|ExchangeHelper
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
comment|/**  * @{link Consumer} implementation for Javaspaces  *   * @version   */
end_comment

begin_class
DECL|class|JavaSpaceConsumer
specifier|public
class|class
name|JavaSpaceConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|READ
specifier|public
specifier|static
specifier|final
name|int
name|READ
init|=
literal|1
decl_stmt|;
DECL|field|TAKE
specifier|public
specifier|static
specifier|final
name|int
name|TAKE
init|=
literal|0
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
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
name|JavaSpaceConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|concurrentConsumers
specifier|private
specifier|final
name|int
name|concurrentConsumers
decl_stmt|;
DECL|field|transactional
specifier|private
specifier|final
name|boolean
name|transactional
decl_stmt|;
DECL|field|transactionTimeout
specifier|private
specifier|final
name|long
name|transactionTimeout
decl_stmt|;
DECL|field|verb
specifier|private
specifier|final
name|String
name|verb
decl_stmt|;
DECL|field|templateId
specifier|private
specifier|final
name|String
name|templateId
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ScheduledThreadPoolExecutor
name|executor
decl_stmt|;
DECL|field|javaSpace
specifier|private
name|JavaSpace
name|javaSpace
decl_stmt|;
DECL|field|transactionHelper
specifier|private
name|TransactionHelper
name|transactionHelper
decl_stmt|;
DECL|method|JavaSpaceConsumer (final JavaSpaceEndpoint endpoint, Processor processor)
specifier|public
name|JavaSpaceConsumer
parameter_list|(
specifier|final
name|JavaSpaceEndpoint
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
name|this
operator|.
name|concurrentConsumers
operator|=
name|endpoint
operator|.
name|getConcurrentConsumers
argument_list|()
expr_stmt|;
name|this
operator|.
name|transactional
operator|=
name|endpoint
operator|.
name|isTransactional
argument_list|()
expr_stmt|;
name|this
operator|.
name|transactionTimeout
operator|=
name|endpoint
operator|.
name|getTransactionTimeout
argument_list|()
expr_stmt|;
name|this
operator|.
name|verb
operator|=
name|endpoint
operator|.
name|getVerb
argument_list|()
expr_stmt|;
name|this
operator|.
name|templateId
operator|=
name|endpoint
operator|.
name|getTemplateId
argument_list|()
expr_stmt|;
name|this
operator|.
name|executor
operator|=
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
name|this
operator|.
name|concurrentConsumers
argument_list|)
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: There should be a switch to enable/disable using this security hack
name|Utility
operator|.
name|setSecurityPolicy
argument_list|(
literal|"policy.all"
argument_list|,
literal|"policy_consumer.all"
argument_list|)
expr_stmt|;
name|int
name|verb
init|=
name|TAKE
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|verb
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"read"
argument_list|)
condition|)
block|{
name|verb
operator|=
name|READ
expr_stmt|;
block|}
name|javaSpace
operator|=
name|JiniSpaceAccessor
operator|.
name|findSpace
argument_list|(
operator|(
operator|(
name|JavaSpaceEndpoint
operator|)
name|this
operator|.
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getUrl
argument_list|()
argument_list|,
operator|(
operator|(
name|JavaSpaceEndpoint
operator|)
name|this
operator|.
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getSpaceName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|transactional
condition|)
block|{
name|transactionHelper
operator|=
name|TransactionHelper
operator|.
name|getInstance
argument_list|(
operator|(
operator|(
name|JavaSpaceEndpoint
operator|)
name|this
operator|.
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|concurrentConsumers
condition|;
operator|++
name|i
control|)
block|{
name|Task
name|worker
init|=
operator|new
name|Task
argument_list|(
operator|(
name|JavaSpaceEndpoint
operator|)
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|this
operator|.
name|getProcessor
argument_list|()
argument_list|,
name|javaSpace
argument_list|,
name|transactionHelper
argument_list|,
name|transactionTimeout
argument_list|,
name|verb
argument_list|,
name|templateId
argument_list|)
decl_stmt|;
name|executor
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|worker
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
expr_stmt|;
block|}
operator|(
operator|new
name|File
argument_list|(
literal|"policy_consumer.all"
argument_list|)
operator|)
operator|.
name|delete
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
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

begin_class
DECL|class|Task
class|class
name|Task
implements|implements
name|Runnable
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|JavaSpaceEndpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|javaSpace
specifier|private
specifier|final
name|JavaSpace
name|javaSpace
decl_stmt|;
DECL|field|transactionHelper
specifier|private
specifier|final
name|TransactionHelper
name|transactionHelper
decl_stmt|;
DECL|field|transactionTimeout
specifier|private
specifier|final
name|long
name|transactionTimeout
decl_stmt|;
DECL|field|verb
specifier|private
specifier|final
name|int
name|verb
decl_stmt|;
DECL|field|template
specifier|private
specifier|final
name|Entry
name|template
decl_stmt|;
DECL|method|Task (JavaSpaceEndpoint endpoint, Processor processor, JavaSpace javaSpace, TransactionHelper transactionHelper, long transactionTimeout, int verb, String templateId)
specifier|public
name|Task
parameter_list|(
name|JavaSpaceEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|JavaSpace
name|javaSpace
parameter_list|,
name|TransactionHelper
name|transactionHelper
parameter_list|,
name|long
name|transactionTimeout
parameter_list|,
name|int
name|verb
parameter_list|,
name|String
name|templateId
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|javaSpace
operator|=
name|javaSpace
expr_stmt|;
name|this
operator|.
name|transactionHelper
operator|=
name|transactionHelper
expr_stmt|;
name|this
operator|.
name|transactionTimeout
operator|=
name|transactionTimeout
expr_stmt|;
name|this
operator|.
name|verb
operator|=
name|verb
expr_stmt|;
if|if
condition|(
name|templateId
operator|!=
literal|null
condition|)
block|{
name|Entry
name|tmpl
init|=
operator|(
name|Entry
operator|)
name|this
operator|.
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|templateId
argument_list|)
decl_stmt|;
name|template
operator|=
name|javaSpace
operator|.
name|snapshot
argument_list|(
name|tmpl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|template
operator|=
name|javaSpace
operator|.
name|snapshot
argument_list|(
operator|new
name|InEntry
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|Transaction
name|tnx
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DefaultExchange
name|exchange
init|=
operator|(
name|DefaultExchange
operator|)
name|endpoint
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|transactionHelper
operator|!=
literal|null
condition|)
block|{
name|tnx
operator|=
name|transactionHelper
operator|.
name|getJiniTransaction
argument_list|(
name|transactionTimeout
argument_list|)
operator|.
name|transaction
expr_stmt|;
block|}
name|Entry
name|entry
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|verb
condition|)
block|{
case|case
name|JavaSpaceConsumer
operator|.
name|TAKE
case|:
name|entry
operator|=
name|javaSpace
operator|.
name|take
argument_list|(
name|template
argument_list|,
name|tnx
argument_list|,
literal|100
argument_list|)
expr_stmt|;
break|break;
case|case
name|JavaSpaceConsumer
operator|.
name|READ
case|:
name|entry
operator|=
name|javaSpace
operator|.
name|read
argument_list|(
name|template
argument_list|,
name|tnx
argument_list|,
literal|100
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Wrong verb"
argument_list|)
throw|;
block|}
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|entry
operator|instanceof
name|InEntry
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|InEntry
operator|)
name|entry
operator|)
operator|.
name|binary
condition|)
block|{
name|message
operator|.
name|setBody
argument_list|(
operator|(
operator|(
name|InEntry
operator|)
name|entry
operator|)
operator|.
name|buffer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
operator|(
name|InEntry
operator|)
name|entry
operator|)
operator|.
name|buffer
argument_list|)
decl_stmt|;
name|ObjectInputStream
name|ois
init|=
operator|new
name|ObjectInputStream
argument_list|(
name|bis
argument_list|)
decl_stmt|;
name|Object
name|obj
init|=
name|ois
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
if|if
condition|(
name|out
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
operator|&&
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|OutEntry
name|replyCamelEntry
init|=
operator|new
name|OutEntry
argument_list|()
decl_stmt|;
name|replyCamelEntry
operator|.
name|correlationId
operator|=
operator|(
operator|(
name|InEntry
operator|)
name|entry
operator|)
operator|.
name|correlationId
expr_stmt|;
if|if
condition|(
name|out
operator|.
name|getBody
argument_list|()
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|replyCamelEntry
operator|.
name|binary
operator|=
literal|true
expr_stmt|;
name|replyCamelEntry
operator|.
name|buffer
operator|=
operator|(
name|byte
index|[]
operator|)
name|out
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ObjectOutputStream
name|oos
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|bos
argument_list|)
decl_stmt|;
name|oos
operator|.
name|writeObject
argument_list|(
name|out
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|replyCamelEntry
operator|.
name|binary
operator|=
literal|false
expr_stmt|;
name|replyCamelEntry
operator|.
name|buffer
operator|=
name|bos
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
name|javaSpace
operator|.
name|write
argument_list|(
name|replyCamelEntry
argument_list|,
name|tnx
argument_list|,
name|Lease
operator|.
name|FOREVER
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|message
operator|.
name|setBody
argument_list|(
name|entry
argument_list|,
name|Entry
operator|.
name|class
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|tnx
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|tnx
operator|.
name|abort
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownTransactionException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e1
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|CannotAbortException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e1
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RemoteException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e1
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|tnx
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|tnx
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownTransactionException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e1
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RemoteException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e1
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|CannotCommitException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e1
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

