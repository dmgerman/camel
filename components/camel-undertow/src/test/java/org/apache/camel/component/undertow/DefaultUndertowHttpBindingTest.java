begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|delayedPayloads
index|[
literal|0
index|]
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
name|XnioIoThread
name|thread
init|=
name|thread
argument_list|()
decl_stmt|;
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
argument_list|)
block|{
name|int
name|chunk
init|=
literal|0
decl_stmt|;
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
comment|// can only read payloads in the reader thread
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
return|return
literal|0
return|;
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

