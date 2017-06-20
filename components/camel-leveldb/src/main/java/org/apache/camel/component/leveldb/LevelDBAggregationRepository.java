begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.leveldb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|leveldb
package|;
end_package

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
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|concurrent
operator|.
name|TimeUnit
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
name|RecoverableAggregationRepository
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
name|ServiceSupport
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
name|IOHelper
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtbuf
operator|.
name|Buffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|iq80
operator|.
name|leveldb
operator|.
name|DBIterator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|iq80
operator|.
name|leveldb
operator|.
name|WriteBatch
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
comment|/**  * An instance of {@link org.apache.camel.spi.AggregationRepository} which is backed by a {@link LevelDBFile}.  */
end_comment

begin_class
DECL|class|LevelDBAggregationRepository
specifier|public
class|class
name|LevelDBAggregationRepository
extends|extends
name|ServiceSupport
implements|implements
name|RecoverableAggregationRepository
block|{
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
name|LevelDBAggregationRepository
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|levelDBFile
specifier|private
name|LevelDBFile
name|levelDBFile
decl_stmt|;
DECL|field|persistentFileName
specifier|private
name|String
name|persistentFileName
decl_stmt|;
DECL|field|repositoryName
specifier|private
name|String
name|repositoryName
decl_stmt|;
DECL|field|sync
specifier|private
name|boolean
name|sync
decl_stmt|;
DECL|field|returnOldExchange
specifier|private
name|boolean
name|returnOldExchange
decl_stmt|;
DECL|field|codec
specifier|private
name|LevelDBCamelCodec
name|codec
init|=
operator|new
name|LevelDBCamelCodec
argument_list|()
decl_stmt|;
DECL|field|recoveryInterval
specifier|private
name|long
name|recoveryInterval
init|=
literal|5000
decl_stmt|;
DECL|field|useRecovery
specifier|private
name|boolean
name|useRecovery
init|=
literal|true
decl_stmt|;
DECL|field|maximumRedeliveries
specifier|private
name|int
name|maximumRedeliveries
decl_stmt|;
DECL|field|deadLetterUri
specifier|private
name|String
name|deadLetterUri
decl_stmt|;
DECL|field|allowSerializedHeaders
specifier|private
name|boolean
name|allowSerializedHeaders
decl_stmt|;
comment|/**      * Creates an aggregation repository      */
DECL|method|LevelDBAggregationRepository ()
specifier|public
name|LevelDBAggregationRepository
parameter_list|()
block|{     }
comment|/**      * Creates an aggregation repository      *      * @param repositoryName the repository name      */
DECL|method|LevelDBAggregationRepository (String repositoryName)
specifier|public
name|LevelDBAggregationRepository
parameter_list|(
name|String
name|repositoryName
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|repositoryName
argument_list|,
literal|"repositoryName"
argument_list|)
expr_stmt|;
name|this
operator|.
name|repositoryName
operator|=
name|repositoryName
expr_stmt|;
block|}
comment|/**      * Creates an aggregation repository using a new {@link LevelDBFile}      * that persists using the provided file.      *      * @param repositoryName     the repository name      * @param persistentFileName the persistent store filename      */
DECL|method|LevelDBAggregationRepository (String repositoryName, String persistentFileName)
specifier|public
name|LevelDBAggregationRepository
parameter_list|(
name|String
name|repositoryName
parameter_list|,
name|String
name|persistentFileName
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|repositoryName
argument_list|,
literal|"repositoryName"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|persistentFileName
argument_list|,
literal|"persistentFileName"
argument_list|)
expr_stmt|;
name|this
operator|.
name|repositoryName
operator|=
name|repositoryName
expr_stmt|;
name|this
operator|.
name|persistentFileName
operator|=
name|persistentFileName
expr_stmt|;
block|}
comment|/**      * Creates an aggregation repository using the provided {@link LevelDBFile}.      *      * @param repositoryName the repository name      * @param levelDBFile    the leveldb file to use as persistent store      */
DECL|method|LevelDBAggregationRepository (String repositoryName, LevelDBFile levelDBFile)
specifier|public
name|LevelDBAggregationRepository
parameter_list|(
name|String
name|repositoryName
parameter_list|,
name|LevelDBFile
name|levelDBFile
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|repositoryName
argument_list|,
literal|"repositoryName"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|levelDBFile
argument_list|,
literal|"levelDBFile"
argument_list|)
expr_stmt|;
name|this
operator|.
name|levelDBFile
operator|=
name|levelDBFile
expr_stmt|;
name|this
operator|.
name|repositoryName
operator|=
name|repositoryName
expr_stmt|;
block|}
DECL|method|add (final CamelContext camelContext, final String key, final Exchange exchange)
specifier|public
name|Exchange
name|add
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|String
name|key
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding key [{}] -> {}"
argument_list|,
name|key
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
name|byte
index|[]
name|lDbKey
init|=
name|keyBuilder
argument_list|(
name|repositoryName
argument_list|,
name|key
argument_list|)
decl_stmt|;
specifier|final
name|Buffer
name|exchangeBuffer
init|=
name|codec
operator|.
name|marshallExchange
argument_list|(
name|camelContext
argument_list|,
name|exchange
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
name|byte
index|[]
name|rc
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isReturnOldExchange
argument_list|()
condition|)
block|{
name|rc
operator|=
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|get
argument_list|(
name|lDbKey
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Adding key index {} for repository {}"
argument_list|,
name|key
argument_list|,
name|repositoryName
argument_list|)
expr_stmt|;
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|put
argument_list|(
name|lDbKey
argument_list|,
name|exchangeBuffer
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|levelDBFile
operator|.
name|getWriteOptions
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Added key index {}"
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|rc
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// only return old exchange if enabled
if|if
condition|(
name|isReturnOldExchange
argument_list|()
condition|)
block|{
return|return
name|codec
operator|.
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
operator|new
name|Buffer
argument_list|(
name|rc
argument_list|)
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error adding to repository "
operator|+
name|repositoryName
operator|+
literal|" with key "
operator|+
name|key
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|get (final CamelContext camelContext, final String key)
specifier|public
name|Exchange
name|get
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|String
name|key
parameter_list|)
block|{
name|Exchange
name|answer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|lDbKey
init|=
name|keyBuilder
argument_list|(
name|repositoryName
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Getting key index {}"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|byte
index|[]
name|rc
init|=
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|get
argument_list|(
name|lDbKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|codec
operator|.
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
operator|new
name|Buffer
argument_list|(
name|rc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error getting key "
operator|+
name|key
operator|+
literal|" from repository "
operator|+
name|repositoryName
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting key  [{}] -> {}"
argument_list|,
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|remove (final CamelContext camelContext, final String key, final Exchange exchange)
specifier|public
name|void
name|remove
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|String
name|key
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Removing key [{}]"
argument_list|,
name|key
argument_list|)
expr_stmt|;
try|try
block|{
name|byte
index|[]
name|lDbKey
init|=
name|keyBuilder
argument_list|(
name|repositoryName
argument_list|,
name|key
argument_list|)
decl_stmt|;
specifier|final
name|String
name|exchangeId
init|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
specifier|final
name|Buffer
name|exchangeBuffer
init|=
name|codec
operator|.
name|marshallExchange
argument_list|(
name|camelContext
argument_list|,
name|exchange
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
comment|// remove the exchange
name|byte
index|[]
name|rc
init|=
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|get
argument_list|(
name|lDbKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
name|WriteBatch
name|batch
init|=
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|createWriteBatch
argument_list|()
decl_stmt|;
try|try
block|{
name|batch
operator|.
name|delete
argument_list|(
name|lDbKey
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Removed key index {} -> {}"
argument_list|,
name|key
argument_list|,
operator|new
name|Buffer
argument_list|(
name|rc
argument_list|)
argument_list|)
expr_stmt|;
comment|// add exchange to confirmed index
name|byte
index|[]
name|confirmedLDBKey
init|=
name|keyBuilder
argument_list|(
name|getRepositoryNameCompleted
argument_list|()
argument_list|,
name|exchangeId
argument_list|)
decl_stmt|;
name|batch
operator|.
name|put
argument_list|(
name|confirmedLDBKey
argument_list|,
name|exchangeBuffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Added confirm index {} for repository {}"
argument_list|,
name|exchangeId
argument_list|,
name|getRepositoryNameCompleted
argument_list|()
argument_list|)
expr_stmt|;
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|write
argument_list|(
name|batch
argument_list|,
name|levelDBFile
operator|.
name|getWriteOptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|batch
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error removing key "
operator|+
name|key
operator|+
literal|" from repository "
operator|+
name|repositoryName
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|confirm (final CamelContext camelContext, final String exchangeId)
specifier|public
name|void
name|confirm
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|String
name|exchangeId
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Confirming exchangeId [{}]"
argument_list|,
name|exchangeId
argument_list|)
expr_stmt|;
name|byte
index|[]
name|confirmedLDBKey
init|=
name|keyBuilder
argument_list|(
name|getRepositoryNameCompleted
argument_list|()
argument_list|,
name|exchangeId
argument_list|)
decl_stmt|;
name|byte
index|[]
name|rc
init|=
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|get
argument_list|(
name|confirmedLDBKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|delete
argument_list|(
name|confirmedLDBKey
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Removed confirm index {} -> {}"
argument_list|,
name|exchangeId
argument_list|,
operator|new
name|Buffer
argument_list|(
name|rc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getKeys ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getKeys
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// interval task could potentially be running while we are shutting down so check for that
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|DBIterator
name|it
init|=
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|keyBuffer
decl_stmt|;
try|try
block|{
name|String
name|prefix
init|=
name|repositoryName
operator|+
literal|'\0'
decl_stmt|;
for|for
control|(
name|it
operator|.
name|seek
argument_list|(
name|keyBuilder
argument_list|(
name|repositoryName
argument_list|,
literal|""
argument_list|)
argument_list|)
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
name|it
operator|.
name|next
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
break|break;
block|}
name|keyBuffer
operator|=
name|asString
argument_list|(
name|it
operator|.
name|peekNext
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|keyBuffer
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
break|break;
block|}
name|String
name|key
init|=
name|keyBuffer
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"getKey [{}]"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// Make sure you close the iterator to avoid resource leaks.
name|IOHelper
operator|.
name|close
argument_list|(
name|it
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|keys
argument_list|)
return|;
block|}
DECL|method|scan (CamelContext camelContext)
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|scan
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|DBIterator
name|it
init|=
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|keyBuffer
decl_stmt|;
try|try
block|{
name|String
name|prefix
init|=
name|getRepositoryNameCompleted
argument_list|()
operator|+
literal|'\0'
decl_stmt|;
for|for
control|(
name|it
operator|.
name|seek
argument_list|(
name|keyBuilder
argument_list|(
name|getRepositoryNameCompleted
argument_list|()
argument_list|,
literal|""
argument_list|)
argument_list|)
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
name|it
operator|.
name|next
argument_list|()
control|)
block|{
name|keyBuffer
operator|=
name|asString
argument_list|(
name|it
operator|.
name|peekNext
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|keyBuffer
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
break|break;
block|}
name|String
name|exchangeId
init|=
name|keyBuffer
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Scan exchangeId [{}]"
argument_list|,
name|exchangeId
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|exchangeId
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// Make sure you close the iterator to avoid resource leaks.
name|IOHelper
operator|.
name|close
argument_list|(
name|it
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Scanned and found no exchange to recover."
argument_list|)
expr_stmt|;
block|}
else|else
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
literal|"Scanned and found {} exchange(s) to recover (note some of them may already be in progress)."
argument_list|,
name|answer
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|recover (CamelContext camelContext, final String exchangeId)
specifier|public
name|Exchange
name|recover
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|String
name|exchangeId
parameter_list|)
block|{
name|Exchange
name|answer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|completedLDBKey
init|=
name|keyBuilder
argument_list|(
name|getRepositoryNameCompleted
argument_list|()
argument_list|,
name|exchangeId
argument_list|)
decl_stmt|;
name|byte
index|[]
name|rc
init|=
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|get
argument_list|(
name|completedLDBKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|codec
operator|.
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
operator|new
name|Buffer
argument_list|(
name|rc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error recovering exchangeId "
operator|+
name|exchangeId
operator|+
literal|" from repository "
operator|+
name|repositoryName
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Recovering exchangeId [{}] -> {}"
argument_list|,
name|exchangeId
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|size (final String repositoryName)
specifier|private
name|int
name|size
parameter_list|(
specifier|final
name|String
name|repositoryName
parameter_list|)
block|{
name|DBIterator
name|it
init|=
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
name|repositoryName
operator|+
literal|'\0'
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
try|try
block|{
for|for
control|(
name|it
operator|.
name|seek
argument_list|(
name|keyBuilder
argument_list|(
name|repositoryName
argument_list|,
literal|""
argument_list|)
argument_list|)
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
name|it
operator|.
name|next
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|asString
argument_list|(
name|it
operator|.
name|peekNext
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
break|break;
block|}
name|count
operator|++
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// Make sure you close the iterator to avoid resource leaks.
name|IOHelper
operator|.
name|close
argument_list|(
name|it
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Size of repository [{}] -> {}"
argument_list|,
name|repositoryName
argument_list|,
name|count
argument_list|)
expr_stmt|;
return|return
name|count
return|;
block|}
DECL|method|getLevelDBFile ()
specifier|public
name|LevelDBFile
name|getLevelDBFile
parameter_list|()
block|{
return|return
name|levelDBFile
return|;
block|}
DECL|method|setLevelDBFile (LevelDBFile levelDBFile)
specifier|public
name|void
name|setLevelDBFile
parameter_list|(
name|LevelDBFile
name|levelDBFile
parameter_list|)
block|{
name|this
operator|.
name|levelDBFile
operator|=
name|levelDBFile
expr_stmt|;
block|}
DECL|method|getRepositoryName ()
specifier|public
name|String
name|getRepositoryName
parameter_list|()
block|{
return|return
name|repositoryName
return|;
block|}
DECL|method|getRepositoryNameCompleted ()
specifier|private
name|String
name|getRepositoryNameCompleted
parameter_list|()
block|{
return|return
name|repositoryName
operator|+
literal|"-completed"
return|;
block|}
DECL|method|setRepositoryName (String repositoryName)
specifier|public
name|void
name|setRepositoryName
parameter_list|(
name|String
name|repositoryName
parameter_list|)
block|{
name|this
operator|.
name|repositoryName
operator|=
name|repositoryName
expr_stmt|;
block|}
DECL|method|isSync ()
specifier|public
name|boolean
name|isSync
parameter_list|()
block|{
return|return
name|sync
return|;
block|}
DECL|method|setSync (boolean sync)
specifier|public
name|void
name|setSync
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|this
operator|.
name|sync
operator|=
name|sync
expr_stmt|;
block|}
DECL|method|isReturnOldExchange ()
specifier|public
name|boolean
name|isReturnOldExchange
parameter_list|()
block|{
return|return
name|returnOldExchange
return|;
block|}
DECL|method|setReturnOldExchange (boolean returnOldExchange)
specifier|public
name|void
name|setReturnOldExchange
parameter_list|(
name|boolean
name|returnOldExchange
parameter_list|)
block|{
name|this
operator|.
name|returnOldExchange
operator|=
name|returnOldExchange
expr_stmt|;
block|}
DECL|method|setRecoveryInterval (long interval, TimeUnit timeUnit)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|interval
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|recoveryInterval
operator|=
name|timeUnit
operator|.
name|toMillis
argument_list|(
name|interval
argument_list|)
expr_stmt|;
block|}
DECL|method|setRecoveryInterval (long interval)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|interval
parameter_list|)
block|{
name|this
operator|.
name|recoveryInterval
operator|=
name|interval
expr_stmt|;
block|}
DECL|method|getRecoveryIntervalInMillis ()
specifier|public
name|long
name|getRecoveryIntervalInMillis
parameter_list|()
block|{
return|return
name|recoveryInterval
return|;
block|}
DECL|method|isUseRecovery ()
specifier|public
name|boolean
name|isUseRecovery
parameter_list|()
block|{
return|return
name|useRecovery
return|;
block|}
DECL|method|setUseRecovery (boolean useRecovery)
specifier|public
name|void
name|setUseRecovery
parameter_list|(
name|boolean
name|useRecovery
parameter_list|)
block|{
name|this
operator|.
name|useRecovery
operator|=
name|useRecovery
expr_stmt|;
block|}
DECL|method|getMaximumRedeliveries ()
specifier|public
name|int
name|getMaximumRedeliveries
parameter_list|()
block|{
return|return
name|maximumRedeliveries
return|;
block|}
DECL|method|setMaximumRedeliveries (int maximumRedeliveries)
specifier|public
name|void
name|setMaximumRedeliveries
parameter_list|(
name|int
name|maximumRedeliveries
parameter_list|)
block|{
name|this
operator|.
name|maximumRedeliveries
operator|=
name|maximumRedeliveries
expr_stmt|;
block|}
DECL|method|getDeadLetterUri ()
specifier|public
name|String
name|getDeadLetterUri
parameter_list|()
block|{
return|return
name|deadLetterUri
return|;
block|}
DECL|method|setDeadLetterUri (String deadLetterUri)
specifier|public
name|void
name|setDeadLetterUri
parameter_list|(
name|String
name|deadLetterUri
parameter_list|)
block|{
name|this
operator|.
name|deadLetterUri
operator|=
name|deadLetterUri
expr_stmt|;
block|}
DECL|method|getPersistentFileName ()
specifier|public
name|String
name|getPersistentFileName
parameter_list|()
block|{
return|return
name|persistentFileName
return|;
block|}
DECL|method|setPersistentFileName (String persistentFileName)
specifier|public
name|void
name|setPersistentFileName
parameter_list|(
name|String
name|persistentFileName
parameter_list|)
block|{
name|this
operator|.
name|persistentFileName
operator|=
name|persistentFileName
expr_stmt|;
block|}
DECL|method|isAllowSerializedHeaders ()
specifier|public
name|boolean
name|isAllowSerializedHeaders
parameter_list|()
block|{
return|return
name|allowSerializedHeaders
return|;
block|}
DECL|method|setAllowSerializedHeaders (boolean allowSerializedHeaders)
specifier|public
name|void
name|setAllowSerializedHeaders
parameter_list|(
name|boolean
name|allowSerializedHeaders
parameter_list|)
block|{
name|this
operator|.
name|allowSerializedHeaders
operator|=
name|allowSerializedHeaders
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
comment|// either we have a LevelDB configured or we use a provided fileName
if|if
condition|(
name|levelDBFile
operator|==
literal|null
operator|&&
name|persistentFileName
operator|!=
literal|null
condition|)
block|{
name|levelDBFile
operator|=
operator|new
name|LevelDBFile
argument_list|()
expr_stmt|;
name|levelDBFile
operator|.
name|setSync
argument_list|(
name|isSync
argument_list|()
argument_list|)
expr_stmt|;
name|levelDBFile
operator|.
name|setFileName
argument_list|(
name|persistentFileName
argument_list|)
expr_stmt|;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|levelDBFile
argument_list|,
literal|"Either set a persistentFileName or a levelDBFile"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|repositoryName
argument_list|,
literal|"repositoryName"
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|levelDBFile
argument_list|)
expr_stmt|;
comment|// log number of existing exchanges
name|int
name|current
init|=
name|size
argument_list|(
name|getRepositoryName
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|completed
init|=
name|size
argument_list|(
name|getRepositoryNameCompleted
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"On startup there are "
operator|+
name|current
operator|+
literal|" aggregate exchanges (not completed) in repository: "
operator|+
name|getRepositoryName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"On startup there are no existing aggregate exchanges (not completed) in repository: "
operator|+
name|getRepositoryName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|completed
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"On startup there are "
operator|+
name|completed
operator|+
literal|" completed exchanges to be recovered in repository: "
operator|+
name|getRepositoryNameCompleted
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"On startup there are no completed exchanges to be recovered in repository: "
operator|+
name|getRepositoryNameCompleted
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|levelDBFile
argument_list|)
expr_stmt|;
block|}
DECL|method|keyBuilder (String repo, String key)
specifier|public
specifier|static
name|byte
index|[]
name|keyBuilder
parameter_list|(
name|String
name|repo
parameter_list|,
name|String
name|key
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|repo
operator|+
literal|'\0'
operator|+
name|key
operator|)
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|asString (byte[] value)
specifier|public
specifier|static
name|String
name|asString
parameter_list|(
name|byte
index|[]
name|value
parameter_list|)
block|{
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
else|else
block|{
try|try
block|{
return|return
operator|new
name|String
argument_list|(
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|var2
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|var2
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

