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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Service
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
name|fusesource
operator|.
name|hawtbuf
operator|.
name|codec
operator|.
name|BufferCodec
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
name|codec
operator|.
name|IntegerCodec
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
name|codec
operator|.
name|StringCodec
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
name|BTreeIndexFactory
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
name|OptimisticUpdateException
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
name|SortedIndex
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
name|api
operator|.
name|TxPageFile
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
name|TxPageFileFactory
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
comment|/**  * Manages access to a shared<a href="http://hawtdb.fusesource.org/">HawtDB</a> file.  *<p/>  * Will by default not sync writes which allows it to be faster.  * You can force syncing by setting the sync option to<tt>true</tt>.  */
end_comment

begin_class
DECL|class|HawtDBFile
specifier|public
class|class
name|HawtDBFile
extends|extends
name|TxPageFileFactory
implements|implements
name|Service
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
name|HawtDBFile
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// the root which contains an index with name -> page for the real indexes
DECL|field|ROOT_INDEXES_FACTORY
specifier|private
specifier|static
specifier|final
name|BTreeIndexFactory
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|ROOT_INDEXES_FACTORY
init|=
operator|new
name|BTreeIndexFactory
argument_list|<>
argument_list|()
decl_stmt|;
comment|// the real indexes where we store persisted data in buffers
DECL|field|INDEX_FACTORY
specifier|private
specifier|static
specifier|final
name|BTreeIndexFactory
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|INDEX_FACTORY
init|=
operator|new
name|BTreeIndexFactory
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|pageFile
specifier|private
name|TxPageFile
name|pageFile
decl_stmt|;
static|static
block|{
name|ROOT_INDEXES_FACTORY
operator|.
name|setKeyCodec
argument_list|(
name|StringCodec
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|ROOT_INDEXES_FACTORY
operator|.
name|setValueCodec
argument_list|(
name|IntegerCodec
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
comment|//ROOT_INDEXES_FACTORY.setDeferredEncoding(true);
comment|// TODO: use false due CAMEL-3826 until root cause is fixed in hawtdb
name|ROOT_INDEXES_FACTORY
operator|.
name|setDeferredEncoding
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|INDEX_FACTORY
operator|.
name|setKeyCodec
argument_list|(
name|BufferCodec
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|INDEX_FACTORY
operator|.
name|setValueCodec
argument_list|(
name|BufferCodec
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
comment|//INDEX_FACTORY.setDeferredEncoding(true);
comment|// TODO: use false due CAMEL-3826 until root cause is fixed in hawtdb
name|INDEX_FACTORY
operator|.
name|setDeferredEncoding
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|HawtDBFile ()
specifier|public
name|HawtDBFile
parameter_list|()
block|{
name|setSync
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A file must be configured"
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
literal|"Starting HawtDB using file: {}"
argument_list|,
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|open
argument_list|()
expr_stmt|;
name|pageFile
operator|=
name|getTxPageFile
argument_list|()
expr_stmt|;
name|execute
argument_list|(
operator|new
name|Work
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|execute
parameter_list|(
name|Transaction
name|tx
parameter_list|)
block|{
if|if
condition|(
operator|!
name|tx
operator|.
name|allocator
argument_list|()
operator|.
name|isAllocated
argument_list|(
literal|0
argument_list|)
condition|)
block|{
comment|// if we just created the file, first allocated page should be 0
name|ROOT_INDEXES_FACTORY
operator|.
name|create
argument_list|(
name|tx
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Aggregation repository data store created using file: {}"
argument_list|,
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SortedIndex
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|indexes
init|=
name|ROOT_INDEXES_FACTORY
operator|.
name|open
argument_list|(
name|tx
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Aggregation repository data store loaded using file: "
operator|+
name|getFile
argument_list|()
operator|+
literal|" containing "
operator|+
name|indexes
operator|.
name|size
argument_list|()
operator|+
literal|" repositories."
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
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
literal|"Allocation repository file: "
operator|+
name|getFile
argument_list|()
return|;
block|}
block|}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|File
name|file
init|=
name|getFile
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping HawtDB using file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
try|try
block|{
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error closing HawtDB file "
operator|+
name|file
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|pageFile
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|execute (Work<T> work)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|Work
argument_list|<
name|T
argument_list|>
name|work
parameter_list|)
block|{
return|return
name|execute
argument_list|(
name|work
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|execute (Work<T> work, boolean rollbackOnOptimisticUpdateException)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|Work
argument_list|<
name|T
argument_list|>
name|work
parameter_list|,
name|boolean
name|rollbackOnOptimisticUpdateException
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Executing work +++ start +++ {}"
argument_list|,
name|work
argument_list|)
expr_stmt|;
name|Transaction
name|tx
init|=
name|pageFile
operator|.
name|tx
argument_list|()
decl_stmt|;
name|T
name|answer
init|=
name|doExecute
argument_list|(
name|work
argument_list|,
name|tx
argument_list|,
name|pageFile
argument_list|,
name|rollbackOnOptimisticUpdateException
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Executing work +++ done  +++ {}"
argument_list|,
name|work
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getRepositoryIndex (Transaction tx, String name, boolean create)
specifier|public
name|SortedIndex
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|getRepositoryIndex
parameter_list|(
name|Transaction
name|tx
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
name|SortedIndex
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|answer
init|=
literal|null
decl_stmt|;
name|SortedIndex
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|indexes
init|=
name|ROOT_INDEXES_FACTORY
operator|.
name|open
argument_list|(
name|tx
argument_list|)
decl_stmt|;
name|Integer
name|location
init|=
name|indexes
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|create
operator|&&
name|location
operator|==
literal|null
condition|)
block|{
comment|// create it..
name|SortedIndex
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
name|created
init|=
name|INDEX_FACTORY
operator|.
name|create
argument_list|(
name|tx
argument_list|)
decl_stmt|;
name|int
name|page
init|=
name|created
operator|.
name|getIndexLocation
argument_list|()
decl_stmt|;
comment|// add it to indexes so we can find it the next time
name|indexes
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|page
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created new repository index with name {} at location {}"
argument_list|,
name|name
argument_list|,
name|page
argument_list|)
expr_stmt|;
name|answer
operator|=
name|created
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|location
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Repository index with name {} at location {}"
argument_list|,
name|name
argument_list|,
name|location
argument_list|)
expr_stmt|;
name|answer
operator|=
name|INDEX_FACTORY
operator|.
name|open
argument_list|(
name|tx
argument_list|,
name|location
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Repository index with name {} -> {}"
argument_list|,
name|name
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|doExecute (Work<T> work, Transaction tx, TxPageFile page, boolean handleOptimisticLockingException)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|doExecute
parameter_list|(
name|Work
argument_list|<
name|T
argument_list|>
name|work
parameter_list|,
name|Transaction
name|tx
parameter_list|,
name|TxPageFile
name|page
parameter_list|,
name|boolean
name|handleOptimisticLockingException
parameter_list|)
block|{
name|T
name|answer
init|=
literal|null
decl_stmt|;
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|int
name|attempt
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
try|try
block|{
comment|// only log at DEBUG level if we are retrying
if|if
condition|(
name|attempt
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Attempt {} to execute work {}"
argument_list|,
name|attempt
argument_list|,
name|work
argument_list|)
expr_stmt|;
block|}
name|attempt
operator|++
expr_stmt|;
comment|// execute and get answer
name|answer
operator|=
name|work
operator|.
name|execute
argument_list|(
name|tx
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"TX is read only: {} for executed work: {}"
argument_list|,
name|tx
operator|.
name|isReadOnly
argument_list|()
argument_list|,
name|work
argument_list|)
expr_stmt|;
comment|// commit work
name|tx
operator|.
name|commit
argument_list|()
expr_stmt|;
comment|// and flush so we ensure data is spooled to disk
name|page
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// and we are done
name|done
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OptimisticUpdateException
name|e
parameter_list|)
block|{
if|if
condition|(
name|handleOptimisticLockingException
condition|)
block|{
comment|// retry as we hit an optimistic update error
name|LOG
operator|.
name|warn
argument_list|(
literal|"OptimisticUpdateException occurred at attempt "
operator|+
name|attempt
operator|+
literal|" executing work "
operator|+
name|work
operator|+
literal|". Will do rollback and retry."
argument_list|)
expr_stmt|;
comment|// no harm doing rollback before retry and no wait is needed
name|tx
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// we must rollback and rethrow
name|tx
operator|.
name|rollback
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error executing work "
operator|+
name|work
operator|+
literal|". Will do rollback."
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|tx
operator|.
name|rollback
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

