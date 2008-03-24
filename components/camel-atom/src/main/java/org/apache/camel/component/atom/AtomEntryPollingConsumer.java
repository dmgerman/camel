begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
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
name|abdera
operator|.
name|model
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Feed
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
name|PollingConsumerSupport
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|AtomEntryPollingConsumer
specifier|public
class|class
name|AtomEntryPollingConsumer
extends|extends
name|PollingConsumerSupport
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|AtomEndpoint
name|endpoint
decl_stmt|;
DECL|field|document
specifier|private
name|Document
argument_list|<
name|Feed
argument_list|>
name|document
decl_stmt|;
DECL|field|entryIndex
specifier|private
name|int
name|entryIndex
decl_stmt|;
DECL|field|entryFilter
specifier|private
name|EntryFilter
name|entryFilter
init|=
operator|new
name|UpdatedDateFilter
argument_list|()
decl_stmt|;
DECL|field|list
specifier|private
name|List
argument_list|<
name|Entry
argument_list|>
name|list
decl_stmt|;
DECL|method|AtomEntryPollingConsumer (AtomEndpoint endpoint)
specifier|public
name|AtomEntryPollingConsumer
parameter_list|(
name|AtomEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|receiveNoWait ()
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|()
block|{
try|try
block|{
name|getDocument
argument_list|()
expr_stmt|;
while|while
condition|(
name|hasNextEntry
argument_list|()
condition|)
block|{
name|Entry
name|entry
init|=
name|list
operator|.
name|get
argument_list|(
name|entryIndex
operator|--
argument_list|)
decl_stmt|;
if|if
condition|(
name|entryFilter
operator|.
name|isValidEntry
argument_list|(
name|endpoint
argument_list|,
name|document
argument_list|,
name|entry
argument_list|)
condition|)
block|{
return|return
name|endpoint
operator|.
name|createExchange
argument_list|(
name|document
argument_list|,
name|entry
argument_list|)
return|;
block|}
block|}
name|document
operator|=
literal|null
expr_stmt|;
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|receive ()
specifier|public
name|Exchange
name|receive
parameter_list|()
block|{
return|return
name|receiveNoWait
argument_list|()
return|;
block|}
DECL|method|receive (long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
return|return
name|receiveNoWait
argument_list|()
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getEntryFilter ()
specifier|public
name|EntryFilter
name|getEntryFilter
parameter_list|()
block|{
return|return
name|entryFilter
return|;
block|}
DECL|method|setEntryFilter (EntryFilter entryFilter)
specifier|public
name|void
name|setEntryFilter
parameter_list|(
name|EntryFilter
name|entryFilter
parameter_list|)
block|{
name|this
operator|.
name|entryFilter
operator|=
name|entryFilter
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|getDocument ()
specifier|public
name|Document
argument_list|<
name|Feed
argument_list|>
name|getDocument
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|document
operator|==
literal|null
condition|)
block|{
name|document
operator|=
name|endpoint
operator|.
name|parseDocument
argument_list|()
expr_stmt|;
name|list
operator|=
name|document
operator|.
name|getRoot
argument_list|()
operator|.
name|getEntries
argument_list|()
expr_stmt|;
name|entryIndex
operator|=
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
expr_stmt|;
block|}
return|return
name|document
return|;
block|}
DECL|method|hasNextEntry ()
specifier|protected
name|boolean
name|hasNextEntry
parameter_list|()
block|{
return|return
name|entryIndex
operator|>=
literal|0
return|;
block|}
block|}
end_class

end_unit

