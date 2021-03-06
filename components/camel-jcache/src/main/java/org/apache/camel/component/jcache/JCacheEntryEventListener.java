begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcache
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryCreatedListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryExpiredListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryListenerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryRemovedListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryUpdatedListener
import|;
end_import

begin_class
DECL|class|JCacheEntryEventListener
class|class
name|JCacheEntryEventListener
implements|implements
name|CacheEntryCreatedListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
implements|,
name|CacheEntryUpdatedListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
implements|,
name|CacheEntryRemovedListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
implements|,
name|CacheEntryExpiredListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
annotation|@
name|Override
DECL|method|onCreated (Iterable<CacheEntryEvent<?, ?>> events)
specifier|public
name|void
name|onCreated
parameter_list|(
name|Iterable
argument_list|<
name|CacheEntryEvent
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|events
parameter_list|)
throws|throws
name|CacheEntryListenerException
block|{
name|onEvents
argument_list|(
name|events
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onExpired (Iterable<CacheEntryEvent<?, ?>> events)
specifier|public
name|void
name|onExpired
parameter_list|(
name|Iterable
argument_list|<
name|CacheEntryEvent
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|events
parameter_list|)
throws|throws
name|CacheEntryListenerException
block|{
name|onEvents
argument_list|(
name|events
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onRemoved (Iterable<CacheEntryEvent<?, ?>> events)
specifier|public
name|void
name|onRemoved
parameter_list|(
name|Iterable
argument_list|<
name|CacheEntryEvent
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|events
parameter_list|)
throws|throws
name|CacheEntryListenerException
block|{
name|onEvents
argument_list|(
name|events
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onUpdated (Iterable<CacheEntryEvent<?, ?>> events)
specifier|public
name|void
name|onUpdated
parameter_list|(
name|Iterable
argument_list|<
name|CacheEntryEvent
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|events
parameter_list|)
throws|throws
name|CacheEntryListenerException
block|{
name|onEvents
argument_list|(
name|events
argument_list|)
expr_stmt|;
block|}
DECL|method|onEvents (Iterable<CacheEntryEvent<?, ?>> events)
specifier|protected
name|void
name|onEvents
parameter_list|(
name|Iterable
argument_list|<
name|CacheEntryEvent
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|events
parameter_list|)
block|{     }
block|}
end_class

end_unit

