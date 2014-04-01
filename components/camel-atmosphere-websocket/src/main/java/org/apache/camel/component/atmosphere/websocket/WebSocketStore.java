begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *   */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmosphere.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmosphere
operator|.
name|websocket
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|atmosphere
operator|.
name|websocket
operator|.
name|WebSocket
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_interface
DECL|interface|WebSocketStore
specifier|public
interface|interface
name|WebSocketStore
extends|extends
name|Service
block|{
DECL|method|addWebSocket (String connectionKey, WebSocket websocket)
name|void
name|addWebSocket
parameter_list|(
name|String
name|connectionKey
parameter_list|,
name|WebSocket
name|websocket
parameter_list|)
function_decl|;
DECL|method|removeWebSocket (String connectionKey)
name|void
name|removeWebSocket
parameter_list|(
name|String
name|connectionKey
parameter_list|)
function_decl|;
DECL|method|removeWebSocket (WebSocket websocket)
name|void
name|removeWebSocket
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
function_decl|;
DECL|method|getConnectionKey (WebSocket websocket)
name|String
name|getConnectionKey
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
function_decl|;
DECL|method|getWebSocket (String connectionKey)
name|WebSocket
name|getWebSocket
parameter_list|(
name|String
name|connectionKey
parameter_list|)
function_decl|;
DECL|method|getAllWebSockets ()
name|Collection
argument_list|<
name|WebSocket
argument_list|>
name|getAllWebSockets
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

