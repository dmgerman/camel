begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|util
operator|.
name|concurrent
operator|.
name|ThreadHelper
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
comment|/**  * Factory to create {@link LRUCache} instances.  */
end_comment

begin_class
DECL|class|LRUCacheFactory
specifier|public
specifier|final
class|class
name|LRUCacheFactory
block|{
comment|// TODO: use LRUCacheFactory in other places to create the LRUCaches
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
name|LRUCacheFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|LRUCacheFactory ()
specifier|private
name|LRUCacheFactory
parameter_list|()
block|{     }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|warmUp ()
specifier|public
specifier|static
name|void
name|warmUp
parameter_list|()
block|{
comment|// create a dummy map in a separate thread to warmup the Caffeine cache
comment|// as we want to do this as early as possible while creating CamelContext
comment|// so when Camel is starting up its faster as the Caffeine cache has been initialized
name|Runnable
name|warmup
init|=
parameter_list|()
lambda|->
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Warming up LRUCache ..."
argument_list|)
expr_stmt|;
name|newLRUCache
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Warming up LRUCache complete"
argument_list|)
expr_stmt|;
block|}
decl_stmt|;
name|String
name|threadName
init|=
name|ThreadHelper
operator|.
name|resolveThreadName
argument_list|(
literal|null
argument_list|,
literal|"LRUCacheFactory"
argument_list|)
decl_stmt|;
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|warmup
argument_list|,
name|threadName
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|newLRUCache (int maximumCacheSize)
specifier|public
specifier|static
name|LRUCache
name|newLRUCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
return|return
operator|new
name|LRUCache
argument_list|(
name|maximumCacheSize
argument_list|)
return|;
block|}
DECL|method|newLRUWeakCache (int maximumCacheSize)
specifier|public
specifier|static
name|LRUWeakCache
name|newLRUWeakCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
return|return
operator|new
name|LRUWeakCache
argument_list|(
name|maximumCacheSize
argument_list|)
return|;
block|}
block|}
end_class

end_unit

