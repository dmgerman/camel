begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
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
name|FileOutputStream
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Scanner
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
name|atomic
operator|.
name|AtomicBoolean
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
name|IdempotentRepository
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
name|LRUCache
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
comment|/**  * A file based implementation of {@link org.apache.camel.spi.IdempotentRepository}.  *<p/>  * Care should be taken to use a suitable underlying {@link java.util.Map} to avoid this class being a  * memory leak.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|FileIdempotentRepository
specifier|public
class|class
name|FileIdempotentRepository
implements|implements
name|IdempotentRepository
argument_list|<
name|String
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
name|FileIdempotentRepository
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|STORE_DELIMITER
specifier|private
specifier|static
specifier|final
name|String
name|STORE_DELIMITER
init|=
literal|"\n"
decl_stmt|;
DECL|field|cache
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cache
decl_stmt|;
DECL|field|fileStore
specifier|private
name|File
name|fileStore
decl_stmt|;
DECL|field|maxFileStoreSize
specifier|private
name|long
name|maxFileStoreSize
init|=
literal|1024
operator|*
literal|1000L
decl_stmt|;
comment|// 1mb store file
DECL|field|init
specifier|private
name|AtomicBoolean
name|init
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|method|FileIdempotentRepository ()
specifier|public
name|FileIdempotentRepository
parameter_list|()
block|{
comment|// default use a 1st level cache
name|this
operator|.
name|cache
operator|=
operator|new
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
DECL|method|FileIdempotentRepository (File fileStore, Map<String, Object> set)
specifier|public
name|FileIdempotentRepository
parameter_list|(
name|File
name|fileStore
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|set
parameter_list|)
block|{
name|this
operator|.
name|fileStore
operator|=
name|fileStore
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|set
expr_stmt|;
block|}
comment|/**      * Creates a new file based repository using a {@link org.apache.camel.util.LRUCache}      * as 1st level cache with a default of 1000 entries in the cache.      *      * @param fileStore  the file store      */
DECL|method|fileIdempotentRepository (File fileStore)
specifier|public
specifier|static
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|fileIdempotentRepository
parameter_list|(
name|File
name|fileStore
parameter_list|)
block|{
return|return
name|fileIdempotentRepository
argument_list|(
name|fileStore
argument_list|,
literal|1000
argument_list|)
return|;
block|}
comment|/**      * Creates a new file based repository using a {@link org.apache.camel.util.LRUCache}      * as 1st level cache.      *      * @param fileStore  the file store      * @param cacheSize  the cache size      */
DECL|method|fileIdempotentRepository (File fileStore, int cacheSize)
specifier|public
specifier|static
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|fileIdempotentRepository
parameter_list|(
name|File
name|fileStore
parameter_list|,
name|int
name|cacheSize
parameter_list|)
block|{
return|return
name|fileIdempotentRepository
argument_list|(
name|fileStore
argument_list|,
operator|new
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|cacheSize
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a new file based repository using a {@link org.apache.camel.util.LRUCache}      * as 1st level cache.      *      * @param fileStore  the file store      * @param cacheSize  the cache size      * @param maxFileStoreSize  the max size in bytes for the filestore file       */
DECL|method|fileIdempotentRepository (File fileStore, int cacheSize, long maxFileStoreSize)
specifier|public
specifier|static
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|fileIdempotentRepository
parameter_list|(
name|File
name|fileStore
parameter_list|,
name|int
name|cacheSize
parameter_list|,
name|long
name|maxFileStoreSize
parameter_list|)
block|{
name|FileIdempotentRepository
name|repository
init|=
operator|new
name|FileIdempotentRepository
argument_list|(
name|fileStore
argument_list|,
operator|new
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|cacheSize
argument_list|)
argument_list|)
decl_stmt|;
name|repository
operator|.
name|setMaxFileStoreSize
argument_list|(
name|maxFileStoreSize
argument_list|)
expr_stmt|;
return|return
name|repository
return|;
block|}
comment|/**      * Creates a new file based repository using the given {@link java.util.Map}      * as 1st level cache.      *<p/>      * Care should be taken to use a suitable underlying {@link java.util.Map} to avoid this class being a      * memory leak.      *      * @param store  the file store      * @param cache  the cache to use as 1st level cache      */
DECL|method|fileIdempotentRepository (File store, Map<String, Object> cache)
specifier|public
specifier|static
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|fileIdempotentRepository
parameter_list|(
name|File
name|store
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|)
block|{
return|return
operator|new
name|FileIdempotentRepository
argument_list|(
name|store
argument_list|,
name|cache
argument_list|)
return|;
block|}
DECL|method|add (String messageId)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|messageId
parameter_list|)
block|{
synchronized|synchronized
init|(
name|cache
init|)
block|{
comment|// init store if not loaded before
if|if
condition|(
name|init
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|loadStore
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|cache
operator|.
name|containsKey
argument_list|(
name|messageId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|cache
operator|.
name|put
argument_list|(
name|messageId
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
if|if
condition|(
name|fileStore
operator|.
name|length
argument_list|()
operator|<
name|maxFileStoreSize
condition|)
block|{
comment|// just append to store
name|appendToStore
argument_list|(
name|messageId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// trunk store and flush the cache
name|trunkStore
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
DECL|method|contains (String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
block|{
synchronized|synchronized
init|(
name|cache
init|)
block|{
comment|// init store if not loaded before
if|if
condition|(
name|init
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|loadStore
argument_list|()
expr_stmt|;
block|}
return|return
name|cache
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
DECL|method|remove (String key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
synchronized|synchronized
init|(
name|cache
init|)
block|{
comment|// init store if not loaded before
if|if
condition|(
name|init
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|loadStore
argument_list|()
expr_stmt|;
block|}
return|return
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
DECL|method|getFileStore ()
specifier|public
name|File
name|getFileStore
parameter_list|()
block|{
return|return
name|fileStore
return|;
block|}
DECL|method|setFileStore (File fileStore)
specifier|public
name|void
name|setFileStore
parameter_list|(
name|File
name|fileStore
parameter_list|)
block|{
name|this
operator|.
name|fileStore
operator|=
name|fileStore
expr_stmt|;
block|}
DECL|method|getCache ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
DECL|method|setCache (Map<String, Object> cache)
specifier|public
name|void
name|setCache
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
DECL|method|getMaxFileStoreSize ()
specifier|public
name|long
name|getMaxFileStoreSize
parameter_list|()
block|{
return|return
name|maxFileStoreSize
return|;
block|}
comment|/**      * Sets the maximum filesize for the file store in bytes.      *<p/>      * The default is 1mb.      */
DECL|method|setMaxFileStoreSize (long maxFileStoreSize)
specifier|public
name|void
name|setMaxFileStoreSize
parameter_list|(
name|long
name|maxFileStoreSize
parameter_list|)
block|{
name|this
operator|.
name|maxFileStoreSize
operator|=
name|maxFileStoreSize
expr_stmt|;
block|}
comment|/**      * Sets the cache size      */
DECL|method|setCacheSize (int size)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|cache
operator|=
operator|new
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends the given message id to the file store      *      * @param messageId  the message id      */
DECL|method|appendToStore (final String messageId)
specifier|protected
name|void
name|appendToStore
parameter_list|(
specifier|final
name|String
name|messageId
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
literal|"Appending "
operator|+
name|messageId
operator|+
literal|" to idempotent filestore: "
operator|+
name|fileStore
argument_list|)
expr_stmt|;
block|}
name|FileOutputStream
name|fos
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// create store if missing
if|if
condition|(
operator|!
name|fileStore
operator|.
name|exists
argument_list|()
condition|)
block|{
name|fileStore
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
comment|// append to store
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|fileStore
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|messageId
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|STORE_DELIMITER
operator|.
name|getBytes
argument_list|()
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|fos
argument_list|,
literal|"Appending to file idempotent repository"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Trunks the file store when the max store size is hit by rewriting the 1st level cache      * to the file store.      */
DECL|method|trunkStore ()
specifier|protected
name|void
name|trunkStore
parameter_list|()
block|{
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Trunking idempotent filestore: "
operator|+
name|fileStore
argument_list|)
expr_stmt|;
block|}
name|FileOutputStream
name|fos
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|fileStore
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|cache
operator|.
name|keySet
argument_list|()
control|)
block|{
name|fos
operator|.
name|write
argument_list|(
name|key
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|STORE_DELIMITER
operator|.
name|getBytes
argument_list|()
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|fos
argument_list|,
literal|"Trunking file idempotent repository"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Loads the given file store into the 1st level cache      */
DECL|method|loadStore ()
specifier|protected
name|void
name|loadStore
parameter_list|()
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
literal|"Loading to 1st level cache from idempotent filestore: "
operator|+
name|fileStore
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|fileStore
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return;
block|}
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Scanner
name|scanner
init|=
literal|null
decl_stmt|;
try|try
block|{
name|scanner
operator|=
operator|new
name|Scanner
argument_list|(
name|fileStore
argument_list|)
expr_stmt|;
name|scanner
operator|.
name|useDelimiter
argument_list|(
name|STORE_DELIMITER
argument_list|)
expr_stmt|;
while|while
condition|(
name|scanner
operator|.
name|hasNextLine
argument_list|()
condition|)
block|{
name|String
name|line
init|=
name|scanner
operator|.
name|nextLine
argument_list|()
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|line
argument_list|,
name|line
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|scanner
operator|!=
literal|null
condition|)
block|{
name|scanner
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
literal|"Loaded "
operator|+
name|cache
operator|.
name|size
argument_list|()
operator|+
literal|" to the 1st level cache from idempotent filestore: "
operator|+
name|fileStore
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

