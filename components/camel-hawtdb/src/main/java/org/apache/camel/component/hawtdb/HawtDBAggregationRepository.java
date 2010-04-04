begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hawtdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hawtdb
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|impl
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
name|spi
operator|.
name|AggregationRepository
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

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdb
operator|.
name|api
operator|.
name|Index
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdb
operator|.
name|api
operator|.
name|Transaction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdb
operator|.
name|util
operator|.
name|buffer
operator|.
name|Buffer
import|;
end_import

begin_comment
comment|/**  * An instance of AggregationRepository which is backed by a HawtDB.  */
end_comment

begin_class
DECL|class|HawtDBAggregationRepository
specifier|public
class|class
name|HawtDBAggregationRepository
parameter_list|<
name|K
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|AggregationRepository
argument_list|<
name|K
argument_list|>
implements|,
name|RecoverableAggregationRepository
argument_list|<
name|K
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|HawtDBAggregationRepository
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|hawtDBFile
specifier|private
name|HawtDBFile
name|hawtDBFile
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
DECL|field|bufferSize
specifier|private
name|Integer
name|bufferSize
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
DECL|field|marshaller
specifier|private
name|HawtDBCamelMarshaller
argument_list|<
name|K
argument_list|>
name|marshaller
init|=
operator|new
name|HawtDBCamelMarshaller
argument_list|<
name|K
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|interval
specifier|private
name|long
name|interval
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
comment|/**      * Creates an aggregation repository      */
DECL|method|HawtDBAggregationRepository ()
specifier|public
name|HawtDBAggregationRepository
parameter_list|()
block|{     }
comment|/**      * Creates an aggregation repository      *      * @param repositoryName the repository name      */
DECL|method|HawtDBAggregationRepository (String repositoryName)
specifier|public
name|HawtDBAggregationRepository
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
comment|/**      * Creates an aggregation repository using a new {@link org.apache.camel.component.hawtdb.HawtDBFile}      * that persists using the provided file.      *      * @param repositoryName the repository name      * @param persistentFileName the persistent store filename      */
DECL|method|HawtDBAggregationRepository (String repositoryName, String persistentFileName)
specifier|public
name|HawtDBAggregationRepository
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
comment|/**      * Creates an aggregation repository using the provided {@link org.apache.camel.component.hawtdb.HawtDBFile}.      *      * @param repositoryName the repository name      * @param hawtDBFile the hawtdb file to use as persistent store      */
DECL|method|HawtDBAggregationRepository (String repositoryName, HawtDBFile hawtDBFile)
specifier|public
name|HawtDBAggregationRepository
parameter_list|(
name|String
name|repositoryName
parameter_list|,
name|HawtDBFile
name|hawtDBFile
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
name|hawtDBFile
argument_list|,
literal|"hawtDBFile"
argument_list|)
expr_stmt|;
name|this
operator|.
name|hawtDBFile
operator|=
name|hawtDBFile
expr_stmt|;
name|this
operator|.
name|repositoryName
operator|=
name|repositoryName
expr_stmt|;
block|}
DECL|method|add (final CamelContext camelContext, final K key, final Exchange exchange)
specifier|public
name|Exchange
name|add
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|K
name|key
parameter_list|,
specifier|final
name|Exchange
name|exchange
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
literal|"Adding key   ["
operator|+
name|key
operator|+
literal|"] -> "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// If we could guarantee that the key and exchange are immutable,
comment|// then we could have stuck them directly into the index,
comment|// HawtDB could then eliminate the need to marshal and un-marshal
comment|// in some cases.  But since we can.. we are going to force
comment|// early marshaling.
specifier|final
name|Buffer
name|keyBuffer
init|=
name|marshaller
operator|.
name|marshallKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
specifier|final
name|Buffer
name|exchangeBuffer
init|=
name|marshaller
operator|.
name|marshallExchange
argument_list|(
name|camelContext
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|Buffer
name|rc
init|=
name|hawtDBFile
operator|.
name|execute
argument_list|(
operator|new
name|Work
argument_list|<
name|Buffer
argument_list|>
argument_list|()
block|{
specifier|public
name|Buffer
name|execute
parameter_list|(
name|Transaction
name|tx
parameter_list|)
block|{
name|Index
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|index
init|=
name|hawtDBFile
operator|.
name|getRepositoryIndex
argument_list|(
name|tx
argument_list|,
name|repositoryName
argument_list|)
decl_stmt|;
return|return
name|index
operator|.
name|put
argument_list|(
name|keyBuffer
argument_list|,
name|exchangeBuffer
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Adding key ["
operator|+
name|key
operator|+
literal|"]"
return|;
block|}
block|}
argument_list|)
decl_stmt|;
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
name|marshaller
operator|.
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|rc
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
DECL|method|get (final CamelContext camelContext, final K key)
specifier|public
name|Exchange
name|get
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|K
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
specifier|final
name|Buffer
name|keyBuffer
init|=
name|marshaller
operator|.
name|marshallKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Buffer
name|rc
init|=
name|hawtDBFile
operator|.
name|execute
argument_list|(
operator|new
name|Work
argument_list|<
name|Buffer
argument_list|>
argument_list|()
block|{
specifier|public
name|Buffer
name|execute
parameter_list|(
name|Transaction
name|tx
parameter_list|)
block|{
name|Index
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|index
init|=
name|hawtDBFile
operator|.
name|getRepositoryIndex
argument_list|(
name|tx
argument_list|,
name|repositoryName
argument_list|)
decl_stmt|;
return|return
name|index
operator|.
name|get
argument_list|(
name|keyBuffer
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Getting key ["
operator|+
name|key
operator|+
literal|"]"
return|;
block|}
block|}
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
name|marshaller
operator|.
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|rc
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
literal|"Getting key  ["
operator|+
name|key
operator|+
literal|"] -> "
operator|+
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|remove (final CamelContext camelContext, final K key, final Exchange exchange)
specifier|public
name|void
name|remove
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|K
name|key
parameter_list|,
specifier|final
name|Exchange
name|exchange
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
literal|"Removing key ["
operator|+
name|key
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
specifier|final
name|Buffer
name|keyBuffer
init|=
name|marshaller
operator|.
name|marshallKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
specifier|final
name|Buffer
name|confirmKeyBuffer
init|=
name|marshaller
operator|.
name|marshallConfirmKey
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Buffer
name|exchangeBuffer
init|=
name|marshaller
operator|.
name|marshallExchange
argument_list|(
name|camelContext
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|hawtDBFile
operator|.
name|execute
argument_list|(
operator|new
name|Work
argument_list|<
name|Buffer
argument_list|>
argument_list|()
block|{
specifier|public
name|Buffer
name|execute
parameter_list|(
name|Transaction
name|tx
parameter_list|)
block|{
name|Index
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|index
init|=
name|hawtDBFile
operator|.
name|getRepositoryIndex
argument_list|(
name|tx
argument_list|,
name|repositoryName
argument_list|)
decl_stmt|;
comment|// remove from the in progress index
name|index
operator|.
name|remove
argument_list|(
name|keyBuffer
argument_list|)
expr_stmt|;
comment|// and add it to the confirmed index
name|Index
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|indexCompleted
init|=
name|hawtDBFile
operator|.
name|getRepositoryIndex
argument_list|(
name|tx
argument_list|,
name|getRepositoryNameCompleted
argument_list|()
argument_list|)
decl_stmt|;
name|indexCompleted
operator|.
name|put
argument_list|(
name|confirmKeyBuffer
argument_list|,
name|exchangeBuffer
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Removing key ["
operator|+
name|key
operator|+
literal|"]"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
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
literal|"Confirming exchangeId ["
operator|+
name|exchangeId
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
specifier|final
name|Buffer
name|confirmKeyBuffer
init|=
name|marshaller
operator|.
name|marshallConfirmKey
argument_list|(
name|exchangeId
argument_list|)
decl_stmt|;
name|hawtDBFile
operator|.
name|execute
argument_list|(
operator|new
name|Work
argument_list|<
name|Buffer
argument_list|>
argument_list|()
block|{
specifier|public
name|Buffer
name|execute
parameter_list|(
name|Transaction
name|tx
parameter_list|)
block|{
name|Index
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|indexCompleted
init|=
name|hawtDBFile
operator|.
name|getRepositoryIndex
argument_list|(
name|tx
argument_list|,
name|getRepositoryNameCompleted
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|indexCompleted
operator|.
name|remove
argument_list|(
name|confirmKeyBuffer
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Confirming exchangeId ["
operator|+
name|exchangeId
operator|+
literal|"]"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
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
literal|"Error confirming exchangeId "
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
name|hawtDBFile
operator|.
name|execute
argument_list|(
operator|new
name|Work
argument_list|<
name|Buffer
argument_list|>
argument_list|()
block|{
specifier|public
name|Buffer
name|execute
parameter_list|(
name|Transaction
name|tx
parameter_list|)
block|{
comment|// scan could potentially be running while we are shutting down so check for that
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
name|Index
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|indexCompleted
init|=
name|hawtDBFile
operator|.
name|getRepositoryIndex
argument_list|(
name|tx
argument_list|,
name|getRepositoryNameCompleted
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
argument_list|>
name|it
init|=
name|indexCompleted
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|// scan could potentially be running while we are shutting down so check for that
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
operator|&&
name|isRunAllowed
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Buffer
name|keyBuffer
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|exchangeId
decl_stmt|;
try|try
block|{
name|exchangeId
operator|=
name|marshaller
operator|.
name|unmarshallConfirmKey
argument_list|(
name|keyBuffer
argument_list|)
expr_stmt|;
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
literal|"Error unmarshalling confirm key: "
operator|+
name|keyBuffer
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|exchangeId
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|exchangeId
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Scan"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
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
literal|"Scanned and found "
operator|+
name|answer
operator|.
name|size
argument_list|()
operator|+
literal|" exchange(s) to recover (note some of them may already be in progress)."
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
specifier|final
name|Buffer
name|confirmKeyBuffer
init|=
name|marshaller
operator|.
name|marshallConfirmKey
argument_list|(
name|exchangeId
argument_list|)
decl_stmt|;
name|Buffer
name|rc
init|=
name|hawtDBFile
operator|.
name|execute
argument_list|(
operator|new
name|Work
argument_list|<
name|Buffer
argument_list|>
argument_list|()
block|{
specifier|public
name|Buffer
name|execute
parameter_list|(
name|Transaction
name|tx
parameter_list|)
block|{
name|Index
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|indexCompleted
init|=
name|hawtDBFile
operator|.
name|getRepositoryIndex
argument_list|(
name|tx
argument_list|,
name|getRepositoryNameCompleted
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|indexCompleted
operator|.
name|get
argument_list|(
name|confirmKeyBuffer
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Recovering exchangeId ["
operator|+
name|exchangeId
operator|+
literal|"]"
return|;
block|}
block|}
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
name|marshaller
operator|.
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|rc
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
literal|"Recovering exchangeId ["
operator|+
name|exchangeId
operator|+
literal|"] -> "
operator|+
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getHawtDBFile ()
specifier|public
name|HawtDBFile
name|getHawtDBFile
parameter_list|()
block|{
return|return
name|hawtDBFile
return|;
block|}
DECL|method|setHawtDBFile (HawtDBFile hawtDBFile)
specifier|public
name|void
name|setHawtDBFile
parameter_list|(
name|HawtDBFile
name|hawtDBFile
parameter_list|)
block|{
name|this
operator|.
name|hawtDBFile
operator|=
name|hawtDBFile
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
DECL|method|getBufferSize ()
specifier|public
name|Integer
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
DECL|method|setBufferSize (Integer bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|Integer
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
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
DECL|method|setCheckInterval (long interval, TimeUnit timeUnit)
specifier|public
name|void
name|setCheckInterval
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
name|interval
operator|=
name|timeUnit
operator|.
name|toMillis
argument_list|(
name|interval
argument_list|)
expr_stmt|;
block|}
DECL|method|setCheckInterval (long interval)
specifier|public
name|void
name|setCheckInterval
parameter_list|(
name|long
name|interval
parameter_list|)
block|{
name|this
operator|.
name|interval
operator|=
name|interval
expr_stmt|;
block|}
DECL|method|getCheckIntervalInMillis ()
specifier|public
name|long
name|getCheckIntervalInMillis
parameter_list|()
block|{
return|return
name|interval
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
comment|// either we have a HawtDB configured or we use a provided fileName
if|if
condition|(
name|hawtDBFile
operator|==
literal|null
operator|&&
name|persistentFileName
operator|!=
literal|null
condition|)
block|{
name|hawtDBFile
operator|=
operator|new
name|HawtDBFile
argument_list|()
expr_stmt|;
name|hawtDBFile
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
name|persistentFileName
argument_list|)
argument_list|)
expr_stmt|;
name|hawtDBFile
operator|.
name|setSync
argument_list|(
name|isSync
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getBufferSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|hawtDBFile
operator|.
name|setMappingSegementSize
argument_list|(
name|getBufferSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|hawtDBFile
argument_list|,
literal|"Either set a persistentFileName or a hawtDBFile"
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
name|hawtDBFile
argument_list|)
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|hawtDBFile
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

