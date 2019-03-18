begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
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
name|nio
operator|.
name|ByteBuffer
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
name|ExecutorService
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
name|Executors
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|XnioIoThread
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|channels
operator|.
name|EmptyStreamSourceChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|channels
operator|.
name|StreamSourceChannel
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
DECL|class|DefaultUndertowHttpBindingTest
specifier|public
class|class
name|DefaultUndertowHttpBindingTest
block|{
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|1000
argument_list|)
DECL|method|readEntireDelayedPayload ()
specifier|public
name|void
name|readEntireDelayedPayload
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|delayedPayloads
init|=
operator|new
name|String
index|[]
block|{
literal|""
block|,
literal|"chunk"
block|,         }
decl_stmt|;
name|StreamSourceChannel
name|source
init|=
name|source
argument_list|(
name|delayedPayloads
argument_list|)
decl_stmt|;
name|DefaultUndertowHttpBinding
name|binding
init|=
operator|new
name|DefaultUndertowHttpBinding
argument_list|()
decl_stmt|;
name|String
name|result
init|=
operator|new
name|String
argument_list|(
name|binding
operator|.
name|readFromChannel
argument_list|(
name|source
argument_list|)
argument_list|)
decl_stmt|;
name|checkResult
argument_list|(
name|result
argument_list|,
name|delayedPayloads
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|1000
argument_list|)
DECL|method|readEntireMultiDelayedPayload ()
specifier|public
name|void
name|readEntireMultiDelayedPayload
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|delayedPayloads
init|=
operator|new
name|String
index|[]
block|{
literal|""
block|,
literal|"first "
block|,
literal|"second"
block|,         }
decl_stmt|;
name|StreamSourceChannel
name|source
init|=
name|source
argument_list|(
name|delayedPayloads
argument_list|)
decl_stmt|;
name|DefaultUndertowHttpBinding
name|binding
init|=
operator|new
name|DefaultUndertowHttpBinding
argument_list|()
decl_stmt|;
name|String
name|result
init|=
operator|new
name|String
argument_list|(
name|binding
operator|.
name|readFromChannel
argument_list|(
name|source
argument_list|)
argument_list|)
decl_stmt|;
name|checkResult
argument_list|(
name|result
argument_list|,
name|delayedPayloads
argument_list|)
expr_stmt|;
block|}
DECL|method|checkResult (String result, String[] delayedPayloads)
specifier|private
name|void
name|checkResult
parameter_list|(
name|String
name|result
parameter_list|,
name|String
index|[]
name|delayedPayloads
parameter_list|)
block|{
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|Stream
operator|.
name|of
argument_list|(
name|delayedPayloads
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|1000
argument_list|)
DECL|method|readEntireMultiDelayedWithPausePayload ()
specifier|public
name|void
name|readEntireMultiDelayedWithPausePayload
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|delayedPayloads
init|=
operator|new
name|String
index|[]
block|{
literal|""
block|,
literal|"first "
block|,
literal|""
block|,
literal|"second"
block|,         }
decl_stmt|;
name|StreamSourceChannel
name|source
init|=
name|source
argument_list|(
name|delayedPayloads
argument_list|)
decl_stmt|;
name|DefaultUndertowHttpBinding
name|binding
init|=
operator|new
name|DefaultUndertowHttpBinding
argument_list|()
decl_stmt|;
name|String
name|result
init|=
operator|new
name|String
argument_list|(
name|binding
operator|.
name|readFromChannel
argument_list|(
name|source
argument_list|)
argument_list|)
decl_stmt|;
name|checkResult
argument_list|(
name|result
argument_list|,
name|delayedPayloads
argument_list|)
expr_stmt|;
block|}
DECL|method|source (final String[] delayedPayloads)
specifier|private
name|StreamSourceChannel
name|source
parameter_list|(
specifier|final
name|String
index|[]
name|delayedPayloads
parameter_list|)
block|{
name|Thread
name|sourceThread
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
return|return
operator|new
name|EmptyStreamSourceChannel
argument_list|(
name|thread
argument_list|()
argument_list|)
block|{
name|int
name|chunk
decl_stmt|;
name|boolean
name|mustWait
decl_stmt|;
comment|// make sure that the caller is not spinning on read==0
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|ByteBuffer
name|dst
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|mustWait
condition|)
block|{
name|fail
argument_list|(
literal|"must wait before reading"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|chunk
operator|<
name|delayedPayloads
operator|.
name|length
condition|)
block|{
name|byte
index|[]
name|delayedPayload
init|=
name|delayedPayloads
index|[
name|chunk
index|]
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|dst
operator|.
name|put
argument_list|(
name|delayedPayload
argument_list|)
expr_stmt|;
name|chunk
operator|++
expr_stmt|;
if|if
condition|(
name|delayedPayload
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|mustWait
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|delayedPayload
operator|.
name|length
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|resumeReads
parameter_list|()
block|{
comment|/**                  * {@link io.undertow.server.HttpServerExchange.ReadDispatchChannel} delays resumes in the main thread                  */
if|if
condition|(
name|sourceThread
operator|!=
name|Thread
operator|.
name|currentThread
argument_list|()
condition|)
block|{
name|super
operator|.
name|resumeReads
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|awaitReadable
parameter_list|()
throws|throws
name|IOException
block|{
name|mustWait
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|awaitReadable
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|thread ()
specifier|private
name|XnioIoThread
name|thread
parameter_list|()
block|{
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|1
argument_list|)
decl_stmt|;
return|return
operator|new
name|XnioIoThread
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Key
name|executeAfter
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|long
name|l
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|execute
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Key
name|executeAtInterval
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|long
name|l
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|execute
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

