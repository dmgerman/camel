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
name|internal
operator|.
name|page
operator|.
name|HawtPageFile
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
name|internal
operator|.
name|page
operator|.
name|HawtPageFileFactory
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
name|marshaller
operator|.
name|IntegerMarshaller
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
name|marshaller
operator|.
name|StringMarshaller
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
name|marshaller
operator|.
name|VariableBufferMarshaller
import|;
end_import

begin_comment
comment|/**  * Manages access to a shared HawtDB file.  *<p/>  * Will by default not sync writes which allows it to be faster.  * You can force syncing by setting sync=true.  */
end_comment

begin_class
DECL|class|HawtDBFile
specifier|public
class|class
name|HawtDBFile
extends|extends
name|HawtPageFileFactory
implements|implements
name|Service
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
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
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
argument_list|<
name|Buffer
argument_list|,
name|Buffer
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|pageFile
specifier|private
name|HawtPageFile
name|pageFile
decl_stmt|;
static|static
block|{
name|ROOT_INDEXES_FACTORY
operator|.
name|setKeyMarshaller
argument_list|(
name|StringMarshaller
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|ROOT_INDEXES_FACTORY
operator|.
name|setValueMarshaller
argument_list|(
name|IntegerMarshaller
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|ROOT_INDEXES_FACTORY
operator|.
name|setDeferredEncoding
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|INDEX_FACTORY
operator|.
name|setKeyMarshaller
argument_list|(
name|VariableBufferMarshaller
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|INDEX_FACTORY
operator|.
name|setValueMarshaller
argument_list|(
name|VariableBufferMarshaller
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|INDEX_FACTORY
operator|.
name|setDeferredEncoding
argument_list|(
literal|true
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
literal|"Starting HawtDB using file: "
operator|+
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|boolean
name|initialize
init|=
operator|!
name|file
operator|.
name|exists
argument_list|()
decl_stmt|;
name|open
argument_list|()
expr_stmt|;
name|pageFile
operator|=
name|getConcurrentPageFile
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
name|initialize
condition|)
block|{
name|int
name|page
init|=
name|tx
operator|.
name|allocator
argument_list|()
operator|.
name|alloc
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// if we just created the file, first allocated page should be 0
assert|assert
name|page
operator|==
literal|0
assert|;
name|ROOT_INDEXES_FACTORY
operator|.
name|create
argument_list|(
name|tx
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Aggregation repository data store created using file: "
operator|+
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Index
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
argument_list|,
literal|0
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
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
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
literal|"Stopping HawtDB using file: "
operator|+
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|close
argument_list|()
expr_stmt|;
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
literal|"Executing work "
operator|+
name|work
argument_list|)
expr_stmt|;
block|}
name|Transaction
name|tx
init|=
name|pageFile
operator|.
name|tx
argument_list|()
decl_stmt|;
try|try
block|{
name|T
name|rc
init|=
name|work
operator|.
name|execute
argument_list|(
name|tx
argument_list|)
decl_stmt|;
name|tx
operator|.
name|commit
argument_list|()
expr_stmt|;
return|return
name|rc
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
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
literal|"Error executing work "
operator|+
name|work
operator|+
literal|" will do rollback"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getRepositoryIndex (Transaction tx, String name)
specifier|public
name|Index
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
parameter_list|)
block|{
name|Index
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
argument_list|,
literal|0
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
name|location
operator|==
literal|null
condition|)
block|{
comment|// create it..
name|int
name|page
init|=
name|tx
operator|.
name|allocator
argument_list|()
operator|.
name|alloc
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Index
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
argument_list|,
name|page
argument_list|)
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
literal|"Created new repository index with name "
operator|+
name|name
operator|+
literal|" at location "
operator|+
name|page
argument_list|)
expr_stmt|;
block|}
return|return
name|created
return|;
block|}
else|else
block|{
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
literal|"Repository index with name "
operator|+
name|name
operator|+
literal|" at location "
operator|+
name|location
argument_list|)
expr_stmt|;
block|}
return|return
name|INDEX_FACTORY
operator|.
name|open
argument_list|(
name|tx
argument_list|,
name|location
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

