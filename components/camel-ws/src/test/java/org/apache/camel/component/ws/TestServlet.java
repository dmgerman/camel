begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|WebSocket
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|WebSocketServlet
import|;
end_import

begin_class
DECL|class|TestServlet
specifier|public
class|class
name|TestServlet
extends|extends
name|WebSocketServlet
block|{
DECL|field|OPCODE_CONTINUATION
specifier|private
specifier|static
specifier|final
name|byte
name|OPCODE_CONTINUATION
init|=
literal|0x00
decl_stmt|;
DECL|field|OPCODE_TEXT
specifier|private
specifier|static
specifier|final
name|byte
name|OPCODE_TEXT
init|=
literal|0x01
decl_stmt|;
DECL|field|OPCODE_BINARY
specifier|private
specifier|static
specifier|final
name|byte
name|OPCODE_BINARY
init|=
literal|0x02
decl_stmt|;
DECL|field|FLAGS_FINAL
specifier|private
specifier|static
specifier|final
name|byte
name|FLAGS_FINAL
init|=
literal|0x08
decl_stmt|;
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|messages
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|messages
decl_stmt|;
DECL|method|TestServlet (List<Object> messages)
specifier|public
name|TestServlet
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|messages
parameter_list|)
block|{
name|this
operator|.
name|messages
operator|=
name|messages
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doWebSocketConnect (HttpServletRequest request, String protocol)
specifier|public
name|WebSocket
name|doWebSocketConnect
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|String
name|protocol
parameter_list|)
block|{
return|return
operator|new
name|TestWsSocket
argument_list|()
return|;
block|}
DECL|class|TestWsSocket
specifier|private
class|class
name|TestWsSocket
implements|implements
name|WebSocket
operator|.
name|OnTextMessage
implements|,
name|WebSocket
operator|.
name|OnBinaryMessage
implements|,
name|WebSocket
operator|.
name|OnFrame
block|{
DECL|field|con
specifier|protected
name|Connection
name|con
decl_stmt|;
DECL|field|frameBuffer
specifier|protected
name|ByteArrayOutputStream
name|frameBuffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|onOpen (Connection connection)
specifier|public
name|void
name|onOpen
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|con
operator|=
name|connection
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onClose (int i, String s)
specifier|public
name|void
name|onClose
parameter_list|(
name|int
name|i
parameter_list|,
name|String
name|s
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onMessage (String data)
specifier|public
name|void
name|onMessage
parameter_list|(
name|String
name|data
parameter_list|)
block|{
try|try
block|{
name|messages
operator|.
name|add
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|con
operator|.
name|sendMessage
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onMessage (byte[] data, int offset, int length)
specifier|public
name|void
name|onMessage
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|length
operator|<
name|data
operator|.
name|length
condition|)
block|{
name|byte
index|[]
name|odata
init|=
name|data
decl_stmt|;
name|data
operator|=
operator|new
name|byte
index|[
name|length
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|odata
argument_list|,
name|offset
argument_list|,
name|data
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|offset
operator|=
literal|0
expr_stmt|;
block|}
name|messages
operator|.
name|add
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|con
operator|.
name|sendMessage
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|onMessage (byte[] data, int offset, int length, boolean last)
specifier|public
name|void
name|onMessage
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
name|frameBuffer
operator|.
name|write
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|last
condition|)
block|{
name|data
operator|=
name|frameBuffer
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
name|frameBuffer
operator|.
name|reset
argument_list|()
expr_stmt|;
name|messages
operator|.
name|add
argument_list|(
name|data
argument_list|)
expr_stmt|;
try|try
block|{
name|con
operator|.
name|sendMessage
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onFrame (byte flags, byte opcode, byte[] data, int offset, int length)
specifier|public
name|boolean
name|onFrame
parameter_list|(
name|byte
name|flags
parameter_list|,
name|byte
name|opcode
parameter_list|,
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
if|if
condition|(
name|OPCODE_TEXT
operator|==
name|opcode
operator|||
name|OPCODE_BINARY
operator|==
name|opcode
condition|)
block|{
if|if
condition|(
literal|0
operator|!=
operator|(
name|FLAGS_FINAL
operator|&
name|flags
operator|)
condition|)
block|{
comment|// a non-framed text or binary
return|return
literal|false
return|;
block|}
name|onMessage
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|OPCODE_CONTINUATION
operator|==
name|opcode
condition|)
block|{
name|boolean
name|f
init|=
literal|0
operator|!=
operator|(
name|FLAGS_FINAL
operator|&
name|flags
operator|)
decl_stmt|;
name|onMessage
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|,
name|f
argument_list|)
expr_stmt|;
return|return
operator|!
name|f
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|onHandshake (FrameConnection connection)
specifier|public
name|void
name|onHandshake
parameter_list|(
name|FrameConnection
name|connection
parameter_list|)
block|{         }
block|}
block|}
end_class

end_unit

