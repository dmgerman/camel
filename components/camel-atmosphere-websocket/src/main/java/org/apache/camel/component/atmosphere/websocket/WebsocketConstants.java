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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|WebsocketConstants
specifier|public
class|class
name|WebsocketConstants
block|{
DECL|field|CONNECTION_KEY
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION_KEY
init|=
literal|"websocket.connectionKey"
decl_stmt|;
DECL|field|SEND_TO_ALL
specifier|public
specifier|static
specifier|final
name|String
name|SEND_TO_ALL
init|=
literal|"websocket.sendToAll"
decl_stmt|;
block|}
end_class

end_unit

